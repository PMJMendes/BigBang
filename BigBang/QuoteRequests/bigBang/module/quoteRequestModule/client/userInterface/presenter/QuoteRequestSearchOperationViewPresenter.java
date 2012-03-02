package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestSearchOperationViewPresenter implements ViewPresenter {

	public interface Display {
		Widget asWidget();
	}
	
	protected Display view;
	protected boolean bound;
	
	public QuoteRequestSearchOperationViewPresenter(View view) {
		setView(view);
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
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}

	public void bind() {
		if(bound)
			return;
		
		//APPLICATION-WIDE EVENTS
	}

}
