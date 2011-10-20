package bigBang.module.insurancePolicyModule.server;

import bigBang.definitions.client.dataAccess.SearchParameter;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class InsurancePolicyServiceImpl extends RemoteServiceServlet implements InsurancePolicyService {

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
	public InsurancePolicy getPolicy(String policyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsurancePolicy voidPolicy(String policyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePolicy(String policyId) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InsurancePolicy editPolicy(InsurancePolicy policy)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

}
