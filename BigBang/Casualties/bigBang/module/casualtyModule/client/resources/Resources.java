package bigBang.module.casualtyModule.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	@Source("images/searchIcon1.png")
	ImageResource searchIcon();

	@Source("images/active.png")
	ImageResource activeCasualtyIcon();
	
	@Source("images/inactive.png")
	ImageResource inactiveCasualtyIcon();
	
	
}
