package bigBang.library.client.userInterface.presenter;


import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
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

public class SubCasualtySelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter{

	public static enum Action{
		CONFIRM,
		CANCEL
	}

	public static interface Display{
		HasValueSelectables<SubCasualtyStub> getList();
		HasEditableValue<SubCasualty> getForm();

		void allowConfirm(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void setOperationId(String operationId);
		void setOwnerId(String ownerId);
	}

	private Display view;
	private boolean bound = false;
	private SubCasualtyDataBroker broker;

	public SubCasualtySelectionViewPresenter(Display view){
		broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
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
				ValueSelectable<SubCasualtyStub> selected = (ValueSelectable<SubCasualtyStub>) event.getFirstSelected();
				SubCasualtyStub subCasualty = selected == null ? null : selected.getValue();
				if(subCasualty == null) {
					view.getForm().setValue(null);
				}else{
					broker.getSubCasualty(subCasualty.id, new ResponseHandler<SubCasualty>(){

						@Override
						public void onResponse(SubCasualty response) {
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

		view.getForm().addValueChangeHandler(new ValueChangeHandler<SubCasualty>() {

			@Override
			public void onValueChange(ValueChangeEvent<SubCasualty> event) {
				SubCasualty subCasualty = event.getValue();
				view.allowConfirm(subCasualty != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onSubCasualty(view.getForm().getValue());
					break;
				case CANCEL:
					onSelectionCancelled();
					break;
				}
			}
		});

		bound = true;
	}

	protected void onSelectionCancelled() {
		ValueChangeEvent.fire(this, "CANCELLED_SELECTION");
		
	}

	protected void onSubCasualty(SubCasualty value) {
		ValueChangeEvent.fire(this, value == null ? null : value.id);
		
	}

	public void go(){
		bind();
		this.view.asWidget().setSize("900px", "600px");
		initWidget(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
	}

	private void clearView() {
		view.getList().clearSelection();
		view.getForm().setValue(null);
		view.allowConfirm(false);		
	}

	@Override
	public String getValue() {
		return view.getForm().getValue() != null ? view.getForm().getValue().id : null;
	}

	@Override
	public void setValue(String value) {
		setValue(value, true);
	}

	@Override
	public void setValue(String value, final boolean fireEvents) {
		if(value != null){
			this.broker.getSubCasualty(value, new ResponseHandler<SubCasualty>() {

				@Override
				public void onResponse(SubCasualty response) {
					view.getForm().setValue(response);
					if(fireEvents) {
						ValueChangeEvent.fire(SubCasualtySelectionViewPresenter.this, response.id);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onResponse(null);
				}
			});
		}
		
		else{
			view.getForm().setValue(null);
			if(fireEvents) {
				ValueChangeEvent.fire(SubCasualtySelectionViewPresenter.this, null);
			}
		}
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

	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
	

	public void setOperationId(String operationId) {
		view.setOperationId(operationId);		
	}

}
