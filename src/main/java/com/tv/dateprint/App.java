package com.tv.dateprint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

//    private static Scene scene;
    
    private static Stage primaryStage;
        
    private void setPrimaryStage(Stage stage) {
        App.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return App.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        setPrimaryStage(primaryStage);
        
//        primaryStage.setX(0);
//        primaryStage.setY(0);
//        primaryStage.setWidth(800);
//        primaryStage.setHeight(600);
        
        Parent root = FXMLLoader.load(getClass().getResource("dateprint.fxml"));
        
        primaryStage.setTitle("Date Print");
        primaryStage.setScene(new Scene(root));
        
        InputStream iconStream = getClass().getResourceAsStream("prnlogo.png");
        Image image = new Image(iconStream);
        primaryStage.getIcons().add(image);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}