package Application;

import Domain.Presentation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class PlaySoundCommand extends PresentationCommand {

    public String soundUrl;

    public PlaySoundCommand(Presentation presentation, String soundUrl) {
        super(presentation);
        this.soundUrl = soundUrl;
    }

    @Override
    public void execute() {
        try {
            URL url = getClass().getResource("/" + soundUrl.replaceFirst("^/+", "")); // ensure leading slash
            if (url == null) {
                System.err.println("Sound file not found: " + soundUrl);
                return;
            }

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
