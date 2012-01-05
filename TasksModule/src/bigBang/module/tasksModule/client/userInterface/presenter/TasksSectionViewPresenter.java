package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.Collection;
import java.util.HashMap;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.event.OperationWasExecutedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenterManager;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.tasksModule.client.TasksSection;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class TasksSectionViewPresenter implements SectionViewPresenter, ViewPresenterManager {

	public interface Display {
		HasValueSelectables<TaskStub> getTaskList();
		void presentTaskScreen(Task task, ViewPresenter presenter);

		HasWidgets getOperationViewContainer();
		Widget asWidget();
		void showTaskDone();
		
		void clear();
	}

	private TasksSection tasksSection;

	private EventBus eventBus;
	private Display view;
	protected TasksBroker broker;
	protected int taskDataVersion;
	protected Task currentTask;

	private HashMap <String, ViewPresenter> viewPresenterCache;

	public TasksSectionViewPresenter(EventBus eventBus, Service tasksService, View view) {
		viewPresenterCache = new HashMap<String, ViewPresenter>();
		setEventBus(eventBus);
		setService(tasksService);
		setView(view);
		broker = (TasksBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.TASK);
	}

	public MenuSection getSection() {
		return this.tasksSection;
	}

	public void setSection(MenuSection section) {
		this.tasksSection = (TasksSection)section;
		this.tasksSection.registerEventHandlers(eventBus);
	}

	public void setService(Service service) {
		return;
	}

	public void setEventBus(EventBus eventBus) {
		if(eventBus == null)
			return;
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	public void bind() {
		this.view.getTaskList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<TaskStub> selected = (ValueSelectable<TaskStub>) event.getFirstSelected();
				TaskStub selectedValue = selected == null ? null : selected.getValue();
				if(selectedValue == null){
					view.clear();
					currentTask = null;
				}else{
					if(selectedValue.id != null){
						broker.getTask(selectedValue.id, new ResponseHandler<Task>(){

							@Override
							public void onResponse(Task response) {
								showScreenForTask(response);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {}

						});
					}
				}
			}
		});
	}

	protected void showScreenForTask(Task task) {
		//eventBus.fireEvent(new ScreeninOperationInvokedEvent(((Task)event.getValue()).operationId, ((Task)event.getValue()).operationInstanceId, presenterManager));
		//ViewPresenter presenter = new UndoOperationViewPresenter(eventBus, null, null, "");
		//view.presentTaskScreen(task, presenter);
		this.currentTask = task;
	}

	public void registerEventHandlers(EventBus eventBus) {
		eventBus.addHandler(OperationWasExecutedEvent.TYPE, new OperationWasExecutedEventHandler() {
			
			@Override
			public void onOperationWasExecuted(String operationId, String processId) {
				if(currentTask != null){
					//TODO CHECKS
					broker.getTask(currentTask.id, new ResponseHandler<Task>() {

						@Override
						public void onResponse(Task response) {
						}

						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
				}
			}
		});
	}

	public void managePresenter(String presenterId, ViewPresenter presenter) {
		this.viewPresenterCache.put(presenterId, presenter);
		((OperationViewPresenter)presenter).goCompact(this.view.getOperationViewContainer());
		//((OperationViewPresenter)presenter).setTargetEntity(((Task)this.view.getTaskList().getValue()).targetId);
	}

}
