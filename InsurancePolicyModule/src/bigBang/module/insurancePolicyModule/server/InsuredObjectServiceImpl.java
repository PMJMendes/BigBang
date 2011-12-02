package bigBang.module.insurancePolicyModule.server;

import java.util.UUID;

import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsuredObjectSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;

public class InsuredObjectServiceImpl
	extends SearchServiceBase
{
	private static final long serialVersionUID = 1L;

	protected UUID getObjectID()
	{
		return Constants.ObjID_PolicyObject;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Name]"};
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
			pstrBuffer.append(" AND [:Policy] = '").append(lParam.policyId).append("')");
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
