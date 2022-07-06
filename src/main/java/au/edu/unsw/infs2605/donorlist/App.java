package au.edu.unsw.infs2605.donorlist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.fxml.FXML;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * JavaFX App
 */
public class App extends Application {
   
    private static Scene scene;
    private static int Id = 1;
    
    public void setId(int Id){
        this.Id = Id;
    }
    public int getId(){
        return Id;
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("DonorList");
        scene = new Scene(loadFXML("donorListDisplay"), 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        root.setStyle("-fx-font-family: serif");
        return root;
    }
    
    public static void connect() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");

		Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
		Statement st = conn.createStatement();
		String createQuery = "CREATE TABLE IF NOT EXISTS Donor" 
                                + "( donorId INT PRIMARY KEY"
                                + ", firstName TEXT NOT NULL"
                                + ", lastName TEXT NOT NULL"
                                + ", bDay TEXT NOT NULL"
                                + ", gender TEXT NOT NULL"
                                + ", bloodType TEXT NOT NULL"
                                + ", email TEXT NOT NULL"
                                + ", mobileNo TEXT NOT NULL"
                                + ", address TEXT NOT NULL"
                                + ", notes TEXT NOT NULL"
                                + ", bloodDCount INT NOT NULL"
                                + ", plasmaDCount INT NOT NULL"
                                + ", timestamp TEXT NOT NULL"
                                + ")";
		st.execute(createQuery);
		st.close(); 
		conn.close();      
    }
    
    public static void insertDonors() throws SQLException{
		Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
		
		Statement st = conn.createStatement();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Timestamp time = new Timestamp(System.currentTimeMillis());
                String ts = sdf.format(time);

		ArrayList<String> insertStatements = new ArrayList<>();
		insertStatements.add(
			"INSERT INTO Donor(donorId, firstName, lastName, bDay, gender, "
                                + "bloodType, email, mobileNo, address, "
                                + "notes, bloodDCount, plasmaDCount, timestamp)"
			+ "VALUES(000001, 'Monica', 'Geller', '17/03/1978', 'Female',"
                                + "  'A+', 'monica.g@gmail.com', '+61434529576', 'New York, USA', "
                                + "'N/A', 2, 0, '"+ts+"');"
		);
		insertStatements.add(
			"INSERT INTO Donor(donorId, firstName, lastName, bDay, gender, "
                                + "bloodType, email, mobileNo, address, "
                                + "notes, bloodDCount, plasmaDCount, timestamp)"
			+ "VALUES(000002, 'Ross', 'Geller', '23/06/1976', 'Male',"
                                + "  'O+', 'ross.g@gmail.com', '+61463453423', 'New York, USA', "
                                + "'N/A', 3, 1, '"+ts+"');"
		);
		insertStatements.add(
			"INSERT INTO Donor(donorId, firstName, lastName, bDay, gender,"
                                + " bloodType, email, mobileNo, address, "
                                + "notes, bloodDCount, plasmaDCount, timestamp)"
			+ "VALUES(000003, 'Phoebe', 'Buffay', '08/05/1974', 'Female',"
                                + "  'B+', 'smellycatg@gmail.com', '+61427394342', 'Sydney, Australia',"
                                + " 'vegetarian', 5, 2, '"+ts+"');"
		);
		insertStatements.add(
			"INSERT INTO Donor(donorId, firstName, lastName, bDay, gender,"
                                + " bloodType, email, mobileNo, address, "
                                + "notes, bloodDCount, plasmaDCount, timestamp)"
			+ "VALUES(000004, 'Joey', 'Tribbiani', '21/08/1980', 'Male',"
                                + "  'A-', 'joey.t@gmail.com', '+61421235827', 'Wynyard, Ausralia',"
                                + " 'Allergies: peanut', 1, 0, '"+ts+"');"
		);
		
		for (String thisStatement : insertStatements) {
			st.execute(thisStatement);
                       
		}
                
		st.close();
		conn.close(); 
    }
    
    public static void drop() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
		Statement st = conn.createStatement();
		String dropQuery = "DROP TABLE IF EXISTS Donor ";
		st.execute(dropQuery);
		st.close(); 
		conn.close();
    }

    public static void main(String[] args) throws  ClassNotFoundException, SQLException{
        drop();
        connect();
        insertDonors();
        Application.launch(args);
    }

}