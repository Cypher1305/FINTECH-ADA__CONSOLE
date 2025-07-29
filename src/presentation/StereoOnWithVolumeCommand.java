package presentation;

public class StereoOnWithVolumeCommand implements Command {
    Stereo stereo;
    int previousVolume;

    public StereoOnWithVolumeCommand(Stereo stereo) {
        this.stereo = stereo;
    }

    public void execute() {
        stereo.on();
        stereo.setVolume(11);
    }

    public void undo() {
        stereo.off();
    }
}