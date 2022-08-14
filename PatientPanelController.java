package MVC_Structure.controller;

import MVC_Structure.controller.LoginMassageBoxController;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;


public class PatientPanelController {

    @FXML
    private TextField txtPId;
    
    @FXML
    private TextField txtGender;

    @FXML
    private TextField txtPFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtphone;

    @FXML
    private TextField txtAge;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtProblem;

    @FXML
    private TextField txtRoomNumber;

    @FXML
    private TextField txtCost;
    
    @FXML
    private TableView tableview;
    final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    
        DbConnector db = new DbConnector();
        public Connection con;
        PreparedStatement prst = null;
        ResultSet rs = null;

    //Add Patient Code

    public boolean AddPatient()throws IOException{
        boolean added= false;

        PreparedStatement prst = null;
        ResultSet rs = null;

//        String PID = txtPId.getText();
//        String PFirstName = txtPFirstName.getText();
//        String PLastName = txtLastName.getText();
//        String PAge = txtAge.getText();
//        LocalDate PDate = txtDate.getValue();
//        String Gender = txtGender.getText();
//        String PPhone = txtphone.getText();
//        String PProblem = txtProblem.getText();
//        String RoomNo = txtRoomNumber.getText();
//        String PCost = txtCost.getText();

//uncomment upper code if you are using GUI. And for testing below static variable are assigned.
        
        String PID = "12";
        String PFirstName = "A";
        String PLastName = "B";
        String PAge = "15";
        LocalDate PDate = LocalDate.parse("2020-12-26");
        String Gender = "Female";
        String PPhone ="10000000000";
        String PProblem = "Pain";
        String RoomNo = "5";
        String PCost = "1500";

        try{

            
            con= db.getConnection();
            Statement stmt=con.createStatement();

            String CheckQuery = "select PId from patient";

            prst = con.prepareStatement(CheckQuery);
            rs = prst.executeQuery();
            while (rs.next()){
                String Pid = rs.getString("PId");
                if(Pid.equals(PID)){
                    //LoginMassageBoxController.display("Insert Status", "This Id Already Use So try to another Id...");
                }
            }

            stmt.executeUpdate("INSERT INTO patient VALUES ('"+PID+"','"+PFirstName+"','"+PLastName+"','"+PAge+"','"+PDate+"','"+Gender+"','"+PPhone+"','"+PProblem+"','"+RoomNo+"', '"+PCost+"')");

           // LoginMassageBoxController.display("Insert Status", "Insert Successfully");
            added=true;
            con.close();

        }catch(Exception e){ System.out.println(e);}
        return added;
    }




//Search code

    public void SearchPatient()throws IOException{

       
        String Id = null, FirstName = null;
        Id = txtPId.getText();
       // FirstName = txtFirstName.getText();

        String query = "select PId, FirstName, LastName, Age, Date3, Gender,Phone, Problem, RId, Cost from patient where  PId = ?  ";

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,Id);
            //prst.setString(2,FirstName);
            rs = prst.executeQuery();

            while(rs.next()){

                //    String DId = rs.getString("DId");
                String PFirstName = rs.getString("FirstName");
                String PLastName = rs.getString("LastName");
                String PAge = rs.getString("Age");
                Date DDate = rs.getDate("Date3");
                String Gender = rs.getString("Gender");
                String Phone = rs.getString("Phone");
                String PProblem = rs.getString("Problem");
                String RId = rs.getString("RId");
                String PCost = rs.getString("Cost");
                String datetxt = DDate.toString();

                txtPFirstName.setText(PFirstName);
                txtLastName.setText(PLastName);
                txtAge.setText(PAge);
                txtDate.setValue(LocalDate.parse(datetxt));
                txtphone.setText(Phone);
                txtProblem.setText(PProblem);
                txtRoomNumber.setText(RId);
                txtCost.setText(PCost);
                txtGender.setText(Gender);

            }

        }catch(Exception e){ System.out.println(e);}
    }
    
    public void patientTable()throws IOException{

        
        String SQL = "select * from patient";

       
        try{
                tableview.getItems().clear();
                tableview.getColumns().clear();
            
             con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(SQL);
            //prst.setString(2,FirstName);
            rs = prst.executeQuery();
            System.out.println("for er age");
for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;    
                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){   
                    
                    
                    
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {  
                        
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });

                tableview.getColumns().addAll(col); 
                
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
            
            
          }catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");             
          }
      }



    // Update code


    public void UpdatePatientr(ActionEvent even)throws Exception{

        
        String PId1 = txtPId.getText();

        String FirstName = txtPFirstName.getText();
        String LastName = txtLastName.getText();
        String Age = txtAge.getText();
        LocalDate DDate = txtDate.getValue();
        String Gender = "Male";
        String Phone = txtphone.getText();
        String Problem = txtProblem.getText();
        String RoomNumber = txtRoomNumber.getText();
        String Cost = txtCost.getText();
        String dateshow = DDate.toString();

        String query = "UPDATE patient set FirstName = ?, LastName = ?, Age = ?, Date3 = ?,Gender = ? ,Phone = ?, Problem = ?, RId = ?, Cost = ? where PId = ?";

        try{
            
             con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,FirstName);
            prst.setString(2,LastName);
            prst.setString(3,Age);
            prst.setString(4,dateshow);
            prst.setString(5, Gender);
            prst.setString(6,Phone);
            prst.setString(7,Problem);
            prst.setString(8,RoomNumber);
            prst.setString(9,Cost);
            prst.setString(10,PId1);

            int i = prst.executeUpdate();
            if(i==1) {
                LoginMassageBoxController.display("UpDate Status", "UpDate Successfully");
            }
        }
        catch(SQLException e){ System.out.println(e);}
    }






    //Delete code

    public void DeletePatient(ActionEvent even)throws Exception{

        
        String PId = txtPId.getText();

        String query = "delete from patient where PId = ?";

        try{
            
             con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,PId);
            int i = prst.executeUpdate();
            if(i==1)
                LoginMassageBoxController.display("Delete Status", "Delete Successfully");

        }
        catch(SQLException e){ System.out.println(e);}
    }



    //Back Code

    public void Backmain(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/AdminPanel.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }



    // RoomTable Code

    public void RoomTable(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/RoomR.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }

    // Back Code

    public void Back(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/ReceptionPanel.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }

}
