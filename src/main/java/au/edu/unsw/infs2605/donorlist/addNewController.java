/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package au.edu.unsw.infs2605.donorlist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author mac
 */

public class addNewController extends App {
    @FXML
    Label donorId;
    
    @FXML
    TextField firstNameInput;
    
    @FXML
    TextField lastNameInput;
    
    @FXML
    TextField bDayInput;
    
    @FXML
    ChoiceBox genderOption;
    
    @FXML
    ChoiceBox bloodTypeOption;
    
    @FXML
    TextField emailInput;
    
    @FXML
    TextField mobileNoInput;
    
    @FXML
    TextField addressInput;
    
    @FXML
    TextField notesInput;
    
    @FXML
    Label bloodDonationCount;
    
    @FXML
    Label plasmaDonationCount;
    
    @FXML 
    VBox vbox2;
    
    @FXML
    ListView<Label> mainListView;
    
    int bloodCount = 0;
    int plasmaCount = 0;
    
    @FXML
    private void addValueToList() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
	Statement st = conn.createStatement();
        String selectQuery = "SELECT donorId, firstName, lastName FROM Donor;"; 
	ResultSet rs = st.executeQuery(selectQuery);
        while (rs.next()){
            Label donor = new Label(rs.getInt(1) + " – " + rs.getString(2) + " " +rs.getString(3));
            donor.setId(String.valueOf(rs.getInt(1)));
            mainListView.getItems().add(donor);
        }
        st.close();
	conn.close(); 
    }
    
    @FXML
    private void clickListItem() {
        Label selected = mainListView.getSelectionModel().getSelectedItem();
        try{ 
            super.setId(Integer.parseInt(selected.getId()));
            goToDisplay();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public int getNewId() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
	Statement st = conn.createStatement();
        String selectQuery = "SELECT Count(donorId) FROM Donor;"; 
	ResultSet rs = st.executeQuery(selectQuery);
        int id = 0;
        while (rs.next()){
            id = rs.getInt(1);
        }
        st.close();
	conn.close(); 
        
        return id + 1; 
    }
    
    public void addNew() throws SQLException, IOException{
        int newId = getNewId();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(time);
        
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String insertStatement = "INSERT INTO Donor(donorId, firstName, lastName, bDay, gender, "
                    + "bloodType, email, mobileNo, address, "
                    + "notes, bloodDCount, plasmaDCount, timestamp)"
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement st = conn.prepareStatement(insertStatement);
        
        st.setInt(1, newId);
        st.setString(2, firstNameInput.getText());
        st.setString(3, lastNameInput.getText());
        st.setString(4, bDayInput.getText()); //need to change to date
        st.setString(5, genderOption.getValue().toString());
        st.setString(6, bloodTypeOption.getValue().toString());
        st.setString(7, emailInput.getText());
        st.setString(8, mobileNoInput.getText());
        st.setString(9, addressInput.getText());
        st.setString(10, notesInput.getText());
        st.setInt(11, Integer.parseInt(bloodDonationCount.getText()));
        st.setInt(12, Integer.parseInt(plasmaDonationCount.getText()));
        if (Integer.parseInt(bloodDonationCount.getText()) == 0 && Integer.parseInt(plasmaDonationCount.getText()) == 0){
            st.setString(13, "...");
        }else{
            st.setString(13, ts);
        }
        
        st.execute();
        super.setId(newId);
	
        st.close();
        conn.close();
        goToDisplay();
    }
    
    @FXML
    public void bAddition2() throws SQLException{
        bloodDonationCount.setText(String.valueOf(Integer.parseInt(bloodDonationCount.getText()) + 1));
    }
    
    @FXML
    public void pAddition2() throws SQLException{
        plasmaDonationCount.setText(String.valueOf(Integer.parseInt(plasmaDonationCount.getText()) + 1));  
    }
    
    @FXML
    public void bSubtraction2() throws SQLException{
        if (Integer.parseInt(bloodDonationCount.getText()) != 0){
        bloodDonationCount.setText(String.valueOf(Integer.parseInt(bloodDonationCount.getText()) - 1));
        }
    }
    
    @FXML
    public void pSubtraction2() throws SQLException{
        if (Integer.parseInt(plasmaDonationCount.getText()) != 0){
        plasmaDonationCount.setText(String.valueOf(Integer.parseInt(plasmaDonationCount.getText()) - 1));
        }
    }
    
    public void initialize() throws SQLException {
        addValueToList();
        donorId.setText(String.valueOf(getNewId()));
        genderOption.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        bloodTypeOption.setItems(FXCollections.observableArrayList("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"));
    }
    
    @FXML
    private void goToDisplay() throws IOException {
        App.setRoot("donorListDisplay");
    }
}
