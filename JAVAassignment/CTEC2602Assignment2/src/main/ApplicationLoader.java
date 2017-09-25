package main;
/**
 * @author Laurynas Mykolaitis, P1415604x
 */

import controller.WindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.StudentProfile;
import view.WindowRootPane;

public class ApplicationLoader extends Application {

	private WindowRootPane view;

	@Override
	public void init() {
		//create model and view and pass their references to the controller
		StudentProfile model = new StudentProfile();
		view = new WindowRootPane();
		new WindowController(view, model);
	}


	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Module Chooser GUI");
		stage.setResizable(false);
		stage.setScene(new Scene(view, 600,600));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
