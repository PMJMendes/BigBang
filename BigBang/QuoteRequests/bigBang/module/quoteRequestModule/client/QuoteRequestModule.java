package bigBang.module.quoteRequestModule.client;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.quoteRequestModule.client.dataAccess.NegotiationBrokerImpl;
import bigBang.module.quoteRequestModule.client.dataAccess.QuoteRequestBrokerImpl;
import bigBang.module.quoteRequestModule.client.dataAccess.QuoteRequestInsuredObjectBrokerImpl;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationCancellationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationDeleteViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationGrantViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.NegotiationTasksViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestCloseViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestConversationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestDeleteViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestMassManagerTransferViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestOperationsViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestReceiveMessageViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSearchOperationViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSectionViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.QuoteRequestSendMessageViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.presenter.SingleQuoteRequestManagerTransferViewPresenter;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationCancellationView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationDeleteView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationGrantView;
import bigBang.module.quoteRequestModule.client.userInterface.view.NegotiationTasksView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestCloseView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestConversationView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestDeleteView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestMassManagerTransferView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestOperationsView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestReceiveMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestSearchOperationView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestSectionView;
import bigBang.module.quoteRequestModule.client.userInterface.view.QuoteRequestSendMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.view.SingleQuoteRequestManagerTransferView;

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
		
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_DELETE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestDeleteView view = (QuoteRequestDeleteView) GWT.create(QuoteRequestDeleteView.class);
				ViewPresenter presenter = new QuoteRequestDeleteViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_CLOSE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestCloseView view = (QuoteRequestCloseView) GWT.create(QuoteRequestCloseView.class);
				ViewPresenter presenter = new QuoteRequestCloseViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_MASS_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestMassManagerTransferView view = (QuoteRequestMassManagerTransferView) GWT.create(QuoteRequestMassManagerTransferView.class);
				QuoteRequestMassManagerTransferViewPresenter presenter = new QuoteRequestMassManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_SINGLE_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SingleQuoteRequestManagerTransferView view = (SingleQuoteRequestManagerTransferView) GWT.create(SingleQuoteRequestManagerTransferView.class);
				SingleQuoteRequestManagerTransferViewPresenter presenter = new SingleQuoteRequestManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("NEGOTIATION_CANCEL", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationCancellationView view = (NegotiationCancellationView) GWT.create(NegotiationCancellationView.class); 
				NegotiationCancellationViewPresenter presenter = new NegotiationCancellationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("NEGOTIATION_GRANT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationGrantView view = (NegotiationGrantView ) GWT.create(NegotiationGrantView.class);
				NegotiationGrantViewPresenter presenter = new NegotiationGrantViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("NEGOTIATION_TASKS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				NegotiationTasksView view = (NegotiationTasksView) GWT.create(NegotiationTasksView.class);
				ViewPresenter presenter = new NegotiationTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_SEND_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestSendMessageView view = (QuoteRequestSendMessageView) GWT.create(QuoteRequestSendMessageView.class);
				ViewPresenter presenter = new QuoteRequestSendMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_RECEIVE_MESSAGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestReceiveMessageView view = (QuoteRequestReceiveMessageView) GWT.create(QuoteRequestReceiveMessageView.class);
				ViewPresenter presenter = new QuoteRequestReceiveMessageViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("QUOTE_REQUEST_CONVERSATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				QuoteRequestConversationView view = (QuoteRequestConversationView) GWT.create(QuoteRequestConversationView.class);
				ViewPresenter presenter = new QuoteRequestConversationViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		QuoteRequestInsuredObjectBrokerImpl requestObjectsBroker = new QuoteRequestInsuredObjectBrokerImpl();
		
		return new DataBroker<?>[]{
				new QuoteRequestBrokerImpl(requestObjectsBroker),
				new NegotiationBrokerImpl(),
				requestObjectsBroker
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.NEGOTIATION,
		};
	}

}
