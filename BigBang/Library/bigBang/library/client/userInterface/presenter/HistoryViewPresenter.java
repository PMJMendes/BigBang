package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class HistoryViewPresenter implements ViewPresenter {

	public static enum Action {
		REVERT_OPERATION,
		NAVIGATE_TO_AUXILIARY_PROCESS
	}

	public interface Display {
		//LIST
		HasValueSelectables<HistoryItemStub> getUndoItemList();
		void setObjectId(String objectId);
		void selectItem(String id);
		void refreshList();

		//FORM
		HasValue<HistoryItem> getForm();

		//PERMISSIONS
		void allowUndo(boolean allow);
		void allowNavigateToAuxiliaryProcess(boolean allow);

		void confirmUndo(ResponseHandler<Boolean> handler);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		Widget asWidget();
	}

	private Display view;
	protected HistoryBroker historyBroker;
	protected NegotiationBroker negBroker;
	private boolean bound = false;

	public HistoryViewPresenter(Display view) {
		this.historyBroker = (HistoryBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.HISTORY);
		negBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
		setView((View) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		if(!bound)
			bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String objectId = parameterHolder.getParameter("historyownerid");
		objectId = objectId == null ? new String() : objectId;
		String itemId = parameterHolder.getParameter("historyitemid");
		itemId = itemId == null ? new String() : itemId;

		if(objectId.isEmpty()){
			clearView();
		}else{
			showHistory(objectId);
			if(!itemId.isEmpty()){
				showHistoryItem(objectId, itemId);
			}else{
				view.allowNavigateToAuxiliaryProcess(false);
				view.allowUndo(false);
			}
		}
	}

	private void bind() {
		if(bound)
			return;
		view.registerActionHandler(new ActionInvokedEventHandler<HistoryViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case REVERT_OPERATION:
					onRevertOperation();
					break;
				case NAVIGATE_TO_AUXILIARY_PROCESS:
					onNavigateToAuxiliaryProcess();
					break;
				}
			}
		});
		view.getUndoItemList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<HistoryItemStub> selectedItem = (ValueSelectable<HistoryItemStub>) event.getFirstSelected();
				HistoryItemStub item = selectedItem == null ? null : selectedItem.getValue();
				String itemId = item == null ? null : item.id;

				NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
				if(itemId == null){
					navigationItem.removeParameter("historyitemid");
				}else{
					navigationItem.setParameter("historyitemid", itemId);
				}
				NavigationHistoryManager.getInstance().go(navigationItem);
			}
		});

		//APPLICATION-WIDE EVENTS

		bound = true;
	}

	private void clearView(){
		view.getForm().setValue(null);
		view.setObjectId(null);
		view.allowUndo(false);
		view.allowNavigateToAuxiliaryProcess(false);
	}

	private void showHistory(String objectId){
		view.setObjectId(objectId);
	}
	
	private void showHistoryItem(String objectId, String itemId){
		historyBroker.getItem(itemId, objectId, new ResponseHandler<HistoryItem>() {

			@Override
			public void onResponse(HistoryItem response) {
				view.getForm().setValue(response);
				view.allowNavigateToAuxiliaryProcess(response.otherObjectId != null && response.otherObjectTypeId != null);
				view.allowUndo(response.canUndo);
				view.selectItem(response.id);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onShowHistoryFailed();
			}
		});
	}
	
	private void onRevertOperation(){
		view.confirmUndo(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(response){
					historyBroker.undo(view.getForm().getValue().id, new ResponseHandler<HistoryItem>() {

						@Override
						public void onResponse(HistoryItem response) {
							onRevertOperationSuccess();
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.removeParameter("historyItemId");
							NavigationHistoryManager.getInstance().go(item);
							view.refreshList();
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onRevertOperationFailed();
						}
					});
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onRevertOperationFailed();
			}
		});
	}

	private void onNavigateToAuxiliaryProcess(){
		HistoryItem historyItem = view.getForm().getValue();
		String auxObjectType = historyItem.otherObjectTypeId;
		String auxObjectId = historyItem.otherObjectId;

		if(auxObjectType == null || auxObjectId == null){
			onNavigateToAuxiliaryProcess();
		}else{
			if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
				navigateToClient(auxObjectId);
				
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
				navigateToInsurancePolicy(auxObjectId);
				
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
				navigateToReceipt(auxObjectId);
	
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
				navigateToCasualty(auxObjectId);

			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)){
				navigateToQuoteRequest(auxObjectId);

			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.COMPLAINT)){
				navigateToComplaint(auxObjectId);

			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){
				navigateToExpense(auxObjectId);

			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.RISK_ANALISYS)){
				navigateToRiskAnalysis(auxObjectId);

			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
				navigateToNegotiation(auxObjectId);
				
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.SIGNATURE_REQUEST)){
				navigateToSignatureRequest(auxObjectId);
				
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.DAS_REQUEST)){
				navigateToDASRequest(auxObjectId);
				
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
				navigateToSubCasualty(auxObjectId);
				
			}else{
				onNavigateToAuxiliaryProcessFailed();
				return;
			}
		}
	}
	
	private void navigateToSubCasualty(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "casualty");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("casualtyid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);		
	}

	private void navigateToDASRequest(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "receipt");
		navigationItem.pushIntoStackParameter("display", "dasrequest");
		navigationItem.setParameter("dasrequestid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);		
	}

	private void navigateToSignatureRequest(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "receipt");
		navigationItem.pushIntoStackParameter("display", "signaturerequest");
		navigationItem.setParameter("signaturerequestid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);		
	}

	private void navigateToNegotiation(final String auxObjectId) {
		final NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");

		negBroker.getNegotiation(auxObjectId, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				if(response.ownerTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
					navigationItem.setParameter("section", "insurancepolicy");
				}
				else{
					navigationItem.setParameter("section", "quoterequest");
				}
				navigationItem.pushIntoStackParameter("display", "negotiation");
				navigationItem.setParameter("ownertypeid", response.ownerTypeId);
				navigationItem.setParameter("negotiationid", auxObjectId);
				NavigationHistoryManager.getInstance().go(navigationItem);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onNavigateToAuxiliaryProcessFailed();
			}
		});
		
		
		
	}

	private void navigateToRiskAnalysis(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "riskanalisys");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("riskanalisysid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);
	}

	private void navigateToExpense(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "expense");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("expenseid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);
	}

	private void navigateToComplaint(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "complaint");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("complaintid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);		
	}

	private void navigateToQuoteRequest(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "quoterequest");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("quoterequestid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);		
	}

	private void navigateToCasualty(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "casualty");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("casualtyid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);
	}

	private void navigateToReceipt(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "receipt");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("receiptid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);		
	}

	private void navigateToInsurancePolicy(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "insurancepolicy");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("policyid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);		
	}

	private void navigateToClient(String auxObjectId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section", "client");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.setParameter("clientid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);
	}

	private void onRevertOperationSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A operação foi revertida com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onShowHistoryFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar o histórico, neste momento."), TYPE.ALERT_NOTIFICATION));
	}

	private void onNavigateToAuxiliaryProcessFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível navegar até ao processo auxiliar"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onRevertOperationFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível reverter a operação"), TYPE.ALERT_NOTIFICATION));
	}

}
