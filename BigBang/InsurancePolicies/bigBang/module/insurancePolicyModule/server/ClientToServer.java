package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.Data.PolicyCoInsurerData;
import com.premiumminds.BigBang.Jewel.Data.PolicyCoverageData;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.PolicyExerciseData;
import com.premiumminds.BigBang.Jewel.Data.PolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.PolicyValueData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyCoverageData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyValueData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoInsurer;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyValue;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class ClientToServer
{
	private boolean mbForSubPolicy;
	private UUID midOwner;
	private UUID midParent;

	private class FieldContainerReader
	{
		private FieldContainer mobjContainer;
		private int mlngObject;
		private int mlngExercise;
		private ArrayList<PolicyValueData> marrPData;
		private ArrayList<SubPolicyValueData> marrSPData;
		private HashSet<UUID> msetDeletia;

		public FieldContainerReader withSource(boolean pbForSubPolicy)
			throws BigBangException
		{
			mbForSubPolicy = pbForSubPolicy;
			if ( mbForSubPolicy )
			{
				marrSPData = new ArrayList<SubPolicyValueData>();
				marrPData = null;
			}
			else
			{
				marrPData = new ArrayList<PolicyValueData>();
				marrSPData = null;
			}
			msetDeletia = new HashSet<UUID>();
			midOwner = null;
			midParent = null;

			return this;
		}
		
		public FieldContainerReader withSource(Policy pobjPolicy)
			throws BigBangException
		{
			PolicyValue[] larrValues;
			int i;

			mbForSubPolicy = false;
			marrPData = new ArrayList<PolicyValueData>();
			marrSPData = null;
			msetDeletia = new HashSet<UUID>();

			midOwner = pobjPolicy.getKey();
			midParent = midOwner;
			try
			{
				larrValues = pobjPolicy.GetCurrentValues();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			for ( i = 0; i < larrValues.length; i++ )
				msetDeletia.add(larrValues[i].getKey());

			return this;
		}
		
		public FieldContainerReader withSource(com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjSubPolicy)
			throws BigBangException
		{
			SubPolicyValue[] larrValues;
			int i;

			mbForSubPolicy = true;
			marrSPData = new ArrayList<SubPolicyValueData>();
			marrPData = null;
			msetDeletia = new HashSet<UUID>();

			midOwner = pobjSubPolicy.getKey();
			midParent = pobjSubPolicy.GetOwner().getKey();
			try
			{
				larrValues = pobjSubPolicy.GetCurrentValues();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			for ( i = 0; i < larrValues.length; i++ )
				msetDeletia.add(larrValues[i].getKey());

			return this;
		}

		public FieldContainerReader withContainer(FieldContainer pobjContainer, int plngObject, int plngExercise)
		{
			mobjContainer = pobjContainer;
			mlngObject = plngObject;
			mlngExercise = plngExercise;
			return this;
		}

		public FieldContainerReader read()
		{
			int i;

			for ( i = 0; i < mobjContainer.headerFields.length; i++ )
				readField(mobjContainer.headerFields[i]);

			for ( i = 0; i < mobjContainer.columnFields.length; i++ )
				readField(mobjContainer.columnFields[i]);

			for ( i = 0; i < mobjContainer.extraFields.length; i++ )
				readField(mobjContainer.extraFields[i]);

			return this;
		}

		public PolicyValueData[] resultP()
		{
			PolicyValueData lobjAux;

			for (UUID lid: msetDeletia)
			{
				lobjAux = new PolicyValueData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrPData.add(lobjAux);
			}

			return marrPData.toArray(new PolicyValueData[marrPData.size()]);
		}

		public SubPolicyValueData[] resultS()
		{
			SubPolicyValueData lobjAux;

			for (UUID lid: msetDeletia)
			{
				lobjAux = new SubPolicyValueData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrSPData.add(lobjAux);
			}

			return marrSPData.toArray(new SubPolicyValueData[marrSPData.size()]);
		}

		private void readField(FieldContainer.HeaderField pobjField)
		{
			if ( mbForSubPolicy )
				readSubPolicyField(pobjField);
			else readPolicyField(pobjField);
		}

		private void readPolicyField(FieldContainer.HeaderField pobjField)
		{
			PolicyValueData lobjAux;

			lobjAux = new PolicyValueData();

			if ( pobjField.valueId == null )
			{
				lobjAux.mid = null;
				lobjAux.mbNew = true;
			}
			else
			{
				lobjAux.mid = UUID.fromString(pobjField.valueId);
				msetDeletia.remove(lobjAux.mid);
				lobjAux.mbNew = false;
			}
			lobjAux.mbDeleted = false;

			lobjAux.mstrValue = pobjField.value;
			lobjAux.midOwner = midOwner;
			lobjAux.midField = UUID.fromString(pobjField.fieldId);
			lobjAux.mlngObject = mlngObject;
			lobjAux.mlngExercise = mlngExercise;

			marrPData.add(lobjAux);
		}

		private void readSubPolicyField(FieldContainer.HeaderField pobjField)
		{
			SubPolicyValueData lobjAux;

			lobjAux = new SubPolicyValueData();

			if ( pobjField.valueId == null )
			{
				lobjAux.mid = null;
				lobjAux.mbNew = true;
			}
			else
			{
				lobjAux.mid = UUID.fromString(pobjField.valueId);
				msetDeletia.remove(lobjAux.mid);
				lobjAux.mbNew = false;
			}
			lobjAux.mbDeleted = false;

			lobjAux.mstrValue = pobjField.value;
			lobjAux.midOwner = midOwner;
			lobjAux.midField = UUID.fromString(pobjField.fieldId);
			lobjAux.mlngObject = mlngObject;
			lobjAux.mlngExercise = mlngExercise;

			marrSPData.add(lobjAux);
		}
	}

	private FieldContainerReader mobjBaseReader;
	
	private FieldContainerReader getBaseReader()
	{
		if ( mobjBaseReader == null )
			mobjBaseReader = new FieldContainerReader();
		return mobjBaseReader;
	}

	private class ExerciseReader
	{
		private PolicyExerciseData mobjOutExercise;
		private ComplexFieldContainer.ExerciseData mobjExercise;
		private int mlngObject;
		private int mlngExercise;

		public ExerciseReader withContainer(ComplexFieldContainer.ExerciseData pobjExercise, int plngForObject, int plngForExercise)
		{
			mobjExercise = pobjExercise;
			mlngObject = plngForObject;
			mlngExercise = plngForExercise;
			return this;
		}

		public ExerciseReader read()
		{
			if ( mlngObject < 0 )
				readFullHeader();

			getBaseReader()
					.withContainer(mobjExercise, mlngObject, mlngExercise)
					.read();

			return this;
		}

		public PolicyExerciseData result()
		{
			return mobjOutExercise;
		}

		private void readFullHeader()
		{
			mobjOutExercise = new PolicyExerciseData();

			if ( (mobjExercise.id == null) || (mobjExercise.id.equals("forced")) )
			{
				mobjOutExercise.mid = null;
				mobjOutExercise.mbNew = true;
			}
			else
			{
				mobjOutExercise.mid = UUID.fromString(mobjExercise.id);
				mobjOutExercise.mbNew = false;
			}
			mobjOutExercise.mbDeleted = false;

			mobjOutExercise.mstrLabel = mobjExercise.label;
			mobjOutExercise.midOwner = midParent;
			mobjOutExercise.mdtStart = ( mobjExercise.startDate == null ? null : Timestamp.valueOf(mobjExercise.startDate + " 00:00:00.0") );
			mobjOutExercise.mdtEnd = ( mobjExercise.endDate == null ? null : Timestamp.valueOf(mobjExercise.endDate + " 00:00:00.0") );
		}
	}

	private class ComplexFieldContainerReader
	{
		private ComplexFieldContainer mobjContainer;
		private int mlngObject;
		private boolean[] marrActive;
		private ArrayList<PolicyExerciseData> marrData;

		public ComplexFieldContainerReader withContainer(ComplexFieldContainer pobjContainer, int plngObject)
		{
			mobjContainer = pobjContainer;
			mlngObject = plngObject;
			return this;
		}

		public ComplexFieldContainerReader read()
		{
			ExerciseReader lobjReader;
			int i, n;

			getBaseReader()
					.withContainer(mobjContainer, mlngObject, -1)
					.read();

			if ( !mobjContainer.hasExercises )
				return this;

			if ( mlngObject < 0 )
			{
				marrActive = new boolean[mobjContainer.exerciseData.length];
				marrData = new ArrayList<PolicyExerciseData>();
			}

			lobjReader = new ExerciseReader();

			n = 0;
			for ( i = 0; i < mobjContainer.exerciseData.length; i ++ )
			{
				if ( mlngObject < 0 )
					marrActive[i] = mobjContainer.exerciseData[i].isActive;

				if ( !marrActive[i] )
					continue;

				lobjReader
						.withContainer(mobjContainer.exerciseData[i], mlngObject, n)
						.read();
				n++;

				if ( mlngObject < 0 )
					marrData.add(lobjReader.result());
			}

			return this;
		}

		public PolicyExerciseData[] result()
		{
			if ( mlngObject < 0 )
				return marrData.toArray(new PolicyExerciseData[marrData.size()]);

			return null;
		}
	}

	private ComplexFieldContainerReader mobjComplexReader;

	private ComplexFieldContainerReader getComplexReader()
	{
		if ( mobjComplexReader == null )
			mobjComplexReader = new ComplexFieldContainerReader();
		return mobjComplexReader;
	}

	private class ObjectReader
	{
		private PolicyObjectData mobjOutPObject;
		private SubPolicyObjectData mobjOutSPObject;
		private InsuredObject mobjObject;
		private int mlngObject;

		public ObjectReader withContainer(InsuredObject pobjObject, int plngObject)
		{
			mobjObject = pobjObject;
			mlngObject = plngObject;
			return this;
		}

		public ObjectReader read()
			throws BigBangException
		{
			if ( mbForSubPolicy )
			{
				if ( InsuredObject.Change.DELETED.equals(mobjObject.change) )
					readSPDeleteHeader();
				else
				{
					getComplexReader()
							.withContainer(mobjObject, mlngObject)
							.read();
	
					readSPFullHeader();
				}
			}
			else
			{
				if ( InsuredObject.Change.DELETED.equals(mobjObject.change) )
					readPDeleteHeader();
				else
				{
					getComplexReader()
							.withContainer(mobjObject, mlngObject)
							.read();
	
					readPFullHeader();
				}
			}

			return this;
		}

		public PolicyObjectData resultP()
		{
			return mobjOutPObject;
		}

		public SubPolicyObjectData resultSP()
		{
			return mobjOutSPObject;
		}

		private void readPDeleteHeader()
		{
			if ( mobjObject.id == null )
				mobjOutPObject = null;
			else
			{
				mobjOutPObject = new PolicyObjectData();
				mobjOutPObject.mid = UUID.fromString(mobjObject.id);
				mobjOutPObject.mbNew = false;
				mobjOutPObject.mbDeleted = true;
			}
		}

		private void readSPDeleteHeader()
		{
			if ( mobjObject.id == null )
				mobjOutSPObject = null;
			else
			{
				mobjOutSPObject = new SubPolicyObjectData();
				mobjOutSPObject.mid = UUID.fromString(mobjObject.id);
				mobjOutSPObject.mbNew = false;
				mobjOutSPObject.mbDeleted = true;
			}
		}

		private void readPFullHeader()
			throws BigBangException
		{
			UUID lidZipCode;

			try
			{
				if ( (mobjObject.address != null) && (mobjObject.address.zipCode != null) )
					lidZipCode = ZipCodeBridge.GetZipCode(mobjObject.address.zipCode.code, mobjObject.address.zipCode.city,
							mobjObject.address.zipCode.county, mobjObject.address.zipCode.district,
							mobjObject.address.zipCode.country);
				else
					lidZipCode = null;
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			if ( InsuredObject.Change.MODIFIED.equals(mobjObject.change) )
				mobjOutPObject.mid = UUID.fromString(mobjObject.id);
			else
				mobjOutPObject.mid = null;

			mobjOutPObject.mstrName = mobjObject.unitIdentification;
			mobjOutPObject.midOwner = midOwner;
			mobjOutPObject.midType = UUID.fromString(mobjObject.typeId);
			if ( mobjObject.address != null )
			{
				mobjOutPObject.mstrAddress1 = mobjObject.address.street1;
				mobjOutPObject.mstrAddress2 = mobjObject.address.street2;
				if ( mobjObject.address.zipCode != null )
					mobjOutPObject.midZipCode = lidZipCode;
				else
					mobjOutPObject.midZipCode = null;
			}
			else
			{
				mobjOutPObject.mstrAddress1 = null;
				mobjOutPObject.mstrAddress2 = null;
				mobjOutPObject.midZipCode = null;
			}
			mobjOutPObject.mdtInclusion = ( mobjObject.inclusionDate == null ? null :
					Timestamp.valueOf(mobjObject.inclusionDate + " 00:00:00.0") );
			mobjOutPObject.mdtExclusion = ( mobjObject.exclusionDate == null ? null :
					Timestamp.valueOf(mobjObject.exclusionDate + " 00:00:00.0") );

			mobjOutPObject.mstrFiscalI = mobjObject.taxNumberPerson;
			mobjOutPObject.midSex = ( mobjObject.genderId == null ? null : UUID.fromString(mobjObject.genderId) );
			mobjOutPObject.mdtDateOfBirth = ( mobjObject.birthDate == null ? null :
					Timestamp.valueOf(mobjObject.birthDate + " 00:00:00.0") );
			mobjOutPObject.mlngClientNumberI = ( mobjObject.clientNumberPerson == null ? null :
					Integer.decode(mobjObject.clientNumberPerson) );
			mobjOutPObject.mstrInsurerIDI = mobjObject.insuranceCompanyInternalIdPerson;

			mobjOutPObject.mstrFiscalC = mobjObject.taxNumberCompany;
			mobjOutPObject.midPredomCAE = ( mobjObject.caeId == null ? null : UUID.fromString(mobjObject.caeId) );
			mobjOutPObject.midGrievousCAE = ( mobjObject.grievousCaeId == null ? null : UUID.fromString(mobjObject.grievousCaeId) );
			mobjOutPObject.mstrActivityNotes = mobjObject.activityNotes;
			mobjOutPObject.mstrProductNotes = mobjObject.productNotes;
			mobjOutPObject.midSales = ( mobjObject.businessVolumeId == null ? null : UUID.fromString(mobjObject.businessVolumeId) );
			mobjOutPObject.mstrEUEntity = mobjObject.europeanUnionEntity;
			mobjOutPObject.mlngClientNumberC = ( mobjObject.clientNumberGroup == null ? null :
					Integer.decode(mobjObject.clientNumberGroup) );

			mobjOutPObject.mstrMakeAndModel = mobjObject.makeAndModel;
			mobjOutPObject.mstrEquipmentNotes = mobjObject.equipmentDescription;
			mobjOutPObject.mdtFirstRegistry = ( mobjObject.firstRegistryDate == null ? null :
					Timestamp.valueOf(mobjObject.firstRegistryDate + " 00:00:00.0") );
			mobjOutPObject.mlngManufactureYear = ( mobjObject.manufactureYear == null ? null :
					Integer.decode(mobjObject.manufactureYear) );
			mobjOutPObject.mstrClientIDE = mobjObject.clientInternalId;
			mobjOutPObject.mstrInsurerIDE = mobjObject.insuranceCompanyInternalIdVehicle;

			mobjOutPObject.mstrSiteNotes = mobjObject.siteDescription;

			mobjOutPObject.mstrSpecies = mobjObject.species;
			mobjOutPObject.mstrRace = mobjObject.race;
			mobjOutPObject.mlngAge = ( mobjObject.birthYear == null ? null :
					Integer.decode(mobjObject.birthYear) );
			mobjOutPObject.mstrCityNumber = mobjObject.cityRegistryNumber;
			mobjOutPObject.mstrElectronicIDTag = mobjObject.electronicIdTag;
		}

		private void readSPFullHeader()
			throws BigBangException
		{
			UUID lidZipCode;

			try
			{
				if ( (mobjObject.address != null) && (mobjObject.address.zipCode != null) )
					lidZipCode = ZipCodeBridge.GetZipCode(mobjObject.address.zipCode.code, mobjObject.address.zipCode.city,
							mobjObject.address.zipCode.county, mobjObject.address.zipCode.district,
							mobjObject.address.zipCode.country);
				else
					lidZipCode = null;
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			if ( InsuredObject.Change.MODIFIED.equals(mobjObject.change) )
				mobjOutSPObject.mid = UUID.fromString(mobjObject.id);
			else
				mobjOutSPObject.mid = null;

			mobjOutSPObject.mstrName = mobjObject.unitIdentification;
			mobjOutSPObject.midOwner = midOwner;
			mobjOutSPObject.midType = UUID.fromString(mobjObject.typeId);
			if ( mobjObject.address != null )
			{
				mobjOutSPObject.mstrAddress1 = mobjObject.address.street1;
				mobjOutSPObject.mstrAddress2 = mobjObject.address.street2;
				if ( mobjObject.address.zipCode != null )
					mobjOutSPObject.midZipCode = lidZipCode;
				else
					mobjOutSPObject.midZipCode = null;
			}
			else
			{
				mobjOutSPObject.mstrAddress1 = null;
				mobjOutSPObject.mstrAddress2 = null;
				mobjOutSPObject.midZipCode = null;
			}
			mobjOutSPObject.mdtInclusion = ( mobjObject.inclusionDate == null ? null :
					Timestamp.valueOf(mobjObject.inclusionDate + " 00:00:00.0") );
			mobjOutSPObject.mdtExclusion = ( mobjObject.exclusionDate == null ? null :
					Timestamp.valueOf(mobjObject.exclusionDate + " 00:00:00.0") );

			mobjOutSPObject.mstrFiscalI = mobjObject.taxNumberPerson;
			mobjOutSPObject.midSex = ( mobjObject.genderId == null ? null : UUID.fromString(mobjObject.genderId) );
			mobjOutSPObject.mdtDateOfBirth = ( mobjObject.birthDate == null ? null :
					Timestamp.valueOf(mobjObject.birthDate + " 00:00:00.0") );
			mobjOutSPObject.mlngClientNumberI = ( mobjObject.clientNumberPerson == null ? null :
					Integer.decode(mobjObject.clientNumberPerson) );
			mobjOutSPObject.mstrInsurerIDI = mobjObject.insuranceCompanyInternalIdPerson;

			mobjOutSPObject.mstrFiscalC = mobjObject.taxNumberCompany;
			mobjOutSPObject.midPredomCAE = ( mobjObject.caeId == null ? null : UUID.fromString(mobjObject.caeId) );
			mobjOutSPObject.midGrievousCAE = ( mobjObject.grievousCaeId == null ? null : UUID.fromString(mobjObject.grievousCaeId) );
			mobjOutSPObject.mstrActivityNotes = mobjObject.activityNotes;
			mobjOutSPObject.mstrProductNotes = mobjObject.productNotes;
			mobjOutSPObject.midSales = ( mobjObject.businessVolumeId == null ? null : UUID.fromString(mobjObject.businessVolumeId) );
			mobjOutSPObject.mstrEUEntity = mobjObject.europeanUnionEntity;
			mobjOutSPObject.mlngClientNumberC = ( mobjObject.clientNumberGroup == null ? null :
					Integer.decode(mobjObject.clientNumberGroup) );

			mobjOutSPObject.mstrMakeAndModel = mobjObject.makeAndModel;
			mobjOutSPObject.mstrEquipmentNotes = mobjObject.equipmentDescription;
			mobjOutSPObject.mdtFirstRegistry = ( mobjObject.firstRegistryDate == null ? null :
					Timestamp.valueOf(mobjObject.firstRegistryDate + " 00:00:00.0") );
			mobjOutSPObject.mlngManufactureYear = ( mobjObject.manufactureYear == null ? null :
					Integer.decode(mobjObject.manufactureYear) );
			mobjOutSPObject.mstrClientIDE = mobjObject.clientInternalId;
			mobjOutSPObject.mstrInsurerIDE = mobjObject.insuranceCompanyInternalIdVehicle;

			mobjOutSPObject.mstrSiteNotes = mobjObject.siteDescription;

			mobjOutSPObject.mstrSpecies = mobjObject.species;
			mobjOutSPObject.mstrRace = mobjObject.race;
			mobjOutSPObject.mlngAge = ( mobjObject.birthYear == null ? null :
					Integer.decode(mobjObject.birthYear) );
			mobjOutSPObject.mstrCityNumber = mobjObject.cityRegistryNumber;
			mobjOutSPObject.mstrElectronicIDTag = mobjObject.electronicIdTag;
		}
	}

	private class StructuredReader
	{
		private StructuredFieldContainer mobjContainer;
		private ArrayList<PolicyCoverageData> marrPCData;
		private ArrayList<PolicyObjectData> marrPOData;
		private ArrayList<SubPolicyCoverageData> marrSPCData;
		private ArrayList<SubPolicyObjectData> marrSPOData;
		private HashSet<UUID> msetCDeletia;
		private HashSet<UUID> msetODeletia;

		public StructuredReader withSource(boolean pbForSubPolicy)
			throws BigBangException
		{
			if ( pbForSubPolicy )
			{
				marrSPCData = new ArrayList<SubPolicyCoverageData>();
				marrSPOData = new ArrayList<SubPolicyObjectData>();
				marrPCData = null;
				marrPOData = null;
			}
			else
			{
				marrPCData = new ArrayList<PolicyCoverageData>();
				marrPOData = new ArrayList<PolicyObjectData>();
				marrSPCData = null;
				marrSPOData = null;
			}
			msetCDeletia = new HashSet<UUID>();
			msetODeletia = new HashSet<UUID>();

			getBaseReader()
					.withSource(pbForSubPolicy);

			return this;
		}

		public StructuredReader withSource(Policy pobjPolicy)
			throws BigBangException
		{
			PolicyCoverage[] larrCovs;
			PolicyObject[] larrObjs;
			int i;

			try
			{
				larrCovs = pobjPolicy.GetCurrentCoverages();
				larrObjs = pobjPolicy.GetCurrentObjects();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			marrPCData = new ArrayList<PolicyCoverageData>();
			marrPOData = new ArrayList<PolicyObjectData>();
			msetCDeletia = new HashSet<UUID>();
			msetODeletia = new HashSet<UUID>();
			marrSPCData = null;
			marrSPOData = null;

			for ( i = 0; i < larrCovs.length; i++ )
				msetCDeletia.add(larrCovs[i].getKey());

			for ( i = 0; i < larrObjs.length; i++ )
				msetODeletia.add(larrObjs[i].getKey());

			getBaseReader().withSource(pobjPolicy);

			return this;
		}

		public StructuredReader withSource(com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjSubPolicy)
			throws BigBangException
		{
			SubPolicyCoverage[] larrCovs;
			SubPolicyObject[] larrObjs;
			int i;

			try
			{
				larrCovs = pobjSubPolicy.GetCurrentCoverages();
				larrObjs = pobjSubPolicy.GetCurrentObjects();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			marrSPCData = new ArrayList<SubPolicyCoverageData>();
			marrSPOData = new ArrayList<SubPolicyObjectData>();
			msetCDeletia = new HashSet<UUID>();
			msetODeletia = new HashSet<UUID>();
			marrPCData = null;
			marrPOData = null;

			for ( i = 0; i < larrCovs.length; i++ )
				msetCDeletia.add(larrCovs[i].getKey());

			for ( i = 0; i < larrObjs.length; i++ )
				msetODeletia.add(larrObjs[i].getKey());

			getBaseReader().withSource(pobjSubPolicy);

			return this;
		}

		public StructuredReader withContainer(StructuredFieldContainer pobjContainer)
		{
			mobjContainer = pobjContainer;
			return this;
		}

		public StructuredReader read()
			throws BigBangException
		{
			getComplexReader()
					.withContainer(mobjContainer, -1)
					.read();

			if ( mbForSubPolicy )
			{
				readSubPolicyCoverages();
				readSubPolicyObjects();
			}
			else
			{
				readPolicyCoverages();
				readPolicyObjects();
			}
			return this;
		}

		public PolicyCoverageData[] resultPC()
		{
			PolicyCoverageData lobjAux;

			for (UUID lid: msetCDeletia)
			{
				lobjAux = new PolicyCoverageData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrPCData.add(lobjAux);
			}

			return marrPCData.toArray(new PolicyCoverageData[marrPCData.size()]);
		}

		public SubPolicyCoverageData[] resultSPC()
		{
			SubPolicyCoverageData lobjAux;

			for (UUID lid: msetCDeletia)
			{
				lobjAux = new SubPolicyCoverageData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrSPCData.add(lobjAux);
			}

			return marrSPCData.toArray(new SubPolicyCoverageData[marrSPCData.size()]);
		}

		public PolicyObjectData[] resultPO()
		{
			PolicyObjectData lobjAux;

			for (UUID lid: msetODeletia)
			{
				lobjAux = new PolicyObjectData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrPOData.add(lobjAux);
			}

			return marrPOData.toArray(new PolicyObjectData[marrPOData.size()]);
		}

		public SubPolicyObjectData[] resultSPO()
		{
			SubPolicyObjectData lobjAux;

			for (UUID lid: msetODeletia)
			{
				lobjAux = new SubPolicyObjectData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrSPOData.add(lobjAux);
			}

			return marrSPOData.toArray(new SubPolicyObjectData[marrSPOData.size()]);
		}

		private void readPolicyCoverages()
		{
			PolicyCoverageData lobjAux;
			int i;

			for ( i = 0; i < mobjContainer.coverages.length; i++ )
			{
				lobjAux = new PolicyCoverageData();

				if ( lobjAux != null )
					marrPCData.add(lobjAux);
			}
		}

		private void readSubPolicyCoverages()
		{
			SubPolicyCoverageData lobjAux;
			int i;

			for ( i = 0; i < mobjContainer.coverages.length; i++ )
			{
				lobjAux = new SubPolicyCoverageData();

				if ( lobjAux != null )
					marrSPCData.add(lobjAux);
			}
		}

		private void readPolicyObjects()
			throws BigBangException
		{
			ObjectReader lobjReader;
			PolicyObjectData lobjAux;
			int i, n;

			lobjReader = new ObjectReader();

			n = 0;
			for ( i = 0; i < mobjContainer.changedObjects.length; i++ )
			{
				lobjAux = lobjReader
						.withContainer(mobjContainer.changedObjects[i], n)
						.read()
						.resultP();

				if ( lobjAux != null )
				{
					marrPOData.add(lobjAux);
					n++;
				}
			}
		}

		private void readSubPolicyObjects()
			throws BigBangException
		{
			ObjectReader lobjReader;
			SubPolicyObjectData lobjAux;
			int i, n;

			lobjReader = new ObjectReader();

			n = 0;
			for ( i = 0; i < mobjContainer.changedObjects.length; i++ )
			{
				lobjAux = lobjReader
						.withContainer(mobjContainer.changedObjects[i], n)
						.read()
						.resultSP();

				if ( lobjAux != null )
				{
					marrSPOData.add(lobjAux);
					n++;
				}
			}
		}
	}

	private StructuredReader mobjStructuredReader;

	private StructuredReader getStructuredReader()
	{
		if ( mobjStructuredReader == null )
			mobjStructuredReader = new StructuredReader();
		return mobjStructuredReader;
	}

	private class PolicyReader
	{
		private PolicyData mobjOutPolicy;
		private PolicyCoInsurer[] marrReference;
		private InsurancePolicy mobjPolicy;

		public PolicyReader withSource()
			throws BigBangException
		{
			getStructuredReader().withSource(false);
			return this;
		}

		public PolicyReader withSource(Policy pobjPolicy)
			throws BigBangException
		{
			getStructuredReader().withSource(pobjPolicy);
			return this;
		}

		public PolicyReader withContainer(InsurancePolicy pobjPolicy)
			throws BigBangException
		{
			mobjPolicy = pobjPolicy;

			getStructuredReader()
					.withContainer(pobjPolicy)
					.read();

			return this;
		}

		public PolicyReader read()
			throws BigBangException
		{
			readHeader();
			readCoInsurers();

			mobjOutPolicy.marrCoverages = getStructuredReader()
					.resultPC();
			mobjOutPolicy.marrObjects = getStructuredReader()
					.resultPO();
			mobjOutPolicy.marrExercises = getComplexReader()
					.result();
			mobjOutPolicy.marrValues = getBaseReader()
					.resultP();

			return this;
		}

		public PolicyData result()
		{
			return mobjOutPolicy;
		}

		private void readHeader()
		{
			mobjOutPolicy = new PolicyData();

			mobjOutPolicy.mid = ( mobjPolicy.id == null ? null : UUID.fromString(mobjPolicy.id) );

			mobjOutPolicy.midClient = ( mobjPolicy.clientId == null ? null : UUID.fromString(mobjPolicy.clientId) );
			mobjOutPolicy.mstrNumber = mobjPolicy.number;
			mobjOutPolicy.midCompany = UUID.fromString(mobjPolicy.insuranceAgencyId);
			mobjOutPolicy.midSubLine = UUID.fromString(mobjPolicy.subLineId);
			mobjOutPolicy.mdtBeginDate = ( mobjPolicy.startDate == null ? null :
					Timestamp.valueOf(mobjPolicy.startDate + " 00:00:00.0") );
			mobjOutPolicy.midDuration = UUID.fromString(mobjPolicy.durationId);
			mobjOutPolicy.midFractioning = UUID.fromString(mobjPolicy.fractioningId);
			mobjOutPolicy.mlngMaturityDay = mobjPolicy.maturityDay;
			mobjOutPolicy.mlngMaturityMonth = mobjPolicy.maturityMonth;
			mobjOutPolicy.mdtEndDate = ( mobjPolicy.expirationDate == null ? null :
					Timestamp.valueOf(mobjPolicy.expirationDate + " 00:00:00.0") );
			mobjOutPolicy.mstrNotes = mobjPolicy.notes;
			mobjOutPolicy.midMediator = ( mobjPolicy.mediatorId == null ? null : UUID.fromString(mobjPolicy.mediatorId) );
			mobjOutPolicy.mbCaseStudy = mobjPolicy.caseStudy;
			mobjOutPolicy.midStatus = null;
			mobjOutPolicy.mdblPremium = ( mobjPolicy.premium == null ? null : new BigDecimal(mobjPolicy.premium+"") );
			mobjOutPolicy.mstrDocuShare = mobjPolicy.docushare;
			mobjOutPolicy.midProfile = ( mobjPolicy.operationalProfileId == null ? null : UUID.fromString(mobjPolicy.operationalProfileId) );

			mobjOutPolicy.midManager = ( mobjPolicy.managerId == null ? null : UUID.fromString(mobjPolicy.managerId) );
			mobjOutPolicy.midProcess = ( mobjPolicy.processId == null ? null : UUID.fromString(mobjPolicy.processId) );

			mobjOutPolicy.mbModified = true;
		}

		private void readCoInsurers()
		{
			HashMap<UUID, UUID> lmapCoInsurers;
			ArrayList<PolicyCoInsurerData> larrData;
			PolicyCoInsurerData lobjCoInsurer;
			int i;

			lmapCoInsurers = new HashMap<UUID, UUID>();

			if ( marrReference != null )
			{
				for ( i = 0; i < marrReference.length; i++ )
					lmapCoInsurers.put((UUID)marrReference[i].getAt(1), marrReference[i].getKey());
			}

			larrData = new ArrayList<PolicyCoInsurerData>();

			if ( mobjPolicy.coInsurers != null )
			{
				for ( i = 0; i < mobjPolicy.coInsurers.length; i++ )
				{
					lobjCoInsurer = new PolicyCoInsurerData();
					lobjCoInsurer.midPolicy = mobjOutPolicy.mid;
					lobjCoInsurer.midCompany = UUID.fromString(mobjPolicy.coInsurers[i].insuranceAgencyId);
					lobjCoInsurer.mdblPercent = new BigDecimal(mobjPolicy.coInsurers[i].percent+"");
					lobjCoInsurer.mbDeleted = false;

					lobjCoInsurer.mid = lmapCoInsurers.get(lobjCoInsurer.midCompany);
					if ( lobjCoInsurer.mid == null )
						lobjCoInsurer.mbNew = true;
					else
					{
						lobjCoInsurer.mbNew = false;
						lmapCoInsurers.remove(lobjCoInsurer.midCompany);
					}
					
					larrData.add(lobjCoInsurer);
				}
			}

			for ( UUID lid: lmapCoInsurers.keySet() )
			{
				lobjCoInsurer = new PolicyCoInsurerData();
				lobjCoInsurer.mid = lmapCoInsurers.get(lid);
				lobjCoInsurer.mbNew = false;
				lobjCoInsurer.mbDeleted = true;
				larrData.add(lobjCoInsurer);
			}

			mobjOutPolicy.marrCoInsurers = larrData.toArray(new PolicyCoInsurerData[larrData.size()]);
		}
	}

	private class SubPolicyReader
	{
		private SubPolicyData mobjOutSubPolicy;
		private SubPolicy mobjSubPolicy;

		public SubPolicyReader withSource()
			throws BigBangException
		{
			getStructuredReader().withSource(true);
			return this;
		}

		public SubPolicyReader withSource(com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjSubPolicy)
			throws BigBangException
		{
			getStructuredReader().withSource(pobjSubPolicy);
			return this;
		}

		public SubPolicyReader withContainer(SubPolicy pobjPolicy)
			throws BigBangException
		{
			mobjSubPolicy = pobjPolicy;

			getStructuredReader()
					.withContainer(pobjPolicy)
					.read();

			return this;
		}

		public SubPolicyReader read()
			throws BigBangException
		{
			readHeader();

			mobjOutSubPolicy.marrCoverages = getStructuredReader()
					.resultSPC();
			mobjOutSubPolicy.marrObjects = getStructuredReader()
					.resultSPO();
			mobjOutSubPolicy.marrValues = getBaseReader()
					.resultS();

			return this;
		}

		public SubPolicyData result()
		{
			return mobjOutSubPolicy;
		}

		private void readHeader()
		{
			mobjOutSubPolicy = new SubPolicyData();

			mobjOutSubPolicy.mid = ( mobjSubPolicy.id == null ? null : UUID.fromString(mobjSubPolicy.id) );

			mobjOutSubPolicy.mstrNumber = mobjSubPolicy.number;
			mobjOutSubPolicy.midSubscriber = ( mobjSubPolicy.clientId == null ? null : UUID.fromString(mobjSubPolicy.clientId) );
			mobjOutSubPolicy.mdtBeginDate = ( mobjSubPolicy.startDate == null ? null :
					Timestamp.valueOf(mobjSubPolicy.startDate + " 00:00:00.0") );
			mobjOutSubPolicy.midFractioning = UUID.fromString(mobjSubPolicy.fractioningId);
			mobjOutSubPolicy.mdtEndDate = ( mobjSubPolicy.expirationDate == null ? null :
					Timestamp.valueOf(mobjSubPolicy.expirationDate + " 00:00:00.0") );
			mobjOutSubPolicy.mstrNotes = mobjSubPolicy.notes;
			mobjOutSubPolicy.midStatus = null;
			mobjOutSubPolicy.mdblPremium = ( mobjSubPolicy.premium == null ? null : new BigDecimal(mobjSubPolicy.premium+"") );
			mobjOutSubPolicy.mstrDocuShare = mobjSubPolicy.docushare;

			mobjOutSubPolicy.midManager = ( mobjSubPolicy.managerId == null ? null : UUID.fromString(mobjSubPolicy.managerId) );
			mobjOutSubPolicy.midProcess = ( mobjSubPolicy.processId == null ? null : UUID.fromString(mobjSubPolicy.processId) );

			mobjOutSubPolicy.mbModified = true;
		}
	}

	public PolicyData getPDataForCreate(InsurancePolicy pobjSource)
		throws BigBangException
	{
		return new PolicyReader()
				.withSource()
				.withContainer(pobjSource)
				.read()
				.result();
	}

	public PolicyData getPDataForEdit(Policy pobjOriginal, InsurancePolicy pobjSource)
		throws BigBangException
	{
		return new PolicyReader()
				.withSource(pobjOriginal)
				.withContainer(pobjSource)
				.read()
				.result();
	}

	public SubPolicyData getSPDataForCreate(SubPolicy pobjSource)
		throws BigBangException
	{
		return new SubPolicyReader()
				.withSource()
				.withContainer(pobjSource)
				.read()
				.result();
	}

	public SubPolicyData getSPDataForEdit(com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjOriginal, SubPolicy pobjSource)
		throws BigBangException
	{
		return new SubPolicyReader()
				.withSource(pobjOriginal)
				.withContainer(pobjSource)
				.read()
				.result();
	}
}
