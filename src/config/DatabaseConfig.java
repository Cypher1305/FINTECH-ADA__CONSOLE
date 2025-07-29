package config;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:8080/ada";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "guidy"; // Ajustez selon votre configuration
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Pool de connexions (optionnel)
    public static final int MAX_CONNECTIONS = 10;
    public static final int INITIAL_CONNECTIONS = 5;
}