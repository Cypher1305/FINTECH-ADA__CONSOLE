package presentation;

public class RemoteLoader {
    public static void main(String[] args) {
        RemoteControl remoteControl = new RemoteControl();

        // Création des appareils (Receivers)
        Light livingRoomLight = new Light("Salon");
        Light kitchenLight = new Light("Cuisine");
        Stereo stereo = new Stereo("Salon");

        // Création des commandes
        LightOnCommand livingRoomLightOn = new LightOnCommand(livingRoomLight);
        LightOffCommand livingRoomLightOff = new LightOffCommand(livingRoomLight);
        LightOnCommand kitchenLightOn = new LightOnCommand(kitchenLight);
        LightOffCommand kitchenLightOff = new LightOffCommand(kitchenLight);

        StereoOnWithVolumeCommand stereoOnWithVolume = new StereoOnWithVolumeCommand(stereo);

        // Configuration de la télécommande
        remoteControl.setCommand(0, livingRoomLightOn, livingRoomLightOff);
        remoteControl.setCommand(1, kitchenLightOn, kitchenLightOff);
        remoteControl.setCommand(2, stereoOnWithVolume, new NoCommand());

        // Création d'une macro pour tout allumer
        Command[] partyOn = {livingRoomLightOn, stereoOnWithVolume};
        Command[] partyOff = {livingRoomLightOff, new NoCommand()};
        MacroCommand partyOnMacro = new MacroCommand(partyOn);
        MacroCommand partyOffMacro = new MacroCommand(partyOff);

        remoteControl.setCommand(3, partyOnMacro, partyOffMacro);

        System.out.println(remoteControl);

        // Test des commandes
        System.out.println("=== Test des commandes ===");
        remoteControl.onButtonPressed(0);
        remoteControl.offButtonPressed(0);
        remoteControl.onButtonPressed(1);
        remoteControl.undoButtonPressed();

        System.out.println("\n=== Test de la macro ===");
        remoteControl.onButtonPressed(3);
        System.out.println("Appui sur undo:");
        remoteControl.undoButtonPressed();
    }
}
