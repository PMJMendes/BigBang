package bigBang.library.shared;

import bigBang.library.shared.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.userInterface.view.View;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public interface Operation extends Identifiable {
	
	public void init();
	
	public String getId();
	
	public AbstractImagePrototype getIcon();
	
	public String getDescription();
	
	public String getShortDescription();
	
	public String getOwnerProcessId();
	
}
