package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestCloseToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestCloseForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestCloseViewPresenter;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;

public class QuoteRequestCloseView extends View implements QuoteRequestCloseViewPresenter.Display {

	private ActionInvokedEventHandler<QuoteRequestCloseViewPresenter.Action> actionHandler;
	private FormView<String> form;
	private QuoteRequestCloseToolBar toolBar;

	public QuoteRequestCloseView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolBar = new QuoteRequestCloseToolBar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestCloseViewPresenter.Action>(QuoteRequestCloseViewPresenter.Action.CANCEL));
			}

			@Override
			public void onClose() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestCloseViewPresenter.Action>(QuoteRequestCloseViewPresenter.Action.CLOSE));
			}
		};
		wrapper.add(toolBar);
		
		form = new QuoteRequestCloseForm();
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form.getNonScrollableContent(), "100%");

		initWidget(wrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<String> getForm() {
		return this.form;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<QuoteRequestCloseViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}

}
