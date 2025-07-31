package ci.ada.config;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/fintech_ada";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "monpasswordsecret"; // Ajustez selon votre configuration
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    //Pool de connexions (optionnel);
    //public static final int MAX_CONNECTIONS = 10;
    //public static final int INITIAL_CONNECTIONS = 5;
}