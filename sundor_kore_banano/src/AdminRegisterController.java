import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminRegisterController implements Initializable{
	@FXML
	TextField fname;
	@FXML
	TextField lname;
	@FXML
	TextField username;
	@FXML
	PasswordField password;
	@FXML
	TextField phoneno;
	@FXML
	TextField email;
	@FXML
	DatePicker dob;
	@FXML
	ComboBox<String> gender;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		Customer c=StartScreen.customer;
//		username.setText(c.getUsername());
//		password.setText(c.getPassword());
//		email.setText(c.getEmail());
		gender.setItems(FXCollections.observableArrayList("Male","Female"));
	}

	public void back() {
		new Bank().changeScene("adminLogin.fxml", "Admin - Login", 553, 457);
	}

	public void submit() throws SQLException {
		//System.out.println(gender.getSelectionModel().getSelectedItem());
		Admin c=new Admin(username.getText(), password.getText(), email.getText(), fname.getText(), lname.getText(), phoneno.getText(),dob.getValue().toString(), gender.getSelectionModel().getSelectedItem());
		objUpload(c);
		new Bank().changeScene("adminLogin.fxml", "Admin - Login", 553, 457);
	}

	public void objUpload(Admin c) throws SQLException {
		String query1 = " INSERT INTO adminAll VALUES ('"+c.getUsername()+"','"+c.getFname()+"','"+c.getLname()+"','"+c.getPhoneno()+"','"+c.getEmail()+"','"+c.getDob()+"','"+c.getGender()+"');";
		PreparedStatement ps=Bank.con.prepareStatement(query1);
		int i=ps.executeUpdate();
		if(i==1)System.out.println("Success1");
		else System.out.println("Failure1");
		String query2=" INSERT INTO admin VALUES ('"+c.getUsername()+"','"+c.getPassword()+"');";
		ps=Bank.con.prepareStatement(query2);
		int j=ps.executeUpdate();
		if(j==1)System.out.println("Success2");
		else System.out.println("Failure2");
	}
}
