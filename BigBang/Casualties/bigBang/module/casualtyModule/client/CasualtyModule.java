package bigBang.module.casualtyModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.casualtyModule.client.dataAccess.CasualtyDataBrokerImpl;
import bigBang.module.casualtyModule.client.dataAccess.SubCasualtyDataBrokerImpl;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyCloseViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyDeleteViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyInfoOrDocumentRequestViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyManagerTransferViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyMassManagerTransferViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyOperationsViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySearchOperationViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyDeleteViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.SubCasualtyViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtyCloseView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtyDeleteView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtyInfoOrDocumentRequestView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtyManagerTransferView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtyMassManagerTransferView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtyOperationsView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtySearchOperationView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtySectionView;
import bigBang.module.casualtyModule.client.userInterface.view.SubCasualtyDeleteView;
import bigBang.module.casualtyModule.client.userInterface.view.SubCasualtyView;

import com.google.gwt.core.client.GWT;

public class CasualtyModule implements Module {

	private boolean initialized = false;

	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtySectionView casualtySectionView = (CasualtySectionView) GWT.create(CasualtySectionView.class);
				CasualtySectionViewPresenter casualtySectionViewPresenter = new CasualtySectionViewPresenter(casualtySectionView);
				return casualtySectionViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_OPERATIONS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtyOperationsView view = (CasualtyOperationsView) GWT.create(CasualtyOperationsView.class);
				ViewPresenter presenter = new CasualtyOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_SEARCH", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtySearchOperationView casualtySearchOperationView = (CasualtySearchOperationView) GWT.create(CasualtySearchOperationView.class);
				CasualtySearchOperationViewPresenter casualtySearchOperationViewPresenter = new CasualtySearchOperationViewPresenter(casualtySearchOperationView);
				return casualtySearchOperationViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_DELETE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtyDeleteView view = (CasualtyDeleteView) GWT.create(CasualtyDeleteView.class);
				CasualtyDeleteViewPresenter presenter = new CasualtyDeleteViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_CLOSE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtyCloseView view = (CasualtyCloseView) GWT.create(CasualtyCloseView.class);
				CasualtyCloseViewPresenter presenter = new CasualtyCloseViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SUB_CASUALTY_VIEW", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubCasualtyView view = (SubCasualtyView) GWT.create(SubCasualtyView.class);
				SubCasualtyViewPresenter presenter = new SubCasualtyViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("SUB_CASUALTY_DELETE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SubCasualtyDeleteView view = (SubCasualtyDeleteView) GWT.create(SubCasualtyDeleteView.class);
				ViewPresenter presenter = new SubCasualtyDeleteViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_MASS_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtyMassManagerTransferView view = (CasualtyMassManagerTransferView) GWT.create(CasualtyMassManagerTransferView.class);
				ViewPresenter presenter = new CasualtyMassManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtyManagerTransferView view = (CasualtyManagerTransferView) GWT.create(CasualtyManagerTransferView.class);
				ViewPresenter presenter = new CasualtyManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CASUALTY_CLIENT_INFO_OR_DOCUMENT_REQUEST", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CasualtyInfoOrDocumentRequestView view = (CasualtyInfoOrDocumentRequestView) GWT.create(CasualtyInfoOrDocumentRequestView.class);
				ViewPresenter presenter = new CasualtyInfoOrDocumentRequestViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new CasualtyDataBrokerImpl(),
				new SubCasualtyDataBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

}
