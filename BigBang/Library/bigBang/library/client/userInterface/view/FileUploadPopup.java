package bigBang.library.client.userInterface.view;

import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.DocuShareNavigationPanel;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public interface FileUploadPopup
{

	public FileUploadPopup getUploadPopup();
	public String getFileStorageId();
	public String getFilename();
	public void setParameters(String ownerId, String ownerTypeId);
	public void hidePopup();
	
	public static class FileUploadPopupDisk extends PopupPanel implements FileUploadPopup{

		private Label mlblError;
		private FormPanel mfrmMain;
		private FileUpload mfupMain;
		private Button mbtnOk;
		private Button mbtnCancel;
		private String filename;
		
		public FormPanel getSubmitForm(){
			return mfrmMain;
		}
		
		@Override
		public String getFilename() {
			return filename;
		}

		@Override
		public String getFileStorageId() {
			return fileStorageId;
		}


		private String fileStorageId;
		
		private ActionInvokedEventHandler<Action> actionHandler;
		
		public FileUploadPopupDisk(String key){
			
			super();
			this.getElement().getStyle().setZIndex(12000);
			VerticalPanel lvert;
			HorizontalPanel lhorz;

			lvert = new VerticalPanel();

			mlblError = new Label(" ");
			lvert.add(mlblError);

			mfrmMain = new FormPanel();
			mfrmMain.setEncoding(FormPanel.ENCODING_MULTIPART);
			mfrmMain.setMethod(FormPanel.METHOD_POST);
			String action = GWT.getModuleBaseURL() + "bbfile";
			mfrmMain.setAction(action);
			lvert.add(mfrmMain);
			mfupMain = new FileUpload();
			mfrmMain.setWidget(mfupMain);

			mfupMain.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					if(mfupMain.getFilename().isEmpty()){
						mbtnOk.setEnabled(false);
					}
					else
						mbtnOk.setEnabled(true);
					
				}
			});
			lhorz = new HorizontalPanel();
			mbtnOk = new Button();
			mbtnOk.setText("Ok");
			mbtnOk.setEnabled(false);
			lhorz.add(mbtnOk);
			mbtnCancel = new Button();
			mbtnCancel.setText("Cancel");
			lhorz.add(mbtnCancel);
			lvert.add(lhorz);

			add(lvert);
			
			SetKey(key);

//			mfrmMain.addSubmitCompleteHandler(new SubmitCompleteHandler()
//			{
//				public void onSubmitComplete(SubmitCompleteEvent event)
//				{
//					String lstrResults;
//
//					mbtnOk.setEnabled(true);
//					mbtnCancel.setEnabled(true);
//
//					lstrResults = event.getResults();
//					if ( lstrResults.startsWith("!") )
//					{
//						SetError(lstrResults.substring(1));
//						return;
//					}
//
//					String [] splitString =  lstrResults.split("!");
//					
//					String fileStorageId = splitString[0];
//					String filename = splitString[1];
//					
//					FileUploadPopupDisk.this.fileStorageId = fileStorageId;
//					FileUploadPopupDisk.this.filename = filename;
//					fireAction(Action.UPLOAD_SUCCESS);
//					
//					hidePopup();
//				}
//
//
//			});
			
			
			mbtnOk.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event)
		        {
					mbtnOk.setEnabled(false);
					mbtnCancel.setEnabled(false);
					SetError(null);
					mfrmMain.submit();
		        }
		    });
			mbtnCancel.addClickHandler(new ClickHandler()
			{
				public void onClick(ClickEvent event)
		        {
					hidePopup();
		        }
			});	
			
			this.center();
		}
		
		public void SetKey(String pstrKey)
		{
			if ( pstrKey == null )
				mfupMain.setName("none");
			else
				mfupMain.setName(pstrKey);
		}
		
		
		private void SetError(String pstrError)
		{
			if ( (pstrError == null) || (pstrError.equals("")) )
				mlblError.setText(" ");
			else
				mlblError.setText(pstrError);
		}
		
		public void initHandler(ActionInvokedEventHandler<Action> actionHandler){

			this.actionHandler = actionHandler;

		}

		@Override
		public FileUploadPopup getUploadPopup() {
			return this;
		}

		@Override
		public void setParameters(String ownerId, String ownerTypeId) {
		}
			
	}
	
	public static class FileUploadPopupDocuShare extends PopupPanel implements FileUploadPopup{
		
		DocuShareNavigationPanel list = new DocuShareNavigationPanel();
		VerticalPanel lvert;
		
		public FileUploadPopupDocuShare(){
			super();
			this.getElement().getStyle().setZIndex(12000);
			lvert = new VerticalPanel();
			lvert.add(list);
			list.setSize("300px", "400px");
			add(lvert);
			this.center();
			
		}

		@Override
		public void setParameters(String ownerId, String ownerTypeId){
			list.setParameters(ownerId, ownerTypeId);
		}
		
		public DocuShareNavigationPanel getPanel(){
			return list;
		}

		@Override
		public FileUploadPopup getUploadPopup() {
			return this;
		}

		@Override
		public String getFileStorageId() {
			return null;
		}

		@Override
		public String getFilename() {
			return null;
		}
	}
}
		
