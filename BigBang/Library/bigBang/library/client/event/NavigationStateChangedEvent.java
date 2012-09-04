package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.UIObject;

public class NavigationStateChangedEvent extends GwtEvent<NavigationStateChangedEventHandler>{

	public static Type<NavigationStateChangedEventHandler> TYPE = new Type<NavigationStateChangedEventHandler>();
	protected UIObject object;
	
	
	public NavigationStateChangedEvent(UIObject object){
		this.object = object;
	}
	
	public Object getObject(){
		return object;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NavigationStateChangedEventHandler> getAssociatedType() {
		return TYPE;
	}
	

	@Override
	protected void dispatch(NavigationStateChangedEventHandler handler) {
		handler.onNavigationStateChanged(this);	
	}

}
