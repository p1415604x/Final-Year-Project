package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Course;
import model.Module;
import model.StudentProfile;
import view.WindowRootPane;

public class WindowController {

	private StudentProfile model;
	private WindowRootPane view;

	public WindowController(WindowRootPane view, StudentProfile model) {
		//initialise model and view fields
		this.model = model;
		this.view = view;

		//Attach all handlers
		this.attachEventHandlers();

		//Listener to fill up unselected and selected views after selecting course
		this.view.getProfileForm().addSelectionChangeListener((observable, oldVal, newVal) -> {
			view.getModulesForm().getSelectedItems().clear();
			view.getModulesForm().getUnselectedItems().clear();
			view.getModulesForm().resetCredits();
			newVal.getModulesOnCourse().forEach(e-> {
				if(e.isMandatory()==false) {
					view.getModulesForm().setUnselectedItems(e);
				}
			});
			newVal.getModulesOnCourse().forEach(e-> {
				if(e.isMandatory()==true) {
					view.getModulesForm().setSelectedItems(e);
					view.getModulesForm().addCredits(e);
					model.addSelectedModule(e);
				}
			});
			model.setCourse(newVal);
			view.getOverviewForm().setProfileDataForOverview(model);
		});

		//Listener which enables submit button only if credit sum is 120
		this.view.getModulesForm().addCreditChangeListener((observable, oldVal, newVal) -> {
			if(view.getModulesForm().getCredits()==120) {
				view.getModulesForm().enableSubmit();
			} else {
				view.getModulesForm().disableSubmit();
				view.getOverviewForm().disableSaveOverview();
			}
		});

		//Populates combo box with courses in create profile tab
		view.getProfileForm().populateComboBox(setupAndGetCourses());

	}

//Adds all handlers to specific areas of the code
	private void attachEventHandlers() {
		//attaching event handlers - some done as lambda expressions and some as inner classes
		//TOP MENU
		view.getMenu().addLoadHandler(new LoadMenuHandler());	//Menu - Load
		view.getMenu().addSaveHandler(new SaveMenuHandler());	//Menu - Save
		view.getMenu().addExitHandler(e -> System.exit(0));		//Menu - Exit
		view.getMenu().addAboutHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION,
				"Information Dialog", null, "An application written by using MVC model \n"
						+ "By Laurynas Mykoklaitis \nP1415604x" )); //Menu - About
		view.getMenu().addHelpHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION,
				"Information Dialog", null, "No additional information provided at the moment.")); // Menu - Help
		//Create profile Button
		view.getProfileForm().addCreateHandler(new AddCreateProfileHandler());
		//Buttons in module selection
		view.getModulesForm().addAddHandler(new AddHandler());	//Button - Add
		view.getModulesForm().addResetHandler(new ResetHandler());	//Button - Reset
		view.getModulesForm().addRemoveHandler(new RemoveHandler());	//Button - Remove
		view.getModulesForm().addSubmitHandler(new SubmitHandler());	//Button - Submit
		//Button in overview tab
		view.getOverviewForm().addSaveOverviewHandler(new OverviewHandler()); //Button - Save Overview
	}

//--------------------------HANDLER INNER CLASSES--------------------------------------
//HANDLERS FOR BUTTONS IN MODULE SELECTION

	//Button - Add
	private class AddHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent arg0) {
			//retrieves name from view
			if (view.getModulesForm().getUnselectedItem() != null) {
				view.getModulesForm().setSelectedItems(view.getModulesForm().getUnselectedItem());
				view.getModulesForm().addCredits(view.getModulesForm().getUnselectedItem());
				view.getModulesForm().removeUnselectedItemsFocusedItem();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Selection Error");
				alert.setContentText("Please select an item to add.");
				alert.showAndWait();
			}
		}
	}

	//Button - Remove
	private class RemoveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if (view.getModulesForm().getSelectedItem() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Selection Error");
				alert.setContentText("Please select an item to remove.");
				alert.showAndWait();
		} else if (view.getModulesForm().getSelectedItem().isMandatory()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Selection Error");
				alert.setContentText("You cannot remove mandatory course!.");
				alert.showAndWait();
		} else {
				view.getModulesForm().setUnselectedItems(view.getModulesForm().getSelectedItem());
				view.getModulesForm().subtractCredits(view.getModulesForm().getSelectedItem());
				view.getModulesForm().removeSelectedItemsFocusedItem();
			}
		}
	}

	//Button - Submit
	private class SubmitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			model.clearSelectedModules();
			view.getModulesForm().getSelectedItems().forEach(x -> model.addSelectedModule(x));
			view.getOverviewForm().setProfileDataForOverview(model);
			view.getOverviewForm().enableSaveOverview();
			alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Modules Have Been Submited", "Modules have been added to the model...");
		}

	}

	//Button - Reset
	private class ResetHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			view.getModulesForm().getSelectedItems().clear();
			view.getModulesForm().getUnselectedItems().clear();
			view.getModulesForm().resetCredits();
			model.clearSelectedModules();
			model.getCourse().getModulesOnCourse().forEach(e-> {
				if(e.isMandatory()==false) {
					view.getModulesForm().setUnselectedItems(e);
				}
			});
			model.getCourse().getModulesOnCourse().forEach(e-> {
				if(e.isMandatory()==true) {
					view.getModulesForm().setSelectedItems(e);
					model.addSelectedModule(e);
				}
			});
			view.getOverviewForm().setProfileDataForOverview(model);
			view.getOverviewForm().disableSaveOverview();
			alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Reset Completed!", "Modules deleted from model; Overview updated!");
		}

	}

