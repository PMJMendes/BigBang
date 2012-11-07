package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class ClientSelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		HasValueSelectables<ClientStub> getList();
		HasEditableValue<Client> getForm();

		void allowConfirm(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void setOperationId(String opId);
	}

	private Display view;
	private boolean bound = false;
	private ClientProcessBroker clientBroker;

	public ClientSelectionViewPresenter(Display view){
		clientBroker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
		setView((UIObject)view);		
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	public void go() {
		bind();
		this.view.asWidget().setSize("900px", "600px");
		initWidget(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		String opId = parameterHolder == null ? null : parameterHolder.getParameter("operationId");
		view.setOperationId(opId);
	}
	
	private void clearView() {
		view.getList().clearSelection();
		view.getForm().setValue(null);
		view.allowConfirm(false);
	}

	private void bind() {
		if(bound){return;}
		
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> selected = (ValueSelectable<ClientStub>) event.getFirstSelected();
				ClientStub client = selected == null ? null : selected.getValue();
				if(client == null) {
					view.getForm().setValue(null);
				}else{
					clientBroker.getClient(client.id, new ResponseHandler<Client>(){

						@Override
						public void onResponse(Client response) {
							view.getForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							view.getForm().setValue(null);
							view.getList().clearSelection();
						}
					});
				}
			}
		});
		
		view.getForm().addValueChangeHandler(new ValueChangeHandler<Client>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Client> event) {
				Client client = event.getValue();
				view.allowConfirm(client != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<ClientSelectionViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onClientSelected(view.getForm().getValue());
					break;
				case CANCEL:
					onSelectionCancelled();
					break;
				}
			}
		});

		bound = true;
	}
	
	protected void onClientSelected(Client client){
		ValueChangeEvent.fire(this, client == null ? null : client.id);
	}

	protected void onSelectionCancelled(){
		ValueChangeEvent.fire(this, "CANCELLED_SELECTION");
	}
	
	@Override
	public String getValue() {
		return view.getForm().getValue().id;
	}

	@Override
	public void setValue(String value) {
		setValue(value, true);
	}

	@Override
	public void setValue(String value, final boolean fireEvents) {
		this.clientBroker.getClient(value, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.getForm().setValue(response);
				if(fireEvents) {
					ValueChangeEvent.fire(ClientSelectionViewPresenter.this, response.id);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void setListId(String listId) {
		return;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	//TODO
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

}
