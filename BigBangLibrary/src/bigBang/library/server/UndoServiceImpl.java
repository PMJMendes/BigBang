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
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.Constants;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.SysObjects.Operation;
import bigBang.library.interfaces.UndoService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ProcessUndoItem;
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
        FileXfer lrefFile;
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
				lrefFile = (lobjLog.getAt(6) instanceof FileXfer ? (FileXfer)lobjLog.getAt(6) : new FileXfer((byte[])lobjLog.getAt(6)));
				lobjOp = Operation.getOperation(lrefFile);

				lobjAux.id = lobjLog.getKey().toString();
				lobjAux.username = ((IUser)User.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjLog.getAt(3))).getUserName();
				lobjAux.timeStamp = ((Timestamp)lobjLog.getAt(2)).toString();
				lobjAux.shortDescription = lobjOp.ShortDesc();
				lobjAux.description = lobjOp.LongDesc("<br />");
				lobjAux.undoDescription = lobjOp.UndoDesc("<br />");

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
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}
}
