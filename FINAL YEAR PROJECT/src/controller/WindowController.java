package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Profile;
import model.Questions;
import view.WindowRootPane;

public class WindowController  {

	private Profile model;
	private WindowRootPane view;

	private Scanner fileIn;
	private BufferedWriter writer;

	public WindowController(WindowRootPane view, Profile model) throws IOException {
		//initialise model and view fields
		this.model = model;
		this.view = view;

		//Attach all handlers
		this.attachEventHandlers();

		this.addQuestions();

		view.getGrid().getQuestions(model.getQuestionsFromProfile());
		view.getGrid().setQuestions(model);

		view.getQuestionManager().getQuestions(model.getQuestionsFromProfile());
		view.getQuestionManager().setQuestions(model);
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
		view.getGrid().addGenerateHandler(new GenerateHandler());
		view.getGrid().addHintHandler(new selectHint());
		view.getGrid().addCheckHandler(new checkAnswers());
		view.getProfileForm().addCreateProfileHandler(new AddCreateProfileHandler());
		view.getQuestionManager().addAddQuestionHandler(new AddAddQuestionHandler());
		view.getQuestionManager().addRefreshQuestionHandler(new addRefreshQuestionHandler());
		view.getQuestionManager().addDeleteQuestionHandler(new addDeleteQuestionHandler());


	}

//------------------------HANDLERS FOR MENU-----------------------------------------------

