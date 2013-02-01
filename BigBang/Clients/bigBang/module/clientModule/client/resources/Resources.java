package bigBang.module.clientModule.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface Resources extends ClientBundle {

	@Source("images/searchIcon1.png")
	ImageResource searchIcon();
	
	@Source("images/clientManagerIcon1.png")
	ImageResource clientManagerIcon();
	
	@Source("images/verticalColumnBackground1.png")
	@ImageOptions(repeatStyle=RepeatStyle.Vertical)
	ImageResource verticalColumnBackground1();
	
	@Source("images/reportIcon.png")
	ImageResource reportIcon();

}
