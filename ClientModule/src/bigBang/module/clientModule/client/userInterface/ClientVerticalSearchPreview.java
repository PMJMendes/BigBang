package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.view.ClientFormView;

public class ClientVerticalSearchPreview extends View {
	
	protected ClientSearchPanel searchPanel;
	protected ClientFormView form;
	
	public ClientVerticalSearchPreview(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new ClientSearchPanel();
		form = new ClientFormView();
		form.setReadOnly(true);
		
		mainWrapper.addNorth(searchPanel, 400);
		mainWrapper.add(form);
		
		initWidget(mainWrapper);
	}
}
