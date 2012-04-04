package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectOperationsToolbar;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestForm;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestObjectForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestObjectViewPresenter;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QuoteRequestObjectView extends View implements QuoteRequestObjectViewPresenter.Display {

	protected QuoteRequestForm quoteRequestForm;
	protected QuoteRequestObjectForm form;
	private InsuredObjectOperationsToolbar toolbar;
	protected ActionInvokedEventHandler<QuoteRequestObjectViewPresenter.Action> actionHandler;
	
	public QuoteRequestObjectView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		VerticalPanel insurancePolicyFormWrapper = new VerticalPanel();
		insurancePolicyFormWrapper.setSize("100%", "100%");
				
		ListHeader insurancePolicyFormHeader = new ListHeader("Consulta de Mercado");
		insurancePolicyFormHeader.setHeight("30px");
		insurancePolicyFormWrapper.add(insurancePolicyFormHeader);
		
		quoteRequestForm = new QuoteRequestForm();
		quoteRequestForm.setSize("100%", "100%");
		insurancePolicyFormWrapper.add(quoteRequestForm);
		insurancePolicyFormWrapper.setCellHeight(quoteRequestForm, "100%");
		mainWrapper.addWest(insurancePolicyFormWrapper, 600);
		
		VerticalPanel objectFormWrapper = new VerticalPanel();
		objectFormWrapper.setSize("100%", "100%");
		
		ListHeader objectHeader = new ListHeader("Unidade de Risco");
		objectHeader.setHeight("30px");
		objectFormWrapper.add(objectHeader);

		this.toolbar = new InsuredObjectOperationsToolbar(){

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestObjectViewPresenter.Action>(QuoteRequestObjectViewPresenter.Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestObjectViewPresenter.Action>(QuoteRequestObjectViewPresenter.Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestObjectViewPresenter.Action>(QuoteRequestObjectViewPresenter.Action.CANCEL_EDIT));
			}

			@Override
			public void onDeleteRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestObjectViewPresenter.Action>(QuoteRequestObjectViewPresenter.Action.DELETE));
			}
		};
		
		objectFormWrapper.add(toolbar);

		form = new QuoteRequestObjectForm();
		form.setSize("100%", "100%");
		objectFormWrapper.add(form);
		form.showTypeSection(false);
		objectFormWrapper.setCellHeight(form, "100%");
		mainWrapper.add(objectFormWrapper);

		quoteRequestForm.setReadOnly(true);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<QuoteRequestObject> getQuoteRequestObjectForm() {
		return this.form;
	}

	@Override
	public HasEditableValue<QuoteRequest> getQuoteRequestForm() {
		return this.quoteRequestForm;
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<QuoteRequestObjectViewPresenter.Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void allowEdit(boolean allow) {
		toolbar.allowEdit(allow);
	}

}
