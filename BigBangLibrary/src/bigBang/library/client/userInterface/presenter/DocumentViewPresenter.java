package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection.DocumentDetailEntry;
import bigBang.library.client.userInterface.view.DocumentSections.FileNoteSection;
import bigBang.library.client.userInterface.view.DocumentSections.GeneralInfoSection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DocumentViewPresenter implements ViewPresenter, DocumentsBrokerClient{

	private Display view;
	private Document doc;
	private boolean bound = false;
	private DocumentsBroker broker;
	private int versionNumber;

	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		NEW_FILE,
		NEW_NOTE, CHANGE_TO_FILE, CHANGE_TO_NOTE, ADD_NEW_DETAIL, REMOVE_FILE, DELETE_DETAIL
	}

	public DocumentViewPresenter(Display view){

		broker = (DocumentsBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DOCUMENT);
		this.setView((UIObject) view);

	}

	public interface Display{

		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void createNewFile();
		void createNewNote();
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
		
		broker.unregisterClient(this);
		
		String idOwner = parameterHolder.getParameter("id");
		String idDoc = parameterHolder.getParameter("documentid");
		boolean hasPermissions = parameterHolder.getParameter("editpermission") != null;

		if(idOwner == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar um documento sem cliente associado."), TYPE.ALERT_NOTIFICATION));
			view.getGeneralInfo().getToolbar().lockAll();
			view.getFileNote().generateNewDocument();
			view.addDetail(null);
			view.setEditable(false);
			return;
		}
		else{

			broker.registerClient(this, idOwner);
			
		}	
		if(idDoc == null){

			if(hasPermissions){
				setDocument(null);
			}
			else{
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o documento."), TYPE.ALERT_NOTIFICATION));
				view.getGeneralInfo().getToolbar().lockAll();
				view.getFileNote().generateNewDocument();
				view.addDetail(null);
				view.setEditable(false);
			}
		}
		else{
			broker.getDocument(idOwner, idDoc, new ResponseHandler<Document>() {
				@Override
				public void onResponse(Document response) {

					doc = response;
					setDocument(doc);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o documento pedido."), TYPE.ALERT_NOTIFICATION));
				}
			});

		}
		//OWNER ID OBRIGATORIO
		//if docid == null && hasPermissions, new doc
		//else
		//erro - Não é possivel criar ou editar o documento.


		//idOwner = idOwner == null ? new String() : idOwner;
		//idDoc = idDoc == null ? new String() : idDoc;

		if(!hasPermissions){
			view.getGeneralInfo().getToolbar().lockAll();
		}

		//		broker.getDocument(id, new ResponseHandler<Document>() {
		//			
		//			@Override
		//			public void onResponse(Document response) {
		//				// TODO Auto-generated method stub
		//				
		//			}
		//			
		//			@Override
		//			public void onError(Collection<ResponseError> errors) {
		//				// TODO Auto-generated method stub
		//				
		//			}
		//		});
		//		
		//		if(groupId.isEmpty()){
		//			clearView();
		//		}else if(groupId.equalsIgnoreCase("new")){
		//			setupNewClientGroup();
		//		}else{
		//			showClientGroup(groupId);
		//		}
	}

	private void bind() {
		if(bound){
			return;
		}
		view.registerDeleteHandler(new DeleteRequestEventHandler(){

			@Override
			public void onDeleteRequest(Object object) {
				//TODO APAGAR DA BD
				List<DocInfo> list = view.getDetails().getList();

				for(ValueSelectable<DocInfo> cont: list){
					if(cont.getValue() == object) {
						list.remove(cont);
						break;
					}

				}


			}


		});

		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {

				switch(action.getAction()){
				case NEW_FILE: 
					view.createNewFile(); 
					break;
				case CANCEL:{
					if(doc == null){
						NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
						navig.removeParameter("documentid");
						navig.removeParameter("operation");
						NavigationHistoryManager.getInstance().go(navig);
						break;
					}
					setDocument(view.getInfo());
					view.setEditable(false);
					break;
				}
				case EDIT:{
					view.setEditable(true);
					break;
				}
				case NEW_NOTE:{
					view.createNewNote();
					break;
				}
				case SAVE:{
					boolean success = false;
					doc = getDocument();
					if(success){
						setDocument(doc);
						//TODO SAVE TO BD
						view.setSaveMode(false);
					}
					else{
						//TODO FIRE ERROR

					}
					break;
				}
				case CHANGE_TO_FILE: {
					view.createNewFile(); 
					break;
				}
				case CHANGE_TO_NOTE:{
					view.createNewNote();
					break;
				}
				case  ADD_NEW_DETAIL:{
					addDetail();
					break;
				}
				case REMOVE_FILE:{
					removeFile();
					break;
				}
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

				newD.parameters = new DocInfo[view.getDetails().getList().size()-1];

				for(int i = 0; i<view.getDetails().getList().size()-1; i++){

					newD.parameters[i] = new DocInfo();
					newD.parameters[i].name = ((DocumentDetailEntry) view.getDetails().getList().get(i)).getInfo().getValue();
					newD.parameters[i].value = ((DocumentDetailEntry) view.getDetails().getList().get(i)).getInfoValue().getValue();

				}
				if(!view.getFileNote().isFileBoolean()){
					newD.fileStorageId = null;
					newD.fileName = null;
				}
				else{
					if(view.getFileNote().getFilename().isVisible()){
						newD.fileName = view.getFileNote().getFilename().getValue();
					}
					else
						newD.fileName = view.getFileNote().getUpload().getFilename();
				}


				return newD;

			}

			private void removeFile() {
				view.getFileNote().removeFile();
			}


		});
	}

	public void setDocument(Document doc) {

		view.getDetails().getList().clear();

		if(doc == null){
			view.getFileNote().generateNewDocument();
			view.addDetail(null);
			view.setSaveMode(true);
			return;
		}

		if(doc.fileStorageId != null){
			view.getFileNote().createNewFile();
			view.getFileNote().setDocumentFile(doc);
		}else{
			view.getFileNote().createNewNote();
			view.getFileNote().setDocumentNote(doc);
		}
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
		
		NavigationHistoryItem navig = null;

	}

	@Override
	public void addDocument(String ownerId, Document document) {
		
		return;
	}

	@Override
	public void updateDocument(String ownerId, Document document) {
		
		if(doc.id.equalsIgnoreCase(document.id)){
			NavigationHistoryManager.getInstance().reload();
		}
	}


}
