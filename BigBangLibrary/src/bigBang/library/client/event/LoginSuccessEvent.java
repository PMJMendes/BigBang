package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginSuccessEvent extends GwtEvent<LoginSuccessEventHandler> {

	public static Type<LoginSuccessEventHandler> TYPE = new Type<LoginSuccessEventHandler>();
	private String username;
	private String domain;
	
	public LoginSuccessEvent(String username, String domain) {
		this.username = username;
		this.domain = domain;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getDomain(){
		return domain;
	}
	
	@Override
	public Type<LoginSuccessEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginSuccessEventHandler handler) {
		handler.onLoginSuccess(this);
	}

}
