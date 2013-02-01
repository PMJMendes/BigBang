package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CasualtySelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter{

	public enum Action {
		CANCEL, CONFIRM
	}

	public interface Display {

		HasValueSelectables<CasualtyStub> getList();

		HasEditableValue<Casualty> getForm();

		void allowConfirm(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		void setOperationId(String operationId);

		Widget asWidget();

	}
	
	private Display view;
	private boolean bound = false;
	private CasualtyDataBroker broker;
	
	public CasualtySelectionViewPresenter(Display view){
		broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
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

	private void bind() {
		if(bound){return;}

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<CasualtyStub> selected = (ValueSelectable<CasualtyStub>) event.getFirstSelected();
				CasualtyStub casualty = selected == null ? null : selected.getValue();
				if(casualty == null) {
					view.getForm().setValue(null);
				}else{
					broker.getCasualty(casualty.id, new ResponseHandler<Casualty>(){

						@Override
						public void onResponse(Casualty response) {
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

		view.getForm().addValueChangeHandler(new ValueChangeHandler<Casualty>() {

			@Override
			public void onValueChange(ValueChangeEvent<Casualty> event) {
				Casualty casualty = event.getValue();
				view.allowConfirm(casualty != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onCasualtySelected(view.getForm().getValue());
					break;
				case CANCEL:
					onSelectionCancelled();
					break;
				}
			}
		});

		bound = true;		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
	}
	
	public void go() {
		bind();
		this.view.asWidget().setSize("900px", "600px");
		initWidget(this.view.asWidget());
	}


	private void clearView() {
		view.getList().clearSelection();
		view.getForm().setValue(null);
		view.allowConfirm(false);		
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
		this.broker.getCasualty(value, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				view.getForm().setValue(response);
				if(fireEvents) {
					ValueChangeEvent.fire(CasualtySelectionViewPresenter.this, response.id);
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
		// TODO
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected void onCasualtySelected(Casualty casualty){
		ValueChangeEvent.fire(this, casualty == null ? null : casualty.id);
	}

	protected void onSelectionCancelled(){
		ValueChangeEvent.fire(this, "CANCELLED_SELECTION");
	}

}
