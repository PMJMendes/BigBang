package bigBang.module.expenseModule.server;

import java.util.UUID;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.module.expenseModule.interfaces.ExpenseService;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExpenseServiceImpl
	extends SearchServiceBase
	implements ExpenseService
{
	private static final long serialVersionUID = 1L;

	protected UUID getObjectID()
	{
		return Constants.ObjID_Expense;
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
