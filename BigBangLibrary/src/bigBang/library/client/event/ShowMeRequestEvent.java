package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowMeRequestEvent extends GwtEvent<ShowMeRequestEventHandler> {

	public static Type<ShowMeRequestEventHandler> TYPE = new Type<ShowMeRequestEventHandler>();
	
	protected Object me;
	
	public ShowMeRequestEvent(Object me){
		this.setSource(me);
		this.me = me;
	}

	public Object getMe(){
		return this.me;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ShowMeRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShowMeRequestEventHandler handler) {
		handler.onShowMeRequest(this);
	}
	
}
