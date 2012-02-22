package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.definitions.shared.InsurancePolicy.ExtraField;
import bigBang.definitions.shared.InsurancePolicy.HeaderField;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.BigBangTypifiedListBroker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.TypifiedListBroker;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.insurancePolicyModule.client.dataAccess.PolicyTypifiedListBroker;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasValue;

public abstract class InsurancePolicyForm extends FormView<InsurancePolicy> {

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

	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField number;
	protected TextBoxFormField client;
	protected ExpandableListBoxFormField insuranceAgency;
	protected ExpandableListBoxFormField category;
	protected ExpandableListBoxFormField line;
	protected ExpandableListBoxFormField subLine;
	protected ExpandableListBoxFormField mediator;
	protected ExpandableListBoxFormField insuredObjects;
	protected ExpandableListBoxFormField exercises;
	protected TextBoxFormField policyStatus;
	protected ListBoxFormField maturityDay;
	protected ListBoxFormField maturityMonth;
	protected DatePickerFormField startDate;
	protected DatePickerFormField endDate;
	protected ExpandableListBoxFormField duration;
	protected ExpandableListBoxFormField fractioning;
	protected PolicyFormTable table;

	protected Map<String, HeaderFormField> headerFields;
	protected FormViewSection headerFieldsSection;

	protected Map<String, HeaderFormField> extraFields;
	protected FormViewSection extraFieldsSection;

	protected CheckBoxFormField caseStudy;
	protected TextAreaFormField notes;
	private CheckBoxFormField coInsurance;
	private CoInsurerSelection coInsurers;

