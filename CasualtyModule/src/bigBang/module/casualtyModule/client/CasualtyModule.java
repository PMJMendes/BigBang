package bigBang.module.casualtyModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtyOperationsViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySearchOperationViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySectionViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtyOperationsView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtySearchOperationView;
import bigBang.module.casualtyModule.client.userInterface.view.CasualtySectionView;

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
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getBrokerDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

}
