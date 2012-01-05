package bigBang.module.insurancePolicyModule.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.NewSearchResult;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseService;
import bigBang.module.insurancePolicyModule.shared.ExerciseSearchParameter;
import bigBang.module.insurancePolicyModule.shared.ExerciseSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;

public class PolicyExerciseServiceImpl
	extends SearchServiceBase implements PolicyExerciseService
{
	private static final long serialVersionUID = 1L;

	public Exercise getExercise(String exerciseId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidExercise;
		PolicyExercise lobjExercise;
		Exercise lobjResult;
		Policy lobjPolicy;
		PolicyObject[] larrObjects;
		PolicyCoverage[] larrCoverages;
		PolicyValue[] larrFixed;
		PolicyValue[][] larrVariable;
		int i, j, k;
		ArrayList<PolicyCoverage> larrLocalCoverages;
		PolicyCoverage lobjHeaderCoverage;
		ArrayList<Exercise.CoverageData.FixedField> larrAuxFixed;
		Exercise.CoverageData.FixedField lobjFixed;
		ArrayList<Exercise.CoverageData.VariableField> larrAuxVariable;
		Exercise.CoverageData.VariableField lobjVariable;
		Hashtable<UUID, Exercise.CoverageData.VariableField> larrValueMap;
		Hashtable<UUID, ArrayList<Exercise.CoverageData.VariableField.VariableValue>> larrAuxMap;
		Exercise.CoverageData.VariableField.VariableValue lobjValue;
		ArrayList<Exercise.CoverageData.VariableField.VariableValue> larrAuxValues;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidExercise = UUID.fromString(exerciseId);

		try
		{
			lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), lidExercise);
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjExercise.getAt(1));

			larrObjects = lobjPolicy.GetCurrentObjects();

			larrCoverages = lobjPolicy.GetCurrentCoverages();

			larrFixed = lobjPolicy.GetCurrentKeyedValues(null, lidExercise);

			larrVariable = new PolicyValue[larrObjects.length][];
			for ( i = 0; i < larrObjects.length; i++ )
				larrVariable[i] = lobjPolicy.GetCurrentKeyedValues(larrObjects[i].getKey(), lidExercise);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Exercise();
		lobjResult.id = lobjExercise.getKey().toString();
		lobjResult.ownerId = ((UUID)lobjExercise.getAt(1)).toString();
		lobjResult.label = lobjExercise.getLabel();
		lobjResult.startDate = ( lobjExercise.getAt(2) == null ? null :
				((Timestamp)lobjExercise.getAt(2)).toString().substring(0, 10) );
		lobjResult.endDate = ( lobjExercise.getAt(3) == null ? null :
				((Timestamp)lobjExercise.getAt(3)).toString().substring(0, 10) );

		java.util.Arrays.sort(larrObjects, new Comparator<PolicyObject>()
		{
			public int compare(PolicyObject o1, PolicyObject o2)
			{
				return o1.getLabel().compareTo(o2.getLabel());
			}
		});
		lobjResult.objects = new Exercise.InsuredObject[larrObjects.length];
		for ( i = 0; i < larrObjects.length; i++ )
		{
			lobjResult.objects[i] = new Exercise.InsuredObject();
			lobjResult.objects[i].id = larrObjects[i].getKey().toString();
			lobjResult.objects[i].tempObjectId = null;
			lobjResult.objects[i].label = larrObjects[i].getLabel();
		}

		lobjHeaderCoverage = null;
		larrLocalCoverages = new ArrayList<PolicyCoverage>();
		for ( i = 0; i < larrCoverages.length; i++ )
		{
			if ( larrCoverages[i].GetCoverage().IsHeader() )
				lobjHeaderCoverage = larrCoverages[i];
			else
				larrLocalCoverages.add(larrCoverages[i]);
		}
		larrCoverages = larrLocalCoverages.toArray(new PolicyCoverage[larrLocalCoverages.size()]);
		java.util.Arrays.sort(larrCoverages, new Comparator<PolicyCoverage>()
		{
			public int compare(PolicyCoverage o1, PolicyCoverage o2)
			{
				if ( o1.GetCoverage().IsMandatory() == o2.GetCoverage().IsMandatory() )
					return o1.GetCoverage().getLabel().compareTo(o2.GetCoverage().getLabel());
				if ( o1.GetCoverage().IsMandatory() )
					return -1;
				return 1;
			}
		});
		lobjResult.coverageData = new Exercise.CoverageData[larrCoverages.length];
		larrValueMap = new Hashtable<UUID, Exercise.CoverageData.VariableField>();
		larrAuxMap = new Hashtable<UUID, ArrayList<Exercise.CoverageData.VariableField.VariableValue>>();
		for ( i = 0; i < larrCoverages.length; i++ )
		{
			lobjResult.coverageData[i] = new Exercise.CoverageData();
			lobjResult.coverageData[i].coverageId = larrCoverages[i].GetCoverage().getKey().toString();
			larrAuxFixed = new ArrayList<Exercise.CoverageData.FixedField>();
			for ( j = 0; j < larrFixed.length; j++ )
			{
				if ( !larrFixed[j].GetTax().GetCoverage().getKey().equals(larrCoverages[i].GetCoverage().getKey()) )
					continue;

				lobjFixed = new Exercise.CoverageData.FixedField();
				lobjFixed.fieldId = larrFixed[j].GetTax().getKey().toString();
				lobjFixed.fieldName = larrFixed[j].GetTax().getLabel();
				lobjFixed.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrFixed[j].GetTax().GetFieldType());
				lobjFixed.unitsLabel = larrFixed[j].GetTax().GetUnitsLabel();
				lobjFixed.refersToId = ( larrFixed[j].GetTax().GetRefersToID() == null ? null :
					larrFixed[j].GetTax().GetRefersToID().toString() );
				lobjFixed.columnIndex = larrFixed[j].GetTax().GetColumnOrder();
				lobjFixed.value = larrFixed[j].getLabel();
				larrAuxFixed.add(lobjFixed);
			}
			lobjResult.coverageData[i].fixedFields =
					larrAuxFixed.toArray(new Exercise.CoverageData.FixedField[larrAuxFixed.size()]);
			java.util.Arrays.sort(lobjResult.coverageData[i].fixedFields, new Comparator<Exercise.CoverageData.FixedField>()
			{
				public int compare(Exercise.CoverageData.FixedField o1, Exercise.CoverageData.FixedField o2)
				{
					if ( (o1.type == o2.type) && (o1.refersToId == o1.refersToId) )
						return o1.fieldName.compareTo(o2.fieldName);
					if ( o1.type == o2.type )
						return o1.refersToId.compareTo(o2.refersToId);
					return o1.type.compareTo(o2.type);
				}
			});

			larrAuxVariable = new ArrayList<Exercise.CoverageData.VariableField>();
			for ( j = 0; j < larrObjects.length; j++ )
			{
				for ( k = 0; k < larrVariable[j].length; k++ )
				{
					if ( !larrVariable[j][k].GetTax().GetCoverage().getKey().equals(larrCoverages[i].GetCoverage().getKey()) )
						continue;

					lobjVariable = larrValueMap.get(larrVariable[j][k].GetTax().getKey());
					if ( lobjVariable == null )
					{
						lobjVariable = new Exercise.CoverageData.VariableField();
						lobjVariable.fieldId = larrVariable[j][k].GetTax().getKey().toString();
						lobjVariable.fieldName = larrVariable[j][k].GetTax().getLabel();
						lobjVariable.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrVariable[j][k].GetTax().GetFieldType());
						lobjVariable.unitsLabel = larrVariable[j][k].GetTax().GetUnitsLabel();
						lobjVariable.refersToId = ( larrVariable[j][k].GetTax().GetRefersToID() == null ? null :
								larrVariable[j][k].GetTax().GetRefersToID().toString() );
						lobjVariable.columnIndex = larrVariable[j][k].GetTax().GetColumnOrder();
						larrValueMap.put(larrVariable[j][k].GetTax().getKey(), lobjVariable);
						larrAuxMap.put(larrVariable[j][k].GetTax().getKey(),
								new ArrayList<Exercise.CoverageData.VariableField.VariableValue>());
						larrAuxVariable.add(lobjVariable);
					}
					lobjValue = new Exercise.CoverageData.VariableField.VariableValue();
					lobjValue.objectIndex = j;
					lobjValue.value = larrVariable[j][k].getLabel();
					larrAuxMap.get(larrVariable[j][k].GetTax().getKey()).add(lobjValue);
				}
			}
			lobjResult.coverageData[i].variableFields =
					larrAuxVariable.toArray(new Exercise.CoverageData.VariableField[larrAuxVariable.size()]);
			java.util.Arrays.sort(lobjResult.coverageData[i].variableFields,
					new Comparator<Exercise.CoverageData.VariableField>()
			{
				public int compare(Exercise.CoverageData.VariableField o1, Exercise.CoverageData.VariableField o2)
				{
					if ( (o1.type == o2.type) && (o1.refersToId == o1.refersToId) )
						return o1.fieldName.compareTo(o2.fieldName);
					if ( o1.type == o2.type )
						return o1.refersToId.compareTo(o2.refersToId);
					return o1.type.compareTo(o2.type);
				}
			});
			for ( j = 0; j < lobjResult.coverageData[i].variableFields.length; j++ )
			{
				larrAuxValues = larrAuxMap.get(UUID.fromString(lobjResult.coverageData[i].variableFields[j].fieldId));
				lobjResult.coverageData[i].variableFields[j].data =
						larrAuxValues.toArray(new Exercise.CoverageData.VariableField.VariableValue[larrAuxValues.size()]);
			}
		}
		if ( lobjHeaderCoverage != null )
		{
			lobjResult.headerData = new Exercise.CoverageData();
			larrAuxFixed = new ArrayList<Exercise.CoverageData.FixedField>();
			for ( j = 0; j < larrFixed.length; j++ )
			{
				if ( !larrFixed[j].GetTax().GetCoverage().getKey().equals(lobjHeaderCoverage.GetCoverage().getKey()) )
					continue;

				lobjFixed = new Exercise.CoverageData.FixedField();
				lobjFixed.fieldId = larrFixed[j].GetTax().getKey().toString();
				lobjFixed.fieldName = larrFixed[j].GetTax().getLabel();
				lobjFixed.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrFixed[j].GetTax().GetFieldType());
				lobjFixed.unitsLabel = larrFixed[j].GetTax().GetUnitsLabel();
				lobjFixed.refersToId = ( larrFixed[j].GetTax().GetRefersToID() == null ? null :
					larrFixed[j].GetTax().GetRefersToID().toString() );
				lobjFixed.columnIndex = larrFixed[j].GetTax().GetColumnOrder();
				lobjFixed.value = larrFixed[j].getLabel();
				larrAuxFixed.add(lobjFixed);
			}
			lobjResult.headerData.fixedFields =
					larrAuxFixed.toArray(new Exercise.CoverageData.FixedField[larrAuxFixed.size()]);
			java.util.Arrays.sort(lobjResult.headerData.fixedFields, new Comparator<Exercise.CoverageData.FixedField>()
			{
				public int compare(Exercise.CoverageData.FixedField o1, Exercise.CoverageData.FixedField o2)
				{
					if ( (o1.type == o2.type) && (o1.refersToId == o1.refersToId) )
						return o1.fieldName.compareTo(o2.fieldName);
					if ( o1.type == o2.type )
						return o1.refersToId.compareTo(o2.refersToId);
					return o1.type.compareTo(o2.type);
				}
			});

			larrAuxVariable = new ArrayList<Exercise.CoverageData.VariableField>();
			for ( j = 0; j < larrObjects.length; j++ )
			{
				for ( k = 0; k < larrVariable[j].length; k++ )
				{
					if ( !larrVariable[j][k].GetTax().GetCoverage().getKey().equals(lobjHeaderCoverage.GetCoverage().getKey()) )
						continue;

					lobjVariable = larrValueMap.get(larrVariable[j][k].GetTax().getKey());
					if ( lobjVariable == null )
					{
						lobjVariable = new Exercise.CoverageData.VariableField();
						lobjVariable.fieldId = larrVariable[j][k].GetTax().getKey().toString();
						lobjVariable.fieldName = larrVariable[j][k].GetTax().getLabel();
						lobjVariable.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrVariable[j][k].GetTax().GetFieldType());
						lobjVariable.unitsLabel = larrVariable[j][k].GetTax().GetUnitsLabel();
						lobjVariable.refersToId = ( larrVariable[j][k].GetTax().GetRefersToID() == null ? null :
								larrVariable[j][k].GetTax().GetRefersToID().toString() );
						lobjVariable.columnIndex = larrVariable[j][k].GetTax().GetColumnOrder();
						larrValueMap.put(larrVariable[j][k].GetTax().getKey(), lobjVariable);
						larrAuxMap.put(larrVariable[j][k].GetTax().getKey(),
								new ArrayList<Exercise.CoverageData.VariableField.VariableValue>());
						larrAuxVariable.add(lobjVariable);
					}
					lobjValue = new Exercise.CoverageData.VariableField.VariableValue();
					lobjValue.objectIndex = j;
					lobjValue.value = larrVariable[j][k].getLabel();
					larrAuxMap.get(larrVariable[j][k].GetTax().getKey()).add(lobjValue);
				}
			}
			lobjResult.headerData.variableFields =
					larrAuxVariable.toArray(new Exercise.CoverageData.VariableField[larrAuxVariable.size()]);
			java.util.Arrays.sort(lobjResult.headerData.variableFields,
					new Comparator<Exercise.CoverageData.VariableField>()
			{
				public int compare(Exercise.CoverageData.VariableField o1, Exercise.CoverageData.VariableField o2)
				{
					if ( (o1.type == o2.type) && (o1.refersToId == o1.refersToId) )
						return o1.fieldName.compareTo(o2.fieldName);
					if ( o1.type == o2.type )
						return o1.refersToId.compareTo(o2.refersToId);
					return o1.type.compareTo(o2.type);
				}
			});
			for ( j = 0; j < lobjResult.headerData.variableFields.length; j++ )
			{
				larrAuxValues = larrAuxMap.get(UUID.fromString(lobjResult.headerData.variableFields[j].fieldId));
				lobjResult.headerData.variableFields[j].data =
						larrAuxValues.toArray(new Exercise.CoverageData.VariableField.VariableValue[larrAuxValues.size()]);
			}
		}

		return lobjResult;
	}

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
		ExerciseStub lobjResult;

		lobjResult = new ExerciseStub();
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
	
	@Override
	public NewSearchResult openSearch(SearchParameter[] parameters,
			SortParameter[] sorts, int size) throws SessionExpiredException,
			BigBangException {
		// TODO Auto-generated method stub
		return super.openSearch(parameters, sorts, size);
	}
}
