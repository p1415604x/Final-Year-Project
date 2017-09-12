package view;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class WindowRootPane extends BorderPane {

	private WindowMenu wm;
	private GridView gv;
	private createProfileForm cpf;
	private questionManager qmgr;
	private TabPane myTabPane;
	private Tab createProfile, gridView, addQuestions;

	public WindowRootPane() {

		//background
		this.setStyle("-fx-background-image: url('/view/background.jpg')");

		wm = new WindowMenu();
		gv = new GridView();
		cpf = new createProfileForm();
		qmgr = new questionManager();

		this.setTop(wm);
		this.setCenter(gv);

		myTabPane = new TabPane();
		myTabPane.setPadding(new Insets(0,0,20,0));
		createProfile = new Tab("Create Profile");
		gridView = new Tab("Crossword");
		addQuestions = new Tab("Question manager");
		myTabPane.getTabs().addAll(createProfile, gridView, addQuestions);
		this.setCenter(myTabPane);
		myTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		myTabPane.getTabs().get(1).setDisable(true);

		//HOW TO ADD CONTENT TO TABS
	    createProfile.setContent(cpf);
	    gridView.setContent(gv);
	    addQuestions.setContent(qmgr);

	}

	public WindowMenu getMenu() {
		return wm;
	}

	public GridView getGrid() {
		return gv;
	}

	public createProfileForm getProfileForm() {
		return cpf;
	}

	public questionManager getQuestionManager() {
		return qmgr;
	}

	public void openGridTab() {
		myTabPane.getSelectionModel().select(gridView);
	}

	public void disableTab() {
		myTabPane.getTabs().get(1).setDisable(true);
	}

	public void enableTab() {
		myTabPane.getTabs().get(1).setDisable(false);
	}
}
