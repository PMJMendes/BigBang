package bigBang.library.client.userInterface.view;

import bigBang.library.client.userInterface.DocuShareNavigationPanel;
import bigBang.library.client.userInterface.DocumentNavigationList;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;
import bigBang.library.client.userInterface.view.DocumentSections.FileNoteSection;
import bigBang.library.client.userInterface.view.FileUploadPopup.FileUploadPopupDisk;
import bigBang.library.client.userInterface.view.FileUploadPopup.Filetype;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class FileUploadPopup
	extends DialogBox
{
	
	public static enum Filetype{
		DISK,
		DOCUSHARE
	}
	
	public static class TypeChooserPopup extends PopupPanel{
		
		private FileUploadPopupDisk fileUploadPopupDisk;
		private FileUploadPopupDocuShare fileUploadPopupDocuShare;

		//private FileUploadPopupDocushare fileUploadPopupDocushare;
		private HorizontalPanel buttonsPanel = new HorizontalPanel();
		private Button fromDisk;
		private Button fromDocuShare;
		private DocumentSections.FileNoteSection fileNoteSection;
		
		public TypeChooserPopup(final DocumentSections.FileNoteSection fileNoteSection){
			super();
			this.fileNoteSection = fileNoteSection;
			
			this.setSize("200px", "200px");
			
			fromDisk = new Button("Disco Rígido");
			fromDocuShare = new Button("DocuShare");
			fromDisk.setSize("90px", "90px");
			fromDocuShare.setSize("90px", "90px");
			
			fromDisk.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					fileNoteSection.fireAction(Action.NEW_FILE_FROM_DISK);
				}
			});
			
			fromDocuShare.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					fileNoteSection.fireAction(Action.NEW_FILE_FROM_DOCUSHARE);					
				}
			});
			
			buttonsPanel.add(fromDisk);
			buttonsPanel.add(fromDocuShare);
			buttonsPanel.setSize("100%", "100%");
			
			add(buttonsPanel);
		}

		public void setType(Filetype type, String docId) {
			hidePopup();
			
			if(type == Filetype.DISK){
				fileUploadPopupDisk = new FileUploadPopupDisk(docId, fileNoteSection);
				fileUploadPopupDisk.center();
			}
			else
				fileUploadPopupDocuShare = new FileUploadPopupDocuShare(docId, fileNoteSection);
				fileUploadPopupDocuShare.center();
		}
		
		public FileUploadPopupDisk getFileUploadPopupDisk() {
			return fileUploadPopupDisk;
		}
		
		
		public FileUploadPopupDocuShare getFileUploadPopupDocuShare() {
			return fileUploadPopupDocuShare;
		}
		
		
	}
	
	public static class FileUploadPopupDisk extends PopupPanel{

		private Label mlblError;
		private FormPanel mfrmMain;
		private FileUpload mfupMain;
		private Button mbtnOk;
		private Button mbtnCancel;
		
		
		public FileUploadPopupDisk(String key, final DocumentSections.FileNoteSection fileNoteSection){
			
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
			lhorz = new HorizontalPanel();
			mbtnOk = new Button();
			mbtnOk.setText("Ok");
			lhorz.add(mbtnOk);
			mbtnCancel = new Button();
			mbtnCancel.setText("Cancel");
			lhorz.add(mbtnCancel);
			lvert.add(lhorz);

			add(lvert);
			
			SetKey(key);

			mfrmMain.addSubmitCompleteHandler(new SubmitCompleteHandler()
			{
				public void onSubmitComplete(SubmitCompleteEvent event)
				{
					String lstrResults;

					mbtnOk.setEnabled(true);
					mbtnCancel.setEnabled(true);

					lstrResults = event.getResults();
					if ( lstrResults.startsWith("!") )
					{
						SetError(lstrResults.substring(1));
						return;
					}

					String [] splitString =  lstrResults.split("!");
					
					String fileStorageId = splitString[0];
					String filename = splitString[1];
					
					fileNoteSection.setFileStorageId(fileStorageId);
					fileNoteSection.setFileUploadFilename(filename);
					fileNoteSection.fireAction(Action.UPLOAD_SUCCESS);
					
					hidePopup();
				}
			});
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
			
	}
	
	public static class FileUploadPopupDocuShare extends PopupPanel{
		
		DocuShareNavigationPanel list = new DocuShareNavigationPanel();
		VerticalPanel lvert;
		public FileUploadPopupDocuShare(String docId, FileNoteSection fileNoteSection){
			super();
			this.getElement().getStyle().setZIndex(12000);
			
			lvert = new VerticalPanel();
			lvert.add(list);
			list.setSize("300px", "400px");
			add(lvert);
			
		}
	}
}
		
