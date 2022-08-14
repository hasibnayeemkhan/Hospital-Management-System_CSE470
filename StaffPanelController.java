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

public class StaffPanelController {

    @FXML
    private TextField txtSId;

    @FXML
    private TextField txtSFirstName;

    @FXML
    private TextField txtSLastName;

    @FXML
    private TextField txtSPhone;

    @FXML
    private TextField txtSAge;

    @FXML
    private DatePicker txtSDate;

    @FXML
    private TextField txtSWorkType;

    @FXML
    private TextField txtSSalary;

    @FXML
    private TextField txtSinoutTime;
    final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
    @FXML
    private TableView tableview;
    
    DbConnector db = new DbConnector();
        public Connection con;
        PreparedStatement prst = null;
        ResultSet rs = null;


    public void AddStaff(ActionEvent even)throws IOException{

        

        String SId = txtSId.getText();
        String SFirstName = txtSFirstName.getText();
        String SLastName = txtSLastName.getText();
        String SAge = txtSAge.getText();
        LocalDate SDate = txtSDate.getValue();
        String SPhone = txtSPhone.getText();
       // String Gender = "Male";
        String WorkType = txtSWorkType.getText();
        String SSalary = txtSSalary.getText();
        String InOutTime = txtSinoutTime.getText();

        try{
            con= db.getConnection(); 
            Statement stmt=con.createStatement();

            String CheckQuery = "select SId from staff";

            prst = con.prepareStatement(CheckQuery);
            rs = prst.executeQuery();
            while (rs.next()){
                String Sid = rs.getString("SId");
                if(Sid.equals(SId)){
                    LoginMassageBoxController.display("Insert Status", "This Id Already Use So try to another Id...");
                }
            }

            stmt.executeUpdate("INSERT INTO staff VALUES ('"+SId+"','"+SFirstName+"','"+SLastName+"','"+SAge+"','"+SDate+"','"+SPhone+"','"+ " Male" +"','"+WorkType+"','"+SSalary+"', '"+InOutTime+"' )");

            LoginMassageBoxController.display("Insert Status", "Insert Successfully");

            con.close();

        }catch(Exception e){ System.out.println(e);}
    }


    //Search code

    public void SearchStaff(ActionEvent even)throws IOException{

        
        String Id = null, FirstName = null;
        Id = txtSId.getText();
       // FirstName = txtFirstName.getText();


        String query = "select SId, FirstName, LastName, Age, Date2, Phone, Gender, WorkType, Salary, InoutTime from staff where  SId = ?  ";


        try{
            

             con= db.getConnection();
            String CheckQuery = "select SId from staff";

             prst = con.prepareStatement(CheckQuery);
             rs = prst.executeQuery();
             while (rs.next()){
                 String Sid = rs.getString("SId");

                 if(Sid.equals(Id)) {


             Statement stmt = con.createStatement();
             prst = con.prepareStatement(query);
             prst.setString(1, Id);
             //prst.setString(2,FirstName);
             rs = prst.executeQuery();

             while (rs.next()) {

                 //    String SId = rs.getString("SId");
                 String SFirstName = rs.getString("FirstName");
                 String SLastName = rs.getString("LastName");
                 String SAge = rs.getString("Age");
                 Date SDate = rs.getDate("Date2");
                 String SPhone = rs.getString("Phone");
                 String SGender1 = rs.getString("Gender");
                 String SWorkType = rs.getString("WorkType");
                 String SSalary = rs.getString("Salary");
                 String SInoutTime = rs.getString("InoutTime");
                 String datetxt = SDate.toString();

                 txtSFirstName.setText(SFirstName);
                 txtSLastName.setText(SLastName);
                 txtSAge.setText(SAge);
                 txtSDate.setValue(LocalDate.parse(datetxt));
                 txtSPhone.setText(SPhone);
                 // txt.setText(Gender1);
                 txtSWorkType.setText(SWorkType);
                 txtSSalary.setText(SSalary);
                 txtSinoutTime.setText(SInoutTime);

             }
         }
          
            }

        }catch(Exception e){ System.out.println(e);}
    }
    
     public void staffTable()throws IOException{

        
        String SQL = "select * from staff";

       
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







    //Delete code

    public void DeleteStaff(ActionEvent even)throws Exception{

        
        String SId = txtSId.getText();

        String query = "delete from staff where SId = ?";

        try{
            
             con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,SId);
            int i = prst.executeUpdate();
            if(i==1)
                LoginMassageBoxController.display("Delete Status", "Delete Successfully");

        }
        catch(Exception e){ System.out.println(e);}
    }



    // Update code


    public void UpdateStaff(ActionEvent even)throws Exception{

        
        String SId1 = txtSId.getText();

        String SFirstName = txtSFirstName.getText();
        String SLastName = txtSLastName.getText();
        String SAge = txtSAge.getText();
        LocalDate SDate = txtSDate.getValue();
        String SPhone = txtSPhone.getText();
        String Gender = "Male";
        String SWorkType = txtSWorkType.getText();
        String SSalary = txtSSalary.getText();
        String SInoutTime = txtSinoutTime.getText();
        String dateshow = SDate.toString();

        String query = "UPDATE staff set FirstName = ?, LastName = ?, Age = ?, Date2 = ?,Phone = ? ,Gender = ?, WorkType = ?, Salary = ?, InoutTime = ? where SId = ?";

        try{
            
             con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,SFirstName);
            prst.setString(2,SLastName);
            prst.setString(3,SAge);
            prst.setString(4,dateshow);
            prst.setString(5, SPhone);
            prst.setString(6,Gender);
            prst.setString(7,SWorkType);
            prst.setString(8,SSalary);
            prst.setString(9,SInoutTime);
            prst.setString(10,SId1);

            int i = prst.executeUpdate();
            if(i==1) {
                LoginMassageBoxController.display("UpDate Status", "UpDate Successfully");
            }
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


    //DoctorTable Code

    public void DoctorTable(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/Doctor.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }


}
