package bigBang.module.quoteRequestModule.server;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class QuoteRequestServiceImpl extends RemoteServiceServlet implements QuoteRequestService {

	private static final long serialVersionUID = 1L;

	@Override
	public NewSearchResult openSearch(SearchParameter[] parameters, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResult[] search(String workspaceId,
			SearchParameter[] parameters, int size)
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
	public NewSearchResult openForOperation(String opId,
			SearchParameter[] parameters, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}
}
