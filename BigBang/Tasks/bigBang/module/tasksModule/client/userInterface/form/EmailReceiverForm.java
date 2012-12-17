package bigBang.module.tasksModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Kind;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.view.FormView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class EmailReceiverForm extends FormView<Message>{

	protected ListBoxFormField referenceType;
	@SuppressWarnings("rawtypes")
	protected FormField[] references;
	protected RadioButtonFormField newOrOldSubject;
	private HorizontalPanel referenceWrapper;
	List<DocInfo> details;
	DocInfo[] docInfo;
	ExpandableListBoxFormField conversationList;
	NumericTextBoxFormField replyLimit;
	private RadioButtonFormField expectsResponse;
	ExpandableListBoxFormField requestType;

	public EmailReceiverForm(){

		addSection("Referência");

		newOrOldSubject = new RadioButtonFormField("Assunto");
		newOrOldSubject.addOption("OLD", "Assunto Activo");
		newOrOldSubject.addOption("NEW", "Novo Assunto");

		addFormField(newOrOldSubject);

		newOrOldSubject.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null){
					newOrOldSubject.setValue("OLD");
				}else{
					referenceWrapper.setVisible(!event.getValue().equalsIgnoreCase("OLD"));
					conversationList.setVisible(event.getValue().equalsIgnoreCase("OLD"));
					validate();
				}
			}
		});

		referenceType = new ListBoxFormField("");
		referenceType.addItem("Cliente", BigBangConstants.EntityIds.CLIENT);
		referenceType.addItem("Apólice nº.", BigBangConstants.EntityIds.INSURANCE_POLICY);
		referenceType.addItem("Apólice Adesão nº.", BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		referenceType.addItem("Sinistro nº.", BigBangConstants.EntityIds.CASUALTY);
		referenceType.addItem("Sub-Sinistro nº.", BigBangConstants.EntityIds.SUB_CASUALTY);
		referenceType.addItem("Despesa de Saúde nº.", BigBangConstants.EntityIds.EXPENSE);


		references = new FormField[6];
		referenceWrapper = new HorizontalPanel();
		referenceWrapper.add(referenceType);
		setReferences();

		referenceType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				changeReference(event.getValue());
			}
		});

		addWidget(referenceWrapper);
		registerFormField(referenceType);		

		conversationList = new ExpandableListBoxFormField("");
		conversationList.setFieldWidth("500px");

		addFormField(conversationList);

		addSection("Receber Mensagem");

		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Mensagem");
		addFormField(requestType);
		
		expectsResponse = new RadioButtonFormField("Espera resposta");
		expectsResponse.addOption("YES", "Sim");
		expectsResponse.addOption("NO", "Não");

		replyLimit = new NumericTextBoxFormField("Prazo de Resposta", false);
		replyLimit.setUnitsLabel("dias");
		replyLimit.setFieldWidth("70px");

		expectsResponse.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equals("YES")){
					replyLimit.clear();
					replyLimit.setEditable(true);
					replyLimit.setReadOnly(false);
				}else{
					replyLimit.clear();
					replyLimit.setEditable(false);
					replyLimit.setReadOnly(true);
				}
			}
		});

		addFormField(expectsResponse, true);
		addFormField(replyLimit, true);

		newOrOldSubject.setValue("OLD");		

		setValidator(new EmailReceiverFormValidator(this));
	}

	protected void changeReference(String value) {

		for(int i = 0; i<references.length; i++){
			references[i].setVisible(false);
			references[i].clear();
		}

		if(value == null){
			return;
		}

		if(value.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			references[0].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
			references[1].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
			references[2].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
			references[3].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
			references[4].setVisible(true);
		}else if(value.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
			references[5].setVisible(true);
		}
	}

	@SuppressWarnings("unchecked")
	private void setReferences() {

		references[0] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.CLIENT, new HasParameters());
		referenceWrapper.add(references[0]);
		registerFormField(references[0]);
		//POLICY 
		references[1] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.INSURANCE_POLICY, new HasParameters());
		referenceWrapper.add(references[1]);
		registerFormField(references[1]);
		//SUB POLICY 
		references[2] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY, new HasParameters());
		referenceWrapper.add(references[2]);
		registerFormField(references[2]);
		//CASUALTY
		references[3] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.CASUALTY, new HasParameters());
		referenceWrapper.add(references[3]);
		registerFormField(references[3]);
		//SUB_CASUALTY
		references[4] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.SUB_CASUALTY, new HasParameters());
		referenceWrapper.add(references[4]);
		registerFormField(references[4]);
		//EXPENSE
		references[5] = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.EXPENSE, new HasParameters());
		referenceWrapper.add(references[5]);
		registerFormField(references[5]);

		for(int i = 1; i<references.length; i++){
			references[i].setVisible(false);
		}		
	}

	@Override
	public Message getInfo() {

		Message message = new Message();
		message.conversationId = conversationList.getValue();
		message.kind = Kind.EMAIL;
		
		return message;

	}

	@Override
	public void setInfo(Message info) {
		return;
	}

	public void setConversations(java.util.List<TipifiedListItem> items){
		conversationList.setTypifiedListItems(items);		
	}

	public Integer getReplyLimit() {
		try{
			return replyLimit.getValue().intValue();
		}catch(Exception e){
			return null;
		}
	}

	public String getParentId() {

		for(int i = 0; i<references.length; i++){
			if(references[i].isVisible()){
				return (String)references[i].getValue();
			}
		}
		return null;
	}

	public String getParentType() {
		return referenceType.getValue();
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		expectsResponse.setValue("YES");
		newOrOldSubject.setValue("OLD");
	}
	
	public String getRequestType(){
		return requestType.getValue();
	}

	
}
