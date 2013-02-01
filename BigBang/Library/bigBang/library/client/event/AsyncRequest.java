package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

import bigBang.definitions.client.response.ResponseError;
import bigBang.library.client.HasParameters;

public abstract class AsyncRequest<T extends Object> extends GwtEvent<AsyncRequestHandler> {

	public static Type<AsyncRequestHandler> TYPE = new Type<AsyncRequestHandler>();
	
	protected HasParameters parameters;
	
	public AsyncRequest(HasParameters parameters){
		this.parameters = parameters;
	}
	
	public HasParameters getParameters(){
		return this.parameters;
	}
	
	public void reply(T response){
		onResponse(response);
	}
	
	public void replyError(ResponseError error) {
		onError(error);
	}
	
	public abstract void onResponse(T response);
	
	public abstract void onError(ResponseError error);

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AsyncRequestHandler> getAssociatedType() {
		return TYPE;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void dispatch(AsyncRequestHandler handler) {
		handler.onRequest((AsyncRequest<Object>) this);
	}
	
}
