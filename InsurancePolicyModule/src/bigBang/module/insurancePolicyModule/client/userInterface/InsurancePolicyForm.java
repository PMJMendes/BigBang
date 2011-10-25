package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class InsurancePolicyForm extends FormView<InsurancePolicy> {

	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField number;
	protected ExpandableListBoxFormField insuranceAgency;
	protected ExpandableListBoxFormField category;
	protected ExpandableListBoxFormField line;
	protected ExpandableListBoxFormField subLine;
	protected ExpandableListBoxFormField mediator;
	protected ListBoxFormField maturityDay;
	protected ListBoxFormField maturityMonth;
	protected CheckBoxFormField temporary;
	protected DatePickerFormField endDate;
	//TODO dynamic fields
	protected CheckBoxFormField caseStudy;
	protected TextAreaFormField notes;

	public InsurancePolicyForm(){
		super();
		addSection("Apólice");
		number  = new TextBoxFormField("Número");
		number.setFieldWidth("100px");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		insuranceAgency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		category = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CATEGORY, "Categoria");
		line = new ExpandableListBoxFormField("Ramo");
		subLine = new ExpandableListBoxFormField("Modalidade");
		maturityDay = new ListBoxFormField("Dia de Vencimento");
		maturityMonth = new ListBoxFormField("Mês de Vencimento");
		temporary = new CheckBoxFormField("Temporária");
		endDate = new DatePickerFormField("Data de Fim");
		caseStudy = new CheckBoxFormField("Case Study");
		notes = new TextAreaFormField("Observações");

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

		addFormField(number);
		addFormField(manager);
		addFormField(insuranceAgency);
		addFormField(mediator);
		addFormField(category);
		addFormField(line);
		addFormField(subLine);
		addFormField(maturityDay);
		addFormField(maturityMonth);
		addFormField(temporary);
		addFormField(endDate);
		addFormField(temporary);
		addWidget(new Label("Acrescentar campos dinamicos"));
		addFormField(caseStudy);
		addFormField(notes);

		addSection("Cliente");

		addSection("Seguradora");

		category.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					line.clearValues();
				}else{
					line.setListId(BigBangConstants.EntityIds.LINE+"/"+event.getValue());
				}
			}
		});
		line.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null || event.getValue().isEmpty()){
					subLine.clearValues();
				}else{
					subLine.setListId(BigBangConstants.EntityIds.SUB_LINE+"/"+event.getValue());
				}
			}
		});
	}

	@Override
	public InsurancePolicy getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfo(InsurancePolicy info) {
		if(info == null) {
			clearInfo();
		}else{
			this.manager.setValue(info.managerId);
			this.number.setValue(info.number);
			this.insuranceAgency.setValue(info.insuranceAgencyId);
			this.category.setValue(info.categoryId, true);
			this.line.setValue(info.lineId, true);
			this.subLine.setValue(info.subLineId, true);
			this.mediator.setValue(info.mediatorId);
			this.maturityDay.setValue(info.maturityDay+"");
			this.maturityMonth.setValue(info.maturityMonth+"");
//			//this.temporary.setValue(info.te)
//			protected DatePickerFormField endDate;
//			//TODO dynamic fields
//			protected CheckBoxFormField caseStudy;
//			protected TextAreaFormField notes;
		}
	}

}
