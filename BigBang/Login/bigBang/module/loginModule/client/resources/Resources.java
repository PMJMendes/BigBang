package bigBang.module.loginModule.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface Resources extends ClientBundle {
	
	public interface LoginCss extends CssResource {
		String background();
		String errorLabel();
	}
	
	Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("css/login.css")
	public LoginCss css();

	@Source("images/background.png")
	@ImageOptions(repeatStyle=RepeatStyle.Both)
	ImageResource background();
	
	@Source("images/logo.png")
	ImageResource logo();
	
	@Source("images/login.png")
	ImageResource login();

}
