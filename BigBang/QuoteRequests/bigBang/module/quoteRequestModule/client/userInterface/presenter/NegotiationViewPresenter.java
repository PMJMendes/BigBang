package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.SubProcessesBroker;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class NegotiationViewPresenter implements ViewPresenter{

	public static enum Action{
		EDIT, SAVE, CANCEL, DELETE, SELECTION_CHANGED_CONTACT, SELECTION_CHANGED_DOCUMENT, CANCEL_NEGOTIATION, EXTERNAL_REQUEST, GRANT, RESPONSE

	}


	public static interface Display{

		Widget asWidget();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		HasValue<ProcessBase> getOwnerForm();

		HasValueSelectables<Contact> getContactList();

		HasValueSelectables<Document> getDocumentList();
		
		HasValueSelectables<HistoryItemStub> getHistoryList();
		
		HasValueSelectables<BigBangProcess> getSubProcessList();

		HasEditableValue<Negotiation> getForm();

		void setToolbarSaveMode(boolean b);

		void setParentHeaderTitle(String title);

		void disableToolbar();

		void allowEdit(boolean b);

		void allowDelete(boolean b);
		
		void allowCancelNegotiation(boolean b);

		void applyOwnerToList(String negotiationId);

		void allowExternalRequest(boolean hasPermission);

		void allowGrant(boolean hasPermission);
		
		void allowResponse(boolean hasPermission);

		HasSelectables<ValueSelectable<Document>> getDocumentsList();

		HasSelectables<ValueSelectable<Contact>> getContactsList();

		void enableContactCreation(boolean allow);

		void enableDocumentCreation(boolean allow);

		void setOwnerTypeId(String negotiation);

		void allowContactDocumentEdit(boolean hasPermission);

	}

	protected Display view;
	protected boolean bound = false;
	private NegotiationBroker negotiationBroker;
	private String negotiationId;
	protected String ownerId;
	//private boolean hasPermissions;
	protected String ownerTypeId;
	protected String managerId;
	protected String companyId;


	public NegotiationViewPresenter(Display view){
		setView((UIObject)view);
		negotiationBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
	} 

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	private void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<NegotiationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:{
					if(view.getForm().getInfo().id == null){
						NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
						item.removeParameter("show");
						item.popFromStackParameter("display");
						item.removeParameter("negotiationid");
						item.removeParameter("ownertypeid");
						NavigationHistoryManager.getInstance().go(item);
					}
					view.getForm().revert();
					view.setToolbarSaveMode(false);
					view.getForm().setReadOnly(true);
					break;
				}
				case DELETE:{
					if(view.getForm().getInfo().id == null){
						NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
						item.removeParameter("show");
						item.popFromStackParameter("display");
						item.removeParameter("negotiationid");
						item.removeParameter("ownertypeid");
						NavigationHistoryManager.getInstance().go(item);
						return;
					}
					removeNegotiation();
					break;
				}
				case EDIT:{
					view.getForm().setReadOnly(false);
					view.setToolbarSaveMode(true);
					break;
				}
				case SAVE:{
					Negotiation newNeg = view.getForm().getInfo();
					if(newNeg.id == null){
						newNeg.ownerId = ownerId;
						createNegotiation(newNeg);
					}
					else{
						updateNegotiation(newNeg);
					}
					break;
				}
				case SELECTION_CHANGED_CONTACT:{
					@SuppressWarnings("unchecked")
					Contact contact = ((ListEntry<Contact>)view.getContactList().getSelected().toArray()[0]).getValue();
					showContact(contact);
					break;
				}
				case SELECTION_CHANGED_DOCUMENT:{
					@SuppressWarnings("unchecked")
					Document document = ((ListEntry<Document>)view.getDocumentList().getSelected().toArray()[0]).getValue();
					showDocument(document);
					break;
				}
				case CANCEL_NEGOTIATION:{
					cancelNegotiation();
					break;
				}
				case EXTERNAL_REQUEST:
					receiveExternalRequest();
					break;
					
				case GRANT:{
					grant();
					break;
				}
				
				case RESPONSE:{
					response();
					break;
				}
				}
				
			}
		});
		
		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selectable = (ValueSelectable<?>) event.getFirstSelected();
				
				if(selectable != null) {
					
					if(event.getSource() == view.getHistoryList()){ //HISTORY
						showHistory(view.getForm().getValue().id, ((HistoryItemStub) selectable.getValue()).id);
					} else if(event.getSource() == view.getSubProcessList()){ //SUB PROCESSES
						showSubProcess(((BigBangProcess) selectable.getValue()).dataId);
					}
				}
			}
		};
		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Contact> selected = (ValueSelectable<Contact>) event.getFirstSelected();
				Contact item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "contactmanagement");
					navItem.setParameter("ownerid", view.getForm().getValue().id);
					navItem.setParameter("contactid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Document> selected = (ValueSelectable<Document>) event.getFirstSelected();
				Document item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "documentmanagement");
					navItem.setParameter("ownerid", item.ownerId);
					navItem.setParameter("documentid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getSubProcessList().addSelectionChangedEventHandler(selectionChangedHandler);
		
		bound = true;
	}

	protected void response() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "responsenegotiation");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void grant() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "grantnegotiation");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void receiveExternalRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "externalrequest");
		item.setParameter("externalrequestid", "new");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		negotiationId = parameterHolder.getParameter("negotiationid");
		view.setOwnerTypeId(BigBangConstants.EntityIds.NEGOTIATION);

		if(negotiationId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a negociação."), TYPE.ALERT_NOTIFICATION));
			view.getForm().setReadOnly(true);
			view.disableToolbar();
		}
		else if(negotiationId.equalsIgnoreCase("new")){
			Negotiation newNeg = new Negotiation();
			newNeg.companyId = companyId;
			newNeg.managerId = managerId;
			newNeg.ownerId = ownerId;
			view.getForm().setInfo(newNeg);
			view.getForm().setReadOnly(false);
			view.setToolbarSaveMode(true);
			view.allowEdit(true);
			view.allowCancelNegotiation(false);
			view.allowExternalRequest(false);
			view.allowGrant(false);
			view.allowResponse(false);
			view.allowResponse(false);
			
		}else{

			negotiationBroker.getNegotiation(negotiationId, new ResponseHandler<Negotiation>() {

				@Override
				public void onResponse(Negotiation response) {
					view.getForm().setInfo(response);
					view.applyOwnerToList(negotiationId);
					view.getForm().setReadOnly(true);
					view.setToolbarSaveMode(false);
					view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.DELETE_NEGOTIATION));
					view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.UPDATE_NEGOTIATION));
					view.allowContactDocumentEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.UPDATE_NEGOTIATION));
					view.allowCancelNegotiation(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.CANCEL_NEGOTIATION));
					view.allowExternalRequest(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.EXTERNAL_REQUEST));
					view.allowGrant(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.GRANT_NEGOTIATION));
					view.allowResponse(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.RECEIVE_QUOTE));
					view.enableDocumentCreation(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.UPDATE_NEGOTIATION));
					view.enableContactCreation(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.NegotiationProcess.UPDATE_NEGOTIATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a negociação."), TYPE.ALERT_NOTIFICATION));
					view.getForm().setReadOnly(true);
					view.disableToolbar();
				}
			});
		}


	}

	protected abstract void createNegotiation(Negotiation negotiation);

	protected void updateNegotiation(Negotiation negotiation){

		negotiationBroker.updateNegotiation(negotiation, new ResponseHandler<Negotiation>() {

			@Override
			public void onResponse(Negotiation response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Negociação gravada com sucesso."), TYPE.TRAY_NOTIFICATION));
				view.setToolbarSaveMode(false);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao gravar a negociação."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void removeNegotiation(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "deletenegotiation");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void cancelNegotiation() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "cancelnegotiation");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showContact(Contact contact) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "contactmanagement");
		item.setParameter("ownerid", contact.ownerId);
		item.setParameter("ownertypeid", contact.ownerTypeId);
		item.setParameter("contactid", contact.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showDocument(Document document){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "documentmanagement");
		item.setParameter("ownerid", document.ownerId);
		item.setParameter("ownertypeid", document.ownerTypeId);
		item.setParameter("documentid", document.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showSubProcess(String subProcessId){
		SubProcessesBroker subProcessBroker = (SubProcessesBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.PROCESS);
		
		subProcessBroker.getSubProcess(subProcessId, new ResponseHandler<BigBangProcess>() {
			
			@Override
			public void onResponse(BigBangProcess response) {
				String type = response.dataTypeId;
				if(type.equalsIgnoreCase(BigBangConstants.EntityIds.EXTERNAL_INFO_REQUEST)) {
					showExternalRequest(response.dataId);
				}
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				view.getSubProcessList().clearSelection();
			}
		});
	}
	
	private void showExternalRequest(String requestId){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "viewnegotiationexternalrequest");
		item.setParameter("externalrequestid", requestId);
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void showHistory(String ownerId, String itemId){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "history");
		item.setParameter("historyownerid", ownerId);
		item.setParameter("historyitemid", itemId);
		NavigationHistoryManager.getInstance().go(item);
	}

}
