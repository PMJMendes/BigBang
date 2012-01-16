package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import bigBang.library.client.userInterface.presenter.ManagerTransferViewPresenter;

public class ManagerTransferView extends View implements ManagerTransferViewPresenter.Display {

	public ManagerTransferView(){
		SimplePanel wrapper = new SimplePanel();
		initWidget(wrapper);
		
		wrapper.add(new Label("TRANSFERENCIA DE GESTOR"));
	}
	
	@Override
	protected void initializeView() {
		return;
	}

}
