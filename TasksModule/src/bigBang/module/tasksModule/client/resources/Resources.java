package bigBang.module.tasksModule.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	//@Source("images/validSmall.png")
	//ImageResource validSmallIcon();
	
	@Source("images/invalidSmall.png")
	ImageResource invalidSmallIcon();
	
	@Source("images/completedSmall.png")
	ImageResource completedSmallIcon();
	
	@Source("images/pendingSmall.png")
	ImageResource pendingSmallIcon();
	
	@Source("images/urgentSmall.png")
	ImageResource urgentSmallIcon();

}
