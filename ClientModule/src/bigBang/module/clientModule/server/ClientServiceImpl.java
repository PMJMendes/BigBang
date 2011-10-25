package bigBang.module.clientModule.server;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.ZipCodeBridge;
import com.premiumminds.BigBang.Jewel.Data.ClientData;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Objects.MgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Client.CreateMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.CreatePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Client.DeleteClient;
import com.premiumminds.BigBang.Jewel.Operations.Client.ManageClientData;
import com.premiumminds.BigBang.Jewel.Operations.Client.MergeWithOther;
import com.premiumminds.BigBang.Jewel.Operations.General.CreateClient;

public class ClientServiceImpl
	extends SearchServiceBase
	implements ClientService
{
	private static final long serialVersionUID = 1L;

	public Client getClient(String clientId)
		throws SessionExpiredException, BigBangException
	{
		UUID lid;
		com.premiumminds.BigBang.Jewel.Objects.Client lobjClient;
		ObjectBase lobjZipCode;
		Client lobjResult;
		IProcess lobjProc;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lid = UUID.fromString(clientId);
		try
		{
			lobjClient = com.premiumminds.BigBang.Jewel.Objects.Client.GetInstance(Engine.getCurrentNameSpace(), lid);
			if ( lobjClient.GetProcessID() == null )
				throw new BigBangException("Erro: Cliente sem processo de suporte. (Cliente n. "
						+ lobjClient.getAt(1).toString() + ")");
			if ( lobjClient.getAt(4) == null )
				lobjZipCode = null;
			else
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjClient.getAt(4));
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjClient.GetProcessID());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Client();

		lobjResult.id = lid.toString();
		lobjResult.name = (String)lobjClient.getAt(0);
		lobjResult.clientNumber = lobjClient.getAt(1).toString();
		lobjResult.groupName = null;
		lobjResult.groupId = (lobjClient.getAt(10) == null ? null : lobjClient.getAt(10).toString());
		lobjResult.taxNumber = (String)lobjClient.getAt(5);
		lobjResult.address = new Address();
		lobjResult.address.street1 = (String)lobjClient.getAt(2);
		lobjResult.address.street2 = (String)lobjClient.getAt(3);
		if ( lobjZipCode == null )
			lobjResult.address.zipCode = null;
		else
		{
			lobjResult.address.zipCode = new ZipCode();
			lobjResult.address.zipCode.code = (String)lobjZipCode.getAt(0);
			lobjResult.address.zipCode.city = (String)lobjZipCode.getAt(1);
			lobjResult.address.zipCode.county = (String)lobjZipCode.getAt(2);
			lobjResult.address.zipCode.district = (String)lobjZipCode.getAt(3);
			lobjResult.address.zipCode.country = (String)lobjZipCode.getAt(4);
		}
		lobjResult.NIB = (String)lobjClient.getAt(11);
		lobjResult.typeId = lobjClient.getAt(6).toString();
		lobjResult.subtypeId = (lobjClient.getAt(7) == null ? null : lobjClient.getAt(7).toString());
		lobjResult.mediatorId = lobjClient.getAt(8).toString();
		lobjResult.operationalProfileId = lobjClient.getAt(9).toString();
		lobjResult.caeId = (lobjClient.getAt(16) == null ? null : lobjClient.getAt(16).toString());
		lobjResult.activityNotes = (String)lobjClient.getAt(17);
		lobjResult.sizeId = (lobjClient.getAt(18) == null ? null : lobjClient.getAt(18).toString());
		lobjResult.revenueId = (lobjClient.getAt(19) == null ? null : lobjClient.getAt(19).toString());
		lobjResult.birthDate = (lobjClient.getAt(12) == null ? null : ((Timestamp)lobjClient.getAt(12)).toString());
		lobjResult.genderId = (lobjClient.getAt(13) == null ? null : lobjClient.getAt(13).toString());
		lobjResult.maritalStatusId = (lobjClient.getAt(14) == null ? null : lobjClient.getAt(14).toString());
		lobjResult.professionId = (lobjClient.getAt(15) == null ? null : lobjClient.getAt(15).toString());
		lobjResult.notes = (String)lobjClient.getAt(20);

		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.managerId = lobjProc.GetManagerID().toString();

		return lobjResult;
	}

	public Client createClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		CreateClient lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopCC = new CreateClient(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lopCC.mobjData = new ClientData();

			lopCC.mobjData.mid = null;
			lopCC.mobjData.mlngNumber = 0;
			lopCC.mobjData.midProcess = null;
			lopCC.mobjData.mobjPrevValues = null;

			lopCC.mobjData.mstrName = client.name;
			if ( client.address != null )
			{
				lopCC.mobjData.mstrAddress1 = client.address.street1;
				lopCC.mobjData.mstrAddress2 = client.address.street2;
				if ( client.address.zipCode != null )
					lopCC.mobjData.midZipCode = ZipCodeBridge.GetZipCode(client.address.zipCode.code,
							client.address.zipCode.city, client.address.zipCode.county, client.address.zipCode.district,
							client.address.zipCode.country);
				else
					lopCC.mobjData.midZipCode = null;
			}
			else
			{
				lopCC.mobjData.mstrAddress1 = null;
				lopCC.mobjData.mstrAddress2 = null;
				lopCC.mobjData.midZipCode = null;
			}
			lopCC.mobjData.mstrFiscal = client.taxNumber;
			lopCC.mobjData.midType = ( client.typeId == null ? null : UUID.fromString(client.typeId) );
			lopCC.mobjData.midSubtype = ( client.subtypeId == null ? null : UUID.fromString(client.subtypeId) );
			lopCC.mobjData.midManager = null; //( client.managerId == null ? Engine.getCurrentUser() : UUID.fromString(client.managerId) );
			lopCC.mobjData.midMediator = ( client.mediatorId == null ? null : UUID.fromString(client.mediatorId) );
			lopCC.mobjData.midProfile = ( client.operationalProfileId == null ? null : UUID.fromString(client.operationalProfileId) );
			lopCC.mobjData.midGroup = ( client.groupId == null ? null : UUID.fromString(client.groupId) );
			lopCC.mobjData.mstrBankingID = client.NIB;
			lopCC.mobjData.mdtDateOfBirth = ( client.birthDate == null ? null : Timestamp.valueOf(client.birthDate) );
			lopCC.mobjData.midSex = ( client.genderId == null ? null : UUID.fromString(client.genderId) );
			lopCC.mobjData.midMarital = ( client.maritalStatusId == null ? null : UUID.fromString(client.maritalStatusId) );
			lopCC.mobjData.midProfession = ( client.professionId == null ? null : UUID.fromString(client.professionId) );
			lopCC.mobjData.midCAE = ( client.caeId == null ? null : UUID.fromString(client.caeId) );
			lopCC.mobjData.mstrCAENotes = client.activityNotes;
			lopCC.mobjData.midSize = ( client.sizeId == null ? null : UUID.fromString(client.sizeId) );
			lopCC.mobjData.midSales = ( client.revenueId == null ? null : UUID.fromString(client.revenueId) );
			lopCC.mobjData.mstrNotes = client.notes;

			if ( (client.contacts != null) && (client.contacts.length > 0) )
			{
				lopCC.mobjContactOps = new ContactOps();
				lopCC.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(lopCC.mobjContactOps,
						client.contacts, Constants.ObjID_Client);
			}
			else
				lopCC.mobjContactOps = null;
			if ( (client.documents != null) && (client.documents.length > 0) )
			{
				lopCC.mobjDocOps = new DocOps();
				lopCC.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(lopCC.mobjDocOps,
						client.documents, Constants.ObjID_Client);
			}
			else
				lopCC.mobjDocOps = null;

			lopCC.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		client.id = lopCC.mobjData.mid.toString();
		client.clientNumber = Integer.toString(lopCC.mobjData.mlngNumber);
		client.processId = lopCC.mobjData.midProcess.toString();
		client.managerId = lopCC.mobjData.midManager.toString();
		if ( (client.contacts != null) && (client.contacts.length > 0) )
			ContactsServiceImpl.WalkContactTree(lopCC.mobjContactOps.marrCreate, client.contacts);
		if ( (client.documents != null) && (client.documents.length > 0) )
			DocumentServiceImpl.WalkDocTree(lopCC.mobjDocOps.marrCreate, client.documents);

		return client;
	}

	public Client editClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		ManageClientData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMD = new ManageClientData(UUID.fromString(client.processId));
			lopMD.mobjData = new ClientData();

			lopMD.mobjData.mid = UUID.fromString(client.id);
			lopMD.mobjData.mlngNumber = Integer.parseInt(client.clientNumber);
			lopMD.mobjData.midProcess = UUID.fromString(client.processId);
			lopMD.mobjData.mobjPrevValues = null;

			lopMD.mobjData.mstrName = client.name;
			if ( client.address != null )
			{
				lopMD.mobjData.mstrAddress1 = client.address.street1;
				lopMD.mobjData.mstrAddress2 = client.address.street2;
				if ( client.address.zipCode != null )
					lopMD.mobjData.midZipCode = ZipCodeBridge.GetZipCode(client.address.zipCode.code,
							client.address.zipCode.city, client.address.zipCode.county, client.address.zipCode.district,
							client.address.zipCode.country);
				else
					lopMD.mobjData.midZipCode = null;
			}
			else
			{
				lopMD.mobjData.mstrAddress1 = null;
				lopMD.mobjData.mstrAddress2 = null;
				lopMD.mobjData.midZipCode = null;
			}
			lopMD.mobjData.mstrFiscal = client.taxNumber;
			lopMD.mobjData.midType = ( client.typeId == null ? null : UUID.fromString(client.typeId) );
			lopMD.mobjData.midSubtype = ( client.subtypeId == null ? null : UUID.fromString(client.subtypeId) );
			lopMD.mobjData.midManager = null; //( client.managerId == null ? null : UUID.fromString(client.managerId) );
			lopMD.mobjData.midMediator = ( client.mediatorId == null ? null : UUID.fromString(client.mediatorId) );
			lopMD.mobjData.midProfile = ( client.operationalProfileId == null ? null : UUID.fromString(client.operationalProfileId) );
			lopMD.mobjData.midGroup = ( client.groupId == null ? null : UUID.fromString(client.groupId) );
			lopMD.mobjData.mstrBankingID = client.NIB;
			lopMD.mobjData.mdtDateOfBirth = ( client.birthDate == null ? null : Timestamp.valueOf(client.birthDate) );
			lopMD.mobjData.midSex = ( client.genderId == null ? null : UUID.fromString(client.genderId) );
			lopMD.mobjData.midMarital = ( client.maritalStatusId == null ? null : UUID.fromString(client.maritalStatusId) );
			lopMD.mobjData.midProfession = ( client.professionId == null ? null : UUID.fromString(client.professionId) );
			lopMD.mobjData.midCAE = ( client.caeId == null ? null : UUID.fromString(client.caeId) );
			lopMD.mobjData.mstrCAENotes = client.activityNotes;
			lopMD.mobjData.midSize = ( client.sizeId == null ? null : UUID.fromString(client.sizeId) );
			lopMD.mobjData.midSales = ( client.revenueId == null ? null : UUID.fromString(client.revenueId) );
			lopMD.mobjData.mstrNotes = client.notes;

			lopMD.mobjContactOps = null;
			lopMD.mobjDocOps = null;

			lopMD.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		client.managerId = lopMD.mobjData.midManager.toString();
		return client;
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		CreateMgrXFer lobjCMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjCMX = new CreateMgrXFer(UUID.fromString(transfer.managedProcessIds[0]));
		lobjCMX.midNewManager = UUID.fromString(transfer.newManagerId);
		lobjCMX.mbMassTransfer = false;

		try
		{
			lobjCMX.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		transfer.directTransfer = lobjCMX.mbDirectTransfer;
		if ( transfer.directTransfer )
		{
			transfer.id = null;
			transfer.processId = null;
			transfer.status = ManagerTransfer.Status.ACCEPTED;
		}
		else
		{
			transfer.id = lobjCMX.midTransferObject.toString();
			transfer.processId = lobjCMX.midCreatedSubproc.toString();
			transfer.status = ManagerTransfer.Status.PENDING;
		}

		return transfer;
	}

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Client mergeWithClient(String originalId, Client receptor)
		throws SessionExpiredException, BigBangException
	{
		MergeWithOther lopMWO;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lopMWO = new MergeWithOther(UUID.fromString(receptor.processId));
		lopMWO.mobjData = new ClientData();
		lopMWO.mobjData.mid = UUID.fromString(originalId);
		try
		{
			lopMWO.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return receptor;
	}

	public InsurancePolicy createPolicy(Client client, InsurancePolicy policy)
		throws SessionExpiredException, BigBangException
	{
		CreatePolicy lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopCP = new CreatePolicy(UUID.fromString(client.processId));
			lopCP.mobjData = new PolicyData();

			lopCP.mobjData.mid = null;

			lopCP.mobjData.mstrNumber = policy.number;
			lopCP.mobjData.midCompany = UUID.fromString(policy.insuranceAgencyId);
			lopCP.mobjData.midSubLine = UUID.fromString(policy.subLineId);
			lopCP.mobjData.mdtBeginDate = ( policy.startDate == null ? null : Timestamp.valueOf(policy.startDate) );
			lopCP.mobjData.midDuration = UUID.fromString(policy.durationId);
			lopCP.mobjData.midFractioning = UUID.fromString(policy.fractioningId);
			lopCP.mobjData.mlngMaturityDay = policy.maturityDay;
			lopCP.mobjData.mlngMaturityMonth = policy.maturityMonth;
			lopCP.mobjData.mdtEndDate = ( policy.expirationDate == null ? null : Timestamp.valueOf(policy.expirationDate) );
			lopCP.mobjData.mstrNotes = policy.notes;

			lopCP.mobjData.midManager = ( policy.managerId == null ? null : UUID.fromString(policy.managerId) );
			lopCP.mobjData.midProcess = null;

			lopCP.mobjData.mobjPrevValues = null;

			if ( (policy.contacts != null) && (policy.contacts.length > 0) )
			{
				lopCP.mobjContactOps = new ContactOps();
				lopCP.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(lopCP.mobjContactOps,
						policy.contacts, Constants.ObjID_Client);
			}
			else
				lopCP.mobjContactOps = null;
			if ( (policy.documents != null) && (policy.documents.length > 0) )
			{
				lopCP.mobjDocOps = new DocOps();
				lopCP.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(lopCP.mobjDocOps,
						policy.documents, Constants.ObjID_Client);
			}
			else
				lopCP.mobjDocOps = null;

			lopCP.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		policy.id = lopCP.mobjData.mid.toString();
		policy.processId = lopCP.mobjData.midProcess.toString();
		policy.number = lopCP.mobjData.mstrNumber;
		policy.managerId = lopCP.mobjData.midManager.toString();
		policy.mediatorId = lopCP.mobjData.midMediator.toString();
		if ( (policy.contacts != null) && (policy.contacts.length > 0) )
			ContactsServiceImpl.WalkContactTree(lopCP.mobjContactOps.marrCreate, policy.contacts);
		if ( (policy.documents != null) && (policy.documents.length > 0) )
			DocumentServiceImpl.WalkDocTree(lopCP.mobjDocOps.marrCreate, policy.documents);

		return policy;
	}

	@Override
	public RiskAnalysis createRiskAnalisys(String clientId,
			RiskAnalysis riskAnalisys) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteRequest createQuoteRequest(String clientId, QuoteRequest request)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Casualty createCasualty(String clientId, Casualty casualty)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteClient(String clientId, String processId)
		throws SessionExpiredException, BigBangException
	{
		DeleteClient lobjDC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjDC = new DeleteClient(UUID.fromString(processId));
		lobjDC.midClient = UUID.fromString(clientId);

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
		int i;
		MgrXFer lobjXFer;
		IScript lobjScript;
		IProcess lobjProc;
		CreateMgrXFer lobjCMX;
		AgendaItem lobjItem;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	lidManager = UUID.fromString(transfer.newManagerId);
    	if ( lidManager == null )
    		throw new BigBangException("Erro: Novo gestor não indicado.");

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
		try
		{
			if ( !lidManager.equals(Engine.getCurrentUser()) )
			{
				lobjXFer = MgrXFer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjXFer.setAt(1, null);
				lobjXFer.setAt(2, lidManager);
				lobjXFer.setAt(3, "Transferência de Gestor de Cliente");
				lobjXFer.setAt(4, Engine.getCurrentUser());
				lobjXFer.setAt(5, true);
				lobjXFer.setAt(6, Constants.ObjID_Client);
				lobjXFer.SaveToDb(ldb);

				lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_MgrXFer);
				lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjXFer.getKey(), null, ldb);
			}

			for ( i = 0; i < transfer.managedProcessIds.length; i++ )
			{
				lobjCMX = new CreateMgrXFer(UUID.fromString(transfer.managedProcessIds[i]));
				lobjCMX.midNewManager = lidManager;
				lobjCMX.mbMassTransfer = true;
				if ( !lidManager.equals(Engine.getCurrentUser()) )
				{
					lobjCMX.midTransferObject = lobjXFer.getKey();
					lobjCMX.midCreatedSubproc = lobjProc.getKey();
				}
				lobjCMX.Execute(ldb);
			}

			if ( !lidManager.equals(Engine.getCurrentUser()) )
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Cliente");
				lobjItem.setAt(1, Engine.getCurrentUser());
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Valid);
				lobjItem.SaveToDb(ldb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_CancelXFer}, ldb);

				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Transferência de Gestor de Cliente");
				lobjItem.setAt(1, lidManager);
				lobjItem.setAt(2, Constants.ProcID_MgrXFer);
				lobjItem.setAt(3, ldtAux);
				lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(ldb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()},
						new UUID[] {Constants.OPID_AcceptXFer, Constants.OPID_CancelXFer}, ldb);
			}
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (SQLException e1) {}
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

		transfer.directTransfer = lidManager.equals(Engine.getCurrentUser());
		if ( transfer.directTransfer )
		{
			transfer.id = null;
			transfer.processId = null;
			transfer.status = ManagerTransfer.Status.ACCEPTED;
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
		return Constants.ObjID_Client;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Name]", "[:Number]", "[:Group:Name]", "[:Process]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ClientSearchParameter lParam;
		String lstrAux;
		int[] larrMembers;
		java.lang.Object[] larrValues;
		IEntity lrefUserDecs;
        Calendar ldtAux;
		int i;

		if ( !(pParam instanceof ClientSearchParameter) )
			return false;
		lParam = (ClientSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Group:Name] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( (lParam.postalCodes != null) && (lParam.postalCodes.length > 0) )
		{
			pstrBuffer.append(" AND [:Zip Code] IN (");
			for ( i = 0; i < lParam.postalCodes.length; i++ )
			{
				if ( i > 0 )
					pstrBuffer.append(", ");
				pstrBuffer.append("'").append(lParam.postalCodes[i]).append("'");
			}
			pstrBuffer.append(")");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.costCenterId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] IN (SELECT [User] FROM (");
			larrMembers = new int[1];
			larrMembers[0] = 2;
			larrValues = new java.lang.Object[1];
			larrValues[0] = UUID.fromString(lParam.costCenterId);
			try
			{
				lrefUserDecs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
				pstrBuffer.append(lrefUserDecs.SQLForSelectByMembers(larrMembers, larrValues, null));
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [Aux2])");
		}

		if ( (lParam.CAEs != null) && (lParam.CAEs.length > 0) )
		{
			pstrBuffer.append(" AND [:Activity Code] IN (");
			for ( i = 0; i < lParam.CAEs.length; i++ )
			{
				if ( i > 0 )
					pstrBuffer.append(", ");
				pstrBuffer.append("'").append(lParam.CAEs[i]).append("'");
			}
			pstrBuffer.append(")");
		}

		if ( lParam.mediatorId != null )
		{
			pstrBuffer.append(" AND [:Mediator] = '").append(lParam.mediatorId).append("'");
		}

		if ( lParam.opProfileId != null )
		{
			pstrBuffer.append(" AND [:Profile] = '").append(lParam.opProfileId).append("'");
		}

		if ( lParam.workerSizeId != null )
		{
			pstrBuffer.append(" AND [:Size] = '").append(lParam.workerSizeId).append("'");
		}

		if ( lParam.salesVolumeId != null )
		{
			pstrBuffer.append(" AND [:Sales] = '").append(lParam.salesVolumeId).append("'");
		}

		if ( lParam.maritalStatusId != null )
		{
			pstrBuffer.append(" AND [:Marital Status] = '").append(lParam.maritalStatusId).append("'");
		}

		if ( (lParam.professionIds != null) && (lParam.professionIds.length > 0) )
		{
			pstrBuffer.append(" AND [:Profession] IN (");
			for ( i = 0; i < lParam.professionIds.length; i++ )
			{
				if ( i > 0 )
					pstrBuffer.append(", ");
				pstrBuffer.append("'").append(lParam.professionIds[i]).append("'");
			}
			pstrBuffer.append(")");
		}

		if ( lParam.birthDateFrom != null )
		{
			pstrBuffer.append(" AND [:Date of Birth] >= '").append(lParam.birthDateFrom).append("'");
		}

		if ( lParam.birthDateTo != null )
		{
			pstrBuffer.append(" AND [:Date of Birth] < '");
        	ldtAux = Calendar.getInstance();
        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.birthDateTo).getTime());
        	ldtAux.add(Calendar.DAY_OF_MONTH, 1);
			pstrBuffer.append((new Timestamp(ldtAux.getTimeInMillis())).toString().substring(0, 10)).append("'");
		}

		if ( (lParam.activityNotes != null) && (lParam.activityNotes.trim().length() > 0) )
		{
			pstrBuffer.append(" AND [:Activity Notes] LIKE N'%").append(lParam.activityNotes.trim().replace("'", "''")).append("%'");
		}

		if ( (lParam.notes != null) && (lParam.notes.trim().length() > 0) )
		{
			pstrBuffer.append(" AND [:Notes] LIKE N'%").append(lParam.notes.trim().replace("'", "''")).append("%'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
	{
		ClientSortParameter lParam;

		if ( !(pParam instanceof ClientSortParameter) )
			return false;
		lParam = (ClientSortParameter)pParam;

		if ( lParam.field == ClientSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == ClientSortParameter.SortableField.NAME )
			pstrBuffer.append("[:Name]");

		if ( lParam.field == ClientSortParameter.SortableField.GROUP )
			pstrBuffer.append("[:Group:Name]");

		if ( lParam.field == ClientSortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, java.lang.Object[] parrValues)
	{
		ClientStub lobjResult;

		lobjResult = new ClientStub();
		lobjResult.id = pid.toString();
		lobjResult.name = (String)parrValues[0];
		lobjResult.clientNumber = parrValues[1].toString();
		lobjResult.groupName = (parrValues[2] == null ? null : parrValues[2].toString());
		lobjResult.processId = parrValues[3].toString();

		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
	{
		ClientSearchParameter lParam;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof ClientSearchParameter) )
				continue;
			lParam = (ClientSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%' THEN -PATINDEX(N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%', [:Name]) ELSE ")
					.append("CASE WHEN [:Group:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%' THEN -1000*PATINDEX(N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%', [:Group:Name]) ELSE 0 END END");
		}

		return lbFound;
	}
}
