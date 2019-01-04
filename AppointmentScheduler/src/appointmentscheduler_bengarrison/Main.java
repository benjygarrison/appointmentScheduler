
package appointmentscheduler_bengarrison;

import db_utilities.db_connector;
import java.io.IOException;
import java.sql.Connection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Ben Garrison
 */
public class Main extends Application 
{
    
    private static Connection connection;
    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));
        Scene scene = new Scene(root);       
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) 
    {
        db_connector.init();
        connection = db_connector.getConn();
        launch(args);
    }
    
}
