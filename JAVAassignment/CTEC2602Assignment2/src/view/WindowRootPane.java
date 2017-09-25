package view;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class WindowRootPane extends BorderPane {

	private WindowMenu wm;
	private createProfileForm cpf;
	private selectModulesForm smf;
	private OverviewForm ovf;
	private TabPane myTabPane;

	public WindowRootPane() {


		//background
		this.setStyle("-fx-background-color: #D15D78;");

		wm = new WindowMenu();
		cpf = new createProfileForm();
		smf = new selectModulesForm();
		ovf = new OverviewForm();

		this.setTop(wm);

		myTabPane = new TabPane();
		myTabPane.setPadding(new Insets(0,0,20,0));
		Tab createProfile = new Tab("Create Profile");
		Tab selectModules = new Tab("Select Modules");
		Tab Overview = new Tab("Overview Section");
		myTabPane.getTabs().addAll(createProfile, selectModules, Overview);
		this.setCenter(myTabPane);
		myTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		//HOW TO ADD CONTENT TO TABS
	    createProfile.setContent(cpf);
	    selectModules.setContent(smf);
	    Overview.setContent(ovf);


	}

	public WindowMenu getMenu() {
		return wm;
	}

	public createProfileForm getProfileForm() {
		return cpf;
	}

	public selectModulesForm getModulesForm() {
		return smf;
	}

	public OverviewForm getOverviewForm() {
		return ovf;
	}

	public void changeTab(int index) {
		myTabPane.getSelectionModel().select(index);
	}
}
