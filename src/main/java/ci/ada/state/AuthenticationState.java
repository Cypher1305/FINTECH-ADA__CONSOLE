package ci.ada.state;


import ci.ada.Interfaces.AppState;
import ci.ada.models.UserAccount;
import ci.ada.services.UserService;

import java.util.Optional;
import java.util.Scanner;

public class AuthenticationState implements AppState {
    private final UserService userService;

    public AuthenticationState(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void handle(MainContext context) {
        Scanner scanner = context.getScanner();
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
