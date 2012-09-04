package bigBang.library.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.ProcessData;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.interfaces.TransferManagerService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;

public class TransferManagerServiceImpl
	extends EngineImplementor
	implements TransferManagerService
{
	private static final long serialVersionUID = 1L;

	public static ManagerTransfer sGetTransfer(UUID pidTransfer)
		throws BigBangException
	{
		MgrXFer lobjXFer;
		ManagerTransfer lobjResult;
		UUID[] larrProcesses;
		int i;
		IProcess lobjProcess;

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), pidTransfer);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new ManagerTransfer();

		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		try
		{
			lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		lobjResult.processId = lobjXFer.GetProcessID().toString();

		larrProcesses = lobjXFer.GetProcessIDs();
		lobjResult.dataObjectIds = new String[larrProcesses.length];
		lobjResult.objectStubs = new SearchResult[larrProcesses.length];
		for ( i = 0; i < larrProcesses.length; i++ )
		{
			try
			{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcesses[i]);
				lobjResult.objectStubs[i] = BuildStub(lobjXFer.GetOuterObjectType(), lobjProcess.GetDataKey());
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjResult.dataObjectIds[i] = lobjProcess.GetDataKey().toString();
		}

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjXFer.GetProcessID());

		return lobjResult;
	}

	public static ManagerTransfer sCreateMassTransfer(ManagerTransfer transfer, UUID pidScript)
		throws BigBangException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		UUID lidManager;
		UUID lidEntity;
		SQLServer ldb;
		MgrXFer lobjXFer;
		UUID [] larrProcessIDs;
		ProcessData lobjTarget;
		int i;
		IScript lobjScript;

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	if ( transfer.newManagerId == null )
    		throw new BigBangException("Erro: Novo gestor não indicado.");
    	lidManager = UUID.fromString(transfer.newManagerId);

		try
		{
			lidEntity = Engine.FindEntity(Engine.getCurrentNameSpace(), UUID.fromString(transfer.objectTypeId));
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
		larrProcessIDs = new UUID[transfer.dataObjectIds.length];
		try
		{
			for ( i = 0 ; i < larrProcessIDs.length; i++ )
			{
				lobjTarget = (ProcessData)Engine.GetWorkInstance(lidEntity, UUID.fromString(transfer.dataObjectIds[i]));
				larrProcessIDs[i] = lobjTarget.GetProcessID();
			}

			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjXFer.setAt(MgrXFer.I.NEWMANAGER, lidManager);
			lobjXFer.setAt(MgrXFer.I.SCRIPT, pidScript);
			lobjXFer.SaveToDb(ldb);
			lobjXFer.InitNew(larrProcessIDs, ldb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_MgrXFer);
			lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjXFer.getKey(), null, null, ldb);
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

		return sGetTransfer(lobjXFer.getKey());
	}

	public ManagerTransfer getTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetTransfer(UUID.fromString(transferId));
	}

	public ManagerTransfer acceptTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
//		MgrXFer lobjXFer;
//		UUID lidProc;
//		ExecXFer lobjAX;
//		UUID[] larrProcs;
//		ManagerTransfer lobjResult;
//		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		throw new BigBangException("Deprecated.");

//		try
//		{
//			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
//			lidProc = lobjXFer.GetProcessID();
//
//			lobjAX = new ExecXFer(lidProc);
//			lobjAX.Execute();
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangException(e.getMessage(), e);
//		}
//
//		if ( lobjXFer.IsMassTransfer() )
//			larrProcs = lobjXFer.GetProcessIDs();
//		else
//			larrProcs = new UUID[] {lobjAX.midParentProc};
//
//		lobjResult = new ManagerTransfer();
//		lobjResult.id = lobjXFer.getKey().toString();
//		lobjResult.managedProcessIds = new String[larrProcs.length];
//		lobjResult.dataObjectIds = new String[larrProcs.length];
//		for ( i = 0; i < larrProcs.length; i++ )
//		{
//			lobjResult.managedProcessIds[i] = larrProcs[i].toString();
//			try
//			{
//				lobjResult.dataObjectIds[i] = PNProcess.GetInstance(Engine.getCurrentNameSpace(),
//							larrProcs[i]).GetData().getKey().toString();
//			}
//			catch (Throwable e)
//			{
//				lobjResult.dataObjectIds[i] = null;
//			}
//		}
//		lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
//		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
//		lobjResult.processId = lidProc.toString();
//		lobjResult.status = ManagerTransfer.Status.ACCEPTED;
//
//		return lobjResult;
	}

	public ManagerTransfer cancelTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
