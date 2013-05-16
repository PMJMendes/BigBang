package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.ExpenseSelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ExpenseSelectionViewPresenter.Action;
import bigBang.module.expenseModule.client.userInterface.ExpenseSearchPanel;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExpenseSelectionView extends View implements ExpenseSelectionViewPresenter.Display{

	private ExpenseSearchPanel list;
	private ExpenseForm form;
	private ActionInvokedEventHandler<ExpenseSelectionViewPresenter.Action> handler;
	private Button confirmButton, cancelButton;
	
	public ExpenseSelectionView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		list = new ExpenseSearchPanel();
		
		confirmButton = new Button("Confirmar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<ExpenseSelectionViewPresenter.Action>(Action.CONFIRM));
			}
		});
		cancelButton = new Button("Cancelar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handler.onActionInvoked(new ActionInvokedEvent<ExpenseSelectionViewPresenter.Action>(Action.CANCEL));
			}
		});
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		HorizontalPanel buttonsWrapper = new HorizontalPanel();
		buttonsWrapper.setSpacing(5);
		buttonsWrapper.add(confirmButton);
		buttonsWrapper.add(cancelButton);
		
		ListHeader header = new ListHeader("Despesa de Saúde");
		header.setRightWidget(buttonsWrapper);

		form = new ExpenseForm();
		form.setSize("100%", "100%");
		form.setReadOnly(true);

		formWrapper.add(header);
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");

		VerticalPanel listWrapper = new VerticalPanel();
		listWrapper.setSize("100%", "100%");
		listWrapper.add(new ListHeader("Lista de Despesas de Saúde"));
		list.setWidth("360px");
		listWrapper.add(list);
		listWrapper.setCellHeight(list, "100%");		
		wrapper.addWest(listWrapper, 360);
		wrapper.add(formWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<ExpenseStub> getList() {
		return this.list;
	}

	@Override
	public HasEditableValue<Expense> getForm() {
		return this.form;
	}

	@Override
	public void allowConfirm(boolean allow) {
		this.confirmButton.setEnabled(allow);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void setOperationId(String operationId) {
		list.setOperationId(operationId);
		list.doSearch();
	}

}
