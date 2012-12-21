package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.ProcessBase;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Selectable;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.ConversationBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.MessagesList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.form.SubCasualtyForm;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyHeaderForm;
import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyHeaderForm;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ConversationTasksViewPresenter implements ViewPresenter, HasOperationPermissions, ConversationBrokerClient{

	public static enum Action {
		GO_TO_PROCESS, CLICK_CLOSE, CLICK_SEND, CLICK_RECEIVE, SEND, CANCEL, RECEIVE, CLICK_REPEAT
	}

	public static interface Display{
		HasEditableValue<Conversation> getForm();
		View getParentForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		//PERMISSIONS
		void clearAllowedPermissions();
		Widget asWidget();
		void allowClose(boolean b);
		void allowReceive(boolean b);
		void allowRepeat(boolean b);
		void allowSend(boolean b);
		void setOwnerForm(View form);
		void setOwnerFormValue(ProcessBase response);
		void setOwner(String ownerId);
		List<Message> getMessageList();
		void setMessage(Message m);
		HasEditableValue<Conversation> getSendMessageForm();
		void setFormVisible(boolean isSendMessage);
		void lockAllMainToolbar();
		HasEditableValue<Conversation> getReceiveMessageForm();
		void closeSubForms();
		void addContact(String string, String id, String client);
		void showOverlayViewContainer(boolean b);
		HasWidgets getOverlayViewContainer();
		void setMainFormVisible(boolean b);
	}

	protected boolean bound = false;
	protected ConversationBroker broker;
	protected Display view;
	String conversationId;
	protected Message currentMessage;
	protected Conversation conversation;
	private boolean isSendMessage;
	private boolean toSend;
	private ViewPresenterController overlayController;

	public ConversationTasksViewPresenter(Display view) {
		setView((UIObject) view);
		this.broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);
	}
	@Override
	public void setPermittedOperations(String[] operationIds) {
		view.clearAllowedPermissions();
		for(String opid : operationIds){
			if(BigBangConstants.OperationIds.ConversationProcess.CLOSE.equalsIgnoreCase(opid)){
				view.allowClose(true);
			}else if(BigBangConstants.OperationIds.ConversationProcess.RECEIVE.equalsIgnoreCase(opid)){
				view.allowReceive(true);
			}else if(BigBangConstants.OperationIds.ConversationProcess.REPEAT.equalsIgnoreCase(opid)){
				view.allowRepeat(true);
			}else if(BigBangConstants.OperationIds.ConversationProcess.SEND.equalsIgnoreCase(opid)){
				view.allowSend(true);
			}
		}
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
		initController();
	}

	private void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<ConversationTasksViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case GO_TO_PROCESS:
					NavigationHistoryManager.getInstance().NavigateToProcess(BigBangConstants.EntityIds.CONVERSATION,conversationId);
					break;
				case CANCEL:
					onCancel();
					break;
				case CLICK_CLOSE:
					onClickClose();
					break;
				case CLICK_RECEIVE:
					onClickReceive();
					break;
				case CLICK_SEND:
					onClickSend();
					break;
				case RECEIVE: 
					onReceive();
					break;
				case SEND:
					if(toSend)
						onSend();
					else 
						onRepeatMessage();
					break;
				case CLICK_REPEAT:
					onClickRepeat();
					break;
				}
				
			}
		});

		view.getMessageList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				resetView();
				if(event.getSelected() != null && event.getSelected().size() > 0){

					Message m = new Message();

					for(Selectable sel : event.getSelected()){
						m = ((MessagesList.Entry)sel).getValue();
					}
					currentMessage = m;
					setPermissions();
					view.setMessage(m);
				}
			}
		});

		bound = true;
	}
	protected void onSend() {
		if(view.getSendMessageForm().validate()){
			broker.sendMessage(view.getSendMessageForm().getInfo().messages[0], view.getSendMessageForm().getInfo().replylimit, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					conversation = response;
					resetView();
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem enviada com sucesso"), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar a mensagem."), TYPE.ALERT_NOTIFICATION));					

				}
			});
		}
		else{
			onFormValidationFailed();
		}	}
	protected void onReceive() {
		if(view.getReceiveMessageForm().validate()){
			Conversation conv = view.getReceiveMessageForm().getInfo();

			broker.receiveMessage(conv.messages[0], conv.replylimit, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					conversation = response;
					resetView();
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem enviada com sucesso"), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível receber a mensagem."), TYPE.ALERT_NOTIFICATION));					
				}
			});
		}
		else{
			onFormValidationFailed();
		}
	}
	protected void onRepeatMessage() {
		if(view.getSendMessageForm().validate()){
			broker.repeatMessage(view.getSendMessageForm().getInfo().messages[0], view.getSendMessageForm().getInfo().replylimit, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					conversation = response;
					resetView();
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem enviada com sucesso"), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível repetir a mensagem."), TYPE.ALERT_NOTIFICATION));					
				}
			});
		}
		else{
			onFormValidationFailed();
		}
	}
	protected void onClickSend() {
		isSendMessage = true;
		toSend = true;
		Conversation conv = view.getForm().getValue();
		conv.messages = new Message[1];
		view.getSendMessageForm().setValue(conv);
		view.setFormVisible(isSendMessage);
		view.setMainFormVisible(false);
	}
	protected void onClickRepeat() {
		toSend = false;
		isSendMessage = true;
		view.setFormVisible(isSendMessage);
		Conversation conv = view.getForm().getValue();
		conv.messages = new Message[1];
		conv.messages[0] = (currentMessage);
		view.getSendMessageForm().setValue(conv);
		view.setMainFormVisible(false);

	}
	protected void onClickReceive() {
		isSendMessage = false;
		Conversation conv = view.getForm().getValue();
		conv.messages = new Message[1];
		view.getReceiveMessageForm().setValue(conv);
		view.setFormVisible(isSendMessage);
		view.setMainFormVisible(false);
	}
	protected void onClickClose() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "conversationclose");
		item.setParameter("conversationid", conversation.id);
		this.overlayController.onParameters(item);
	}
	protected void onCancel() {
		resetView();
	}
	protected void resetView() {
		view.closeSubForms();
		setPermissions();
		view.setMainFormVisible(true);
	}
	@Override
	public void setParameters(HasParameters parameterHolder) {
		conversationId = parameterHolder.getParameter("id");

		if(conversationId != null){
			getConversation();
		}
	}

	private void getConversation() {
		broker.getConversation(conversationId, new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				setOwner(response);
				view.getForm().setValue(response);
				conversation = response;
				view.setOwner(conversation.id);
				setPermissions();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar a troca de mensagens"), TYPE.ALERT_NOTIFICATION));

			}
		});
	}
	protected void setOwner(Conversation response) {
		if(BigBangConstants.EntityIds.CLIENT.equalsIgnoreCase(response.parentDataTypeId)){
			ClientProcessBroker broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
			broker.getClient(response.parentDataObjectId, new ResponseHandler<Client>() {

				@Override
				public void onResponse(Client response) {
					view.setOwnerForm(new ClientForm());
					view.setOwnerFormValue(response);
					
					view.addContact("Cliente (" + response.name +")", response.id, BigBangConstants.EntityIds.CLIENT);
					view.addContact("Mediador (" + response.mediatorName + ")", response.mediatorId, BigBangConstants.EntityIds.MEDIATOR);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});
		}else if(BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(response.parentDataTypeId)){
			InsurancePolicyBroker broker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
			broker.getPolicy(response.parentDataObjectId, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					view.setOwnerForm(new InsurancePolicyHeaderForm());
					view.setOwnerFormValue(response);
					
					view.addContact("Apólice (" + response.number + ")", response.id, BigBangConstants.EntityIds.INSURANCE_POLICY);
					view.addContact("Seguradora (" + response.insuranceAgencyName + ")", response.insuranceAgencyId, BigBangConstants.EntityIds.INSURANCE_AGENCY);
					view.addContact("Mediador (" + response.inheritMediatorName + ")", response.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
					view.addContact("Cliente (" + response.clientName + ")", response.clientId, BigBangConstants.EntityIds.CLIENT);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});
		}else if(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(response.parentDataTypeId)){
			InsuranceSubPolicyBroker broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
			broker.getSubPolicy(response.parentDataObjectId, new ResponseHandler<SubPolicy>() {

				@Override
				public void onResponse(SubPolicy response) {
					view.setOwnerForm(new SubPolicyHeaderForm());
					view.setOwnerFormValue(response);
					
					view.addContact("Apólice Adesão (" + response.number + ")", response.id, BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
					view.addContact("Apólice (" + response.mainPolicyNumber + ")", response.mainPolicyId, BigBangConstants.EntityIds.INSURANCE_POLICY);
					view.addContact("Tomador (" + response.inheritClientName + ")", response.inheritClientId, BigBangConstants.EntityIds.CLIENT);
					view.addContact("Subscritor (" + response.clientName + ")", response.clientId, BigBangConstants.EntityIds.CLIENT);

				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});
		}else if(BigBangConstants.EntityIds.CASUALTY.equalsIgnoreCase(response.parentDataTypeId)){
			CasualtyDataBroker broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
			broker.getCasualty(response.parentDataObjectId, new ResponseHandler<Casualty>() {

				@Override
				public void onResponse(Casualty response) {
					view.setOwnerForm(new CasualtyForm());
					view.setOwnerFormValue(response);
					
					view.addContact("Sinistro (" + response.processNumber + ")", response.id, BigBangConstants.EntityIds.CASUALTY);
					view.addContact("Cliente (" + response.clientName + ")", response.clientId, BigBangConstants.EntityIds.CLIENT);		
					view.addContact("Mediador (" + response.inheritMediatorName + ")", response.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);

				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});

		}else if(BigBangConstants.EntityIds.SUB_CASUALTY.equalsIgnoreCase(response.parentDataTypeId)){
			SubCasualtyDataBroker broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
			broker.getSubCasualty(response.parentDataObjectId, new ResponseHandler<SubCasualty>() {

				@Override
				public void onResponse(SubCasualty response) {
					view.setOwnerForm(new SubCasualtyForm());
					view.setOwnerFormValue(response);
					
					view.addContact("Sub-Sinistro (" + response.number + ")", response.id , BigBangConstants.EntityIds.SUB_CASUALTY);
					view.addContact("Apólice " + (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(response.referenceTypeId) ? "Adesão " : "") + "(" + response.referenceNumber + ")", response.referenceId, response.referenceTypeId);
					view.addContact("Seguradora (" + response.inheritInsurerName + ")", response.inheritInsurerId, BigBangConstants.EntityIds.INSURANCE_AGENCY);
					if (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(response.referenceTypeId)) {
						view.addContact("Cliente Principal (" + response.inheritMasterClientName + ")", response.inheritMasterClientId, BigBangConstants.EntityIds.CLIENT);
						view.addContact("Mediador do Cliente Principal (" + response.inheritMasterMediatorName + ")", response.inheritMasterMediatorId, BigBangConstants.EntityIds.MEDIATOR);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});

		}else if(BigBangConstants.EntityIds.NEGOTIATION.equalsIgnoreCase(response.parentDataTypeId)){
			NegotiationBroker broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
			broker.getNegotiation(response.parentDataObjectId, new ResponseHandler<Negotiation>() {

				@Override
				public void onResponse(Negotiation response) {
					view.setOwnerForm(new NegotiationForm());
					view.setOwnerFormValue(response);
					
					view.addContact("Negociação (" + response.companyName + ")",response.id, BigBangConstants.EntityIds.NEGOTIATION);
					view.addContact((response.ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY) ? "Apólice" : "Consulta de Mercado") + " (" + response.ownerLabel + ")",response.ownerId, response.ownerTypeId);
					view.addContact("Seguradora (" + response.companyName + ")", response.companyId, BigBangConstants.EntityIds.INSURANCE_AGENCY);

					if(BigBangConstants.EntityIds.INSURANCE_POLICY.equalsIgnoreCase(response.ownerTypeId)){
						view.addContact("Mediador (" + response.inheritMediatorName + ")", response.inheritMediatorId , BigBangConstants.EntityIds.MEDIATOR);
						view.addContact("Cliente (" + response.inheritClientName + ")", response.inheritClientId, BigBangConstants.EntityIds.CLIENT);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});
		}else if(BigBangConstants.EntityIds.EXPENSE.equalsIgnoreCase(response.parentDataTypeId)){
			ExpenseDataBroker broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
			broker.getExpense(response.parentDataObjectId, new ResponseHandler<Expense>() {

				@Override
				public void onResponse(Expense response) {
					view.setOwnerForm(new ExpenseForm());
					view.setOwnerFormValue(response);
					
					view.addContact("Despesa de Saúde (" + response.number + ")", response.id , BigBangConstants.EntityIds.EXPENSE);
					view.addContact("Apólice " + (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(response.referenceTypeId) ? "Adesão " : "") + "(" + response.referenceNumber + ")", response.referenceId, response.referenceTypeId);
					view.addContact("Seguradora (" + response.inheritInsurerName + ")", response.inheritInsurerId, BigBangConstants.EntityIds.INSURANCE_AGENCY);
					if (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(response.referenceTypeId)) {
						view.addContact("Cliente Subscritor (" + response.clientName + ")", response.clientId, BigBangConstants.EntityIds.CLIENT);
						view.addContact("Mediador do Cliente Subscritor (" + response.inheritMediatorName + ")", response.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
						view.addContact("Cliente Principal (" + response.inheritMasterClientName + ")", response.inheritMasterClientId, BigBangConstants.EntityIds.CLIENT);
						view.addContact("Mediador da Apólice Mãe (" + response.inheritMasterMediatorName + ")", response.inheritMasterMediatorId, BigBangConstants.EntityIds.MEDIATOR);
					}
					else {
						view.addContact("Cliente (" + response.clientName + ")", response.clientId, BigBangConstants.EntityIds.CLIENT);
						view.addContact("Mediador (" + response.inheritMediatorName + ")", response.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});
		}
	}

	public void onGetOwnerFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o processo pai."), TYPE.ALERT_NOTIFICATION));
	}

	protected void setPermissions() {
		view.allowSend(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.SEND));
		view.allowRepeat(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.REPEAT) && (currentMessage == null || !Conversation.Direction.INCOMING.equals(currentMessage.direction)));
		view.allowReceive(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.RECEIVE));
		view.allowClose(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.CLOSE));
	}
	
	protected void onFormValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
	}
	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
		
	}
	@Override
	public int getDataVersion(String dataElementId) {
		return 0;
	}
	@Override
	public void updateConversation(Conversation response) {
		return;
		
	}
	
	protected void initController(){
		this.overlayController = new ViewPresenterController(view.getOverlayViewContainer()) {

			@Override
			public void onParameters(HasParameters parameters) {
				String show = parameters.getParameter("show");
				show = show == null ? new String() : show;

				if(show.isEmpty()){
					view.showOverlayViewContainer(false);

				//OVERLAY VIEWS
				}else if(show.equalsIgnoreCase("conversationclose")){
					present("CONVERSATION_CLOSE", parameters);
					view.showOverlayViewContainer(true);
				}
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				view.showOverlayViewContainer(false);
			}
		};
	}
}
