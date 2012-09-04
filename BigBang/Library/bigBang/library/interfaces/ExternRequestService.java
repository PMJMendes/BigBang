package bigBang.library.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ExternalInfoRequest.Closing;
import bigBang.definitions.shared.ExternalInfoRequest.Incoming;
import bigBang.definitions.shared.ExternalInfoRequest.Outgoing;
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

	public ExternalInfoRequest getRequest(String id) throws SessionExpiredException, BigBangException;
	public ExternalInfoRequest sendInformation(Outgoing outgoing) throws SessionExpiredException, BigBangException;
	public ExternalInfoRequest receiveAdditional(Incoming incoming) throws SessionExpiredException, BigBangException;
	public void closeRequest(Closing closing) throws SessionExpiredException, BigBangException;
}
