package bigBang.library.server;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.interfaces.TransferManagerService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.AcceptXFer;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.CancelXFer;

public class TransferManagerServiceImpl
	extends EngineImplementor
	implements TransferManagerService
{
	private static final long serialVersionUID = 1L;

	public static SearchResult sBuildStub(UUID pidType, UUID pidKey)
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

			if ( lobjAux instanceof Policy )
				return BuildPolicyStub((Policy)lobjAux);

			throw new BigBangException("Erro: Objecto não suporta transferências de gestor.");
		}

	public ManagerTransfer getTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		ManagerTransfer lobjResult;
		UUID[] larrProcesses;
		int i;
		IProcess lobjProcess;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new ManagerTransfer();

		lobjResult.id = lobjXFer.getKey().toString();
//		lobjResult.directTransfer;
//		lobjResult.status;
		lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.isMassTransfer = lobjXFer.IsMassTransfer();
		lobjResult.processId = lobjXFer.GetProcessID().toString();

		if ( lobjResult.isMassTransfer )
		{
			larrProcesses = lobjXFer.GetProcessIDs();
			lobjResult.managedProcessIds = new String[larrProcesses.length];
			lobjResult.dataObjectIds = new String[larrProcesses.length];
			lobjResult.objectStubs = new SearchResult[larrProcesses.length];
			for ( i = 0; i < larrProcesses.length; i++ )
			{
				lobjResult.managedProcessIds[i] = larrProcesses[i].toString();
				try
				{
					lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), larrProcesses[i]);
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
				lobjResult.dataObjectIds[i] = lobjProcess.GetDataKey().toString();
				lobjResult.objectStubs[i] = sBuildStub(lobjXFer.GetOuterObjectType(), lobjProcess.GetDataKey());
			}
		}
		else
		{
			try
			{
				lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjXFer.GetProcessID()).GetParent();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjResult.managedProcessIds = new String[] {lobjProcess.getKey().toString()};
			lobjResult.dataObjectIds = new String[] {lobjProcess.GetDataKey().toString()};
			lobjResult.objectStubs = new SearchResult[] {sBuildStub(lobjXFer.GetOuterObjectType(), lobjProcess.GetDataKey())};
		}

		return lobjResult;
	}

	public ManagerTransfer acceptTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		AcceptXFer lobjAX;
		ManagerTransfer lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjAX = new AcceptXFer(lidProc);
			lobjAX.mbMassTransfer = false;
			lobjAX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[] {lobjAX.midParentProc.toString()};
		try
		{
			lobjResult.dataObjectIds = new String[] {PNProcess.GetInstance(Engine.getCurrentNameSpace(),
					lobjAX.midParentProc).GetData().getKey().toString()};
		}
		catch (JewelPetriException e)
		{
			lobjResult.dataObjectIds = new String[] {null};
		}
		lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.ACCEPTED;

		return lobjResult;
	}

	public ManagerTransfer cancelTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		CancelXFer lobjCX;
		ManagerTransfer lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjCX = new CancelXFer(lidProc);
			lobjCX.mbMassTransfer = false;
			lobjCX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[] {lobjCX.midParentProc.toString()};
		try
		{
			lobjResult.dataObjectIds = new String[] {PNProcess.GetInstance(Engine.getCurrentNameSpace(),
					lobjCX.midParentProc).GetData().getKey().toString()};
		}
		catch (JewelPetriException e)
		{
			lobjResult.dataObjectIds = new String[] {null};
		}
		lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.CANCELED;

		return lobjResult;
	}

	public ManagerTransfer massAcceptTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		AcceptXFer lobjAX;
		ManagerTransfer lobjResult;
		UUID[] larrProcs;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjAX = new AcceptXFer(lidProc);
			lobjAX.mbMassTransfer = true;
			lobjAX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrProcs = lobjXFer.GetProcessIDs();

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[larrProcs.length];
		lobjResult.dataObjectIds = new String[larrProcs.length];
		for ( i = 0; i < larrProcs.length; i++ )
		{
			lobjResult.managedProcessIds[i] = larrProcs[i].toString();
			try
			{
				lobjResult.dataObjectIds[i] = PNProcess.GetInstance(Engine.getCurrentNameSpace(),
							larrProcs[i]).GetData().getKey().toString();
			}
			catch (Throwable e)
			{
				lobjResult.dataObjectIds[i] = null;
			}
		}
		lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.ACCEPTED;

		return lobjResult;
	}

	public ManagerTransfer massCancelTransfer(String transferId)
		throws SessionExpiredException, BigBangException
	{
		MgrXFer lobjXFer;
		UUID lidProc;
		CancelXFer lobjCX;
		ManagerTransfer lobjResult;
		UUID[] larrProcs;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transferId));
			lidProc = lobjXFer.GetProcessID();

			lobjCX = new CancelXFer(lidProc);
			lobjCX.mbMassTransfer = true;
			lobjCX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		larrProcs = lobjXFer.GetProcessIDs();

		lobjResult = new ManagerTransfer();
		lobjResult.id = lobjXFer.getKey().toString();
		lobjResult.managedProcessIds = new String[larrProcs.length];
		lobjResult.dataObjectIds = new String[larrProcs.length];
		for ( i = 0; i < larrProcs.length; i++ )
		{
			lobjResult.managedProcessIds[i] = larrProcs[i].toString();
			try
			{
				lobjResult.dataObjectIds[i] = PNProcess.GetInstance(Engine.getCurrentNameSpace(),
							larrProcs[i]).GetData().getKey().toString();
			}
			catch (Throwable e)
			{
				lobjResult.dataObjectIds[i] = null;
			}
		}
		lobjResult.objectTypeId = lobjXFer.GetOuterObjectType().toString();
		lobjResult.newManagerId = lobjXFer.GetNewManagerID().toString();
		lobjResult.processId = lidProc.toString();
		lobjResult.status = ManagerTransfer.Status.CANCELED;

		return lobjResult;
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

	private static InsurancePolicyStub BuildPolicyStub(Policy pobjPolicy)
	{
		InsurancePolicyStub lobjResult;

		IProcess lobjProcess;
		Client lobjAuxClient;
		SubLine lobjSubline;
		Line lobjLine;
		Category lobjCategory;
		ObjectBase lobjStatus;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), pobjPolicy.GetProcessID());
			try
			{
				lobjAuxClient = (Client)lobjProcess.GetParent().GetData();
			}
			catch (Throwable e)
			{
				lobjAuxClient = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjAuxClient = null;
		}

		try
		{
			lobjSubline = SubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)pobjPolicy.getAt(3));
			try
			{
				lobjLine = Line.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubline.getAt(1));
				try
				{
					lobjCategory = Category.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjLine.getAt(1));
				}
				catch (BigBangJewelException e)
				{
					lobjCategory = null;
				}
			}
			catch (Throwable e1)
			{
				lobjLine = null;
				lobjCategory = null;
			}
		}
		catch (Throwable e)
		{
			lobjSubline = null;
			lobjLine = null;
			lobjCategory = null;
		}

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
		lobjResult.categoryId = (lobjCategory == null ? null : lobjCategory.getKey().toString());
		lobjResult.categoryName = (lobjCategory == null ? "(Erro a obter o nome da categoria.)" : lobjCategory.getLabel());
		lobjResult.lineId = (lobjLine == null ? null : lobjLine.getKey().toString());
		lobjResult.lineName = (lobjLine == null ? "(Erro a obter o nome do ramo.)" : lobjLine.getLabel());
		lobjResult.subLineId = ((UUID)pobjPolicy.getAt(3)).toString();
		lobjResult.subLineName = (lobjSubline == null ? "(Erro a obter o nome da modalidade.)" : lobjSubline.getLabel());
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
}
