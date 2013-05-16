package bigBang.module.tasksModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Document;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.HasNavigationStateChangedHandlers;
import bigBang.library.client.event.NavigationStateChangedEvent;
import bigBang.library.client.event.NavigationStateChangedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.DocuShareItem;
import bigBang.module.tasksModule.client.userInterface.DocumentImagePanel;
import bigBang.module.tasksModule.client.userInterface.view.MailOrganizerView;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class MailOrganizerViewPresenter implements ViewPresenter{

	public enum Action {
		CONFIRM, CANCEL

	}

	public interface Display {

		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		void clear();

		DocumentImagePanel getPanel();

		HasEditableValue<Document> getForm();

		DocuShareItem getSelectedDocushareItem();

		String getDocuShareHandle();

		String getDirectoryHandle();

		void panelNavigateBack();

		void setReadOnly(boolean b);

		HasNavigationStateChangedHandlers getNavigationPanel();

		void clearNavigationPanel();

	}

	private Display view;
	private boolean bound = false;
	private DocumentsBroker broker;

	public MailOrganizerViewPresenter(MailOrganizerView view) {
		broker = (DocumentsBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DOCUMENT);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<MailOrganizerViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case CANCEL:
					onCancel();
					break;
				case CONFIRM:
					onSaveDocument();
					break;
				}
			}

		});

		view.getNavigationPanel().registerNavigationStateChangedHandler(new NavigationStateChangedEventHandler() {

			@Override
			public void onNavigationStateChanged(NavigationStateChangedEvent event) {
				if(event.getObject() instanceof ImageHandlerPanel){
					view.setReadOnly(false);
					view.getForm().validate();
				}
				else{
					view.clear();
					view.setReadOnly(true);
				}
			}
		});

		bound = true;
	}

	protected void onSaveDocument() {

		if(view.getForm().validate()){
			Document doc = view.getForm().getInfo();
			final DocuShareHandle handle = new DocuShareHandle();
			handle.handle = view.getDocuShareHandle();
			handle.locationHandle = view.getDirectoryHandle();
			doc.source = handle;
			broker.createDocumentSerial(doc, new ResponseHandler<Document>() {

				@Override
				public void onResponse(Document response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Documento criado com sucesso."), TYPE.TRAY_NOTIFICATION));					
					view.getPanel().removeSelected(handle.handle);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o documento."), TYPE.ALERT_NOTIFICATION));					
					reSet();				
				}
			});
		}		
	}

	protected void reSet() {
		view.clear();
		view.setReadOnly(true);
	}

	protected void onCancel() {
		reSet();
		view.panelNavigateBack();
		}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.clear();
		view.clearNavigationPanel();
		view.setReadOnly(true);
	}

}
