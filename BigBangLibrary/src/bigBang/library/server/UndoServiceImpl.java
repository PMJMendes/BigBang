package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Petri.Constants;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.SysObjects.Operation;
import Jewel.Petri.SysObjects.UndoOperation;
import bigBang.library.interfaces.UndoService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.ProcessUndoItem;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;

public class UndoServiceImpl
	extends EngineImplementor
	implements UndoService
{
	private static final long serialVersionUID = 1L;

	public ProcessUndoItem[] getProcessUndoItems(String processId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<ProcessUndoItem> larrAux;
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefLogs;
        MasterDB ldb;
        ResultSet lrsLogs;
        ProcessUndoItem lobjAux;
        PNLog lobjLog;
        Operation lobjOp;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = new ArrayList<ProcessUndoItem>();

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKProcess_In_Log;
		larrParams = new java.lang.Object[1];
		larrParams[0] = UUID.fromString(processId);

		try
		{
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PNLog)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsLogs = lrefLogs.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			while ( lrsLogs.next() )
			{
				lobjAux = new ProcessUndoItem();
				lobjLog = PNLog.GetInstance(Engine.getCurrentNameSpace(), lrsLogs);
				lobjOp = lobjLog.GetOperationData();

				lobjAux.id = lobjLog.getKey().toString();
				lobjAux.username = ((IUser)User.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjLog.getAt(3))).getUserName();
				lobjAux.timeStamp = ((Timestamp)lobjLog.getAt(2)).toString();
				lobjAux.shortDescription = lobjOp.ShortDesc();
				lobjAux.description = lobjOp.LongDesc("<br />");
				lobjAux.undoDescription = lobjOp.UndoDesc("<br />");
				lobjAux.canUndo = (lobjLog.GetOperation().GetUndoOp() != null);

				larrAux.add(lobjAux);
			}
		}
		catch (Throwable e)
		{
			try { lrsLogs.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsLogs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrAux.toArray(new ProcessUndoItem[larrAux.size()]);
	}
	
	public ProcessUndoItem undo(String undoItemId)
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

	@Override
	public NewSearchResult openSearch(SearchParameter[] parameters, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult openForOperation(String opId,
			SearchParameter[] parameters, int size)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NewSearchResult search(String workspaceId,
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

}
