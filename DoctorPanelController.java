package MVC_Structure.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.sql.*;
import java.io.IOException;
import MVC_Structure.model.DbConnector;
import MVC_Structure.model.DbConnector;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DoctorPanelController {

    @FXML
    private TextField txtDoctorId;
    @FXML
    private TextField viewAll;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtAge;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtPhone;

    


    @FXML
    private TextField txtSpecialist;

    @FXML
    private TextField txtsalary;
    @FXML
    private TextField txtGender;

    
    
    
    final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
    @FXML
    private TableView tableview;


//Add code
    
        DbConnector db = new DbConnector();
        public Connection con;
        PreparedStatement prst = null;
        ResultSet rs = null;
        
        

    public void AddDoctor(ActionEvent even)throws IOException{


        String DId = txtDoctorId.getText();
        String DFirstName = txtFirstName.getText();
        String DLastName = txtLastName.getText();
        String DAge = txtAge.getText();
        LocalDate DDate = txtDate.getValue();
        String DPhone = txtPhone.getText();
        String Gender= txtGender.getText();

        //String Gender = null, GenderF = null;
        //Gender = gender.getText();
        //GenderF = genderF.getText();
        //Female = txtfemale.getText();

        String DSpecialist = txtSpecialist.getText();
        String DSalary = txtsalary.getText();

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();

            String CheckQuery = "select DId from doctor";

            prst = con.prepareStatement(CheckQuery);
            rs = prst.executeQuery();
            while (rs.next()){
                String Did = rs.getString("DId");
                if(Did.equals(DId)){
                    LoginMassageBoxController.display("Insert Status", "This Id Already Use So try to another Id...");
                }
            }
            //System.out.println(Gender);

            stmt.executeUpdate("INSERT INTO doctor VALUES ('" + DId + "','" + DFirstName + "','" + DLastName + "','" + DAge + "','" + DDate + "','" + DPhone + "','" + Gender + "','" + DSpecialist + "', '" + DSalary + "')");


            LoginMassageBoxController.display("Insert Status", "Insert Successfully");


         con.close();

        }catch(Exception e){ System.out.println(e);}
    }

//Search code

    public void SearchDoctor(ActionEvent even)throws IOException{

        
        String Id = null, FirstName = null;
        Id = txtDoctorId.getText();
        FirstName = txtFirstName.getText();

        String query = "select DId, FirstName, LastName, Age, Date1, Phone, Gender, Specialist, Salary from doctor where  DId = ?  ";

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,Id);
            //prst.setString(2,FirstName);
            rs = prst.executeQuery();

            while(rs.next()){

                //    String DId = rs.getString("DId");
                String DFirstName = rs.getString("FirstName");
                String DLastName = rs.getString("LastName");
                String DAge = rs.getString("Age");
                Date DDate = rs.getDate("Date1");
                String DPhone = rs.getString("Phone");
                String Gender1 = rs.getString("Gender");
                String DSpecialist = rs.getString("Specialist");
                String DSalary = rs.getString("Salary");
                String datetxt = DDate.toString();

                txtFirstName.setText(DFirstName);
                txtLastName.setText(DLastName);
                txtAge.setText(DAge);
                txtDate.setValue(LocalDate.parse(datetxt));
                txtPhone.setText(DPhone);
                txtGender.setText(Gender1);
                txtSpecialist.setText(DSpecialist);
                txtsalary.setText(DSalary);

            }

        }catch(Exception e){ System.out.println(e);}
    }

    
    public void doctorTable()throws IOException{

        
        String SQL = "select * from doctor";

       
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
                
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){   
                    
                    
                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {  
                        
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
            
            
          }catch(SQLException e){
              System.out.println("Error on Building Data");             
          }
      }



    

// Update code
    public void UpdateDoctor(ActionEvent even)throws Exception{

        
        
       
        String DId1 = txtDoctorId.getText();

        String DFirstName = txtFirstName.getText();
        String DLastName = txtLastName.getText();
        String DAge = txtAge.getText();
        LocalDate DDate = txtDate.getValue();
        String DPhone = txtPhone.getText();
        String Gender = "Male";
        String DSpecialist = txtSpecialist.getText();
        String DSalary = txtsalary.getText();
        String dateshow = DDate.toString();

        String query = "UPDATE doctor set FirstName = ?, LastName = ?, Age = ?, Date1 = ?,Phone = ? ,Gender = ?, Specialist = ?, Salary = ? where DId = ?";

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,DFirstName);
            prst.setString(2,DLastName);
            prst.setString(3,DAge);
            prst.setString(4,dateshow);
            prst.setString(5, DPhone);
            prst.setString(6,Gender);
            prst.setString(7,DSpecialist);
            prst.setString(8,DSalary);
            prst.setString(9,DId1);

            int i = prst.executeUpdate();
            if(i==1) {
                LoginMassageBoxController.display("UpDate Status", "UpDate Successfully");
            }
        }
        catch(Exception e){ System.out.println(e);}
    }


  //Delete code

    public void DeleteDoctor(ActionEvent even)throws Exception{

       
        String DId = txtDoctorId.getText();

        String query = "delete from doctor where DId = ?";

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,DId);
            int i = prst.executeUpdate();
            if(i==1)
            LoginMassageBoxController.display("Delete Status", "Delete Successfully");

        }
        catch(Exception e){ System.out.println(e);}
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



    //RoomTable Code

    public void RoomTable(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/Room.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }


    //StaffTable Code

    public void StaffTable(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/Staff.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }

}
