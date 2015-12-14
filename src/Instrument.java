import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.sound.sampled.*;

import java.io.File;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.data.audiofile.AudioFileReader;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

/**
 * Created by dennis on 12/11/15.
 */
public class Instrument
{
    private CheckBox[] notes = new CheckBox[16];
    private TextField path = new TextField();
    private Label message = new Label("No file loaded");
    private SamplePlayer audioFile = null;
    private AudioContext ac = null;
    private Button update = new Button("Update");

    public Instrument(int amount)
    {
        update.setOnAction(e ->
        {
            try
            {
                updateAudio(path.getText());
            }
            catch (Exception ex)
            {
                message.setText("Failed to load "+path.getText());
                ex.printStackTrace();
            }
        });
        for(int i = 0; i < amount; i++)
        {
            notes[i] = new CheckBox();
        }
    }

    public void updateAudio(String path) throws Exception
    {
        File soundFile = new File(System.getProperty("user.dir") +"/src/"+path);
        try
        {
            ac = new AudioContext();
            audioFile = new SamplePlayer(ac, SampleManager.sample(soundFile.toString()));
            Gain g = new Gain(ac, 2, 0.2f);
            g.addInput(audioFile);
            ac.out.addInput(g);
            audioFile.setKillOnEnd(false);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new Exception();
        }
        message.setText(path + " loaded");
    }

    public CheckBox[] getNotes()
    {
        return notes;
    }

    public void triggerNote(int value)
    {
        if(notes[value].isSelected())
        {
            if(audioFile != null)
            {
                audioFile.setToLoopStart();
                ac.start();
            }
        }
    }

    public TextField getTextField()
    {
        return path;
    }

    public Button getButton()
    {
        return update;
    }

    public Label getLabel()
    {
        return message;
    }

}
