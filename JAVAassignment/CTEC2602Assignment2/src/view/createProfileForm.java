package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import model.Course;
import model.Name;

public class createProfileForm extends GridPane{

	//private Button btCreateProfile;
	private ComboBox<Course> cbSelectCourse;
	private Button btCreateProfile;
	private TextField tfPnumber, tfFirstName, tfLastName, tfEmail;
	private DatePicker dpDate;

	createProfileForm() {


			GridPane myGridPane = new GridPane();

		        myGridPane.setPadding(new Insets(20, 0, 20, 20));
		        myGridPane.setHgap(7); myGridPane.setVgap(7);

//CREATE LABELS AND FIELDS

		        Label lbSelectCourse = new Label("Select Course:");
		        cbSelectCourse = new ComboBox<Course>();

		        Label lbPnumber = new Label("P number:");
		        tfPnumber = new TextField();

		        Label lbFirstName = new Label("First Name:");
		        tfFirstName = new TextField();

		        Label lbLastName = new Label("Last Name:");
		        tfLastName = new TextField();

		        Label lbEmail = new Label("Email:");
		        tfEmail = new TextField();

		        Label lbDate = new Label("Application Date:");
		        dpDate = new DatePicker();

		        //DatePicker formatting
		        String myPattern = "yyyy-MM-dd";

		        dpDate.setConverter(new StringConverter<LocalDate>() {
		             DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(myPattern);

		             public String toString(LocalDate date) {
		                 if (date != null) {
		                     return dateFormatter.format(date);
		                 } else {
		                     return "";
		                 }
		             }

		             public LocalDate fromString(String string) {
		                 if (string != null && !string.isEmpty()) {
		                     return LocalDate.parse(string, dateFormatter);
		                 } else {
		                     return null;
		                 }
		             }
		         });

		        btCreateProfile = new Button("Create Profile");

//ADD LABELS AND FIELDS
		        myGridPane.add(lbSelectCourse,0,0);
		        myGridPane.add(cbSelectCourse,1,0);

		        myGridPane.add(lbPnumber,0,1);
		        myGridPane.add(tfPnumber,1,1);

		        myGridPane.add(lbFirstName,0,2);
		        myGridPane.add(tfFirstName,1,2);

		        myGridPane.add(lbLastName,0,3);
		        myGridPane.add(tfLastName,1,3);

		        myGridPane.add(lbEmail,0,4);
		        myGridPane.add(tfEmail,1,4);

		        myGridPane.add(lbDate,0,5);
		        myGridPane.add(dpDate,1,5);

		        myGridPane.add(btCreateProfile,1,6);


	    this.getChildren().add(myGridPane);
	}

	//these methods allow handlers to be externally attached to this view

	public void addCourseHandler(EventHandler<ActionEvent> handler) {
		cbSelectCourse.setOnAction(handler);
	}

	public void addCreateHandler(EventHandler<ActionEvent> handler) {
		btCreateProfile.setOnAction(handler);
	}


		public Course getSelectedCoures() {
			return cbSelectCourse.getSelectionModel().getSelectedItem();
		}

		public void addSelectionChangeListener(ChangeListener<Course> listener) {
			cbSelectCourse.getSelectionModel().selectedItemProperty().addListener(listener);
		}

		public String getPnumber() {
			return tfPnumber.getText();
		}

		public Name getName() {
			return new Name(tfFirstName.getText(), tfLastName.getText());
		}

		public String getEmail() {
			return tfEmail.getText();
		}

		public LocalDate getDate() {
			return dpDate.getValue();
		}

		public void populateComboBox(Course[] courses) {
			cbSelectCourse.getItems().addAll(courses);
			cbSelectCourse.getSelectionModel().select(0);
		}

		public void clearForm() {
	        cbSelectCourse.getSelectionModel().select(0);
	        tfPnumber.setText("");
	        tfFirstName.setText("");
	        tfLastName.setText("");
	        tfEmail.setText("");
	        dpDate.getEditor().clear();
		}

		public void populateForm(Course a, String b, String c, String d, String e, LocalDate f) {
			cbSelectCourse.getSelectionModel().select(a);
	        tfPnumber.setText(b);
	        tfFirstName.setText(c);
	        tfLastName.setText(d);
	        tfEmail.setText(e);
	        dpDate.setValue(f);
		}

}
