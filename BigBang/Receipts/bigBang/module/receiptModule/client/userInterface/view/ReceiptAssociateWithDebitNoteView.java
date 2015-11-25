package bigBang.module.receiptModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DebitNote;
import bigBang.library.client.Session;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptAssociateWithDebitNoteViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.ReceiptAssociateWithDebitNoteViewPresenter.Action;

public class ReceiptAssociateWithDebitNoteView extends View implements ReceiptAssociateWithDebitNoteViewPresenter.Display{
	
	private List<DebitNote> list;
	private Button confirm;
	private Button cancel;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public static class DebitNoteEntry extends ListEntry<DebitNote>{

		public DebitNoteEntry(DebitNote value) {
			super(value);
			this.setTitle(value.value + Session.getCurrency());
			this.setText("Data de Vencimento: " + value.maturityDate);
			setHeight("40px");
		}
		
	}
	
	public ReceiptAssociateWithDebitNoteView(){
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("400px", "400px");
		
		HorizontalPanel buttons = new HorizontalPanel();
		
		confirm = new Button("Confirmar");
		
		confirm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CONFIRM));
			}
		});
		
		cancel = new Button("Cancelar");
		
		cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		});
		
		list = new List<DebitNote>();
		
		buttons.add(confirm);
		buttons.add(cancel);
		buttons.setSpacing(5);
		
		ListHeader header = new ListHeader("Lista de Notas de Débito");
		header.setRightWidget(buttons);
		
		list.setHeaderWidget(header);
		
		wrapper.add(list);
		
	}
	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public List<DebitNote> getList(){
		return list;
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		actionHandler = handler;
		
	}
	@Override
	public void clear() {
		list.clear();
	}
	@Override
	public void addToList(DebitNote[] debitNotes) {
	
		for(int i = 0; i<debitNotes.length; i++){
			list.add(new DebitNoteEntry(debitNotes[i]));
		}
		
	}


}
