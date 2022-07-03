package au.edu.unsw.infs2605.donorlist;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javafx.fxml.FXML;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
//import java.util.Date;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.scene.text.TextAlignment;
/**
 *
 * @author mac
 */

public class donorListEditController extends App{
    @FXML
    Label donorId;
    
    @FXML
    TextField firstNameInput;
    
    @FXML
    TextField lastNameInput;
    
    @FXML
    TextField bDayInput;
    
    @FXML
    TextField genderOption;
    
    @FXML
    TextField bloodTypeOption;
    
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
    Label firstName;
    
    @FXML
    Label lastName;
    
    @FXML
    Label bDay;
    
    @FXML
    Label gender;
    
    @FXML
    Label bloodType;
    
    @FXML
    Label email;
    
    @FXML
    Label mobileNo;
    
    @FXML
    Label address;
    
    @FXML
    Label notes;
    
    @FXML
    Label timestamp;
    
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
    
    public void defaultValue(int Id) throws SQLException, NullPointerException{
        
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String selectQuery = "SELECT * FROM Donor WHERE donorId = ?;";
        PreparedStatement st = conn.prepareStatement(selectQuery);
        st.setInt(1, Id);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()){
            donorId.setText(String.valueOf(Id));
            firstNameInput.setText(rs.getString(2));
            lastNameInput.setText(rs.getString(3));
            bDayInput.setText(rs.getString(4)); //need to change format
            genderOption.setText(rs.getString(5));
            bloodTypeOption.setText(rs.getString(6));
            emailInput.setText(rs.getString(7));
            mobileNoInput.setText(rs.getString(8));
            addressInput.setText(rs.getString(9));
            notesInput.setText(rs.getString(10));
            bloodDonationCount.setText(String.valueOf(rs.getInt(11)));
            plasmaDonationCount.setText(String.valueOf(rs.getInt(12)));
            timestamp.setText("Last edited at " + rs.getString(13));
        }
        
