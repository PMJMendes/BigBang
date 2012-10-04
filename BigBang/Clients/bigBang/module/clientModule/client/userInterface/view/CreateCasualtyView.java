package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.casualtyModule.client.userInterface.form.CasualtyForm;
import bigBang.module.clientModule.client.userInterface.CreateCasualtyOperationsToolbar;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;
import bigBang.module.clientModule.client.userInterface.presenter.CreateCasualtyViewPresenter.Action;
import bigBang.module.clientModule.client.userInterface.presenter.CreateCasualtyViewPresenter;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;

public class CreateCasualtyView extends View implements CreateCasualtyViewPresenter.Display {

	protected ClientForm clientForm;
	protected CasualtyForm form;
	protected ActionInvokedEventHandler<Action> handler;
	protected CreateCasualtyOperationsToolbar toolbar;
	
	public CreateCasualtyView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel parentWrapper = new VerticalPanel();
		parentWrapper.setSize("100%", "100%");
		wrapper.addWest(parentWrapper, 600);
		
		ListHeader parentHeader = new ListHeader("Cliente");
		parentHeader.setHeight("30px");
		parentWrapper.add(parentHeader);
		
		clientForm = new ClientForm();
		clientForm.setReadOnly(true);
		clientForm.setSize("100%", "100%");
		parentWrapper.add(clientForm);
		parentWrapper.setCellHeight(clientForm, "100%");
		
		VerticalPanel casualtyWrapper = new VerticalPanel();
		casualtyWrapper.setSize("100%", "100%");
		wrapper.add(casualtyWrapper);
		
		ListHeader casualtyHeader = new ListHeader("Sinistro");
		casualtyHeader.setHeight("30px");
		casualtyWrapper.add(casualtyHeader);
		
		toolbar = new CreateCasualtyOperationsToolbar() {
			
			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<CreateCasualtyViewPresenter.Action>(Action.SAVE));
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<CreateCasualtyViewPresenter.Action>(Action.CANCEL));

			}
		};
		casualtyWrapper.add(toolbar);
		
		form = new CasualtyForm();
		form.setForCreate();
		form.setReadOnly(false);
		form.setSize("100%", "100%");
		casualtyWrapper.add(form);
		casualtyWrapper.setCellHeight(form, "100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValue<Client> getClientForm() {
		return this.clientForm;
	}

	@Override
	public HasEditableValue<Casualty> getForm() {
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
