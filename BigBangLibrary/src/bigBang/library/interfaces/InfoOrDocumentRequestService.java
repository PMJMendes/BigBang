package bigBang.library.interfaces;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InfoOrDocumentRequestService")
public interface InfoOrDocumentRequestService
	extends RemoteService
{
	public static class Util
	{
		private static InfoOrDocumentRequestServiceAsync instance;

		public static InfoOrDocumentRequestServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(InfoOrDocumentRequestService.class);

			return instance;
		}
	}

	public InfoOrDocumentRequest repeatRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public InfoOrDocumentRequest receiveResponse(InfoOrDocumentRequest.Response response) throws SessionExpiredException, BigBangException;
	public void cancelRequest(InfoOrDocumentRequest.Cancellation cancellation) throws SessionExpiredException, BigBangException;
}
