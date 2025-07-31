package ci.ada.dao;


import ci.ada.database.DatabaseConnection;
import ci.ada.models.UserAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserAccountDao {
    private Connection connection;

    public UserAccountDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean create(UserAccount userAccount) {
        String sql = "INSERT INTO user_accounts (username, password, is_active) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, userAccount.getUsername());
            pstmt.setString(2, userAccount.getPassword());
            pstmt.setBoolean(3, userAccount.isActive());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userAccount.setId(generatedKeys.getLong(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du compte utilisateur: " + e.getMessage());
        }
        return false;
    }

    public Optional<UserAccount> findById(int id) {
        String sql = "SELECT * FROM user_accounts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUserAccount(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<UserAccount> findByUsername(String username) {
        String sql = "SELECT * FROM user_accounts WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUserAccount(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par username: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<UserAccount> findAll() {
        List<UserAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM user_accounts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(mapResultSetToUserAccount(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les comptes: " + e.getMessage());
        }
        return accounts;
    }

    public boolean update(UserAccount userAccount) {
        String sql = "UPDATE user_accounts SET username = ?, password = ?, is_active = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userAccount.getUsername());
            pstmt.setString(2, userAccount.getPassword());
            pstmt.setBoolean(3, userAccount.isActive());
            pstmt.setLong(4, userAccount.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM user_accounts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
        }
        return false;
    }

    private UserAccount mapResultSetToUserAccount(ResultSet rs) throws SQLException {
        UserAccount account = new UserAccount();
        account.setId(rs.getLong("id"));
        account.setUsername(rs.getString("username"));
        account.setPassword(rs.getString("password"));
        account.setActive(rs.getBoolean("is_active"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            account.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            account.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return account;
    }
}