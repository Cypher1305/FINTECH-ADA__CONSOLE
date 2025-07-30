# Fintech_ada (Java Console Application)

**Fintech_ada** is a console-based Java application designed to simulate a simple fintech ecosystem. It was developed as part of a revision project to reinforce core Java concepts, MySQL database integration, and essential design patterns.

---

## Features

-  Admin, Customer, and Merchant account management
-  Wallet and transaction operations
-  Authentication & User Roles
  
-  Layered architecture (DAO, Service, Model)  
-  Design Patterns used:
      - **State**: to manage user session states
      - **Strategy**: to generate user matricules based on type
      - **Factory**: to create users based on roles
      - **Singleton**: for database connection
      - **Chain of Responsibility**: for request validation flow
-  MySQL database integration (via JDBC)

---

## Technologies

- Java (Native)
- MySQL
- DBeaver
- IntelliJ IDEA Community Edition

---

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/<your-username>/fintech_ada.git
cd fintech_ada
```

2. Set up your MySQL database:

    - Create the database and required tables using the provided SQL script (fintech_ada.sql).

    - Update your database credentials in DBConnectionManager.java:

    ```bash
    private static final String URL = "jdbc:mysql://localhost:3306/fintech_ada";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";
    ```

3. Run the project:

    - From your IDE (IntelliJ/NetBeans) → Run Main.java
      
    Or use:

   ```bash
   javac Main.java
   java Main
   ```
---


_XoXo_ 😘❤️❤️❤️
