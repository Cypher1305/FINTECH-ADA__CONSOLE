package presentation;

public class MacroCommand implements Command {
    Command[] commands;

    public MacroCommand(Command[] commands) {
        this.commands = commands;
    }

    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    public void undo() {
        // Undo en ordre inverse
        for (int i = commands.length - 1; i >= 0; i--) {
            commands[i].undo();
        }
    }
}