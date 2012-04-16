package bigBang.module.casualtyModule.server;

import java.util.UUID;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.SubCasualtyService;

public class SubCasualtyServiceImpl
	extends SearchServiceBase
	implements SubCasualtyService
{

	private static final long serialVersionUID = 1L;

	@Override
	public SubCasualty getSubCasualty(String subCasualtyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubCasualty editSubCasualty(SubCasualty subCasualty)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubCasualty closeProcess(String subCasualtyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSubCasualty(String subCasualtyId, String reason)
			throws SessionExpiredException, BigBangException {
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
