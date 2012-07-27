package bigBang.module.clientModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
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
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.InfoOrDocumentRequestServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.server.TransferManagerServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.casualtyModule.server.CasualtyServiceImpl;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.CasualtyData;
import com.premiumminds.BigBang.Jewel.Data.ClientData;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Client.CreateCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Client.CreateInfoRequest;
import com.premiumminds.BigBang.Jewel.Operations.Client.DeleteClient;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExecMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Client.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Client.MergeIntoAnother;
import com.premiumminds.BigBang.Jewel.Operations.General.CreateClient;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

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
		ClientGroup lobjGroup;
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
			if ( lobjClient.getAt(10) == null )
				lobjGroup = null;
			else
				lobjGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjClient.getAt(10));
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
		lobjResult.groupId = (lobjGroup == null ? null : lobjGroup.getKey().toString());
		lobjResult.groupName = (lobjGroup == null ? null : lobjGroup.getLabel());
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
		lobjResult.birthDate = (lobjClient.getAt(12) == null ? null : ((Timestamp)lobjClient.getAt(12)).toString().substring(0, 10));
		lobjResult.genderId = (lobjClient.getAt(13) == null ? null : lobjClient.getAt(13).toString());
		lobjResult.maritalStatusId = (lobjClient.getAt(14) == null ? null : lobjClient.getAt(14).toString());
		lobjResult.professionId = (lobjClient.getAt(15) == null ? null : lobjClient.getAt(15).toString());
		lobjResult.notes = (String)lobjClient.getAt(20);
		lobjResult.docushare = (String)lobjClient.getAt(23);

		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.managerId = lobjProc.GetManagerID().toString();

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

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
			lopCC.mobjData.midManager = ( client.managerId == null ? Engine.getCurrentUser() : UUID.fromString(client.managerId) );
			lopCC.mobjData.midMediator = ( client.mediatorId == null ? null : UUID.fromString(client.mediatorId) );
			lopCC.mobjData.midProfile = ( client.operationalProfileId == null ? null : UUID.fromString(client.operationalProfileId) );
			lopCC.mobjData.midGroup = ( client.groupId == null ? null : UUID.fromString(client.groupId) );
			lopCC.mobjData.mstrBankingID = client.NIB;
			lopCC.mobjData.mdtDateOfBirth = ( client.birthDate == null ? null : Timestamp.valueOf(client.birthDate + " 00:00:00.0") );
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
				lopCC.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(client.contacts);
			}
			else
				lopCC.mobjContactOps = null;
			if ( (client.documents != null) && (client.documents.length > 0) )
			{
				lopCC.mobjDocOps = new DocOps();
				lopCC.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(client.documents);
			}
			else
				lopCC.mobjDocOps = null;

			lopCC.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getClient(lopCC.mobjData.mid.toString());
	}

	public Client editClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		ManageData lopMD;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lopMD = new ManageData(UUID.fromString(client.processId));
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
			lopMD.mobjData.mdtDateOfBirth = ( client.birthDate == null ? null : Timestamp.valueOf(client.birthDate + " 00:00:00.0") );
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

		return getClient(client.id);
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Client lobjClient;
		ExecMgrXFer lobjEMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjClient = com.premiumminds.BigBang.Jewel.Objects.Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(transfer.dataObjectIds[0]));

			lobjEMX = new ExecMgrXFer(lobjClient.GetProcessID());
			lobjEMX.midNewManager = UUID.fromString(transfer.newManagerId);
			lobjEMX.midMassProcess = null;

			lobjEMX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return transfer;
	}

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Client lobjClient;
		CreateInfoRequest lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjClient = com.premiumminds.BigBang.Jewel.Objects.Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));

			lopCIR = new CreateInfoRequest(lobjClient.GetProcessID());
			lopCIR.midRequestType = UUID.fromString(request.requestTypeId);
			lopCIR.mobjMessage = MessageBridge.outgoingToServer(request.message);
			lopCIR.mlngDays = request.replylimit;

			lopCIR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lopCIR.midRequestObject);
	}

	public Client mergeWithClient(String originalId, String receptorId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Client lobjClient;
		MergeIntoAnother lopMIA;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjClient = com.premiumminds.BigBang.Jewel.Objects.Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(originalId));

			lopMIA = new MergeIntoAnother(lobjClient.GetProcessID());
			lopMIA.midClientDestination = UUID.fromString(receptorId);
			lopMIA.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getClient(receptorId);
	}

	public InsurancePolicy createPolicy(String clientProcessId, InsurancePolicy policy)
		throws SessionExpiredException, BigBangException
	{
//		CreatePolicy lopCP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

//		try
//		{
//			lopCP = new CreatePolicy(UUID.fromString(clientProcessId));
//			lopCP.mobjData = new PolicyData();
//
//			lopCP.mobjData.mid = null;
//
//			lopCP.mobjData.mstrNumber = policy.number;
//			lopCP.mobjData.midCompany = UUID.fromString(policy.insuranceAgencyId);
//			lopCP.mobjData.midSubLine = UUID.fromString(policy.subLineId);
//			lopCP.mobjData.mdtBeginDate = ( policy.startDate == null ? null : Timestamp.valueOf(policy.startDate + " 00:00:00.0") );
//			lopCP.mobjData.midDuration = UUID.fromString(policy.durationId);
//			lopCP.mobjData.midFractioning = UUID.fromString(policy.fractioningId);
//			lopCP.mobjData.mlngMaturityDay = policy.maturityDay;
//			lopCP.mobjData.mlngMaturityMonth = policy.maturityMonth;
//			lopCP.mobjData.mdtEndDate = ( policy.expirationDate == null ? null :
//					Timestamp.valueOf(policy.expirationDate + " 00:00:00.0") );
//			lopCP.mobjData.mstrNotes = policy.notes;
//			lopCP.mobjData.midMediator = ( policy.mediatorId == null ? null : UUID.fromString(policy.mediatorId) );
//			lopCP.mobjData.mbCaseStudy = policy.caseStudy;
//
//			lopCP.mobjData.midManager = ( policy.managerId == null ? null : UUID.fromString(policy.managerId) );
//			lopCP.mobjData.midProcess = null;
//
//			lopCP.mobjData.mobjPrevValues = null;
//
//			if ( (policy.contacts != null) && (policy.contacts.length > 0) )
//			{
//				lopCP.mobjContactOps = new ContactOps();
//				lopCP.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(lopCP.mobjContactOps,
//						policy.contacts, Constants.ObjID_Policy);
//			}
//			else
//				lopCP.mobjContactOps = null;
//			if ( (policy.documents != null) && (policy.documents.length > 0) )
//			{
//				lopCP.mobjDocOps = new DocOps();
//				lopCP.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(lopCP.mobjDocOps,
//						policy.documents, Constants.ObjID_Policy);
//			}
//			else
//				lopCP.mobjDocOps = null;
//
//			lopCP.Execute();
//
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangException(e.getMessage(), e);
//		}
//
//		policy.id = lopCP.mobjData.mid.toString();
//		policy.processId = lopCP.mobjData.midProcess.toString();
//		policy.number = lopCP.mobjData.mstrNumber;
//		policy.managerId = lopCP.mobjData.midManager.toString();
//		policy.mediatorId = lopCP.mobjData.midMediator.toString();
//		if ( (policy.contacts != null) && (policy.contacts.length > 0) )
//			ContactsServiceImpl.WalkContactTree(lopCP.mobjContactOps.marrCreate, policy.contacts);
//		if ( (policy.documents != null) && (policy.documents.length > 0) )
//			DocumentServiceImpl.WalkDocTree(lopCP.mobjDocOps.marrCreate, policy.documents);
//
//		return policy;
		throw new BigBangException("Erro: Operação de crição directa não suportada.");
	}

	@Override
	public RiskAnalysis createRiskAnalisys(String clientId,
			RiskAnalysis riskAnalisys) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public QuoteRequest createQuoteRequest(String clientId, QuoteRequest request)
		throws SessionExpiredException, BigBangException
	{
		throw new BigBangException("Erro: Operação de crição directa não suportada.");
	}

	public Casualty createCasualty(Casualty casualty)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Client lobjClient;
		CreateCasualty lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjClient = com.premiumminds.BigBang.Jewel.Objects.Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(casualty.clientId));
		}
		catch (BigBangJewelException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateCasualty(lobjClient.GetProcessID());
		lopCC.mobjData = new CasualtyData();
		lopCC.mobjData.mdtCasualtyDate = Timestamp.valueOf(casualty.casualtyDate + " 00:00:00.0");
		lopCC.mobjData.mstrDescription = casualty.description;
		lopCC.mobjData.mstrNotes = casualty.internalNotes;
		lopCC.mobjData.mbCaseStudy = casualty.caseStudy;
		lopCC.mobjData.midManager = (casualty.managerId == null ? null : UUID.fromString(casualty.managerId));
		lopCC.mobjData.mobjPrevValues = null;
		lopCC.mobjContactOps = null;
		lopCC.mobjDocOps = null;

		try
		{
			lopCC.Execute();
		}
		catch (JewelPetriException e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return CasualtyServiceImpl.sGetCasualty(lopCC.mobjData.mid);
	}

	public void deleteClient(String clientId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.Client lobjClient;
		DeleteClient lobjDC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjClient = com.premiumminds.BigBang.Jewel.Objects.Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(clientId));

			lobjDC = new DeleteClient(lobjClient.GetProcessID());
			lobjDC.midClient = UUID.fromString(clientId);
			lobjDC.mstrReason = reason;

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
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		transfer.objectTypeId = Constants.ObjID_Client.toString();

		return TransferManagerServiceImpl.sCreateMassTransfer(transfer, Constants.ProcID_Client);
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
			pstrBuffer.append(" AND [:Activity Code] IN ('");
			for ( i = 0; i < lParam.CAEs.length; i++ )
			{
				if ( i > 0 )
					pstrBuffer.append("', '");
				pstrBuffer.append(lParam.CAEs[i]);
			}
			pstrBuffer.append("')");
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
			pstrBuffer.append(" AND [:Profession] IN ('");
			for ( i = 0; i < lParam.professionIds.length; i++ )
			{
				if ( i > 0 )
					pstrBuffer.append("', '");
				pstrBuffer.append(lParam.professionIds[i]);
			}
			pstrBuffer.append("')");
		}

		if ( lParam.birthDateFrom != null )
		{
			pstrBuffer.append(" AND [:Date of Birth] >= '").append(lParam.birthDateFrom).append("'");
		}

		if ( lParam.birthDateTo != null )
		{
			pstrBuffer.append(" AND [:Date of Birth] < '");
        	ldtAux = Calendar.getInstance();
        	ldtAux.setTimeInMillis(Timestamp.valueOf(lParam.birthDateTo + " 00:00:00.0").getTime());
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
		throws BigBangException
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
		String lstrAux;
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
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Name] LIKE N'%").append(lstrAux).append("%' THEN -PATINDEX(N'%").append(lstrAux)
					.append("%', [:Name]) ELSE ")
					.append("CASE WHEN [:Group:Name] LIKE N'%").append(lstrAux).append("%' THEN -1000*PATINDEX(N'%").append(lstrAux)
					.append("%', [:Group:Name]) ELSE 0 END END");
		}

		return lbFound;
	}
}
