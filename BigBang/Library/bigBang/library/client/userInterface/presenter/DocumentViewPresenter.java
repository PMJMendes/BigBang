package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ScanHandle;
import bigBang.definitions.shared.Document;
import bigBang.library.client.BigBangAsyncCallback;
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
import bigBang.library.interfaces.ScanItemService;
import bigBang.library.interfaces.ScanItemServiceAsync;
import bigBang.library.interfaces.MailService;
import bigBang.library.shared.ScanItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DocumentViewPresenter implements ViewPresenter, DocumentsBrokerClient{

	private Display view;
	private boolean bound = false;
	private DocumentsBroker broker;
	protected ScanItemServiceAsync scanItemService;
	private int versionNumber;
	private String ownerId;
	private String documentId;
	private boolean newDocument;
	private String ownerTypeId;
	private String emailId;
	private String attId;
	protected boolean allow = true;

	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		ADD_NEW_DETAIL, 
		DELETE, 
		UPLOAD_SUCCESS,
		DOWNLOAD_FILE,
		NEW_FILE_FROM_DISK,
		NEW_FILE_FROM_DOCUSHARE
	}

	public DocumentViewPresenter(Display view){

		broker = (DocumentsBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DOCUMENT);
		scanItemService = ScanItemService.Util.getInstance();
		this.setView((UIObject) view);

	}

	public interface Display{

		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void setEditable(boolean b);
		void clear();
		HasEditableValue<Document> getForm();
		void lockToolbar(boolean b);
		String getCurrentFileStorageId();
		String getFileInfo();
		void hasFile(boolean b);
		void setFilename(String string);
		void setFileStorageId(String string);
		ScanHandle getScanHandle();
		String getLocationHandle();
		void setMimeType(String mimeType);
		void setToolBarSaveMode(boolean b);
		ScanItem getScanItem();

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

		view.clear();
		view.setToolBarSaveMode(false);
		broker.unregisterClient(this);
		ownerId = parameterHolder.getParameter("ownerid");
		documentId = parameterHolder.getParameter("documentid");
		ownerTypeId = parameterHolder.getParameter("ownertypeid");
		emailId = parameterHolder.getParameter("emailId");
		attId = parameterHolder.getParameter("attId");
		boolean hasPermissions = true; //TODO PERMISSIONS

		if(ownerId == null){
//			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar um documento sem pasta associada."), TYPE.ALERT_NOTIFICATION));
			view.setEditable(false);
//			return;
		}
		else{
			broker.registerClient(this, ownerId);
		}

		if ( documentId == null ) {
			MailService.Util.getInstance().getAttAsDocFromStorage(emailId, attId, new BigBangAsyncCallback<Document>() {
				@Override
				public void onResponseSuccess(Document response) {
					view.getForm().setValue(response);
					view.setEditable(false);
					view.lockToolbar(true);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.removeParameter("documentid");
					navig.removeParameter("ownertypeid");
					navig.removeParameter("show");
					NavigationHistoryManager.getInstance().go(navig);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o anexo pedido."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}

		else if(documentId.equalsIgnoreCase("new")){

			if(hasPermissions){
				Document doc = new Document();
				doc.ownerId = ownerId;
				doc.ownerTypeId = ownerTypeId;
				view.getForm().setValue(doc);
				view.setToolBarSaveMode(true);
				view.setEditable(true);
				newDocument = true;
			}
			else{
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o documento."), TYPE.ALERT_NOTIFICATION));
				view.setEditable(false);
				view.lockToolbar(true);
			}
		}
		else{
			newDocument = false;
			broker.getDocument(ownerId, documentId, new ResponseHandler<Document>() {
				@Override
				public void onResponse(Document response) {
					view.getForm().setValue(response);
					view.setEditable(false);
					view.lockToolbar(!allow);
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
					if(view.getForm().validate()) {
						createUpdateDocument(view.getForm().getInfo());
					}else{
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
					}
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
					String fileInfo = view.getFileInfo();
					String[] splitted = fileInfo.split("!");
					view.setFilename(splitted[1]);
					view.setFileStorageId(splitted[0]);
					view.hasFile(true);
					break;
				}
				case NEW_FILE_FROM_DOCUSHARE:{
					ScanItem item = view.getScanItem();
					ScanHandle handle = new ScanHandle();
					handle.docushare = item.docushare;
					handle.handle = item.handle;
					handle.locationHandle = view.getLocationHandle();
					view.setFilename(item.fileName);
					view.setMimeType(item.mimeType);
					view.hasFile(true);
					break;
				}
				case ADD_NEW_DETAIL:
					break;
				case UPLOAD_SUCCESS:
					break;
				default:
					break;
				}
			}

		});

		bound = true;
	}
	private void downloadFile() {

		if(view.getCurrentFileStorageId() != null){
			Window.open(GWT.getModuleBaseURL() + "bbfile?fileref=" + view.getCurrentFileStorageId() , null, null);
		}else{
			ScanHandle handle = view.getScanHandle();
			scanItemService.getItem(handle.handle, new BigBangAsyncCallback<String>() {

				@Override
				public void onResponseSuccess(String result) {
					Window.open(GWT.getModuleBaseURL() + "bbfile?fileref=" + result , null, null);
				}


			});
		}

	}

	private void removeDocument() {

		broker.deleteDocument(view.getForm().getValue().id, new ResponseHandler<Void>() {

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
			broker.createDocument(temp, new ResponseHandler<Document>() {

				@Override
				public void onResponse(Document response) {
					onCreateDocument();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o documento."), TYPE.ALERT_NOTIFICATION));

				}
			});
		}

		else
		{
			broker.updateDocument(temp, new ResponseHandler<Document>() {

				@Override
				public void onResponse(Document response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Documento gravado com sucesso."), TYPE.TRAY_NOTIFICATION));
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.removeParameter("documentid");
					navig.removeParameter("ownertypeid");
					navig.removeParameter("show");
					NavigationHistoryManager.getInstance().go(navig);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar o documento."), TYPE.ALERT_NOTIFICATION));

				}
			});
		}

	}

	protected void onCreateDocument() {
		NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
		navig.removeParameter("documentid");
		navig.removeParameter("ownertypeid");
		navig.removeParameter("show");
		NavigationHistoryManager.getInstance().go(navig);
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Documento criado com sucesso."), TYPE.TRAY_NOTIFICATION));		
	}

	private void cancelChanges(){

		if(newDocument){
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

	public void allowEdit(boolean b) {
		allow = b;
	}


}
