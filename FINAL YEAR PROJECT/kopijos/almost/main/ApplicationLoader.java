package main;
/**
 * @author Laurynas Mykolaitis, P1415604x
 */

import java.io.IOException;

import controller.WindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Profile;
import view.WindowRootPane;

public class ApplicationLoader extends Application {

	private WindowRootPane view;
	private Profile model;

	@Override
	public void init() throws IOException {
		//create model and view and pass their references to the controller
		model = new Profile();
		view = new WindowRootPane();
		new WindowController(view, model);
	}


	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("CrossWords puzzle v1.0");
		stage.setResizable(true);
		stage.setScene(new Scene(view, 600,600));
		stage.setMaximized(true);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
