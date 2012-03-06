package bigBang.module.quoteRequestModule.client.userInterface;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationViewPresenter.Action;

public abstract class NegotiationView<T> extends View implements NegotiationViewPresenter.Display{

	protected NegotiationForm form;
	private BigBangOperationsToolBar toolbar;
	private MenuItem delete;
	private ActionInvokedEventHandler<Action> handler;
	protected FormView<T> ownerForm;
	protected DocumentsList documents;
	protected ContactsList contacts;

	
	public NegotiationView(FormView<T> ownerForm){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT);
			}

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE);
			}

			@Override
			public void onCancelRequest() {

				fireAction(Action.CANCEL);

			}

		};

		toolbar.hideAll();
		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				fireAction(Action.DELETE);
			}
		});
		toolbar.addItem(SUB_MENU.ADMIN, delete);
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);
		
		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		ListHeader ownerHeader = new ListHeader("Ficha de Processo");
		ownerHeader.setHeight("30px");
		ownerWrapper.add(ownerHeader);
		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerWrapper, "100%");
		mainWrapper.addWest(ownerWrapper, 665);
		
		StackPanel stackWrapper = new StackPanel();
		stackWrapper.add(contacts, "Contactos");
		stackWrapper.add(documents, "Documentos");

		stackWrapper.setHeight("100%");
		mainWrapper.addEast(stackWrapper, 250);
		
		
		VerticalPanel negotiationFormWrapper = new VerticalPanel();
		negotiationFormWrapper.setSize("100%", "100%");
		ListHeader negotiationHeader = new ListHeader("Negociação");
		negotiationFormWrapper.add(negotiationHeader);
		negotiationHeader.setHeight("30px");
		negotiationFormWrapper.add(toolbar);
		negotiationFormWrapper.setCellHeight(toolbar, "21px");
		form = new NegotiationForm();
		negotiationFormWrapper.add(form);
		negotiationFormWrapper.setHeight("100%");
		mainWrapper.add(negotiationFormWrapper);
	
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
}
