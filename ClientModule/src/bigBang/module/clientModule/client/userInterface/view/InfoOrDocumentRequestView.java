package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.view.View;

public abstract class InfoOrDocumentRequestView extends View {
	
	protected InfoOrDocumentRequestForm form;
	protected ClientFormView clientForm;
	
	public InfoOrDocumentRequestView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		BigBangOperationsToolBar toolbar = new BigBangOperationsToolBar() {
			
			@Override
			public void onSaveRequest() {}
			
			@Override
			public void onEditRequest() {}
			
			@Override
			public void onCancelRequest() {}
		};
		toolbar.hideAll();
		
		toolbar.addItem(new MenuItem("Enviar Pedido", new Command() {
			
			@Override
			public void execute() {
				onSendButtonPressed();
			}
		}));

		wrapper.add(toolbar);
		wrapper.setCellHeight(toolbar, "21px");
		
		mainWrapper.addWest(wrapper, 665);
		this.clientForm = new ClientFormView();
		mainWrapper.add(clientForm);
		this.clientForm.setReadOnly(true);
		mainWrapper.setWidgetMinSize(wrapper, 665);
		
		form = new InfoOrDocumentRequestForm();
		wrapper.add(form);
		
		initWidget(mainWrapper);
	}
	
	public void setClient(Client client){
		clientForm.setValue(client);
	}
	
	public void setRequest(InfoOrDocumentRequest request) {
		this.form.setValue(request);
	}
	
	public abstract void onSendButtonPressed();

	public abstract void onBackButtonPressed();
}