//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import Interfaces.AppState;
import models.*;
import services.UserService;
import database.DatabaseConnection;
import utils.MainContext;
import utils.MainMenuState;

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
            DatabaseConnection.getInstance().getConnection();
            MainContext context = new MainContext();
            context.setState((AppState) new MainMenuState());
            context.start();
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
    }

}
