package bigBang.module.tasksModule.client.userInterface.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub.Status;
import bigBang.library.client.EventBus;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

public abstract class DismissTaskPresenter implements ViewPresenter{
	
	private Task dismissTask;
	private Display view;
	
	public interface Display{
		
		public void showTask(Task dismissTask);
		public HasClickHandlers getDismissButton();
		public void clear();
		Widget asWidget();
		public void lockView(boolean b);
	}
	

	public void setTask(Task task){
		dismissTask = task;
		if(task == null){
			
			this.view.clear();
			this.view.lockView(true);
			return;
			
		}
		else
			this.view.lockView(false);
		
	
		if(task.status != Status.COMPLETED){
			
			throw new RuntimeException("WRONG TASK STATUS");
			
		}
		
		view.showTask(task);
		
		
		
		
		
	}
	public void go(HasWidgets container){
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind(){
		this.view.getDismissButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				dismissTask(dismissTask);
				
			}
		});
		
		
	}
	
	public abstract void dismissTask(Task task);

	@Override
	public void setService(Service service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setView(View view) {
	
		this.view = (Display)view;
		
	}


	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub
		
	}

}
