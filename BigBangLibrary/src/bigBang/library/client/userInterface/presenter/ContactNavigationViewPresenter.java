package bigBang.library.client.userInterface.presenter;


import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ContactNavigationViewPresenter implements ViewPresenter{

	private Display view;
	private boolean bound;
	
	public ContactNavigationViewPresenter(Display view){
		
		setView((UIObject)view);
		
	}
	
	public interface Display{
		
		Widget asWidget();
		void setHomeWidget(UIObject view);
		HasWidgets getNextContainer();
		
	}
	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
		
	}

	@Override
	public void go(HasWidgets container) {
		
		bind();
		bound = true;
		container.clear();
		container.add(this.view.asWidget());
		
	}

	private void bind() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		
		ContactViewPresenter presenter = (ContactViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("SINGLE_CONTACT");
		HasWidgets container = view.getNextContainer();
		presenter.go(container);
		view.setHomeWidget((UIObject) container);
		presenter.setParameters(parameterHolder);
		presenter.registerActionHandler(new ActionInvokedEventHandler<ContactViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				
				
				
			}
		});
		
	}

}