        st.close();
	conn.close(); 
    }
 
    public void saveFile() throws ParseException, IOException, SQLException{
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(time);
        
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String updateQuery = "UPDATE Donor SET firstName = ?, lastName = ?, bDay = ?, gender = ?, "
                + "bloodType = ?, email  = ?, mobileNo = ?, address = ?, notes = ? WHERE donorId = ?"; 
        PreparedStatement st = conn.prepareStatement(updateQuery);
        st.setInt(10, getId());
        st.setString(1, firstNameInput.getText());
        st.setString(2, lastNameInput.getText());
        st.setString(3, bDayInput.getText()); //need to change to date
        st.setString(4, genderOption.getText());
        st.setString(5, bloodTypeOption.getText());
        st.setString(6, emailInput.getText());
        st.setString(7, mobileNoInput.getText());
        st.setString(8, addressInput.getText());
        st.setString(9, notesInput.getText());
        
        st.executeUpdate();
        
        st.close();
        conn.close(); 
        goToDisplay();
    }
    
    public void updateTime() throws SQLException{
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(time);
        
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String updateQuery = "UPDATE Donor SET timestamp = ? WHERE donorId = ?"; 
        PreparedStatement st = conn.prepareStatement(updateQuery);
        st.setInt(2, getId());
        st.setString(1, ts);
        
        st.executeUpdate();
        
        st.close();
        conn.close(); 
        timestamp.setText("Last edited at " + ts);
    }
    
    public int getBloodCount() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String selectQuery = "SELECT bloodDCount FROM Donor WHERE donorId = ?;";
        PreparedStatement st = conn.prepareStatement(selectQuery);
        st.setInt(1, getId());
        ResultSet rs = st.executeQuery();
        
        while (rs.next()){
            bloodDonationCount.setVisible(true);
            bloodCount = rs.getInt(1);
        }
        st.close();
        conn.close(); 
        return bloodCount;
    }
    
    public int getPlasmaCount() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String selectQuery = "SELECT plasmaDCount FROM Donor WHERE donorId = ?;";
        PreparedStatement st = conn.prepareStatement(selectQuery);
        st.setInt(1, getId());
        ResultSet rs = st.executeQuery();
        
        while (rs.next()){
            plasmaDonationCount.setVisible(true);
            plasmaCount = rs.getInt(1);
        }
        st.close();
        conn.close(); 
        return plasmaCount;
    }
    
    @FXML
    public void bAddition2() throws SQLException{
        getBloodCount();
        bloodDonationCount.setText(String.valueOf(bloodCount + 1));
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String selectQuery = "UPDATE Donor SET bloodDCount = ? WHERE donorId = ?;";
        PreparedStatement st = conn.prepareStatement(selectQuery);
        st.setInt(2, getId());
        st.setInt(1, bloodCount + 1);
        st.executeUpdate();
        
        st.close();
        conn.close(); 
        
        updateTime();
    }
    
    @FXML
    public void pAddition2() throws SQLException{
        getPlasmaCount();
        plasmaDonationCount.setText(String.valueOf(plasmaCount + 1));
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String selectQuery = "UPDATE Donor SET plasmaDCount = ? WHERE donorId = ?;";
        PreparedStatement st = conn.prepareStatement(selectQuery);
        st.setInt(2, getId());
        st.setInt(1, plasmaCount + 1);
        st.executeUpdate();
        
        st.close();
        conn.close(); 
        
        updateTime();
    }
    
    @FXML
    public void bSubtraction2() throws SQLException{
        getBloodCount();
        if (bloodCount != 0){
            bloodDonationCount.setText(String.valueOf(bloodCount - 1));
            Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            String selectQuery = "UPDATE Donor SET bloodDCount = ? WHERE donorId = ?;";
            PreparedStatement st = conn.prepareStatement(selectQuery);
            st.setInt(2, getId());
            st.setInt(1, bloodCount - 1);
            st.executeUpdate();

            st.close();
            conn.close(); 
            updateTime();
        }
        
    }
    
    @FXML
    public void pSubtraction2() throws SQLException{
        getPlasmaCount();
        if (plasmaCount != 0){
            plasmaDonationCount.setText(String.valueOf(plasmaCount - 1));
            Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            String selectQuery = "UPDATE Donor SET plasmaDCount = ? WHERE donorId = ?;";
            PreparedStatement st = conn.prepareStatement(selectQuery);
            st.setInt(2, getId());
            st.setInt(1, plasmaCount - 1);
            st.executeUpdate();

            st.close();
            conn.close(); 
            updateTime();
        }
        
    }
    
    @FXML
    public void displayValue(int Id) throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
        String selectQuery = "SELECT * FROM Donor WHERE donorId = ?;";
        PreparedStatement st = conn.prepareStatement(selectQuery);
        st.setInt(1, Id);
        ResultSet rs = st.executeQuery();
        
        while (rs.next()){
            donorId.setText(String.valueOf(Id));
            firstName.setText(rs.getString(2));
            lastName.setText(rs.getString(3));
            bDay.setText(rs.getString(4)); //need to change format
            gender.setText(rs.getString(5));
            bloodType.setText(rs.getString(6));
            email.setText(rs.getString(7));
            mobileNo.setText(rs.getString(8));
            address.setText(rs.getString(9));
            notes.setText(rs.getString(10));
            bloodDonationCount.setText(String.valueOf(rs.getInt(11)));
            plasmaDonationCount.setText(String.valueOf(rs.getInt(12)));
        }
        st.close();
	conn.close();
    }

    public void initialize() throws SQLException {
        defaultValue(super.getId());
        addValueToList();
    }

    @FXML
    private void goToDisplay() throws IOException {
        App.setRoot("donorListDisplay");
    }
     
    @FXML
    private void goToAdd() throws IOException {
        App.setRoot("donorListAdd");
    }
}

