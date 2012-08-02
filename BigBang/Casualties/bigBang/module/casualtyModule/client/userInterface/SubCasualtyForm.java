package bigBang.module.casualtyModule.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyItem;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.InsuranceSubPolicySelectionViewPresenter;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.InsurancePolicySelectionView;
import bigBang.library.client.userInterface.view.InsuranceSubPolicySelectionView;

public class SubCasualtyForm extends FormView<SubCasualty> {

	protected TextBoxFormField number;
	protected ListBoxFormField referenceType;
	protected ExpandableSelectionFormField policyReference, subPolicyReference;
	protected TextBoxFormField referenceDetails;
	protected TextBoxFormField insurerProcessNumber;
	protected CheckBoxFormField hasJudicial;
	protected TextBoxFormField status;
	protected TextAreaFormField notes, internalNotes;
	protected NavigationFormField casualty;

	protected FormViewSection notesSection, internalNotesSection;

	protected Collection<SubCasualtyItemSection> subCasualtyItemSections;
	protected NewSubCasualtyItemSection newItemSection;

	public SubCasualtyForm(){
		casualty = new NavigationFormField("Sinistro");
		
		number = new TextBoxFormField("Número");
		number.setFieldWidth("175px");
		number.setEditable(false);
		referenceType = new ListBoxFormField("");
		referenceType.addItem("Apólice nº", BigBangConstants.EntityIds.INSURANCE_POLICY);
		referenceType.addItem("Apólice Adesão nº", BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		referenceType.setMandatory(true);
		referenceType.removeItem(0); //Removes the empty value

		HorizontalPanel referenceWrapper = new HorizontalPanel();
		
		//POLICY REFERENCE
		InsurancePolicySelectionViewPresenter policySelectionPanel = new InsurancePolicySelectionViewPresenter((InsurancePolicySelectionView) GWT.create(InsurancePolicySelectionView.class));
		policySelectionPanel.go();
		policyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, "", policySelectionPanel);
		policyReference.setMandatory(true);
		
		//SUB POLICY REFERENCE
		InsuranceSubPolicySelectionViewPresenter subPolicySelectionPanel = new InsuranceSubPolicySelectionViewPresenter((InsuranceSubPolicySelectionView) GWT.create(InsuranceSubPolicySelectionView.class));
		subPolicySelectionPanel.go();
		subPolicyReference = new ExpandableSelectionFormField(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, "", subPolicySelectionPanel);
		subPolicyReference.setMandatory(true);

		referenceDetails = new TextBoxFormField();
		referenceDetails.setEditable(false);
		
		insurerProcessNumber = new TextBoxFormField("Número de Processo na Seguradora");
		insurerProcessNumber.setFieldWidth("175px");
		status = new TextBoxFormField("Estado");
		status.setEditable(false);
		status.setFieldWidth("175px");
		notes = new TextAreaFormField();
		internalNotes = new TextAreaFormField();
		hasJudicial = new CheckBoxFormField("Tem processo Judicial");

		addSection("Informação Geral");
		addFormField(casualty, false);
		
		referenceWrapper.add(referenceType);
		referenceWrapper.add(policyReference);
		referenceWrapper.add(subPolicyReference);
		referenceWrapper.add(referenceDetails);
		referenceWrapper.setCellVerticalAlignment(referenceType, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(policyReference, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(subPolicyReference, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setCellVerticalAlignment(referenceDetails, HasVerticalAlignment.ALIGN_MIDDLE);
		referenceWrapper.setHeight("45px");
		
		registerFormField(referenceType);
		registerFormField(policyReference);
		registerFormField(subPolicyReference);
		registerFormField(referenceDetails);
		
		addWidget(referenceWrapper);
		
		referenceType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String type = event.getValue();

				if(type == null || type.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
					policyReference.setVisible(true);
					subPolicyReference.setVisible(false);
				}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
					policyReference.setVisible(false);
					subPolicyReference.setVisible(true);
				}
				referenceDetails.clear();
			}
		});
		
		addFormField(number, true);
		addFormField(insurerProcessNumber, true);
		addFormField(status, true);
		addFormField(hasJudicial, false);

		this.subCasualtyItemSections = new ArrayList<SubCasualtyItemSection>();

