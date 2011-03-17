package bigBang.library.shared.userInterface.presenter;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Service;
import bigBang.library.shared.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;

public interface ViewPresenter {	
	
	public void setService(Service service);
	
	public void setEventBus(EventBus eventBus);
	
	public void setView(View view);
	
	public void go(final HasWidgets container);
	
	public void bind();

	public void registerEventHandlers(EventBus eventBus);
	
}
