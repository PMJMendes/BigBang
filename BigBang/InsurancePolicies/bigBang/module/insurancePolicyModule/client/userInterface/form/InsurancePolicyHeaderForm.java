package bigBang.module.insurancePolicyModule.client.userInterface.form;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Image;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.DayMonthDatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.insurancePolicyModule.client.resources.Resources;
import bigBang.module.insurancePolicyModule.client.userInterface.CoInsurerSelection;
import bigBang.module.insurancePolicyModule.client.userInterface.HeaderFieldsSection;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

public class InsurancePolicyHeaderForm extends FormView<InsurancePolicy>{

	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField number;
	protected NavigationFormField client;
	protected ExpandableListBoxFormField insuranceAgency;
	protected TextBoxFormField categoryLineSubLine;
	protected ExpandableListBoxFormField mediator;
	protected TextBoxFormField policyStatus;
	protected DayMonthDatePickerFormField maturityDate;
	protected DatePickerFormField startDate;
	protected DatePickerFormField endDate;
	protected ExpandableListBoxFormField duration;
	protected ExpandableListBoxFormField fractioning;
	protected NumericTextBoxFormField premium;
	protected ExpandableListBoxFormField operationalProfile;
	protected Image statusIcon;
	protected FormViewSection coInsurersSection;
	protected CheckBoxFormField caseStudy;
	protected CheckBoxFormField coInsurance;
	protected CoInsurerSelection coInsurers;
	private HeaderFieldsSection headerForm;