	public InsurancePolicyForm(){
		super();

		headerFields = new HashMap<String, HeaderFormField>();
		extraFields = new HashMap<String, HeaderFormField>();

		addSection("Apólice");
		number  = new TextBoxFormField("Número");
		client = new TextBoxFormField("Cliente");
		number.setFieldWidth("175px");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		insuranceAgency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		category = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CATEGORY, "Categoria");
		line = new ExpandableListBoxFormField("Ramo");
		subLine = new ExpandableListBoxFormField("Modalidade");
		maturityDay = new ListBoxFormField("Dia de Vencimento");
		duration = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.DURATION, "Duração");
		maturityMonth = new ListBoxFormField("Mês de Vencimento");
		startDate = new DatePickerFormField("Data de Início");
		endDate = new DatePickerFormField("Data de Fim");
		fractioning = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.FRACTIONING, "Fraccionamento");
		caseStudy = new CheckBoxFormField("Case Study");

		//CO-INSURANCE
		coInsurance = new CheckBoxFormField("Co-Seguro");
		coInsurers = new CoInsurerSelection();

		notes = new TextAreaFormField();
		notes.setSize("100%", "200px");
		policyStatus = new TextBoxFormField("Estado");
		policyStatus.setFieldWidth("100%");
		policyStatus.setEditable(false);
		table = new PolicyFormTable();
		table.setSize("100%", "100%");

		exercises = new ExpandableListBoxFormField("Exercício");
		insuredObjects = new ExpandableListBoxFormField("Unidade de Risco");

		exercises.setEditable(false);
		insuredObjects.setEditable(false);

		maturityMonth.addItem("Janeiro", "1");
		maturityMonth.addItem("Fevereiro", "2");
		maturityMonth.addItem("Março", "3");
		maturityMonth.addItem("Abril", "4");
		maturityMonth.addItem("Maio", "5");
		maturityMonth.addItem("Junho", "6");
		maturityMonth.addItem("Julho", "7");
		maturityMonth.addItem("Agosto", "8");
		maturityMonth.addItem("Setembro", "9");
		maturityMonth.addItem("Outubro", "10");
		maturityMonth.addItem("Novembro", "11");
		maturityMonth.addItem("Dezembro", "12");

		for(int i = 1; i <= 31; i++){
			maturityDay.addItem(i+"", i+"");
		}

		addFormField(client, false);

		addFormField(number, true);
		addFormField(insuranceAgency, false);

		addFormFieldGroup(new FormField<?>[]{
				number,
				insuranceAgency,
				policyStatus
		}, true);

		FormField<?>[] group1 = new FormField<?>[]{
				manager,
				mediator,
				fractioning,
				duration
		};
		addFormFieldGroup(group1, true);

		FormField<?>[] group2 = new FormField<?>[]{
				startDate,
				endDate,
				maturityDay,
				maturityMonth
		};
		addFormFieldGroup(group2, true);

		FormField<?>[] group3 = new FormField<?>[]{
				caseStudy,
				category,
				line,
				subLine
		};
		addFormFieldGroup(group3, true);

		//CO-INSURANCE

		FormField<?>[] group4 = new FormField<?>[]{
				coInsurance,
				coInsurers
		};

		addFormFieldGroup(group4, true);

		coInsurance.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if(event.getValue()){
					coInsurers.setMainCoInsuranceAgency(insuranceAgency.getValue());
					coInsurers.setVisible(true);
				}
				else{
					coInsurers.setVisible(false);
				}

			}
		});


		insuranceAgency.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				coInsurers.setMainCoInsuranceAgency(event.getValue());

			}
		});

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

		category.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					line.clearValues();
				}else{
					line.setListId(BigBangConstants.EntityIds.LINE+"/"+event.getValue(), null);
				}
			}
		});
		line.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					subLine.clearValues();
				}else{
					subLine.setListId(BigBangConstants.EntityIds.SUB_LINE+"/"+event.getValue(), null);
				}
			}
		});
		subLine.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String value = event.getValue();
				onSubLineChanged(value);
			}
		});

		this.manager.lock(true);
		this.client.setEditable(false);

		clearValue();
		setValue(this.value);
	}

	public abstract void onSubLineChanged(String subLineId);

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
	public InsurancePolicy getInfo() {
		InsurancePolicy result = this.value;

		result.managerId = manager.getValue();
		result.number = number.getValue();
		result.insuranceAgencyId = insuranceAgency.getValue();
		result.categoryId = category.getValue();
		result.lineId = line.getValue();
		result.subLineId = subLine.getValue();
		result.mediatorId = mediator.getValue();
		try{
			result.maturityDay = Integer.parseInt(maturityDay.getValue());
		}catch(Exception e) {
			result.maturityDay = -1;
		}
		try{
			result.maturityMonth = Integer.parseInt(maturityMonth.getValue());
		}catch(Exception e) {
			result.maturityMonth = -1;
		}
		result.startDate = startDate.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(startDate.getValue());
		result.durationId = duration.getValue();
		result.fractioningId = fractioning.getValue();
		result.caseStudy = caseStudy.getValue();
		result.notes = notes.getValue();

		if(coInsurance.getValue()){
			result.coInsurers = coInsurers.getValue();
		}
		else{
			result.coInsurers = null;
		}
		coInsurers.clear();
		
		
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
	public void setInfo(final InsurancePolicy info) {
		if(info == null) {
			clearInfo();
		}else{
			this.manager.setValue(info.managerId);
			this.number.setValue(info.number);
			this.insuranceAgency.setValue(info.insuranceAgencyId);
			this.category.setValue(info.categoryId, false);

			String categoryId = info.categoryId == null ? "" : info.categoryId;
			this.line.setListId(BigBangConstants.EntityIds.LINE+"/"+categoryId, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					String lineId = info.lineId == null ? "" : info.lineId;
					line.setValue(lineId, false);
					subLine.setListId(BigBangConstants.EntityIds.SUB_LINE+"/"+lineId, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							String subLineId = info.subLineId == null ? "" : info.subLineId;
							subLine.setValue(subLineId, false);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}

			});

			this.mediator.setValue(info.mediatorId);
			this.maturityDay.setValue(info.maturityDay+"");
			this.maturityMonth.setValue(info.maturityMonth+"");
			this.duration.setValue(info.durationId);
			this.fractioning.setValue(info.fractioningId);

			if(((InsurancePolicyBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY)).isTemp(info.id)){
				PolicyTypifiedListBroker policyListBroker = PolicyTypifiedListBroker.Util.getInstance();
				InsurancePolicyBroker policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
				this.exercises.setTypifiedDataBroker((TypifiedListBroker) policyListBroker);
				this.exercises.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_EXERCISES+"/"+policyBroker.getEffectiveId(info.id), new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
				this.insuredObjects.setTypifiedDataBroker((TypifiedListBroker) policyListBroker);
				this.insuredObjects.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECTS+"/"+policyBroker.getEffectiveId(info.id), new ResponseHandler<Void>() {

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
				this.exercises.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_EXERCISES+"/"+info.id, new ResponseHandler<Void>() {

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
				this.insuredObjects.setListId(BigBangConstants.EntityIds.INSURANCE_POLICY_INSURED_OBJECTS+"/"+info.id, new ResponseHandler<Void>() {

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

			if(info.clientId != null) {
				ClientProcessBroker clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
				clientBroker.getClient(info.clientId, new ResponseHandler<Client>() {

					@Override
					public void onResponse(Client response) {
						InsurancePolicyForm.this.client.setValue(response.name + " (" + response.clientNumber + ")");
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}


			this.policyStatus.setValue(info.statusText);
			this.caseStudy.setValue(info.caseStudy);
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


			coInsurance.setWidth("343px");

			if(info.coInsurers != null){
				this.coInsurance.setValue(true);
				this.coInsurers.setMainCoInsuranceAgency(info.insuranceAgencyId);
				this.coInsurers.setValue(info.coInsurers);
				this.coInsurers.setEditable(false);
			}
			else{
				this.coInsurance.setValue(false);
				this.coInsurers.setVisible(false);
			}


			setExtraFields(info.extraData);
		}
	}

	public PolicyFormTable getTable(){
		return this.table;
	}

	protected void setCoverages(Coverage[] coverages){
		this.table.setCoverages(coverages);
	}

	@Override
	protected void clearValue() {
		super.clearValue();
		this.value = new InsurancePolicy();
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
}
