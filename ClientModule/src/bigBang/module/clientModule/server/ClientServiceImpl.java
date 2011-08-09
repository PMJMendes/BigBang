package bigBang.module.clientModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SessionExpiredException;
import bigBang.library.shared.SortOrder;
import bigBang.library.shared.SortParameter;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;

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
		lobjResult.name = (String)lobjClient.getAt(1);
		lobjResult.clientNumber = lobjClient.getAt(0).toString();
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
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Client editClient(Client client)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public void deleteClient(String clientId)
		throws SessionExpiredException, BigBangException
	{
			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Client;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Name]", "[:Number]", "[:Group:Name]"};
	}

	protected void buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ClientSearchParameter lParam;
		int[] larrMembers;
		java.lang.Object[] larrValues;
		IEntity lrefUserDecs;
        Calendar ldtAux;
		int i;

		if ( !(pParam instanceof ClientSearchParameter) )
			return;
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
			pstrBuffer.append(" AND [:Manager] IN (SELECT [PK] FROM (");
			larrMembers = new int[1];
			larrMembers[0] = 2;
			larrValues = new java.lang.Object[1];
			larrValues[0] = UUID.fromString(lParam.costCenterId);
			try
			{
				lrefUserDecs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
				pstrBuffer.append(lrefUserDecs.SQLForSelectByMembers(larrMembers, larrValues, new int[0]));
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
	}

	protected void buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
	{
		ClientSortParameter lParam;

		if ( !(pParam instanceof ClientSortParameter) )
			return;
		lParam = (ClientSortParameter)pParam;

		if ( lParam.field == ClientSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return;
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
			pstrBuffer.append("CASE WHEN [:Group:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''").replace(" ", "%"))
					.append("%' THEN 10 ELSE 0 END + CASE WHEN [:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''")
					.replace(" ", "%")).append("%' THEN 1 ELSE 0 END");
		}

		return lbFound;
	}
}
