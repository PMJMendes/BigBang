package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class DeleteRequestEvent extends GwtEvent<DeleteRequestEventHandler>{

	public static Type<DeleteRequestEventHandler> TYPE = new Type<DeleteRequestEventHandler>();
	protected Object object;
	
	public Object getObject() {
		return object;
	}

	public DeleteRequestEvent(Object object){
		
		this.object = object;
		
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DeleteRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DeleteRequestEventHandler handler) {
		handler.onDeleteRequest(this.object);
		
	}
	
	

}
