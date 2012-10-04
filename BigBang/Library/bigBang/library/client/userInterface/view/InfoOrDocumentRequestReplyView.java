package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.InfoOrDocumentRequest.Response;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.form.InfoOrDocumentRequestReplyForm;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestReplyViewPresenter;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestReplyViewPresenter.Action;

public class InfoOrDocumentRequestReplyView extends View implements InfoOrDocumentRequestReplyViewPresenter.Display {

	private ActionInvokedEventHandler<Action> handler;
	private InfoOrDocumentRequestReplyForm form;
	private BigBangOperationsToolBar toolbar;
	
	public InfoOrDocumentRequestReplyView(){
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("700px", "400px");
		
		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				return;
			}

			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestReplyViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<InfoOrDocumentRequestReplyViewPresenter.Action>(Action.CANCEL));
			}
		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.setSaveModeEnabled(true);
		wrapper.add(toolbar);
		
		form = new InfoOrDocumentRequestReplyForm();
		form.setSize("100%", "100%");
		form.setReadOnly(false);
		
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Response> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		toolbar.setSaveModeEnabled(enabled);
	}

}
