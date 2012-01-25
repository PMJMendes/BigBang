package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Constants;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

public class BigBangProcessServiceImpl
	extends EngineImplementor
	implements BigBangProcessService
{
	private static final long serialVersionUID = 1L;

	public static IProcess sGetProcessFromDataObject(UUID pidDataObject)
		throws BigBangException
	{
		IEntity lrefProcess;
        MasterDB ldb;
        ResultSet lrsProcesses;
		IProcess lobjProcess;

        try
		{
			lrefProcess = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PNProcess)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsProcesses = lrefProcess.SelectByMembers(ldb, new int[] {Constants.FKData_In_Process},
					new java.lang.Object[] {pidDataObject}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			if ( lrsProcesses.next() )
			{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lrsProcesses);

				if ( lrsProcesses.next() )
					throw new BigBangException("Unexpected: More than one process for data object.");
			}
			else
				throw new BigBangException("Erro: Esse objecto não tem processo associado.");
		}
		catch (BigBangException e)
		{
			try { lrsProcesses.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsProcesses.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsProcesses.close();
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

		return lobjProcess;
	}

	public BigBangProcess getProcess(String dataObjectId)
		throws SessionExpiredException, BigBangException
	{
        IProcess lobjProcess;
        IScript lobjScript;
        IProcess lobjParent;
        IScript lobjSuper;
        
        BigBangProcess lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjProcess = sGetProcessFromDataObject(UUID.fromString(dataObjectId));
		try
		{
			lobjScript = lobjProcess.GetScript();
			lobjParent = lobjProcess.GetParent();
			lobjSuper = lobjParent.GetScript();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new BigBangProcess();
		lobjResult.dataTypeId = lobjScript.GetDataType().toString();
		lobjResult.dataId = lobjProcess.GetDataKey().toString();
		lobjResult.processTypeId = lobjScript.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.tag = lobjScript.getLabel();
		lobjResult.ownerDataId = lobjParent.GetDataKey().toString();
		lobjResult.ownerDataTypeId = lobjSuper.GetDataType().toString();
		lobjResult.ownerProcId = lobjParent.getKey().toString();
		lobjResult.ownerProcTypeId = lobjParent.GetScriptID().toString();

		return lobjResult;
	}

	public BigBangProcess[] getSubProcesses(String dataObjectId)
		throws SessionExpiredException, BigBangException
	{
		IProcess lobjParent;
		IEntity lrefProcess;
        MasterDB ldb;
        ResultSet lrsProcesses;
		ArrayList<BigBangProcess> larrAux;
        IProcess lobjProcess;
        IScript lobjScript;
        UUID lidData;
        BigBangProcess lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjParent = sGetProcessFromDataObject(UUID.fromString(dataObjectId));

		try
		{
			lrefProcess = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PNProcess)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsProcesses = lrefProcess.SelectByMembers(ldb, new int[] {Constants.FKParent_In_Process},
					new java.lang.Object[] {lobjParent.getKey()}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		larrAux = new ArrayList<BigBangProcess>();

		try
		{
			while ( lrsProcesses.next() )
			{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lrsProcesses);
				if ( !lobjProcess.IsRunning() )
					continue;
				lobjScript = lobjProcess.GetScript();
				if ( lobjScript.IsTopLevel() )
					continue;
				lidData = lobjProcess.GetDataKey();

				lobjResult = new BigBangProcess();
				lobjResult.dataTypeId = lobjScript.GetDataType().toString();
				lobjResult.dataId = ( lidData == null ? null : lidData.toString() );
				lobjResult.processTypeId = lobjScript.getKey().toString();
				lobjResult.processId = lobjProcess.getKey().toString();
				lobjResult.tag = lobjScript.getLabel();
				lobjResult.ownerDataId = lobjParent.GetDataKey().toString();
				lobjResult.ownerDataTypeId = lobjParent.GetScript().GetDataType().toString();
				lobjResult.ownerProcId = lobjParent.getKey().toString();
				lobjResult.ownerProcTypeId = lobjParent.GetScriptID().toString();
				larrAux.add(lobjResult);
			}
		}
		catch (Throwable e)
		{
			try { lrsProcesses.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangException(e.getMessage(), e);
		}

		try
		{
			lrsProcesses.close();
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

		return larrAux.toArray(new BigBangProcess[larrAux.size()]);
	}
}
