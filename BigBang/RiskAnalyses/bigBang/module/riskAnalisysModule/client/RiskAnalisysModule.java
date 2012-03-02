package bigBang.module.riskAnalisysModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysOperationsViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysSearchOperationViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.presenter.RiskAnalisysSectionViewPresenter;
import bigBang.module.riskAnalisysModule.client.userInterface.view.RiskAnalisysOperationsView;
import bigBang.module.riskAnalisysModule.client.userInterface.view.RiskAnalisysSearchOperationView;
import bigBang.module.riskAnalisysModule.client.userInterface.view.RiskAnalisysSectionView;

import com.google.gwt.core.client.GWT;

public class RiskAnalisysModule implements Module {

	private boolean initialized = false;

	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RISK_ANALISYS_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				RiskAnalisysSectionView view = (RiskAnalisysSectionView) GWT.create(RiskAnalisysSectionView.class);
				RiskAnalisysSectionViewPresenter presenter = new RiskAnalisysSectionViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RISK_ANALISYS_OPERATIONS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				RiskAnalisysOperationsView view = (RiskAnalisysOperationsView) GWT.create(RiskAnalisysOperationsView.class);
				ViewPresenter presenter = new RiskAnalisysOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("RISK_ANALISYS_SEARCH_OPERATION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				RiskAnalisysSearchOperationView view = (RiskAnalisysSearchOperationView) GWT.create(RiskAnalisysSearchOperationView.class);
				ViewPresenter presenter = new RiskAnalisysSearchOperationViewPresenter(view);
				return presenter;
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
