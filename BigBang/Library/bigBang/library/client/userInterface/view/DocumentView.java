package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Document;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DocumentForm;
import bigBang.library.client.userInterface.DocumentOperationsToolBar;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;
import bigBang.library.shared.DocuShareItem;

import com.google.gwt.user.client.ui.VerticalPanel;

public class DocumentView extends View implements DocumentViewPresenter.Display{

	private VerticalPanel wrapper;
	private DocumentForm form;
	ActionInvokedEventHandler<Action> actionHandler;
	private DocumentOperationsToolBar toolbar;
	
	public DocumentView(){

		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		toolbar = new DocumentOperationsToolBar() {
			
			@Override
			public void onSaveRequest() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onEditRequest() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDeleteRequest() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancelRequest() {
				// TODO Auto-generated method stub
				
			}
		};
		
		wrapper.add(toolbar);
		
		form = new DocumentForm() {
			
			@Override
			protected void onSubmitComplete(String results) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void onPressedRemoveFile() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void onDownloadFile() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void onDocushareItemChanged(DocuShareItem value) {
				// TODO Auto-generated method stub
				
			}
		};
		
		form.getNonScrollableContent().setSize("400px", "700px");
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form, "100%");
	}

	@Override
	protected void initializeView() {
		return;
	}



	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
		
	}
	
	@Override
	protected void onDetach() {

		super.onDetach();
	}


	@Override
	public void clear() {
		
		form.clearInfo();
		
	}

	@Override
	public void setEditable(boolean b) {
		form.setReadOnly(!b);
	}

	@Override
	public HasEditableValue<Document> getForm() {
		return form;
	}

	@Override
	public void lockToolbar(boolean b) {
		// TODO Auto-generated method stub
		
	}


}
