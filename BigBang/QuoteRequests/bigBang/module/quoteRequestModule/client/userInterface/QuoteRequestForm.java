package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.HashMap;
import java.util.Map;

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
	protected Map<String, SubLineDataSection> subLineSections;
	protected FormViewSection notesSection;


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

		this.notesSection = new FormViewSection("Notas Internas");
		this.notesSection.addFormField(notes);
		addSection(this.notesSection);

		this.subLineSections = new HashMap<String, SubLineDataSection>();
		
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
			result.requestData = getRequestSubLineData();
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
		addSection(notesSection);
	}

	protected RequestSubLine[] getRequestSubLineData(){
		RequestSubLine[] result = new RequestSubLine[this.subLineSections.size()];
		
		int i = 0;
		for(SubLineDataSection section : this.subLineSections.values()) {
			result[i] = section.getSubLineData();
			i++;
		}

		return result;
	}

	protected void clearRequestDataInfo() {
		for(String subLineId : this.subLineSections.keySet()) {
			FormViewSection section = this.subLineSections.get(subLineId);
			removeSection(section);
		}
	}

	protected void addSubLineDataSection(RequestSubLine subLinedata){
		SubLineDataSection section = new SubLineDataSection(subLinedata);
		this.subLineSections.put(subLinedata.qrslId, section);
		addSection(section);		
	}
	
	protected SubLineDataSection getSubLineDataSection(String subLineId){
		return this.subLineSections.get(subLineId);
	}

	protected void removeSubLineDataSection(String subLineId){
		this.removeSection(this.subLineSections.get(subLineId));
		this.subLineSections.remove(subLineId);
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
