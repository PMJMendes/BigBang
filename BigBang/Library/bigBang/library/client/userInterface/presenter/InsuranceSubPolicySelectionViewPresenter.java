package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ExpandableSelectionFormFieldPanel;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyServiceAsync;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class InsuranceSubPolicySelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		HasValueSelectables<SubPolicyStub> getList();
		HasEditableValue<SubPolicy> getForm();

		void allowConfirm(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void setOperationId(String operationId);
		void setOwnerId(String ownerId);
		void setClientId(String clientId);
	}

	private Display view;
	private boolean bound = false;
	private SubPolicyServiceAsync service;

	public InsuranceSubPolicySelectionViewPresenter(Display view){
		service = SubPolicyService.Util.getInstance();
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
		String clientId = parameterHolder.getParameter("clientid");
		if(ownerId != null && !ownerId.isEmpty()){
			view.setOwnerId(ownerId);
		}

		if(clientId != null && !clientId.isEmpty()){
			view.setClientId(clientId);
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
				ValueSelectable<SubPolicyStub> selected = (ValueSelectable<SubPolicyStub>) event.getFirstSelected();
				SubPolicyStub policy = selected == null ? null : selected.getValue();
				if(policy == null) {
					view.getForm().setValue(null);
				}else{
					service.getSubPolicy(policy.id, new BigBangAsyncCallback<SubPolicy>(){

						@Override
						public void onResponseSuccess(SubPolicy result) {
							view.getForm().setValue(result);

						}

						@Override
						public void onResponseFailure(Throwable caught) {
							view.getForm().setValue(null);
							view.getList().clearSelection();
						}


					});

				}
			}
		});

		view.getForm().addValueChangeHandler(new ValueChangeHandler<SubPolicy>() {

			@Override
			public void onValueChange(ValueChangeEvent<SubPolicy> event) {
				SubPolicy policy = event.getValue();
				view.allowConfirm(policy != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<InsuranceSubPolicySelectionViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onSubPolicySelected(view.getForm().getValue());
					break;
				case CANCEL:
					onSelectionCancelled();
					break;
				}
			}
		});

		bound = true;
	}

	protected void onSubPolicySelected(SubPolicy subPolicy){
		ValueChangeEvent.fire(this, subPolicy == null ? null : subPolicy.id);
	}

	protected void onSelectionCancelled(){
		ValueChangeEvent.fire(this, "CANCELLED_SELECTION");
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
			service.getSubPolicy(value, new BigBangAsyncCallback<SubPolicy>(){

				@Override
				public void onResponseSuccess(SubPolicy result) {
					view.getForm().setValue(result);

				}

				@Override
				public void onResponseFailure(Throwable caught) {
					view.getForm().setValue(null);
					view.getList().clearSelection();
				}
			});
		}

		else{
			view.getForm().setValue(null);
			if(fireEvents) {
				ValueChangeEvent.fire(InsuranceSubPolicySelectionViewPresenter.this, null);
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
