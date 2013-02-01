package bigBang.library.client.event;

public interface FiresAsyncRequests {

	void registerRequestHandler(AsyncRequestHandler handler);
	
	void fireRequest(AsyncRequest<?> request);
	
}
