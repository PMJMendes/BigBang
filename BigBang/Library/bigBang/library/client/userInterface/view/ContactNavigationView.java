package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.presenter.ContactNavigationViewPresenter;

public class ContactNavigationView extends NavigationPanel implements ContactNavigationViewPresenter.Display{

	public ContactNavigationView(){
		setSize("680px", "680px");
	}

	@Override
	public void setHomeWidget(UIObject view) {
		super.setHomeWidget((Widget) view);

	}

	@Override
	public void navigateTo(Widget w){
		super.navigateTo(w);
	}

	@Override
	public HasWidgets getNextContainer(){
		SimplePanel panel = new SimplePanel();
		panel.setSize("680px", "680px");
		return panel;
	}

}
