package bigBang.client;

import bigBang.client.tests.TestsView;
import bigBang.client.tests.TestsViewPresenter;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.BigBangDocumentsBroker;
import bigBang.library.client.dataAccess.ConversationBrokerImpl;
import bigBang.library.client.dataAccess.HistoryBrokerImpl;
import bigBang.library.client.dataAccess.SubProcessesBrokerImpl;
import bigBang.library.client.dataAccess.TypifiedTextBrokerImpl;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.CancelInfoOrDocumentRequestViewPresenter;
import bigBang.library.client.userInterface.presenter.ContactNavigationViewPresenter;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter;
import bigBang.library.client.userInterface.presenter.ConversationTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalInfoRequestTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.ConversationClosingViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalRequestContinuationViewPresenter;
import bigBang.library.client.userInterface.presenter.ExternalRequestReplyViewPresenter;
import bigBang.library.client.userInterface.presenter.HistoryViewPresenter;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestReplyViewPresenter;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.ReportTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.ReportViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.CancelInfoOrDocumentRequestView;
import bigBang.library.client.userInterface.view.ContactNavigationView;
import bigBang.library.client.userInterface.view.ContactView;
import bigBang.library.client.userInterface.view.ConversationTasksView;
import bigBang.library.client.userInterface.view.DocumentView;
import bigBang.library.client.userInterface.view.ExternalInfoRequestTasksView;
import bigBang.library.client.userInterface.view.ConversationClosingView;
import bigBang.library.client.userInterface.view.ExternalRequestContinuationView;
import bigBang.library.client.userInterface.view.ExternalRequestReplyView;
import bigBang.library.client.userInterface.view.HistoryView;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestReplyView;
import bigBang.library.client.userInterface.view.ManagerTransferWithToolbarView;
import bigBang.library.client.userInterface.view.ReportTasksView;
import bigBang.library.client.userInterface.view.ReportView;
import com.google.gwt.core.client.GWT;

public class BigBangModule implements Module {

	private boolean initialized = false;

	@Override
	public void initialize() {
		NavigationHistoryManager.Util.getInstance().setProcessMapper(new BigBangProcessNavigationMapper());
		registerViewPresenters();
		initialized = true;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("TESTS_SECTION", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				TestsView view = (TestsView) GWT.create(TestsView.class);
				ViewPresenter presenter = new TestsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ManagerTransferWithToolbarView view = (ManagerTransferWithToolbarView) GWT.create(ManagerTransferWithToolbarView.class);
				ViewPresenter presenter = new ManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("HISTORY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				HistoryView view = (HistoryView) GWT.create(HistoryView.class);
				HistoryViewPresenter presenter = new HistoryViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("DOCUMENT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				DocumentView view = (DocumentView) GWT.create(DocumentView.class);
				ViewPresenter presenter = new DocumentViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SINGLE_CONTACT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ContactView view = (ContactView) GWT.create(ContactView.class);
				ViewPresenter presenter = new ContactViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CONTACT", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ContactNavigationView view = (ContactNavigationView) GWT.create(ContactNavigationView.class);
				ViewPresenter presenter = new ContactNavigationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INFO_OR_DOCUMENT_REQUEST_CANCELLATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CancelInfoOrDocumentRequestView view = (CancelInfoOrDocumentRequestView) GWT.create(CancelInfoOrDocumentRequestView.class);
				ViewPresenter presenter = new CancelInfoOrDocumentRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("INFO_OR_DOCUMENT_REQUEST_REPLY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				InfoOrDocumentRequestReplyView view = (InfoOrDocumentRequestReplyView) GWT.create(InfoOrDocumentRequestReplyView.class);
				ViewPresenter presenter = new InfoOrDocumentRequestReplyViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_REPLY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExternalRequestReplyView view = (ExternalRequestReplyView) GWT.create(ExternalRequestReplyView.class);
				ViewPresenter presenter = new ExternalRequestReplyViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_CONTINUATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExternalRequestContinuationView view = (ExternalRequestContinuationView) GWT.create(ExternalRequestContinuationView.class);
				ViewPresenter presenter = new ExternalRequestContinuationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CONVERSATION_CLOSE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ConversationClosingView view = (ConversationClosingView) GWT.create(ConversationClosingView.class);
				ViewPresenter presenter = new ConversationClosingViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("REPORTS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReportView view = (ReportView) GWT.create(ReportView.class);
				ReportViewPresenter presenter = new ReportViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("EXTERNAL_INFO_REQUEST_TASKS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ExternalInfoRequestTasksView view = (ExternalInfoRequestTasksView) GWT.create(ExternalInfoRequestTasksView.class);
				ViewPresenter presenter = new ExternalInfoRequestTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("REPORT_TASKS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ReportTasksView view = (ReportTasksView) GWT.create(ReportTasksView.class);
				ReportTasksViewPresenter presenter = new ReportTasksViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("TASKS_CONVERSATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ConversationTasksView view = (ConversationTasksView) GWT.create(ConversationTasksView.class);
				ConversationTasksViewPresenter presenter = new ConversationTasksViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new HistoryBrokerImpl()	,
				(DataBroker<?>) BigBangContactsListBroker.Util.getInstance(),
				(DataBroker<?>) BigBangDocumentsBroker.Util.getInstance(),
				new SubProcessesBrokerImpl(),
				new TypifiedTextBrokerImpl(),
				new ConversationBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return null;
	}

}
