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
	private TabPane myTabPane;
	private Tab createProfile, gridView;

	public WindowRootPane() {

		//background
		this.setStyle("-fx-background-image: url('/view/background.jpg')");

		wm = new WindowMenu();
		gv = new GridView();
		cpf = new createProfileForm();

		this.setTop(wm);
		this.setCenter(gv);

		wm = new WindowMenu();
		gv = new GridView();

		this.setTop(wm);

		myTabPane = new TabPane();
		myTabPane.setPadding(new Insets(0,0,20,0));
		createProfile = new Tab("Create Profile");
		gridView = new Tab("Crossword");
		myTabPane.getTabs().addAll(createProfile, gridView);
		this.setCenter(myTabPane);
		myTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		myTabPane.getTabs().get(1).setDisable(true);

		//HOW TO ADD CONTENT TO TABS
	    createProfile.setContent(cpf);
	    gridView.setContent(gv);

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
