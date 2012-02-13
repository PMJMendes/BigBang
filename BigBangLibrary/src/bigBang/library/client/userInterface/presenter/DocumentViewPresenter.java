package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.ContentChangedEventHandler;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection.DocumentDetailEntry;
import bigBang.library.client.userInterface.view.DocumentSections.FileNoteSection;
import bigBang.library.client.userInterface.view.DocumentSections.GeneralInfoSection;
import bigBang.library.client.userInterface.view.FileUploadPopup;
import bigBang.library.interfaces.DocuShareService;
import bigBang.library.interfaces.DocuShareServiceAsync;
import bigBang.library.shared.DocuShareItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
	private String ownerTypeId;

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
		void addDetail(DocInfo docInfo);
		GeneralInfoSection getGeneralInfo();
		FileNoteSection getFileNote();
		DetailsSection getDetails();
		void setEditable(boolean b);
		Document getInfo();
		DocumentDetailEntry initializeDocumentDetailEntry();
		void setValue(Document doc);
		public void registerDeleteHandler(
				DeleteRequestEventHandler deleteRequestEventHandler);
		void setSaveMode(boolean b);
		void registerValueChangeHandler(ValueChangeHandler<DocuShareItem> valueChangeHandler);
		public void registerContentChangedEventHandler(ContentChangedEventHandler contentChangedEventHandler);
		void clearAll();

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
		view.clearAll();
		broker.unregisterClient(this);
		ownerId = parameterHolder.getParameter("id");
		documentId = parameterHolder.getParameter("documentid");
		ownerTypeId = parameterHolder.getParameter("ownertypeid");
		boolean hasPermissions = parameterHolder.getParameter("editpermission") != null;

		if(ownerId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar um documento sem cliente associado."), TYPE.ALERT_NOTIFICATION));
			view.getGeneralInfo().getToolbar().lockAll();
			view.addDetail(null);
			view.setEditable(false);
			return;
		}
		else{
			broker.registerClient(this, ownerId);
		}	
		if(documentId == null){

			if(hasPermissions){
				setDocument(null);
			}
			else{
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o documento."), TYPE.ALERT_NOTIFICATION));
				view.getGeneralInfo().getToolbar().lockAll();
				view.addDetail(null);
				view.setEditable(false);
			}
		}
		else{
			broker.getDocument(ownerId, documentId, new ResponseHandler<Document>() {
				@Override
				public void onResponse(Document response) {
					doc = response;
					setDocument(doc);
					view.setEditable(false);

				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.removeParameter("documentid");
					navig.removeParameter("operation");
					navig.removeParameter("ownertypeid");
					navig.removeParameter("show");
					NavigationHistoryManager.getInstance().go(navig);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o documento pedido."), TYPE.ALERT_NOTIFICATION));
				}
			});

		}

		if(!hasPermissions){
			view.getGeneralInfo().getToolbar().lockAll();
		}

	}

	private void bind() {
		if(bound){
			return;
		}


		view.registerDeleteHandler(new DeleteRequestEventHandler(){

			@Override
			public void onDeleteRequest(Object object) {

				for(ValueSelectable<DocInfo> cont: view.getDetails().getList()){
					if(cont.getValue() == object) {;
					view.getDetails().getList().remove(cont);
					break;
					}

				}

			}


		});
		


		view.registerContentChangedEventHandler(new ContentChangedEventHandler() {
			@Override
			public void onContentChanged() {
				if(view.getFileNote().getNote().getNativeField().getValue().length() > 0)
					view.getFileNote().lockFiles(true);
				else if(view.getGeneralInfo().getToolbar().isSaveModeEnabled()){
					view.getFileNote().lockFiles(false);
				}
			}
		});

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
					Document temp = getDocument();
					if(temp.text != null && temp.text.length() > 0){
						temp.fileName = null;
						temp.fileStorageId = null;
					}else{
						temp.fileName = view.getFileNote().getFileUploadFilename();
						temp.fileStorageId = view.getFileNote().getFileStorageId();
						temp.text = null;
					}
					createUpdateDocument(temp);
					break;
				}
				case ADD_NEW_DETAIL:{
					addDetail();
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
					view.getFileNote().setUploadDialog(new FileUploadPopup.FileUploadPopupDisk(doc != null ? doc.fileStorageId : null));
					view.getFileNote().getUploadDialog().initHandler(new ActionInvokedEventHandler<DocumentViewPresenter.Action>() {

						@Override
						public void onActionInvoked(ActionInvokedEvent<Action> action) {

							view.getFileNote().hasFile(true);
							view.getFileNote().setFileStorageId(view.getFileNote().getUploadDialog().getFileStorageId());
							doc.fileStorageId=view.getFileNote().getFileStorageId();
							view.getFileNote().getFilename().setValue(view.getFileNote().getUploadDialog().getFilename());
							view.getFileNote().getMimeImage().setResource(view.getFileNote().getMimeImage(""));
						}
					});
					
					break;
				}
				case NEW_FILE_FROM_DOCUSHARE:{

					view.getFileNote().setUploadDialog(new FileUploadPopup.FileUploadPopupDocuShare(doc != null ? doc.fileStorageId : null));
					view.getFileNote().getUploadDialog().getUploadPopup().setParameters(ownerId, ownerTypeId);
					view.registerValueChangeHandler(new ValueChangeHandler<DocuShareItem>() {

						@Override
						public void onValueChange(
								final ValueChangeEvent<DocuShareItem> event) {
							docuShareservice = DocuShareService.Util.getInstance();
							docuShareservice.getItem(event.getValue().handle, new BigBangAsyncCallback<String>() {

								@Override
								public void onSuccess(String result) {

									view.getFileNote().hasFile(true);
									view.getFileNote().getFilename().setValue(event.getValue().desc);
									view.getFileNote().setFileUploadFilename(event.getValue().desc);
									view.getFileNote().setFileStorageId(result);
									doc.fileStorageId = result;
									view.getFileNote().getUploadDialog().getUploadPopup().hidePopup();
									view.getFileNote().getMimeImage().setResource(view.getFileNote().getMimeImage(""));
								}

								@Override
								public void onFailure(Throwable caught) {
									EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o ficheiro."), TYPE.TRAY_NOTIFICATION));
									super.onFailure(caught);
								}
							});

						}


					});

					break;
				}

				}

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
						view.setSaveMode(true);
					}


				});

			}

			private void createUpdateDocument(Document temp) {



				if(temp.id == null){

					temp.fileName = view.getFileNote().getFilename().getValue();
					temp.ownerId = ownerId;
					temp.ownerTypeId = ownerTypeId;
					broker.createDocument(temp, new ResponseHandler<Document>() {

						@Override
						public void onResponse(Document response) {

							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Documento criado com sucesso."), TYPE.TRAY_NOTIFICATION));
							NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
							navig.setParameter("documentid", response.id);
							NavigationHistoryManager.getInstance().go(navig);

						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o documento."), TYPE.ALERT_NOTIFICATION));
							view.setSaveMode(true);
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
							navig.setParameter("documentid", response.id);
							NavigationHistoryManager.getInstance().go(navig);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar o documento."), TYPE.ALERT_NOTIFICATION));
							view.setSaveMode(true);
						}
					});
				}

			}

			private Document getDocument() {


				Document newD = new Document();

				if(getInfo() != null)
					newD = getInfo();

				newD.name = view.getGeneralInfo().getName().getValue();
				newD.docTypeId = view.getGeneralInfo().getDocType().getValue();
				newD.fileName = view.getFileNote().getFilename().getValue();
				newD.text = view.getFileNote().getNote().getValue();
				newD.fileStorageId = view.getFileNote().getFileStorageId();
				newD.fileName = view.getFileNote().getFileUploadFilename();

				for(int i = 0; i<view.getDetails().getList().size()-1; i++){

					if(((DocumentDetailEntry)view.getDetails().getList().get(i)).getInfo().getValue() == null || ((DocumentDetailEntry)view.getDetails().getList().get(i)).getInfoValue().getValue() == null ){
						view.getDetails().getList().remove(i);
						i--;
					}

				}

				newD.parameters = new DocInfo[view.getDetails().getList().size()-1];

				for(int i = 0; i<view.getDetails().getList().size()-1; i++){

					if(view.getDetails().getList().get(i).getText() != null && view.getDetails().getList().get(i).getText() != null)
						newD.parameters[i] = new DocInfo();
					newD.parameters[i].name = ((DocumentDetailEntry) view.getDetails().getList().get(i)).getInfo().getValue();
					newD.parameters[i].value = ((DocumentDetailEntry) view.getDetails().getList().get(i)).getInfoValue().getValue();

				}

				return newD;

			}

			private void removeFile() {

				doc.fileStorageId = null;
				view.getFileNote().setFileStorageId(null);
				doc.fileName = null;
				view.getFileNote().setFileUploadFilename(null);
				doc.mimeType = null;
				view.getFileNote().removeFile();

			}
		});
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
			navig.removeParameter("operation");
			navig.removeParameter("ownertypeid");
			navig.removeParameter("show");
			NavigationHistoryManager.getInstance().go(navig);
			return;
		}

		NavigationHistoryManager.getInstance().reload();

	}

	public void setDocument(Document doc) {

		view.getDetails().getList().clear();

		if(doc == null){
			doc = new Document();
			view.setValue(doc);
			view.setEditable(true);
			view.addDetail(null);
			view.setSaveMode(true);
			view.getFileNote().setDocument(doc);
			return;
		}

		view.getFileNote().setDocument(doc);
		view.setValue(doc);
		this.doc = doc;
		DocInfo[] docInfo = doc.parameters;
		view.getGeneralInfo().setDocument(doc);

		for(int i = 0; i< docInfo.length; i++){

			view.addDetail(docInfo[i]);
		}
		view.addDetail(null);
		view.setEditable(false);

	}


	public Document getInfo(){

		return this.doc;

	}

	public void addDetail(){

		DocInfo temp = new DocInfo();
		view.getDetails().getList().remove(view.getDetails().getList().size()-1);
		view.addDetail(temp);
		view.addDetail(null);

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
		navig.removeParameter("operation");
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
