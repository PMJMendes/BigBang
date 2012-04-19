package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicy.Coverage;
import bigBang.definitions.shared.SubPolicy.ExtraField;
import bigBang.definitions.shared.SubPolicy.HeaderField;
import bigBang.definitions.shared.SubPolicy.TableSection;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormField;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.insurancePolicyModule.client.dataAccess.SubPolicyTypifiedListBroker;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyClientSelectionViewPresenter;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasValue;

public class SubPolicyForm extends FormView<SubPolicy> {

	protected static class HeaderFormField extends FormField<String> {

		public String id;
		protected FormField<String> field;
		protected HeaderField headerField;
		protected String coverageId;

		public HeaderFormField(HeaderField field){
			super();
			this.id = field.fieldId;
			this.headerField = field;

			if(field instanceof ExtraField){
				coverageId = ((ExtraField)field).coverageId;
			}

			switch(field.type) {
			case LIST:
				ExpandableListBoxFormField listField = new ExpandableListBoxFormField(field.fieldName);
				listField.setEditable(true);
				listField.setListId(BigBangConstants.TypifiedListIds.FIELD_VALUES+"/"+field.fieldId, null);
				this.field = listField;
				break;
			case REFERENCE:
				ExpandableListBoxFormField referenceListField = new ExpandableListBoxFormField(field.fieldName);
				referenceListField.setEditable(true);
				referenceListField.setListId(field.refersToId, null);
				this.field = referenceListField;
				break;
			case NUMERIC:
				this.field = new TextBoxFormField(field.fieldName, new FieldValidator<String>() {

					@Override
					public boolean isValid(String value) {
						try{
							Integer.parseInt(value);
						}catch(Exception e){
							return false;
						}
						return true;
					}

					@Override
					public boolean isMandatory() {
						return false;
					}

					@Override
					public String getErrorMessage() {
						return "Apenas valores numéricos";
					}
				});
				break;
			case TEXT:
				this.field = new TextBoxFormField(field.fieldName);
				break;
			case BOOLEAN:
				RadioButtonFormField radioField = new RadioButtonFormField(field.fieldName);
				radioField.addOption("1", "Sim");
				radioField.addOption("0", "Não");
				this.field = radioField;
				break;
			case DATE:
				//				DatePickerFormField dateField = new DatePickerFormField(field.fieldName);
				//				this.field = dateField;
				break;
			default:
				break;
			}
			this.field.setUnitsLabel(field.unitsLabel);

			initWidget(this.field);
		}

		@Override
		public void clear() {
			this.field.clear();
		}

		@Override
		public void setReadOnly(boolean readonly) {
			this.field.setReadOnly(readonly);
		}

		@Override
		public boolean isReadOnly() {
			return this.field.isReadOnly();
		}

		@Override
		public void setLabelWidth(String width) {
			this.field.setLabelWidth(width);
		}

		@Override
		public void setFieldWidth(String width) {
			this.field.setFieldWidth(width);
		}

		@Override
		public void setValue(String value, boolean fireEvents) {
			this.field.setValue(value, fireEvents);
		}

		@Override
		public String getValue() {
			return this.field.getValue() == null ? null : this.field.getValue().toString();
		}
	}

	protected TextBoxFormField manager; //ro
	protected TextBoxFormField number;
	protected ExpandableSelectionFormField client;
	protected TextBoxFormField insuranceAgency;//ro
	protected TextBoxFormField category;//ro
	protected TextBoxFormField line;//ro
	protected TextBoxFormField subLine;//ro
	protected TextBoxFormField mediator;//ro
	protected ExpandableListBoxFormField insuredObjects;
	protected ExpandableListBoxFormField exercises;//ro
	protected TextBoxFormField policyStatus;//ro
	protected DatePickerFormField startDate;
	protected DatePickerFormField endDate;
	protected ExpandableListBoxFormField fractioning;
	protected TextBoxFormField premium;
	protected SubPolicyFormTable table;

	protected Map<String, HeaderFormField> headerFields;
	protected FormViewSection headerFieldsSection;

	protected Map<String, HeaderFormField> extraFields;
	protected FormViewSection extraFieldsSection;

	protected TextAreaFormField notes;

