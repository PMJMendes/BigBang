package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SessionExpiredEvent extends GwtEvent<SessionExpiredEventHandler> {
	
	public static Type<SessionExpiredEventHandler> TYPE = new Type<SessionExpiredEventHandler>();
	
	@Override
	public Type<SessionExpiredEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SessionExpiredEventHandler handler) {
		handler.onSessionExpired();
	}

}
