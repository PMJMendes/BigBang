package bigBang.library.server;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Petri.Constants;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.SysObjects.UndoOperation;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.interfaces.HistoryService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SessionExpiredException;
import bigBang.library.shared.SortParameter;

public class HistoryServiceImpl
	extends SearchServiceBase
	implements HistoryService
{
	private static final long serialVersionUID = 1L;

//				lobjAux.id = lobjLog.getKey().toString();
//				lobjAux.username = ((IUser)User.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjLog.getAt(3))).getUserName();
//				lobjAux.timeStamp = ((Timestamp)lobjLog.getAt(2)).toString();
//				lobjAux.shortDescription = lobjOp.ShortDesc();
//				lobjAux.description = lobjOp.LongDesc("<br />");
//				lobjAux.undoDescription = lobjOp.UndoDesc("<br />");
//				lobjAux.canUndo = (lobjLog.GetOperation().GetUndoOp() != null);
	
	public HistoryItem undo(String undoItemId)
		throws SessionExpiredException, BigBangException
	{
        PNLog lobjLog;
        UndoOperation lobjRunnable;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(undoItemId));
			lobjRunnable = (UndoOperation)lobjLog.GetOperation().GetUndoOp().GetNewInstance(lobjLog.GetProcessID());
			lobjRunnable.mobjSourceOp = lobjLog.GetOperationData();
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
		return new String[] {"[:Name]", "[:Number]", "[:Group:Name]"};
	}

	@Override
	protected void buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
			throws BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buildSort(StringBuilder pstrBuffer, SortParameter pParam,
			SearchParameter[] parrParams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected SearchResult buildResult(UUID pid, Object[] parrValues) {
		// TODO Auto-generated method stub
		return null;
	}

}
