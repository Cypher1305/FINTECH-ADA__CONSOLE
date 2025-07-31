package ci.ada;



import ci.ada.Interfaces.AppState;
import ci.ada.database.DatabaseConnection;
import ci.ada.state.MainContext;
import ci.ada.state.MainMenuState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
public class App
{
    public static void main( String[] args )
    {
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