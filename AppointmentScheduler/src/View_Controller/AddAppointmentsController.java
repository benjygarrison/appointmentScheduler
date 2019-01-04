
package View_Controller;

import Model.Appointment;
import Model.Customer;
import db_utilities.db_connector;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Garrison
 * 
 */
public class AddAppointmentsController implements Initializable {
    
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> idColumn;
    
    @FXML
    private Button returnButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameText; 
    @FXML
    private TextField locationText; 
    @FXML
    private TextField typeText; 
    
    @FXML
    private ComboBox<String> comboBoxLocation;
    @FXML  
    private ComboBox<String> comboBoxStartTime;
    @FXML
    private ComboBox<String> comboBoxEndTime;    
    @FXML
    private DatePicker datePicker;
           
    String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    
    String tStart;
    String tEnd;
    
    Date date;           
    Date date2;
       
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    ObservableList<Appointment> appointmentRange = FXCollections.observableArrayList();
    
    @FXML
    private void addAppoinments(ActionEvent event) throws Exception{

    try{        
        TablePosition pos = customerTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Customer item = customerTableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        TableColumn id = idColumn;
            
        String selectCustomer = (String) col.getCellObservableValue(item).getValue();    
        String data = (String) id.getCellObservableValue(item).getValue();
                
        String getLocation = comboBoxLocation.getValue();
        String getType = typeText.getText();
        
        LocalDate selectedDate = datePicker.getValue();
        String localDateString = selectedDate.toString();
        
        String startTime = comboBoxStartTime.getValue();
        String endTime = comboBoxEndTime.getValue();
                
        LocalTime begin = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        LocalDate today = LocalDate.now();
                                        
        String getStartFinal = localDateString + " " + startTime;
        String getEndFinal = localDateString + " " + endTime;
               
        LocalTime now = LocalTime.now();
        
        if(end.isBefore(begin)){
           Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("End time cannot be before start time.");
                alert.showAndWait();
        }

        if(selectedDate.isBefore(today)){
           Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Appointment date cannot be before current time.");
                alert.showAndWait();
        }
        
        if(localDateString.length() < 1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select a date.");
                alert.showAndWait();
        }
        
        else{
            try{
                PreparedStatement ps1 = db_connector.getConn().prepareStatement(                 
                        "SELECT count(appointment.appointmentid) "
                        + " FROM appointment WHERE (appointment.start = ? AND "
                        + " appointment.end = ?)"
                        + " OR (appointment.end Between ? AND ?)\n"
                        + " OR (appointment.start Between ? AND ?)");
                                
                        ps1.setString(1, getStartFinal);
                        ps1.setString(2, getEndFinal);
                        ps1.setString(3, getStartFinal);
                        ps1.setString(4, getEndFinal);                    
                        ps1.setString(5, getStartFinal);
                        ps1.setString(6, getEndFinal);
                        
                        ResultSet rs = ps1.executeQuery();
                        
                        while (rs.next()) {
            
                        String isConflict = rs.getString("count(appointment.appointmentid)");                                               
                                                
                            if(!"0".equals(isConflict)){
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("There is a scheduling conflict. \n"
                                + "Please pick other times.");
                                alert.showAndWait();
                            }
                            else{
                                try{
                                    PreparedStatement ps = db_connector.getConn().prepareStatement("INSERT INTO "
                                    + "appointment (customerId, userId, title, description,"
                                    + " location, contact, type, url, start, end, createDate, createdBy,"
                                    + " lastUpdate, lastUpdateBy) "
                                    + "VALUES (?, 3, 'appointment', 'routine', ?, 'Ben', ?, 'N/A', ?, "
                                    + "?, now(), 'Ben', now(), 'Ben')");                                
            
                                    if(getLocation.length() == 0 || getType.length() == 0){                               
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setContentText("One or more fields is empty \n"
                                        + "please fill in all fields.");
                                        alert.showAndWait();
                                    } 
                     
                                    else{                    
                                        ps.setString(1, data);
                                        ps.setString(2, getLocation);
                                        ps.setString(3, getType);
                                        ps.setString(4, getStartFinal);                    
                                        ps.setString(5, getEndFinal);
                                        ps.execute();                
                                    }            
                                }catch (SQLException ex){System.out.println("Check your SQL" + ex.getMessage());}
                            }    
                            
                        }
                    }catch (SQLException ex){System.out.println("Check your SQL" + ex.getMessage());}                         
                }   
            }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Did you select a customer name from the list\n"
            + "and a date from the calender?" + e.getMessage());
            alert.showAndWait();}
    }
    
    @FXML
    private void returnButtonClicked(ActionEvent event) throws Exception {                       
        Parent HomeScreenParent = FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        Scene HomeScreenScene = new Scene(HomeScreenParent);
        Stage HomeScreenStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        HomeScreenStage.setScene(HomeScreenScene);
        HomeScreenStage.show();  
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        populateCustomerList();
                
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerTableView.setItems(customerList);
        
        comboBoxStartTime.getItems().addAll("09:00", "09:15","09:30", "09:45","10:00", "10:15","10:30", "10:45","11:00", "11:15","11:30", "11:45","12:00", "12:15","12:30", "12:45","13:00", "13:15","13:30", "13:45","14:00", "14:15","14:30", "14:45","15:00", "15:15","15:30", "15:45","16:00", "16:15","16:30","16:45");
        comboBoxEndTime.getItems().addAll("09:15","09:30", "09:45","10:00", "10:15","10:30", "10:45","11:00", "11:15","11:30", "11:45","12:00", "12:15","12:30", "12:45","13:00", "13:15","13:30", "13:45","14:00", "14:15","14:30", "14:45","15:00", "15:15","15:30", "15:45","16:00", "16:15","16:30","16:45", "17:00");
        comboBoxLocation.getItems().addAll ("Phoenix", "New York", "London");
        comboBoxStartTime.getSelectionModel().selectFirst();
        comboBoxEndTime.getSelectionModel().selectFirst();
        comboBoxLocation.getSelectionModel().selectFirst();
    }    
    
    public void populateCustomerList(){
        customerTableView.getItems().clear();
        try (
            PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT customer.customerName, "
            + "customer.customerid FROM customer ");
            ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {
            
                String tCustomerName = rs.getString("customer.customerName");
                String tCustomerId = rs.getString("customer.customerId");
                
                customerList.add(new Customer(tCustomerName, tCustomerId));
            }
        }catch (SQLException sqe1){
            System.out.println("Check your SQL" + sqe1.getMessage());
        }catch (Exception e){
            System.out.println("Something besides the SQL went wrong.");}
    }
        
    public void populateAppointmentRange(){        
        try (
            PreparedStatement statement1 = db_connector.getConn().prepareStatement("SELECT appointment.start, "
            + " appointment.end FROM appointment WHERE appointment.userId = 3 ");
            ResultSet rs1 = statement1.executeQuery();) {

            while (rs1.next()) {
            
                tStart = rs1.getString("appointment.start");
                tEnd = rs1.getString("appointment.end");
                
                date = sdf.parse(tStart);             
                date2 = sdf.parse(tEnd);
                                                                                                
                appointmentRange.add(new Appointment(tStart, tEnd));
                
            }
        }catch (SQLException sqe1){
            System.out.println("Check your SQL" + sqe1.getMessage());
        }catch (Exception e){
            System.out.println("Something besides the SQL went wrong." + e.getMessage());}
    }
                                                      
}
