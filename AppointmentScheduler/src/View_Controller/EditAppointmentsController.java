
package View_Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Model.Appointment;
import db_utilities.db_connector;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Garrison
 */
public class EditAppointmentsController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, String> customerColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentIdColumn;

    @FXML
    private ComboBox<String> comboBoxLocation;
    @FXML  
    private ComboBox<String> comboBoxStartTime;
    @FXML
    private ComboBox<String> comboBoxEndTime;    
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField typeText;

    @FXML
    private Button editButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button updateTimesButton;
    @FXML
    private Button updateTypeButton;
    @FXML
    private Button updateLocationButton;

    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    String tCustomer;
    String tType;
    String tStart;
    String tEnd;
    String tLocation;
    String tAppointmentId;
    
    String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    
    @FXML
    private void updateTimesButtonClicked(ActionEvent event) throws Exception{
        
        try{
        TablePosition pos = appointmentTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Appointment item = appointmentTableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        TableColumn id = appointmentIdColumn;
                                
        String data = (String) id.getCellObservableValue(item).getValue();
 
        LocalDate selectedDate = datePicker.getValue();
        String localDateString = selectedDate.toString();
        
        String startTime = comboBoxStartTime.getValue();
        String endTime = comboBoxEndTime.getValue();
                
        LocalTime begin = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
                                        
        String getStartFinal = localDateString + " " + startTime;
        String getEndFinal = localDateString + " " + endTime;


        if (col == startColumn || col == endColumn){
            
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
                                    PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE appointment "
                                    + "SET start = ? WHERE appointmentId = ? ");

                                    ps.setString(1, getStartFinal);               
                                    ps.setString(2, data);
                
                                    PreparedStatement ps2 = db_connector.getConn().prepareStatement("UPDATE appointment "
                                    + "SET end = ? WHERE appointmentId = ? ");

                                    ps2.setString(1, getEndFinal);
                                    ps2.setString(2, data);
                
                                    ps.execute();                
                                    ps2.execute();                                
                                    } catch (SQLException ex){System.out.println("Check your SQL" + ex.getMessage());}
                                }   
                            }
                        }catch (SQLException ex){System.out.println("Check your SQL" + ex.getMessage());}                               
                    }
        }
        if (col == locationColumn || col == customerColumn || col == typeColumn){                                       
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("It seems like you've clicked on the "
                    + "Type, Location, or Customer column. Please click on the "
                    + "Start or End columns.");
            alert.showAndWait();
            } 
        }catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Did you select a field to edit"
                + " and/or select a calender date?");
        alert.showAndWait();}
                
        populateAppointments(); 
    
    }
    
    @FXML
    private void updateTypeButtonClicked(ActionEvent event) throws Exception {
       
    try{
        TablePosition pos = appointmentTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Appointment item = appointmentTableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        TableColumn id = appointmentIdColumn;
                                
        String data = (String) id.getCellObservableValue(item).getValue();
        String getType = typeText.getText();

        if (col == typeColumn){
            try{
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE appointment "
                        + "SET type = ? WHERE appointmentId = ? ");

                if(getType.length() == 0){
                     Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a new appointment type.");
                    alert.showAndWait();
                }
                
                else{
                ps.setString(1, getType);
                ps.setString(2, data);
                ps.execute();
                }
            } catch (SQLException ex){
                System.out.println("Check your SQL" + ex.getMessage());}
        }
         
        if (col == locationColumn || col == startColumn || col == endColumn || col == customerColumn){                                       
                 Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("It seems like you've clicked on the "
                    + "Start, End, Location, or Customer column. Please click on the "
                    + "Type column.");
                alert.showAndWait();
            }              
        }catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Did you select a field to edit?");
        alert.showAndWait();}
                
        populateAppointments(); 
    
    }
    
    @FXML
    private void updateLocationButtonClicked(ActionEvent event) throws Exception {
       
    try{
        TablePosition pos = appointmentTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Appointment item = appointmentTableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        TableColumn id = appointmentIdColumn;
                                
        String data = (String) id.getCellObservableValue(item).getValue();
        
        String getLocation = comboBoxLocation.getValue();           
         
        if (col == locationColumn){                                       
                try{
                    PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE appointment "
                            + "SET location = ? WHERE appointmentId = ? ");
                    
                    ps.setString(1, getLocation);
                    ps.setString(2, data);
                    ps.execute();
                } catch (SQLException ex){
                    System.out.println("Check your SQL" + ex.getMessage());}
            }
        
        if(col == typeColumn || col == startColumn || col == endColumn || col == customerColumn){
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("It seems like you've clicked on the "
                    + "Start, End, Type, or Customer column. Please click on the "
                    + "Location column.");
                alert.showAndWait();
            }         
        
        }catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Did you select a field to edit?");
        alert.showAndWait();}
                
        populateAppointments(); 
    
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
    public void initialize(URL url, ResourceBundle resources) {
                  
        populateAppointments();

        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTableView.setItems(appointmentList);
        
        comboBoxStartTime.getItems().addAll("09:00", "09:15","09:30", "09:45","10:00", "10:15","10:30", "10:45","11:00", "11:15","11:30", "11:45","12:00", "12:15","12:30", "12:45","13:00", "13:15","13:30", "13:45","14:00", "14:15","14:30", "14:45","15:00", "15:15","15:30", "15:45","16:00", "16:15","16:30","16:45");
        comboBoxEndTime.getItems().addAll("09:15","09:30", "09:45","10:00", "10:15","10:30", "10:45","11:00", "11:15","11:30", "11:45","12:00", "12:15","12:30", "12:45","13:00", "13:15","13:30", "13:45","14:00", "14:15","14:30", "14:45","15:00", "15:15","15:30", "15:45","16:00", "16:15","16:30","16:45", "17:00");
        comboBoxLocation.getItems().addAll ("Phoenix", "New York", "London");
        comboBoxStartTime.getSelectionModel().selectFirst();
        comboBoxEndTime.getSelectionModel().selectFirst();
        comboBoxLocation.getSelectionModel().selectFirst();

    }

    public void populateAppointments() {
        appointmentTableView.getItems().clear();
        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT customer.customerName, appointment.type, \n" +
                "appointment.start, appointment.end, appointment.appointmentId, appointment.location FROM appointment, customer WHERE \n" +
                "customer.customerId = appointment.customerId AND appointment.userId = 3\n" +
                " AND appointment.start Between now() AND DATE_ADD(NOW(), INTERVAL 1 year) ORDER BY appointment.start");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {

                tCustomer = rs.getString("customer.customerName");
                tType = rs.getString("appointment.type");
                tStart = rs.getString("appointment.start");
                tEnd = rs.getString("appointment.end");
                tLocation = rs.getString("appointment.location");
                tAppointmentId = rs.getString("appointment.appointmentId");
                                                               
                appointmentList.add(new Appointment(tCustomer, tType, tStart, tEnd, tLocation, tAppointmentId));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL" + sqe.getMessage());
        } catch (Exception e) {
            System.out.println("Something besides SQL went wrong.");
        }
    }        
}
