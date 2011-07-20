package bigBang.module.clientModule.client;

import java.util.ArrayList;

import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientManagerTransferOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientManagerTransferOperationView;
import bigBang.module.clientModule.client.userInterface.view.ClientMergeOperationView;
import bigBang.module.clientModule.client.userInterface.view.ClientSearchOperationView;
import bigBang.module.clientModule.shared.operation.ClientManagerTransferOperation;
import bigBang.module.clientModule.shared.operation.ClientMergeOperation;
import bigBang.module.clientModule.shared.operation.ClientSearchOperation;

import com.google.gwt.core.client.GWT;

public class ClientSection implements MenuSection {

	private static final String ID = "CLIENT_SECTION";
	private static final String DESCRIPTION = "Cliente";
	private static final String SHORT_DESCRIPTION = "Cliente";
	
	private TextBadge badge;
	private ArrayList<OperationViewPresenter> sectionOperationPresenters; 
	
	public ClientSection(){
		init();
	}
	
	private void init(){
		this.badge = null;
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		
		/*Init the operations available for this process section*/
		
		/* SEARCH */
		ClientSearchOperation clientSearchOperation = (ClientSearchOperation)GWT.create(ClientSearchOperation.class);
		ClientSearchOperationView clientSearchOperationView = new ClientSearchOperationView();
		ClientSearchOperationViewPresenter clientSearchOperationPresenter = new ClientSearchOperationViewPresenter(null, null, clientSearchOperationView);
		clientSearchOperationPresenter.setOperation(clientSearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)clientSearchOperationPresenter);
		
		/* TRANSFERENCIA DE GESTOR */
		ClientManagerTransferOperation clientManagerTransferOperation = (ClientManagerTransferOperation)GWT.create(ClientManagerTransferOperation.class);
		ClientManagerTransferOperationView clientManagerTransferOperationView = new ClientManagerTransferOperationView();
		ClientManagerTransferOperationViewPresenter clientManagerTransferOperationViewPresenter = new ClientManagerTransferOperationViewPresenter(null, null, clientManagerTransferOperationView);
		clientManagerTransferOperationViewPresenter.setOperation(clientManagerTransferOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)clientManagerTransferOperationViewPresenter);
		
//TODO IMPORTANT
		/* MERGE */ 
//		ClientMergeOperation clientMergeOperation = (ClientMergeOperation)GWT.create(ClientMergeOperation.class);
//		ClientMergeOperationView clientMergeOperationView = new ClientMergeOperationView();
//		ClientMergeOperationViewPresenter clientMergeOperationPresenter = new ClientMergeOperationViewPresenter(null, null, clientMergeOperationView);
//		clientMergeOperationPresenter.setOperation(clientMergeOperation);
//		this.sectionOperationPresenters.add((OperationViewPresenter)clientMergeOperationPresenter);
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

	public void registerEventHandlers(EventBus eventBus) {
		for(ViewPresenter p : sectionOperationPresenters)
			p.setEventBus(eventBus);	
	}

	@Override
	public MenuSections getMenuIndex() {
		return MenuSections.CLIENT_SECTION;
	}

}
