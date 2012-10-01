package bigBang.module.tasksModule.server;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.tasksModule.interfaces.TasksService;
import bigBang.module.tasksModule.shared.TaskSearchParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;

public class TasksServiceImpl
	extends SearchServiceBase
	implements TasksService
{
	private static final long serialVersionUID = 1L;

	public Task getTask(String taskId)
		throws SessionExpiredException, BigBangException
	{
		UUID lid;
		AgendaItem lobjAgenda;
		IScript lobjScript;
		IProcess lobjProcess;
		Task lobjResult;
		UUID[] larrProcs;
		UUID[] larrOps;
		UUID lidAgendaProcs;
		UUID lidAgendaOps;
		ObjectBase lobjAux;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lid = UUID.fromString(taskId);
		try
		{
			lobjAgenda = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), lid);
			larrProcs = lobjAgenda.GetAgendaProcIDs();
			larrOps = lobjAgenda.GetAgendaOpIDs();
			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgenda.getAt(2));
			lidAgendaProcs = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess);
			lidAgendaOps = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaOp);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Task();

		lobjResult.id = lid.toString();
		lobjResult.description = (String)lobjAgenda.getAt(0);
		lobjResult.timeStamp = ((Timestamp)lobjAgenda.getAt(3)).toString();
		lobjResult.dueDate = ((Timestamp)lobjAgenda.getAt(4)).toString();
		if(((UUID)lobjAgenda.getAt(5)).compareTo(Constants.UrgID_Invalid) == 0)
			lobjResult.status = TaskStub.Status.INVALID;
		if(((UUID)lobjAgenda.getAt(5)).compareTo(Constants.UrgID_Valid) == 0)
			lobjResult.status = TaskStub.Status.VALID;
		if(((UUID)lobjAgenda.getAt(5)).compareTo(Constants.UrgID_Pending) == 0)
			lobjResult.status = TaskStub.Status.PENDING;
		if(((UUID)lobjAgenda.getAt(5)).compareTo(Constants.UrgID_Urgent) == 0)
			lobjResult.status = TaskStub.Status.URGENT;
		if(((UUID)lobjAgenda.getAt(5)).compareTo(Constants.UrgID_Completed) == 0)
			lobjResult.status = TaskStub.Status.COMPLETED;
		lobjResult.longDesc = (String)lobjAgenda.getAt(6);
		lobjResult.reportId = ( lobjAgenda.getAt(8) == null ? null : ((UUID)lobjAgenda.getAt(8)).toString() );
		lobjResult.params = ( lobjAgenda.getAt(7) == null ? null : ((String)lobjAgenda.getAt(7)).split("\\|") );
		lobjResult.processTypeId = lobjScript.getKey().toString();
		lobjResult.objectTypeId = lobjScript.GetDataType().toString();

		lobjResult.processIds = new String[larrProcs.length];
		lobjResult.objectIds = new String[larrProcs.length];
		for ( i = 0; i < larrProcs.length; i++ )
		{
			try
			{
				lobjAux = Engine.GetWorkInstance(lidAgendaProcs, larrProcs[i]);
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAux.getAt(1));
				lobjResult.objectIds[i] = ( lobjProcess.GetDataKey() == null ? null : lobjProcess.GetData().getKey().toString() );
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjResult.processIds[i] = lobjProcess.getKey().toString();
		}

		lobjResult.operationIds = new String[larrOps.length];
		for ( i = 0; i < larrOps.length; i++ )
		{
			try
			{
				lobjAux = Engine.GetWorkInstance(lidAgendaOps, larrOps[i]);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjResult.operationIds[i] = ((UUID)lobjAux.getAt(1)).toString();
		}

		return lobjResult;
	}

	public void dismissTask(String taskId)
		throws SessionExpiredException, BigBangException
	{
		AgendaItem lobjAgenda;
		UUID[] larrAux;
		MasterDB ldb;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAgenda = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(taskId));
			larrAux = lobjAgenda.GetAgendaOpIDs();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( larrAux.length > 0 )
			throw new BigBangException("Erro: Não pode cancelar items de agenda com operações associadas.");

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjAgenda.ClearData(ldb);
			lobjAgenda.getDefinition().Delete(ldb, lobjAgenda.getKey());
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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
	}

	public void reassignTask(String taskId, String userId)
		throws SessionExpiredException, BigBangException
	{
		AgendaItem lobjAgenda;
		MasterDB ldb;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjAgenda = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(taskId));
			lobjAgenda.setAt(1, UUID.fromString(userId));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lobjAgenda.SaveToDb(ldb);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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
	}

	public int getPendingTasksCount()
		throws SessionExpiredException, BigBangException
	{
        MasterDB ldb;
        ResultSet lrsItems;
        int llngResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

        try
        {
	        lrsItems = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaItem))
	        		.SelectByMembers(ldb, new int[] {1}, new java.lang.Object[] {Engine.getCurrentUser()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		llngResult = 0;
		try
		{
	        while (lrsItems.next())
	        	llngResult++;
        }
        catch (Throwable e)
        {
			try { lrsItems.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangException(e.getMessage(), e);
        }

        try
        {
        	lrsItems.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return llngResult;
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_AgendaItem;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Description]", "[:Timestamp]", "[:Due Date]", "[:Level]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		TaskSearchParameter lParam;
		IEntity lrefDecos;
		IEntity lrefProcs;

		if ( !(pParam instanceof TaskSearchParameter) )
			return false;
		lParam = (TaskSearchParameter)pParam;

		pstrBuffer.append(" AND ([:User] = '").append(Engine.getCurrentUser().toString()).append("'");
		pstrBuffer.append(" OR [:User] IN (SELECT [:User] FROM (");
		try
		{
			lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
			pstrBuffer.append(lrefDecos.SQLForSelectSingle());
		}
		catch (Throwable e)
		{
    		throw new BigBangException(e.getMessage(), e);
		}
		pstrBuffer.append(") [AuxDel] WHERE [:Surrogate] = '").append(Engine.getCurrentUser().toString()).append("'))");

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			pstrBuffer.append(" AND [:Description] LIKE N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%'");
		}

		if ( lParam.operationId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Item] FROM (");
			try
			{
				lrefProcs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaOp));
				pstrBuffer.append(lrefProcs.SQLForSelectMultiFiltered(new int[] {1},
						new java.lang.Object[] {UUID.fromString(lParam.operationId)}));
			}
			catch (Throwable e)
			{
	    		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [Aux3])");
		}
		else if ( lParam.processId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Item] FROM (");
			try
			{
				lrefProcs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
				pstrBuffer.append(lrefProcs.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
	    		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [Aux2] WHERE [:Process:Script] = '").append(lParam.processId).append("')");
		}
		
		if ( lParam.afterTimestamp != null )
		{
			pstrBuffer.append(" AND [:Timestamp] > '").append(lParam.afterTimestamp).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
	{
		TaskSortParameter lParam;

		if ( !(pParam instanceof TaskSortParameter) )
			return false;
		lParam = (TaskSortParameter)pParam;

		if ( lParam.field == TaskSortParameter.SortableField.TAG )
			pstrBuffer.append("[:Description]");

		if ( lParam.field == TaskSortParameter.SortableField.STATUS )
			pstrBuffer.append("[:Level:Level]");

		if ( lParam.field == TaskSortParameter.SortableField.CREATION_DATE )
			pstrBuffer.append("[:Timestamp]");

		if ( lParam.field == TaskSortParameter.SortableField.DUE_DATE )
			pstrBuffer.append("[:Due Date]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		TaskStub lobjResult;

		lobjResult = new TaskStub();
		lobjResult.id = pid.toString();
		lobjResult.description = (String)parrValues[0];
		lobjResult.timeStamp = ((Timestamp)parrValues[1]).toString();
		lobjResult.dueDate = ((Timestamp)parrValues[2]).toString();
		if(((UUID)parrValues[3]).compareTo(Constants.UrgID_Invalid) == 0)
			lobjResult.status = TaskStub.Status.INVALID;
		if(((UUID)parrValues[3]).compareTo(Constants.UrgID_Valid) == 0)
			lobjResult.status = TaskStub.Status.VALID;
		if(((UUID)parrValues[3]).compareTo(Constants.UrgID_Pending) == 0)
			lobjResult.status = TaskStub.Status.PENDING;
		if(((UUID)parrValues[3]).compareTo(Constants.UrgID_Urgent) == 0)
			lobjResult.status = TaskStub.Status.URGENT;
		if(((UUID)parrValues[3]).compareTo(Constants.UrgID_Completed) == 0)
			lobjResult.status = TaskStub.Status.COMPLETED;

		return lobjResult;
	}
}
