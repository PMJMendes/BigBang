package bigBang.module.generalSystemModule.client.userInterface.form;

import java.util.ArrayList;

import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class InsuranceAgencyForm extends FormView<InsuranceAgency> {

	protected TextBoxFormField name;
	protected TextBoxFormField acronym;
	protected TextBoxFormField ISPNumber;
	protected ArrayList<TextBoxFormField> ownMediatorCodeList;
	protected TextBoxFormField taxNumber;
	protected TextBoxFormField NIB;
	protected TextBoxFormField acctCode;
	protected AddressFormField address;
	
	protected FormViewSection ownMediatorCodeSection;
	
	public InsuranceAgencyForm(){
		ownMediatorCodeList = new ArrayList<TextBoxFormField>();
		
		name = new TextBoxFormField("Nome");
		acronym = new TextBoxFormField("Sigla");
		acronym.setFieldWidth("100px");
		ISPNumber = new TextBoxFormField("Número do ISP");
		ISPNumber.setFieldWidth("100px");
		taxNumber = new TextBoxFormField("NIF/NIPC");
		taxNumber.setFieldWidth("100px");
		acctCode = new TextBoxFormField("Terminação (72113 XXX 1)");
		acctCode.setFieldWidth("100px");
		NIB = new TextBoxFormField("NIB");
		address = new AddressFormField();

		addSection("Informação Geral");
		
		addFormField(name);
		addFormField(acronym);
		addFormField(taxNumber);
		addFormField(ISPNumber);
		addFormField(acctCode);
		addFormField(NIB);
		
		ownMediatorCodeSection = new FormViewSection("Códigos de Mediador");
		addSection(ownMediatorCodeSection);
		
		addSection("Morada");
		
		addFormField(address);
		
		clearInfo();
		setValidator(new InsuranceAgencyFormValidator(this));
	}
	
	@Override
	public InsuranceAgency getInfo() {
		InsuranceAgency info = value == null ? new InsuranceAgency() : new InsuranceAgency(value);
		info.name = name.getValue();
		info.acronym = acronym.getValue();
		info.ISPNumber = ISPNumber.getValue();
		info.taxNumber = taxNumber.getValue();
		info.accountingCode = acctCode.getValue();
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
		acctCode.setValue(info.accountingCode);
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
		TextBoxFormField ownMediatorCode = new TextBoxFormField((this.ownMediatorCodeList.size() + 1) + "");
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
				TextBoxFormField field = new TextBoxFormField("1");
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
				TextBoxFormField field = new TextBoxFormField("1");
				this.ownMediatorCodeList.add(field);
				this.ownMediatorCodeSection.addFormField(field);
			}else
				this.ownMediatorCodeSection.clear();
		}
	}
}
