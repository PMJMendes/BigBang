package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.CommissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.formValidator.MediatorFormValidator;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

public class MediatorForm extends FormView<Mediator> {

	private Mediator mediator;
	
	private TextBoxFormField name;
	private TextBoxFormField ISPNumber;
	private TextBoxFormField taxNumber;
	private ExpandableListBoxFormField comissionProfile;
	private AddressFormField address;
	private TextBoxFormField NIB;
	
	private Button editCostCenterButton;
	private Button saveCostCenterButton;
	private Button deleteCostCenterButton;
	
	public MediatorForm() {
		addSection("Informação geral");
		
		name = new TextBoxFormField("Nome", new MediatorFormValidator.NameValidator());
		ISPNumber = new TextBoxFormField("Número do ISP", new MediatorFormValidator.ISPNumberValidator());
		taxNumber = new TextBoxFormField("NIF/NIPC", new MediatorFormValidator.TaxNumberValidator());
		comissionProfile = new ExpandableListBoxFormField(ModuleConstants.ListIDs.CommissionProfiles, "Perfil de comissão", new MediatorFormValidator.ComissionProfileValidator());
		NIB = new TextBoxFormField("NIB/IBAN", new MediatorFormValidator.NIBValidator());
		
		addFormField(name);
		addFormField(taxNumber);
		addFormField(NIB);
		addFormField(ISPNumber);
		addFormField(comissionProfile);
		
		this.editCostCenterButton = new Button("Editar");	
		this.saveCostCenterButton = new Button("Guardar");
		this.deleteCostCenterButton = new Button("Apagar");
		
		this.addButton(editCostCenterButton);
		this.addButton(saveCostCenterButton);
		this.addButton(deleteCostCenterButton);
		
		addSection("Morada");
		
		address = new AddressFormField(new MediatorFormValidator.AddressValidator());
		addFormField(address);
	}
	
	public void setComissionProfiles(CommissionProfile[] comissionProfiles) {
		comissionProfile.clearValues();
		for(int i = 0; i < comissionProfiles.length; i++) {
			comissionProfile.addItem(comissionProfiles[i].value, comissionProfiles[i].id);
		}
	}
	
	@Override
	public Mediator getInfo() {
		mediator.name = name.getValue();
		mediator.ISPNumber = ISPNumber.getValue();
		mediator.taxNumber = taxNumber.getValue();
		mediator.comissionProfile.id = comissionProfile.getValue();
		mediator.comissionProfile.value = comissionProfile.getSelectedItemText();
		mediator.address = address.getValue();
		mediator.NIB = NIB.getValue();
		return mediator;
	}
	
	@Override
	public void setInfo(Mediator info) {
		this.mediator =  info != null ? (Mediator) info : new Mediator();
		name.setValue(mediator.name);
		ISPNumber.setValue(mediator.ISPNumber);
		taxNumber.setValue(mediator.taxNumber);
		comissionProfile.setValue(mediator.comissionProfile.id);
		address.setValue(mediator.address);
		NIB.setValue(mediator.NIB);
	}
	
	public HasClickHandlers getSaveButton() {
		return saveCostCenterButton;
	}
	
	public HasClickHandlers getEditButton() {
		return editCostCenterButton;
	}
	
	public HasClickHandlers getDeleteButton() {
		return deleteCostCenterButton;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		saveCostCenterButton.setVisible(!readOnly);
		editCostCenterButton.setVisible(readOnly);
	}
	
}
