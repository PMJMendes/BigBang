package bigBang.library.client.userInterface.presenter;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.Checkable;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Session;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.CheckedSelectionChangedEvent;
import bigBang.library.client.event.CheckedSelectionChangedEventHandler;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class MassManagerTransferViewPresenter<T extends ProcessBase, T2 extends T> implements ViewPresenter {

	public static enum Action {
		TRANSFER,
		CANCEL,
		CLEAR_SELECTED_PROCESSES,
		SELECT_ALL_PROCESSES
	}

	public interface Display<T extends ProcessBase, T2 extends T> {
		void setOperationFilter(String operationId);

		HasValue<String> getNewManagerForm();
		HasEditableValue<T2> getSelectedProcessForm();

		HasValueSelectables<T> getMainList();
		HasValueSelectables<T> getSelectedList();
		HasCheckables getCheckableMainList();
		HasCheckables getCheckableSelectedList();
		
		void refreshMainList();
		
		void markAllForCheck();
		void markForCheck(String id);
		void markForUncheck(String id);
		void addProcessToTransfer(T value);
		void removeProcessFromTransfer(String id);
		void removeAllProcessesFromTransfer();
		
		void allowTransfer(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected String ownerId;
	protected Display<T, T2> view;
	private boolean bound = false;

	public MassManagerTransferViewPresenter(Display<T, T2> view){
		setView((UIObject) view);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setView(UIObject view) {
		this.view = (Display<T, T2>) view;
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
		showMassManagerTransferCreationScreen();
	}

	protected void bind(){
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<MassManagerTransferViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case TRANSFER:
					String newManagerId = view.getNewManagerForm().getValue();
					ArrayList<T> affectedProcesses = new ArrayList<T>();
					for(ValueSelectable<T> entry : view.getSelectedList().getAll()){
						affectedProcesses.add(entry.getValue());
					}
					onTransfer(newManagerId, affectedProcesses);
					break;
				case CANCEL:
					onCancel();
					break;
				case CLEAR_SELECTED_PROCESSES:
					view.removeAllProcessesFromTransfer();
					break;
				case SELECT_ALL_PROCESSES:
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
				ValueSelectable<T> entry = (ValueSelectable<T>) checkable;
				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
					view.addProcessToTransfer(entry.getValue());
				}else{
					view.markForUncheck(id);
					view.removeProcessFromTransfer(id);
				}
			}
		});
		view.getCheckableSelectedList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {

			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();

				@SuppressWarnings("unchecked")
				ValueSelectable<T> entry = (ValueSelectable<T>) checkable;

				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
				}else{
					view.markForUncheck(id);
					view.removeProcessFromTransfer(id);
				}
			}
		});

		bound = true;
	}

	private void clearView(){
		view.getMainList().clearSelection();
		view.removeAllProcessesFromTransfer();
		view.getNewManagerForm().setValue(null);
	}

	private void showMassManagerTransferCreationScreen(){
		view.getNewManagerForm().setValue(Session.getUserId());
		checkUserPermission(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(response){
					view.allowTransfer(true);
					view.getNewManagerForm().setValue(null);
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

	protected abstract void onTransfer(String newManagerId, Collection<T> affectedProcesses);

	protected abstract void onCancel();

	protected abstract void checkUserPermission(ResponseHandler<Boolean> handler);

	protected abstract void onUserLacksPermission();

	protected abstract void onTransferSuccess();

	protected abstract void onTransferFailed();

}
