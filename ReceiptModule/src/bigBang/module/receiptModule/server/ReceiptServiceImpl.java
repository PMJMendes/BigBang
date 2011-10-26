package bigBang.module.receiptModule.server;

import java.util.UUID;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.module.receiptModule.interfaces.ReceiptService;

public class ReceiptServiceImpl
	extends SearchServiceBase
	implements ReceiptService
{
	private static final long serialVersionUID = 1L;

	protected UUID getObjectID()
	{
		return null;
	}

	protected String[] getColumns()
	{
		return null;
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		return false;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		return false;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		return null;
	}
}
