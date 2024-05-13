import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class registerController implements Initializable{
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
		gender.setItems(FXCollections.observableArrayList("Male","Female"));
	}

	public void back() {
		new Bank().changeScene("adminLogin.fxml", "Admin - Login", 553, 457);
	}

	public void submit() throws SQLException {
		//System.out.println(gender.getSelectionModel().getSelectedItem());
		Customer c=new Customer(username.getText(), password.getText(), email.getText(), fname.getText(), lname.getText(), phoneno.getText(),dob.getValue().toString(), gender.getSelectionModel().getSelectedItem());
		objUpload(c);
		new Bank().changeScene("CustomerLogin.fxml", "Customer - Login", 553, 457);
	}

	public void objUpload(Customer c) throws SQLException {
		String query1 = " INSERT INTO customerAll VALUES ('"+c.getUsername()+"','"+c.getFname()+"','"+c.getLname()+"','"+c.getPhoneno()+"','"+c.getEmail()+"','"+c.getDob()+"','"+c.getGender()+"' ,"+0+" );";
		PreparedStatement ps=Bank.con.prepareStatement(query1);
		int i=ps.executeUpdate();
		if(i==1)System.out.println("Success1");
		else System.out.println("Failure1");
		String query2=" INSERT INTO customer VALUES ('"+c.getUsername()+"','"+c.getPassword()+"');";
		ps=Bank.con.prepareStatement(query2);
		int j=ps.executeUpdate();
		if(j==1)System.out.println("Success2");
		else System.out.println("Failure2");
	}
}
