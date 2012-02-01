package bigBang.library.client.userInterface.view;

import bigBang.library.client.userInterface.DocumentNavigationList;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;

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

public class FileUploadPopup
	extends DialogBox
{
	private Label mlblError;
	private FormPanel mfrmMain;
	private FileUpload mfupMain;
	private Button mbtnOk;
	private Button mbtnCancel;
	private Button docuShareButton;


	public FileUploadPopup(final DocumentSections.FileNoteSection fileNoteSection)
	{
		super(false, true);
		this.setStyleName("gwt-MenuBarPopup");
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
		docuShareButton = new Button();
		docuShareButton.setText("Ficheiro do DocuShare");
		lhorz.add(docuShareButton);
		lhorz.add(mbtnCancel);
		lvert.add(lhorz);

		setWidget(lvert);

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
				
				hide();
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
				hide();
	        }
		});
		
		docuShareButton.addClickHandler(new ClickHandler() 
		{
			
			public void onClick(ClickEvent event) {
			
//				PopupPanel navigDocShare = new PopupPanel();
//				DocumentNavigationList list = new DocumentNavigationList();
//				navigDocShare.add(list);
//				navigDocShare.setStyleName("gwt-MenuBarPopup");
//				navigDocShare.setVisible(true);
				hide();
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
