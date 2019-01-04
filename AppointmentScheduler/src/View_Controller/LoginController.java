
package View_Controller;

import Model.Appointment;
import db_utilities.db_connector;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Garrison
 */
public class LoginController implements Initializable {

    @FXML
    private Label appointmentLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    
    String loginWarningTitle;
    String loginWarningHeader;    
    String loginEmptyHeader;
       
    @FXML 
    private void LoginButtonClicked(ActionEvent event) throws IOException                     
    {            
    
        boolean login = String.valueOf(usernameText.getText()).equals("test") && 
                String.valueOf(passwordText.getText()).equals("test");
        
        boolean empty = String.valueOf(usernameText.getText()).equals("") || 
                String.valueOf(passwordText.getText()).equals("");
        
        if(login){    
            
            populateAppointmentsLogin();
                                     
            Parent HomeScreenParent = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
            Scene HomeScreenScene = new Scene(HomeScreenParent);
            Stage HomeScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            HomeScreenStage.setScene(HomeScreenScene);
            HomeScreenStage.show();
 
            // Code for keeping log of logins in .txt file starts below.
            
            try(FileWriter fw = new FileWriter("src/View_Controller/log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
            {
            out.println("User: 'Ben' logged in at " + Instant.now());
            } catch (IOException e) {}       
	
        }
        
        else if(empty)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(loginWarningTitle);
            alert.setHeaderText(loginEmptyHeader);       
            alert.showAndWait();
        }                    
        else
        {                          
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(loginWarningTitle);
            alert.setHeaderText(loginWarningHeader);       
            alert.showAndWait();                          
        }       
    }
        
    @FXML
    private void ExitButtonClicked(ActionEvent event) throws Exception{
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want exit?");
            alert.showAndWait()
            .filter(yes -> yes == ButtonType.OK)
            .ifPresent((ButtonType yes) -> {    
                System.exit(0);                 
                }                               
            );                                       
    }                     
               
    @Override
    public void initialize(URL location, ResourceBundle rb)     
    {        
        //The two lines of code below this comment translate the Login screen
        //into Japanese or Portuguese if the Local is changed to Japan or Brazil.        
        //Un-comment either line to test language change.
        
        //Locale.setDefault(new Locale("ja", "JP"));
        //Locale.setDefault(new Locale("pt", "BR"));
        
        rb = ResourceBundle.getBundle("View_Controller/rb", Locale.getDefault());
        loginWarningTitle = (rb.getString("warningTitle"));
        loginWarningHeader = (rb.getString("warningHeader"));
        loginEmptyHeader = (rb.getString("emptyHeader"));
        appointmentLabel.setText(rb.getString("appointmentScheduler"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        exitButton.setText(rb.getString("exit"));  
                
    }        
    
    public void populateAppointmentsLogin() {
        
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat sdf = new SimpleDateFormat(pattern);
        
        ObservableList<Appointment> appointmentListLogin = FXCollections.observableArrayList();
        
        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT customer.customerName, appointment.type,\n" +
                "appointment.start, appointment.end, appointment.appointmentId, appointment.location FROM appointment, customer WHERE\n" +
                "customer.customerId = appointment.customerId AND appointment.userId = 3\n" +
                "AND appointment.start Between now() AND DATE_ADD(NOW(), INTERVAL 16 minute) ORDER BY appointment.start");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {

          
                String start = rs.getString("appointment.start");
               
                if(start != null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alert!");
                    alert.setHeaderText("You have an appointment in the next 15 minutes.");       
                    alert.showAndWait();
                }
                                
                appointmentListLogin.add(new Appointment(start));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL" + sqe.getMessage());
        } catch (Exception e) {
            System.out.println("Something besides SQL went wrong.");
        }
    }
    
}
