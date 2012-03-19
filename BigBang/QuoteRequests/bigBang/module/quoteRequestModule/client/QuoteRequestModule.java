package bigBang.module.quoteRequestModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.quoteRequestModule.client.dataAccess.NegotiationBrokerImpl;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationDeleteViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationExternalRequestViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationGrantViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestOperationsViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSectionViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationDeleteView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationExternalRequestView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationGrantView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestOperationsView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestSearchOperationView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestSectionView;

import com.google.gwt.core.client.GWT;

public class QuoteRequestModule implements Module {

	private boolean initialized = true;

	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestSectionView quoteRequestSectionView = (QuoteRequestSectionView) GWT.create(QuoteRequestSectionView.class);
				QuoteRequestSectionViewPresenter quoteRequestSectionViewPresenter = new QuoteRequestSectionViewPresenter(quoteRequestSectionView);
				return quoteRequestSectionViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_OPERATIONS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestOperationsView view = (QuoteRequestOperationsView) GWT.create(QuoteRequestOperationsView.class);
				ViewPresenter presenter = new QuoteRequestOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_SEARCH", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestSearchOperationView quoteRequestSearchOperationView = (QuoteRequestSearchOperationView) GWT.create(QuoteRequestSearchOperationView.class);
				QuoteRequestSearchOperationViewPresenter quoteRequestSearchOperationViewPresenter = new QuoteRequestSearchOperationViewPresenter(quoteRequestSearchOperationView);
				return quoteRequestSearchOperationViewPresenter;
			}
		});
		
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_NEGOTIATION_DELETE", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				NegotiationDeleteView view = (NegotiationDeleteView) GWT.create(NegotiationDeleteView.class); 
				NegotiationDeleteViewPresenter presenter = new NegotiationDeleteViewPresenter(view);
				return presenter;
			}
		});
		

		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_NEGOTIATION_GRANT", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				NegotiationGrantView view = (NegotiationGrantView) GWT.create(NegotiationGrantView.class); 
				NegotiationGrantViewPresenter presenter = new NegotiationGrantViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		
		return new DataBroker<?>[]{
				new NegotiationBrokerImpl()
			};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.NEGOTIATION,
			};
	}

}
