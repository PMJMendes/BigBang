package bigBang.library.client.userInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Document;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangDocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Button;

public class DocumentsList extends FilterableList<Document> implements DocumentsBrokerClient {

	public static class Entry extends ListEntry<Document> {

		public Entry(Document value) {
			super(value);
			setHeight("25px");
			titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}

		public <I extends Object> void setInfo(I info) {
			Document c = (Document) info;
			this.setTitle(c.name);
		};
	}

	protected int documentsDataVersion;
	protected String ownerId;
	protected DocumentsBroker broker;
	private String ownerTypeId;
	private Button createNew;

	public DocumentsList(){
		
		createNew = new Button("Criar novo Documento");
		createNew.setEnabled(false);
		createNew.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewDocument();
			}
		});
		
		this.showFilterField(false);
		this.showSearchField(true);
		
		headerContainer.add(createNew);
		createNew.setWidth("100%");
		createNew.setHeight("32px");

		broker = BigBangDocumentsBroker.Util.getInstance();

		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()) {
					setOwner(ownerId);
				}else{
					discardOwner();
				}
			}
		});
	}

	protected void createNewDocument() {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "documentmanagement");
		navItem.setParameter("ownerid", ownerId );
		navItem.setParameter("ownertypeid", ownerTypeId);
		navItem.setParameter("editpermission", createNew.isEnabled() ? "1" : "0");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	public void setOwner(final String ownerId){
		discardOwner();
		if(ownerId != null){
			this.broker.registerClient(this, ownerId);
			this.broker.getDocuments(ownerId, new ResponseHandler<Collection<Document>>() {

				@Override
				public void onResponse(Collection<Document> response) {
					setDocuments(ownerId, new ArrayList<Document>(response));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					GWT.log("Could not get the documents for owner " + ownerId);
				}
			});
		}
		this.ownerId = ownerId;
	}

	public void discardOwner(){
		this.clear();
		if(ownerId != null) {
			broker.unregisterClient(this);
			this.ownerId = null;
		}
	}

	public void addEntry(Document c){
		this.add(new Entry(c));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.DOCUMENT)) {
			this.documentsDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.DOCUMENT)){
			return documentsDataVersion;
		}
		return -1;
	}

	@Override
	public void setDocumentsDataVersionNumber(String ownerId, int number) {
		documentsDataVersion = number;
	}

	@Override
	public void setDocuments(String ownerId, List<Document> documents) {
		this.clear();
		if(documents != null){
			for(Document c : documents) {
				addEntry(c);
			}
		}
	}

	@Override
	public void removeDocument(String ownerId, Document document) {
		if(ownerId != null && this.ownerId != null && ownerId.equalsIgnoreCase(this.ownerId)){
			for(ValueSelectable<Document> s : this) {
				if(s.getValue().id.equalsIgnoreCase(document.id)){
					remove(s);
				}
			}
		}
	}

	@Override
	public void addDocument(String ownerId, Document document) {
		if(ownerId != null && this.ownerId != null && ownerId.equalsIgnoreCase(this.ownerId)) {
			addEntry(document);
		}
	}

	@Override
	public void updateDocument(String ownerId, Document document) {
		if(ownerId != null && this.ownerId != null && ownerId.equalsIgnoreCase(this.ownerId)){
			for(ValueSelectable<Document> s : this){
				if(s.getValue().id.equalsIgnoreCase(document.id)){
					s.setValue(document);
				}
			}
		}
	}

	@Override
	public int getDocumentsDataVersionNumber(String ownerId) {
		return this.documentsDataVersion;
	}

	public void setOwnerType(String client) {
		ownerTypeId = client;
	}
	
	public void allowCreation(boolean hasPermission) {
		createNew.setEnabled(hasPermission);
	}

}