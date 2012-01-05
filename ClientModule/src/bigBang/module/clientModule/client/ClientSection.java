package bigBang.module.clientModule.client;

import java.util.ArrayList;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;
import bigBang.library.client.event.ScreenInvokedEvent;
import bigBang.library.client.event.ScreenInvokedEventHandler;
import bigBang.library.client.event.ShowMeRequestEvent;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientManagerTransferOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientManagerTransferOperationView;
import bigBang.module.clientModule.client.userInterface.view.ClientSearchOperationView;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;
import bigBang.module.clientModule.shared.operation.ClientManagerTransferOperation;
import bigBang.module.clientModule.shared.operation.ClientSearchOperation;

import com.google.gwt.core.client.GWT;

public class ClientSection implements MenuSection {

	private static final String ID = "CLIENT_SECTION";
	private static final String DESCRIPTION = "Clientes";
	private static final String SHORT_DESCRIPTION = "Clientes";
	
	private TextBadge badge;
	private ArrayList<OperationViewPresenter> sectionOperationPresenters; 
	
	public ClientSection(){
		init();
	}
	
	private void init(){
		this.badge = null;
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();

		/*Init the operations available for this process section*/
//		BigBangPermissionManager.Util.getInstance().getProcessPermissionContext(BigBangConstants.EntityIds.CLIENT, new ResponseHandler<Void>() {
//			
//			@Override
//			public void onResponse(Void response) {
				ClientServiceAsync clientService = ClientService.Util.getInstance();
				
				/* SEARCH */
				ClientSearchOperation clientSearchOperation = (ClientSearchOperation)GWT.create(ClientSearchOperation.class);
				ClientSearchOperationView clientSearchOperationView = new ClientSearchOperationView();
				ClientSearchOperationViewPresenter clientSearchOperationPresenter = new ClientSearchOperationViewPresenter(null, clientService, clientSearchOperationView);
				clientSearchOperationPresenter.setOperation(clientSearchOperation);
				sectionOperationPresenters.add((OperationViewPresenter)clientSearchOperationPresenter);
				
				/* TRANSFERENCIA DE GESTOR */
				ClientManagerTransferOperation clientManagerTransferOperation = (ClientManagerTransferOperation)GWT.create(ClientManagerTransferOperation.class);
				ClientManagerTransferOperationView clientManagerTransferOperationView = new ClientManagerTransferOperationView();
				ClientManagerTransferOperationViewPresenter clientManagerTransferOperationViewPresenter = new ClientManagerTransferOperationViewPresenter(null, clientService, clientManagerTransferOperationView);
				clientManagerTransferOperationViewPresenter.setOperation(clientManagerTransferOperation);
				sectionOperationPresenters.add((OperationViewPresenter)clientManagerTransferOperationViewPresenter);
//			}
//			
//			@Override
//			public void onError(Collection<ResponseError> errors) {
//			}
//		});
	}
	
	public String getId() {
		return ID;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public TextBadge getBadge() {
		return this.badge;
	}
	
	public boolean hasBadge(){
		return badge != null;
	}
	
	public OperationViewPresenter[] getOperationPresenters(){
		int nOps = this.sectionOperationPresenters.size();
		OperationViewPresenter[] result = new OperationViewPresenter[nOps];
		for(int i = 0; i < nOps; i++)
			result[i] = (OperationViewPresenter)this.sectionOperationPresenters.toArray()[i];
		return result;
	}

	public void registerEventHandlers(final EventBus eventBus) {
		for(ViewPresenter p : sectionOperationPresenters)
			p.setEventBus(eventBus);
				
		eventBus.addHandler(ScreenInvokedEvent.TYPE, new ScreenInvokedEventHandler() {
			
			@Override
			public void onScreenInvoked(ScreenInvokedEvent event) {
				if(event.getProcessTypeId().equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
					eventBus.fireEvent(new ShowMeRequestEvent(this));
				}
			}
		});
	}

	@Override
	public MenuSections getMenuIndex() {
		return MenuSections.CLIENT_SECTION;
	}

}
