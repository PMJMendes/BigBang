package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.CommissionProfile;
import bigBang.definitions.shared.Mediator;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.formValidator.MediatorFormValidator;

public class MediatorForm extends FormView<Mediator> {

	private TextBoxFormField name;
	private TextBoxFormField ISPNumber;
	private TextBoxFormField taxNumber;
	private ExpandableListBoxFormField comissionProfile;
	private AddressFormField address;
	private TextBoxFormField NIB;
	
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
		Mediator info = this.value == null ? new Mediator() : new Mediator(value);
		info.name = name.getValue();
		info.ISPNumber = ISPNumber.getValue();
		info.taxNumber = taxNumber.getValue();
		info.comissionProfile.id = comissionProfile.getValue();
		info.comissionProfile.value = comissionProfile.getSelectedItemText();
		info.address = address.getValue();
		info.NIB = NIB.getValue();
		return info;
	}
	
	@Override
	public void setInfo(Mediator info) {
		if(info == null){
			clearInfo();
			return;
		}
		name.setValue(info.name);
		ISPNumber.setValue(info.ISPNumber);
		taxNumber.setValue(info.taxNumber);
		comissionProfile.setValue(info.comissionProfile.id);
		address.setValue(info.address);
		NIB.setValue(info.NIB);
	}
	
}