	public SubPolicyForm(){
		super();
		this.scrollWrapper.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		
		headerFields = new HashMap<String, HeaderFormField>();
		extraFields = new HashMap<String, HeaderFormField>();

		addSection("Apólice Adesão");
		number  = new TextBoxFormField("Número");
		number.setFieldWidth("175px");
		
		ExpandableSelectionFormFieldPanel clientSelectionPanel = (ExpandableSelectionFormFieldPanel) ViewPresenterFactory.getInstance().getViewPresenter("INSURANCE_POLICY_SUB_POLICY_CLIENT_SELECTION");
		((SubPolicyClientSelectionViewPresenter)clientSelectionPanel).go();
		client = new ExpandableSelectionFormField(BigBangConstants.EntityIds.CLIENT, "Cliente Aderente", clientSelectionPanel); //TODO
		client.setFieldWidth("547px");
		manager = new TextBoxFormField("Gestor");
		manager.setEditable(false);
		manager.setFieldWidth("175px");
		insuranceAgency = new TextBoxFormField("Seguradora");
		insuranceAgency.setFieldWidth("175px");
		insuranceAgency.setEditable(false);
		mediator = new TextBoxFormField("Mediador");
		mediator.setEditable(false);
		mediator.setFieldWidth("175px");
		category = new TextBoxFormField("Categoria");
		category.setEditable(false);
		category.setFieldWidth("175px");
		line = new TextBoxFormField("Ramo");
		line.setEditable(false);
		line.setFieldWidth("175px");
		subLine = new TextBoxFormField("Modalidade");
		subLine.setEditable(false);
		subLine.setFieldWidth("175px");
		startDate = new DatePickerFormField("Data de Início");
		endDate = new DatePickerFormField("Data de Fim");
		fractioning = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.FRACTIONING, "Fraccionamento");
		premium = new TextBoxFormField("Prémio Comercial");
		premium.setFieldWidth("175px");
		
		notes = new TextAreaFormField();
		notes.setSize("100%", "200px");
		policyStatus = new TextBoxFormField("Estado");
		policyStatus.setFieldWidth("175px");
		policyStatus.setFieldWidth("100%");
		policyStatus.setEditable(false);
		table = new SubPolicyFormTable();
		table.setSize("100%", "100%");

		exercises = new ExpandableListBoxFormField("Exercício");
		insuredObjects = new ExpandableListBoxFormField("Unidade de Risco");

		exercises.setEditable(false);
		insuredObjects.setEditable(false);

		addFormField(client, false);
		addFormField(number, true);
		addFormField(insuranceAgency, false);

		addFormFieldGroup(new FormField<?>[]{
				number,
				insuranceAgency,
				policyStatus,
				manager
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				mediator,
				fractioning,
				startDate,
				endDate
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				premium,
				category,
				line,
				subLine
		}, true);

		this.headerFieldsSection = new FormViewSection("Informação Específica da Modalidade");
		addSection(headerFieldsSection);

		addSection("Coberturas");
		addFormField(this.insuredObjects, true);
		addFormField(this.exercises, true);

		addWidget(this.table);

		this.extraFieldsSection = new FormViewSection("Informação Extra");
		addSection(this.extraFieldsSection);

		addSection("Notas");
		addFormField(notes);

