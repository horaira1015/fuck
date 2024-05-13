import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Bank extends Application {
	public static Connection con;
	static Stage pstage;
	public static void main(String[] args) throws SQLException {
		connectDB("jdbc:mysql://localhost:3306/aproject","root","12345678"); //connectDB(url,user,password);
		launch(args);
	}
	public static void connectDB(String url,String user,String pass) throws SQLException { //Connecting to database at the start of the program
		con= DriverManager.getConnection(url, user, pass);
		System.out.println(con);
	}

	public void changeScene(String gui, String title, int width, int height) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(gui));//loading FXML file
			Scene scene = new Scene(root, width, height);  //setting width and height of the window //loading CSS file if used
			pstage.setScene(scene);  // scene=content inside window
			pstage.setTitle(title); //Title of the window
			pstage.setResizable(false); //Making window non resizable
			pstage.show();  //Showing window to user
		} catch (Exception e) {
			e.printStackTrace();  //Printing Exception
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		pstage=primaryStage;
		changeScene("openingPage.fxml","Start Screen",651,422);
	}
}