//		MgrXFer lobjXFer;
//		UUID lidProc;
//		CancelXFer lobjCX;
//		UUID[] larrProcs;
//		ManagerTransfer lobjResult;
//		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		throw new BigBangException("Deprecated.");

//		try
//		{
//			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
//			lidProc = lobjXFer.GetProcessID();
//
//			lobjCX = new CancelXFer(lidProc);
//			lobjCX.Execute();
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangException(e.getMessage(), e);
//		}
//
//		if ( lobjXFer.IsMassTransfer() )
//			larrProcs = lobjXFer.GetProcessIDs();
//		else
//			larrProcs = new UUID[] {lobjCX.midParentProc};
//
//		lobjResult = new ManagerTransfer();
//		lobjResult.id = lobjXFer.getKey().toString();
//		lobjResult.managedProcessIds = new String[larrProcs.length];
//		lobjResult.dataObjectIds = new String[larrProcs.length];
//		for ( i = 0; i < larrProcs.length; i++ )
//		{
//			lobjResult.managedProcessIds[i] = larrProcs[i].toString();
//			try
//			{
//				lobjResult.dataObjectIds[i] = PNProcess.GetInstance(Engine.getCurrentNameSpace(),
//							larrProcs[i]).GetData().getKey().toString();
//			}
//			catch (Throwable e)
//			{
//				lobjResult.dataObjectIds[i] = null;
//			}
//		}
//		lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
//		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
//		lobjResult.processId = lidProc.toString();
//		lobjResult.status = ManagerTransfer.Status.CANCELED;
//
//		return lobjResult;
	}

	private static SearchResult BuildStub(UUID pidType, UUID pidKey)
		throws BigBangException
	{
		ObjectBase lobjAux;

		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), pidType), pidKey);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( lobjAux instanceof Client )
			return BuildClientStub((Client)lobjAux);

		if ( lobjAux instanceof QuoteRequest )
			return BuildQuoteRequestStub((QuoteRequest)lobjAux);

		if ( lobjAux instanceof Policy )
			return BuildPolicyStub((Policy)lobjAux);

		if ( lobjAux instanceof Casualty )
			return BuildCasualtyStub((Casualty)lobjAux);

		throw new BigBangException("Erro: Objecto não suporta transferências de gestor.");
	}

	private static ClientStub BuildClientStub(Client pobjClient)
	{
		ClientStub lobjResult;

		lobjResult = new ClientStub();

		lobjResult.id = pobjClient.getKey().toString();
		lobjResult.name = pobjClient.getLabel();
		lobjResult.clientNumber = ((Integer)pobjClient.getAt(1)).toString();
		if ( pobjClient.getAt(10) == null )
			lobjResult.groupName = null;
		else
		{
			try
			{
				lobjResult.groupName = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), (UUID)pobjClient.getAt(10)).getLabel();
			}
			catch (BigBangJewelException e)
			{
				lobjResult.groupName = "(Erro a obter o nome do grupo.)";
			}
		}
		lobjResult.processId = pobjClient.GetProcessID().toString();

		return lobjResult;
	}

	private static QuoteRequestStub BuildQuoteRequestStub(QuoteRequest pobjRequest)
	{
		IProcess lobjProcess;
		QuoteRequestStub lobjResult;
		Client lobjClient;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), pobjRequest.GetProcessID());
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

		lobjResult = new QuoteRequestStub();

		lobjResult.id = pobjRequest.getKey().toString();
		lobjResult.processNumber = pobjRequest.getLabel();
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.caseStudy = (Boolean)pobjRequest.getAt(4);
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		lobjResult.isOpen = (lobjProcess == null ? false : lobjProcess.IsRunning());
		return lobjResult;
	}

	private static InsurancePolicyStub BuildPolicyStub(Policy pobjPolicy)
	{
		InsurancePolicyStub lobjResult;
		IProcess lobjProcess;
		Client lobjAuxClient;
		SubLine lobjSubLine;
		Line lobjLine;
		Category lobjCategory;
		ObjectBase lobjStatus;
		String lstrObject;

		lstrObject = "(Erro a obter as unidades de risco)";
		lobjProcess = null;
		lobjAuxClient = null;
		lobjAuxClient = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), pobjPolicy.GetProcessID());
			try
			{
				lobjAuxClient = (Client)lobjProcess.GetParent().GetData();
			}
			catch (Throwable e)
			{
			}
			lstrObject = pobjPolicy.GetObjectFootprint();
		}
		catch (Throwable e)
		{
		}

		lobjSubLine = pobjPolicy.GetSubLine();
		lobjLine = lobjSubLine.getLine();
		lobjCategory = lobjLine.getCategory();

		try
		{
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
					(UUID)pobjPolicy.getAt(13));
		}
		catch (Throwable e)
		{
			lobjStatus = null;
		}

		lobjResult = new InsurancePolicyStub();

		lobjResult.id = pobjPolicy.getKey().toString();
		lobjResult.number = pobjPolicy.getLabel();
		lobjResult.clientId = (lobjAuxClient == null ? null : lobjAuxClient.getKey().toString());
		lobjResult.clientNumber = (lobjAuxClient == null ? "" : ((Integer)lobjAuxClient.getAt(1)).toString());
		lobjResult.clientName = (lobjAuxClient == null ? "(Erro a obter o nome do cliente.)" : lobjAuxClient.getLabel());
		lobjResult.categoryId = lobjCategory.getKey().toString();
		lobjResult.categoryName = lobjCategory.getLabel();
		lobjResult.lineId = lobjLine.getKey().toString();
		lobjResult.lineName = lobjLine.getLabel();
		lobjResult.subLineId = lobjSubLine.getKey().toString();
		lobjResult.subLineName = lobjSubLine.getLabel();
		lobjResult.insuredObject = lstrObject;
		lobjResult.caseStudy = (Boolean)pobjPolicy.getAt(12);
		lobjResult.statusId = ((UUID)pobjPolicy.getAt(13)).toString();
		lobjResult.statusText = (lobjStatus == null ? "(Erro a obter o estado.)" : lobjStatus.getLabel());
		if ( lobjStatus != null )
		{
			switch ( (Integer)lobjStatus.getAt(1) )
			{
			case 0:
				lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
				break;

			case 1:
				lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
				break;

			case 2:
				lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
				break;
			}
		}
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());

		return lobjResult;
	}

	private static CasualtyStub BuildCasualtyStub(Casualty pobjCasualty)
	{
		IProcess lobjProcess;
		Client lobjAuxClient;
		CasualtyStub lobjResult;
		com.premiumminds.BigBang.Jewel.Objects.SubCasualty lobjSub;
		String lstrCat;
		String lstrObj;

		lobjProcess = null;
		lobjAuxClient = null;
		lstrCat = null;
		lstrObj = null;
		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), pobjCasualty.GetProcessID());
			try
			{
				lobjAuxClient = (Client)lobjProcess.GetParent().GetData();
			}
			catch (Throwable e)
			{
			}
			lobjSub = ((com.premiumminds.BigBang.Jewel.Objects.Casualty)lobjProcess.GetData()).GetFirstSubCasualty();
			if ( lobjSub != null )
			{
				lstrCat = lobjSub.GetSubLine().getLabel();
				lstrObj = lobjSub.GetObjectName();
			}
		}
		catch (Throwable e)
		{
		}

		lobjResult = new CasualtyStub();

		lobjResult.id = pobjCasualty.getKey().toString();
		lobjResult.processNumber = pobjCasualty.getLabel();
		lobjResult.clientId = (lobjAuxClient == null ? null : lobjAuxClient.getKey().toString());
		lobjResult.clientNumber = (lobjAuxClient == null ? "" : ((Integer)lobjAuxClient.getAt(1)).toString());
		lobjResult.clientName = (lobjAuxClient == null ? "(Erro)" : lobjAuxClient.getLabel());
		lobjResult.casualtyDate = ((Timestamp)pobjCasualty.getAt(2)).toString().substring(0, 10);
		lobjResult.caseStudy = (Boolean)pobjCasualty.getAt(5);
		lobjResult.isOpen = lobjProcess.IsRunning();
		lobjResult.policyCategory = lstrCat;
		lobjResult.insuredObject = lstrObj;
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());

		return lobjResult;
	}
}
