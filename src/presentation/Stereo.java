package presentation;

public class Stereo {
    private String location;
    private boolean isOn;
    private int volume;

    public Stereo(String location) {
        this.location = location;
        this.isOn = false;
        this.volume = 0;
    }

    public void on() {
        isOn = true;
        System.out.println(location + " stéréo allumée");
    }

    public void off() {
        isOn = false;
        System.out.println(location + " stéréo éteinte");
    }

    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println(location + " volume réglé à " + volume);
    }
}
