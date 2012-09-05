package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Image;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.DayMonthDatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NavigationFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

public class InsurancePolicyForm extends FormView<InsurancePolicy>{
	
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
	protected TextAreaFormField notes;
	private CheckBoxFormField coInsurance;
	private CoInsurerSelection coInsurers;
	private HeaderFieldsSection headerForm;
	
	public InsurancePolicyForm() {
		addSection("Apólice");
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
		startDate = new DatePickerFormField("Data de Início");
		startDate.setMandatory(true);
		endDate = new DatePickerFormField("Data de Fim");
		fractioning = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.FRACTIONING, "Fraccionamento");
		fractioning.setMandatory(true);
		premium = new NumericTextBoxFormField("Prémio Comercial Anual", true);
		premium.setFieldWidth("175px");
		premium.setUnitsLabel("€");
		operationalProfile = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.OPERATIONAL_PROFILES, "Perfil Operational");
		operationalProfile.setEmptyValueString("O mesmo do Cliente");
		caseStudy = new CheckBoxFormField("Case Study");

		//CO-INSURANCE
		coInsurance = new CheckBoxFormField("Co-Seguro");
		coInsurers = new CoInsurerSelection();

		notes = new TextAreaFormField();
		notes.setSize("100%", "200px");
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
		
		headerForm = new HeaderFieldsSection("TEM DE LEVAR O TEXTO CATEGORIA / RAMO / MODALIDADE");
		addSection(headerForm);

		//TODO TO REMOVE
		coInsurance.setValue(false);
	}
	
	@Override
	public InsurancePolicy getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(InsurancePolicy info) {
		
		
	}

}
