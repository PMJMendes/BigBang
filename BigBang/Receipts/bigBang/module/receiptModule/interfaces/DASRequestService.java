package bigBang.module.receiptModule.interfaces;

import bigBang.definitions.shared.DASRequest;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DASRequestService")
public interface DASRequestService
	extends RemoteService
{
	public static class Util
	{
		private static DASRequestServiceAsync instance;

		public static DASRequestServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(DASRequestService.class);

			return instance;
		}
	}

	public DASRequest getRequest(String id) throws SessionExpiredException, BigBangException;
	public DASRequest repeatRequest(DASRequest request) throws SessionExpiredException, BigBangException;
	public DASRequest receiveResponse(DASRequest.Response response) throws SessionExpiredException, BigBangException;
	public void cancelRequest(DASRequest.Cancellation cancellation) throws SessionExpiredException, BigBangException;
}
