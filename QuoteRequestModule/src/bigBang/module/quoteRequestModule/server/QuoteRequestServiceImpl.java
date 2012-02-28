package bigBang.module.quoteRequestModule.server;

import java.util.UUID;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;

public class QuoteRequestServiceImpl
	extends SearchServiceBase 
	implements QuoteRequestService
{
	private static final long serialVersionUID = 1L;

	@Override
	public QuoteRequest getRequest(String requestId)
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
	public RiskAnalysis createRiskAnalisys(RiskAnalysis riskAnalisys)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject insertInsuredObject(InsuredObject object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagerTransfer[] createManagerTransfer(
			String[] quoteRequestIds, String managerId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoOrDocumentRequest createInfoOrDocumentRequest(
			InfoOrDocumentRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Negotiation createNegotiation(Negotiation negotiation)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected UUID getObjectID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean buildFilter(StringBuilder pstrBuffer,
			SearchParameter pParam) throws BigBangException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam,
			SearchParameter[] parrParams) throws BigBangException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected SearchResult buildResult(UUID pid, Object[] parrValues) {
		// TODO Auto-generated method stub
		return null;
	}
}
