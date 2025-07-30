package utils;
import Interfaces.AppState;

import java.util.Scanner;
public class MainContext {

        private AppState currentState;
        private final Scanner scanner;

        public MainContext() {
            this.scanner = new Scanner(System.in);
        }

        public void setState(AppState state) {
            this.currentState = state;
        }

        public Scanner getScanner() {
            return scanner;
        }

        public void start() {
            while (currentState != null) {
                currentState.handle(this);
            }
        }

        public void exit() {
            setState(null); // quitter
        }

}
