package bigBang.module.tasksModule.client.userInterface.presenter;

import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub.Status;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

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
	
	public DismissTaskPresenter(Display view){
		setView((UIObject) view);
	}
	
	@Override
	public void setView(UIObject view) {
	
		this.view = (Display)view;
		
	}
	
	@Override
	public void go(HasWidgets container){
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	public void bind(){
		this.view.getDismissButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				dismissTask(dismissTask);
				
			}
		});
		
		//APPLICATION-WIDE EVENTS		
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
	
	public abstract void dismissTask(Task task);

}
