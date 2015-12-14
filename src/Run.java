/**
 * Created by dennis on 12/11/15.
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Run extends Application
{
    private int value = 0;
    private Pane p = null;

    public static void main(String[] args)
    {
        launch(args);
    }



    public void start(Stage window)
    {
        window.setTitle("Incredibly flexible and useful audio sequencer with horrible audio support");
        p = new Pane();
        value = 16;

        Scene scene = new Scene(p, 800, 480);

        window.setScene(scene);
        window.setResizable(false);
        window.show();

        Sequencer seq = new Sequencer(value, p);
    }
}
