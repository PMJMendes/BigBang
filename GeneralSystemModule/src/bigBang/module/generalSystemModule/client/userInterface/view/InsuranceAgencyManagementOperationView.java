package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.InsuranceAgencyList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.InsuranceAgencyManagementOperationViewPresenter;

public class InsuranceAgencyManagementOperationView extends View implements InsuranceAgencyManagementOperationViewPresenter.Display {

	private final int AGENCIES_LIST_WIDTH = 400; //px
	
	private InsuranceAgencyList insuranceAgencyList;
	private InsuranceAgencyForm insuranceAgencyform;
	private InsuranceAgencyForm newInsuranceAgencyForm;
	private PopupPanel newInsuranceAgencyPopup;
	
	private Button editInsuranceAgencyButton;
	private Button saveInsuranceAgencyButton;
	private Button deleteInsuranceAgencyButton;
	private Button newInsuranceAgencyButton;
	private Button submitNewInsuranceAgencyButton;
		
	public InsuranceAgencyManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		insuranceAgencyList = new InsuranceAgencyList();
		insuranceAgencyform = new InsuranceAgencyForm();
		newInsuranceAgencyForm = new InsuranceAgencyForm();
		newInsuranceAgencyPopup = new PopupPanel("Criação de Seguradora");
		
		editInsuranceAgencyButton = new Button("Editar");
		saveInsuranceAgencyButton = new Button("Guardar");
		deleteInsuranceAgencyButton = new Button("Apagar");
		newInsuranceAgencyButton = new Button("Novo");
		submitNewInsuranceAgencyButton = new Button("Submeter");
		
		insuranceAgencyform.addButton(editInsuranceAgencyButton);
		insuranceAgencyform.addButton(saveInsuranceAgencyButton);
		insuranceAgencyform.addButton(newInsuranceAgencyButton);
		insuranceAgencyform.addButton(deleteInsuranceAgencyButton);
		
		newInsuranceAgencyForm.addButton(submitNewInsuranceAgencyButton);
		newInsuranceAgencyPopup.add(newInsuranceAgencyForm);
		
		wrapper.addWest(insuranceAgencyList, AGENCIES_LIST_WIDTH);
		wrapper.setWidgetMinSize(insuranceAgencyList, AGENCIES_LIST_WIDTH);
		
		wrapper.add(insuranceAgencyform);
		
		initWidget(wrapper);
	}
	
}
