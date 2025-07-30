package dao;

import database.DatabaseConnection;
import enums.UserType;
import models.Admin;
import models.UserAccount;
import utils.GenerateMatricule;

import java.sql.*;
import java.util.*;

public class AdminDao { ;
    private Connection connection;
    private UserAccountDao userAccountDao;

    public AdminDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.userAccountDao = new UserAccountDao();
    }

    public boolean create(Admin admin) {
        try {
            connection.setAutoCommit(false);

            // 1. Créer le compte utilisateur
            if (!userAccountDao.create(admin.getUserAccount())) {
                connection.rollback();
                return false;
            }

            // 2. Créer l'utilisateur
            String userSql = "INSERT INTO users (firstname, lastname, email, phone_number, user_account_id, user_type, matricule) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, admin.getFirstname());
                pstmt.setString(2, admin.getLastname());
                pstmt.setString(3, admin.getEmail());
                pstmt.setString(4, admin.getPhoneNumber());
                pstmt.setInt(5, admin.getUserAccount().getId());
                pstmt.setString(6, UserType.ADMIN.name());
                pstmt.setString(7, GenerateMatricule.generateMatricule(admin.getFirstname(), admin.getLastname()));

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            admin.setId(generatedKeys.getInt(1));
                        }
                    }
                } else {
                    connection.rollback();
                    return false;
                }
            }

            // 3. Créer les privilèges
            if (!admin.getPrivileges().isEmpty()) {
                String privilegeSql = "INSERT INTO admin_privileges (user_id, privilege_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(privilegeSql)) {
                    for (String privilege : admin.getPrivileges()) {
                        pstmt.setInt(1, admin.getId());
                        pstmt.setString(2, privilege);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
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
            System.err.println("Erreur lors de la création de l'admin: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la restauration de l'autocommit: " + e.getMessage());
            }
        }
        return false;
    }

    public Optional<Admin> findById(int id) {
        String sql = """
            SELECT u.*, ua.username, ua.password, ua.is_active, ua.created_at, ua.updated_at
            FROM users u 
            JOIN user_accounts ua ON u.user_account_id = ua.id 
            WHERE u.id = ? AND u.user_type = 'ADMIN'
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = mapResultSetToAdmin(rs);
                    loadPrivileges(admin);
                    return Optional.of(admin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'admin par ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Admin> findAll() {
        List<Admin> admins = new ArrayList<>();
        String sql = """
            SELECT u.*, ua.username, ua.password, ua.is_active, ua.created_at, ua.updated_at
            FROM users u 
            JOIN user_accounts ua ON u.user_account_id = ua.id 
            WHERE u.user_type = 'ADMIN'
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Admin admin = mapResultSetToAdmin(rs);
                loadPrivileges(admin);
                admins.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les admins: " + e.getMessage());
        }
        return admins;
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getInt("id"));
        admin.setFirstname(rs.getString("firstname"));
        admin.setLastname(rs.getString("lastname"));
        admin.setEmail(rs.getString("email"));
        admin.setPhoneNumber(rs.getString("phone_number"));

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

        admin.setUserAccount(userAccount);
        return admin;
    }

    private void loadPrivileges(Admin admin) {
        String sql = "SELECT privilege_name FROM admin_privileges WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, admin.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> privileges = new ArrayList<>();
                while (rs.next()) {
                    privileges.add(rs.getString("privilege_name"));
                }
                admin.setPrivileges(privileges);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des privilèges: " + e.getMessage());
        }
    }

    public boolean update(Admin admin) {
        try {
            connection.setAutoCommit(false);

            // Mettre à jour le compte utilisateur
            if (!userAccountDao.update(admin.getUserAccount())) {
                connection.rollback();
                return false;
            }

            // Mettre à jour les informations utilisateur
            String userSql = "UPDATE users SET firstname = ?, lastname = ?, email = ?, phone_number = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(userSql)) {
                pstmt.setString(1, admin.getFirstname());
                pstmt.setString(2, admin.getLastname());
                pstmt.setString(3, admin.getEmail());
                pstmt.setString(4, admin.getPhoneNumber());
                pstmt.setInt(5, admin.getId());

                if (pstmt.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Supprimer les anciens privilèges
            String deletePrivilegesSql = "DELETE FROM admin_privileges WHERE user_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deletePrivilegesSql)) {
                pstmt.setInt(1, admin.getId());
                pstmt.executeUpdate();
            }

            // Ajouter les nouveaux privilèges
            if (!admin.getPrivileges().isEmpty()) {
                String insertPrivilegeSql = "INSERT INTO admin_privileges (user_id, privilege_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertPrivilegeSql)) {
                    for (String privilege : admin.getPrivileges()) {
                        pstmt.setInt(1, admin.getId());
                        pstmt.setString(2, privilege);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
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
            System.err.println("Erreur lors de la mise à jour de l'admin: " + e.getMessage());
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
        String sql = "DELETE FROM users WHERE id = ? AND user_type = 'ADMIN'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'admin: " + e.getMessage());
        }
        return false;
    }
}
