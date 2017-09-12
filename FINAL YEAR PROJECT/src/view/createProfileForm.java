package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class createProfileForm extends BorderPane{

	//private Button btCreateProfile;
	private Button btCreateProfile;
	private TextField tfFirstName, tfLastName, tfEmail;
	private DatePicker dpDate;
	private RadioButton rb1,rb2,rb3;
	final ToggleGroup group;

	createProfileForm() {


			GridPane myGridPane = new GridPane();

		        myGridPane.setPadding(new Insets(20, 0, 20, 20));
		        myGridPane.setHgap(10); myGridPane.setVgap(10);

//CREATE LABELS AND FIELDS

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

		        group = new ToggleGroup();
		        rb1 = new RadioButton("Easy");
		        rb1.setUserData("Easy");
		        rb2 = new RadioButton("Medium");
		        rb2.setUserData("Medium");
		        rb3 = new RadioButton("Hard");
		        rb3.setUserData("Hard");


		        rb1.setToggleGroup(group);
		        rb1.setSelected(true);
		        rb2.setToggleGroup(group);
		        rb3.setToggleGroup(group);

		        Image ButtonImage = new Image(getClass().getResourceAsStream("profile.png"));
		        btCreateProfile = new Button("Create Profile");
		        btCreateProfile.setGraphic(new ImageView(ButtonImage));

//ADD LABELS AND FIELDS

		        myGridPane.add(lbFirstName,0,0);
		        myGridPane.add(tfFirstName,1,0);

		        myGridPane.add(lbLastName,0,1);
		        myGridPane.add(tfLastName,1,1);

		        myGridPane.add(lbEmail,0,2);
		        myGridPane.add(tfEmail,1,2);

		        myGridPane.add(lbDate,0,3);
		        myGridPane.add(dpDate,1,3);

		        myGridPane.add(rb1,0,8);
		        myGridPane.add(rb2,0,9);
		        myGridPane.add(rb3,0,10);

		        myGridPane.add(btCreateProfile,1,18);



	    this.setCenter(myGridPane);








	}

	//these methods allow handlers to be externally attached to this view


		public String getName() {
			return new String(tfFirstName.getText());
		}

		public String getSurname() {
			return new String(tfLastName.getText());
		}

		public String getEmail() {
			return new String(tfEmail.getText());
		}

		public LocalDate getDate() {
			return dpDate.getValue();
		}

		public Toggle getToggle() {
			return group.getSelectedToggle();
		}


		public void clearForm() {
	        tfFirstName.setText("");
	        tfLastName.setText("");
	        tfEmail.setText("");
	        dpDate.getEditor().clear();
	        rb1.setSelected(true);
		}

		public void populateForm(String a, String b, String c, LocalDate d, String e) {
	        tfFirstName.setText(a);
	        tfLastName.setText(b);
	        tfEmail.setText(c);
	        dpDate.setValue(d);
	        if(e.contains("Easy")) {rb1.setSelected(true);}
	        else if(e.contains("Medium")) {rb2.setSelected(true);}
	        else {rb3.setSelected(true);}
		}

		public int whichRadioButton() {
			int res = 0;
			if(rb1.isSelected()==true) {
				res=1;
			} else if(rb2.isSelected()==true) {
				res=2;
			} else {
				res=3;
			}
			return res;
		}

		public void addCreateProfileHandler(EventHandler<ActionEvent> handler) {
			btCreateProfile.setOnAction(handler);
		}



}
