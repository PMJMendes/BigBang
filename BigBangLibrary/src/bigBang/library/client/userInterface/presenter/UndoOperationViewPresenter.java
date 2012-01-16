package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class UndoOperationViewPresenter implements ViewPresenter {

	public static enum Action {
		REVERT_OPERATION,
		NAVIGATE_TO_AUXILIARY_PROCESS
	}

	public interface Display {
		//LIST
		HasValueSelectables<HistoryItemStub> getUndoItemList();
		void setObjectId(String objectId);
		void refreshList();

		//FORM
		HasValue<HistoryItem> getForm();

		//MISC
		void setUndoable(boolean undoable);
		void selectItem(String id);
		
		void showConfirmUndo(ConfirmationCallback callback);
		void showErrors(Collection<ResponseError> errors);
		HandlerRegistration addAttachHandler(AttachEvent.Handler handler);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		void clear();
		void clearUndoItemList();
		
		Widget asWidget();
	}

	private Display view;
	protected HistoryBroker historyBroker;
	private boolean bound = false;

	public UndoOperationViewPresenter(Display view) {
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
		String objectId = parameterHolder.getParameter("id");
		objectId = objectId == null ? new String() : objectId;
		String itemId = parameterHolder.getParameter("historyitemid");
		itemId = itemId == null ? new String() : itemId;
		
		if(objectId.isEmpty()){
			clearView();
		}else{
			showHistory(objectId, itemId);
		}
	}
	
	private void bind() {
		if(bound)
			return;
//		view.registerActionHandler(new ActionInvokedEventHandler<UndoOperationViewPresenter.Action>() {
//
//			@Override
//			public void onActionInvoked(ActionInvokedEvent<Action> action) {
//				switch(action.getAction()) {
//				case REVERT_OPERATION:
//					onRevertOperation();
//					break;
//				case NAVIGATE_TO_AUXILIARY_PROCESS:
//					onNavigateToAuxiliaryProcess();
//					break;
//				}
//			}
//
//			public void onRevertOperation() {
//				view.showConfirmUndo(new ConfirmationCallback() {
//
//					@Override
//					public void onResult(boolean result) {
//						if(result){
//							historyBroker.undo(view.getForm().getValue().id, new ResponseHandler<HistoryItem>() {
//
//								@Override
//								public void onResponse(HistoryItem response) {
//									view.clear();
//								}
//
//								@Override
//								public void onError(Collection<ResponseError> errors) {
//									view.showErrors(errors);
//								}
//							});
//						}
//					}
//				});
//			}
//		});
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
					navigationItem.setParameter("historyItemid", itemId);
				}
				NavigationHistoryManager.getInstance().go(navigationItem);
			}
		});

		//APPLICATION-WIDE EVENTS
		
		bound = true;
	}
	
	private void clearView(){
		view.getForm().setValue(null);
		view.clearUndoItemList();
		view.setUndoable(false);
	}
	
	private void showHistory(String objectId, String itemId){
		view.setObjectId(objectId);
		historyBroker.getItem(itemId, objectId, new ResponseHandler<HistoryItem>() {
			
			@Override
			public void onResponse(HistoryItem response) {
				view.getForm().setValue(response);
				//TODO
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				//TODO
			}
		});
	}
	
	protected void onNavigateToAuxiliaryProcess(){
		//TODO
	}

}
