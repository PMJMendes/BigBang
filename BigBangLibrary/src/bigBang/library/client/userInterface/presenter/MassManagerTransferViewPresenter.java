package bigBang.library.client.userInterface.presenter;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.User;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class MassManagerTransferViewPresenter<T> implements ViewPresenter {

	public static enum Action {
		TRANSFER,
		CANCEL
	}

	public interface Display<T> {
		void setOperationFilter(String operationId);

		HasEditableValue<User> getNewManagerForm();
		HasEditableValue<T> getSelectedProcessForm();

		HasValueSelectables<T> getMainList();
		HasValueSelectables<T> getSelectedList();
		HasCheckables getCheckableMainList();

		void allowTransfer(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}
	
	protected String ownerId;
	private Display<T> view;
	private boolean bound = false;
	
	public MassManagerTransferViewPresenter(Display<T> view){
		setView((UIObject) view);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setView(UIObject view) {
		this.view = (Display<T>) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		ownerId = parameterHolder.getParameter("id");
		ownerId = ownerId == null ? new String() : ownerId;

		if(ownerId.isEmpty()){
			clearView();
			onGetOwnerFailed();
		}else{
			showMassManagerTransferCreationScreen(ownerId);
		}
	}

	private void bind(){
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<MassManagerTransferViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case TRANSFER:
					ArrayList<T> affectedProcesses = new ArrayList<T>();
					for(ValueSelectable<T> entry : view.getSelectedList().getAll()){
						affectedProcesses.add(entry.getValue());
					}
					onTransfer(ownerId, affectedProcesses);
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	private void clearView(){
		view.getMainList().clearSelection();
		view.getSelectedList().clearSelection();
		view.getNewManagerForm().setReadOnly(true);
	}

	private void showMassManagerTransferCreationScreen(String ownerId){
		checkOwnerPermission(ownerId, new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(response){
					view.allowTransfer(true);
					view.getNewManagerForm().setReadOnly(false);
				}else{
					onUserLacksPermission();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	protected abstract void onTransfer(String ownerId, Collection<T> affectedProcesses);

	protected abstract void onCancel();
	
	protected abstract void checkOwnerPermission(String ownerId, ResponseHandler<Boolean> handler);

	protected abstract void onGetOwnerFailed();

	protected abstract void onUserLacksPermission();
	
}
