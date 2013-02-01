package bigBang.library.client.userInterface;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.dataAccess.FileTypifiedListBroker;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.interfaces.FileService;

public class FileImportFormViewSection extends FormViewSection {

	protected ExpandableListBoxFormField fileFormat;
	protected FileUpload fileUpload;
	protected FormPanel form;
	protected Button uploadButton;

	protected BigBangAsyncCallback<Void> callback;

	public FileImportFormViewSection(String filter) {
		this("Importação por Ficheiro", filter);
	}

	public FileImportFormViewSection(String title, String filter) {
		super(title);
		fileFormat = new ExpandableListBoxFormField("Formato do ficheiro");
		fileFormat.setTypifiedDataBroker(FileTypifiedListBroker.Util.getInstance());
		fileFormat.allowEdition(false);
		fileFormat.setListId(BigBangConstants.TypifiedListIds.FILE_SPECIFICATIONS + (filter == null ? "" : ("/" + filter)), new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});

		fileUpload = new FileUpload();
		fileUpload.setName("none");
		form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		String action = GWT.getModuleBaseURL() + "bbfile";
		form.setAction(action);
		form.add(fileUpload);

		addFormField(fileFormat);
		addWidget(form);
		uploadButton = new Button("Importar");
		addWidget(uploadButton);

		fileFormat.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fileUpload.setEnabled(event.getValue() != null);
			}
		});

		fileUpload.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				uploadButton.setEnabled(fileUpload.getFilename() != null && !fileUpload.getFilename().isEmpty());
			}
		});
		fileUpload.setEnabled(false);

		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onSubmit();
			}
		});
		uploadButton.setEnabled(false);

		form.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				uploadButton.setEnabled(false);
			}
		});

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				uploadButton.setEnabled(true);
				handleResults(event.getResults());
			}
		});
	}

	protected void onSubmit() {
		this.form.submit();
		callback = new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				return;
			}
		};
	}

	protected void handleResults(String results){
		if(results == null) {
			onError("Não foi possível enviar o ficheiro para processamento.");
		}else{
			String[] tokens = results.split("!");
			if(tokens.length < 1) {
				onError("Não foi possível enviar o ficheiro para processamento.");
			}else if(tokens[0].equalsIgnoreCase("!")){
				onError("Não foi possível enviar o ficheiro para processamento.");
			}else{
				processFileWithStorageId(tokens[0]);
			}
		}
	}

	protected void processFileWithStorageId(String storageId) {
		FileService.Util.getInstance().process(fileFormat.getValue(), storageId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				FileImportFormViewSection.this.onSuccess();
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				onError("Não foi possível importar o ficheiro");
				FileImportFormViewSection.this.clearFields();
				super.onResponseFailure(caught);
			}
		});
	}


	protected void onError(String desc) {
		callback.onFailure(null);
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", desc), Notification.TYPE.ALERT_NOTIFICATION));
	}

	protected void onSuccess(){
		callback.onSuccess(null);
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O ficheiro está a ser processado"), Notification.TYPE.INFO_TRAY_NOTIFICATION));
		clearFields();
	}

	protected void clearFields(){
		NavigationHistoryManager.getInstance().reload();
	} 
}
