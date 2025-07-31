package ci.ada.state;


import ci.ada.Interfaces.AppState;
import ci.ada.models.Admin;
import ci.ada.models.UserAccount;
import ci.ada.services.UserService;

import java.util.Scanner;

public class CreateAdminState implements AppState {
    private final UserService userService;

    public CreateAdminState(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(MainContext context) {
        Scanner scanner = context.getScanner();

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
            System.out.println("✅ Admin créé avec succès!");
        } else {
            System.out.println("❌ Erreur lors de la création de l'admin.");
        }

        context.setState(new MainMenuState()); // retour au menu
    }
}
