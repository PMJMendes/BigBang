package bigBang.library.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExternRequestService")
public interface ExternRequestService
	extends RemoteService
{
	public static class Util
	{
		private static ExternRequestServiceAsync instance;

		public static ExternRequestServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(ExternRequestService.class);

			return instance;
		}
	}

	public ExternRequestService getRequest(String id) throws SessionExpiredException, BigBangException;
	public ExternRequestService repeatRequest(ExternRequestService request) throws SessionExpiredException, BigBangException;
	public ExternRequestService receiveResponse(ExternalInfoRequest.Incoming response) throws SessionExpiredException, BigBangException;
	public void cancelRequest(ExternalInfoRequest.Closing cancellation) throws SessionExpiredException, BigBangException;
}
