package bigBang.module.receiptModule.interfaces;

import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SignatureRequestService")
public interface SignatureRequestService
	extends RemoteService
{
	public static class Util
	{
		private static SignatureRequestServiceAsync instance;

		public static SignatureRequestServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(SignatureRequestService.class);

			return instance;
		}
	}

	public SignatureRequest getRequest(String id) throws SessionExpiredException, BigBangException;
	public SignatureRequest repeatRequest(SignatureRequest request) throws SessionExpiredException, BigBangException;
	public SignatureRequest receiveResponse(SignatureRequest.Response response) throws SessionExpiredException, BigBangException;
	public void cancelRequest(SignatureRequest.Cancellation cancellation) throws SessionExpiredException, BigBangException;
}
