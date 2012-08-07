package bigBang.library.client.userInterface;

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

import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class FileImportFormViewSection extends CollapsibleFormViewSection {

	protected ListBoxFormField fileFormat;
	protected FileUpload fileUpload;
	protected FormPanel form;
	protected Button uploadButton;
	
	public FileImportFormViewSection(String target) {
		super("Importação por Ficheiro");
		fileFormat = new ListBoxFormField("Formato do ficheiro");
		fileFormat.addItem("teste", "teste");
		
		fileUpload = new FileUpload();
		form = new FormPanel(target);
		form.add(fileUpload);
		addFormField(fileFormat, false);
		addWidget(form, false);
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
			}
		});
		
		this.expand();
	}
	
	protected void onSubmit() {
		this.form.submit();
	}

}
