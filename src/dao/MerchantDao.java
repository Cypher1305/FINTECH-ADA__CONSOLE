package dao;


import database.DatabaseConnection;
import enums.UserType;
import models.Merchant;
import models.UserAccount;
import utils.GenerateMatricule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MerchantDao {
    private Connection connection;
    private UserAccountDao userAccountDao;

    public MerchantDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.userAccountDao = new UserAccountDao();
    }

    public boolean create(Merchant merchant) {
        try {
            connection.setAutoCommit(false);

            // 1. Créer le compte utilisateur
            if (!userAccountDao.create(merchant.getUserAccount())) {
                connection.rollback();
                return false;
            }

            // 2. Créer l'utilisateur
            String userSql = "INSERT INTO users (firstname, lastname, email, phone_number, user_account_id, user_type, matricule) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, merchant.getFirstname());
                pstmt.setString(2, merchant.getLastname());
                pstmt.setString(3, merchant.getEmail());
                pstmt.setString(4, merchant.getPhoneNumber());
                pstmt.setInt(5, merchant.getUserAccount().getId());
                pstmt.setString(6, UserType.MERCHANT.name());
                pstmt.setString(7, GenerateMatricule.generateMatricule(merchant.getFirstname(), merchant.getLastname()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            merchant.setId(generatedKeys.getInt(1));
                        }
                    }
                } else {
                    connection.rollback();
                    return false;
                }
            }

            // 3. Créer les informations marchant
            String merchantSql = "INSERT INTO merchant_info (user_id, store_name, store_address, tax_number) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(merchantSql)) {
                pstmt.setInt(1, merchant.getId());
                pstmt.setString(2, merchant.getStoreName());
                pstmt.setString(3, merchant.getStoreAddress());
                pstmt.setString(4, merchant.getTaxNumber());
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
            System.err.println("Erreur lors de la création du marchant: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la restauration de l'autocommit: " + e.getMessage());
            }
        }
        return false;
    }

    public Optional<Merchant> findById(int id) {
        String sql = """
            SELECT u.*, ua.username, ua.password, ua.is_active, ua.created_at, ua.updated_at,
                   mi.store_name, mi.store_address, mi.tax_number
            FROM users u 
            JOIN user_accounts ua ON u.user_account_id = ua.id 
            JOIN merchant_info mi ON u.id = mi.user_id
            WHERE u.id = ? AND u.user_type = 'MERCHANT'
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMerchant(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du marchant par ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Merchant> findAll() {
        List<Merchant> merchants = new ArrayList<>();
        String sql = """
            SELECT u.*, ua.username, ua.password, ua.is_active, ua.created_at, ua.updated_at,
                   mi.store_name, mi.store_address, mi.tax_number
            FROM users u 
            JOIN user_accounts ua ON u.user_account_id = ua.id 
            JOIN merchant_info mi ON u.id = mi.user_id
            WHERE u.user_type = 'MERCHANT'
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                merchants.add(mapResultSetToMerchant(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les marchants: " + e.getMessage());
        }
        return merchants;
    }

    private Merchant mapResultSetToMerchant(ResultSet rs) throws SQLException {
        Merchant merchant = new Merchant();
        merchant.setId(rs.getInt("id"));
        merchant.setFirstname(rs.getString("firstname"));
        merchant.setLastname(rs.getString("lastname"));
        merchant.setEmail(rs.getString("email"));
        merchant.setPhoneNumber(rs.getString("phone_number"));
        merchant.setStoreName(rs.getString("store_name"));
        merchant.setStoreAddress(rs.getString("store_address"));
        merchant.setTaxNumber(rs.getString("tax_number"));

        // Mapper le compte utilisateur
        UserAccount userAccount = new UserAccount();
        userAccount.setId(rs.getInt("user_account_id"));
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

        merchant.setUserAccount(userAccount);
        return merchant;
    }

    public boolean update(Merchant merchant) {
        try {
            connection.setAutoCommit(false);

            // Mettre à jour le compte utilisateur
            if (!userAccountDao.update(merchant.getUserAccount())) {
                connection.rollback();
                return false;
            }

            // Mettre à jour les informations utilisateur
            String userSql = "UPDATE users SET firstname = ?, lastname = ?, email = ?, phone_number = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(userSql)) {
                pstmt.setString(1, merchant.getFirstname());
                pstmt.setString(2, merchant.getLastname());
                pstmt.setString(3, merchant.getEmail());
                pstmt.setString(4, merchant.getPhoneNumber());
                pstmt.setInt(5, merchant.getId());

                if (pstmt.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Mettre à jour les informations marchant
            String merchantSql = "UPDATE merchant_info SET store_name = ?, store_address = ?, tax_number = ? WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(merchantSql)) {
                pstmt.setString(1, merchant.getStoreName());
                pstmt.setString(2, merchant.getStoreAddress());
                pstmt.setString(3, merchant.getTaxNumber());
                pstmt.setInt(4, merchant.getId());

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
            System.err.println("Erreur lors de la mise à jour du marchant: " + e.getMessage());
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
        String sql = "DELETE FROM users WHERE id = ? AND user_type = 'MERCHANT'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du marchant: " + e.getMessage());
        }
        return false;
    }
}