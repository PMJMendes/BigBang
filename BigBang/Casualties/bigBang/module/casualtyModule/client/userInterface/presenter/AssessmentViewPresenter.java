package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.AssessmentBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.SubProcessesBroker;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class AssessmentViewPresenter implements ViewPresenter{

	public enum Action{
		SAVE,
		CANCEL,
		SEND_MESSAGE,
		RECEIVE_MESSAGE,
		CLOSE, EDIT, BACK
	}

	public interface Display{
		HasEditableValue<Assessment> getForm();
		HasValue<SubCasualty> getOwnerForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		void allowEdit(boolean b);
		void setSaveMode(boolean b);
		void lockToolbar();
		void allowSendMessage(boolean hasPermission);
		void allowReceiveMessage(boolean hasPermission);
		void allowClose(boolean hasPermission);
		void clear();
		void setOwner(String id);
		HasValueSelectables<BigBangProcess> getSubProcessList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
	}

	private Display view;
	private boolean bound = false;
	private SubCasualtyDataBroker subCasualtyBroker;
	private AssessmentBroker broker;
	private String assessmentId;
	private String subCasualtyId;
	protected Assessment assessment;

	public AssessmentViewPresenter(Display view) {
		this.subCasualtyBroker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		this.broker = (AssessmentBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.ASSESSMENT);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	private void bind() {
		if(bound){
			return;
		}
		view.registerActionHandler(new ActionInvokedEventHandler<AssessmentViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CLOSE:
					onClose();
					break;
				case RECEIVE_MESSAGE:
					onReceiveMessage();
					break;
				case SAVE:
					if(view.getForm().validate()){
						onSave();
					}else{
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem problemas no preenchimento do formulário."), TYPE.ERROR_TRAY_NOTIFICATION));
					}
					break;
				case SEND_MESSAGE:
					onSendMessage();
					break;
				case EDIT:
					onEdit();
					break;
				case BACK:
					onNavigateBack();
					break;
				}
			}
		});
		
		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selectable = (ValueSelectable<?>) event.getFirstSelected();

				if(selectable != null) {

					if(event.getSource() == view.getHistoryList()){ //HISTORY
						showHistory(((HistoryItemStub) selectable.getValue()).id);
					} else if(event.getSource() == view.getSubProcessList()){ //SUB PROCESSES
						showSubProcess(((BigBangProcess) selectable.getValue()).dataId);
					}
				}
			}
		};
		
		view.getSubProcessList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);
		
		bound = true;
	}

	protected void showSubProcess(String dataId) {
		SubProcessesBroker subProcessBroker = (SubProcessesBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.PROCESS);

		subProcessBroker.getSubProcess(dataId, new ResponseHandler<BigBangProcess>() {

			@Override
			public void onResponse(BigBangProcess response) {
				String type = response.dataTypeId;
				if(type.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.pushIntoStackParameter("display", "assessmentconversation");
					item.setParameter("conversationid", response.dataId);
					NavigationHistoryManager.getInstance().go(item);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				view.getSubProcessList().clearSelection();
			}
		});
	}

	protected void showHistory(String id) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", view.getForm().getValue().id);
		navItem.setParameter("historyItemId", id);
		NavigationHistoryManager.getInstance().go(navItem);		
	}

	protected void onEdit() {
		view.getForm().setReadOnly(false);
	}

	protected void onSendMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "assessmentsendmessage");
		NavigationHistoryManager.getInstance().go(item);		
	}

	protected void onSave() {
		if(assessmentId.equalsIgnoreCase("new")){
			subCasualtyBroker.createAssessment(view.getForm().getInfo(), new ResponseHandler<Assessment>() {

				@Override
				public void onResponse(Assessment response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Peritagem ou Averiguação criada com sucesso."), TYPE.TRAY_NOTIFICATION));
					assessment = response;
					assessmentId = response.id;
					getAssessment();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a Peritagem ou Averiguação."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
		else{
			broker.editAssessment(view.getForm().getInfo(), new ResponseHandler<Assessment>() {

				@Override
				public void onResponse(Assessment response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Alterações gravadas com sucesso."), TYPE.TRAY_NOTIFICATION));
					assessment = response;
					assessmentId = response.id;
					getAssessment();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar as alterações."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
	}

	protected void onReceiveMessage() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "assessmentreceivemessage");
		item.setParameter("ownerid", item.getParameter("assessmentid"));
		NavigationHistoryManager.getInstance().go(item);				
	}

	protected void onClose() {
		broker.closeProcess(assessmentId, new ResponseHandler<Assessment>() {
			
			@Override
			public void onResponse(Assessment response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Processo fechado com sucesso."), TYPE.TRAY_NOTIFICATION));
				assessment = response;
				getAssessment();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível fechar o processo."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	protected void onCancel() {
		if(assessmentId.equalsIgnoreCase("new")){
			onNavigateBack();
		}else{
			view.getForm().setReadOnly(true);
			view.getForm().revert();
		}
	}

	private void onNavigateBack() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("assessmentid");
		NavigationHistoryManager.getInstance().go(item);		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		assessmentId = parameterHolder.getParameter("assessmentid");
		subCasualtyId = parameterHolder.getParameter("subcasualtyId");
		view.clear();
		
		if(subCasualtyId != null){
			subCasualtyBroker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

				@Override
				public void onResponse(SubCasualty response) {
					view.getOwnerForm().setValue(response);
					getAssessment();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetOwnerFailed();
				}
			});
		}
		else{
			onGetOwnerFailed();
		}
	}

	protected void getAssessment() {
		if(assessmentId.equalsIgnoreCase("new")){
			view.lockToolbar();
			view.setSaveMode(true);
			view.allowEdit(true);
			view.getForm().setReadOnly(false);
			Assessment ass = new Assessment();
			ass.subCasualtyId = subCasualtyId;
			view.getForm().setValue(ass);
			
		}else{
			broker.getAssessment(assessmentId, new ResponseHandler<Assessment>() {

				@Override
				public void onResponse(Assessment response) {
					assessment = response;
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					view.setSaveMode(false);
					view.setOwner(response.id);
					setPermissions();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Peritagem ou Averiguação."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
	}

	protected void setPermissions() {
		view.lockToolbar();
		view.allowEdit(PermissionChecker.hasPermission(assessment, BigBangConstants.OperationIds.AssessmentProcess.EDIT));
		view.allowSendMessage(PermissionChecker.hasPermission(assessment, BigBangConstants.OperationIds.AssessmentProcess.CONVERSATION));
		view.allowReceiveMessage(PermissionChecker.hasPermission(assessment, BigBangConstants.OperationIds.AssessmentProcess.CONVERSATION));
		view.allowClose(PermissionChecker.hasPermission(assessment, BigBangConstants.OperationIds.AssessmentProcess.CLOSE));
	}

	private void onGetOwnerFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o sub-sinistro."), TYPE.ALERT_NOTIFICATION));		
	}

}
