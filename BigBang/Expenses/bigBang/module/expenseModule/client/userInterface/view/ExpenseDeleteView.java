package bigBang.module.expenseModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.DeleteExpenseForm;
import bigBang.module.expenseModule.client.userInterface.ExpenseDeleteToolbar;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseDeleteViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseDeleteViewPresenter.Action;

public class ExpenseDeleteView extends View implements ExpenseDeleteViewPresenter.Display{

	private ExpenseDeleteToolbar toolbar;
	private ActionInvokedEventHandler<Action> handler;
	private DeleteExpenseForm deleteForm;
	
	public ExpenseDeleteView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		
		toolbar = new ExpenseDeleteToolbar() {
			
			@Override
			public void onDeleteExpenseRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ExpenseDeleteViewPresenter.Action>(Action.DELETE_EXPENSE));
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ExpenseDeleteViewPresenter.Action>(Action.CANCEL));
			}
		};
		
	
		deleteForm = new DeleteExpenseForm();
		
		wrapper.add(toolbar);
		wrapper.add(deleteForm.getNonScrollableContent());
		wrapper.setCellHeight(deleteForm.getNonScrollableContent(), "100%");
		
	}
	
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
		
	}

	@Override
	public HasEditableValue<String> getForm() {
		return deleteForm;
	}

	@Override
	protected void initializeView() {
		return;
	}

}
