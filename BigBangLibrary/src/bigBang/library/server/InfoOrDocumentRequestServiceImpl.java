package bigBang.library.server;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
import bigBang.definitions.shared.InfoOrDocumentRequest.Response;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class InfoOrDocumentRequestServiceImpl
	extends EngineImplementor
	implements InfoOrDocumentRequestService
{
	private static final long serialVersionUID = 1L;

	public InfoOrDocumentRequest repeatRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public InfoOrDocumentRequest receiveResponse(Response response)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public void cancelRequest(Cancellation cancellation)
		throws SessionExpiredException, BigBangException
	{
			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();
	}
}
