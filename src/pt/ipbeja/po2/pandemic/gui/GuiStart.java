package pt.ipbeja.po2.pandemic.gui;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

//instanciar a classe contagios e colocar a board na scene.

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiStart extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        ContagiousBoard board = new ContagiousBoard();
        Scene scene = new Scene(board);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Moving Rectangle");
        primaryStage.setOnCloseRequest((e) -> {
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(String args) {
        Application.launch(args);
    }
}
