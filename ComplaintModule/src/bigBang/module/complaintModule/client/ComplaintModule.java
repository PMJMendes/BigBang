package bigBang.module.complaintModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.complaintModule.client.userInterface.presenter.ComplaintOperationsViewPresenter;
import bigBang.module.complaintModule.client.userInterface.presenter.ComplaintSearchOperationViewPresenter;
import bigBang.module.complaintModule.client.userInterface.presenter.ComplaintSectionViewPresenter;
import bigBang.module.complaintModule.client.userInterface.view.ComplaintOperationsView;
import bigBang.module.complaintModule.client.userInterface.view.ComplaintSearchOperationView;
import bigBang.module.complaintModule.client.userInterface.view.ComplaintSectionView;

import com.google.gwt.core.client.GWT;

public class ComplaintModule implements Module {

	private boolean initialized = false;

	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("COMPLAINT_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ComplaintSectionView complaintSectionView = (ComplaintSectionView) GWT.create(ComplaintSectionView.class);
				ComplaintSectionViewPresenter complaintSectionViewPresenter = new ComplaintSectionViewPresenter(complaintSectionView);
				return complaintSectionViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("COMPLAINT_OPERATIONS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ComplaintOperationsView view = (ComplaintOperationsView) GWT.create(ComplaintOperationsView.class);
				ComplaintOperationsViewPresenter presenter = new ComplaintOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("COMPLAINT_SEARCH", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ComplaintSearchOperationView complaintSearchOperationView = (ComplaintSearchOperationView) GWT.create(ComplaintSearchOperationView.class);
				ComplaintSearchOperationViewPresenter complaintSearchOperationViewPresenter = new ComplaintSearchOperationViewPresenter(complaintSearchOperationView);
				return complaintSearchOperationViewPresenter;			
			}
		});
	}

	public boolean isInitialized() {
		return initialized;
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
