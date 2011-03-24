package bigBang.module.generalSystemModule.client.userInterface.view;

import java.util.ArrayList;

import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;
import bigBang.module.generalSystemModule.shared.formValidator.InsuranceAgencyFormValidator;

public class InsuranceAgencyForm extends FormView {

	private TextBoxFormField name;
	private TextBoxFormField acronym;
	private TextBoxFormField ISPNumber;
	private ArrayList<TextBoxFormField> ownMediatorCodeList;
	private TextBoxFormField taxNumber;
	private TextBoxFormField NIB;
	private AddressFormField address;
	
	private FormViewSection ownMediatorCodeSection;
	
	private InsuranceAgency info;
	
	public InsuranceAgencyForm(){
		ownMediatorCodeList = new ArrayList<TextBoxFormField>();
		
		name = new TextBoxFormField("Nome", new InsuranceAgencyFormValidator.NameValidator());
		acronym = new TextBoxFormField("Sigla", new InsuranceAgencyFormValidator.AcronymValidator());
		ISPNumber = new TextBoxFormField("Número do ISP", new InsuranceAgencyFormValidator.ISPNumberValidator());
		taxNumber = new TextBoxFormField("NIF/NIPC", new InsuranceAgencyFormValidator.TaxNumberValidator());
		NIB = new TextBoxFormField("NIP", new InsuranceAgencyFormValidator.NIBValidator());
		address = new AddressFormField(new InsuranceAgencyFormValidator.AddressValidator());

		addSection("Informação Geral");
		
		addFormField(name);
		addFormField(acronym);
		addFormField(taxNumber);
		addFormField(NIB);
		
		addSection("Códigos de Mediador");
		ownMediatorCodeSection = currentSection;		
		
		addSection("Morada");
		
		addFormField(address);
		
		clearInfo();
	}
	
	@Override
	public Object getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(Object info) {
		clearInfo();
		InsuranceAgency agency = (InsuranceAgency) info;
		name.setValue(agency.name);
		acronym.setValue(agency.acronym);
		taxNumber.setValue(agency.taxNumber);
		NIB.setValue(agency.NIB);
		address.setValue(agency.address);
		for(int i = 0; i < agency.ownMediatorCodes.length; i++) {
			addOwnMediatorCodeField(agency.ownMediatorCodes[i]);
		}		
	}

	@Override
	public void clearInfo() {
		name.setValue("");
		acronym.setValue("");
		taxNumber.setValue("");
		NIB.setValue("");
		address.clear();
		ownMediatorCodeSection.clear();
		ownMediatorCodeList.clear();
		addOwnMediatorCodeField();
	}
	
	public void addOwnMediatorCodeField(){
		addOwnMediatorCodeField(null);
	}
	
	public void addOwnMediatorCodeField(String value) {
		TextBoxFormField ownMediatorCode = new TextBoxFormField((this.ownMediatorCodeList.size() + 1) + "", new InsuranceAgencyFormValidator.OwnMediatorCodeValidator());
		if(value != null)
			ownMediatorCode.setValue(value);
		ownMediatorCodeList.add(ownMediatorCode);
		ownMediatorCodeSection.addFormField(ownMediatorCode);
	}

}
