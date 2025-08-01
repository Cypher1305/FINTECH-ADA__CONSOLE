# Fintech_ada (Java Console Application)

**Fintech_ada** is a console-based Java application designed to simulate a simple fintech ecosystem. It was developed as part of a revision project to reinforce core Java concepts, MySQL database integration, and essential design patterns.

---

## Features

-  Admin, Customer, and Merchant account management
-  Wallet and transaction operations
-  Authentication & User Roles
  
    ### In `main` branch:
  
  - Java Project (Native Java)
  - Layered architecture (DAO, Service, Model)

    
     ### In `master` branch:

   - Create a maven project (`maven-archetype-simple`)
   - Import our Java console project
   - Apply Design Pattern to existing project
    
   -  Design Patterns used:
      - **Singleton**: to ensure a single instance of the database connection throughout the application, avoiding multiple unnecessary connections.
      - **State**: to manage user session behavior depending on their states.
      - **Facade**: to provide a simplified interface to coordinate multiple services so the main application logic remains clean and easy to use.
      - **Strategy**: to implements dynamic generation of user matricules based on user types. Makes it easy to extend matricule logic for new user roles.
      - **Adaptor**: to allow the system to register users (especially customers) from external spreadsheet files by converting spreadsheet data formats into the application's internal Customer objects.
      - **Prototype**: to enable cloning of existing objects without depending on their specific classes (especially in our case: Merchant class). This is useful for quickly creating new users or sessions based on a predefined model.
   -  MySQL database integration (via JDBC)

---

## Tech & Tools

- Java
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

    - From your IDE (IntelliJ/NetBeans) → Run Main.java (on main) or App.java (on master)
      
    Or use:

   ```bash
   //On main (!in the good repository)
   
   javac Main.java
   java Main

   //On master (!in the good repository)

   mvn compile
   mvn exec:java
   ```
---


_XoXo_ 😘❤️❤️❤️
