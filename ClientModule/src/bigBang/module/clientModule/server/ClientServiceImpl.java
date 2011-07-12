package bigBang.module.clientModule.server;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientStub;

public class ClientServiceImpl
	extends SearchServiceBase
	implements ClientService
{
	private static final long serialVersionUID = 1L;

	public Client getClient(String clientId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
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
			pstrBuffer.append(" AND ([:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''")).append("%'")
					.append(" OR CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lParam.freeText.trim().replace("'", "''"))
					.append("%'")
					.append(" OR [:Group:Name] LIKE N'%").append(lParam.freeText.trim().replace("'", "''")).append("%')");
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

	protected String[] getColumns()
	{
		return new String[] {"[:Name]", "[:Number]", "[:Group:Name]"};
	}

	protected SearchResult buildResult(UUID pid, java.lang.Object[] parrValues)
	{
		ClientStub lobjResult;

		lobjResult = new ClientStub();
		lobjResult.id = pid.toString();
		lobjResult.name = (String)parrValues[0];
		lobjResult.clientNumber = parrValues[1].toString();
		lobjResult.groupName = parrValues[2].toString();

		return lobjResult;
	}
}