		this.newItemSection = new NewSubCasualtyItemSection();
		this.newItemSection.newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addSubCasualtyItemSection(new SubCasualtyItem());
			}
		});
		
		notesSection = new FormViewSection("Notas");
		notesSection.addFormField(notes);
		notes.setFieldWidth("600px");
		notes.setFieldHeight("250px");
		
		internalNotesSection = new FormViewSection("Notas Internas");
		internalNotesSection.addFormField(internalNotes);
		internalNotes.setFieldWidth("600px");
		internalNotes.setFieldHeight("250px");
		
		ValueChangeHandler<String> changeHandler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getSource() == referenceType) {
					policyReference.setValue(null, true);
					subPolicyReference.setValue(null, true);
				}
				
				updateItemSections();
			}
		};
		referenceType.addValueChangeHandler(changeHandler);
		policyReference.addValueChangeHandler(changeHandler);
		subPolicySelectionPanel.addValueChangeHandler(changeHandler);
		
		subPolicyReference.setVisible(false);
		referenceType.setValue(BigBangConstants.EntityIds.INSURANCE_POLICY, true);
		
		setValue(new SubCasualty());
	}

	@Override
	public SubCasualty getInfo() {
		SubCasualty result = getValue();

		if(result != null) {
			result.number = number.getValue();
			result.insurerProcessNumber = insurerProcessNumber.getValue();
			result.hasJudicial = hasJudicial.getValue();
			result.text = notes.getValue();
			result.internalNotes = internalNotes.getValue();
			result.referenceTypeId = referenceType.getValue();
			result.referenceId = result.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? policyReference.getValue() : subPolicyReference.getValue();
			result.text = notes.getValue();
			result.internalNotes = internalNotes.getValue();
			result.items = getSubCasualtyItems();
		}

		return result;
	}

	@Override
	public void setInfo(SubCasualty info) {
		if(info != null) {
			if(info.casualtyId != null){
				NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.setParameter("section", "casualty");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.setParameter("casualtyid", info.casualtyId);
				casualty.setValue(navigationItem);
				
				CasualtyDataBroker casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
				casualtyBroker.getCasualty(info.casualtyId, new ResponseHandler<Casualty>() {

					@Override
					public void onResponse(Casualty response) {
						casualty.setValueName("#" + response.processNumber + " - " + response.clientName);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}else{
				casualty.setValue(null);
				casualty.setValueName(null);
			}
			
			number.setValue(info.number);
			referenceType.setValue(info.referenceTypeId, true);
			if(info.referenceTypeId == null) {
				referenceType.setValue(null, true);
				policyReference.setValue(null);
				subPolicyReference.setValue(null);
				
				//clear reference details TODO
				referenceDetails.clear();
			}else if(info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
				referenceType.setValue(BigBangConstants.EntityIds.INSURANCE_POLICY, true);
				policyReference.setValue(info.referenceId);
				subPolicyReference.setValue(null);
				
				//Show details for policy
				InsurancePolicyBroker policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
				policyBroker.getPolicy(info.referenceId, new ResponseHandler<InsurancePolicy>() {
					
					@Override
					public void onResponse(InsurancePolicy response) {
						referenceDetails.setValue(response.categoryName + " / " + response.lineName + " / " + response.subLineName);
					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {
						referenceDetails.setValue("<não disponível>");
					}
				});
				
			}else if(info.referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
				referenceType.setValue(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, true);
				policyReference.setValue(null);
				subPolicyReference.setValue(info.referenceId);
				
				//Show details for sub policy
				InsuranceSubPolicyBroker subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
				subPolicyBroker.getSubPolicy(info.referenceId, new ResponseHandler<SubPolicy>() {

					@Override
					public void onResponse(SubPolicy response) {
						referenceDetails.setValue(response.inheritCategoryName + " / " + response.inheritLineName + " / " + response.inheritSubLineName);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						referenceDetails.setValue("<não disponível>");
					}
				});
			}
			insurerProcessNumber.setValue(info.insurerProcessNumber);
			status.setValue(info.isOpen ? "Aberto" : "Fechado");
			hasJudicial.setValue(info.hasJudicial);
			notes.setValue(info.text);
			internalNotes.setValue(info.internalNotes);

			setSubCasualtyItems(info.items);
		}
		
		addLastFields();
	}

	protected void setSubCasualtyItems(SubCasualtyItem[] items) {
		for(FormViewSection section : this.subCasualtyItemSections) {
			removeSection(section);
		}
		this.subCasualtyItemSections.clear();

		if(items != null) {
			for(SubCasualtyItem item : items) {
				addSubCasualtyItemSection(item);
			}
		}
	}

	protected SubCasualtyItem[] getSubCasualtyItems(){
		SubCasualtyItem[] result = new SubCasualtyItem[subCasualtyItemSections.size()];

		int i = 0;
		for(SubCasualtyItemSection section : subCasualtyItemSections) {
			result[i] = section.getItem();
			i++;
		}

		return result;
	}

	protected void addSubCasualtyItemSection(SubCasualtyItem item){
		if(!item.deleted) {
			String referenceId = referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? policyReference.getValue() : subPolicyReference.getValue();

			final SubCasualtyItemSection section = new SubCasualtyItemSection(item, referenceType.getValue(), referenceId);
			section.setReadOnly(this.isReadOnly());
			this.subCasualtyItemSections.add(section);
			addSection(section);

			section.getRemoveButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					removeItemAndSection(section);
				}
			});
			if(item.id == null){
				section.expand();
			}
			addLastFields();
		}
	}
	
	protected void updateItemSections() {
		String referenceId = referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? policyReference.getValue() : subPolicyReference.getValue();
		for(SubCasualtyItemSection section : subCasualtyItemSections) {
			section.setReference(referenceType.getValue(), referenceId);
		}
	}
	
	protected void removeItemAndSection(SubCasualtyItemSection section){
		section.setVisible(false);
		SubCasualtyItem item = section.getItem();
		item.deleted = true;
		String referenceId = referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? policyReference.getValue() : subPolicyReference.getValue();
		section.setItem(item, referenceType.getValue(), referenceId);
	}

	protected void addLastFields() {
		addSection(newItemSection);
		addSection(notesSection);
		addSection(internalNotesSection);
	}

}
