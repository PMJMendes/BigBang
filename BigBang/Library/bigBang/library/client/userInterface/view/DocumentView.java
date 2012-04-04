package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Document;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
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
	protected String fileInfo;
	protected DocuShareItem docuShareItem;

	public DocumentView(){

		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		toolbar = new DocumentOperationsToolBar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<DocumentViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				form.setReadOnly(false);
			}

			@Override
			public void onDeleteRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<DocumentViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CANCEL));
			}
		};

		wrapper.add(toolbar);

		form = new DocumentForm() {

			@Override
			protected void onSubmitComplete(String results) {
				fileInfo = results;
				uploadDialog.hidePopup();
				actionHandler.onActionInvoked(new ActionInvokedEvent<DocumentViewPresenter.Action>(Action.NEW_FILE_FROM_DISK));
			}

			@Override
			protected void onDownloadFile() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<DocumentViewPresenter.Action>(Action.DOWNLOAD_FILE));
			}

			@Override
			protected void onDocushareItemChanged(DocuShareItem value) {
				docuShareItem = value;
				uploadDialog.hidePopup();
				actionHandler.onActionInvoked(new ActionInvokedEvent<DocumentViewPresenter.Action>(Action.NEW_FILE_FROM_DOCUSHARE));
			}
		};

		form.getNonScrollableContent().setSize("400px", "700px");
		wrapper.add(form.getNonScrollableContent());
		wrapper.setCellHeight(form, "100%");
	}

	@Override
	public String getFileInfo(){
		return fileInfo;
	}

	@Override
	public DocuShareHandle getDocuShareHandle(){
		return form.getDocuShareHandle();
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
		this.toolbar.setLocked(b);
	}

	@Override
	public String getCurrentFileStorageId() {
		return form.getFileStorageId();
	}

	@Override
	public void hasFile(boolean b) {
		form.isFile(b);

	}

	@Override
	public void setFilename(String string) {
		form.setFilename(string);
	}

	@Override
	public void setFileStorageId(String string) {
		form.setFileStorageId(string);

	}

	@Override
	public String getLocationHandle() {
		return form.getUploadPopup().getDirectoryHandle();
	}

	@Override
	public void setMimeType(String mimeType) {
		form.setMimeType(mimeType);
	}
	
	@Override
	public void setToolBarSaveMode(boolean b){
		toolbar.setSaveModeEnabled(b);
	}

	@Override
	public DocuShareItem getDocuShareItem() {
		return docuShareItem;
	}


}
