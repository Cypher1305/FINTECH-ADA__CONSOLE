package ci.ada.state;



import ci.ada.Interfaces.AppState;
import ci.ada.models.Admin;
import ci.ada.models.Customer;
import ci.ada.models.Merchant;
import ci.ada.services.UserService;

import java.util.List;
import java.util.Scanner;

public class ListUsersState implements AppState {
    private final UserService userService;

    public ListUsersState(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(MainContext context) {
        Scanner scanner = context.getScanner();
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
}
