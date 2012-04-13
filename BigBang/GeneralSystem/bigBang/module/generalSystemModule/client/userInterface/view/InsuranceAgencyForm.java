package bigBang.module.generalSystemModule.client.userInterface.view;

import java.util.ArrayList;

import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.generalSystemModule.shared.formValidator.InsuranceAgencyFormValidator;

public class InsuranceAgencyForm extends FormView<InsuranceAgency> {

	private TextBoxFormField name;
	private TextBoxFormField acronym;
	private TextBoxFormField ISPNumber;
	private ArrayList<TextBoxFormField> ownMediatorCodeList;
	private TextBoxFormField taxNumber;
	private TextBoxFormField NIB;
	private AddressFormField address;
	
	private FormViewSection ownMediatorCodeSection;
	
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
		addFormField(ISPNumber);
		addFormField(NIB);
		
		ownMediatorCodeSection = new FormViewSection("Códigos de Mediador");
		addSection(ownMediatorCodeSection);
		
		addSection("Morada");
		
		addFormField(address);
		
		clearInfo();
	}
	
	@Override
	public InsuranceAgency getInfo() {
		InsuranceAgency info = value == null ? new InsuranceAgency() : new InsuranceAgency(value);
		info.name = name.getValue();
		info.acronym = acronym.getValue();
		info.ISPNumber = ISPNumber.getValue();
		info.taxNumber = taxNumber.getValue();
		info.address = address.getValue();
		info.NIB = NIB.getValue();
		info.address = address.getValue();
		info.ownMediatorCodes = new String[this.ownMediatorCodeList.size()];
		for(int i = 0; i < info.ownMediatorCodes.length; i++)
			info.ownMediatorCodes[i] = this.ownMediatorCodeList.get(i).getValue();			
		return info;
	}
	
	@Override
	public void setInfo(InsuranceAgency info) {
		if(info == null){
			clearInfo();
			return;
		}
		name.setValue(info.name);
		acronym.setValue(info.acronym);
		ISPNumber.setValue(info.ISPNumber);
		taxNumber.setValue(info.taxNumber);
		address.setValue(info.address);
		NIB.setValue(info.NIB);
		address.setValue(info.address);
		
		this.ownMediatorCodeSection.clear();
		this.ownMediatorCodeList.clear();
		
		for(int i = 0; i < info.ownMediatorCodes.length; i++) {
			addOwnMediatorCodeField(info.ownMediatorCodes[i]);
		}
	}
	
	public void addOwnMediatorCodeField(String value) {
		TextBoxFormField ownMediatorCode = new TextBoxFormField((this.ownMediatorCodeList.size() + 1) + "", new InsuranceAgencyFormValidator.OwnMediatorCodeValidator());
		if(value != null)
			ownMediatorCode.setValue(value);
		ownMediatorCodeList.add(ownMediatorCode);
		ownMediatorCodeSection.addFormField(ownMediatorCode);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(value == null || value.ownMediatorCodes == null || value.ownMediatorCodes.length == 0){
			if(!isReadOnly()){
				TextBoxFormField field = new TextBoxFormField("1", new InsuranceAgencyFormValidator.OwnMediatorCodeValidator());
				this.ownMediatorCodeList.add(field);
				this.ownMediatorCodeSection.addFormField(field);
			}
		}
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		this.ownMediatorCodeList.clear();
		if(value == null || value.ownMediatorCodes == null || value.ownMediatorCodes.length == 0){
			if(!isReadOnly()){
				TextBoxFormField field = new TextBoxFormField("1", new InsuranceAgencyFormValidator.OwnMediatorCodeValidator());
				this.ownMediatorCodeList.add(field);
				this.ownMediatorCodeSection.addFormField(field);
			}else
				this.ownMediatorCodeSection.clear();
		}
	}
}
