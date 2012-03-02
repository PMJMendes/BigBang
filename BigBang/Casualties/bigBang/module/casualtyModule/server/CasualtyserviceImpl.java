package bigBang.module.casualtyModule.server;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.CasualtyService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CasualtyserviceImpl extends RemoteServiceServlet implements CasualtyService {

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

}
