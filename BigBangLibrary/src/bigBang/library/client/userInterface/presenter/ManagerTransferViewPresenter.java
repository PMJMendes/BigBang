package bigBang.library.client.userInterface.presenter;

import bigBang.library.client.HasParameters;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ManagerTransferViewPresenter implements ViewPresenter {

	public static interface Display {
		Widget asWidget();
	}
	
	private Display view;
	
	public ManagerTransferViewPresenter(Display view){
		setView((UIObject) view);
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	private void bind(){
		//APPLICATION-WIDE EVENTS
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}

}
