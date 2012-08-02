package bigBang.module.insurancePolicyModule.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;
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
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;

public class PolicyObjectServiceImpl
	extends SearchServiceBase
	implements PolicyObjectService
{
	private static final long serialVersionUID = 1L;

	public InsuredObject getObject(String objectId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidObject;
		PolicyObject lobjObject;
		ObjectBase lobjZipCode;
		ObjectBase lobjType;
		InsuredObject lobjResult;
		Policy lobjPolicy;
		PolicyExercise[] larrExercises;
		PolicyCoverage[] larrCoverages;
		PolicyValue[] larrFixed;
		PolicyValue[][] larrVariable;
		int i, j, k;
		ArrayList<PolicyCoverage> larrLocalCoverages;
		PolicyCoverage lobjHeaderCoverage;
		ArrayList<InsuredObject.CoverageData.FixedField> larrAuxFixed;
		InsuredObject.CoverageData.FixedField lobjFixed;
		ArrayList<InsuredObject.CoverageData.VariableField> larrAuxVariable;
		InsuredObject.CoverageData.VariableField lobjVariable;
		Hashtable<UUID, InsuredObject.CoverageData.VariableField> larrValueMap;
		Hashtable<UUID, ArrayList<InsuredObject.CoverageData.VariableField.VariableValue>> larrAuxMap;
		InsuredObject.CoverageData.VariableField.VariableValue lobjValue;
		ArrayList<InsuredObject.CoverageData.VariableField.VariableValue> larrAuxValues;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidObject = UUID.fromString(objectId);

		try
		{
			lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), lidObject);
			if ( lobjObject.getAt(5) == null )
				lobjZipCode = null;
			else
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjObject.getAt(5));
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ObjectType),
					(UUID)lobjObject.getAt(2));
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjObject.getAt(1));

			larrExercises = lobjPolicy.GetCurrentExercises();
			java.util.Arrays.sort(larrExercises, new Comparator<PolicyExercise>()
			{
				public int compare(PolicyExercise o1, PolicyExercise o2)
				{
					if ( o1.getAt(2) == null )
					{
						if ( o2.getAt(2) == null )
							return 0;
						return 1;
					}
					if ( o2.getAt(2) == null )
						return -1;

					return ((Timestamp)o1.getAt(2)).compareTo((Timestamp)o2.getAt(2));
				}
			});

			larrCoverages = lobjPolicy.GetCurrentCoverages();

			larrFixed = lobjPolicy.GetCurrentKeyedValues(lidObject, null);

			larrVariable = new PolicyValue[larrExercises.length][];
			for ( i = 0; i < larrExercises.length; i++ )
				larrVariable[i] = lobjPolicy.GetCurrentKeyedValues(lidObject, larrExercises[i].getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new InsuredObject();
		lobjResult.id = lobjObject.getKey().toString();
		lobjResult.ownerId = ((UUID)lobjObject.getAt(1)).toString();
		lobjResult.unitIdentification = lobjObject.getLabel();
		if ( (lobjObject.getAt(3) != null) || (lobjObject.getAt(4) != null) || (lobjZipCode != null) )
		{
			lobjResult.address = new Address();
			lobjResult.address.street1 = (String)lobjObject.getAt(3);
			lobjResult.address.street2 = (String)lobjObject.getAt(4);
			if ( lobjZipCode != null )
			{
				lobjResult.address.zipCode = new ZipCode();
				lobjResult.address.zipCode.code = (String)lobjZipCode.getAt(0);
				lobjResult.address.zipCode.city = (String)lobjZipCode.getAt(1);
				lobjResult.address.zipCode.county = (String)lobjZipCode.getAt(2);
				lobjResult.address.zipCode.district = (String)lobjZipCode.getAt(3);
				lobjResult.address.zipCode.country = (String)lobjZipCode.getAt(4);
			}
			else
				lobjResult.address.zipCode = null;
		}
		else
			lobjResult.address = null;
		lobjResult.inclusionDate = ( lobjObject.getAt(6) == null ? null :
				((Timestamp)lobjObject.getAt(6)).toString().substring(0, 10) );
		lobjResult.exclusionDate = ( lobjObject.getAt(7) == null ? null :
			((Timestamp)lobjObject.getAt(7)).toString().substring(0, 10) );
		lobjResult.typeId = lobjType.getKey().toString();
		lobjResult.typeText = lobjType.getLabel();

		if ( Constants.ObjTypeID_Person.equals(lobjType.getKey()) )
		{
			lobjResult.taxNumberPerson = (String)lobjObject.getAt(8);
			lobjResult.genderId = ( lobjObject.getAt(9) == null ? null : ((UUID)lobjObject.getAt(9)).toString() );
			lobjResult.birthDate = ( lobjObject.getAt(10) == null ? null :
					((Timestamp)lobjObject.getAt(10)).toString().substring(0, 10) );
			lobjResult.clientNumberPerson = ( lobjObject.getAt(11) == null ? null : ((Integer)lobjObject.getAt(11)).toString() );
			lobjResult.insuranceCompanyInternalIdPerson = (String)lobjObject.getAt(12);
		}

		if ( Constants.ObjTypeID_Group.equals(lobjType.getKey()) )
		{
			lobjResult.taxNumberCompany = (String)lobjObject.getAt(13);
			lobjResult.caeId = ( lobjObject.getAt(14) == null ? null : ((UUID)lobjObject.getAt(14)).toString() );
			lobjResult.grievousCaeId = ( lobjObject.getAt(15) == null ? null : ((UUID)lobjObject.getAt(15)).toString() );
			lobjResult.activityNotes = (String)lobjObject.getAt(16);
			lobjResult.productNotes = (String)lobjObject.getAt(17);
			lobjResult.businessVolumeId = ( lobjObject.getAt(18) == null ? null : ((UUID)lobjObject.getAt(18)).toString() );
			lobjResult.europeanUnionEntity = (String)lobjObject.getAt(19);
			lobjResult.clientNumberGroup = ( lobjObject.getAt(20) == null ? null : ((Integer)lobjObject.getAt(20)).toString() );
		}

		if ( Constants.ObjTypeID_Equipment.equals(lobjType.getKey()) )
		{
			lobjResult.makeAndModel = (String)lobjObject.getAt(21);
			lobjResult.equipmentDescription = (String)lobjObject.getAt(22);
			lobjResult.firstRegistryDate = ( lobjObject.getAt(23) == null ? null :
				((Timestamp)lobjObject.getAt(23)).toString().substring(0, 10) );
			lobjResult.manufactureYear = ( lobjObject.getAt(24) == null ? null : ((Integer)lobjObject.getAt(24)).toString() );
			lobjResult.clientInternalId = (String)lobjObject.getAt(25);
			lobjResult.insuranceCompanyInternalIdVehicle = (String)lobjObject.getAt(26);
		}

		if ( Constants.ObjTypeID_Site.equals(lobjType.getKey()) )
		{
			lobjResult.siteDescription = (String)lobjObject.getAt(27);
		}

		if ( Constants.ObjTypeID_Animal.equals(lobjType.getKey()) )
		{
			lobjResult.species = (String)lobjObject.getAt(28);
			lobjResult.race = (String)lobjObject.getAt(29);
			lobjResult.birthYear = ( lobjObject.getAt(30) == null ? null : ((Integer)lobjObject.getAt(30)).toString() );
			lobjResult.cityRegistryNumber = (String)lobjObject.getAt(31);
			lobjResult.electronicIdTag = (String)lobjObject.getAt(32);
		}

		lobjResult.exercises = new InsuredObject.Exercise[larrExercises.length];
		for ( i = 0; i < larrExercises.length; i++ )
		{
			lobjResult.exercises[i] = new InsuredObject.Exercise();
			lobjResult.exercises[i].id = larrExercises[i].getKey().toString();
			lobjResult.exercises[i].label = larrExercises[i].getLabel();
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
				{
					if ( o1.GetCoverage().GetOrder() == o2.GetCoverage().GetOrder() )
						return o1.GetCoverage().getLabel().compareTo(o2.GetCoverage().getLabel());
					return o1.GetCoverage().GetOrder() - o2.GetCoverage().GetOrder();
				}
				if ( o1.GetCoverage().IsMandatory() )
					return -1;
				return 1;
			}
		});
		lobjResult.coverageData = new InsuredObject.CoverageData[larrCoverages.length];
		larrValueMap = new Hashtable<UUID, InsuredObject.CoverageData.VariableField>();
		larrAuxMap = new Hashtable<UUID, ArrayList<InsuredObject.CoverageData.VariableField.VariableValue>>();
		for ( i = 0; i < larrCoverages.length; i++ )
		{
			lobjResult.coverageData[i] = new InsuredObject.CoverageData();
			lobjResult.coverageData[i].coverageId = larrCoverages[i].GetCoverage().getKey().toString();
			lobjResult.coverageData[i].coverageLabel = larrCoverages[i].GetCoverage().getLabel();
			larrAuxFixed = new ArrayList<InsuredObject.CoverageData.FixedField>();
			for ( j = 0; j < larrFixed.length; j++ )
			{
				if ( !larrFixed[j].GetTax().GetCoverage().getKey().equals(larrCoverages[i].GetCoverage().getKey()) )
					continue;

				lobjFixed = new InsuredObject.CoverageData.FixedField();
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
					larrAuxFixed.toArray(new InsuredObject.CoverageData.FixedField[larrAuxFixed.size()]);
			java.util.Arrays.sort(lobjResult.coverageData[i].fixedFields, new Comparator<InsuredObject.CoverageData.FixedField>()
			{
				public int compare(InsuredObject.CoverageData.FixedField o1, InsuredObject.CoverageData.FixedField o2)
				{
					if ( o1.columnIndex == o2.columnIndex )
					{
						if ( o1.type == o2.type )
						{
							if ( o1.refersToId == o1.refersToId )
								return o1.fieldName.compareTo(o2.fieldName);
							return o1.refersToId.compareTo(o2.refersToId);
						}
						return o1.type.compareTo(o2.type);
					}
					if ( (o1.columnIndex < 0) || (o2.columnIndex < 0) )
						return o2.columnIndex - o1.columnIndex;
					return o1.columnIndex - o2.columnIndex;
				}
			});

			larrAuxVariable = new ArrayList<InsuredObject.CoverageData.VariableField>();
			for ( j = 0; j < larrExercises.length; j++ )
			{
				for ( k = 0; k < larrVariable[j].length; k++ )
				{
					if ( !larrVariable[j][k].GetTax().GetCoverage().getKey().equals(larrCoverages[i].GetCoverage().getKey()) )
						continue;

					lobjVariable = larrValueMap.get(larrVariable[j][k].GetTax().getKey());
					if ( lobjVariable == null )
					{
						lobjVariable = new InsuredObject.CoverageData.VariableField();
						lobjVariable.fieldId = larrVariable[j][k].GetTax().getKey().toString();
						lobjVariable.fieldName = larrVariable[j][k].GetTax().getLabel();
						lobjVariable.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrVariable[j][k].GetTax().GetFieldType());
						lobjVariable.unitsLabel = larrVariable[j][k].GetTax().GetUnitsLabel();
						lobjVariable.refersToId = ( larrVariable[j][k].GetTax().GetRefersToID() == null ? null :
								larrVariable[j][k].GetTax().GetRefersToID().toString() );
						lobjVariable.columnIndex = larrVariable[j][k].GetTax().GetColumnOrder();
						larrValueMap.put(larrVariable[j][k].GetTax().getKey(), lobjVariable);
						larrAuxMap.put(larrVariable[j][k].GetTax().getKey(),
								new ArrayList<InsuredObject.CoverageData.VariableField.VariableValue>());
						larrAuxVariable.add(lobjVariable);
					}
					lobjValue = new InsuredObject.CoverageData.VariableField.VariableValue();
					lobjValue.exerciseIndex = j;
					lobjValue.value = larrVariable[j][k].getLabel();
					larrAuxMap.get(larrVariable[j][k].GetTax().getKey()).add(lobjValue);
				}
			}
			lobjResult.coverageData[i].variableFields =
					larrAuxVariable.toArray(new InsuredObject.CoverageData.VariableField[larrAuxVariable.size()]);
			java.util.Arrays.sort(lobjResult.coverageData[i].variableFields,
					new Comparator<InsuredObject.CoverageData.VariableField>()
			{
				public int compare(InsuredObject.CoverageData.VariableField o1, InsuredObject.CoverageData.VariableField o2)
				{
					if ( o1.columnIndex == o2.columnIndex )
					{
						if ( o1.type == o2.type )
						{
							if ( o1.refersToId == o1.refersToId )
								return o1.fieldName.compareTo(o2.fieldName);
							return o1.refersToId.compareTo(o2.refersToId);
						}
						return o1.type.compareTo(o2.type);
					}
					if ( (o1.columnIndex < 0) || (o2.columnIndex < 0) )
						return o2.columnIndex - o1.columnIndex;
					return o1.columnIndex - o2.columnIndex;
				}
			});
			for ( j = 0; j < lobjResult.coverageData[i].variableFields.length; j++ )
			{
				larrAuxValues = larrAuxMap.get(UUID.fromString(lobjResult.coverageData[i].variableFields[j].fieldId));
				lobjResult.coverageData[i].variableFields[j].data =
						larrAuxValues.toArray(new InsuredObject.CoverageData.VariableField.VariableValue[larrAuxValues.size()]);
			}
		}
		if ( lobjHeaderCoverage != null )
		{
			lobjResult.headerData = new InsuredObject.CoverageData();
			larrAuxFixed = new ArrayList<InsuredObject.CoverageData.FixedField>();
			for ( j = 0; j < larrFixed.length; j++ )
			{
				if ( !larrFixed[j].GetTax().GetCoverage().getKey().equals(lobjHeaderCoverage.GetCoverage().getKey()) )
					continue;

				lobjFixed = new InsuredObject.CoverageData.FixedField();
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
					larrAuxFixed.toArray(new InsuredObject.CoverageData.FixedField[larrAuxFixed.size()]);
			java.util.Arrays.sort(lobjResult.headerData.fixedFields, new Comparator<InsuredObject.CoverageData.FixedField>()
			{
				public int compare(InsuredObject.CoverageData.FixedField o1, InsuredObject.CoverageData.FixedField o2)
				{
					if ( o1.columnIndex == o2.columnIndex )
					{
						if ( o1.type == o2.type )
						{
							if ( o1.refersToId == o1.refersToId )
								return o1.fieldName.compareTo(o2.fieldName);
							return o1.refersToId.compareTo(o2.refersToId);
						}
						return o1.type.compareTo(o2.type);
					}
					if ( (o1.columnIndex < 0) || (o2.columnIndex < 0) )
						return o2.columnIndex - o1.columnIndex;
					return o1.columnIndex - o2.columnIndex;
				}
			});

			larrAuxVariable = new ArrayList<InsuredObject.CoverageData.VariableField>();
			for ( j = 0; j < larrExercises.length; j++ )
			{
				for ( k = 0; k < larrVariable[j].length; k++ )
				{
					if ( !larrVariable[j][k].GetTax().GetCoverage().getKey().equals(lobjHeaderCoverage.GetCoverage().getKey()) )
						continue;

					lobjVariable = larrValueMap.get(larrVariable[j][k].GetTax().getKey());
					if ( lobjVariable == null )
					{
						lobjVariable = new InsuredObject.CoverageData.VariableField();
						lobjVariable.fieldId = larrVariable[j][k].GetTax().getKey().toString();
						lobjVariable.fieldName = larrVariable[j][k].GetTax().getLabel();
						lobjVariable.type = InsurancePolicyServiceImpl.GetFieldTypeByID(larrVariable[j][k].GetTax().GetFieldType());
						lobjVariable.unitsLabel = larrVariable[j][k].GetTax().GetUnitsLabel();
						lobjVariable.refersToId = ( larrVariable[j][k].GetTax().GetRefersToID() == null ? null :
								larrVariable[j][k].GetTax().GetRefersToID().toString() );
						lobjVariable.columnIndex = larrVariable[j][k].GetTax().GetColumnOrder();
						larrValueMap.put(larrVariable[j][k].GetTax().getKey(), lobjVariable);
						larrAuxMap.put(larrVariable[j][k].GetTax().getKey(),
								new ArrayList<InsuredObject.CoverageData.VariableField.VariableValue>());
						larrAuxVariable.add(lobjVariable);
					}
					lobjValue = new InsuredObject.CoverageData.VariableField.VariableValue();
					lobjValue.exerciseIndex = j;
					lobjValue.value = larrVariable[j][k].getLabel();
					larrAuxMap.get(larrVariable[j][k].GetTax().getKey()).add(lobjValue);
				}
			}
			lobjResult.headerData.variableFields =
					larrAuxVariable.toArray(new InsuredObject.CoverageData.VariableField[larrAuxVariable.size()]);
			java.util.Arrays.sort(lobjResult.headerData.variableFields,
					new Comparator<InsuredObject.CoverageData.VariableField>()
			{
				public int compare(InsuredObject.CoverageData.VariableField o1, InsuredObject.CoverageData.VariableField o2)
				{
					if ( o1.columnIndex == o2.columnIndex )
					{
						if ( o1.type == o2.type )
						{
							if ( o1.refersToId == o1.refersToId )
								return o1.fieldName.compareTo(o2.fieldName);
							return o1.refersToId.compareTo(o2.refersToId);
						}
						return o1.type.compareTo(o2.type);
					}
					if ( (o1.columnIndex < 0) || (o2.columnIndex < 0) )
						return o2.columnIndex - o1.columnIndex;
					return o1.columnIndex - o2.columnIndex;
				}
			});
			for ( j = 0; j < lobjResult.headerData.variableFields.length; j++ )
			{
				larrAuxValues = larrAuxMap.get(UUID.fromString(lobjResult.headerData.variableFields[j].fieldId));
				lobjResult.headerData.variableFields[j].data =
						larrAuxValues.toArray(new InsuredObject.CoverageData.VariableField.VariableValue[larrAuxValues.size()]);
			}
		}

		return lobjResult;
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
		lobjResult.ownerId = ((UUID)parrValues[1]).toString();
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
