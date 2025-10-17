package Application;

import Domain.Presentation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlaySoundCommand extends PresentationCommand {

    public String soundUrl;

    public PlaySoundCommand(Presentation presentation, String soundUrl) {
        super(presentation);
    }

    @Override
    public void execute() {
        //Moet getest worden, niet zeker of dit werkt - Tibo
        //try {
            //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(soundUrl));
            //Clip clip = AudioSystem.getClip();
            //clip.open(audioInputStream);
            //clip.start();
            //clip.stop();
        //} catch (Exception ex) {
            //ex.printStackTrace();
        //}
    }
}
