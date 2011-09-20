package bigBang.module.tasksModule.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import bigBang.definitions.client.dataAccess.SearchParameter;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.Task;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.tasksModule.interfaces.TasksService;
import bigBang.module.tasksModule.shared.TaskSearchParameter;

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
		UUID[] larrAux;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lid = UUID.fromString(taskId);
		try
		{
			lobjAgenda = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), lid);
			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgenda.getAt(2));
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
		lobjResult.urgencyId = ((UUID)lobjAgenda.getAt(5)).toString();
		lobjResult.processTypeId = lobjScript.getKey().toString();
		lobjResult.objectTypeId = lobjScript.GetDataType().toString();

		larrAux = lobjAgenda.GetProcessIDs();
		lobjResult.processIds = new String[larrAux.length];
		lobjResult.objectIds = new String[larrAux.length];
		for ( i = 0; i < larrAux.length; i++ )
		{
			lobjResult.processIds[i] = larrAux[i].toString();
			try
			{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrAux[i]);
				lobjResult.objectIds[i] = lobjProcess.GetData().getKey().toString();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}

		larrAux = lobjAgenda.GetOperationIDs();
		lobjResult.operationIds = new String[larrAux.length];
		for ( i = 0; i < larrAux.length; i++ )
			lobjResult.operationIds[i] = larrAux[i].toString();

		return null;
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
		int[] larrMembers;
		java.lang.Object[] larrValues;
		IEntity lrefProcs;

		if ( !(pParam instanceof TaskSearchParameter) )
			return false;
		lParam = (TaskSearchParameter)pParam;

		pstrBuffer.append(" AND [:User] = '").append(Engine.getCurrentUser().toString()).append("'");

		if ( lParam.processId != null )
		{
			pstrBuffer.append("[PK] IN (SELECT [:Item] FROM (");
			larrMembers = new int[1];
			larrMembers[0] = 1;
			larrValues = new java.lang.Object[1];
			larrValues[0] = UUID.fromString(lParam.processId);
			try
			{
				lrefProcs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
				pstrBuffer.append(lrefProcs.SQLForSelectByMembers(larrMembers, larrValues, null));
			}
			catch (Throwable e)
			{
	    		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [Aux2])");
		}

		if ( lParam.operationId != null )
		{
			pstrBuffer.append("[PK] IN (SELECT [:Item] FROM (");
			larrMembers = new int[1];
			larrMembers[0] = 1;
			larrValues = new java.lang.Object[1];
			larrValues[0] = UUID.fromString(lParam.operationId);
			try
			{
				lrefProcs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaOp));
				pstrBuffer.append(lrefProcs.SQLForSelectByMembers(larrMembers, larrValues, null));
			}
			catch (Throwable e)
			{
	    		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [Aux3])");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
	{
//		TaskSortParameter lParam;

//		if ( !(pParam instanceof TaskSortParameter) )
			return false;
//		lParam = (TaskSortParameter)pParam;

//		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		TaskStub lobjResult;

		lobjResult = new TaskStub();
		lobjResult.id = pid.toString();
		lobjResult.description = (String)parrValues[0];
		lobjResult.timeStamp = ((Timestamp)parrValues[1]).toString();
		lobjResult.dueDate = ((Timestamp)parrValues[2]).toString();
		lobjResult.urgencyId = ((UUID)parrValues[3]).toString();

		return lobjResult;
	}
}
