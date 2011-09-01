package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.view.View;

public abstract class InfoOrDocumentRequestView extends View {
	
	protected InfoOrDocumentRequestForm form;
	
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
		mainWrapper.add(new ClientFormView());
		mainWrapper.setWidgetMinSize(wrapper, 665);
		
		form = new InfoOrDocumentRequestForm();
		wrapper.add(form);
		
		initWidget(mainWrapper);
	}
	
	public abstract void onSendButtonPressed();

	public abstract void onBackButtonPressed();
}