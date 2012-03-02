package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ContentChangedEvent extends GwtEvent<ContentChangedEventHandler>{
	
	public static Type<ContentChangedEventHandler> TYPE = new Type<ContentChangedEventHandler>();
	protected Object object;
	
	public Object getObject(){
		return object;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ContentChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ContentChangedEventHandler handler) {
		handler.onContentChanged();
	}

}
