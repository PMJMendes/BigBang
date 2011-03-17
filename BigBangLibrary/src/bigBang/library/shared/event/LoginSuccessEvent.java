package bigBang.library.shared.event;

import com.google.gwt.event.shared.GwtEvent;

public class LoginSuccessEvent extends GwtEvent<LoginSuccessEventHandler> {

	public static Type<LoginSuccessEventHandler> TYPE = new Type<LoginSuccessEventHandler>();
	private String username;
	
	public LoginSuccessEvent(String username) {
		this.username = username;
	}
	
	public String getUsername(){
		return username;
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
