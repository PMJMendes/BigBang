package bigBang.module.casualtyModule.client.userInterface.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualty.SubCasualtyFraming.SubCasualtyFramingEntity;
import bigBang.definitions.shared.SubCasualty.SubCasualtyInsurerRequest;
import bigBang.definitions.shared.SubCasualty.SubCasualtyItem;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.UnlimitedTextAreaFormField;
import bigBang.library.client.userInterface.presenter.InsurancePolicySelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.InsuranceSubPolicySelectionViewPresenter;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.InsurancePolicySelectionView;
import bigBang.library.client.userInterface.view.InsuranceSubPolicySelectionView;
import bigBang.module.casualtyModule.client.resources.Resources;
import bigBang.module.casualtyModule.client.userInterface.NewSubCasualtyFramingEntitySection;
import bigBang.module.casualtyModule.client.userInterface.NewSubCasualtyInsurerRequestSection;
import bigBang.module.casualtyModule.client.userInterface.NewSubCasualtyItemSection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class SubCasualtyForm extends FormView<SubCasualty> {

	protected TextBoxFormField number;
	protected ListBoxFormField referenceType;
	protected ExpandableSelectionFormField policyReference, subPolicyReference;
	protected NavigationFormField referenceDetails;
	protected TextBoxFormField insurerProcessNumber;
	protected CheckBoxFormField hasJudicial;
	protected TextBoxFormField status;
	protected UnlimitedTextAreaFormField notes, internalNotes;
	protected NavigationFormField casualty;
	protected ExpandableListBoxFormField insuredObject;
	protected RadioButtonFormField belongsToPolicy;
	protected TextBoxFormField insuredObjectName;
	protected Image statusIcon;
	protected FormField<String> carRepair;

	protected FormViewSection notesSection, internalNotesSection;

	protected Collection<SubCasualtyItemSection> subCasualtyItemSections;
	protected NewSubCasualtyItemSection newItemSection;
	
	protected Collection<SubCasualtyInsurerRequestSection> subCasualtyInsurerRequestSections;
	protected NewSubCasualtyInsurerRequestSection newInsurerRequestSection;
	
	protected Collection<SubCasualtyFramingEntitySection> aditionalEntitiesSection;
	protected NewSubCasualtyFramingEntitySection newEntitySection;
	
	protected SubCasualtyFramingSection framingSection;

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

		referenceDetails = new NavigationFormField("");
		referenceDetails.setEditable(false);

		insurerProcessNumber = new TextBoxFormField("Número de Processo na Seguradora");
		insurerProcessNumber.setFieldWidth("175px");
		status = new TextBoxFormField("Estado");
		status.setEditable(false);
		status.setFieldWidth("100%");
		statusIcon = new Image();
		status.add(statusIcon);

		notes = new UnlimitedTextAreaFormField();
		notes.setMaxCharacters(4000, null);
		notes.setFieldWidth("600px");
		notes.setFieldHeight("250px");
		internalNotes = new UnlimitedTextAreaFormField();
		internalNotes.setMaxCharacters(4000, null);
		internalNotes.setFieldWidth("600px");
		internalNotes.setFieldHeight("250px");
		hasJudicial = new CheckBoxFormField("Tem processo Judicial");

		insuredObject = new ExpandableListBoxFormField("Unidade de Risco");
		insuredObject.setWidth("250px");
		insuredObject.setFieldWidth("231px");
		insuredObjectName = new TextBoxFormField("Unidade de Risco");
		insuredObjectName.setFieldWidth("250px");
		belongsToPolicy = new RadioButtonFormField(true);
		belongsToPolicy.addOption("true", "Presente na apólice");
		belongsToPolicy.addOption("false", "Outra");
		carRepair = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.OTHER_ENTITY, null);
		carRepair.setLabelText("Oficina / Outra Entidade");
		
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
		addFormField(insuredObject, true);
		addFormField(insuredObjectName, true);
		addFormField(belongsToPolicy, true);
		addFormField(carRepair, true);
		
		belongsToPolicy.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equalsIgnoreCase("true")){
					insuredObject.setVisible(true);
					insuredObjectName.setVisible(false);

					setReference(referenceType.getValue(), referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? policyReference.getValue() : subPolicyReference.getValue());
				}else{
					insuredObject.setVisible(false);
					insuredObjectName.setVisible(true);
				}
				insuredObjectName.setValue(null);
				insuredObject.setValue(null);
			}
		});
		
		this.framingSection = new SubCasualtyFramingSection("Enquadramento");
		addSection(framingSection);

		this.aditionalEntitiesSection = new ArrayList<SubCasualtyFramingEntitySection>();

		this.newEntitySection = new NewSubCasualtyFramingEntitySection();
		this.newEntitySection.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addSubCasualtyFramingEntitySection(new SubCasualtyFramingEntity());
			}
		});
		
		addSection(newEntitySection);

		this.subCasualtyItemSections = new ArrayList<SubCasualtyItemSection>();

		this.newItemSection = new NewSubCasualtyItemSection();
		this.newItemSection.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addSubCasualtyItemSection(new SubCasualtyItem());
			}
		});
		
		this.subCasualtyInsurerRequestSections = new ArrayList<SubCasualtyInsurerRequestSection>();

		this.newInsurerRequestSection = new NewSubCasualtyInsurerRequestSection();
		this.newInsurerRequestSection.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addSubCasualtyInsurerRequestSection(new SubCasualtyInsurerRequest());
			}
		});

		notesSection = new FormViewSection("Notas");
		notesSection.addFormField(notes);

		internalNotesSection = new FormViewSection("Notas Internas");
		internalNotesSection.addFormField(internalNotes);

		ValueChangeHandler<String> changeHandler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				if(event.getSource() == referenceType) {
					policyReference.setValue(null, true);
					subPolicyReference.setValue(null, true);
				}
				else if((belongsToPolicy.getValue() != null) && belongsToPolicy.getValue().equalsIgnoreCase("true")) {
					boolean isPolicy = referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY);
					String reference = isPolicy ? policyReference.getValue() : subPolicyReference.getValue();
					setReference(referenceType.getValue(), reference);
				}

				if (referenceType.getValue() != null && (policyReference.getValue()!=null || subPolicyReference.getValue()!=null)) {
					boolean isPolicy = referenceType.getValue().equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY);
					String reference = isPolicy ? policyReference.getValue() : subPolicyReference.getValue();
					framingSection.setIsWorkAccidents(false, isPolicy, reference);
				}
					
				updateItemSections();	
			}
		};
		referenceType.addValueChangeHandler(changeHandler);
		policyReference.addValueChangeHandler(changeHandler);
		subPolicyReference.addValueChangeHandler(changeHandler);

		subPolicyReference.setVisible(false);
		referenceType.setValue(BigBangConstants.EntityIds.INSURANCE_POLICY, true);

		setValue(new SubCasualty());

		setValidator(new SubCasualtyFormValidator(this));
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
			result.insurerRequests = getSubCasualtyInsurerRequests();
			result.framing = framingSection.getFraming();
			if (aditionalEntitiesSection.size() > 0 && result.framing!=null) {
				result.framing.framingEntities = getSubCasualtyFramingEntities();
			}	
			result.insuredObjectId = insuredObject.getValue();
			result.insuredObjectName = insuredObjectName.getValue();
			result.serviceCenterId = carRepair.getValue();
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
				casualty.setValueName(info.inheritCasualtyNumber);
			}else{
				casualty.setValue(null);
				casualty.setValueName(null);
			}

			number.setValue(info.number);
			
			if(info.insuredObjectName != null){
				belongsToPolicy.setValue("false", true);
				insuredObjectName.setValue(info.insuredObjectName);
			}else{
				setReference(info.referenceTypeId, info.referenceId);

				belongsToPolicy.setValue("true", true);
				insuredObject.setValue(info.insuredObjectId);
			}
			
			referenceType.setValue(info.referenceTypeId, true);
			
			if(info.referenceTypeId == null) {
				referenceType.setValue(null, true);
				policyReference.setValue(null);
				subPolicyReference.setValue(null);

				
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
						NavigationHistoryItem item = new NavigationHistoryItem();
						item.setParameter("section", "insurancepolicy");
						item.setStackParameter("display");
						item.pushIntoStackParameter("display", "search");
						item.setParameter("policyid", response.id);

						referenceDetails.setValue(item);
						referenceDetails.setValueName(response.categoryName + " / " + response.lineName + " / " + response.subLineName);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						referenceDetails.setValue(null);
						referenceDetails.setValueName("<não disponível>");
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
						NavigationHistoryItem item = new NavigationHistoryItem();
						item.setParameter("section", "insurancepolicy");
						item.setStackParameter("display");
						item.pushIntoStackParameter("display", "subpolicy");
						item.setParameter("subpolicyid", response.id);

						referenceDetails.setValue(item);
						referenceDetails.setValueName(response.inheritCategoryName + " / " + response.inheritLineName + " / " + response.inheritSubLineName);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						referenceDetails.setValue(null);
						referenceDetails.setValueName("<não disponível>");
					}
				});
			}
			insurerProcessNumber.setValue(info.insurerProcessNumber);
			if(info.id != null){
				status.setValue(info.isOpen ? "Aberto" : "Fechado");
				Resources resources = GWT.create(Resources.class);
				statusIcon.setResource(info.isOpen ? resources.activeCasualtyIcon() : resources.inactiveCasualtyIcon());
			}else{
				status.setValue(null);
				statusIcon.setVisible(false);
			}
			hasJudicial.setValue(info.hasJudicial);
			notes.setValue(info.text);
			internalNotes.setValue(info.internalNotes);
			
			carRepair.setValue(info.serviceCenterId);
			
			setSubCasualtyItems(info.items);
			
			addSection(newItemSection);
			
			framingSection.setFraming(info.framing);
						
			setSubCasualtyInsurerRequests(info.insurerRequests);
			
			if (info.framing!=null) {
				setSubCasualtyFramingEntities(info.framing.framingEntities);
			}
			
			addSection(newInsurerRequestSection);
		} else {
			addSection(newItemSection);
			addSection(newInsurerRequestSection);
		}
		addNotesSections();
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
			addSection(newItemSection);
			addRequests();
			addSection(newInsurerRequestSection);
			addNotesSections();
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
	
	protected void addNotesSections() {
		addSection(notesSection);
		addSection(internalNotesSection);
	}	
	
	protected void addRequests() {
		SubCasualtyInsurerRequest[] subCasualtyInsurerRequests = getSubCasualtyInsurerRequests();
		if (subCasualtyInsurerRequests != null) {
			setSubCasualtyInsurerRequests(subCasualtyInsurerRequests);
		}
	}	

	public void setPanelParameters(HasParameters parameters){
		policyReference.setParameters(parameters);
		String ownerId = parameters.getParameter("ownerid");
		
		if(ownerId != null){
			HasParameters newParameters = new HasParameters();
			newParameters.setParameter("clientid", ownerId);
			subPolicyReference.setParameters(newParameters);
		}
	}

	public void setReference(String referenceTypeId, String referenceId){
		if((referenceId != null) && (referenceId != null)) {
			if(referenceTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)) {
				insuredObject.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECT + "/" + referenceId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}else{
				insuredObject.setListId(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS + "/" + referenceId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}
		}else{
			insuredObject.setListId("", new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}

	public void openNewDetail() {
		addSubCasualtyItemSection(new SubCasualtyItem());
	}
	
	public void openNewInsurerRequest() {
	//	addSubCasualtyInsurerRequestSection(new SubCasualtyInsurerRequest()); TODO: Voltar a pôr isto "coiso" quando passar a ser obrigatório
	}
	
	protected void setSubCasualtyInsurerRequests(SubCasualtyInsurerRequest[] requests) {
		for(FormViewSection section : this.subCasualtyInsurerRequestSections) {
			removeSection(section);
		}
		this.subCasualtyInsurerRequestSections.clear();

		if(requests != null) {
			for(SubCasualtyInsurerRequest request : requests) {
				addSubCasualtyInsurerRequestSection(request);
			}
		}
	}
	
	protected SubCasualtyInsurerRequest[] getSubCasualtyInsurerRequests(){
		SubCasualtyInsurerRequest[] result = new SubCasualtyInsurerRequest[subCasualtyInsurerRequestSections.size()];

		int i = 0;
		for(SubCasualtyInsurerRequestSection section : subCasualtyInsurerRequestSections) {
			result[i] = section.getRequest();
			i++;
		}

		return result;
	}


	protected void addSubCasualtyInsurerRequestSection(SubCasualtyInsurerRequest request){
		if(!request.deleted) {
			
			final SubCasualtyInsurerRequestSection section = new SubCasualtyInsurerRequestSection(request);
			section.setReadOnly(this.isReadOnly());
			this.subCasualtyInsurerRequestSections.add(section);
			addSection(section);

			section.getRemoveButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					removeInsurerRequestAndSection(section);
				}
			});
			if(request.id == null){
				section.requestDate.setValue(new Date(), false);
				section.acceptanceDate.setValue(new Date(), false);
				section.expand();
			}
			addSection(newInsurerRequestSection);
			addNotesSections();
		}
	}
	
	protected void removeInsurerRequestAndSection(SubCasualtyInsurerRequestSection section){
		section.setVisible(false);
		SubCasualtyInsurerRequest request = section.getRequest();
		request.deleted = true;
		section.setRequest(request);
	}
	
	protected void setSubCasualtyFramingEntities(SubCasualtyFramingEntity[] framingEntities) {
		for(SubCasualtyFramingEntitySection section : this.aditionalEntitiesSection) {
			removeSection(section);
		}
		this.aditionalEntitiesSection.clear();

		if(framingEntities != null) {
			for(SubCasualty.SubCasualtyFraming.SubCasualtyFramingEntity entity : framingEntities) {
				addSubCasualtyFramingEntitySection(entity);
			}
		}
	}
	
	protected SubCasualtyFramingEntity[] getSubCasualtyFramingEntities(){
		SubCasualtyFramingEntity[] result = new SubCasualtyFramingEntity[aditionalEntitiesSection.size()];

		int i = 0;
		for(SubCasualtyFramingEntitySection section : aditionalEntitiesSection) {
			result[i] = section.getFramingEntity();
			i++;
		}

		return result;
	}
	
	protected void addSubCasualtyFramingEntitySection(SubCasualtyFramingEntity entity){
		if(!entity.deleted) {

			final SubCasualtyFramingEntitySection section = new SubCasualtyFramingEntitySection(entity);
			section.setReadOnly(this.isReadOnly());
			this.aditionalEntitiesSection.add(section);
			addSection(section);
			
			section.getRemoveButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					removeAditionalEntityAndSection(section);
				}
			});
			if(entity.id == null){
				section.expand();
			}
			
			addSection(newEntitySection);
			setSubCasualtyItems(getSubCasualtyItems());
			addSection(newItemSection);
			addRequests();
			addSection(newInsurerRequestSection);
			addNotesSections();
		} 
	}	
	
	protected void removeAditionalEntityAndSection(SubCasualtyFramingEntitySection section){
		section.setVisible(false);
		SubCasualtyFramingEntity entity = section.getFramingEntity();
		entity.deleted = true;
		section.setFramingEntity(entity);
	}	
	
}
