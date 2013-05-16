package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.ReceiveMessageToolbar;
import bigBang.library.client.userInterface.form.ReceiveMessageForm;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReceiveMessageView<T extends ProcessBase> extends View implements ReceiveMessageViewPresenter.Display<T>{

	private FormView<T> ownerForm;
	private ReceiveMessageForm form;
	private ReceiveMessageToolbar toolbar;

	public ReceiveMessageView(FormView<T> ownerForm){

		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);

		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel left = new VerticalPanel();
		left.setSize("100%", "100%");
		ListHeader ownerHeader = new ListHeader("Ficha de Processo");
		left.add(ownerHeader);
		left.setCellWidth(ownerHeader,"100%");
		ownerHeader.setLeftWidget(new Button("Voltar", new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ReceiveMessageViewPresenter.Action>(Action.ON_CLICK_BACK));
			}

		}));
		left.add(ownerForm);
		left.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(left, 650);

		VerticalPanel right = new VerticalPanel();
		right.setSize("100%", "100%");

		ListHeader externalRequestHeader = new ListHeader("Mensagem");
		right.add(externalRequestHeader);
		right.setCellWidth(externalRequestHeader, "100%");

		toolbar = new ReceiveMessageToolbar() {

			@Override
			public void onReceiveRequest() {
				fireAction(Action.CONFIRM);
			}

			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL);				
			}

		};

		form = new ReceiveMessageForm();
		right.add(toolbar);
		right.add(form);
		right.setCellHeight(form, "100%");
		mainWrapper.add(right);
	}

	protected ActionInvokedEventHandler<Action> actionHandler;

	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}


	@SuppressWarnings("unchecked")
	public HasValue<ProcessBase> getOwnerForm(){
		return (HasValue<ProcessBase>) ownerForm;
	}


	@Override
	public HasEditableValue<Conversation> getForm(){
		return form;
	}

	@Override
	public void setToolbarSaveMode(boolean b) {
		toolbar.setSaveModeEnabled(b);
	}

	protected void initializeView() {
		return;
	}
}
