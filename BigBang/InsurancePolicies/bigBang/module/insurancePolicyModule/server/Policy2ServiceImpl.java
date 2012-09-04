package bigBang.module.insurancePolicyModule.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import Jewel.Engine.Engine;
import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.Object2;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.interfaces.Policy2Service;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public class Policy2ServiceImpl
	extends InsurancePolicyServiceImpl
	implements Policy2Service
{
	private static final long serialVersionUID = 1L;
	
	private static class FieldStructure
	{
		public Coverage[] marrCoverages;
		public Tax[][] marrFields;
	}

	public static FieldContainer.FieldType sGetFieldTypeByID(UUID pidFieldType)
	{
		if ( Constants.FieldID_Boolean.equals(pidFieldType) )
			return FieldContainer.FieldType.BOOLEAN;
		if ( Constants.FieldID_Date.equals(pidFieldType) )
			return FieldContainer.FieldType.DATE;
		if ( Constants.FieldID_List.equals(pidFieldType) )
			return FieldContainer.FieldType.LIST;
		if ( Constants.FieldID_Number.equals(pidFieldType) )
			return FieldContainer.FieldType.NUMERIC;
		if ( Constants.FieldID_Reference.equals(pidFieldType) )
			return FieldContainer.FieldType.REFERENCE;
		if ( Constants.FieldID_Text.equals(pidFieldType) )
			return FieldContainer.FieldType.TEXT;
		return null;
	}

	public static InsurancePolicy sGetEmptyPolicy(UUID pidSubLine)
		throws BigBangException
	{
		SubLine lobjSubLine;
		FieldStructure lobjStructure;
		InsurancePolicy lobjResult;
		ComplexFieldContainer.ExerciseData lobjExercise;

		try
		{
			lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), pidSubLine);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjStructure = sGetSubLineStructure(lobjSubLine);

		lobjResult = sGetPolicyStructure(lobjStructure);

		if ( Constants.ExID_None.equals(lobjSubLine.getExerciseType()) )
		{
			lobjResult.hasExercises = false;
			lobjResult.exerciseData = null;
		}
		else
		{
			lobjResult.hasExercises = true;

			lobjExercise = sGetExerciseStructure(lobjStructure,
					(Constants.ExID_Variable.equals(lobjSubLine.getExerciseType()) ?  "Período Inicial" : "1º Ano"));
			
			lobjResult.exerciseData = new ComplexFieldContainer.ExerciseData[] {lobjExercise};
		}

		return lobjResult;
	}

	public InsurancePolicy getEmptyPolicy(String subLineId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetEmptyPolicy(UUID.fromString(subLineId));
	}

	public Object2 getEmptyObject(String subLineId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public InsurancePolicy getPolicy2(String policyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public Object2 getPolicyObject(String objectId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}

	public InsurancePolicy editPolicy(InsurancePolicy policy)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return null;
	}
	
	private static FieldStructure sGetSubLineStructure(SubLine pobjSubLine)
		throws BigBangException
	{
		FieldStructure lobjResult;
		int i;

		lobjResult = new FieldStructure();

		try
		{
			lobjResult.marrCoverages = pobjSubLine.GetCurrentCoverages();
			sSortCoverages(lobjResult.marrCoverages);

			lobjResult.marrFields = new Tax[lobjResult.marrCoverages.length][];
			for ( i = 0; i < lobjResult.marrCoverages.length; i++ )
			{
				lobjResult.marrFields[i] = lobjResult.marrCoverages[i].GetCurrentTaxes();
				sSortFields(lobjResult.marrFields[i]);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}

	private static InsurancePolicy sGetPolicyStructure(FieldStructure pobjStructure)
		throws BigBangException
	{
		InsurancePolicy lobjResult;
		int llngMaxCol;
		int i, j, k, l;

		lobjResult = new InsurancePolicy();

		llngMaxCol = -1;
		for ( i = 0; i < pobjStructure.marrCoverages.length; i++ )
		{
			for ( j = 0; j < pobjStructure.marrFields[i].length; i++ )
			{
				if ( pobjStructure.marrFields[i][j].IsVisible() && (pobjStructure.marrFields[i][j].GetColumnOrder() > llngMaxCol) )
					llngMaxCol = pobjStructure.marrFields[i][j].GetColumnOrder();
			}
		}

		lobjResult.coverages = new InsurancePolicy.Coverage[pobjStructure.marrCoverages.length - 1];
		lobjResult.columns = new InsurancePolicy.ColumnHeader[llngMaxCol + 1];

		l = 0;
		for ( i = 0; i < pobjStructure.marrCoverages.length; i++ )
		{
			if ( pobjStructure.marrCoverages[i].IsHeader() )
			{
				lobjResult.coverages[l] = new InsurancePolicy.Coverage();
				lobjResult.coverages[l].coverageId = pobjStructure.marrCoverages[i].getKey().toString();
				lobjResult.coverages[l].coverageName = pobjStructure.marrCoverages[i].getLabel();
				lobjResult.coverages[l].mandatory = pobjStructure.marrCoverages[i].IsMandatory();
				lobjResult.coverages[l].order = pobjStructure.marrCoverages[i].GetOrder();
				lobjResult.coverages[l].presentInPolicy = ( lobjResult.coverages[i].mandatory ? true : null );
				l++;

				for ( j = 0; j < pobjStructure.marrFields[i].length; j++ )
				{
					if ( !pobjStructure.marrFields[i][j].IsVisible() )
						continue;

					k = pobjStructure.marrFields[i][j].GetColumnOrder();
					if ( (k >= 0) && (lobjResult.columns[k] == null) )
					{
						lobjResult.columns[k] = new InsurancePolicy.ColumnHeader();
						lobjResult.columns[k].label = pobjStructure.marrFields[i][j].getLabel();
						lobjResult.columns[k].type = sGetFieldTypeByID(pobjStructure.marrFields[i][j].GetFieldType());
						lobjResult.columns[k].unitsLabel = pobjStructure.marrFields[i][j].GetUnitsLabel();
						lobjResult.columns[k].refersToId = pobjStructure.marrFields[i][j].GetRefersToID().toString();
					}
				}
			}
		}

		sBuildFieldContainer(lobjResult, pobjStructure, false, false);

		return lobjResult;
	}
	
	private static ComplexFieldContainer.ExerciseData sGetExerciseStructure(FieldStructure pobjStructure, String pstrLabel)
	{
		ComplexFieldContainer.ExerciseData lobjResult;
		
		lobjResult = new ComplexFieldContainer.ExerciseData();
		lobjResult.label = pstrLabel;

		sBuildFieldContainer(lobjResult, pobjStructure, false, true);

		return lobjResult;
	}

	private static void sSortCoverages(Coverage[] parrCoverages)
	{
		Arrays.sort(parrCoverages, new Comparator<Coverage>()
		{
			public int compare(Coverage o1, Coverage o2)
			{
				if ( o1.IsMandatory() == o2.IsMandatory() )
				{
					if ( o1.GetOrder() == o2.GetOrder() )
						return o1.getKey().compareTo(o2.getKey());
					return o1.GetOrder() - o2.GetOrder();
				}
				if ( o1.IsMandatory() )
					return -1;
				return 1;
			}
		});
	}

	private static void sSortFields(Tax[] parrFields)
	{
		Arrays.sort(parrFields, new Comparator<Tax>()
		{
			public int compare(Tax o1, Tax o2)
			{
				if ( o1.IsVisible() == o2.IsVisible() )
				{
					if ( o1.GetColumnOrder() == o2.GetColumnOrder() )
					{
						if ( o1.GetFieldType().equals(o2.GetFieldType()) )
						{
							if ( (o1.GetRefersToID() == null && o1.GetRefersToID() == null) ||
									o1.GetRefersToID().equals(o1.GetRefersToID()) )
								return o1.getLabel().compareTo(o2.getLabel());
							if ( o1.GetRefersToID() == null )
								return -1;
							if ( o2.GetRefersToID() == null )
								return 1;
							return o1.GetRefersToID().compareTo(o2.GetRefersToID());
						}
						return o1.GetFieldType().compareTo(o2.GetFieldType());
					}
					if ( (o1.GetColumnOrder() < 0) || (o2.GetColumnOrder() < 0) )
						return o2.GetColumnOrder() - o1.GetColumnOrder();
					return o1.GetColumnOrder() - o2.GetColumnOrder();
				}
				if ( o1.IsVisible() )
					return -1;
				return 1;
			}
		});
	}

	private static void sBuildFieldContainer(FieldContainer pobjContainer, FieldStructure parrStructure, boolean pbObjVar, boolean pbExVar)
	{
		ArrayList<FieldContainer.HeaderField> larrHeaders;
		ArrayList<FieldContainer.ColumnField> larrColumns;
		ArrayList<FieldContainer.ExtraField> larrExtras;
		FieldContainer.HeaderField lobjHeader;
		FieldContainer.ColumnField lobjColumn;
		FieldContainer.ExtraField lobjExtra;
		int i, j;

		larrHeaders = new ArrayList<FieldContainer.HeaderField>();
		larrColumns = new ArrayList<FieldContainer.ColumnField>();
		larrExtras = new ArrayList<FieldContainer.ExtraField>();

		for ( i = 0; i < parrStructure.marrCoverages.length; i++ )
		{
			if ( !parrStructure.marrCoverages[i].IsHeader() )
			{
				for ( j = 0; j < parrStructure.marrFields[i].length; j++ )
				{
					if ( !parrStructure.marrFields[i][j].IsVisible() ||
							(parrStructure.marrFields[i][j].GetVariesByObject() != pbObjVar) ||
							(parrStructure.marrFields[i][j].GetVariesByExercise() != pbExVar) )
						continue;

					lobjHeader = new FieldContainer.HeaderField();
					sBuildField(lobjHeader, parrStructure.marrFields[i][j]);
					larrHeaders.add(lobjHeader);
				}
			}
			else
			{
				for ( j = 0; j < parrStructure.marrFields[i].length; j++ )
				{
					if ( !parrStructure.marrFields[i][j].IsVisible() ||
							(parrStructure.marrFields[i][j].GetVariesByObject() != pbObjVar) ||
							(parrStructure.marrFields[i][j].GetVariesByExercise() != pbExVar) )
						continue;

					if ( parrStructure.marrFields[i][j].GetColumnOrder() < 0 )
					{
						lobjExtra = new FieldContainer.ExtraField();
						sBuildField(lobjExtra, parrStructure.marrFields[i][j]);
						lobjExtra.coverageIndex = i;
						larrExtras.add(lobjExtra);
					}
					else
					{
						lobjColumn = new FieldContainer.ColumnField();
						sBuildField(lobjColumn, parrStructure.marrFields[i][j]);
						lobjColumn.coverageIndex = i;
						lobjColumn.columnIndex = parrStructure.marrFields[i][j].GetColumnOrder();
						larrColumns.add(lobjColumn);
					}
				}
			}
		}

		pobjContainer.headerFields = larrHeaders.toArray(new FieldContainer.HeaderField[larrHeaders.size()]);
		pobjContainer.columnFields = larrColumns.toArray(new FieldContainer.ColumnField[larrColumns.size()]);
		pobjContainer.extraFields = larrExtras.toArray(new FieldContainer.ExtraField[larrExtras.size()]);
	}
	
	private static void sBuildField(FieldContainer.HeaderField pobjResult, Tax pobjSource)
	{
		pobjResult.fieldId = pobjSource.getKey().toString();
		pobjResult.fieldName = pobjSource.getLabel();
		pobjResult.type = sGetFieldTypeByID(pobjSource.GetFieldType());
		pobjResult.unitsLabel = pobjSource.GetUnitsLabel();
		pobjResult.refersToId = pobjSource.GetRefersToID().toString();
		pobjResult.order = pobjSource.GetColumnOrder();

		pobjResult.value = pobjSource.GetDefaultValue();
	}
}
