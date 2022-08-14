package MVC_Structure.controller;
import MVC_Structure.model.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class FinanceController {
    
    @FXML
    private TableView costTableView;
    @FXML
    private TextField costField;
    
    final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();


 
    
    
    DbConnector db = new DbConnector();
        public Connection con;
        PreparedStatement prst = null;
        ResultSet rs = null;
        
        
        
    public String costTable()throws IOException{
        String sum="";
        String sum2="";
        Statement stmt;

        
        String SQL = "select FirstName, Date3, Cost from patient";
        String SQL2 = "select SUM(Cost) from patient";
        try{
            con= db.getConnection();
            
            stmt=con.createStatement();
            prst = con.prepareStatement(SQL2);
           
            rs = prst.executeQuery();
            rs.next();
            sum2 = rs.getString(1);
            System.out.println(sum2);
           
        }
        catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");             
          }

        try{
                costTableView.getItems().clear();
                costTableView.getColumns().clear();
            
            
                con= db.getConnection();
            stmt=con.createStatement();
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

                costTableView.getColumns().addAll(col); 
                
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
            costTableView.setItems(data);
            
        
        SQL = "select SUM(Cost) from patient";
            con= db.getConnection();
            stmt=con.createStatement();
            prst = con.prepareStatement(SQL);
            //prst.setString(2,FirstName);
            rs = prst.executeQuery();
            rs.next();
            sum = rs.getString(1);
            System.out.println(sum);
            costField.setText(sum);
        
        
            
          }catch(Exception e){
              e.printStackTrace();
              System.out.println("Error on Building Data");             
          }
        
        
        return sum2;
      }


    //Back Code

    public void Backmain(ActionEvent even) throws IOException {
        ((Node)even.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/MVC_Structure/view/ReceptionPanel.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root, 720, 500));
        primaryStage.show();
    }
    
    
    


}
