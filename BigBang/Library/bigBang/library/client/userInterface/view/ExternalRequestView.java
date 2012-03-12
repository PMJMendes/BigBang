package bigBang.library.client.userInterface.view;

import bigBang.library.client.userInterface.presenter.ExternalRequestViewPresenter;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public abstract class ExternalRequestView<T> extends View implements ExternalRequestViewPresenter.Display{
	
	public ExternalRequestView(FormView<T> ownerForm){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");	
	}
	
	
}
