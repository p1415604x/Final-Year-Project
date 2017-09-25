package view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import model.StudentProfile;

public class OverviewForm extends FlowPane{

	private ListView<String> overviewView;
	private ObservableList<String> profiledata;
	private Button btSave;

	public OverviewForm() {

	    FlowPane flow = new FlowPane();
	    flow.setPadding(new Insets(20, 50, 60, 50));
	    flow.setVgap(40);
	    flow.setHgap(4);
	    flow.setPrefWrapLength(170); // preferred width allows for two columns

	    HBox hb1 = new HBox();
	    HBox hb2 = new HBox();

		overviewView = new ListView<String>();
		overviewView.setPlaceholder(new Label("For details selected overview is not available!"));
		overviewView.setMinSize(500, 400);
		profiledata = FXCollections.observableArrayList();
		hb1.getChildren().add(overviewView);

		btSave = new Button("Save Overview");
		btSave.setMaxSize(125, 35);
		btSave.setDisable(true);
		hb2.getChildren().add(btSave);
		hb2.setPadding(new Insets(0,0,0,200));

		flow.getChildren().addAll(hb1, hb2);

		this.getChildren().add(flow);
	}

	public void setProfileDataForOverview(StudentProfile model) {
		profiledata.clear();
		String data = new String();
		String s = model.getSelectedModules().stream().map(Object::toString).collect(Collectors.joining("\n"));
	    data =  "-------STUDENT ACCOUNT----------" +
	    		"\nCourse: " + model.getCourse().toString() +
	    		"\nP number: " + model.getpNumber() +
	    		"\nName: " + model.getStudentName().getFirstName() + ", " + model.getStudentName().getFamilyName() +
	    		"\nEmail: " + model.getEmail() +
	    		"\nDate: " + model.getDate() +
	    		"\n----------------------------------------\n"
	    		+ "         SELECTED MODULES      \n"
	    		+ "----------------------------------------\n" + s;
		profiledata.addAll(data);
		overviewView.setItems(profiledata);
	}

	public void getProfileDataFromOverview(StudentProfile model) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("Overview.txt", false))) {
	        bw.write("-------STUDENT ACCOUNT----------");
	        bw.newLine();
	        bw.write("Course: " + model.getCourse().toString());
	        bw.newLine();
	        bw.write("P number: " + model.getpNumber());
	        bw.newLine();
	        bw.write("Name: " + model.getStudentName().getFirstName() + ", " + model.getStudentName().getFamilyName());
	        bw.newLine();
	        bw.write("Email: " + model.getEmail());
	        bw.newLine();
	        bw.write("Date: " + model.getDate());
	        bw.newLine();
	        bw.write("----------------------------------------");
	        bw.newLine();
	        bw.write("         SELECTED MODULES      ");
	        bw.newLine();
	        bw.write("----------------------------------------");
	        bw.newLine();
	        String[] modules = model.getSelectedModules().stream().map(Object::toString).collect(Collectors.joining("&&")).split("&&");
	        for (String module: modules) {
	            bw.write(module);
	            bw.newLine();
	        }
	        bw.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void enableSaveOverview() {
		btSave.setDisable(false);
	}
	public void disableSaveOverview() {
		btSave.setDisable(true);
	}

	public void addSaveOverviewHandler(EventHandler<ActionEvent> handler) {
		btSave.setOnAction(handler);
	}

}
