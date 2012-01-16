package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Document;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;

import com.google.gwt.user.client.ui.VerticalPanel;

public class DocumentView extends View implements DocumentViewPresenter.Display{
	
	private VerticalPanel wrapper;
	private Document doc;
	private DocumentSections.DocumentTopSection top;
	private DocumentSections.DocumentMiddleSection middle;
	ActionInvokedEventHandler<Action> actionHandler;
	
	public DocumentView(){
		
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		top = new DocumentSections.DocumentTopSection();
		middle = new DocumentSections.DocumentMiddleSection();
		wrapper.add(top);
		wrapper.add(middle); 
		wrapper.setWidth("100%");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setDocument(Document doc) {
		
		this.doc = doc;
		
		if(doc == null){
			middle.generateNewDocument();
			return;
		}
		else{
			top.setDocument(doc);
//			middle.setDocument(doc);
		}
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		
		this.actionHandler = handler;
		top.initHandler(handler);
		middle.initHandler(handler);

	}

	@Override
	public void createNewFile() {
		middle.createNewFile();
		
	}

	@Override
	public void createNewNote() {
		middle.createNewNote();
		
	}

}
