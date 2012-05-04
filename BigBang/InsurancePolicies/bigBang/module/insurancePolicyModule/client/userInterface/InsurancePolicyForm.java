package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

public abstract class InsurancePolicyForm extends FormView<InsurancePolicy> {	

	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField number;
	protected TextBoxFormField client;
	protected ExpandableListBoxFormField insuranceAgency;
	protected ExpandableListBoxFormField category;
	protected ExpandableListBoxFormField line;
	protected ExpandableListBoxFormField subLine;
	protected ExpandableListBoxFormField mediator;
	protected TextBoxFormField policyStatus;
	protected ListBoxFormField maturityDay;
	protected ListBoxFormField maturityMonth;
	protected DatePickerFormField startDate;
	protected DatePickerFormField endDate;
	protected ExpandableListBoxFormField duration;
	protected ExpandableListBoxFormField fractioning;
	protected TextBoxFormField premium;

	protected FormViewSection coInsurersSection;

	protected InsurancePolicySubLineTableDataSection tableSection;
	protected HeaderFieldsFormSection headerFieldsSection;
	protected ExtraFieldsFormSection extraFieldsSection;

	protected CheckBoxFormField caseStudy;
	protected TextAreaFormField notes;
	private CheckBoxFormField coInsurance;
	private CoInsurerSelection coInsurers;

	public InsurancePolicyForm(){
		super();
		this.scrollWrapper.getElement().getStyle().setOverflowX(Overflow.SCROLL);

		addSection("Apólice");
		number  = new TextBoxFormField("Número");
		client = new TextBoxFormField("Cliente");
		number.setFieldWidth("175px");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		manager.allowEdition(false);
		insuranceAgency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		insuranceAgency.allowEdition(false);
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		mediator.allowEdition(false);
		category = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CATEGORY, "Categoria");
		category.allowEdition(false);
		line = new ExpandableListBoxFormField("Ramo");
		line.allowEdition(false);
		subLine = new ExpandableListBoxFormField("Modalidade");
		subLine.allowEdition(false);
		maturityDay = new ListBoxFormField("Dia de Vencimento");
		duration = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.DURATION, "Duração");
		maturityMonth = new ListBoxFormField("Mês de Vencimento");
		startDate = new DatePickerFormField("Data de Início");
		endDate = new DatePickerFormField("Data de Fim");
		fractioning = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.FRACTIONING, "Fraccionamento");
		premium = new TextBoxFormField("Prémio Comercial");
		premium.setFieldWidth("175px");
		caseStudy = new CheckBoxFormField("Case Study");

		//CO-INSURANCE
		coInsurance = new CheckBoxFormField("Co-Seguro");
		coInsurers = new CoInsurerSelection();

		notes = new TextAreaFormField();
		notes.setSize("100%", "200px");
		policyStatus = new TextBoxFormField("Estado");
		policyStatus.setFieldWidth("100%");
		policyStatus.setEditable(false);

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
				policyStatus,
				premium
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
				coInsurance
		};

		addFormFieldGroup(group4, true);

		coInsurance.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if(event.getValue()){
					coInsurers.setMainCoInsuranceAgency(insuranceAgency.getValue());
					coInsurers.setVisible(true);
					coInsurers.setHeight("200px");
					coInsurersSection.setVisible(true);
				}
				else{
					coInsurers.clear();
					coInsurersSection.setVisible(false);
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

		this.coInsurersSection = new FormViewSection("Co-Seguro");
		this.coInsurersSection.addFormField(this.coInsurers);
		addSection(this.coInsurersSection);

		this.headerFieldsSection = new HeaderFieldsFormSection();
		addSection(headerFieldsSection);

		this.tableSection = new InsurancePolicySubLineTableDataSection();
		addSection(tableSection);

		this.extraFieldsSection = new ExtraFieldsFormSection();
		addSection(extraFieldsSection);

		addSection("Notas");
		addFormField(notes);

		category.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					line.setListId(null, null);
				}else{
					line.setListId(BigBangConstants.EntityIds.LINE+"/"+event.getValue(), null);
				}
			}
		});
		line.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					subLine.setListId(null, null);
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

		this.manager.setEditable(false);
		this.client.setEditable(false);

		clearValue();
		setValue(this.value);

		setForNew();
	}

	public abstract void onSubLineChanged(String subLineId);

	public void setForEdit(){
		this.manager.setEditable(false);
		this.category.setEditable(false);
		this.line.setEditable(false);
		this.subLine.setEditable(false);
		this.insuranceAgency.setEditable(false);
	}

	public void setForNew(){
		this.manager.setEditable(true);
		this.category.setEditable(true);
		this.line.setEditable(true);
		this.subLine.setEditable(true);
		this.insuranceAgency.setEditable(true);
	}

	@Override
	public InsurancePolicy getInfo() {
		InsurancePolicy result = this.value;

		if(result != null) {
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
			result.premium = premium.getValue();
			result.caseStudy = caseStudy.getValue();
			result.notes = notes.getValue();

			if(coInsurance.getValue()){
				result.coInsurers = coInsurers.getValue();
			}
			else{
				result.coInsurers = null;
			}

			result.headerFields = headerFieldsSection.getPolicyFields();
			result.extraData = extraFieldsSection.getPolicyFields();
			result.coverages = tableSection.getCoverages();
		}

		return result;
	}

	@Override
	public void setInfo(final InsurancePolicy info) {
		if(info == null) {
			clearInfo();
		}else{
			this.coInsurers.clear();
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

			this.manager.setValue(info.managerId);
			this.number.setValue(info.number);
			this.insuranceAgency.setValue(info.insuranceAgencyId);

			this.category.setValue(info.categoryId, false);

			String categoryId = info.categoryId == null ? "" : info.categoryId;
			this.line.setListId(BigBangConstants.EntityIds.LINE+"/"+categoryId, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}

			});

			String lineId = info.lineId == null ? "" : info.lineId;
			line.setValue(lineId, false);
			subLine.setListId(BigBangConstants.EntityIds.SUB_LINE+"/"+lineId, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
			String subLineId = info.subLineId == null ? "" : info.subLineId;
			subLine.setValue(subLineId, false);

			this.mediator.setValue(info.mediatorId);
			this.maturityDay.setValue(info.maturityDay+"");
			this.maturityMonth.setValue(info.maturityMonth+"");
			this.duration.setValue(info.durationId);
			this.fractioning.setValue(info.fractioningId);
			this.premium.setValue(info.premium);

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

			this.headerFieldsSection.setPolicyFields(info.headerFields);
			this.tableSection.setInsurancePolicy(info);
			this.extraFieldsSection.setPolicyFields(info.extraData);
		}
	}

	@Override
	protected void clearValue() {
		super.clearValue();
		this.value = new InsurancePolicy();
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		this.tableSection.setInsurancePolicy(null);
		this.extraFieldsSection.setPolicyFields(null);
		this.headerFieldsSection.setPolicyFields(null);
	}

}
