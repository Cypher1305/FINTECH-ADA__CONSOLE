

-- Table des comptes utilisateurs
CREATE TABLE user_accounts (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               username VARCHAR(50) UNIQUE NOT NULL,
                               password VARCHAR(255) NOT NULL,
                               is_active BOOLEAN DEFAULT TRUE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table des utilisateurs (informations de base)
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       firstname VARCHAR(100) NOT NULL,
                       lastname VARCHAR(100) NOT NULL,
                       email VARCHAR(150) UNIQUE NOT NULL,
                       phone_number VARCHAR(20),
                       user_account_id INT,
                       user_type ENUM('ADMIN', 'MERCHANT', 'CUSTOMER') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (user_account_id) REFERENCES user_accounts(id) ON DELETE CASCADE
);

-- Table des privilèges administrateurs
CREATE TABLE admin_privileges (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  user_id INT,
                                  privilege_name VARCHAR(100) NOT NULL,
                                  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table des informations marchands
CREATE TABLE merchant_info (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id INT,
                               store_name VARCHAR(200) NOT NULL,
                               store_address TEXT,
                               tax_number VARCHAR(50),
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table des informations clients
CREATE TABLE customer_info (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id INT,
                               address TEXT,
                               date_of_birth DATE,
                               loyalty_points INT DEFAULT 0,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);