	public InsurancePolicyHeaderForm() {
		addSection("Cabeçalho de Apólice");
		number  = new TextBoxFormField("Número");
		client = new NavigationFormField("Cliente");
		number.setFieldWidth("175px");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor de Apólice");
		manager.setEmptyValueString("O mesmo do Cliente");
		manager.allowEdition(false);
		insuranceAgency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		insuranceAgency.allowEdition(false);
		insuranceAgency.setMandatory(true);
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		mediator.setEmptyValueString("O mesmo do Cliente");
		mediator.allowEdition(false);
		categoryLineSubLine = new TextBoxFormField("Categoria / Ramo / Modalidade");
		categoryLineSubLine.setEditable(false);
		maturityDate = new DayMonthDatePickerFormField("Dia / Mês de Vencimento");
		duration = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.DURATION, "Duração");
		duration.setMandatory(true);
		duration.allowEdition(false);
		startDate = new DatePickerFormField("Data de Início");
		startDate.setMandatory(true);
		endDate = new DatePickerFormField("Data de Fim");
		fractioning = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.FRACTIONING, "Fraccionamento");
		fractioning.allowEdition(false);
		premium = new NumericTextBoxFormField("Prémio Comercial Anual", true);
		premium.setFieldWidth("175px");
		premium.setUnitsLabel("€");
		operationalProfile = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.OPERATIONAL_PROFILES, "Perfil Operational");
		operationalProfile.setEmptyValueString("O mesmo do Cliente");
		operationalProfile.allowEdition(false);
		caseStudy = new CheckBoxFormField("Case Study");

		//CO-INSURANCE
		coInsurance = new CheckBoxFormField("Co-Seguro");
		coInsurers = new CoInsurerSelection();

		policyStatus = new TextBoxFormField("Estado");
		policyStatus.setFieldWidth("100%");
		policyStatus.setEditable(false);
		statusIcon = new Image();
		policyStatus.add(statusIcon);

		addFormField(client, false);
		addFormField(categoryLineSubLine, false);

		addFormFieldGroup(new FormField<?>[]{
				number,
				insuranceAgency,
				policyStatus,
				premium
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				manager,
				mediator,
				operationalProfile,
				fractioning,
		}, true);

		addFormFieldGroup(new FormField<?>[]{
				duration,
				startDate,
				endDate,
				maturityDate,
		}, true);

		//CO-INSURANCE

		FormField<?>[] group4 = new FormField<?>[]{
				coInsurance,
				caseStudy
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
		this.manager.setEditable(false);
		this.client.setEditable(false);

		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if(maturityDate.getValue() == null && !maturityDate.isReadOnly()){
					maturityDate.setValue(event.getValue());
				}
			}
		});

		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if(maturityDate.getDay().isEmpty() && maturityDate.getMonth().isEmpty()){
					maturityDate.setValue(event.getValue());
				}
			}
		});

		duration.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() != null){
					if(event.getValue().equalsIgnoreCase(BigBangConstants.TypifiedListValues.INSURANCE_POLICY_DURATION.TEMPORARY)){
						maturityDate.clear();
						maturityDate.setEditable(false);
						endDate.setMandatory(true);
					}else{
						maturityDate.setEditable(true);
						maturityDate.setReadOnly(isReadOnly());
						endDate.setMandatory(false);
					}
				}
			}
		});

		coInsurance.setValue(false);
		headerForm = new HeaderFieldsSection();
		addSection(headerForm);

		setValidator(new InsurancePolicyHeaderFormValidator(this));
	}

	@Override
	public InsurancePolicy getInfo() {

		InsurancePolicy result = this.value;

		if(result != null) {
			result.managerId = manager.getValue();
			result.number = number.getValue();
			result.insuranceAgencyId = insuranceAgency.getValue();
			String maturityDay = "";
			String maturityMonth = "";
			if(maturityDate.getStringValue() != null && !maturityDate.getStringValue().isEmpty()){
				String[] maturity = maturityDate.getStringValue().split("-");
				maturityMonth = maturity[0];
				maturityDay = maturity[1];
			}
			try{
				result.maturityDay = Integer.parseInt(maturityDay);
			}catch(Exception e) {
				result.maturityDay = -1;
			}
			try{
				result.maturityMonth = Integer.parseInt(maturityMonth);
			}catch(Exception e) {
				result.maturityMonth = -1;
			}
			result.startDate = startDate.getValue() == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(startDate.getValue());
			result.expirationDate = endDate.getValue() == null ? null :  DateTimeFormat.getFormat("yyyy-MM-dd").format(endDate.getValue());
			result.durationId = duration.getValue();
			result.fractioningId = fractioning.getValue();
			result.premium = premium.getValue();
			result.caseStudy = caseStudy.getValue();
			result.operationalProfileId = operationalProfile.getValue();

			if(coInsurance.getValue()){
				result.coInsurers = coInsurers.getValue();
			}
			else{
				result.coInsurers = null;
			}

			result.mediatorId = mediator.getValue();
			result.headerFields = headerForm.getValue();
		}



		return result;
	}

	@Override
	public void setValue(InsurancePolicy value) {
		super.setValue(value);
		if(value == null){
			headerForm.clear();
			headerForm.setVisible(false);
		}
	}

	@Override
	public void setInfo(InsurancePolicy info) {

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

		this.categoryLineSubLine.setValue(info.categoryName + " / " + info.lineName + " / " + info.subLineName);

		this.mediator.setValue(info.mediatorId);
		try{
			this.maturityDate.setValue(DateTimeFormat.getFormat("MM-dd").parse(info.maturityMonth + "-" + info.maturityDay));

		}catch (IllegalArgumentException e) {
			maturityDate.clear();
		}


		this.fractioning.setValue(info.fractioningId);
		this.premium.setValue(info.premium);

		if(info.clientId != null) {
			ClientProcessBroker clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
			clientBroker.getClient(info.clientId, new ResponseHandler<Client>() {

				@Override
				public void onResponse(Client response) {
					NavigationHistoryItem item = new NavigationHistoryItem();
					item.setParameter("section", "client");
					item.setStackParameter("display");
					item.pushIntoStackParameter("display", "search");
					item.setParameter("clientid", response.id);
					InsurancePolicyHeaderForm.this.client.setValue(item);

					InsurancePolicyHeaderForm.this.client.setValueName("#" + response.clientNumber + " - " + response.name);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
		}

		this.policyStatus.setValue(info.statusText);
		Resources resources = GWT.create(Resources.class);
		if(value.statusIcon == null) {
			statusIcon.setResource(resources.provisionalPolicyIcon());
			statusIcon.setVisible(false);
		}else{
			statusIcon.setVisible(true);
			switch(value.statusIcon){
			case OBSOLETE:
				statusIcon.setResource(resources.inactivePolicyIcon());
				break;
			case PROVISIONAL:
				statusIcon.setResource(resources.provisionalPolicyIcon());
				break;
			case VALID:
				statusIcon.setResource(resources.activePolicyIcon());
				break;
			default:
				return;
			}
		}
		this.caseStudy.setValue(info.caseStudy);
		this.operationalProfile.setValue(info.operationalProfileId);

		if(info.startDate != null)
			startDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.startDate), false);
		else
			startDate.clear();

		if(info.expirationDate != null)
			endDate.setValue(DateTimeFormat.getFormat("yyyy-MM-dd").parse(info.expirationDate));
		else
			endDate.clear();

		this.duration.setValue(info.durationId);

		headerForm.setHeaderText(categoryLineSubLine.getValue());
		headerForm.setValue(info.headerFields);

	}

	public void setHeaderFormVisible(boolean b){
		headerForm.setVisible(b);
	}

	public void allowManagerChange(boolean b) {
		manager.setEditable(b);
	}

}
