package bigBang.library.interfaces;

import bigBang.definitions.shared.SearchResult;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ExactItemSubService
	extends RemoteService
{
	SearchResult[] getExactResults(String label) throws SessionExpiredException, BigBangException;
}
