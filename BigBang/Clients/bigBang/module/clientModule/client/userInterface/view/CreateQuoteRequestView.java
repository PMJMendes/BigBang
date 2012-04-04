package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.module.clientModule.client.userInterface.CreateQuoteRequestChildrenPanel;
import bigBang.module.clientModule.client.userInterface.CreateQuoteRequestToolbar;
import bigBang.module.clientModule.client.userInterface.presenter.CreateQuoteRequestViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.CreateQuoteRequestViewPresenter.Action;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestForm;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.FiresAsyncRequests;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;

public class CreateQuoteRequestView extends View implements CreateQuoteRequestViewPresenter.Display {

	protected ClientFormView ownerForm;
	protected QuoteRequestForm requestForm;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected CreateQuoteRequestChildrenPanel childrenPanel;
	protected CreateQuoteRequestToolbar toolbar;
	
	public CreateQuoteRequestView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		wrapper.addWest(ownerWrapper, 600);
		
		ListHeader ownerHeader = new ListHeader("Cliente");
		ownerHeader.setHeight("30px");
		ownerWrapper.add(ownerHeader);
		
		ownerForm = new ClientFormView();
		ownerForm.setSize("100%", "100%");
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		
		SplitLayoutPanel requestWrapper = new SplitLayoutPanel();
		wrapper.add(requestWrapper);
		requestWrapper.setSize("100%", "100%");
		
		this.childrenPanel = new CreateQuoteRequestChildrenPanel();
		this.childrenPanel.setSize("100%", "100%");
		requestWrapper.addEast(this.childrenPanel, 250);
		
		VerticalPanel requestVerticalWrapper = new VerticalPanel();
		requestVerticalWrapper.setSize("100%", "100%");
		requestWrapper.add(requestVerticalWrapper);
		
		ListHeader requestHeader = new ListHeader("Consulta de Mercado");
		requestHeader.setHeight("30px");
		requestVerticalWrapper.add(requestHeader);
		
		toolbar = new CreateQuoteRequestToolbar(){

			@Override
			public void onCreateInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.CREATE_INSURED_OBJECT));
			}

			@Override
			public void onCreatePersonObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.CREATE_PERSON_INSURED_OBJECT));
			}

			@Override
			public void onCreateCompanyObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.CREATE_COMPANY_INSURED_OBJECT));
			}

			@Override
			public void onCreateEquipmentObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.CREATE_EQUIPMENT_INSURED_OBJECT));
			}

			@Override
			public void onCreateLocationObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.CREATE_LOCATION_INSURED_OBJECT));
			}

			@Override
			public void onCreateAnimalObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.CREATE_ANIMAL_INSURED_OBJECT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CreateQuoteRequestViewPresenter.Action>(Action.CANCEL));				
			}
			
		};
		requestVerticalWrapper.add(toolbar);
		
		requestForm = new QuoteRequestForm();
		requestForm.setSize("100%", "100%");
		requestForm.setReadOnly(false);
		requestVerticalWrapper.add(requestForm);
		requestVerticalWrapper.setCellHeight(requestForm, "100%");
		
		requestForm.addValueChangeHandler(new ValueChangeHandler<QuoteRequest>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<QuoteRequest> event) {
				childrenPanel.setOwner(event.getValue());
			}
		});
	}

	@Override
	public HasEditableValue<QuoteRequest> getForm() {
		return this.requestForm;
	}

	@Override
	public HasValue<Client> getOwnerForm() {
		return this.ownerForm;
	}

	@Override
	public FiresAsyncRequests getFormAsFiresAsyncRequests() {
		return this.requestForm;
	}

	@Override
	public HasValueSelectables<QuoteRequestObjectStub> getObjectsList() {
		return this.childrenPanel.insuredObjectsList;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	protected void initializeView() {
		return;
	}

}
