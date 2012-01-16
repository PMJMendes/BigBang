package bigBang.library.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolBar;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection.DocumentDetailEntry;
import bigBang.library.interfaces.Service;

public class DocumentView extends View implements DocumentViewPresenter.Display{
	
	private VerticalPanel wrapper;
	private Document doc;
	private DocumentSections.GeneralInfoSection top;
	private DocumentSections.FileNoteSection middle;
	private DocumentSections.DetailsSection details;
	ActionInvokedEventHandler<Action> actionHandler;
	
	public DocumentView(){
		
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		top = new DocumentSections.GeneralInfoSection();
		middle = new DocumentSections.FileNoteSection();
		details = new DocumentSections.DetailsSection();
		wrapper.add(top);
		wrapper.add(middle); 
		
		ListHeader conts = new ListHeader("Detalhes");
		wrapper.add(conts);
		details.details.setSelectableEntries(false);
		wrapper.add(details.details.getListContent());
		wrapper.setWidth("100%");
	}
	
	@Override
	public Document getInfo(){
		
		return this.doc;
	}
	
	@Override
	protected void initializeView() {
		return;
	}

		
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		
		this.actionHandler = handler;
		top.initHandler(handler);
		middle.initHandler(handler);
		details.initHandler(handler);
	}

	@Override
	public void createNewFile() {
		middle.createNewFile();
		
	}

	@Override
	public void createNewNote() {
		middle.createNewNote();
		
	}
	
	@Override
	public void addDetail(DocInfo docInfo){
		
		details.addDocumentDetail(docInfo);
		
	}
	

	@Override
	public DocumentSections.GeneralInfoSection getGeneralInfo() {
		return top;
	}

	@Override
	public DocumentSections.FileNoteSection getFileNote() {
		return middle;
	}

	@Override
	public DocumentSections.DetailsSection getDetails() {
		return details;
	}

	@Override
	public void setEditable(boolean b) {
		
		details.setEditable(b);
		top.setEditable(b);
		middle.setEditable(b);
		
	}

	@Override
	public DocumentDetailEntry initializeDocumentDetailEntry() {
		
		return details.getNewDocumentDetailEntry();
	}

	@Override
	public void setValue(Document doc) {
		this.doc = doc;
	}


}