	//Menu - Save
	private class SaveMenuHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			model.clearGridInput();
			view.getGrid().getGridInputView(model);
			//save the data model
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SaveObj.dat"))) {
				// get the content in bytes
				oos.writeObject(model); //writes the model object to a file
				oos.flush();
				oos.close();

				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Save success", "Register saved to SaveObj.dat");
			}
			catch (IOException ioExcep){
				System.out.println(ioExcep/*"Error saving..."*/);
			}
		}
	}

	//Menu - Load
	private class LoadMenuHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			//load in the data model
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SaveObj.dat"))) {

				model = (Profile) ois.readObject();
				ois.close();
				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Load success", "Data loaded from saveObj.dat");
			}
			catch (IOException ioExcep){
				System.out.println("Error loading...");
			}
			catch (ClassNotFoundException c) {
				System.out.println("Class Not found");
			}

			view.getGrid().Crossword();
			view.getProfileForm().clearForm();
			view.getProfileForm().populateForm(model.getName(),model.getSurname(),model.getEmail(),model.getDate(), model.getRBt());
			view.getGrid().populateGrid(model);
			view.getGrid().setHintCount(model);
			view.enableTab();


		}
	}

	//Button Generate to create a crossword
	private class GenerateHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			view.getGrid().Clear();
			view.getGrid().Crossword();
		}
	}

	//Profile validation
	private class AddCreateProfileHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			//set data into the model then check input not empty
			view.getGrid().Clear();
			model.setName(view.getProfileForm().getName());
			model.setSurname(view.getProfileForm().getSurname());
			model.setEmail(view.getProfileForm().getEmail());
			model.setData(view.getProfileForm().getDate());
			model.setRbt((String) view.getProfileForm().getToggle().getToggleGroup().getSelectedToggle().getUserData());
			if(model.getName().equals("")) {
				//output error
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "You have to type in your NAME!");
			} else if (model.getSurname().equals("")) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "You have to type in your SURNAME!");
			} else if (model.getEmail().equals("")) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "You have to type in your EMAIL!");
			} else if (model.getEmail().contains("@")==false || model.getEmail().contains(".")==false) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "Email format is incorrect! (Example: MyEmail@email.com)");
			} else if (model.getDate()==null) {
				alertDialogBuilder(AlertType.ERROR, "Error Dialog", null, "You have to pick the DATE!");
			} else {
				//enable grid view
				if(view.getProfileForm().whichRadioButton()==1) {
					model.setHintNumber("Unlimited");
					view.getGrid().setHintCount(model);
				} else if(view.getProfileForm().whichRadioButton()==2) {
					model.setHintNumber("5");
					view.getGrid().setHintCount(model);
				} else if(view.getProfileForm().whichRadioButton()==3) {
					model.setHintNumber("3");
					view.getGrid().setHintCount(model);
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Selection Error");
					alert.setContentText("Cannot process hint counter");
					alert.showAndWait();
				}
				view.enableTab();
				view.openGridTab();
				alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", "Profile Has Been Created!", "All data has been captured within model...");
			}
		}
	}

	private class AddAddQuestionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if(view.getQuestionManager().getQuestionfieldvalue().equals("")){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Input Error");
				alert.setContentText("Question Text field is empty- cannot add empty");
				alert.showAndWait();
			} else if(view.getQuestionManager().getAnswerfieldvalue().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Input Error");
				alert.setContentText("Answer Text field is empty- cannot add empty");
				alert.showAndWait();
			} else if(view.getQuestionManager().getHintfieldvalue().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Input Error");
				alert.setContentText("Hint Text field is empty- cannot add empty");
				alert.showAndWait();
			} else {
				model.addSingleQuestion(view.getQuestionManager().getQuestionfieldvalue(), view.getQuestionManager().getAnswerfieldvalue(), view.getQuestionManager().getHintfieldvalue());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Question Management");
				alert.setHeaderText("");
				alert.setContentText("Question has been added...");
				alert.showAndWait();
				try
				{
				    writer = new BufferedWriter(new FileWriter("QuestionList.txt", true));
				    writer.append("\r\n" + view.getQuestionManager().getQuestionfieldvalue()+","+view.getQuestionManager().getAnswerfieldvalue()+","+view.getQuestionManager().getHintfieldvalue()+",");
				    writer.flush();
				    writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private class addRefreshQuestionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			view.getQuestionManager().setQuestions(model);
			view.getGrid().setQuestions(model);
		}
	}

	private class addDeleteQuestionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			if (view.getQuestionManager().getSeletedQuestion() == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Selection Error");
				alert.setContentText("Please select a QUESTION to delete...");
				alert.showAndWait();
		} else  {
				File QuestionList = new File("QuestionList.txt");
		        File tempFile = new File("tempFile.txt");
		        String currentLine;
                try {
    		        BufferedReader reader = new BufferedReader(new FileReader(QuestionList));
    		        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		        while((currentLine = reader.readLine()) != null) {
		            if(!currentLine.toUpperCase().contains(view.getQuestionManager().getSeletedQuestion().substring(3).toUpperCase())){
							writer.write(currentLine + "\r\n");
		            }
		        }
		        writer.close();
		        reader.close();
//CHANGE DIRECTORY IF PROJECT MOVED **** CHANGE DIRECTORY IF PROJECT MOVED **** CHANGE DIRECTORY IF PROJECT MOVED **** CHANGE DIRECTORY IF PROJECT MOVED **** CHANGE DIRECTORY IF PROJECT MOVED **** CHANGE DIRECTORY IF PROJECT MOVED ****
		        Files.move(Paths.get("\\tempFile.txt"), Paths.get("\\QuestionList.txt"), StandardCopyOption.ATOMIC_MOVE);
				} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
				}
				model.deleteSingleQuestion(view.getQuestionManager().getSeletedQuestion().substring(3));
		  }
		}

	}





	//
	private class selectHint implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent arg0) {
		if (view.getGrid().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Selection Error");
			alert.setContentText("Please select a QUESTION to get hint.");
			alert.showAndWait();
	} else  {
			view.getGrid().setHintTextBox(model);
			view.getGrid().decrementHintCount(model);
			}
		}
	}

	private class checkAnswers implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			if(view.getGrid().checkInput()==true) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("CONGRATULATIONS!");
				alert.setHeaderText("VICTORY!");
				alert.setContentText("All the answers are correct!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Incorrect");
				alert.setContentText("One or more letters are not correct!");
				alert.showAndWait();
			}

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


	@SuppressWarnings("resource")
	private void addQuestions() {
		ArrayList<Questions> questionlist = new ArrayList<Questions>();
		try
		{
            fileIn = new Scanner(new BufferedReader(new FileReader("QuestionList.txt"))).useDelimiter("\\s*,\\s*");
        	int i = 0;
        	int count = 0;

            while (fileIn.hasNext())
            {
            	if((count % 3) == 0) {
            		questionlist.add(new Questions(i, fileIn.next().toUpperCase(), null, null));
            	} else if ((count % 3) == 1) {
            		questionlist.get(i).setAnswer(fileIn.next().toUpperCase());
            	} else if ((count % 3) == 2) {
            		questionlist.get(i).setHint(fileIn.next().toUpperCase());
            		i++;
            	}
            	count++;
            }
    		model.addQuestionsToProfile(questionlist);
    		fileIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

