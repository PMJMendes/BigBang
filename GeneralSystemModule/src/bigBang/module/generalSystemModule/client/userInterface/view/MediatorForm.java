package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.client.userInterface.MediatorFormValidator;
import bigBang.module.generalSystemModule.shared.ComissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;

public class MediatorForm extends FormView {

	private Mediator mediator;
	
	private TextBoxFormField name;
	private TextBoxFormField ISPNumber;
	private TextBoxFormField taxNumber;
	private ListBoxFormField comissionProfile;
	private AddressFormField address;
	private TextBoxFormField NIB;
	
	public MediatorForm() {
		addSection("Informação geral");
		
		name = new TextBoxFormField("Nome", new MediatorFormValidator.NameValidator());
		ISPNumber = new TextBoxFormField("Número do ISP", new MediatorFormValidator.ISPNumberValidator());
		taxNumber = new TextBoxFormField("Número de contribuinte", new MediatorFormValidator.TaxNumberValidator());
		comissionProfile = new ListBoxFormField("Perfil de comissão", new MediatorFormValidator.ComissionProfileValidator());
		NIB = new TextBoxFormField("NIB", new MediatorFormValidator.NIBValidator());
		
		addFormField(name);
		addFormField(taxNumber);
		addFormField(NIB);
		addFormField(ISPNumber);
		addFormField(comissionProfile);
		
		addSection("Morada");
		
		address = new AddressFormField(new MediatorFormValidator.AddressValidator());
		addFormField(address);
	}
	
	public void setComissionProfiles(ComissionProfile[] comissionProfiles) {
		comissionProfile.addItem("Não atribuído", "");
		for(int i = 0; i < comissionProfiles.length; i++) {
			comissionProfile.addItem(comissionProfiles[i].name, comissionProfiles[i].id);
		}
	}
	
	@Override
	public Object getInfo() {
		mediator.name = name.getValue();
		mediator.ISPNumber = ISPNumber.getValue();
		mediator.taxNumber = taxNumber.getValue();
		mediator.comissionProfile.id = comissionProfile.getValue();
		mediator.comissionProfile.name = comissionProfile.getSelectedItemText();
		mediator.address = address.getValue();
		mediator.NIB = NIB.getValue();
		return mediator;
	}

	@Override
	public void setInfo(Object info) {
		this.mediator = (Mediator) info;
		name.setValue(mediator.name);
		ISPNumber.setValue(mediator.ISPNumber);
		taxNumber.setValue(mediator.taxNumber);
		comissionProfile.setValue(mediator.comissionProfile.id);
		address.setValue(mediator.address);
		NIB.setValue(mediator.NIB);
	}

	@Override
	public void clearInfo() {
		this.mediator = new Mediator();
		name.setValue("");
		ISPNumber.setValue("");
		taxNumber.setValue("");
		comissionProfile.setValue("");
		address.clear();
		NIB.setValue("");
	}
	
}
