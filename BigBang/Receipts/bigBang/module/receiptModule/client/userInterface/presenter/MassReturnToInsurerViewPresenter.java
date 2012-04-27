package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.Checkable;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.CheckedSelectionChangedEvent;
import bigBang.library.client.event.CheckedSelectionChangedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class MassReturnToInsurerViewPresenter implements ViewPresenter{
	
	
	public enum Action{
		SELECT_ALL, MASS_RETURN_TO_INSURER, CLEAR
	}
	
	private Display view;
	private boolean bound = false;
	protected ReceiptDataBroker broker;
	
	
	public MassReturnToInsurerViewPresenter(Display view){
		setView((UIObject)view);
		broker = (ReceiptDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
	}
	
	public interface Display{
		void addReceiptToReturn(ReceiptStub value);
		void removeReceiptToReturn(String id);
		HasCheckables getCheckableSelectedList();
		HasEditableValue<Receipt> getReceiptForm();
		HasValueSelectables<ReceiptStub> getMainList();
		HasValueSelectables<ReceiptStub> getSelectedList();
		HasCheckables getCheckableMainList();
		
		void refreshMainList();
		
		void markAllForCheck();
		void markForCheck(String id);
		void markForUncheck(String id);
		
		void removeAllReceiptsFromReturn();
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> actionInvockedEventHandker);
		void allowCreation(boolean b);
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
		
	}
	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		view.refreshMainList();
		showMassReturnToInsurerScreen();
	}

	private void showMassReturnToInsurerScreen() {
		checkUserPermission(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(response){
					view.allowCreation(true);
					view.getReceiptForm().setValue(null);
				}else{
					onUserLacksPermission();
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
		
	}
	
	private void checkUserPermission(ResponseHandler<Boolean> responseHandler) {
		responseHandler.onResponse(true); //TODO
	}
	protected void onUserLacksPermission() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	private void clearView() {
		view.getMainList().clearSelection();
		view.removeAllReceiptsFromReturn();
		view.getReceiptForm().setValue(null);

	}
	
	public void bind(){
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<MassReturnToInsurerViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CLEAR:
					view.removeAllReceiptsFromReturn();
					break;
				case MASS_RETURN_TO_INSURER:
					massReturnToInsurer(view.getSelectedList().getAll());
					break;
				case SELECT_ALL:
					view.markAllForCheck();
					break;
				}
			}
		});
		view.getCheckableMainList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {

			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();

				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> entry = (ValueSelectable<ReceiptStub>) checkable;
				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
					view.addReceiptToReturn((entry.getValue()));
				}else{
					view.markForUncheck(id);
					view.removeReceiptToReturn(id);
				}

			}
		});

		view.getCheckableSelectedList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {

			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();

				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> entry = (ValueSelectable<ReceiptStub>) checkable;
				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
				}else{
					view.markForUncheck(id);
					view.removeReceiptToReturn(id);
				}

			}
		});
		
		view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				
				@SuppressWarnings("unchecked")
				final
				ValueSelectable<ReceiptStub> selectable = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				
				if(selectable!= null){
					broker.getReceipt(selectable.getValue().id, new ResponseHandler<Receipt>() {
						
						@Override
						public void onResponse(Receipt response) {
							selectable.setSelected(true, true);
							view.getReceiptForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Recibo seleccionado"), TYPE.ALERT_NOTIFICATION));
						}
					});
				}
				
			}
		});
		
		view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				
				@SuppressWarnings("unchecked")
				final
				ValueSelectable<ReceiptStub> selectable = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				
				if(selectable!= null){
					view.getMainList().clearSelection();
					broker.getReceipt(selectable.getValue().id, new ResponseHandler<Receipt>() {
						
						@Override
						public void onResponse(Receipt response) {
							selectable.setSelected(true, true);
							view.getReceiptForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Recibo seleccionado"), TYPE.ALERT_NOTIFICATION));
						}
					});
				}
				
			}
		});
		
		bound = true;
	}
	protected void massReturnToInsurer(
			Collection<ValueSelectable<ReceiptStub>> selected) {
		String [] receiptIds = new String[selected.size()];
		
		int i = 0;
		
		for(ValueSelectable<ReceiptStub> r : selected){
			receiptIds[i] = r.getValue().id;
			i++;
		}
		
		broker.massReturnToInsurer(receiptIds, new ResponseHandler<Void>() {
			
			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recibos devolvidos à seguradora"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ocorreu um erro ao devolver os recibos"), TYPE.ALERT_NOTIFICATION));								
			}
		});
	}

}
