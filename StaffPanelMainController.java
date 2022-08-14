package MVC_Structure.controller;

import MVC_Structure.model.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.*;
import java.io.IOException;
import java.time.LocalDate;

public class StaffPanelMainController {

    @FXML
    private TextField txtDId;

    @FXML
    private Label txtFirstName1;

    @FXML
    private Label txtLastName1;

    @FXML
    private Label txtAge1;

    @FXML
    private Label txtDate1;

    @FXML
    private Label txtPhone1;

    @FXML
    private Label txtVisit1;

    @FXML
    private Label txtSpecialist1;
    
    DbConnector db = new DbConnector();
        public Connection con;
        PreparedStatement prst = null;
        ResultSet rs = null;


    public void Patientpanel(ActionEvent even) throws IOException {
        ((Node) even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/PatientPanelReception.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }

    public void MedicinePanel(ActionEvent even) throws IOException {
        ((Node) even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/MedisinPanelReception.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }

    public void DoctorSearch(ActionEvent even) throws IOException {
        ((Node) even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/SearchDoctorStaff.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }

    public void SearchDoctorInformation(ActionEvent even)throws IOException{

        
        String DId1 = txtDId.getText();
        String query = "select DId, DFirstName, DLastName, DPhone, DAge, DDate, DSpecialist, DVisit from doctor where DId = ?";


        try{
            
             con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,DId1);
            rs = prst.executeQuery();

            while(rs.next()){

                String DId = rs.getString("DId");
                String DFirstName = rs.getString("DFirstName");
                String DLastName = rs.getString("DLastName");
                String DPhone = rs.getString("DPhone");
                String DAge = rs.getString("DAge");
                Date DDate = rs.getDate("DDate");
                String DSpecialist = rs.getString("DSpecialist");
                String DVisit = rs.getString("DVisit");
                String dateshow = DDate.toString();

                txtFirstName1.setText(DFirstName);
                txtLastName1.setText(DLastName);
                txtPhone1.setText(DPhone);
                txtAge1.setText(DAge);
                txtDate1.setText(dateshow);
                txtSpecialist1.setText(DSpecialist);
                txtVisit1.setText(DVisit);

            }

        }catch(Exception e){ System.out.println(e);}
    }

    public void DoctorBack(ActionEvent even) throws IOException {
        ((Node) even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/StaffPanelMain.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }



    public void Logout(ActionEvent even) throws IOException {
        ((Node) even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/AdminReceptionCheck.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }
}