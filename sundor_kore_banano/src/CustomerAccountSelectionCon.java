import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerAccountSelectionCon implements Initializable {

	@FXML
	public Label TotalAccount;
	@FXML
	public Label Fullbalance;
	@FXML
	TableView tableView;
	@FXML
	Label username;
	@FXML
	TextField acNumber;
	Customer c;
	static int acnumber;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		int countAccounts = 0;
		float countBalance = 0;
		try {
			System.out.println("ff");
			c = CustomerLoginController.downloadObj(CustomerLoginController.customername);
			System.out.println(c);
			username.setText(c.getUsername());
			countAccounts = 0;
			String selectQuery = "SELECT accountNumber, balance FROM useraccounts where username='" + CustomerLoginController.customername + "';";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();
			countBalance = 0;
			tableView.getColumns().clear(); // Clear existing columns

			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				//countBalance += rs.getInt(2);
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(tableView.widthProperty().divide(2));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				tableView.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				countAccounts++;
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					if (i == 2) { // Check if it's the balance column
						countBalance += rs.getFloat(i); // Add balance to countBalance
					}
					row.add(rs.getString(i));
				}
				data.add(row);
			}

			tableView.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TotalAccount.setText(String.valueOf(countAccounts));
		Fullbalance.setText(String.valueOf(countBalance));
	}

	public static Account downloadAccount(int id) throws SQLException {

		String query2 = "Select * from useraccounts where accountNumber='" + id + "';";
		Statement st = Bank.con.createStatement();
		st.executeQuery(query2);
		ResultSet rs = st.executeQuery(query2);
		rs.next();
		int accountNumber = rs.getInt(2);
		int balance = rs.getInt(3);
		Account c = new Account(accountNumber, balance, CustomerLoginController.downloadObj(CustomerLoginController.customername));
		return c;
	}

	public void enterAccount(ActionEvent actionEvent) {
		acnumber = Integer.parseInt(acNumber.getText());
		new Bank().changeScene("AccountTransactionHere.fxml", "accountPage", 720, 530);

	}

	public void signout(ActionEvent actionEvent) {
		new Bank().changeScene("CustomerLogin.fxml", "Customer - Login", 553, 457);
	}

	public void makeNewAccount(ActionEvent actionEvent) throws SQLException {
		Statement st = Bank.con.createStatement();
		String query = "INSERT INTO useraccounts (username, balance) VALUES ('" + CustomerLoginController.customername + "', 0)";
		int i = st.executeUpdate(query);
		if (i == 1) System.out.println("Updated");
		else System.out.println("Not updated");
		//showAlert(Alert.AlertType.INFORMATION, "Information", "Update Completed", "Thank You");

		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 663, 432);
	}
}
