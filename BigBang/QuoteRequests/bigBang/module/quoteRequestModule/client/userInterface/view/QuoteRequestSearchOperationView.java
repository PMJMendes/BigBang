package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.ChooseObjectTypePanel;
import bigBang.module.quoteRequestModule.client.userInterface.ChooseSublinePanel;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestObjectSearchPanel;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestChildrenPanel;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestNotesFormSection;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestProcessToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSearchPanel;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSearchPanel.Entry;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSelectButton;
import bigBang.module.quoteRequestModule.client.userInterface.form.CompositeObjectForm;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestHeaderForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter.Action;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QuoteRequestSearchOperationView extends View implements QuoteRequestSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	private QuoteRequestSearchPanel searchPanel;
	protected ActionInvokedEventHandler<QuoteRequestSearchOperationViewPresenter.Action> actionHandler;
	protected QuoteRequestChildrenPanel childrenPanel;
	protected QuoteRequestProcessToolBar toolbar;
	private QuoteRequestObjectSearchPanel objectsList;
	private QuoteRequestHeaderForm quoteRequestForm;
	private CompositeObjectForm objectForm;
	private QuoteRequestNotesFormSection quoteRequestNotesForm;
	private QuoteRequestSelectButton quoteRequestSelectButton;
	private ChooseSublinePanel chooseSublinePanel;
	private ChooseObjectTypePanel chooseObjectTypePanel;


	public QuoteRequestSearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");

		ListHeader searchPanelHeader = new ListHeader("Consultas de Mercado");
		searchPanelWrapper.add(searchPanelHeader);

		quoteRequestForm = new QuoteRequestHeaderForm();
		objectForm = new CompositeObjectForm();

		searchPanel = new QuoteRequestSearchPanel();

		doSearch(true);

		searchPanelWrapper.add(searchPanel);

		searchPanelWrapper.setCellHeight(searchPanel, "100%");

		mainWrapper.addWest(searchPanelWrapper, SEARCH_PANEL_WIDTH);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		childrenPanel = new QuoteRequestChildrenPanel();
		childrenPanel.setSize("100%", "100%");
		contentWrapper.addEast(childrenPanel, 300);

		toolbar = new QuoteRequestProcessToolBar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.EDIT));

			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.CANCEL));
			}

			@Override
			public void onSendResponseToClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.SEND_RESPONSE_TO_CLIENT));
			}

			@Override
			public void onSendMessage() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.SEND_MESSAGE));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.DELETE));
			}

			@Override
			public void onCreateNegotiation() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.CREATE_NEGOTIATION));
			}

			@Override
			public void onCreateManagerTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.MANAGER_TRANSFER));
			}

			@Override
			public void onCloseProcess() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.CLOSE));

			}

			@Override
			public void onReceiveMessage() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.RECEIVE_MESSAGE));

			}
		};

		VerticalPanel formContainer = new VerticalPanel();
		formContainer.setSize("100%", "100%");

		HorizontalPanel formPanel = new HorizontalPanel();
		formPanel.getElement().getStyle().setProperty("borderBottom", "1px solid #688AA2");

		VerticalPanel objectsQuotePanel = new VerticalPanel();
		objectsQuotePanel.getElement().getStyle().setProperty("borderRight", "1px solid #688AA2");

		quoteRequestSelectButton = new QuoteRequestSelectButton(new QuoteRequestStub());

		objectsQuotePanel.setSize("100%", "100%");
		objectsQuotePanel.add(quoteRequestSelectButton);

		quoteRequestSelectButton.addSelectedStateChangedEventHandler(new SelectedStateChangedEventHandler() {

			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				if (event.getSelected()){
					actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.QUOTE_REQUEST_SELECTED));
				}				
			}
		});

		objectsList = new QuoteRequestObjectSearchPanel();
		objectsList.showFilterField(false);
		objectsList.getNewObjectButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(Action.NEW_OBJECT));
			}
		});

		objectsQuotePanel.add(objectsList);
		objectsQuotePanel.setCellHeight(objectsList, "100%");
		objectsList.setSize("100%", "100%");
		formPanel.add(objectsQuotePanel);
		objectsQuotePanel.setCellWidth(objectsList, "200px");
		formPanel.setCellWidth(objectsQuotePanel, "200px");
		formPanel.setCellHeight(objectsQuotePanel, "100%");
		formPanel.add(quoteRequestForm.getNonScrollableContent());
		formPanel.add(objectForm.getNonScrollableContent());

		objectForm.getNonScrollableContent().setVisible(false);

		formPanel.setCellWidth(quoteRequestForm, "100%");
		quoteRequestForm.getNonScrollableContent().setSize("100%", "100%");
		objectForm.getNonScrollableContent().setSize("100%", "100%");
		formPanel.setSize("100%", "100%");
		formContainer.add(formPanel);
		formContainer.setCellHeight(formPanel, "100%");

		ScrollPanel scrollContainer = new ScrollPanel();
		scrollContainer.setSize("100%", "100%");
		scrollContainer.getElement().getStyle().setProperty("overflowx","hidden");
		VerticalPanel centerWrapper = new VerticalPanel();

		centerWrapper.setSize("100%", "100%");

		VerticalPanel toolbarAndCenterWrapper = new VerticalPanel();
		toolbarAndCenterWrapper.setSize("100%", "100%");
		toolbarAndCenterWrapper.add(toolbar);

		centerWrapper.add(formContainer);
		centerWrapper.setCellHeight(formContainer, "100%");
		VerticalPanel detailTableContainer = new VerticalPanel();
		detailTableContainer.setSize("100%", "100%");

		quoteRequestNotesForm = new QuoteRequestNotesFormSection();
		centerWrapper.add(quoteRequestNotesForm);

		scrollContainer.add(centerWrapper);
		scrollContainer.getElement().getStyle().setBackgroundColor("whiteSmoke");
		scrollContainer.getElement().getStyle().setOverflowX(Style.Overflow.HIDDEN);

		toolbarAndCenterWrapper.add(scrollContainer);
		toolbarAndCenterWrapper.setCellHeight(scrollContainer, "100%");
		contentWrapper.add(toolbarAndCenterWrapper);
		mainWrapper.add(contentWrapper);
		
		chooseSublinePanel = new ChooseSublinePanel();
		
		chooseSublinePanel.addAttachHandler(new Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(!event.isAttached()){
					if(chooseSublinePanel.getValue() != null){
						actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.NEW_SUBLINE));
					}
				}
			}
		});

		
		chooseObjectTypePanel.addAttachHandler(new Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(!event.isAttached()){
					if(chooseSublinePanel.getValue() != null){
						actionHandler.onActionInvoked(new ActionInvokedEvent<QuoteRequestSearchOperationViewPresenter.Action>(QuoteRequestSearchOperationViewPresenter.Action.NEW_OBJECT_CHOSEN));
					}
				}
			}
		});
	}



	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void doSearch(boolean b) {
		searchPanel.doSearch(b);
	}



	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	}



	@Override
	public void clear() {
		objectForm.clearInfo();
		quoteRequestForm.clearInfo();
	}



	@Override
	public void setToolbarEditMode(boolean b) {
		toolbar.setSaveModeEnabled(b);
	}



	@Override
	public void allowEdit(boolean b) {
		toolbar.allowEdit(b);
	}



	@Override
	public HasValue<QuoteRequestStub> getQuoteRequestSelector() {
		return quoteRequestSelectButton;
	}



	@Override
	public void setOwner(QuoteRequest object) {
		childrenPanel.setOwner(object);
	}



	@Override
	public void clearObjectsList() {
		
	}



	@Override
	public HasValue<String> getQuoteRequestNotesForm() {
		return quoteRequestNotesForm;
	}



	@Override
	public void setReadOnly(boolean b) {
		objectForm.setReadOnly(b);
		quoteRequestForm.setReadOnly(b);
		quoteRequestNotesForm.setReadOnly(b);
	}



	@Override
	public HasValueSelectables<QuoteRequestStub> getList() {
		return searchPanel;
	}



	@Override
	public void addEntryToList(Entry entry) {
		searchPanel.add(0, entry);
		entry.setSelected(true,false);
	}




	@Override
	public HasValueSelectables<QuoteRequestObjectStub> getInsuredObjectsList() {
		return objectsList;
	}



	@Override
	public HasEditableValue<QuoteRequest> getQuoteRequestHeaderForm() {
		return quoteRequestForm;
	}



	@Override
	public void showObjectForm(boolean b) {
		objectForm.setVisible(b);
	}



	@Override
	public void showQuoteRequestForm(boolean b) {
		this.quoteRequestForm.setVisible(b);
	}



	@Override
	public void setNotesReadOnly(boolean b) {
		quoteRequestNotesForm.setReadOnly(b);
	}



	@Override
	public void setQuoteRequestEntrySelected(boolean b) {
		quoteRequestSelectButton.setSelected(b);
	}



	@Override
	public void allowSendMessage(boolean hasPermission) {
		toolbar.allowSendMessage(hasPermission);

	}



	@Override
	public void allowReceiveMessage(boolean hasPermission) {
		toolbar.allowReceiveMessage(hasPermission);
	}



	@Override
	public void allowDelete(boolean hasPermission) {
		toolbar.allowDelete(hasPermission);
	}



	@Override
	public HasEditableValue<QuoteRequestObject> getObjectForm() {
		return objectForm;
	}



	@Override
	public void clearQuoteRequestSelection() {
		quoteRequestSelectButton.setSelected(false,false);
	}



	@Override
	public void dealWithObject(QuoteRequestObject info) {
		objectsList.dealWithObject(info);
	}



	@Override
	public void removeElementFromList(ValueSelectable<QuoteRequestStub> stub) {
		searchPanel.remove(stub);		
	}



	@Override
	public void focusObjectForm() {
		objectForm.focus();
	}
	
	public String getChosenSubline(){
		return chooseSublinePanel.getValue();
	}


}