package bigBang.module.receiptModule.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ReceiptsList;
import bigBang.module.receiptModule.client.userInterface.ChoiceFromListToolbar;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.ReceiptSearchPanel.Entry;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptChoiceFromListViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptChoiceFromListViewPresenter.Action;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiptChoiceFromListView extends View implements ReceiptChoiceFromListViewPresenter.Display{

	private ReceiptForm form;
	private ReceiptsList list = new ReceiptsList();
	private ActionInvokedEventHandler<Action> actionHandler;
	private ChoiceFromListToolbar toolbar;
	
	public ReceiptChoiceFromListView(){
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("1050px", "650px");
		form = new ReceiptForm();
		
		ListHeader listHeader = new ListHeader("Recibos");
		list.setHeaderWidget(listHeader);
		
		list.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptChoiceFromListViewPresenter.Action>(Action.LIST_CHANGED));
			}
		});
		
		wrapper.addWest(list, 400);
		
		toolbar = new ChoiceFromListToolbar() {
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptChoiceFromListViewPresenter.Action>(Action.CANCEL));
			}
			
			@Override
			public void onConfirmChoice() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiptChoiceFromListViewPresenter.Action>(Action.CHOSEN_RECEIPT));
			}
		};
		
		ListHeader rightHeader = new ListHeader("Ficha do Recibo");
		VerticalPanel rightWrapper = new VerticalPanel();
		rightWrapper.add(rightHeader);
		rightHeader.setWidth("100%");
		rightWrapper.add(toolbar);
		rightWrapper.setSize("100%", "100%");
		toolbar.setWidth("100%");
		rightWrapper.add(form);
		rightWrapper.setCellHeight(form, "100%");
		form.setReadOnly(true);
		wrapper.add(rightWrapper);
		
		
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Receipt> getForm() {
		return form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void clear() {
		form.clearInfo();
		list.clear();
	}

	@Override
	public HasValueSelectables<ReceiptStub> getList() {
		return list;
	}

	@Override
	public void fillList(Collection<ReceiptStub> collection) {
		list.clear();
		form.clearInfo();
		for(ReceiptStub stub : collection){

			list.add(new Entry(stub));

		}
	}

	@Override
	public void enableConfirm(boolean b) {
		toolbar.enableConfirm(b);
	}

}
