
package View_Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Model.Appointment;
import Model.Customer;
import db_utilities.db_connector;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Garrison
 */
public class HomeScreenController implements Initializable {

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
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> address2Column;
    @FXML
    private TableColumn<Customer, String> cityColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> idColumn;
    @FXML
    private TableColumn<Customer, String> addressIdColumn;
    @FXML
    private TableColumn<Customer, String> cityIdColumn;
    @FXML
    private TableColumn<Customer, String> countryIdColumn;

    @FXML
    private TextField customerIdText;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button addCustomersButton;
    @FXML
    private Button deleteCustomers;
    @FXML
    private Button editCustomers;
    @FXML
    private RadioButton weeklyRadioButton;
    @FXML
    private RadioButton monthlyRadioButton;
    @FXML
    private ToggleGroup weeklyMonthlyToggleGroup;        

    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    String tCustomer;
    String tType;
    String tStart;
    String tEnd;
    String tLocation;
    String tAppointmentId;

    String tCustomerId;
    String tCustomerName;
    String tAddress;
    String tAddress2;
    String tCity;
    String tPostalCode;
    String tCountry;
    String tPhone;
    String tAddressId;
    String tCityId;
    String tCountryId;
    
    String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @FXML
    private void weeklyRadioButtonSelected (ActionEvent event) throws Exception{
            
        populateAppointmentsWeekly();        
    }
    
    @FXML
    private void monthlyRadioButtonSelected (ActionEvent event) throws Exception {
            
        populateAppointments();        
    }
    
    
    @FXML
    private void reportsButtonClicked(ActionEvent event) throws Exception {
        Parent ReportsParent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene ReportsScene = new Scene(ReportsParent);
        Stage ReportsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ReportsStage.setScene(ReportsScene);
        ReportsStage.show();  
    }

    @FXML
    private void addAppointmentsClicked(ActionEvent event) throws Exception {
        Parent AddAppointmentsParent = FXMLLoader.load(getClass().getResource("AddAppointments.fxml"));
        Scene AddAppointmentsScene = new Scene(AddAppointmentsParent);
        Stage AddAppointmentsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AddAppointmentsStage.setScene(AddAppointmentsScene);
        AddAppointmentsStage.show();  
    }

    @FXML
    private void editAppointmentsClicked(ActionEvent event) throws Exception {
        Parent AddAppointmentsParent = FXMLLoader.load(getClass().getResource("EditAppointments.fxml"));
        Scene AddAppointmentsScene = new Scene(AddAppointmentsParent);
        Stage AddAppointmentsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AddAppointmentsStage.setScene(AddAppointmentsScene);
        AddAppointmentsStage.show(); 
    }

    @FXML
    private void deleteAppointmentsClicked(ActionEvent event) throws Exception {
    
    try{    
        TablePosition pos = appointmentTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Appointment item = appointmentTableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        TableColumn id = appointmentIdColumn;

        String data = (String) col.getCellObservableValue(item).getValue();
        String data2 = (String) id.getCellObservableValue(item).getValue();

        if (col == customerColumn) {
            String deleteAppointment = data2;
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want delete this appointment?");
            alert.showAndWait()
            .filter(okay -> okay == ButtonType.OK)    
            .ifPresent((ButtonType okay) -> {       
                try {
                    PreparedStatement ps = db_connector.getConn().prepareStatement("DELETE FROM "
                        + "appointment WHERE appointmentId = ?");

                    ps.setString(1, deleteAppointment);                
                    ps.execute();

                }catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());}                     
                });                                     
        } 
        
