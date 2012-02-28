package bigBang.module.quoteRequestModule.server;

import java.util.UUID;

import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.definitions.shared.QuoteRequest.TableSection;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
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
	public TableSection getPage(String requestId, String subLineId,
			String objectId) throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Remap[] openRequestScratchPad(String requestId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequest getRequestInPad(String requestId)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequest updateHeader(QuoteRequest request)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestSubLine addSubLineToPad(String requestId, String subLineId)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSubLineFromPad(String subLineId)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TableSection getPageForEdit(String requestId, String subLineId,
			String objectId) throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableSection savePage(TableSection data)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject getObjectInPad(String objectId)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject createObjectInPad(String requestId)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject createObjectFromClientInPad(String requestId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject updateObjectInPad(InsuredObject data)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteObjectInPad(String objectId)
			throws SessionExpiredException, BigBangException,
			CorruptedPadException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Remap[] commitPad(String requestId) throws SessionExpiredException,
			BigBangException, CorruptedPadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Remap[] discardPad(String requestId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRequest(String requestId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		
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
