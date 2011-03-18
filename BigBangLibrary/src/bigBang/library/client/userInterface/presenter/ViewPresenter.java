package bigBang.library.client.userInterface.presenter;

import bigBang.library.client.EventBus;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.ui.HasWidgets;

public interface ViewPresenter {	
	
	public void setService(Service service);
	
	public void setEventBus(EventBus eventBus);
	
	public void setView(View view);
	
	public void go(final HasWidgets container);
	
	public void bind();

	public void registerEventHandlers(EventBus eventBus);
	
}
