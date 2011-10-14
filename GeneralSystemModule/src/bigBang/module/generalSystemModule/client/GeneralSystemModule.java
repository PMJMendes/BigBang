package bigBang.module.generalSystemModule.client;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.event.ModuleInitializedEvent;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.module.generalSystemModule.client.dataAccess.ClientGroupBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.CostCenterBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.CoverageBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.HistoryBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.InsuranceAgencyBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.MediatorBrokerImpl;
import bigBang.module.generalSystemModule.client.dataAccess.UserBrokerImpl;
import bigBang.module.generalSystemModule.client.userInterface.presenter.GeneralSystemSectionViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.view.GeneralSystemSectionView;
import bigBang.module.generalSystemModule.interfaces.GeneralSystemService;

public class GeneralSystemModule implements Module {

	private SectionViewPresenter[] sectionPresenters;
	private boolean initialized = false;
	public static String processId;

	@Override
	public void initialize(final EventBus eventBus, final BigBangPermissionManager permissionManager) {
		GeneralSystemService.Util.getInstance().getGeneralSystemProcessId(new BigBangAsyncCallback<String>() {

			@Override
			public void onSuccess(final String result) {
				processId = result;
				permissionManager.getProcessPermissionContext(result, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void result2) {
						setup(eventBus, permissionManager, result);
					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
		});
	}

	private void setup(EventBus eventBus, BigBangPermissionManager permissionManager, String processId) {
		sectionPresenters = new SectionViewPresenter[1];

		//GeneralSystem section
		GeneralSystemSection generalSystemSection = new GeneralSystemSection(permissionManager, processId);
		GeneralSystemSectionView generalSystemSectionView = new GeneralSystemSectionView();
		GeneralSystemSectionViewPresenter generalSystemSectionPresenter = new GeneralSystemSectionViewPresenter(eventBus, null, generalSystemSectionView);
		generalSystemSectionPresenter.setSection(generalSystemSection);
		generalSystemSection.registerEventHandlers(eventBus);
		sectionPresenters[0] = generalSystemSectionPresenter;
		this.initialized = true;
		eventBus.fireEvent(new ModuleInitializedEvent(this));
	}

	public void initialize(EventBus eventBus) {
		this.initialize(eventBus, null);
	}

	public boolean isInitialized() {
		return initialized;
	}

	public SectionViewPresenter[] getMainMenuSectionPresenters() {
		return sectionPresenters;
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker<?>[]{
				new CostCenterBrokerImpl(),
				new UserBrokerImpl(),
				new InsuranceAgencyBrokerImpl(),
				new ClientGroupBrokerImpl(),
				new MediatorBrokerImpl(),
				new CoverageBrokerImpl(),
				new HistoryBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.COST_CENTER,
				BigBangConstants.EntityIds.USER,
				BigBangConstants.EntityIds.INSURANCE_AGENCY,
				BigBangConstants.EntityIds.CLIENT_GROUP,
				BigBangConstants.EntityIds.MEDIATOR,
				BigBangConstants.EntityIds.COVERAGE,
				BigBangConstants.EntityIds.HISTORY
		};
	}
}
