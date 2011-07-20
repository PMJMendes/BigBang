package bigBang.module.quoteRequestModule.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.MenuSections;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.TextBadge;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestSearchOperationView;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestServiceAsync;
import bigBang.module.quoteRequestModule.shared.ModuleConstants;
import bigBang.module.quoteRequestModule.shared.operation.QuoteRequestSearchOperation;

public class QuoteRequestSection implements MenuSection {

	private static final String ID = ModuleConstants.ProcessTypeIDs.QUOTE_REQUEST;
	private static final String DESCRIPTION = "Consulta de Mercado";
	private static final String SHORT_DESCRIPTION = "C. Mercado";
	private ArrayList<OperationViewPresenter> sectionOperationPresenters;
	public BigBangPermissionManager permissionManager;

	public QuoteRequestSection(BigBangPermissionManager permissionManager){
		this.sectionOperationPresenters = new ArrayList<OperationViewPresenter>();
		this.permissionManager = permissionManager;

		/* QUOTE REQUEST */
		QuoteRequestSearchOperation quoteRequestSearchOperation = (QuoteRequestSearchOperation)GWT.create(QuoteRequestSearchOperation.class);
		QuoteRequestSearchOperationView quoteRequestSearchOperationView = (QuoteRequestSearchOperationView) GWT.create(QuoteRequestSearchOperationView.class);
		QuoteRequestServiceAsync quoteRequestService = QuoteRequestService.Util.getInstance();
		QuoteRequestSearchOperationViewPresenter quoteRequestSearchOperationViewPresenter = new QuoteRequestSearchOperationViewPresenter(null, quoteRequestService, quoteRequestSearchOperationView);
		quoteRequestSearchOperationViewPresenter.setOperation((Operation) quoteRequestSearchOperation);
		this.sectionOperationPresenters.add((OperationViewPresenter)quoteRequestSearchOperationViewPresenter);
	}

	@Override
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
		return null;
	}

	public boolean hasBadge() {
		return false;
	}

	public OperationViewPresenter[] getOperationPresenters(){
		OperationViewPresenter[] result = new OperationViewPresenter[this.sectionOperationPresenters.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = this.sectionOperationPresenters.get(i);
		}
		return result;
	}

	public void registerEventHandlers(EventBus eventBus) {
		for(ViewPresenter p : this.getOperationPresenters()) {
			p.setEventBus(eventBus);
		}
	}

	@Override
	public MenuSections getMenuIndex() {
		return MenuSections.QUOTE_REQUEST_SECTION;
	}


}
