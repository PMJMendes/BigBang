package bigBang.module.expenseModule.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	@Source("images/searchIcon1.png")
	ImageResource searchIcon();
	
	@Source("images/managerIcon.png")
	ImageResource managerIcon();
	
	@Source("images/active.png")
	ImageResource active();
	
	@Source("images/inactive.png")
	ImageResource inactive();
	
	@Source("images/return.png")
	ImageResource returnToClient();

	@Source("images/insurerNotification.png")
	ImageResource notifyInsurer();
	
	@Source("images/clientNotification.png")
	ImageResource notifyClient();
	
}
