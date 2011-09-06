package bigBang.definitions.client.response;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A handler for a response that will be received asynchronously.
 * 
 * @author Francisco Cabrita @ Premium Minds Lda.
 * @param <T> The type of the response.
 */
public abstract class ResponseHandler<T> {
	
	/**
	 * Called when the response has been received.
	 * @param response the response
	 */
	public abstract void onResponse(T response);
	
	/**
	 * Called if there was an error while waiting for the response
	 * @param errors the collection of ResponseError objects
	 */
	public abstract void onError(Collection<ResponseError> errors);
	
	public void onError(String[] errorStrings){
		Collection<ResponseError> errors = new ArrayList<ResponseError>();
		for(int i = 0; i < errorStrings.length; i++){
			ResponseError error = new ResponseError();
			error.description = errorStrings[i];
			errors.add(error);
		}
	}

}
