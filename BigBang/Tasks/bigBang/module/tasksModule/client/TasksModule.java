package bigBang.module.tasksModule.client;

import com.google.gwt.core.client.GWT;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.event.LoginSuccessEventHandler;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.event.LogoutEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.tasksModule.client.dataAccess.TasksBrokerImpl;
import bigBang.module.tasksModule.client.userInterface.presenter.DismissTaskViewPresenter;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter;
import bigBang.module.tasksModule.client.userInterface.view.DismissTaskView;
import bigBang.module.tasksModule.client.userInterface.view.TasksSectionView;

public class TasksModule implements Module {

	private boolean initialized = false;
	private TasksNotificationsManager notificationsManager;

	@Override
	public void initialize() {
		bindToEvents();
		registerViewPresenters();
		this.notificationsManager = new TasksNotificationsManager();
		initialized = true;
	}

	private void bindToEvents(){
		EventBus.getInstance().addHandler(LoginSuccessEvent.TYPE, new LoginSuccessEventHandler() {

			@Override
			public void onLoginSuccess(LoginSuccessEvent event) {
//				if(!notificationsManager.isRunning()){
//					notificationsManager.run();
//				}
			}
		});
		EventBus.getInstance().addHandler(LogoutEvent.TYPE, new LogoutEventHandler(){

			@Override
			public void onLogout(LogoutEvent event) {
				notificationsManager.cancel();
			}
		});

		//		EventBus.getInstance().addHandler(NumberOfTasksUpdateEvent.TYPE, new NumberOfTasksUpdateEventHandler() {
		//
		//			public void onUpdate(NumberOfTasksUpdateEvent event) {
		//				badge.setText(event.getValue() + "");
		//			}
		//		});
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory factory = ViewPresenterFactory.getInstance();

		factory.registerViewPresenterInstantiator("TASKS_SECTION", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				TasksSectionView view = new TasksSectionView(); 
				TasksSectionViewPresenter presenter = new TasksSectionViewPresenter(view);
				return presenter;
			}
		});
		factory.registerViewPresenterInstantiator("TASKS_DISMISS", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				DismissTaskView view  = (DismissTaskView) GWT.create(DismissTaskView.class);
				ViewPresenter presenter = new DismissTaskViewPresenter(view);
				return presenter;
			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return new DataBroker[]{
				new TasksBrokerImpl()
		};
	}

	@Override
	public String[] getBrokerDependencies() {
		return new String[]{
				BigBangConstants.EntityIds.TASK
		};
	}


}
