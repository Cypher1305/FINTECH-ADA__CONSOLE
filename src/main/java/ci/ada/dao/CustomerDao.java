package ci.ada.dao;


import ci.ada.Interfaces.MatriculeStrategy;
import ci.ada.database.DatabaseConnection;
import ci.ada.enums.UserType;
import ci.ada.models.Customer;
import ci.ada.models.UserAccount;
import ci.ada.utils.MatriculeGeneratorContext;
import ci.ada.utils.MatriculeUtil;
import ci.ada.utils.strategy.AdminMatriculeStrategy;
import ci.ada.utils.strategy.CustomerMatriculeStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDao {
    private Connection connection;
    private UserAccountDao userAccountDao;

    public CustomerDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.userAccountDao = new UserAccountDao();
    }

    public boolean create(Customer customer) {
        try {
            connection.setAutoCommit(false);

            // 1. Créer le compte utilisateur
            if (!userAccountDao.create(customer.getUserAccount())) {
                connection.rollback();
                return false;
            }

            // 2. Créer l'utilisateur
            String userSql = "INSERT INTO users (firstname, lastname, email, phone_number, user_account_id, user_type, matricule) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, customer.getFirstname());
                pstmt.setString(2, customer.getLastname());
                pstmt.setString(3, customer.getEmail());
                pstmt.setString(4, customer.getPhoneNumber());
                pstmt.setLong(5, customer.getUserAccount().getId());
                pstmt.setString(6, UserType.CUSTOMER.name());

                MatriculeGeneratorContext context = new MatriculeGeneratorContext();
                context.setStrategy(new CustomerMatriculeStrategy());
                String matricule = context.generate(customer.getFirstname(), customer.getLastname());
                pstmt.setString(7, matricule);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            customer.setId(generatedKeys.getLong(1));
                        }
                    }
                } else {
                    connection.rollback();
                    return false;
                }
            }

            // 3. Créer les informations client
            String customerSql = "INSERT INTO customer_info (user_id, address, date_of_birth, loyalty_points) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(customerSql)) {
                pstmt.setLong(1, customer.getId());
                pstmt.setString(2, customer.getAddress());
                pstmt.setDate(3, customer.getDateOfBirth() != null ? Date.valueOf(customer.getDateOfBirth()) : null);
                pstmt.setInt(4, customer.getLoyaltyPoints());
                pstmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Erreur lors de la création du client: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la restauration de l'autocommit: " + e.getMessage());
            }
        }
        return false;
    }

    public Optional<Customer> findById(int id) {
        String sql = """
            SELECT u.*, ua.username, ua.password, ua.is_active, ua.created_at, ua.updated_at,
                   ci.address, ci.date_of_birth, ci.loyalty_points
            FROM users u 
            JOIN user_accounts ua ON u.user_account_id = ua.id 
            JOIN customer_info ci ON u.id = ci.user_id
            WHERE u.id = ? AND u.user_type = 'CUSTOMER'
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client par ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = """
            SELECT u.*, ua.username, ua.password, ua.is_active, ua.created_at, ua.updated_at,
                   ci.address, ci.date_of_birth, ci.loyalty_points
            FROM users u 
            JOIN user_accounts ua ON u.user_account_id = ua.id 
            JOIN customer_info ci ON u.id = ci.user_id
            WHERE u.user_type = 'CUSTOMER'
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les clients: " + e.getMessage());
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setFirstname(rs.getString("firstname"));
        customer.setLastname(rs.getString("lastname"));
        customer.setEmail(rs.getString("email"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setAddress(rs.getString("address"));
        customer.setLoyaltyPoints(rs.getInt("loyalty_points"));

        Date dateOfBirth = rs.getDate("date_of_birth");
        if (dateOfBirth != null) {
            customer.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        // Mapper le compte utilisateur
        UserAccount userAccount = new UserAccount();
        userAccount.setId(rs.getLong("user_account_id"));
        userAccount.setUsername(rs.getString("username"));
        userAccount.setPassword(rs.getString("password"));
        userAccount.setActive(rs.getBoolean("is_active"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            userAccount.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            userAccount.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        customer.setUserAccount(userAccount);
        return customer;
    }

    public boolean update(Customer customer) {
        try {
            connection.setAutoCommit(false);

            // Mettre à jour le compte utilisateur
            if (!userAccountDao.update(customer.getUserAccount())) {
                connection.rollback();
                return false;
            }

            // Mettre à jour les informations utilisateur
            String userSql = "UPDATE users SET firstname = ?, lastname = ?, email = ?, phone_number = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(userSql)) {
                pstmt.setString(1, customer.getFirstname());
                pstmt.setString(2, customer.getLastname());
                pstmt.setString(3, customer.getEmail());
                pstmt.setString(4, customer.getPhoneNumber());
                pstmt.setLong(5, customer.getId());

                if (pstmt.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Mettre à jour les informations client
            String customerSql = "UPDATE customer_info SET address = ?, date_of_birth = ?, loyalty_points = ? WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(customerSql)) {
                pstmt.setString(1, customer.getAddress());
                pstmt.setDate(2, customer.getDateOfBirth() != null ? Date.valueOf(customer.getDateOfBirth()) : null);
                pstmt.setInt(3, customer.getLoyaltyPoints());
                pstmt.setLong(4, customer.getId());

                if (pstmt.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Erreur lors de la mise à jour du client: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la restauration de l'autocommit: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ? AND user_type = 'CUSTOMER'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du client: " + e.getMessage());
        }
        return false;
    }

    public List<Customer> findByLoyaltyPointsRange(int minPoints, int maxPoints) {
        List<Customer> customers = new ArrayList<>();
        String sql = """
            SELECT u.*, ua.username, ua.password, ua.is_active, ua.created_at, ua.updated_at,
                   ci.address, ci.date_of_birth, ci.loyalty_points
            FROM users u 
            JOIN user_accounts ua ON u.user_account_id = ua.id 
            JOIN customer_info ci ON u.id = ci.user_id
            WHERE u.user_type = 'CUSTOMER' AND ci.loyalty_points BETWEEN ? AND ?
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, minPoints);
            pstmt.setInt(2, maxPoints);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par points de fidélité: " + e.getMessage());
        }
        return customers;
    }
}