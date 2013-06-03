package bigBang.module.receiptModule.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicySearchPanel.Entry;
import bigBang.module.insurancePolicyModule.client.userInterface.form.SubPolicyForm;
import bigBang.module.receiptModule.client.userInterface.SubPolicyList;
import bigBang.module.receiptModule.client.userInterface.presenter.SubPolicyChoiceFromListViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.SubPolicyChoiceFromListViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubPolicyChoiceFromListView  extends View implements SubPolicyChoiceFromListViewPresenter.Display {

	private SubPolicyForm form;
	private SubPolicyList list;
	private ActionInvokedEventHandler<Action> actionHandler;
	private Button confirm;
	private Button markReceipt;
	
	@Override
	protected void initializeView() {
		return;
	}
	
	public SubPolicyChoiceFromListView(){
		

		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("1050px", "650px");
		list = new SubPolicyList();
		form = new SubPolicyForm();
		
		markReceipt = new Button("Saltar");
		
		markReceipt.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.MARK_RECEIPT));				
			}
		});
		
		Button cancel = new Button("Cancelar");
		
		cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		});
		
		
		confirm = new Button("Confirmar");
		
		confirm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CHOSEN_SUB_POLICY));
			}
		});
		
		
		
		ListHeader listHeader = new ListHeader("Apólices");
		list.setHeaderWidget(listHeader);
		
		list.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.LIST_CHANGED));
			}
		});
		
		wrapper.addWest(list, 400);

		HorizontalPanel buttons = new HorizontalPanel();
		
		buttons.add(confirm);
		buttons.add(cancel);
		buttons.add(markReceipt);
		
		ListHeader rightHeader = new ListHeader("Ficha da Apólice");
		rightHeader.setRightWidget(buttons);
		VerticalPanel rightWrapper = new VerticalPanel();
		rightWrapper.add(rightHeader);
		rightHeader.setWidth("100%");
		rightWrapper.setSize("100%", "100%");
		rightWrapper.add(form);
		rightWrapper.setCellHeight(form, "100%");
		form.setReadOnly(true);
		wrapper.add(rightWrapper);
		
	}
	
	@Override
	public HasEditableValue<SubPolicy> getForm() {
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
	public HasValueSelectables<SubPolicyStub> getList() {
		return list;
	}
	
	@Override
	public void fillList(Collection<SubPolicyStub> collection) {
		list.clear();
		form.clearInfo();
		
		for(SubPolicyStub stub : collection){

			list.add(new Entry(stub));
		}
	}

	@Override
	public void enableConfirm(boolean b) {
		confirm.setEnabled(b);
	}
	
	@Override
	public void enableMarkReceipt(boolean b){
		markReceipt.setVisible(b);
	}
}
