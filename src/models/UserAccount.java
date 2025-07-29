package models;

import enums.UserType;

import java.time.LocalDateTime;

public class UserAccount{
    private int id;
    private String username;
    private String password;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserAccount() {}

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.isActive = true;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }

    public UserType UserType() {
        if (this.isActive) {
            return UserType.ADMIN;
        } else {
            return UserType.CUSTOMER;
        }
    }


    public String getFirstName() {
        return username.split("@")[0];
    }

    public String getLastName() {
        return username.split("@")[1];
    }
}