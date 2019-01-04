
package View_Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Model.Customer;
import db_utilities.db_connector;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Garrison
 */
public class EditCustomersController implements Initializable {


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
    private Button returnButton;
    @FXML
    private Button editCustomers;
    
    @FXML
    private TextField nameText;
    @FXML
    private TextField addressText;
    @FXML
    private TextField address2Text;
    @FXML
    private TextField cityText;
    @FXML
    private TextField postalCodeText;
    @FXML
    private TextField countryText;
    @FXML
    private TextField phoneText;

    
    ObservableList<Customer> customerList = FXCollections.observableArrayList();

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

    @FXML
    private void editCustomersButtonClicked(ActionEvent event) throws Exception {
        
    try{    
       TablePosition pos = customerTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Customer item = customerTableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        TableColumn id = idColumn;
        TableColumn addressId = addressIdColumn;
        TableColumn cityId = cityIdColumn;
        TableColumn countryId = countryIdColumn;

        String data = (String) col.getCellObservableValue(item).getValue();
        String data2 = (String) id.getCellObservableValue(item).getValue();
        String data3 = (String) addressId.getCellObservableValue(item).getValue();
        String data4 = (String) cityId.getCellObservableValue(item).getValue();
        String data5 = (String) countryId.getCellObservableValue(item).getValue();
        
        String getCountry = countryText.getText();
        String getCity = cityText.getText();
        String getAddress = addressText.getText();
        String getAddress2 = address2Text.getText();
        String getPostalCode = postalCodeText.getText();
        String getPhone = phoneText.getText();
        String getName = nameText.getText();

                
        if (col == nameColumn) {
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE customer "
                        + "SET customerName = ? WHERE customerid = ? ");
                                                               
                if(getName == null || getName.length() == 0){
                   Alert alert = new Alert(Alert.AlertType.WARNING);
                   alert.setContentText("Please enter a valid name.");
                   alert.showAndWait();
                }
                
                else{
                ps.setString(1, getName);
                ps.setString(2, data2);
                ps.execute();
                nameText.clear();
                }
            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());
            }
        }

        if (col == addressColumn) {
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE address "
                        + "SET address = ? WHERE addressId = ? ");
                
                if(getAddress == null || getAddress.length() == 0){
                   Alert alert = new Alert(Alert.AlertType.WARNING);
                   alert.setContentText("Please enter a valid address.");
                   alert.showAndWait();
                }

                else{
                ps.setString(1, getAddress);
                ps.setString(2, data3);
                ps.execute();
                addressText.clear();
                }
            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());
            }
        }

        if (col == address2Column) {
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE address "
                        + "SET address2 = ? WHERE addressId = ? ");

                ps.setString(1, getAddress2);
                ps.setString(2, data3);
                ps.execute();
                address2Text.clear();
               
            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());
            }
        }

        if (col == cityColumn) {
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE city "
                        + "SET city = ? WHERE cityId = ? ");

                ps.setString(1, getCity);
                ps.setString(2, data4);
                
                if(getCity == null || getCity.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a valid city.");
                    alert.showAndWait();
                }
                
                else{
                ps.execute();
                cityText.clear();
                }
            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());
            }
        }

        if (col == postalCodeColumn) {
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE address "
                        + "SET postalCode = ? WHERE addressId = ? ");

                ps.setString(1, getPostalCode);
                ps.setString(2, data3);
                
                if(getPostalCode == null || getPostalCode.length() > 10){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Pleas enter a valid postal code. \n"
                    + "(10 characters max.)");
                    alert.showAndWait();
                }
                
                else{
                ps.execute();
                postalCodeText.clear();
                }
            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());
            }
        }

        if (col == countryColumn) {
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE country "
                        + "SET country = ? WHERE countryId = ? ");

                ps.setString(1, getCountry);
                ps.setString(2, data5);
                
                if(getCountry == null || getCountry.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Please enter a valid country.");
                    alert.showAndWait();
                }
                
                else{
                ps.execute();
                countryText.clear();
                }
            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());
            }
        }

        if (col == phoneColumn) {            
            try {
                PreparedStatement ps = db_connector.getConn().prepareStatement("UPDATE address "
                        + "SET phone = ? WHERE addressid = ? ");
                                
                if(getPhone.length() == 0 || (getPhone.length() > 0 && (getPhone.length() < 10 || getPhone.length() > 12))){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                   alert.setContentText("Please enter a valid phone number \n"
                    + "(10 to 12 characters in length)");
                    alert.showAndWait();              
                }
                
                else{                    
                ps.setString(1, getPhone);
                ps.setString(2, data3);                
                ps.execute();
                phoneText.clear();
                }
            } catch (SQLException ex) {
                System.out.println("Check your SQL" + ex.getMessage());
            }
        }
    }catch (Exception e){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Please select a field to edit \n" +
        "before clicking the Edit Customers button.");
        alert.showAndWait();e.printStackTrace();}
        
        populateCustomers();
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
 
        populateCustomers();

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

