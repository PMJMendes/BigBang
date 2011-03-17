package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import bigBang.library.shared.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeOperationViewPresenter;

public class ClientMergeOperationView extends View implements ClientMergeOperationViewPresenter.Display {
	
	private SimplePanel view;
	
	public ClientMergeOperationView(){
		this.view = new SimplePanel();
		view.add(new Label("teste"));
		
		initWidget(this.view);
	}

	public View getInstance() {
		return new ClientMergeOperationView();
	}

}
