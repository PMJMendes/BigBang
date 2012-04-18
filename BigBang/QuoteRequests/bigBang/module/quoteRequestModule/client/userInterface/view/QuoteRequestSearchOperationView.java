package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.FiresAsyncRequests;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestChildrenPanel;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestForm;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestProcessToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSearchPanel;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter.Action;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QuoteRequestSearchOperationView extends View implements QuoteRequestSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX 

	protected QuoteRequestSearchPanel searchPanel;
	protected QuoteRequestForm form;
	protected QuoteRequestProcessToolBar toolbar;
	protected QuoteRequestChildrenPanel childrenPanel;
	protected ActionInvokedEventHandler<Action> handler;

	public QuoteRequestSearchOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		searchPanel = new QuoteRequestSearchPanel();
		wrapper.addWest(searchPanel, 400);

		SplitLayoutPanel quoteRequestWrapper = new SplitLayoutPanel();
		quoteRequestWrapper.setSize("100%", "100%");
		wrapper.add(quoteRequestWrapper);

		childrenPanel = new QuoteRequestChildrenPanel();
		childrenPanel.setSize("100%", "100%");
		quoteRequestWrapper.addEast(childrenPanel, 250);
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		quoteRequestWrapper.add(formWrapper);
		
		toolbar = new QuoteRequestProcessToolBar() {

			@Override
			public void onSaveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.EDIT));

			}

			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CANCEL));

			}

			@Override
			public void onSendResponseToClient() {
				// TODO Auto-generated method stub

			}
			
			public void onCreateManagerTransfer() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CREATE_MANAGER_TRANSFER));
			};

			@Override
			public void onIncludeInsuredObject() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateInsuredObject() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CREATE_INSURED_OBJECT));

			}
			
			@Override
			public void onCreatePersonObject() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CREATE_PERSON_INSURED_OBJECT));
				
			}
			
			@Override
			public void onCreateCompanyObject() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CREATE_COMPANY_INSURED_OBJECT));
				
			}
			
			@Override
			public void onCreateEquipmentObject() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CREATE_EQUIPMENT_INSURED_OBJECT));
				
			}
			
			@Override
			public void onCreateAnimalObject() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CREATE_ANIMAL_INSURED_OBJECT));				
			}
			
			@Override
			public void onCreateLocationObject() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CREATE_LOCATION_INSURED_OBJECT));
				
			}

			@Override
			public void onInfoOrDocumentRequest() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDelete() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onCreateNegotiation() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCloseProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.CLOSE));
			}
		};
		formWrapper.add(toolbar);
				
		form = new QuoteRequestForm();
		form.addValueChangeHandler(new ValueChangeHandler<QuoteRequest>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<QuoteRequest> event) {
				childrenPanel.setOwner(event.getValue());
			}
		});
		form.setSize("100%", "100%");
		formWrapper.add(form);
		formWrapper.setCellHeight(form, "100%");
		
		searchPanel.doSearch();
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<QuoteRequestStub> getList() {
		return searchPanel;
	}

	@Override
	public HasEditableValue<QuoteRequest> getForm() {
		return form;
	}
	
	@Override
	public FiresAsyncRequests getFormAsFiresAsyncRequests() {
		return form;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.lockAll();
	}

	@Override
	public void allowEdit(boolean allow) {
		toolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		toolbar.allowDelete(allow);
	}
	
	@Override
	public void allowClose(boolean allow) {
		toolbar.allowClose(allow);
	}
	
	@Override
	public void allowCreateInsuredObject(boolean allow) {
		toolbar.allowCreateInsuredObject(allow);
	}
	
	@Override
	public void allowCreateManagerTransfer(boolean allow) {
		toolbar.allowCreateManagerTransfer(allow);
	}

	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return childrenPanel.contactsList;
	}
	
	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return childrenPanel.documentsList;
	}
	
	@Override
	public HasValueSelectables<QuoteRequestObjectStub> getObjectsList() {
		return childrenPanel.insuredObjectsList;
	}

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return childrenPanel.subProcessesList;
	}
	
	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return childrenPanel.historyList;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

}