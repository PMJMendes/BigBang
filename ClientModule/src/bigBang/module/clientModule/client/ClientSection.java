package bigBang.module.clientModule.client;

import java.util.Collection;
import java.util.HashMap;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Operation;
import bigBang.library.shared.userInterface.MenuSection;
import bigBang.library.shared.userInterface.TextBadge;
import bigBang.library.shared.userInterface.presenter.OperationViewPresenter;
import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;
import bigBang.library.shared.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSectionViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientMergeOperationView;
import bigBang.module.clientModule.client.userInterface.view.ClientSearchOperationView;
import bigBang.module.clientModule.shared.ClientMergeOperation;
import bigBang.module.clientModule.shared.ClientSearchOperation;

import com.google.gwt.core.client.GWT;

public class ClientSection implements MenuSection {

	private static final String ID = "CLIENT_SECTION";
	private static final String DESCRIPTION = "Cliente";
	private static final String SHORT_DESCRIPTION = "Cliente";
	
	private TextBadge badge;
	private HashMap<String, OperationViewPresenter> sectionOperationPresenters; 
	
	public ClientSection(){
		init();
	}
	
	private void init(){
		this.badge = null;
		this.sectionOperationPresenters = new HashMap<String, OperationViewPresenter>();
		
		/*Init the operations available for this process section*/
		
		/* SEARCH */
		ClientSearchOperation clientSearchOperation = (ClientSearchOperation)GWT.create(ClientSearchOperation.class);
		ClientSearchOperationView clientSearchOperationView = new ClientSearchOperationView();
		ClientSearchOperationViewPresenter clientSearchOperationPresenter = new ClientSearchOperationViewPresenter(null, null, clientSearchOperationView);
		clientSearchOperationPresenter.setOperation(clientSearchOperation);
		this.sectionOperationPresenters.put(ClientSearchOperation.ID, (OperationViewPresenter)clientSearchOperationPresenter);
		
		/* MERGE */
		ClientMergeOperation clientMergeOperation = (ClientMergeOperation)GWT.create(ClientMergeOperation.class);
		ClientMergeOperationView clientMergeOperationView = new ClientMergeOperationView();
		ClientMergeOperationViewPresenter clientMergeOperationPresenter = new ClientMergeOperationViewPresenter(null, null, clientMergeOperationView);
		clientMergeOperationPresenter.setOperation(clientMergeOperation);
		this.sectionOperationPresenters.put(ClientMergeOperation.ID, (OperationViewPresenter)clientMergeOperationPresenter);
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
			result[i] = (OperationViewPresenter)this.sectionOperationPresenters.values().toArray()[i];
		return result;
	}

	public void registerEventHandlers(EventBus eventBus) {
		for(ViewPresenter p : sectionOperationPresenters.values())
			p.setEventBus(eventBus);	
	}

}
