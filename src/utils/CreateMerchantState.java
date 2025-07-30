package utils;

import Interfaces.AppState;
import models.Merchant;
import models.UserAccount;
import services.UserService;

import java.util.Scanner;

public class CreateMerchantState implements AppState {
    private final UserService userService;

    public CreateMerchantState(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(MainContext context) {
        Scanner scanner = context.getScanner();
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
}