        if (col != customerColumn) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please click on a name in the 'Customer' column \n"
            + "and then click the Delete Customers button.");
            alert.showAndWait(); 
        }
        }catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Please click on a name in the 'Customer' column \n"
        + "and then click the Delete Appointments button.");
        alert.showAndWait();}
        
    populateAppointments();
    }

    @FXML
    private void deleteCustomersClicked(ActionEvent event) {
        
    try{    
        TablePosition pos = customerTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Customer item = customerTableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        TableColumn id = idColumn;

        String data = (String) col.getCellObservableValue(item).getValue();
        String data2 = (String) id.getCellObservableValue(item).getValue();

        if (col == nameColumn) {
            String deleteCustomer = data2;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want delete this customer?");
            alert.showAndWait()
            .filter(okay -> okay == ButtonType.OK)    
            .ifPresent((ButtonType okay) -> {       
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("DELETE FROM "
                        + "customer WHERE customerId = ?");

                ps.setString(1, deleteCustomer);                                
                ps.execute();

            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());}
            });
        } if (col != nameColumn) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please click on a name in the 'Name' column \n"
            + "and then click the Delete Customers button.");
            alert.showAndWait(); 
        }
    }catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Please click on a name in the 'Name' column \n"
        + "and then click the Delete Customers button.");
        alert.showAndWait();}
        
    populateCustomers();
    
    }

    @FXML
    private void editCustomersButtonClicked(ActionEvent event) throws Exception {
        Parent AddCustomersParent = FXMLLoader.load(getClass().getResource("EditCustomers.fxml"));
        Scene AddCustomersScene = new Scene(AddCustomersParent);
        Stage AddCustomersStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AddCustomersStage.setScene(AddCustomersScene);
        AddCustomersStage.show();
    }

    @FXML
    private void addCustomersButtonClicked(ActionEvent event) throws Exception {
        Parent AddCustomersParent = FXMLLoader.load(getClass().getResource("AddCustomers.fxml"));
        Scene AddCustomersScene = new Scene(AddCustomersParent);
        Stage AddCustomersStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AddCustomersStage.setScene(AddCustomersScene);
        AddCustomersStage.show();
    }

    @FXML
    private void exitButtonClicked(ActionEvent event) throws Exception {
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
    public void initialize(URL url, ResourceBundle resources) {
                        
        populateAppointments();
        populateCustomers();
        
        weeklyMonthlyToggleGroup = new ToggleGroup();
        this.weeklyRadioButton.setToggleGroup(weeklyMonthlyToggleGroup);
        this.monthlyRadioButton.setToggleGroup(weeklyMonthlyToggleGroup);

        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTableView.setItems(appointmentList);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2Column.setCellValueFactory(new PropertyValueFactory<>("address2"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        addressIdColumn.setCellValueFactory(new PropertyValueFactory<>("addressId"));
        cityIdColumn.setCellValueFactory(new PropertyValueFactory<>("cityId"));
        countryIdColumn.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        customerTableView.setItems(customerList);
        
    }

    public void populateAppointments() {
        appointmentTableView.getItems().clear();
        
        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT customer.customerName, appointment.type, \n" +
                "appointment.start, appointment.end, appointment.appointmentId, appointment.location FROM appointment, customer WHERE \n" +
                "customer.customerId = appointment.customerId AND appointment.userId = 3\n" +
                "AND appointment.start Between now() AND DATE_ADD(NOW(), INTERVAL 1 month) ORDER BY appointment.start");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {

                tCustomer = rs.getString("customer.customerName");
                tType = rs.getString("appointment.type");
                Timestamp tsStart = rs.getTimestamp("appointment.start");
                Timestamp tsEnd = rs.getTimestamp("appointment.end");
                tLocation = rs.getString("appointment.location");
                tAppointmentId = rs.getString("appointment.appointmentId");
                
                ZoneId newzid = ZoneId.systemDefault();
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);                
                String startTime = newLocalStart.format(DateTimeFormatter.ofPattern(pattern));

                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);
                
                String endTime = newLocalEnd.format(DateTimeFormatter.ofPattern(pattern));
                                                                
                tStart = startTime;
                tEnd = endTime;
                                
                appointmentList.add(new Appointment(tCustomer, tType, tStart, tEnd, tLocation, tAppointmentId));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL" + sqe.getMessage());
        } catch (Exception e) {
            System.out.println("Something besides SQL went wrong.");
        }
    }
    
    public void populateAppointmentsWeekly() {
        appointmentTableView.getItems().clear();
        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT customer.customerName, appointment.type, \n" +
                "appointment.start, appointment.end, appointment.appointmentId, appointment.location FROM appointment, customer WHERE \n" +
                "customer.customerId = appointment.customerId AND appointment.userId = 3\n" +
                "AND appointment.start Between now() AND DATE_ADD(NOW(), INTERVAL 1 week) ORDER BY appointment.start");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {

                tCustomer = rs.getString("customer.customerName");
                tType = rs.getString("appointment.type");
                Timestamp tsStart = rs.getTimestamp("appointment.start");
                Timestamp tsEnd = rs.getTimestamp("appointment.end");
                tLocation = rs.getString("appointment.location");
                tAppointmentId = rs.getString("appointment.appointmentId");
                
                ZoneId newzid = ZoneId.systemDefault();
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);                
                String startTime = newLocalStart.format(DateTimeFormatter.ofPattern(pattern));

                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);
                
                String endTime = newLocalEnd.format(DateTimeFormatter.ofPattern(pattern));
                                                               
                tStart = startTime;
                tEnd = endTime;
                                
                appointmentList.add(new Appointment(tCustomer, tType, tStart, tEnd, tLocation, tAppointmentId));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL" + sqe.getMessage());
        } catch (Exception e) {
            System.out.println("Something besides SQL went wrong." + e.getMessage());
        }
    }

    public void populateCustomers() {
        customerTableView.getItems().clear();
        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT customer.customerid, "
                        + " customer.customerName,  address.address, address.address2, address.postalCode, \n"
                        + " address.phone, address.addressId, city.city, city.cityId, country.country, country.countryId FROM customer, address, city, country "
                        + " WHERE customer.addressId = address.addressid \n"
                        + " AND address.cityId = city.cityid AND city.countryId = country.countryid ");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {

                tCustomerId = rs.getString("customer.customerId");
                tCustomerName = rs.getString("customer.customerName");
                tAddress = rs.getString("address.address");
                tAddress2 = rs.getString("address.address2");
                tCity = rs.getString("city.city");
                tPostalCode = rs.getString("address.postalCode");
                tCountry = rs.getString("country.country");
                tPhone = rs.getString("address.phone");
                tAddressId = rs.getString("address.addressId");
                tCityId = rs.getString("city.cityId");
                tCountryId = rs.getString("country.countryId");
       
                customerList.add(new Customer(tCustomerId, tCustomerName, tAddress, tAddress2, tCity, tPostalCode,
                        tCountry, tPhone, tAddressId, tCityId, tCountryId));
            }
        } catch (SQLException sqe1) {
            System.out.println("Check your SQL" + sqe1.getMessage());
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }
    }
}
