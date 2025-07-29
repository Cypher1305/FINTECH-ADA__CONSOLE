//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import models.*;
import services.UserService;
import database.DatabaseConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static UserService userService = new UserService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(""" 
                 ______   _   _     _   _________   ______    _______    _     _                ___       _________         ___
                |  ____| | | | \\  | | |_________| |  ____|  /  _____|  | |   | |              |   |     |  _____  \\      |   |
                | |___   | | |  \\ | |     | |     | |____   | |        | |___| |             |     |    | |      \\|     |     |
                |  ___|  | | |   `  |     | |     |  ____|  | |        |  ___| |            | |___| |   | |      | |    | |___| |
                | |      | | | |\\   |     | |     | |____   | |______  | |   | |  ______  | |     | |  | |_____/  |   | |     | |
                |_|      |_| |_| \\ _|     |_|     |______|   \\______| |_|   |_| |______| |_|       | | |_________/   |_|       |_|   """);

        try {
            // Test de connexion à la base de données
            DatabaseConnection.getInstance().getConnection();

            // Menu principal
            boolean running = true;
            while (running) {
                showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choice) {
                    case 1:
                        createAdmin();
                        break;
                    case 2:
                        createMerchant();
                        break;
                    case 3:
                        createCustomer();
                        break;
                    case 4:
                        listAllUsers();
                        break;
                    case 5:
                        authenticateUser();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Choix invalide!");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        } finally {
            // Fermer la connexion
            DatabaseConnection.getInstance().closeConnection();
            scanner.close();
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Créer un Admin");
        System.out.println("2. Créer un Marchant");
        System.out.println("3. Créer un Client");
        System.out.println("4. Lister tous les utilisateurs");
        System.out.println("5. Authentification");
        System.out.println("6. Quitter");
        System.out.print("Votre choix: ");
    }

    private static void createAdmin() {
        System.out.println("\n=== CRÉATION D'UN ADMIN ===");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Nom: ");
        String lastname = scanner.nextLine();

        System.out.print("Prénom: ");
        String firstname = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();


        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Téléphone: ");
        String phone = scanner.nextLine();

        System.out.print("Privilège: ");
        String privilege = scanner.nextLine();

        UserAccount userAccount = new UserAccount(username, password);
        Admin admin = new Admin(firstname, lastname, email, phone, userAccount);
        admin.addPrivilege(privilege);

        if (userService.createAdmin(admin)) {
            System.out.println("Admin créé avec succès!");
        } else {
            System.out.println("Erreur lors de la création de l'admin.");
        }
    }

    private static void createMerchant() {
        System.out.println("\n=== CRÉATION D'UN MARCHANT ===");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Prénom: ");
        String firstname = scanner.nextLine();

        System.out.print("Nom: ");
        String lastname = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Téléphone: ");
        String phone = scanner.nextLine();

        System.out.print("Nom du magasin: ");
        String storeName = scanner.nextLine();

        System.out.print("Adresse du magasin: ");
        String storeAddress = scanner.nextLine();

        System.out.print("Numéro fiscal: ");
        String taxNumber = scanner.nextLine();

        UserAccount userAccount = new UserAccount(username, password);
        Merchant merchant = new Merchant(firstname, lastname, email, phone, userAccount, storeName, storeAddress, taxNumber);

        if (userService.createMerchant(merchant)) {
            System.out.println("Marchant créé avec succès!");
        } else {
            System.out.println("Erreur lors de la création du marchant.");
        }
    }

    private static void createCustomer() {
        System.out.println("\n=== CRÉATION D'UN CLIENT ===");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Prénom: ");
        String firstname = scanner.nextLine();

        System.out.print("Nom: ");
        String lastname = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Téléphone: ");
        String phone = scanner.nextLine();

        System.out.print("Adresse: ");
        String address = scanner.nextLine();

        System.out.print("Date de naissance (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate dateOfBirth = null;
        try {
            dateOfBirth = LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Format de date invalide, date ignorée.");
        }

        UserAccount userAccount = new UserAccount(username, password);
        Customer customer = new Customer(firstname, lastname, email, phone, userAccount, address, dateOfBirth);

        if (userService.createCustomer(customer)) {
            System.out.println("Client créé avec succès!");
        } else {
            System.out.println("Erreur lors de la création du client.");
        }
    }

    private static void listAllUsers() {
        System.out.println("\n=== LISTE DE TOUS LES UTILISATEURS ===");

        System.out.println("\n--- ADMINS ---");
        List<Admin> admins = userService.getAllAdmins();
        for (Admin admin : admins) {
            System.out.println(admin);
        }

        System.out.println("\n--- MARCHANTS ---");
        List<Merchant> merchants = userService.getAllMerchants();
        for (Merchant merchant : merchants) {
            System.out.println(merchant);
        }

        System.out.println("\n--- CLIENTS ---");
        List<Customer> customers = userService.getAllCustomers();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private static void authenticateUser() {
        System.out.println("\n=== AUTHENTIFICATION ===");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<UserAccount> account = userService.authenticate(username, password);
        if (account.isPresent()) {
            System.out.println("Authentification réussie!");
            System.out.println("Compte: " + account.get());
        } else {
            System.out.println("Échec de l'authentification. Username ou password incorrect.");
        }
    }
}
