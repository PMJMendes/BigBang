package bigBang.module.clientModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.clientModule.client.dataAccess.ClientProcessBrokerImpl;
import bigBang.module.clientModule.client.userInterface.presenter.ClientInfoOrDocumentRequestViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMassManagerTransferViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientOperationsViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSectionViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.CreateInsurancePolicyViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.SingleClientManagerTransferViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ViewClientInfoRequestViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientInfoOrDocumentRequestView;
import bigBang.module.clientModule.client.userInterface.view.ClientMassManagerTransferView;
import bigBang.module.clientModule.client.userInterface.view.ClientMergeView;
import bigBang.module.clientModule.client.userInterface.view.ClientOperationsView;
import bigBang.module.clientModule.client.userInterface.view.ClientSearchOperationView;
import bigBang.module.clientModule.client.userInterface.view.ClientSectionView;
import bigBang.module.clientModule.client.userInterface.view.CreateInsurancePolicyView;
import bigBang.module.clientModule.client.userInterface.view.SingleClientManagerTransferView;
import bigBang.module.clientModule.client.userInterface.view.ViewClientInfoRequestView;

import com.google.gwt.core.client.GWT;

public class ClientModule implements Module {

	private boolean initialized = false;
	
	@Override
	public void initialize() {
		registerViewPresenters();
		initialized = true;
	}

	@Override
	public boolean isInitialized() {
		return this.initialized;
	}
	
	private void registerViewPresenters(){
		
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_SECTION", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				ClientSectionView clientSectionView = (ClientSectionView) GWT.create(ClientSectionView.class);
				ClientSectionViewPresenter clientSectionViewPresenter = new ClientSectionViewPresenter(clientSectionView);
				return clientSectionViewPresenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_OPERATIONS", new ViewPresenterInstantiator() {
			
			@Override
			public ViewPresenter getInstance() {
				ClientOperationsView view = (ClientOperationsView) GWT.create(ClientOperationsView.class);
				ViewPresenter presenter = new ClientOperationsViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_SEARCH", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ClientSearchOperationView view = (ClientSearchOperationView) GWT.create(ClientSearchOperationView.class);
				ViewPresenter presenter = new ClientSearchOperationViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_SINGLE_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				SingleClientManagerTransferView view = (SingleClientManagerTransferView) GWT.create(SingleClientManagerTransferView.class);
				ViewPresenter presenter = new SingleClientManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_MASS_MANAGER_TRANSFER", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ClientMassManagerTransferView view = (ClientMassManagerTransferView) GWT.create(ClientMassManagerTransferView.class);
				ViewPresenter presenter = new ClientMassManagerTransferViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_CREATE_INSURANCE_POLICY", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				CreateInsurancePolicyView view = (CreateInsurancePolicyView) GWT.create(CreateInsurancePolicyView.class);
				CreateInsurancePolicyViewPresenter presenter = new CreateInsurancePolicyViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_MERGE", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ClientMergeView view = (ClientMergeView) GWT.create(ClientMergeView.class);
				ViewPresenter presenter = new ClientMergeViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CLIENT_INFO_OR_DOCUMENT_REQUEST", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ClientInfoOrDocumentRequestView view = (ClientInfoOrDocumentRequestView) GWT.create(ClientInfoOrDocumentRequestView.class);
				ViewPresenter presenter = new ClientInfoOrDocumentRequestViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("VIEW_CLIENT_INFO_OR_DOCUMENT_REQUEST", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ViewClientInfoRequestView view = (ViewClientInfoRequestView) GWT.create(ViewClientInfoRequestView.class);
				ViewPresenter presenter = new ViewClientInfoRequestViewPresenter(view);
				return presenter;
			}
		});
	}
	
	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
			new ClientProcessBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
			BigBangConstants.EntityIds.CLIENT,
//			BigBangConstants.EntityIds.HISTORY,
//			BigBangConstants.EntityIds.USER,
//			BigBangConstants.EntityIds.COST_CENTER
		};
	}
}
