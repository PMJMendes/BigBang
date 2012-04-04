package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Document;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.interfaces.DocuShareServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DocumentViewPresenter implements ViewPresenter, DocumentsBrokerClient{

	private Display view;
	private Document doc;
	private boolean bound = false;
	private DocumentsBroker broker;
	protected DocuShareServiceAsync docuShareservice;
	private int versionNumber;
	private String ownerId;
	private String documentId;
	
	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		ADD_NEW_DETAIL, 
		REMOVE_FILE, 
		DELETE, 
		UPLOAD_SUCCESS,
		DOWNLOAD_FILE, NEW_FILE_FROM_DISK, NEW_FILE_FROM_DOCUSHARE
	}

	public DocumentViewPresenter(Display view){

		broker = (DocumentsBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DOCUMENT);
		this.setView((UIObject) view);

	}

	public interface Display{

		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void setEditable(boolean b);
		void clear();
		HasEditableValue<Document> getForm();
		void lockToolbar(boolean b);

	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		bound = true;
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		doc = null;
		view.clear();
		broker.unregisterClient(this);
		ownerId = parameterHolder.getParameter("ownerid");
		documentId = parameterHolder.getParameter("documentid");
		parameterHolder.getParameter("ownertypeid");
		boolean hasPermissions = parameterHolder.getParameter("editpermission") != null;

		if(ownerId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar um documento sem cliente associado."), TYPE.ALERT_NOTIFICATION));
			view.setEditable(false);
			return;
		}
		else{
			broker.registerClient(this, ownerId);
		}	
		if(documentId == null){

			if(hasPermissions){
				view.getForm().setValue(null);
			}
			else{
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o documento."), TYPE.ALERT_NOTIFICATION));
				view.setEditable(false);
			}
		}
		else{
			broker.getDocument(ownerId, documentId, new ResponseHandler<Document>() {
				@Override
				public void onResponse(Document response) {
					doc = response;
					view.getForm().setValue(doc);
					view.setEditable(false);

				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.removeParameter("documentid");
					navig.removeParameter("ownertypeid");
					navig.removeParameter("show");
					NavigationHistoryManager.getInstance().go(navig);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o documento pedido."), TYPE.ALERT_NOTIFICATION));
				}
			});

		}

		if(!hasPermissions){
			view.lockToolbar(true);
		}

	}

	private void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){
				case CANCEL:{
					cancelChanges();
					break;
				}
				case EDIT:{
					view.setEditable(true);
					break;
				}
				case SAVE:{
					//TODO
					break;
				}
				case REMOVE_FILE:{
					removeFile();
					break;
				}
				case DELETE:{
					removeDocument();
					break;
				}

				case DOWNLOAD_FILE:{
					downloadFile();
					break;
				}

				case NEW_FILE_FROM_DISK:{
					//TODO

					break;
				}
				case NEW_FILE_FROM_DOCUSHARE:{

					//TODO

					break;
				}

				}
			}

		});
	}

	protected void removeFile() {
		// TODO Auto-generated method stub

	}

	private void downloadFile() {

		Window.open(GWT.getModuleBaseURL() + "bbfile?fileref=" + doc.fileStorageId , null, null);

	}

	private void removeDocument() {

		broker.deleteDocument(documentId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Documento eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o documento."), TYPE.ALERT_NOTIFICATION));
				view.setEditable(true);
			}


		});

	}

	private void createUpdateDocument(Document temp) {



		if(temp.id == null){
			//TODO
		}

		else
		{
			//TODO
		}

	}

	protected void clearResources() {

		broker.closeDocumentResource(ownerId, documentId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				GWT.log("Documento com ID: " + documentId + " deu erro ao cancelar alterações.");				
			}
		});

	}

	private void cancelChanges(){

		clearResources();

		if(doc == null){
			NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
			navig.removeParameter("documentid");
			navig.removeParameter("ownertypeid");
			navig.removeParameter("show");
			NavigationHistoryManager.getInstance().go(navig);
			return;
		}

		NavigationHistoryManager.getInstance().reload();

	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {

		versionNumber = number;

	}

	@Override
	public int getDataVersion(String dataElementId) {

		return versionNumber;	
	}

	@Override
	public int getDocumentsDataVersionNumber(String ownerId) {

		return versionNumber;
	}

	@Override
	public void setDocumentsDataVersionNumber(String ownerId, int number) {

		versionNumber = number;

	}

	@Override
	public void setDocuments(String ownerId, java.util.List<Document> documents) {

		return;

	}

	@Override
	public void removeDocument(String ownerId, Document document) {

		NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
		navig.removeParameter("documentid");
		navig.removeParameter("ownertypeid");
		navig.removeParameter("show");
		NavigationHistoryManager.getInstance().go(navig);

	}

	@Override
	public void addDocument(String ownerId, Document document) {

	}

	@Override
	public void updateDocument(String ownerId, Document document) {

		return;
	}


}
