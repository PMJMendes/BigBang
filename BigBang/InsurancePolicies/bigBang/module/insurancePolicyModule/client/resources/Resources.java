package bigBang.module.insurancePolicyModule.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	@Source("images/searchIcon1.png")
	ImageResource searchIcon();
	
	@Source("images/managerIcon.png")
	ImageResource managerIcon();
	
	@Source("images/provisional.png")
	ImageResource provisionalPolicyIcon();
	
	@Source("images/active.png")
	ImageResource activePolicyIcon();
	
	@Source("images/inactive.png")
	ImageResource inactivePolicyIcon();
	
	@Source("images/reportIcon.png")
	ImageResource reportIcon();
	
}
