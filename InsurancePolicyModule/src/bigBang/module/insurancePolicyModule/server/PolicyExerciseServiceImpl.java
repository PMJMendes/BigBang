package bigBang.module.insurancePolicyModule.server;

import java.util.UUID;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.module.insurancePolicyModule.shared.ExerciseSearchParameter;
import bigBang.module.insurancePolicyModule.shared.ExerciseSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;

public class PolicyExerciseServiceImpl
	extends SearchServiceBase
{
	private static final long serialVersionUID = 1L;

	protected UUID getObjectID()
	{
		return Constants.ObjID_PolicyExercise;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Label]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		ExerciseSearchParameter lParam;
		String lstrAux;

		if ( !(pParam instanceof ExerciseSearchParameter) )
			return false;
		lParam = (ExerciseSearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Label] LIKE N'%").append(lstrAux).append("%')");
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
		ExerciseSortParameter lParam;

		if ( !(pParam instanceof ExerciseSortParameter) )
			return false;
		lParam = (ExerciseSortParameter)pParam;

		if ( lParam.field == ExerciseSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == ExerciseSortParameter.SortableField.STARTDATE )
			pstrBuffer.append("[:Start Date]");

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		Exercise lobjResult;

		lobjResult = new Exercise();
		lobjResult.id = pid.toString();
		lobjResult.label = (String)parrValues[0];
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		ExerciseSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof ExerciseSearchParameter) )
				continue;
			lParam = (ExerciseSearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Label])");
		}

		return lbFound;
	}
}
