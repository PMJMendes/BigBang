package bigBang.module.quoteRequestModule.client.userInterface.view;


import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationForm;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationOperationsToolBar;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter.Action;

public abstract class NegotiationView<T> extends View implements NegotiationViewPresenter.Display{

	protected NegotiationForm form;
	private NegotiationOperationsToolBar toolbar;
	private ActionInvokedEventHandler<Action> handler;
	protected FormView<T> ownerForm;
	protected DocumentsList documents;
	protected ContactsList contacts;
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
		};

		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);

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

		StackPanel stackWrapper = new StackPanel();
		stackWrapper.add(contacts, "Contactos");
		stackWrapper.add(documents, "Documentos");

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
		;
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
	public abstract void setParentHeaderTitle(String title);

	@Override
	public void applyOwnerToList(String negotiationId) {
		contacts.setOwner(negotiationId);
		documents.setOwner(negotiationId);
	}
	
	@Override
	public HasValueSelectables<Contact> getContactList() {
		return contacts;
	}
	
	@Override
	public HasValueSelectables<Document> getDocumentList() {
		return documents;
	}
	
}
