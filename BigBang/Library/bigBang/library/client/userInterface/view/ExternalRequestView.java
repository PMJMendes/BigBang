package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ExternalInfoRequestForm;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.ExternalRequestViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalRequestViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class ExternalRequestView<T extends ProcessBase> extends View implements ExternalRequestViewPresenter.Display<T>{

	private FormView<T> ownerForm;
	private ExternalInfoRequestForm form;
	protected ListHeader ownerHeader;
	private BigBangOperationsToolBar toolbar;

	public ExternalRequestView(FormView<T> ownerForm){

		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);

		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel left = new VerticalPanel();
		left.setSize("100%", "100%");
		ownerHeader = new ListHeader();
		left.add(ownerHeader);
		left.setCellWidth(ownerHeader,"100%");
		left.add(ownerForm);
		left.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(left, 650);

		VerticalPanel right = new VerticalPanel();
		right.setSize("100%", "100%");
		
		ListHeader externalRequestHeader = new ListHeader("Pedido de Informação Externo");
		right.add(externalRequestHeader);
		right.setCellWidth(externalRequestHeader, "100%");

		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				return;
			}

			@Override
			public void onSaveRequest() {
				fireAction(Action.CONFIRM);
			}

			@Override
			public void onCancelRequest() {

				fireAction(Action.CANCEL);

			}

		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.setSaveModeEnabled(true);
		form = new ExternalInfoRequestForm();
		right.add(toolbar);
		right.add(form);
		right.setCellHeight(form, "100%");
		mainWrapper.add(right);
	}

	private ActionInvokedEventHandler<Action> actionHandler;

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


	public abstract void setParentHeaderTitle(String title);


	@Override
	public HasEditableValue<ExternalInfoRequest> getForm(){
		return form;
	}

	@Override
	public void setToolbarSaveMode(boolean b) {
		toolbar.setSaveModeEnabled(b);
	}
}
