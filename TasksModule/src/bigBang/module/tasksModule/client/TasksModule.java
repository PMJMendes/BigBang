package bigBang.module.tasksModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.Module;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.tasksModule.client.dataAccess.TasksBrokerImpl;
import bigBang.module.tasksModule.client.userInterface.presenter.TasksSectionViewPresenter;
import bigBang.module.tasksModule.client.userInterface.view.TasksSectionView;

public class TasksModule implements Module {

	private boolean initialized = false;	

	@Override
	public void initialize() {
		//new TasksNotificationsManager().run(); //TODO IMPORTANT FJVC
		bindToEvents();
		registerViewPresenters();
		initialized = true;
	}

	private void bindToEvents(){
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
