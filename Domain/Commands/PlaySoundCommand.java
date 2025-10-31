package Domain.Commands;

import Domain.Entities.Presentation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class PlaySoundCommand extends PresentationCommand {

    public String soundUrl;

    /**
     * Ctor, zet ook de url van het geluid dat moet afgespeeld worden
     */
    public PlaySoundCommand(Presentation presentation, String soundUrl) {
        super(presentation);
        this.soundUrl = soundUrl;
    }

    /**
     * Voer het PlaySound Command uit
     */
    @Override
    public void execute() {
        try {
            //Haal de url op uit de files
            URL url = getClass().getResource("/" + soundUrl.replaceFirst("^/+", "")); // ensure leading slash
            if (url == null) {
                System.err.println("Sound file not found: " + soundUrl);
                return;
            }
            //Maak een InputStream aan en speel de clip af
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
