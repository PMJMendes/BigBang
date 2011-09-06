package bigBang.module.clientModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.client.dataAccess.SearchParameter;
import bigBang.definitions.client.dataAccess.SortOrder;
import bigBang.definitions.client.dataAccess.SortParameter;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.FileServiceImpl;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.ZipCodeBridge;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Client.DeleteClient;
import com.premiumminds.BigBang.Jewel.Operations.Client.ManageClientData;
import com.premiumminds.BigBang.Jewel.Operations.DataObjects.ClientData;
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

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lid = UUID.fromString(clientId);
		try
		{
			lobjClient = com.premiumminds.BigBang.Jewel.Objects.Client.GetInstance(Engine.getCurrentNameSpace(), lid);
			if ( lobjClient.getAt(4) == null )
				lobjZipCode = null;
			else
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjClient.getAt(4));
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
		lobjResult.groupId = (lobjClient.getAt(11) == null ? null : lobjClient.getAt(11).toString());
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
		lobjResult.NIB = (String)lobjClient.getAt(12);
		lobjResult.typeId = lobjClient.getAt(6).toString();
		lobjResult.subtypeId = (lobjClient.getAt(7) == null ? null : lobjClient.getAt(7).toString());
		lobjResult.mediatorId = lobjClient.getAt(9).toString();
		lobjResult.managerId = lobjClient.getAt(8).toString();
		lobjResult.operationalProfileId = lobjClient.getAt(10).toString();
		lobjResult.caeId = (lobjClient.getAt(17) == null ? null : lobjClient.getAt(17).toString());
		lobjResult.activityNotes = (String)lobjClient.getAt(18);
		lobjResult.sizeId = (lobjClient.getAt(19) == null ? null : lobjClient.getAt(19).toString());
		lobjResult.revenueId = (lobjClient.getAt(20) == null ? null : lobjClient.getAt(20).toString());
		lobjResult.birthDate = (lobjClient.getAt(13) == null ? null : ((Timestamp)lobjClient.getAt(13)).toString());
		lobjResult.genderId = (lobjClient.getAt(14) == null ? null : lobjClient.getAt(14).toString());
		lobjResult.maritalStatusId = (lobjClient.getAt(15) == null ? null : lobjClient.getAt(15).toString());
		lobjResult.professionId = (lobjClient.getAt(16) == null ? null : lobjClient.getAt(16).toString());
		lobjResult.notes = (String)lobjClient.getAt(21);

		lobjResult.processId = (lobjClient.getAt(22) == null ? null : lobjClient.getAt(22).toString());

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
			lopCC.mobjData.midManager = ( client.managerId == null ? null : UUID.fromString(client.managerId) );
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
				lopCC.mobjContactOps.marrCreate = BuildContactTree(lopCC.mobjContactOps,
						client.contacts, Constants.ObjID_Client);
			}
			else
				lopCC.mobjContactOps = null;
			if ( (client.documents != null) && (client.documents.length > 0) )
			{
				lopCC.mobjDocOps = new DocOps();
				lopCC.mobjDocOps.marrCreate = BuildDocTree(lopCC.mobjDocOps,
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
		if ( (client.contacts != null) && (client.contacts.length > 0) )
			WalkContactTree(lopCC.mobjContactOps.marrCreate, client.contacts);
		if ( (client.documents != null) && (client.documents.length > 0) )
			WalkDocTree(lopCC.mobjDocOps.marrCreate, client.documents);

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
			lopMD.mobjData.midManager = ( client.managerId == null ? null : UUID.fromString(client.managerId) );
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

		return client;
	}

	@Override
	public RiskAnalisys createRiskAnalisys(String clientId,
			RiskAnalisys riskAnalisys) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsurancePolicy createPolicy(String clientId, InsurancePolicy policy)
			throws SessionExpiredException, BigBangException {
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

	@Override
	public Client mergeWithClient(String originalId, String receptorId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagerTransfer[] createManagerTransfer(String[] clientIds,
			String managerId) throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InfoOrDocumentRequest createInfoOrDocumentRequest(
			InfoOrDocumentRequest request)
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

	protected UUID getObjectID()
	{
		return Constants.ObjID_Client;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Name]", "[:Number]", "[:Group:Name]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ClientSearchParameter lParam;
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
			pstrBuffer.append(" AND ([:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%'")
					.append(" OR CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lParam.freeText.trim().replace("'", "''")
					.replace(" ", "%")).append("%'")
					.append(" OR [:Group:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%')");
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
			pstrBuffer.append(" AND [:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.costCenterId != null )
		{
			pstrBuffer.append(" AND [:Manager] IN (SELECT [User] FROM (");
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

	private ContactOps.ContactData[] BuildContactTree(ContactOps prefAux, Contact[] parrContacts, UUID pidParentType)
		throws BigBangJewelException
	{
		ContactOps.ContactData[] larrResult;
		int i, j;

		if ( (parrContacts == null) || (parrContacts.length == 0) )
			return null;

		larrResult = new ContactOps.ContactData[parrContacts.length];
		for ( i = 0; i < parrContacts.length; i++ )
		{
			larrResult[i] = prefAux.new ContactData();
			larrResult[i].mid = null;
			larrResult[i].mstrName = parrContacts[i].name;
			larrResult[i].midOwnerType = pidParentType;
			larrResult[i].midOwnerId = null;
			if ( parrContacts[i].address != null )
			{
				larrResult[i].mstrAddress1 = parrContacts[i].address.street1;
				larrResult[i].mstrAddress2 = parrContacts[i].address.street2;
				larrResult[i].midZipCode = ZipCodeBridge.GetZipCode(parrContacts[i].address.zipCode.code,
						parrContacts[i].address.zipCode.city, parrContacts[i].address.zipCode.county,
						parrContacts[i].address.zipCode.district, parrContacts[i].address.zipCode.country);
			}
			else
			{
				larrResult[i].mstrAddress1 = null;
				larrResult[i].mstrAddress2 = null;
				larrResult[i].midZipCode = null;
			}
			larrResult[i].midContactType = (parrContacts[i].typeId == null ? null : UUID.fromString(parrContacts[i].typeId));
			if ( parrContacts[i].info != null )
			{
				larrResult[i].marrInfo = new ContactOps.ContactData.ContactInfoData[parrContacts[i].info.length];
				for ( j = 0; j < parrContacts[i].info.length; j++ )
				{
					larrResult[i].marrInfo[j] = larrResult[i].new ContactInfoData();
					larrResult[i].marrInfo[j].midType = UUID.fromString(parrContacts[i].info[j].typeId);
					larrResult[i].marrInfo[j].mstrValue = parrContacts[i].info[j].value;
				}
			}
			else
				larrResult[i].marrInfo = null;
			larrResult[i].marrSubContacts = BuildContactTree(prefAux, parrContacts[i].subContacts, Constants.ObjID_Contact);
		}

		return larrResult;
	}

	private DocOps.DocumentData[] BuildDocTree(DocOps prefAux, Document[] parrDocuments, UUID pidParentType)
		throws BigBangJewelException
	{
		DocOps.DocumentData[] larrResult;
		int i, j;

		if ( (parrDocuments == null) || (parrDocuments.length == 0) )
			return null;

		larrResult = new DocOps.DocumentData[parrDocuments.length];
		for ( i = 0; i < parrDocuments.length; i++ )
		{
			larrResult[i] = prefAux.new DocumentData();
			larrResult[i].mid = null;
			larrResult[i].mstrName = parrDocuments[i].name;
			larrResult[i].midOwnerType = pidParentType;
			larrResult[i].midOwnerId = null;
			larrResult[i].midDocType = (parrDocuments[i].docTypeId == null ? null : UUID.fromString(parrDocuments[i].docTypeId));
			larrResult[i].mstrText = parrDocuments[i].text;
			if ( parrDocuments[i].fileStorageId != null )
				larrResult[i].mobjFile = FileServiceImpl.GetFileXferStorage().
						get(UUID.fromString(parrDocuments[i].fileStorageId)).GetVarData();
			else
				larrResult[i].mobjFile = null;
			if ( parrDocuments[i].parameters != null )
			{
				larrResult[i].marrInfo = new DocOps.DocumentData.DocInfoData[parrDocuments[i].parameters.length];
				for ( j = 0; j < parrDocuments[i].parameters.length; j++ )
				{
					larrResult[i].marrInfo[j] = larrResult[i].new DocInfoData();
					larrResult[i].marrInfo[j].mstrType = parrDocuments[i].parameters[j].name;
					larrResult[i].marrInfo[j].mstrValue = parrDocuments[i].parameters[j].value;
				}
			}
			else
				larrResult[i].marrInfo = null;
		}

		return larrResult;
	}

	private void WalkContactTree(ContactOps.ContactData[] parrResults, Contact[] parrContacts)
	{
		int i;
		
		for ( i = 0; i < parrResults.length; i++ )
		{
			parrContacts[i].id = parrResults[i].mid.toString();
			if ( (parrContacts[i].subContacts != null) && (parrResults[i].marrSubContacts != null) )
				WalkContactTree(parrResults[i].marrSubContacts, parrContacts[i].subContacts);
		}
	}

	private void WalkDocTree(DocOps.DocumentData[] parrResults, Document[] parrDocuments)
	{
		int i;
		
		for ( i = 0; i < parrResults.length; i++ )
			parrDocuments[i].id = parrResults[i].mid.toString();
	}
}
