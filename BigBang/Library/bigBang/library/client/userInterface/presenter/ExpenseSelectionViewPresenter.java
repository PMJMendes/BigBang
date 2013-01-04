package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
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

public class ExpenseSelectionViewPresenter extends ExpandableSelectionFormFieldPanel implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		HasValueSelectables<ExpenseStub> getList();
		HasEditableValue<Expense> getForm();

		void allowConfirm(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void setOperationId(String operationId);

	}

	private Display view;
	private boolean bound = false;
	private ExpenseDataBroker broker;


	public ExpenseSelectionViewPresenter(Display view) {
		broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
		setView((UIObject)view);
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
		this.broker.getExpense(value, new ResponseHandler<Expense>() {

			@Override
			public void onResponse(Expense response) {
				view.getForm().setValue(response);
				if(fireEvents) {
					ValueChangeEvent.fire(ExpenseSelectionViewPresenter.this, response.id);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
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
				ValueSelectable<ExpenseStub> selected = (ValueSelectable<ExpenseStub>) event.getFirstSelected();
				ExpenseStub expense = selected == null ? null : selected.getValue();
				if(expense == null) {
					view.getForm().setValue(null);
				}else{
					broker.getExpense(expense.id, new ResponseHandler<Expense>(){

						@Override
						public void onResponse(Expense response) {
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

		view.getForm().addValueChangeHandler(new ValueChangeHandler<Expense>() {

			@Override
			public void onValueChange(ValueChangeEvent<Expense> event) {
				Expense expense = event.getValue();
				view.allowConfirm(expense != null);
			}
		});

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onExpenseSelected(view.getForm().getValue());
					break;
				case CANCEL:
					onSelectionCancelled();
					break;
				}
			}
		});

		bound = true;		
	}

	protected void onExpenseSelected(Expense expense){
		ValueChangeEvent.fire(this, expense == null ? null : expense.id);
	}

	protected void onSelectionCancelled(){
		ValueChangeEvent.fire(this, "CANCELLED_SELECTION");
	}

	public void go() {
		bind();
		this.view.asWidget().setSize("900px", "600px");
		initWidget(this.view.asWidget());
	}

	@Override
	public void setListId(String listId) {
		return;
	}

	@Override
	public void setParameters(HasParameters parameters) {
		clearView();
	}

	private void clearView() {
		view.getList().clearSelection();
		view.getForm().setValue(null);
		view.allowConfirm(false);		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub

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