		clearValue();
		setValue(this.value);
	}

	public void setForEdit(){
		this.manager.setEditable(false);
		this.category.setEditable(false);
		this.line.setEditable(false);
		this.subLine.setEditable(false);
	}

	public void setForNew(){
		this.manager.setEditable(true);
		this.category.setEditable(true);
		this.line.setEditable(true);
		this.subLine.setEditable(true);
	}

	@Override
	public SubPolicy getInfo() {
		SubPolicy result = this.getValue();
		
		result.clientId = client.getValue();
		result.managerId = manager.getValue();
		result.number = number.getValue();
		result.startDate = startDate.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(startDate.getValue());
		result.fractioningId = fractioning.getValue();
		result.notes = notes.getValue();
		result.premium = premium.getValue();
		result.expirationDate = endDate.getStringValue();
		
		result.headerFields = getHeaderFieldsInfo();
		result.tableData = new TableSection[]{this.table.getData()};
		result.coverages = this.table.getCoveragesData();
		//result.extraData = getExtraFieldsInfo();

		return result;
	}

	protected void setHeaderFields(HeaderField[] fields){
		this.headerFieldsSection.clear();
		this.headerFields.clear();

		for(int i = 0; fields != null && i < fields.length; i++) {
			HeaderFormField field = new HeaderFormField(fields[i]);
			field.setReadOnly(this.isReadOnly());
			field.setValue(fields[i].value);
			field.setFieldWidth("175px");
			this.headerFieldsSection.addFormField(field, true);
			this.headerFields.put(fields[i].fieldId, field);
		}
	}

	protected HeaderField[] getHeaderFieldsInfo(){
		HeaderField[] fields = new HeaderField[this.headerFields.size()];
		int i = 0;
		for(HeaderFormField f : this.headerFields.values()) {
			HeaderField headerField = f.headerField;
			headerField.value = f.getValue();
			fields[i] = headerField;
			i++;
		}
		return fields;
	}

	protected void clearHeaderFieldsInfo(){
		for(HeaderFormField f : this.headerFields.values()) {
			f.clear();
		}
	}

	protected void setExtraFields(ExtraField[] fields){
		this.extraFieldsSection.clear();
		this.extraFields.clear();

		for(int i = 0; fields != null && i < fields.length; i++) {
			HeaderFormField field = new HeaderFormField(fields[i]);
			field.setReadOnly(this.isReadOnly());
			field.setFieldWidth("175px");
			this.extraFieldsSection.addFormField(field, true);
			this.extraFields.put(fields[i].fieldId, field);
		}
	}

	protected ExtraField[] getExtraFieldsInfo(){
		ExtraField[] fields = new ExtraField[this.extraFields.size()];
		int i = 0;
		for(HeaderFormField f : this.extraFields.values()) {
			ExtraField extraField = (ExtraField) f.headerField;
			extraField.value = f.getValue();
			fields[i] = extraField;
			i++;
		}
		return fields;
	}

	protected void clearExtraFields(){
		for(HeaderFormField f : this.extraFields.values()) {
			f.clear();
		}
	}

	@Override
	public void setInfo(final SubPolicy info) {
		if(info == null) {
			clearInfo();
			clearValue();
		}else{
			this.manager.setValue(info.managerName);
			this.number.setValue(info.number);
			this.client.setValue(info.clientId);
			this.insuranceAgency.setValue(info.inheritCompanyName);
			this.category.setValue(info.inheritCategoryName);
			this.line.setValue(info.inheritLineName);
			this.subLine.setValue(info.inheritSubLineName);

			this.mediator.setValue(info.inheritMediatorName);
			this.fractioning.setValue(info.fractioningId);

			if(((InsuranceSubPolicyBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)).isTemp(info.id)){
				SubPolicyTypifiedListBroker subPolicyListBroker = SubPolicyTypifiedListBroker.Util.getInstance();
				InsuranceSubPolicyBroker subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
				this.exercises.setTypifiedDataBroker((TypifiedListBroker) subPolicyListBroker);
				this.exercises.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_EXERCISES+"/"+subPolicyBroker.getEffectiveId(info.id), new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
				this.insuredObjects.setTypifiedDataBroker((TypifiedListBroker) subPolicyListBroker);
				this.insuredObjects.setListId(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS+"/"+subPolicyBroker.getEffectiveId(info.id), new ResponseHandler<Void>() {

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
				this.exercises.setTypifiedDataBroker(BigBangTypifiedListBroker.Util.getInstance());
				this.exercises.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_EXERCISES+"/"+info.mainPolicyId, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
				this.insuredObjects.setTypifiedDataBroker(BigBangTypifiedListBroker.Util.getInstance());
				this.insuredObjects.setListId(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY_INSURED_OBJECTS+"/"+info.id, new ResponseHandler<Void>() {

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

			this.policyStatus.setValue(info.statusText);
			this.notes.setValue(info.notes);

			if(info.startDate != null)
				startDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.startDate));
			if(info.expirationDate != null)
				endDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.expirationDate));
			setHeaderFields(info.headerFields);
			table.setColumnDefinitions(info.columns);
			setCoverages(info.coverages);
			if(info.tableData != null && info.tableData.length > 0){
				this.table.setData(info.tableData[0]);
			}else{
				this.table.clear();
			}

			setExtraFields(info.extraData);
		}
	}

	public SubPolicyFormTable getTable(){
		return this.table;
	}

	protected void setCoverages(Coverage[] coverages){
		this.table.setCoverages(coverages);
	}

	@Override
	protected void clearValue() {
		super.clearValue();
		this.value = new SubPolicy();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(this.extraFields != null) {
			for(HeaderFormField f : this.extraFields.values()){
				f.setReadOnly(readOnly);
			}
		}
		if(this.headerFields != null) {
			for(HeaderFormField f : this.headerFields.values()){
				f.setReadOnly(readOnly);
			}
		}
		if(this.table != null){
			this.table.setReadOnly(readOnly);
		}
		if(this.exercises != null) {
			this.exercises.setReadOnly(false);
		}
		if(this.insuredObjects != null) {
			this.insuredObjects.setReadOnly(false);
		}
	}

	public HasValue<String> getExercisesField(){
		return this.exercises;
	}

	public HasValue<String> getInsuredObjectsField(){
		return this.insuredObjects;
	}
	
	@Override
	public void clearInfo() {
		super.clearInfo();
		this.table.clear();
	}
	
}
