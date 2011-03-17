package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Service;
import bigBang.library.shared.event.OperationInvokedEvent;
import bigBang.library.shared.userInterface.MenuSection;
import bigBang.library.shared.userInterface.presenter.OperationViewPresenter;
import bigBang.library.shared.userInterface.presenter.SectionViewPresenter;
import bigBang.library.shared.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.userInterface.presenter.ViewPresenterManager;
import bigBang.library.shared.userInterface.view.View;
import bigBang.module.tasksModule.client.TasksSection;
import bigBang.module.tasksModule.shared.Task;
import bigBang.module.tasksModule.shared.TasksServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class TasksSectionViewPresenter implements SectionViewPresenter, ViewPresenterManager {

	public interface Display {
		void setListEntries(ArrayList<Task> tasks);
		HasValue<Task> getTaskList();

		HasWidgets getOperationViewContainer();
		Widget asWidget();
	}
	
	private TasksSection tasksSection;
	
	private EventBus eventBus;
	private TasksServiceAsync service;
	private Display view;

	private HashMap <String, ViewPresenter> viewPresenterCache;

	public TasksSectionViewPresenter(EventBus eventBus, Service tasksService, View view) {
		viewPresenterCache = new HashMap<String, ViewPresenter>();
		setEventBus(eventBus);
		setService(tasksService);
		setView(view);
	}
	
	public MenuSection getSection() {
		return this.tasksSection;
	}

	public void setSection(MenuSection section) {
		this.tasksSection = (TasksSection)section;
		this.tasksSection.registerEventHandlers(eventBus);
	}
	
	public void setService(Service service) {
		this.service = (TasksServiceAsync) service;
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
		refreshList();
	}
	
	public void refreshList() {
		this.service.getTasks(new AsyncCallback<Task[]>() {

			public void onFailure(Throwable caught) {
				GWT.log("Failure while fetching the list of tasks");
			}

			public void onSuccess(Task[] result) {
				view.setListEntries(new ArrayList<Task>(Arrays.asList(result)));
			}
		});
	}

	public void bind() {
		final ViewPresenterManager presenterManager = this;

		this.view.getTaskList().addValueChangeHandler(new ValueChangeHandler<Task>() {
			
			public void onValueChange(ValueChangeEvent<Task> event) {
				if(viewPresenterCache.containsKey(((Task)event.getValue()).operationId)){
					((OperationViewPresenter)viewPresenterCache.get(((Task)event.getValue()).operationId)).goCompact(view.getOperationViewContainer());
				}else
					eventBus.fireEvent(new OperationInvokedEvent(((Task)event.getValue()).operationId, ((Task)event.getValue()).operationInstanceId, presenterManager));
			}
		});
	}

	public void registerEventHandlers(EventBus eventBus) {
	//	eventBus.addHandler(type, handler)
	}

	public void managePresenter(String presenterId, ViewPresenter presenter) {
		this.viewPresenterCache.put(presenterId, presenter);
		((OperationViewPresenter)presenter).goCompact(this.view.getOperationViewContainer());
		((OperationViewPresenter)presenter).setTargetEntity(((Task)this.view.getTaskList().getValue()).targetId);
	}

}
