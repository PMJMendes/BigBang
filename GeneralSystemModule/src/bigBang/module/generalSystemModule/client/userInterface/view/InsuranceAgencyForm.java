package bigBang.module.generalSystemModule.client.userInterface.view;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.shared.validator.AddressFieldValidator;
import bigBang.module.generalSystemModule.shared.InsuranceAgency;
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
	
	private Button editCostCenterButton;
	private Button saveCostCenterButton;
	private Button deleteCostCenterButton;
	
	private InsuranceAgency insuranceAgency;
	
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
		
		this.editCostCenterButton = new Button("Editar");	
		this.saveCostCenterButton = new Button("Guardar");
		this.deleteCostCenterButton = new Button("Apagar");
		
		this.addButton(editCostCenterButton);
		this.addButton(saveCostCenterButton);
		this.addButton(deleteCostCenterButton);
		
		addSection("Códigos de Mediador");
		ownMediatorCodeSection = currentSection;		
		
		addSection("Morada");
		
		addFormField(address);
		
		clearInfo();
	}
	
	@Override
	public InsuranceAgency getInfo() {
		insuranceAgency.name = name.getValue();
		insuranceAgency.acronym = acronym.getValue();
		insuranceAgency.ISPNumber = ISPNumber.getValue();
		insuranceAgency.taxNumber = taxNumber.getValue();
		insuranceAgency.address = address.getValue();
		insuranceAgency.NIB = NIB.getValue();
		insuranceAgency.address = address.getValue();
		insuranceAgency.ownMediatorCodes = new String[this.ownMediatorCodeList.size()];
		for(int i = 0; i < insuranceAgency.ownMediatorCodes.length; i++)
			insuranceAgency.ownMediatorCodes[i] = this.ownMediatorCodeList.get(i).getValue();			
		return insuranceAgency;
	}
	
	@Override
	public void setInfo(InsuranceAgency info) {
		this.insuranceAgency = info != null ? (InsuranceAgency) info : new InsuranceAgency();
		name.setValue(insuranceAgency.name);
		acronym.setValue(insuranceAgency.acronym);
		ISPNumber.setValue(insuranceAgency.ISPNumber);
		taxNumber.setValue(insuranceAgency.taxNumber);
		address.setValue(insuranceAgency.address);
		NIB.setValue(insuranceAgency.NIB);
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
		if(insuranceAgency == null || insuranceAgency.ownMediatorCodes == null || insuranceAgency.ownMediatorCodes.length == 0){
			if(!isReadOnly()){
				TextBoxFormField field = new TextBoxFormField("1", new InsuranceAgencyFormValidator.OwnMediatorCodeValidator());
				this.ownMediatorCodeList.add(field);
				this.ownMediatorCodeSection.addFormField(field);
			}else
				this.ownMediatorCodeSection.clear();
		}
		saveCostCenterButton.setVisible(!readOnly);
		editCostCenterButton.setVisible(readOnly);
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		this.ownMediatorCodeList.clear();
		if(insuranceAgency == null || insuranceAgency.ownMediatorCodes == null || insuranceAgency.ownMediatorCodes.length == 0){
			if(!isReadOnly()){
				TextBoxFormField field = new TextBoxFormField("1", new InsuranceAgencyFormValidator.OwnMediatorCodeValidator());
				this.ownMediatorCodeList.add(field);
				this.ownMediatorCodeSection.addFormField(field);
			}else
				this.ownMediatorCodeSection.clear();
		}
	}
}
