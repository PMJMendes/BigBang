package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.Map;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.library.client.FormField;
import bigBang.library.client.event.AsyncRequest;
import bigBang.library.client.event.AsyncRequestHandler;
import bigBang.library.client.event.FiresAsyncRequests;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

public class QuoteRequestForm extends FormView<QuoteRequest> implements FiresAsyncRequests {

	protected TextBoxFormField number;
	protected TextBoxFormField client;
	protected TextBoxFormField status;
	protected ExpandableListBoxFormField manager;
	protected TextBoxFormField inheritedMediator;
	protected ExpandableListBoxFormField policiesMediator;
	protected CheckBoxFormField caseStudyFlag;
	protected TextAreaFormField notes;

	//Dynamic stuff
	protected Map<String, FormViewSection> subLineSections;


	public QuoteRequestForm(){
		number = new TextBoxFormField("Número");
		number.setFieldWidth("175px");
		client = new TextBoxFormField("Cliente");
		client.setEditable(false);
		status = new TextBoxFormField("Estado");
		status.setFieldWidth("175px");
		status.setEditable(false);
		manager = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "Gestor");
		inheritedMediator = new TextBoxFormField("Mediador do Cliente");
		inheritedMediator.setEditable(false);
		inheritedMediator.setFieldWidth("175px");
		policiesMediator = new ExpandableListBoxFormField(BigBangConstants.EntityIds.MEDIATOR, "Mediador das Apólices");
		caseStudyFlag = new CheckBoxFormField("Case Study");
		notes = new TextAreaFormField();

		addSection("Consulta de Mercado");

		addFormField(client);
		addFormFieldGroup(new FormField<?>[]{
				number,
				status,
				manager
		}, true);
		addFormFieldGroup(new FormField<?>[]{
				inheritedMediator,
				policiesMediator,
				caseStudyFlag
		}, false);

		addSection("Notas Internas");

		addFormField(notes);

		setupForCreation();
	}

	public void setupForCreation() {
		number.setEditable(true);
		manager.setEditable(true);
		policiesMediator.setEditable(true);
		caseStudyFlag.setEditable(true);
		notes.setEditable(true);
	}

	public void setupForOpenStatus() {
		number.setEditable(false);
		manager.setEditable(false);
		policiesMediator.setEditable(true);
		caseStudyFlag.setEditable(true);
		notes.setEditable(true);
	}

	public void setupForClosedStatus(){
		number.setEditable(false);
		manager.setEditable(false);
		policiesMediator.setEditable(false);
		caseStudyFlag.setEditable(true);
		notes.setEditable(false);
	}

	@Override
	public QuoteRequest getInfo() {
		QuoteRequest result = getValue();

		if(result != null) {
			result.processNumber = number.getValue();
			result.managerId = manager.getValue();
			result.mediatorId = policiesMediator.getValue();
			result.caseStudy = caseStudyFlag.getValue();
			result.requestData = getRequestData();
		}

		return result;
	}

	@Override
	public void setInfo(QuoteRequest info) {
		if(info == null) {
			clearInfo();
		}else{
			if(!info.isOpen){
				setupForClosedStatus();
			}

			number.setValue(info.processNumber);
			client.setValue(info.clientName + " (" + info.clientNumber + ")");
			status.setValue(info.isOpen ? "Aberta" : "Fechada");
			manager.setValue(info.managerId);
			inheritedMediator.setValue(info.inheritMediatorName);
			policiesMediator.setValue(info.mediatorId);
			caseStudyFlag.setValue(info.caseStudy);
			setRequestData(info.requestData);
		}
	}

	protected void setRequestData(RequestSubLine[] data){
		clearRequestDataInfo();
		for(RequestSubLine subLineData : data){
			addSubLineDataSection(subLineData);
		}
	}

	protected RequestSubLine[] getRequestData(){
		RequestSubLine[] result = new RequestSubLine[this.subLineSections.size()];
		
//		int i = 0;
//		for(RequestSubLine subLineData : data){
//			addSubLineDataSection(subLineData);
//		}
		return null; //TODO
	}

	protected void clearRequestDataInfo() {
		for(String subLineId : this.subLineSections.keySet()) {
			FormViewSection section = this.subLineSections.get(subLineId);
			removeSection(section);
		}
	}

	protected void addSubLineDataSection(RequestSubLine subLinedata){
		//TODO
	}
	
	protected SubLineDataSection getSubLineDataSection(String subLineId){
//		return this.subLineSections.get(subLineId); //TODO
		return null;
	}

	protected void removeSubLineDataSection(String subLineId){
		//TODO
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		this.clearRequestDataInfo();
	}

	@Override
	public void registerRequestHandler(AsyncRequestHandler handler) {
		addHandler(handler, AsyncRequest.TYPE);
	}

	@Override
	public void fireRequest(AsyncRequest<?> request) {
		fireEvent(request);
	}

}
