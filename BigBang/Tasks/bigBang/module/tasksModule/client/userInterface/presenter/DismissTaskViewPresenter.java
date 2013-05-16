package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub.Status;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.tasksModule.client.dataAccess.TasksBroker;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DismissTaskViewPresenter implements ViewPresenter{

	private String dismissTaskId;
	private Display view;
	private boolean bound = false;

	public interface Display{
		HasEditableValue<Task> getForm(); 
		public HasClickHandlers getDismissButton();
		Widget asWidget();
	}

	private TasksBroker broker;
	
	public DismissTaskViewPresenter(Display view){
		this.broker = (TasksBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.TASK);
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
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.dismissTaskId = parameterHolder.getParameter("taskid");
		this.dismissTaskId = this.dismissTaskId == null ? new String() : this.dismissTaskId;
		
		if(this.dismissTaskId.isEmpty()){
			clearView();
		}else{
			showDismiss(this.dismissTaskId);
		}
	}
	
	private void bind(){
		if(bound){return;}
		this.view.getDismissButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dismissTask(DismissTaskViewPresenter.this.dismissTaskId);
			}
		});

		//APPLICATION-WIDE EVENTS
		
		this.bound = true;
	}
	
	private void clearView(){
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
	}

	private void showDismiss(String taskId){
		this.broker.getTask(taskId, new ResponseHandler<Task>() {
			
			@Override
			public void onResponse(Task response) {
				if(response.status != Status.COMPLETED){
					onDismissTaskFailed();
				}else{
					view.getForm().setValue(response);
				}
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetTaskFailed();
			}
		});
	}

	private void dismissTask(String taskId){
		broker.dismissTask(taskId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				DismissTaskViewPresenter.this.dismissTaskId = null;
				onDismissTaskSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onDismissTaskFailed();
			}
		});
	}
	
	private void onDismissTaskSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Tomada de Conhecimento foi notificada"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("taskid");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onGetTaskFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar a notificação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("taskid");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onDismissTaskFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível notificar o sistema da Tomada de Conhecimento"), TYPE.ALERT_NOTIFICATION));
	}
	
}
