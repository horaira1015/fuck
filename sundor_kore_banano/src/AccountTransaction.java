
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import java.sql.Statement;
import java.time.LocalDate;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AccountTransaction implements Initializable {

	@FXML
	Label username;
	@FXML
	Label id;
	@FXML
	Label balance;
	@FXML
	TextField deposit;
	@FXML
	TextField withdraw;
	@FXML
	TextField fname;
	@FXML
	TextField mname;
	@FXML
	TextField lname;
	@FXML
	TextField phoneno;
	@FXML
	TextField email;
	@FXML
	DatePicker dob;
	@FXML
	ComboBox<String> gender;
	@FXML
	TextField tId;
	@FXML
	TextField tAmount;

	Customer c;
	Account a;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			c = CustomerLoginController.downloadObj(CustomerLoginController.customername);
			a = CustomerAccountSelectionCon.downloadAccount(CustomerAccountSelectionCon.acnumber);
			username.setText(c.getUsername());
			id.setText(CustomerAccountSelectionCon.acnumber + "");
			balance.setText(a.balance + "");
			fname.setText(c.getFname());
			lname.setText(c.getLname());
			phoneno.setText(c.getPhoneno());
			email.setText(c.getEmail());
			dob.setValue(LocalDate.parse(c.getDob()));
			gender.setItems(FXCollections.observableArrayList("Male", "Female"));
			gender.setValue(c.getGender());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	//Deposit
	public void dAdd(float rs) {
		deposit.setText(Float.toString(Float.parseFloat(deposit.getText()) + rs));
	}

	public void dAdd100() {
		dAdd(100);
	}

	public void dAdd500() {
		dAdd(500);
	}

	public void dAdd1000() {
		dAdd(1000);
	}

	public void dAdd2000() {
		dAdd(2000);
	}

	public void dAdd5000() {
		dAdd(5000);
	}

	public void dAdd10000() {
		dAdd(10000);
	}

	public void reset() {
		deposit.setText(0 + "");
		withdraw.setText(0 + "");
	}

	public void dProceed() throws SQLException {
		float depositMoney = Float.parseFloat(deposit.getText());
		float total = c.getBalance() + depositMoney;
		String query = "UPDATE customerall SET `Balance` = '" + total + "' WHERE (`username` = '" + CustomerLoginController.customername+ "');";
		Statement st = Bank.con.createStatement();
		int i = st.executeUpdate(query);
		total = a.balance+depositMoney;
		query = "UPDATE useraccounts SET `balance` = '" + total + "' WHERE (`accountNumber` = '" + CustomerAccountSelectionCon.acnumber + "');";
		st.executeUpdate(query);
		query = "insert transactions (AccountNumber, CustomerName, Transactiontype,Amount,ToAccountNumber) values (?,?,?,?,?)";
		PreparedStatement pstmt = Bank.con.prepareStatement(query);

		// Set the variable values using the appropriate setter methods
		pstmt.setInt(1, a.acnumber); // Assuming accountNumber is an int variable
		pstmt.setString(2, c.getUsername()); // Assuming customerName is a String variable
		pstmt.setString(3, "deposit"); // Assuming transactionType is a String variable
		pstmt.setInt(4, (int)depositMoney); // Assuming amount is a double variable
		pstmt.setInt(5, a.acnumber); // Assuming toAccountNumber is an int variable

		// Execute the INSERT query
		pstmt.executeUpdate();

		// Close the PreparedStatement
		pstmt.close();
		if (i == 1) {
			String getBalance = "SELECT balance FROM useraccounts where accountnumber=" + CustomerAccountSelectionCon.acnumber + ";";
			ResultSet rs = st.executeQuery(getBalance);
			rs.next();
			float dbBalance = rs.getFloat(1);
			System.out.println(dbBalance);
			a.depo(depositMoney);
			balance.setText(dbBalance + "");
			System.out.println(balance.getText());
			deposit.setText(0 + "");
		} else {
			System.out.println("problem");
		}
		showAlert(Alert.AlertType.INFORMATION, "Information", "Deposit Transaction Completed", "Thank You");
		new Bank().changeScene("AccountTransactionHere.fxml", "Option Menu", 720, 530);
	}

	public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	//Withdraw
	public void wAdd(float rs) {
		withdraw.setText(Float.toString(Float.parseFloat(withdraw.getText()) + rs));

	}

	public void wAdd100() {
		wAdd(100);
	}

	public void wAdd500() {
		wAdd(500);
	}

	public void wAdd1000() {
		wAdd(1000);
	}

	public void wAdd2000() {
		wAdd(2000);
	}

	public void wAdd5000() {
		wAdd(5000);
	}

	public void wAdd10000() {
		wAdd(10000);
	}

	public void wProceed() throws SQLException {
		float withdrawMoney = Float.parseFloat(withdraw.getText());
		if (withdrawMoney > a.balance) {
			System.out.println("not enough");
			showAlert(Alert.AlertType.INFORMATION, "Information", "Withdraw Transaction Not Completed, Low Balance", "Thank You");
			return;
		}
		float total = c.getBalance() - withdrawMoney;
		String query = "UPDATE customerall SET `Balance` = '" + total + "' WHERE (`username` = '" + CustomerLoginController.customername + "');";
		Statement st = Bank.con.createStatement();
		int i = st.executeUpdate(query);
		total = a.balance-withdrawMoney;
		query = "UPDATE useraccounts SET `Balance` = '" + total + "' WHERE (`accountNumber` = '" + CustomerAccountSelectionCon.acnumber + "');";
		System.out.println(query);
		st.executeUpdate(query);
		query = "insert into transactions (AccountNumber, CustomerName, Transactiontype,Amount,ToAccountNumber) values (?,?,?,?,?)";
		PreparedStatement pstmt = Bank.con.prepareStatement(query);

		// Set the variable values using the appropriate setter methods
		pstmt.setInt(1, a.acnumber); // Assuming accountNumber is an int variable
		pstmt.setString(2, c.getUsername()); // Assuming customerName is a String variable
		pstmt.setString(3, "withdraw"); // Assuming transactionType is a String variable
		pstmt.setInt(4,(int) withdrawMoney); // Assuming amount is a double variable
		pstmt.setInt(5, a.acnumber); // Assuming toAccountNumber is an int variable

		// Execute the INSERT query
		pstmt.executeUpdate();

		// Close the PreparedStatement
		pstmt.close();
		if (i == 1) {
			String getBalance = "SELECT Balance FROM useraccounts where accountNumber=" + CustomerAccountSelectionCon.acnumber + ";";
			ResultSet rs = st.executeQuery(getBalance);
			rs.next();
			float dbBalance = rs.getFloat(1);
			System.out.println(dbBalance);
			balance.setText(dbBalance + "");
			System.out.println(balance.getText());

			a.withdraw(withdrawMoney);

			withdraw.setText(0 + "");
		} else {
			System.out.println("problem");
		}
		showAlert(Alert.AlertType.INFORMATION, "Information", "Withdrow Transaction Completed", "Thank You");
		new Bank().changeScene("AccountTransactionHere.fxml", "Option Menu", 720, 530);
	}

	//Money Transfer
	public void transferMoney() throws SQLException {
		int payeeAcNumber = Integer.parseInt(tId.getText());
		float amount = Float.parseFloat(tAmount.getText());
		if (amount > a.balance) {
			System.out.println("not enough");
			showAlert(Alert.AlertType.INFORMATION, "Information", "Transfer Transaction Not Completed, Low Balance", "Thank You");
			return;
		}
		Statement fx = Bank.con.createStatement();
		String getPayeeName = "SELECT username FROM useraccounts where accountnumber=" + payeeAcNumber + ";";
		ResultSet rs2 = fx.executeQuery(getPayeeName);
		rs2.next();
		String payeeName = rs2.getString(1);

		Account payeeAccount = CustomerAccountSelectionCon.downloadAccount(payeeAcNumber);

		String query1 = "UPDATE useraccounts SET `Balance` = `Balance` - " + amount + " WHERE (`accountNumber` = '" + CustomerAccountSelectionCon.acnumber + "');";
		String query2 = "UPDATE useraccounts SET `Balance` = `Balance` + " + amount + " WHERE (`accountNumber` = '" + payeeAcNumber + "');";
		Statement st = Bank.con.createStatement();
		st.addBatch(query1);
		st.addBatch(query2);

		String query3 = "UPDATE customerall SET `Balance` = `Balance` - " + amount + " WHERE (`username` = '" + CustomerLoginController.customername + "');";
		String query4 = "UPDATE customerall SET `Balance` = `Balance` + " + amount + " WHERE (`username` = '" + payeeName + "');";
		st.addBatch(query3);
		st.addBatch(query4);
		int[] i = st.executeBatch();
		String query = "insert transactions (AccountNumber, CustomerName, Transactiontype,Amount,ToAccountNumber) values (?,?,?,?,?)";
		PreparedStatement pstmt = Bank.con.prepareStatement(query);

		// Set the variable values using the appropriate setter methods
		pstmt.setInt(1, a.acnumber); // Assuming accountNumber is an int variable
		pstmt.setString(2, c.getUsername()); // Assuming customerName is a String variable
		pstmt.setString(3, "transfer"); // Assuming transactionType is a String variable
		pstmt.setInt(4, (int)amount); // Assuming amount is a double variable
		pstmt.setInt(5, payeeAcNumber); // Assuming toAccountNumber is an int variable

		// Execute the INSERT query
		pstmt.executeUpdate();

		// Close the PreparedStatement
		pstmt.close();
		if (i[1] == 1) {
			System.out.println("Payment done");
			String getBalance = "SELECT Balance FROM useraccounts where accountNumber=" + CustomerAccountSelectionCon.acnumber + ";";
			ResultSet rs = st.executeQuery(getBalance);
			rs.next();
			float dbBalance = rs.getFloat(1);
			balance.setText(dbBalance + "");
			tId.clear();
			tAmount.clear();
			payeeAccount.depo(amount);
			a.withdraw(amount);
		} else {
			System.out.println("Payment not done");
		}
		showAlert(Alert.AlertType.INFORMATION, "Information", "Transfer Transaction Completed", "Thank You");

	}

	//Change Details
	public void update() throws SQLException {
		Statement st = Bank.con.createStatement();
		String query = "UPDATE customerall SET `First_name` = '" + fname.getText() + "', `Last_name` = '" + lname.getText() + "', `Phone_no` = '" + phoneno.getText() + "', `Email` = '" + email.getText() + "', `DOB` = '" + dob.getValue().toString() + "' WHERE (`Id` = '" + id.getText() + "');";
		int i = st.executeUpdate(query);
		if (i == 1) System.out.println("Updated");
		else System.out.println("Not updated");
		showAlert(Alert.AlertType.INFORMATION, "Information", "Update Completed", "Thank You");

	}

	//sign out
	public void signOut() {
		new Bank().changeScene("CustomerAccountSelection.fxml", "select AC", 663, 432);
	}
}
