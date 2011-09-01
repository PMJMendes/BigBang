package bigBang.module.quoteRequestModule.server;

import bigBang.definitions.client.dataAccess.SearchParameter;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest.Cancellation;
import bigBang.definitions.shared.ClientInfoOrDocumentRequest.Response;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestToManagerTransfer;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SecuredObject;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class QuoteRequestServiceImpl extends RemoteServiceServlet implements QuoteRequestService {

	private static final long serialVersionUID = 1L;

	@Override
	public NewSearchResult openSearch(SearchParameter[] parameters,
			SortParameter[] sorts, int size) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult openForOperation(String opId,
			SearchParameter[] parameters, SortParameter[] sorts, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult search(String workspaceId,
			SearchParameter[] parameters, SortParameter[] sorts, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResult[] getResults(String workspaceId, int from, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeSearch(String workspaceId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuoteRequest getRequest(String requestId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequest createRequest(QuoteRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequest editRequest(QuoteRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRequest(String requestId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public QuoteRequest closeRequest(String requestId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RiskAnalisys createRiskAnalisys(RiskAnalisys riskAnalisys)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SecuredObject insertSecuredObject(SecuredObject object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestToManagerTransfer[] transferToManager(
			String[] quoteRequestIds, String managerId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestToManagerTransfer acceptTransfer(String transferId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequestToManagerTransfer cancelTransfer(String transferId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientInfoOrDocumentRequest createClientInfoOrDocumentRequest(
			ClientInfoOrDocumentRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientInfoOrDocumentRequest repeatClientInfoOrDocumentRequest(
			ClientInfoOrDocumentRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientInfoOrDocumentRequest receiveClientInfoOrDocumentRequestResponse(
			Response response) throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelClientInfoOrDocumentRequest(Cancellation cancellation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

}
