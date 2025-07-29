package presentation;

// Receiver pour les lumières
public class Light {
    private String location;
    private boolean isOn;

    public Light(String location) {
        this.location = location;
        this.isOn = false;
    }

    public void on() {
        isOn = true;
        System.out.println(location + " lumière allumée");
    }

    public void off() {
        isOn = false;
        System.out.println(location + " lumière éteinte");
    }

    public boolean isOn() {
        return isOn;
    }
}

// Receiver pour la chaîne stéréo
