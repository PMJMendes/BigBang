package bigBang.library.client.userInterface.presenter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;
import bigBang.library.client.BigBangAsyncCallback;


public class InsurancePolicySelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		HasValueSelectables<InsurancePolicyStub> getList();
		HasEditableValue<InsurancePolicy> getForm();

		void allowConfirm(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void setOperationId(String operationId);
		void setOwnerId(String ownerId);
	}

	private Display view;
	private boolean bound = false;
	private InsurancePolicyServiceAsync service;

	public InsurancePolicySelectionViewPresenter(Display view){
		service = InsurancePolicyService.Util.getInstance();
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
		String ownerId = parameterHolder.getParameter("ownerid");
		if(ownerId != null && !ownerId.isEmpty()){
			view.setOwnerId(ownerId);
		}
		clearView();
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
				ValueSelectable<InsurancePolicyStub> selected = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				InsurancePolicyStub policy = selected == null ? null : selected.getValue();
				if(policy == null) {
					view.getForm().setValue(null);
				}else{
					service.getPolicy(policy.id, new BigBangAsyncCallback<InsurancePolicy>(){

						@Override
						public void onResponseSuccess(InsurancePolicy result) {
							view.getForm().setValue(result);

						}

						public void onResponseFailure(Throwable caught) {
							view.getForm().setValue(null);
							view.getList().clearSelection();
						};
					});
				}
			}
		});

		view.getForm().addValueChangeHandler(new ValueChangeHandler<InsurancePolicy>() {

			@Override
			public void onValueChange(ValueChangeEvent<InsurancePolicy> event) {
				InsurancePolicy policy = event.getValue();
				view.allowConfirm(policy != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<InsurancePolicySelectionViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onPolicySelected(view.getForm().getValue());
					break;
				case CANCEL:
					onSelectionCancelled();
					break;
				}
			}
		});

		bound = true;
	}

	protected void onPolicySelected(InsurancePolicy policy){
		ValueChangeEvent.fire(this, policy == null ? null : policy.id);
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

		service.getPolicy(value, new BigBangAsyncCallback<InsurancePolicy>(){

			@Override
			public void onResponseSuccess(InsurancePolicy result) {
				view.getForm().setValue(result);
				if(fireEvents) {
					ValueChangeEvent.fire(InsurancePolicySelectionViewPresenter.this, result.id);
				}

			}

			public void onResponseFailure(Throwable caught) {
				view.getForm().setValue(null);
				view.getList().clearSelection();
			};
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

	public void setOperationId(String operationId) {
		view.setOperationId(operationId);		
	}

}
