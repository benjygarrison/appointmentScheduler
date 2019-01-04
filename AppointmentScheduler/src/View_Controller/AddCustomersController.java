
package View_Controller;

import db_utilities.db_connector;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ben garrison
 */
public class AddCustomersController implements Initializable {

    @FXML
    private Button addButton;
    @FXML
    private Button returnButton;
    @FXML
    private TextField nameText;
    @FXML
    private TextField addressText;
    @FXML
    private TextField addressText2;
    @FXML
    private TextField cityText;
    @FXML
    private TextField postalCodeText;
    @FXML
    private TextField countryText;
    @FXML
    private TextField phoneText;

                                                               
    @FXML
    private void insertCustomer(ActionEvent event){
        
        String getCountry = countryText.getText();
        String getCity = cityText.getText();
        String getAddress = addressText.getText();
        String getAddress2 = addressText2.getText();
        String getPostalCode = postalCodeText.getText();
        String getPhone = phoneText.getText();
        String getName = nameText.getText();
                        
            try{
                PreparedStatement ps = db_connector.getConn().prepareStatement("INSERT INTO "
                + "country (country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, now(), 'Ben', now(), 'Ben')");

                        ps.setString(1, getCountry);
            
                PreparedStatement ps1 = db_connector.getConn().prepareStatement("INSERT INTO city "
                + "(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, LAST_INSERT_ID(), now(), 'Ben', now(), 'Ben')");
                                        
                ps1.setString(1, getCity);
   
                PreparedStatement ps2 = db_connector.getConn().prepareStatement("INSERT INTO address "
                + "(address, address2, postalCode, phone, cityId, createDate, createdBy, "
                + "lastUpdate, lastUpdateBy) VALUES "
                + "(?, ?, ?, ?, LAST_INSERT_ID(), now(), 'Ben', now(), 'Ben')");
                                                        
                ps2.setString(1, getAddress);
                ps2.setString(2, getAddress2);
                ps2.setString(3, getPostalCode);
                ps2.setString(4, getPhone);
                    
                PreparedStatement ps3 = db_connector.getConn().prepareStatement("INSERT INTO customer"
                + "(customerName, addressId, active, createDate, createdBy, lastUpdate, "
                + "lastUpdateBy) VALUES (?, LAST_INSERT_ID(), 0, now(), 'Ben', now(), 'Ben')");
                
                ps3.setString(1, getName);
                
                //if((getName.length() == 0 || getName == null) && (getCity.length() == 0 || getCity == null) && (getCountry.length() == 0 || getCountry == null) && (getPostalCode.length() == 0 || getPostalCode == null) && (getPhone.length() == 0 || getPhone == null)){
                //Alert alert = new Alert(Alert.AlertType.WARNING);
                //alert.setContentText("No information has been entered.");
                //alert.showAndWait();  
                //}
                
                if(getName.length() == 0 || getName == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a name.");
                    alert.showAndWait();
                }
                
                if(getCity.length() == 0 || getCity == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a city.");
                    alert.showAndWait();
                }
                
                if(getCountry.length() == 0 || getCountry == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a country.");
                    alert.showAndWait();
                }
                
                if(getPostalCode.length() == 0 || getPostalCode == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a postal code.");
                    alert.showAndWait();
                }
                
                if(getPostalCode.length() > 10){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Postal Code must be \n"
                    + "10 characters or fewer.");
                    alert.showAndWait();
                }
                
                if(getPhone.length() == 0 || getPhone == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a phone number.");
                    alert.showAndWait();              
                } 
                
                if(getPhone.length() > 0 && (getPhone.length() < 10 || getPhone.length() > 12)){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Phone number must be 10 to 12 characters\n"
                    + "(including hyphens).");
                    alert.showAndWait();              
                }                
                         
                else if (getPhone.length() > 0 && getPostalCode.length() > 0 && getCountry.length() > 0 && getCity.length() > 0 && getName.length() > 0){
                    ps.execute();
                    ps1.execute();
                    ps2.execute();
                    ps3.execute(); 
                }
            }catch (SQLException ex){System.out.println("Check your SQL" + ex.getMessage());}            
    }
    
    @FXML
    private void returnButtonClicked(ActionEvent event) throws IOException{                     
        Parent HomeScreenParent = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        Scene HomeScreenScene = new Scene(HomeScreenParent);
        Stage HomeScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        HomeScreenStage.setScene(HomeScreenScene);
        HomeScreenStage.show(); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
               
    }
     
}
