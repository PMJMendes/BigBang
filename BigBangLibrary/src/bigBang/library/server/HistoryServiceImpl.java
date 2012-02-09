package bigBang.library.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Petri.Constants;
import Jewel.Petri.Interfaces.ILog;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.UndoOperation;
import Jewel.Petri.SysObjects.UndoableOperation;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
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
		IProcess lobjOther;
		IScript lobjScript;
		UUID lidData;

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

			if ( lobjLog.GetExternalProcess() == null )
			{
				lobjOther = null;
				lobjScript = null;
			}
			else
			{
				lobjOther = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjLog.GetExternalProcess());
				lobjScript = lobjOther.GetScript();
			}
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
		else if ( lobjLog.IsUndone() )
			lobjResult.undoDescription = "Esta operação já foi revertida.";
		else
			lobjResult.undoDescription = "N/A";

		if ( lobjOther == null )
		{
			lobjResult.otherObjectTypeId = null;
			lobjResult.otherObjectId = null;
		}
		else
		{
			lobjResult.otherObjectTypeId = lobjScript.GetDataType().toString();
			lidData = lobjOther.GetDataKey();
			lobjResult.otherObjectId = ( lidData == null ? null : lidData.toString() );
		}

		return lobjResult;
	}

	public HistoryItem undo(String undoItemId)
		throws SessionExpiredException, BigBangException
	{
        PNLog lobjLog;
        Operation lobjAux;
        UndoOperation lobjRunnable;
        Operation lobjData;
		ILog lobjNewLog;
		HistoryItem lobjResult;
		IProcess lobjOther;
		IScript lobjScript;
		UUID lidData;
		UndoableOperation.UndoSet[] larrAux;
		int i, j;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(undoItemId));
			if ( lobjLog.GetOperation().GetUndoOp() == null )
				throw new BigBangException("Erro: A operação não define uma operação correspondente para a reversão.");

			lobjAux = lobjLog.GetOperation().GetUndoOp().GetNewInstance(lobjLog.GetProcessID());
			if ( !(lobjAux instanceof UndoOperation) )
				throw new BigBangException("Erro: A operação de reversão está mal definida.");

			lobjData = lobjLog.GetOperationData();
			if ( !(lobjData instanceof UndoableOperation) )
				throw new BigBangException("Erro: A operação não define métodos para reversão.");

			lobjRunnable = (UndoOperation)lobjAux;
			lobjRunnable.midSourceLog = lobjLog.getKey();
			lobjRunnable.midNameSpace = lobjLog.getNameSpace();

			lobjRunnable.Execute(lobjLog.getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjNewLog = lobjRunnable.getLog();

		lobjResult = new HistoryItem();
		lobjResult.id = lobjNewLog.getKey().toString();

		try
		{
			lobjResult.username = lobjNewLog.GetUser().getUserName();
			lobjResult.canUndo = lobjNewLog.CanUndo();

			if ( lobjNewLog.GetExternalProcess() == null )
			{
				lobjOther = null;
				lobjScript = null;
			}
			else
			{
				lobjOther = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjNewLog.GetExternalProcess());
				lobjScript = lobjOther.GetScript();
			}

		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult.timeStamp = lobjNewLog.GetTimestamp().toString();
		lobjResult.shortDescription = lobjRunnable.ShortDesc();
		lobjResult.description = lobjRunnable.LongDesc("<br />");
		lobjResult.undoDescription = "N/A";

		if ( lobjOther == null )
		{
			lobjResult.otherObjectTypeId = null;
			lobjResult.otherObjectId = null;
		}
		else
		{
			lobjResult.otherObjectTypeId = lobjScript.GetDataType().toString();
			lidData = lobjOther.GetDataKey();
			lobjResult.otherObjectId = ( lidData == null ? null : lidData.toString() );
		}

		larrAux = ((UndoableOperation)lobjData).GetSets();

		lobjResult.alteredEntities = new HistoryItem.AlteredItem[larrAux.length];

		for ( i = 0; i < larrAux.length; i++ )
		{
			lobjResult.alteredEntities[i] = new HistoryItem.AlteredItem();
			lobjResult.alteredEntities[i].typeId = larrAux[i].midType.toString();

			if ( larrAux[i].marrCreated != null )
			{
				lobjResult.alteredEntities[i].createdIds = new String[larrAux[i].marrCreated.length];
				for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
					lobjResult.alteredEntities[i].createdIds[j] = larrAux[i].marrCreated[j].toString();
			}

			if ( larrAux[i].marrChanged != null )
			{
				lobjResult.alteredEntities[i].modifiedIds = new String[larrAux[i].marrChanged.length];
				for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
					lobjResult.alteredEntities[i].modifiedIds[j] = larrAux[i].marrChanged[j].toString();
			}

			if ( larrAux[i].marrDeleted != null )
			{
				lobjResult.alteredEntities[i].deletedIds = new String[larrAux[i].marrDeleted.length];
				for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
					lobjResult.alteredEntities[i].deletedIds[j] = larrAux[i].marrDeleted[j].toString();
			}
		}
		return lobjResult;
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

		if ( (lParam.processId == null) && (lParam.dataObjectId == null) )
			throw new BigBangException("Attempt to search through History with a NULL Process ID or Data Object ID.");

		if ( lParam.dataObjectId != null )
			pstrBuffer.append(" AND [:Process:Data] = '").append(lParam.dataObjectId).append("'");
		else
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
