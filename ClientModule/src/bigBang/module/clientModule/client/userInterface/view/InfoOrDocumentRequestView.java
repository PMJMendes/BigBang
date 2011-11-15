package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;

public abstract class InfoOrDocumentRequestView extends View {
	
	protected InfoOrDocumentRequestForm form;
	protected ClientFormView clientForm;
	
	public InfoOrDocumentRequestView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
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
				form.commit();
				onSendButtonPressed();
			}
		}));
		
		VerticalPanel clientFormWrapper = new VerticalPanel();
		clientFormWrapper.setSize("100%", "100%");
		ListHeader clientHeader = new ListHeader("Cliente");
		clientFormWrapper.add(clientHeader);
		clientHeader.setHeight("30px");
		this.clientForm = new ClientFormView();
		clientFormWrapper.add(this.clientForm);
		clientFormWrapper.setCellHeight(this.clientForm, "100%");
		this.clientForm.setReadOnly(true);
		mainWrapper.addWest(clientFormWrapper, 665);
		
		VerticalPanel requestFormWrapper = new VerticalPanel();
		requestFormWrapper.setSize("100%", "100%");
		ListHeader requestHeader = new ListHeader("Pedido");
		requestFormWrapper.add(requestHeader);
		requestHeader.setHeight("30px");
		requestFormWrapper.add(toolbar);
		requestFormWrapper.setCellHeight(toolbar, "21px");
		form = new InfoOrDocumentRequestForm();
		requestFormWrapper.add(form);
		requestFormWrapper.setCellHeight(form, "100%");
		mainWrapper.add(requestFormWrapper);
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