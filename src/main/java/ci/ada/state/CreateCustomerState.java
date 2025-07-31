package ci.ada.state;



import ci.ada.Interfaces.AppState;
import ci.ada.models.Customer;
import ci.ada.models.UserAccount;
import ci.ada.services.UserService;

import java.time.LocalDate;
import java.util.Scanner;

public class CreateCustomerState implements AppState {
    private final UserService userService;

    public CreateCustomerState(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(MainContext context) {
        Scanner scanner = context.getScanner();
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
}