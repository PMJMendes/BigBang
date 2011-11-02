package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.insurancePolicyModule.shared.ModuleConstants;

public class InsurancePolicyForm extends FormView<InsurancePolicy> {

	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField number;
	protected TextBoxFormField client;
	protected ExpandableListBoxFormField insuranceAgency;
	protected ExpandableListBoxFormField category;
	protected ExpandableListBoxFormField line;
	protected ExpandableListBoxFormField subLine;
	protected ExpandableListBoxFormField mediator;
	protected ListBoxFormField maturityDay;
	protected ListBoxFormField maturityMonth;
	protected DatePickerFormField endDate;
	protected ExpandableListBoxFormField duration;
	protected ExpandableListBoxFormField fractioning;
	//TODO dynamic fields
	protected CheckBoxFormField caseStudy;
	protected TextAreaFormField notes;

	public InsurancePolicyForm(){
		super();
		addSection("Apólice");
		number  = new TextBoxFormField("Número");
		client = new TextBoxFormField("Cliente");
		number.setFieldWidth("100px");
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		insuranceAgency = new ExpandableListBoxFormField(BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		mediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		category = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CATEGORY, "Categoria");
		line = new ExpandableListBoxFormField("Ramo");
		subLine = new ExpandableListBoxFormField("Modalidade");
		maturityDay = new ListBoxFormField("Dia de Vencimento");
		duration = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.DURATION, "Duração");
		maturityMonth = new ListBoxFormField("Mês de Vencimento");
		endDate = new DatePickerFormField("Data de Fim");
		fractioning = new ExpandableListBoxFormField(ModuleConstants.TypifiedListIds.FRACTIONING, "Fraccionamento");
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
		addFormField(client);
		addFormField(manager);
		addFormField(insuranceAgency);
		addFormField(mediator);
		addFormField(category);
		addFormField(line);
		addFormField(subLine);
		addFormField(this.duration);
		addFormField(maturityDay);
		addFormField(maturityMonth);
		addFormField(endDate);
		addFormField(fractioning);
		addFormField(caseStudy);
		
		addWidget(new Label("Acrescentar campos dinamicos"));
		
		addFormField(notes);

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
		
		this.manager.lock(true);
		this.client.setEditable(false);
		
		this.setValue(new InsurancePolicy());
	}

	public void allowManagerEdition(boolean allow) {
		this.manager.setEditable(allow);
	}
	
	@Override
	public InsurancePolicy getInfo() {
		InsurancePolicy result = new InsurancePolicy();
		
		result.managerId = manager.getValue();
		result.number = number.getValue();
		result.insuranceAgencyId = insuranceAgency.getValue();
		result.categoryId = category.getValue();
		result.lineId = line.getValue();
		result.subLineId = subLine.getValue();
		result.mediatorId = mediator.getValue();
		result.maturityDay = Integer.parseInt(maturityDay.getValue());
		result.maturityMonth = Integer.parseInt(maturityMonth.getValue());
		result.startDate = "2011-11-01 00:00:00"; //TODO 
		//result. TODO END DATE
		result.durationId = duration.getValue();
		result.fractioningId = duration.getValue();
		result.caseStudy = caseStudy.getValue();
		result.notes = notes.getValue();
		
		return result;
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
			
			this.line.setListId(BigBangConstants.EntityIds.LINE+"/"+info.categoryId);
			this.line.setValue(info.lineId, false);
			this.subLine.setListId(BigBangConstants.EntityIds.SUB_LINE+"/"+info.lineId);
			this.subLine.setValue(info.subLineId, false);
			
			this.mediator.setValue(info.mediatorId);
			this.maturityDay.setValue(info.maturityDay+"");
			this.maturityMonth.setValue(info.maturityMonth+"");
			this.duration.setValue(info.durationId);
			this.fractioning.setValue(info.fractioningId);
			
			ClientProcessBroker clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
			clientBroker.getClient(info.clientId, new ResponseHandler<Client>() {
				
				@Override
				public void onResponse(Client response) {
					InsurancePolicyForm.this.client.setValue(response.name + " (" + response.clientNumber + ")");
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {}
			});
			
			
			
					
			//			protected DatePickerFormField endDate;
//			//TODO dynamic fields
			
			this.caseStudy.setValue(info.caseStudy);
			this.notes.setValue(info.notes);
		}
	}

}
