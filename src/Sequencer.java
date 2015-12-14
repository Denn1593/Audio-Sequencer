import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by dennis on 12/11/15.
 */
public class Sequencer implements Runnable
{
    private int value = 0;
    private long delay = 15000 / 100;
    private Button addButton = new Button("Add instrument");
    private Button play = new Button("Play");
    private Button stop = new Button("stop");
    private Button setTempo = new Button("Set BPM");
    private TextField tempo = new TextField("100");
    private Pane p = null;
    private Thread t = null;
    private Label bpm = new Label("100 BPM");
    private ArrayList<Instrument> instruments = new ArrayList<Instrument>();
    private WritableImage marker = new WritableImage(10, 10);
    private ImageView markerView = new ImageView();

    public Sequencer(int value, Pane p)
    {
        this.value = value;
        this.p = p;
        this.p.getChildren().addAll(addButton, play, stop, tempo, setTempo, markerView, bpm);
        addButton.setLayoutX(10);
        addButton.setLayoutY(10);
        play.setLayoutX(200);
        play.setLayoutY(10);
        tempo.setLayoutX(300);
        tempo.setLayoutY(10);
        tempo.setPrefWidth(45);
        stop.setLayoutX(250);
        stop.setLayoutY(10);
        bpm.setLayoutX(435);
        bpm.setLayoutY(15);
        addButton.setOnAction((e) -> addInstrument());
        play.setOnAction((ee) -> playLoop());
        stop.setOnAction((eee) -> stopLoop());
        stop.setDisable(true);
        setTempo.setOnAction(eee -> setTempoValue(Integer.parseInt(tempo.getText())));
        setTempo.setLayoutX(355);
        setTempo.setLayoutY(10);
        PixelWriter pi = marker.getPixelWriter();
        for(int x = 0; x < 10; x++)
        {
            for(int y = 0; y < 10; y++)
            {
                pi.setColor(x, y, Color.color(1, 0, 0));
            }
        }
        markerView.setImage(marker);
        markerView.setLayoutY(45);
        markerView.setLayoutX(13);
    }

    public void setTempoValue(long value)
    {
        this.delay = 15000 / value;
        bpm.setText(value +" BPM");
    }

    public void playLoop()
    {
        play.setDisable(true);
        stop.setDisable(false);
        t = new Thread(this);
        t.start();
    }

    public void stopLoop()
    {
        play.setDisable(false);
        stop.setDisable(true);
        t.interrupt();
    }

    public void addInstrument()
    {
        Instrument instrument = new Instrument(value);
        instruments.add(instrument);
        for (int i = 0; i < value; i++)
        {
            p.getChildren().add(instrument.getNotes()[i]);
            instrument.getNotes()[i].setLayoutX(10 + i * 20);
            instrument.getNotes()[i].setLayoutY(33 + instruments.size() * 30);
        }
        p.getChildren().add(instrument.getTextField());
        instrument.getTextField().setLayoutY(30 + instruments.size() * 30);
        instrument.getTextField().setLayoutX(30 + value * 20);
        instrument.getTextField().setPrefWidth(100);
        p.getChildren().add(instrument.getButton());
        instrument.getButton().setLayoutY(30 + instruments.size() * 30);
        instrument.getButton().setLayoutX(30 + value * 20 + 100 + 10);
        instrument.getButton().setPrefWidth(90);
        p.getChildren().add(instrument.getLabel());
        instrument.getLabel().setLayoutX(30 + value * 20 + 100 + 10 + 100);
        instrument.getLabel().setLayoutY(35 + instruments.size() * 30);
    }

    public void run()
    {
        int index = -1;
        while(true)
        {
            if(index < value - 1)
            {
                index++;
            }
            else
            {
                index = 0;
            }
            markerView.setLayoutX(13 + index * 20);
            for(int i = 0; i < instruments.size(); i++)
            {
                instruments.get(i).triggerNote(index);
            }
            try
            {
                Thread.sleep(delay);
            }
            catch(InterruptedException I)
            {
                System.out.println("Stop");
                break;
            }
        }
    }
}
