
package View_Controller;

import Model.Report;
import db_utilities.db_connector;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Garrison
 */
public class ReportsController implements Initializable {
    
    @FXML
    private TableView<Report> scheduleTableView;
    @FXML
    private TableColumn<Report, String> nameColumn;
    @FXML
    private TableColumn<Report, String> startColumn;
    @FXML
    private TableColumn<Report, String> endColumn;
    
    @FXML
    private TableView<Report> typeTableView;
    @FXML
    private TableColumn<Report, String> monthColumn;
    @FXML
    private TableColumn<Report, String> yearColumn;
    @FXML
    private TableColumn<Report, String> typeColumn;
    
    @FXML
    private TableView<Report> timeTravelTableView;
    @FXML
    private TableColumn<Report, String> timeTravelColumn;
            
    @FXML
    private Button returnButton;
    
    String pattern = "yyyy-MM-dd HH:mm:ss";
    DateFormat sdf = new SimpleDateFormat(pattern);

    ObservableList<Report> typeList = FXCollections.observableArrayList();
    ObservableList<Report> scheduleList = FXCollections.observableArrayList();
    ObservableList<Report> timeTravelList = FXCollections.observableArrayList();
    
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
                
        populateSchedules();
        populateTimeTravelers();
        populateAppointmentTypes();
                
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));        
        scheduleTableView.setItems(scheduleList);
        
        timeTravelColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        timeTravelTableView.setItems(timeTravelList);
        
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeTableView.setItems(typeList);
                           
    }    
    
    public void populateSchedules(){

        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT appointment.start, "
                        + "appointment.end, user.userName, appointment.userId FROM appointment, user\n" +
                        " WHERE appointment.userId = user.userId ORDER BY appointment.userId ");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {
               
                String start = rs.getString("appointment.start");
                String end = rs.getString("appointment.end");
                String tUserName = rs.getString("user.userName");
                String tUserId = rs.getString("appointment.userId");
                                                                                                                            
                Date date = sdf.parse(start);
                ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
                String myDate = zdt.format(DateTimeFormatter.ofPattern(pattern));                
                
                Date date2 = sdf.parse(end);
                ZonedDateTime zdt2 = ZonedDateTime.ofInstant(date2.toInstant(), ZoneId.of("UTC"));
                String myDate2 = zdt2.format(DateTimeFormatter.ofPattern(pattern));
                
                String tStart = myDate;
                String tEnd = myDate2;
                                
                scheduleList.add(new Report(tStart, tEnd, tUserName, tUserId));                                
            }            

        } catch (SQLException sqe) {
            System.out.println("Check your SQL" + sqe.getMessage());
        }catch (Exception e){
            System.out.println("Something besides SQL went wrong.");}        
    }
    
    public void populateTimeTravelers(){
     
        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT "
                        + "user.userName FROM user WHERE user.userId = 1 OR user.userId = 2 OR user.userId = 4 ");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {

                String tUserName = rs.getString("user.userName");
                                               
                timeTravelList.add(new Report(tUserName));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL" + sqe.getMessage());
        }catch (Exception e){
            System.out.println("Something besides SQL went wrong.");}   
    }
    
    public void populateAppointmentTypes(){
        
        try (
                PreparedStatement statement = db_connector.getConn().prepareStatement("SELECT YEAR(start) "
                        + " YEAR, MONTH (start) MONTH, count(DISTINCT appointment.type) FROM appointment\n" +
                        " GROUP BY YEAR(start), MONTH(start) ");
                ResultSet rs = statement.executeQuery();) {

            while (rs.next()) {

                String tMonth = rs.getString("month");
                String tYear = rs.getString("year");
                String tType = rs.getString("count(DISTINCT appointment.type)");
                
                typeList.add(new Report(tMonth, tYear, tType));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL" + sqe.getMessage());
        }catch (Exception e){
            System.out.println("Something besides SQL went wrong.");}  
    }
}
