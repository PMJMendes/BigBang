package bigBang.library.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Petri.Constants;
import Jewel.Petri.Interfaces.ILog;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.UndoOperation;
import Jewel.Petri.SysObjects.UndoableOperation;
import bigBang.definitions.client.dataAccess.SearchParameter;
import bigBang.definitions.client.dataAccess.SortOrder;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.interfaces.HistoryService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.HistorySearchParameter;
import bigBang.library.shared.HistorySortParameter;
import bigBang.library.shared.SessionExpiredException;

public class HistoryServiceImpl
	extends SearchServiceBase
	implements HistoryService
{
	private static final long serialVersionUID = 1L;

	public HistoryItem getItem(String undoItemId)
		throws SessionExpiredException, BigBangException
	{
		UUID lid;
		ILog lobjLog;
		Operation lobjOp;
		HistoryItem lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lid = UUID.fromString(undoItemId);

		lobjResult = new HistoryItem();
		lobjResult.id = undoItemId;

		try
		{
			lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lid);
			lobjOp = lobjLog.GetOperationData();

			lobjResult.username = lobjLog.GetUser().getUserName();
			lobjResult.canUndo = lobjLog.CanUndo();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult.timeStamp = lobjLog.GetTimestamp().toString();
		lobjResult.shortDescription = lobjOp.ShortDesc();
		lobjResult.description = lobjOp.LongDesc("<br />");

		if ( lobjResult.canUndo )
			lobjResult.undoDescription = ((UndoableOperation)lobjOp).UndoDesc("<br />");
		else
			lobjResult.undoDescription = "N/A";

		return lobjResult;
	}

	public HistoryItem undo(String undoItemId)
		throws SessionExpiredException, BigBangException
	{
        PNLog lobjLog;
        Operation lobjAux;
        UndoOperation lobjRunnable;
        Operation lobjData;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(undoItemId));
			if ( lobjLog.GetOperation().GetUndoOp() == null )
				throw new BigBangException("Error: Operation does not define a corresponding undo operation.");

			lobjAux = lobjLog.GetOperation().GetUndoOp().GetNewInstance(lobjLog.GetProcessID());
			if ( !(lobjAux instanceof UndoOperation) )
				throw new BigBangException("Error: Undo Operation has an improper definition.");

			lobjData = lobjLog.GetOperationData();
			if ( !(lobjData instanceof UndoableOperation) )
				throw new BigBangException("Error: Operation does not define undo methods.");

			lobjRunnable = (UndoOperation)lobjAux;
			lobjRunnable.midSourceLog = lobjLog.getKey();
			lobjRunnable.midNameSpace = lobjLog.getNameSpace();
			lobjRunnable.mobjSourceOp = (UndoableOperation)lobjData;
			lobjRunnable.mrefLog = lobjLog;

			lobjRunnable.Execute(lobjLog.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return null;
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_PNLog;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:User:Username]", "[:Timestamp]", "[:Operation:Name]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		HistorySearchParameter lParam;

		if ( !(pParam instanceof HistorySearchParameter) )
			return false;
		lParam = (HistorySearchParameter)pParam;

		if ( lParam.processId == null )
			throw new BigBangException("Attempt to search through History with a NULL Process ID.");
		pstrBuffer.append(" AND [:Process] = '").append(lParam.processId).append("'");

		if ( lParam.afterTimestamp != null )
		{
			pstrBuffer.append(" AND [:Timestamp] > '").append(lParam.afterTimestamp).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
	{
		HistorySortParameter lParam;

		if ( !(pParam instanceof HistorySortParameter) )
			return false;
		lParam = (HistorySortParameter)pParam;

		if ( lParam.field == HistorySortParameter.SortableField.TIMESTAMP )
			pstrBuffer.append("[:Timestamp]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		HistoryItemStub lobjResult;

		lobjResult = new HistoryItemStub();
		lobjResult.id = pid.toString();
		lobjResult.username = (String)parrValues[0];
		lobjResult.timeStamp = ((Timestamp)parrValues[1]).toString();
		lobjResult.opName = (String)parrValues[2];

		return lobjResult;
	}
}
