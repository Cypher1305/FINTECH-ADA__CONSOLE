package utils;

import Interfaces.AppState;
import services.UserService;

import java.util.Scanner;

public class MainMenuState implements AppState {
        private final UserService userService = new UserService();

        @Override
        public void handle(MainContext context) {
            Scanner scanner = context.getScanner();

            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Créer un Admin");
            System.out.println("2. Créer un Marchant");
            System.out.println("3. Créer un Client");
            System.out.println("4. Lister tous les utilisateurs");
            System.out.println("5. Authentification");
            System.out.println("6. Quitter");
            System.out.print("Votre choix: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consommer la ligne

            switch (choice) {
                case 1 -> context.setState(new CreateAdminState(userService));
                case 2 -> context.setState(new CreateMerchantState(userService));
                case 3 -> context.setState(new CreateCustomerState(userService));
                case 4 -> context.setState(new ListUsersState(userService));
                case 5 -> context.setState(new AuthenticationState(userService));
                case 6 -> context.exit();
                default -> System.out.println("Choix invalide!");
            }
        }
}
