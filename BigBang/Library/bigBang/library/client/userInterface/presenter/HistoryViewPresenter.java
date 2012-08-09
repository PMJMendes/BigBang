package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.BigBangAsyncCallback;
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
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class HistoryViewPresenter implements ViewPresenter {

	public static enum Action {
		REVERT_OPERATION,
		NAVIGATE_TO_AUXILIARY_PROCESS, GO_BACK
	}

	public interface Display {
		//LIST
		HasValueSelectables<HistoryItemStub> getUndoItemList();
		void setObjectId(String objectId);
		void selectItem(String id);
		void refreshList();
		void showBackButton(boolean show);
		
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
	private boolean bound = false;
	
	public HistoryViewPresenter(Display view) {
		this.historyBroker = (HistoryBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.HISTORY);
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
			view.setObjectId(null);
		}else{
			showHistory(objectId);
			if(!itemId.isEmpty()){
				showHistoryItem(objectId, itemId);
			}else{
				view.allowNavigateToAuxiliaryProcess(false);
				view.allowUndo(false);
				view.getForm().setValue(null);
			}
		}
		view.showBackButton(objectId != null && SessionGeneralSystem.getInstance().id != null && !objectId.equalsIgnoreCase(SessionGeneralSystem.getInstance().id));
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
				case GO_BACK:
					onGoBack();
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
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		
		String ownerId = item.getParameter("historyownerid");
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
				navigateToSubCasualty(auxObjectId, ownerId);
				
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)){
				navigateToInfoRequest(auxObjectId, ownerId);
				
			}else if(auxObjectType.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
				navigateToSubPolicy(auxObjectId, ownerId);
			}
			else {
				onNavigateToAuxiliaryProcessFailed();
				return;
			}
		}
	}
	
	private void navigateToSubPolicy(String auxObjectId, String ownerId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section","insurancepolicy");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.pushIntoStackParameter("display", "subpolicy");
		navigationItem.setParameter("policyid", ownerId);
		navigationItem.setParameter("subpolicyid", auxObjectId);
		NavigationHistoryManager.getInstance().go(navigationItem);
	}

	private void onGoBack(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("historyitemid");
		item.removeParameter("historyownerid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void navigateToSubCasualty(String auxObjectId, String ownerId) {
		NavigationHistoryItem navigationItem = new NavigationHistoryItem();
		navigationItem.setStackParameter("display");
		navigationItem.setParameter("section","casualty");
		navigationItem.pushIntoStackParameter("display", "search");
		navigationItem.pushIntoStackParameter("display", "subcasualty");
		navigationItem.setParameter("casualtyid", ownerId);
		navigationItem.setParameter("subcasualtyid", auxObjectId);
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

		NegotiationBroker negBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);

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
	
	private void navigateToInfoRequest(String auxObjectId, final String ownerId) {
		InfoOrDocumentRequestServiceAsync service = InfoOrDocumentRequestService.Util.getInstance();
		service.getRequest(auxObjectId, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(InfoOrDocumentRequest result) {
				
				final NavigationHistoryItem navigationItem = new NavigationHistoryItem();
				navigationItem.setStackParameter("display");
				navigationItem.pushIntoStackParameter("display", "search");
				navigationItem.pushIntoStackParameter("display", "viewinforequest");
				navigationItem.setParameter("requestid", result.id);

				if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.EXPENSE)){					
					navigationItem.setParameter("section", "expense");
					navigationItem.setParameter("expenseid", ownerId);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
					navigationItem.setParameter("section", "client");
					navigationItem.setParameter("clientid", ownerId);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
					navigationItem.setParameter("section", "insurancepolicy");
					navigationItem.setParameter("policyid", ownerId);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY)){
					
					InsuranceSubPolicyBroker broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
					broker.getSubPolicy(ownerId, new ResponseHandler<SubPolicy>() {
						
						@Override
						public void onResponse(SubPolicy response) {
							navigationItem.setParameter("section", "insurancepolicy");
							navigationItem.setParameter("policyid", response.mainPolicyId);
							navigationItem.popFromStackParameter("display");
							navigationItem.pushIntoStackParameter("display", "subpolicy");
							navigationItem.pushIntoStackParameter("display", "viewsubpolicyinforequest");
							navigationItem.setParameter("subpolicyid", response.id);
							NavigationHistoryManager.getInstance().go(navigationItem);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							onNavigateToAuxiliaryProcessFailed();
						}
					});
					
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
					navigationItem.setParameter("section", "casualty" );
					navigationItem.setParameter("casualtyid", ownerId);
					NavigationHistoryManager.getInstance().go(navigationItem);
					
				}else if(result.parentDataTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
					
					SubCasualtyDataBroker broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
					broker.getSubCasualty(ownerId, new ResponseHandler<SubCasualty>() {
						
						@Override
						public void onResponse(SubCasualty response) {
							navigationItem.setParameter("section", "casualty");
							navigationItem.setParameter("casualtyid", response.casualtyId);
							navigationItem.popFromStackParameter("display");
							navigationItem.pushIntoStackParameter("display", "subcasualty");
							navigationItem.pushIntoStackParameter("display", "viewsubcasualtyinforequest");
							navigationItem.setParameter("subcasualtyid", response.id);
							NavigationHistoryManager.getInstance().go(navigationItem);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							onNavigateToAuxiliaryProcessFailed();
						}
					});
					
					
				}else {
					return;
				}
				
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				onNavigateToAuxiliaryProcessFailed();
				super.onResponseFailure(caught);
			}
		});
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
