package bigBang.module.casualtyModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.server.TransferManagerServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.interfaces.CasualtyService;
import bigBang.module.casualtyModule.shared.CasualtySearchParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.CasualtyData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.DeleteCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.AcceptXFer;

public class CasualtyServiceImpl
	extends SearchServiceBase
	implements CasualtyService
{
	private static final long serialVersionUID = 1L;

	public static Casualty sGetCasualty(UUID pidCasualty)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		IProcess lobjProcess;
		Client lobjClient;
		Casualty lobjResult;

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(), pidCasualty);
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjCasualty.GetProcessID());
			lobjClient = (Client)lobjProcess.GetParent().GetData();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Casualty();
		lobjResult.id = lobjCasualty.getKey().toString();
		lobjResult.processId = lobjProcess.getKey().toString();
		lobjResult.processNumber = lobjCasualty.getLabel();
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.casualtyDate = ((Timestamp)lobjCasualty.getAt(2)).toString().substring(0, 10);
		lobjResult.caseStudy = (Boolean)lobjCasualty.getAt(5);
		lobjResult.isOpen = lobjProcess.IsRunning();
		lobjResult.description = (String)lobjCasualty.getAt(3);
		lobjResult.internalNotes = (String)lobjCasualty.getAt(4);
		lobjResult.managerId = lobjProcess.GetManagerID().toString();

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProcess.getKey());

		return lobjResult;
	}

	public Casualty getCasualty(String casualtyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetCasualty(UUID.fromString(casualtyId));
	}

	public Casualty editCasualty(Casualty casualty)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(casualty.id));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopMD = new ManageData(lobjCasualty.GetProcessID());
		lopMD.mobjData = new CasualtyData();
		lopMD.mobjData.mid = lobjCasualty.getKey();
		lopMD.mobjData.mstrNumber = casualty.processNumber;
		lopMD.mobjData.mdtCasualtyDate = Timestamp.valueOf(casualty.casualtyDate + " 00:00:00.0");
		lopMD.mobjData.mstrDescription = casualty.description;
		lopMD.mobjData.mstrNotes = casualty.internalNotes;
		lopMD.mobjData.mbCaseStudy = casualty.caseStudy;
		lopMD.mobjData.midProcess = lobjCasualty.GetProcessID();
		lopMD.mobjData.midManager = null;
		lopMD.mobjData.mobjPrevValues = null;
		lopMD.mobjContactOps = null;
		lopMD.mobjDocOps = null;

		try
		{
			lopMD.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetCasualty(lopMD.mobjData.mid);
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		CreateMgrXFer lobjCMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(transfer.dataObjectIds[0]));

			lobjCMX = new CreateMgrXFer(lobjCasualty.GetProcessID());
			lobjCMX.midNewManager = UUID.fromString(transfer.newManagerId);
			lobjCMX.mbMassTransfer = false;

			lobjCMX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		transfer.objectTypeId = Constants.ObjID_Casualty.toString();
		transfer.objectStubs = new SearchResult[] {TransferManagerServiceImpl.sBuildStub(Constants.ObjID_Casualty,
				UUID.fromString(transfer.dataObjectIds[0]))};
		transfer.managedProcessIds = new String[] {lobjCasualty.GetProcessID().toString()};
		transfer.directTransfer = lobjCMX.mbDirectTransfer;
		if ( transfer.directTransfer )
		{
			transfer.id = null;
			transfer.processId = null;
			transfer.status = ManagerTransfer.Status.DIRECT;
		}
		else
		{
			transfer.id = lobjCMX.midTransferObject.toString();
			transfer.processId = lobjCMX.midCreatedSubproc.toString();
			transfer.status = ManagerTransfer.Status.PENDING;
		}

		return transfer;
	}

	public void deleteCasualty(String casualtyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		DeleteCasualty lobjDC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(casualtyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjDC = new DeleteCasualty(lobjCasualty.GetProcessID());
		lobjDC.midCasualty = UUID.fromString(casualtyId);
		lobjDC.mstrReason = reason;

		try
		{
			lobjDC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		UUID lidManager;
		SQLServer ldb;
		MgrXFer lobjXFer;
		IProcess lobjProc;
		UUID [] larrProcessIDs;
		com.premiumminds.BigBang.Jewel.Objects.Casualty lobjCasualty;
		int i;
		IScript lobjScript;
		CreateMgrXFer lobjCMX;
		AcceptXFer lopAX;
		AgendaItem lobjItem;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	if ( transfer.newManagerId == null )
    		throw new BigBangException("Erro: Novo gestor não indicado.");
    	lidManager = UUID.fromString(transfer.newManagerId);

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

		lobjXFer = null;
		lobjProc = null;
		larrProcessIDs = new UUID[transfer.dataObjectIds.length];
		try
		{
			for ( i = 0 ; i < larrProcessIDs.length; i++ )
			{
				lobjCasualty = com.premiumminds.BigBang.Jewel.Objects.Casualty.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(transfer.dataObjectIds[i]));
				larrProcessIDs[i] = lobjCasualty.GetProcessID();
			}

			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjXFer.setAt(1, null);
			lobjXFer.setAt(2, lidManager);
			lobjXFer.setAt(3, "Transferência de Gestor de Sinistro");
			lobjXFer.setAt(4, Engine.getCurrentUser());
			lobjXFer.setAt(5, true);
			lobjXFer.setAt(6, Constants.ObjID_Casualty);
			lobjXFer.SaveToDb(ldb);
			lobjXFer.InitNew(larrProcessIDs, ldb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_MgrXFer);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjXFer.getKey(), null, null, ldb);

			for ( i = 0; i < larrProcessIDs.length; i++ )
			{
				lobjCMX = new CreateMgrXFer(larrProcessIDs[i]);
				lobjCMX.midNewManager = lidManager;
				lobjCMX.mbMassTransfer = true;
				lobjCMX.midTransferObject = lobjXFer.getKey();
				lobjCMX.midCreatedSubproc = lobjProc.getKey();
				lobjCMX.Execute(ldb);
			}

			if ( lidManager.equals(Engine.getCurrentUser()) )
			{
				lopAX = new AcceptXFer(lobjProc.getKey());
				lopAX.Execute(ldb);
			}
			else
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Sinistro");
				lobjItem.setAt(1, Engine.getCurrentUser());
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Valid);
				lobjItem.SaveToDb(ldb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_MgrXFer_CancelXFer}, ldb);

				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Sinistro");
				lobjItem.setAt(1, lidManager);
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(ldb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()},
						new UUID[] {Constants.OPID_MgrXFer_AcceptXFer, Constants.OPID_MgrXFer_CancelXFer}, ldb);
			}
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

		transfer.objectTypeId = Constants.ObjID_Casualty.toString();
		transfer.objectStubs = new SearchResult[larrProcessIDs.length];
		transfer.managedProcessIds = new String[larrProcessIDs.length];
		for ( i = 0; i < larrProcessIDs.length; i++ )
		{
			transfer.objectStubs[i] = TransferManagerServiceImpl.sBuildStub(Constants.ObjID_Casualty,
					UUID.fromString(transfer.dataObjectIds[i]));
			transfer.managedProcessIds[i] = larrProcessIDs[i].toString();
		}
		transfer.directTransfer = lidManager.equals(Engine.getCurrentUser());
		if ( transfer.directTransfer )
		{
			transfer.id = null;
			transfer.processId = null;
			transfer.status = ManagerTransfer.Status.DIRECT;
		}
		else
		{
			transfer.id = lobjXFer.getKey().toString();
			transfer.processId = lobjProc.getKey().toString();
			transfer.status = ManagerTransfer.Status.PENDING;
		}

		return transfer;
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Casualty;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Date]", "[:Case Study]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		CasualtySearchParameter lParam;
		String lstrAux;
		IEntity lrefClients;
        Calendar ldtAux;

		if ( !(pParam instanceof CasualtySearchParameter) )
			return false;
		lParam = (CasualtySearchParameter)pParam;

		if ( !lParam.includeClosed )
		{
			pstrBuffer.append(" AND [:Process:Running] = 1");
		}

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR (LEFT(CONVERT(NVARCHAR, [:Date], 120), 10) LIKE N'%").append(lstrAux).append("%')")
					.append(" OR [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%'))");
		}

		if ( lParam.dateFrom != null )
		{
			pstrBuffer.append(" AND [:Date of Birth] >= '").append(lParam.dateFrom).append("'");
		}

		if ( lParam.dateTo != null )
		{
			pstrBuffer.append(" AND [:Date of Birth] < '");
        	ldtAux = Calendar.getInstance();
        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.dateTo + " 00:00:00.0").getTime());
        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
			pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10)).append("'");
		}

		if ( lParam.caseStudy != null )
		{
			pstrBuffer.append(" AND [:Case Study] = ").append(lParam.caseStudy ? "1" : "0");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		CasualtySortParameter lParam;
		IEntity lrefClients;

		if ( !(pParam instanceof CasualtySortParameter) )
			return false;
		lParam = (CasualtySortParameter)pParam;

		if ( lParam.field == CasualtySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == CasualtySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == CasualtySortParameter.SortableField.DATE )
			pstrBuffer.append("[:Date]");

		if ( lParam.field == CasualtySortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("(SELECT [:Name] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == CasualtySortParameter.SortableField.CLIENT_NUMBER )
		{
			pstrBuffer.append("(SELECT [:Number] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == CasualtySortParameter.SortableField.MANAGER )
			pstrBuffer.append("[:Process:Manager]");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		CasualtyStub lobjResult;
		Client lobjClient;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				lobjClient = (Client)lobjProcess.GetParent().GetData();
			}
			catch (Throwable e)
			{
				lobjClient = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjClient = null;
		}

		lobjResult = new CasualtyStub();

		lobjResult.id = pid.toString();
		lobjResult.processNumber = (String)parrValues[0];
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.casualtyDate = ((Timestamp)parrValues[2]).toString().substring(0, 10);
		lobjResult.caseStudy = (Boolean)parrValues[3];
		lobjResult.isOpen = lobjProcess.IsRunning();
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		CasualtySearchParameter lParam;
		String lstrAux;
		IEntity lrefClients;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof CasualtySearchParameter) )
				continue;
			lParam = (CasualtySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', (SELECT [:Name] FROM (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])) ELSE ")
					.append("CASE WHEN [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%') THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', CAST((SELECT [:Number] FROM (");
			try
			{
				pstrBuffer.append(lrefClients.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent]) AS NVARCHAR(20))) ELSE ")
					.append("0 END END END");
		}

		return lbFound;
	}
}
