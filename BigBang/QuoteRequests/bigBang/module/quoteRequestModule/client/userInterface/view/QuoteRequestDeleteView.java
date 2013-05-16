package bigBang.module.quoteRequestModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestDeleteToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestDeleteForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestDeleteViewPresenter;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;

public class QuoteRequestDeleteView extends View implements QuoteRequestDeleteViewPresenter.Display {

	private ActionInvokedEventHandler<QuoteRequestDeleteViewPresenter.Action> actionHandler;
	private FormView<String> form;
	private QuoteRequestDeleteToolBar toolBar;

	public QuoteRequestDeleteView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolBar = new QuoteRequestDeleteToolBar() {

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestDeleteViewPresenter.Action>(QuoteRequestDeleteViewPresenter.Action.CANCEL));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestDeleteViewPresenter.Action>(QuoteRequestDeleteViewPresenter.Action.DELETE));
			}
		};
		wrapper.add(toolBar);
		
		form = new QuoteRequestDeleteForm();
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
	public void registerActionHandler(ActionInvokedEventHandler<QuoteRequestDeleteViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}

}
