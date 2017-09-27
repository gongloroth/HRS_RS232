/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serial;


import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author BG
 */
public class SerialWithGUI extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        Serial sr = new Serial();
        
        Serial.SerialWriter wr = new Serial.SerialWriter(sr.getOutputStream());
                
        Button btn1 = new Button();
        btn1.setText("Send Message");
        btn1.setOnAction(e -> 
        {
            wr.writeMSG();
        }
        );
        
        Button btn2 = new Button();
        btn2.setText("Connect");
        btn2.setOnAction(e ->
        {try {
            (new Serial()).connect("COM4");
        }
        catch (Exception err){
            err.printStackTrace();
        }
        }
        );
        
        /*btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        */
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0,10,0,10));
        
        grid.add(btn1, 0, 0);
        grid.add(btn2, 0, 1);
        
        Scene scene = new Scene(grid, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