//------------------------HANDLERS FOR MENU-----------------------------------------------

	//Menu - Save
	private class SaveMenuHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {

			//save the data model
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("StudentObj.dat"))) {

				// get the content in bytes
				oos.writeObject(model); //writes the model object to a file
				oos.flush();
				oos.close();

				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Save success", "Register saved to StudentObj.dat");
			}
			catch (IOException ioExcep){
				System.out.println("Error saving");
			}
		}
	}

	//Menu - Load
	private class LoadMenuHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			//load in the data model
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("StudentObj.dat"))) {

				model = (StudentProfile) ois.readObject();
				ois.close();
				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Load success", "Register loaded from registerObj.dat");
			}
			catch (IOException ioExcep){
				System.out.println("Error loading");
			}
			catch (ClassNotFoundException c) {
				System.out.println("Class Not found");
			}

			//Setting all the data from model back to their fields
			view.getProfileForm().clearForm();
			view.getProfileForm().populateForm(model.getCourse(), model.getpNumber(), model.getStudentName().getFirstName(), model.getStudentName().getFamilyName(), model.getEmail(), model.getDate());
			view.getModulesForm().getSelectedItems().clear();
			view.getModulesForm().getUnselectedItems().clear();
			view.getModulesForm().setSelectedItemsCollection(model.getSelectedModules());
			view.getModulesForm().refreshCredits();
			view.getModulesForm().setUnselectedItemsCollection(model.getCourse().getModulesOnCourse());
			view.getModulesForm().getUnselectedItems().removeAll(model.getSelectedModules());
			view.getOverviewForm().setProfileDataForOverview(model);
			view.getModulesForm().enableListviews();
			view.getOverviewForm().enableSaveOverview();

		}
	}

//------------------------OTHER HANDLERS-----------------------------------------------

	//Button - Create Profile
	private class AddCreateProfileHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			//retrieves name from view
			//StudentProfile n = view.getProfileForm().createStudentProfile();
			model.setCourse(view.getProfileForm().getSelectedCoures());
			model.setpNumber(view.getProfileForm().getPnumber());
			model.setStudentName(view.getProfileForm().getName());
			model.setEmail(view.getProfileForm().getEmail());
			model.setDate(view.getProfileForm().getDate());
			view.getOverviewForm().setProfileDataForOverview(model);
			//check input not empty
			if(model.getCourse()==null) {
				//output error
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "You have to select course!");
			} else if (model.getpNumber().equals("")) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Need to input P number!");
			} else if (model.getStudentName().getFirstName().equals("") || model.getStudentName().getFamilyName().equals("")) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Need to input both first and family name!");
			} else if (model.getEmail().equals("")) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Need to input email!");
			} else if (model.getEmail().contains("@")==false || model.getEmail().contains(".")==false) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Email format is incorrect! (Example: MyEmail@email.com)");
			} else if (model.getDate()==null) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Need to select Date!");
			} else {
				view.getModulesForm().enableListviews();
				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Profile Has Been Created!", "All data has been captured within model...");
			}
		}
	}

	//Button - Save Overview
	private class OverviewHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			view.getOverviewForm().getProfileDataFromOverview(model);
			alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Saved successfully", "Overview saved in the local directory.");
		}
	}


	//helper method to build dialogs
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private Course[] setupAndGetCourses() {
		Module ctec3903 = new Module("CTEC3903", "Software Development Methods", 15, true);
		Module imat3451 = new Module("IMAT3451", "Computing Project", 30, true);
		Module ctec3902_SoftEng = new Module("CTEC3902", "Rigerous Systems", 15, true);
		Module ctec3902_CompSci = new Module("CTEC3902", "Rigerous Systems", 15, false);
		Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false);
		Module ctec3426 = new Module("CTEC3426", "Telematics", 15, false);
		Module ctec3604 = new Module("CTEC3604", "Multi-service Networks", 30, false);
		Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false);
		Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false);
		Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false);
		Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false);
		Module imat3404 = new Module("IMAT3404", "Mobile Robotics", 15, false);
		Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false);
		Module imat3429 = new Module("IMAT3429", "Privacy and Data Protection", 15, false);
		Module imat3902 = new Module("IMAT3902", "Computing Ethics", 15, false);
		Module imat3426_CompSci = new Module("IMAT3426", "Information Systems Strategy and Services", 30, false);
		Course compSci = new Course("Computer Science");
		compSci.addModule(ctec3903);
		compSci.addModule(imat3451);
		compSci.addModule(ctec3902_CompSci);
		compSci.addModule(ctec3110);
		compSci.addModule(ctec3426);
		compSci.addModule(ctec3604);
		compSci.addModule(ctec3410);
		compSci.addModule(ctec3904);
		compSci.addModule(ctec3905);
		compSci.addModule(imat3410);
		compSci.addModule(imat3404);
		compSci.addModule(imat3406);
		compSci.addModule(imat3429);
		compSci.addModule(imat3902);
		compSci.addModule(imat3426_CompSci);
		Course softEng = new Course("Software Engineering");
		softEng.addModule(ctec3903);
		softEng.addModule(imat3451);
		softEng.addModule(ctec3902_SoftEng);
		softEng.addModule(ctec3110);
		softEng.addModule(ctec3426);
		softEng.addModule(ctec3604);
		softEng.addModule(ctec3410);
		softEng.addModule(ctec3904);
		softEng.addModule(ctec3905);
		softEng.addModule(imat3410);
		softEng.addModule(imat3404);
		softEng.addModule(imat3406);
		softEng.addModule(imat3429);
		softEng.addModule(imat3902);
		Course[] courses = new Course[2];
		courses[0] = compSci;
		courses[1] = softEng;
		return courses;
		}

}
