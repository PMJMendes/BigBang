package bigBang.module.insurancePolicyModule.server;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;

public class PolicyObjectServiceImpl
	extends SearchServiceBase
	implements PolicyObjectService
{
	private static final long serialVersionUID = 1L;

	public InsuredObject getObject(String objectId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return new DataBuilder().getObject(UUID.fromString(objectId));
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_PolicyObject;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Name]", "[:Policy]", "[:Type]", "[:Type:Type]", "[:Address 1]", "[:Address 2]", "[:Zip Code]",
				"[:Zip Code:Code]", "[:Zip Code:City]", "[:Zip Code:County]", "[:Zip Code:District]", "[:Zip Code:Country]",
				"[:Inclusion Date]", "[:Exclusion Date]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		InsuredObjectSearchParameter lParam;
		String lstrAux;

		if ( !(pParam instanceof InsuredObjectSearchParameter) )
			return false;
		lParam = (InsuredObjectSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Name] LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.policyId != null )
		{
			pstrBuffer.append(" AND [:Policy] = '").append(lParam.policyId).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		InsuredObjectSortParameter lParam;

		if ( !(pParam instanceof InsuredObjectSortParameter) )
			return false;
		lParam = (InsuredObjectSortParameter)pParam;

		if ( lParam.field == InsuredObjectSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == InsuredObjectSortParameter.SortableField.NAME )
			pstrBuffer.append("[:Name]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		InsuredObjectStub lobjResult;

		lobjResult = new InsuredObjectStub();
		lobjResult.id = pid.toString();
		lobjResult.unitIdentification = (String)parrValues[0];
		lobjResult.typeId = ((UUID)parrValues[2]).toString();
		lobjResult.typeText = (String)parrValues[3];
		if ( (parrValues[4] != null) || (parrValues[5] != null) || (parrValues[6] != null) )
		{
			lobjResult.address = new Address();
			lobjResult.address.street1 = (String)parrValues[4];
			lobjResult.address.street2 = (String)parrValues[5];
			if ( parrValues[6] != null )
			{
				lobjResult.address.zipCode = new ZipCode();
				lobjResult.address.zipCode.code = (String)parrValues[7];
				lobjResult.address.zipCode.city = (String)parrValues[8];
				lobjResult.address.zipCode.county = (String)parrValues[9];
				lobjResult.address.zipCode.district = (String)parrValues[10];
				lobjResult.address.zipCode.country = (String)parrValues[11];
			}
			else
				lobjResult.address.zipCode = null;
		}
		else
			lobjResult.address = null;
		lobjResult.inclusionDate = ( parrValues[12] == null ? null : ((Timestamp)parrValues[12]).toString().substring(0, 10));
		lobjResult.exclusionDate = ( parrValues[13] == null ? null : ((Timestamp)parrValues[13]).toString().substring(0, 10));
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		InsuredObjectSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof InsuredObjectSearchParameter) )
				continue;
			lParam = (InsuredObjectSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Name])");
		}

		return lbFound;
	}
}
