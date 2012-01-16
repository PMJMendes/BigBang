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
import bigBang.library.interfaces.BigBangProcessService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.BigBangProcess;
import bigBang.library.shared.SessionExpiredException;

public class BigBangProcessServiceImpl
	extends EngineImplementor
	implements BigBangProcessService
{
	private static final long serialVersionUID = 1L;

	public BigBangProcess[] getSubProcesses(String parentProcessId)
		throws SessionExpiredException, BigBangException
	{
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
					new java.lang.Object[] {UUID.fromString(parentProcessId)}, new int[0]);
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
