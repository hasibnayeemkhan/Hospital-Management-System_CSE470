package MVC_Structure.controller;

import MVC_Structure.controller.LoginMassageBoxController;
import MVC_Structure.model.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;



public class RoomPanelController {

    @FXML
    private TextField txtRoomNo;

    @FXML
    private TextField txtProblemType;

    @FXML
    private TextField txtDoctorId;

    @FXML
    private TextField txtDoctorName;

    @FXML
    private TextField txtStaffId;

    @FXML
    private TextField txtStaffName;

    @FXML
    private TextField txtStaffWorkName;
    @FXML
    private TableView tableview;
    final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
    
    
        DbConnector db = new DbConnector();
        public Connection con;
        PreparedStatement prst = null;
        ResultSet rs = null;

    //Add code

    public boolean AddRoom()throws IOException {
        boolean added=false;
     
//        String RId = txtRoomNo.getText();
//        String RProblemType = txtProblemType.getText();
//        String RDoctorId = txtDoctorId.getText();
//        String RDoctorName = txtDoctorName.getText();
//        String RStaffId = txtStaffId.getText();
//        String RStaffName = txtStaffName.getText();
//        String RStaffWorkType = txtStaffWorkName.getText();
        
        
        String RId = "7";
        String RProblemType = "something";
        String RDoctorId = "2";
        String RDoctorName = "Mush";
        String RStaffId = "1";
        String RStaffName = "sumi";
        String RStaffWorkType = "nurse";

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();

            String CheckQuery = "select RId from room";

            prst = con.prepareStatement(CheckQuery);
            rs = prst.executeQuery();
            while (rs.next()){
                String Rid = rs.getString("RId");
                if(Rid.equals(RId)){
                   // LoginMassageBoxController.display("Insert Status", "This Id Already Use So try to another Id...");
                }
            }
            

            stmt.executeUpdate("INSERT INTO room VALUES ('" + RId + "','" + RProblemType + "','" + RDoctorId + "','" + RDoctorName + "','" + RStaffId + "','" + RStaffName + "','" + RStaffWorkType + "')");

            added=true;
            //LoginMassageBoxController.display("Insert Status", "Insert Successfully");


            con.close();

        }catch(Exception e){ System.out.println(e);}
        return added;
    }
    
    public void roomTable()throws IOException{

       
        String SQL = "select * from room";

       
        try{
                tableview.getItems().clear();
                tableview.getColumns().clear();
            
            
            con= db.getConnection();
            Statement stmt = con.createStatement();
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




    //Search code

    public void SearchRoom(ActionEvent even)throws IOException{

       
        String Id = null, FirstName = null;
        Id = txtRoomNo.getText();
        //FirstName = txtFirstName.getText();

        String query = "select RId, ProblemType, DId, DoctorName, SId, StaffName, StaffWorkType from room where  RId = ?  ";

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,Id);
            //prst.setString(2,FirstName);
            rs = prst.executeQuery();

            while(rs.next()){

                //    String DId = rs.getString("DId");
                String ProblemType = rs.getString("ProblemType");
                String DId = rs.getString("DId");
                String DoctorName = rs.getString("DoctorName");
                String SId = rs.getString("SId");
                String StaffName = rs.getString("StaffName");
                String StaffWorkType = rs.getString("StaffWorkType");

                txtProblemType.setText(ProblemType);
                txtDoctorId.setText(DId);
                txtDoctorName.setText(DoctorName);
                txtStaffId.setText(SId);
                txtStaffName.setText(StaffName);
                txtStaffWorkName.setText(StaffWorkType);

            }

        }catch(Exception e){ System.out.println(e);}
    }




    // Update code


    public void UpdateRoom(ActionEvent even)throws Exception{

        
        String RId = txtRoomNo.getText();
        String ProblemType = txtProblemType.getText();
        String DId = txtDoctorId.getText();
        String DoctorName = txtDoctorName.getText();
        String SId = txtStaffId.getText();
        String StaffName = txtStaffName.getText();
        String StaffWorkType = txtStaffWorkName.getText();


        String query = "UPDATE room set ProblemType = ?, DId = ?, DoctorName = ?, SId = ?,StaffName = ? ,StaffWorkType = ? where RId = ?";

        try{
            
            con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,ProblemType);
            prst.setString(2,DId);
            prst.setString(3,DoctorName);
            prst.setString(4,SId);
            prst.setString(5, StaffName);
            prst.setString(6,StaffWorkType);
            prst.setString(7,RId);

            int i = prst.executeUpdate();
            if(i==1) {
                LoginMassageBoxController.display("UpDate Status", "UpDate Successfully");
            }
        }
        catch(Exception e){ System.out.println(e);}
    }





    //Delete code

    public void DeleteRoom(ActionEvent even)throws Exception{

       
        String RId = txtRoomNo.getText();

        String query = "delete from room where RId = ?";

        try{
            con= db.getConnection();
            Statement stmt=con.createStatement();
            prst = con.prepareStatement(query);
            prst.setString(1,RId);
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



    //DoctorTable Code

    public void DoctorTable(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/Doctor.fxml"));
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
