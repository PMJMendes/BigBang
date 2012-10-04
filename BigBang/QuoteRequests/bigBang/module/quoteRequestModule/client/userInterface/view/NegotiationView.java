package bigBang.module.quoteRequestModule.client.userInterface.view;


import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationOperationsToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter.Action;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class NegotiationView<T> extends View implements NegotiationViewPresenter.Display{

	protected NegotiationForm form;
	private NegotiationOperationsToolBar toolbar;
	private ActionInvokedEventHandler<Action> handler;
	protected FormView<T> ownerForm;
	protected DocumentsList documents;
	protected ContactsList contacts;
	protected SubProcessesList subProcessesList;
	protected HistoryList historyList;
	protected ListHeader ownerHeader;

	public NegotiationView(FormView<T> ownerForm){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		toolbar = new NegotiationOperationsToolBar() {

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE);
			}

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT);
			}

			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL);
			}

			@Override
			public void onDeleteRequest() {
				fireAction(Action.DELETE);
			}

			@Override
			public void onCancelNegotiationRequest() {
				fireAction(Action.CANCEL_NEGOTIATION);
			}

			@Override
			public void onExternalRequest() {
				fireAction(Action.EXTERNAL_REQUEST);

			}

			@Override
			public void onGrant() {
				fireAction(Action.GRANT);
			}

			@Override
			public void onResponse() {
				fireAction(Action.RESPONSE);				
			}
		};

		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		ownerHeader = new ListHeader();
		ownerHeader.setHeight("30px");
		ownerWrapper.add(ownerHeader);
		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(ownerWrapper, 665);


		SplitLayoutPanel childWrapper = new SplitLayoutPanel();

		contacts = new ContactsList();
		documents = new DocumentsList();

		contacts.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(contacts.getSelected().size()>0){
					fireAction(Action.SELECTION_CHANGED_CONTACT);
				}
			}
		});

		documents.addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(documents.getSelected().size() > 0){
					fireAction(Action.SELECTION_CHANGED_DOCUMENT);
				}

			}
		});

		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();

		StackPanel stackWrapper = new StackPanel();
		stackWrapper.add(contacts, "Contactos");
		stackWrapper.add(documents, "Documentos");
		stackWrapper.add(subProcessesList, "Sub-Processos");
		stackWrapper.add(historyList, "Histórico");

		stackWrapper.setHeight("100%");
		stackWrapper.setWidth("100%");

		VerticalPanel negotiationWrapper = new VerticalPanel();

		negotiationWrapper.setSize("100%", "100%");
		ListHeader negotiationHeader = new ListHeader("Negociação");

		negotiationWrapper.add(negotiationHeader);
		negotiationHeader.setHeight("30px");

		VerticalPanel negotiationInnerWrapper = new VerticalPanel();
		negotiationInnerWrapper.setSize("100%", "100%");
		form = new NegotiationForm();
		negotiationInnerWrapper.add(toolbar);
		negotiationInnerWrapper.add(form);
		negotiationInnerWrapper.setCellHeight(toolbar, "21px");

		childWrapper.setSize("100%", "100%");

		negotiationInnerWrapper.setCellHeight(form, "100%");
		childWrapper.addEast(stackWrapper,260);
		childWrapper.add(negotiationInnerWrapper);

		negotiationWrapper.add(childWrapper);
		negotiationWrapper.setCellHeight(childWrapper, "100%");
		mainWrapper.add(negotiationWrapper);

	}
	protected void fireAction(Action action){
		if(this.handler != null) {
			handler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HasValue<ProcessBase> getOwnerForm() {
		return (HasValue<ProcessBase>) this.ownerForm;
	}

	@Override
	public NegotiationForm getForm() {
		return this.form;
	}

	@Override
	public void initializeView(){
		return;
	}

	@Override
	public void setToolbarSaveMode(boolean b){
		toolbar.setSaveModeEnabled(b);
	}

	@Override
	public void disableToolbar() {
		toolbar.lockAll();
	}

	@Override
	public void allowEdit(boolean b){

		toolbar.allowEdit(b);
	}

	@Override
	public void allowDelete(boolean b){
		toolbar.allowDelete(b);
	}

	@Override
	public void allowCancelNegotiation(boolean b){
		toolbar.allowCancelNegotiation(b);
	}

	@Override
	public void allowExternalRequest(boolean hasPermission) {
		toolbar.allowExternalRequest(hasPermission);
	}

	public abstract void setParentHeaderTitle(String title);

	@Override
	public void applyOwnerToList(String negotiationId) {
		contacts.setOwner(negotiationId);
		documents.setOwner(negotiationId);
		subProcessesList.setOwner(negotiationId);
		historyList.setOwner(negotiationId);
	}

	@Override
	public HasValueSelectables<Contact> getContactList() {
		return contacts;
	}

	@Override
	public HasValueSelectables<Document> getDocumentList() {
		return documents;
	}
	@Override
	public void allowGrant(boolean hasPermission) {
		toolbar.allowGrant(hasPermission);
	}

	@Override
	public void allowResponse(boolean hasPermission) {
		toolbar.allowResponse(hasPermission);
	};

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessList() {
		return this.subProcessesList;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.historyList;
	}
	@Override
	public HasSelectables<ValueSelectable<Document>> getDocumentsList() {
		return documents;
	}
	@Override
	public HasSelectables<ValueSelectable<Contact>> getContactsList() {
		return contacts;
	}
	@Override
	public void enableDocumentCreation(boolean allow) {
		documents.allowCreation(allow);
		
	}
	@Override
	public void enableContactCreation(boolean allow) {
		contacts.allowCreation(allow);	
	}
	
	@Override
	public void setOwnerTypeId(String ownerTypeId){
		contacts.setOwnerType(ownerTypeId);
		documents.setOwnerType(ownerTypeId);
	}
	
	public void allowContactDocumentEdit(boolean hasPermission) {
		contacts.allowCreation(hasPermission);
		documents.allowCreation(hasPermission);
		
	};

}
