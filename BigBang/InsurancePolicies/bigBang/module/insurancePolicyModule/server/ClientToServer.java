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
import bigBang.definitions.shared.InsuredObjectStub;
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
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyValue;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class ClientToServer
{
	private UUID midOwner;
	private UUID midParent;
	private boolean mbForSubPolicy;
	private Policy mobjPolicy;
	private com.premiumminds.BigBang.Jewel.Objects.SubPolicy mobjSubPolicy;

	private class FieldContainerReader
	{
		private boolean mbIsEmpty;
		private ArrayList<PolicyValueData> marrPData;
		private ArrayList<SubPolicyValueData> marrSPData;
		private HashSet<UUID> msetDeletia;

		public void withOriginal(boolean pbForSubPolicy)
		{
			mbIsEmpty = true;
			mobjPolicy = null;
			mobjSubPolicy = null;

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
		}
		
		public void withOriginal(Policy pobjPolicy)
		{
			mbIsEmpty = false;
			mobjPolicy = pobjPolicy;
			mobjSubPolicy = null;

			mbForSubPolicy = false;
			marrPData = new ArrayList<PolicyValueData>();
			marrSPData = null;
			msetDeletia = new HashSet<UUID>();

			midOwner = pobjPolicy.getKey();
			midParent = midOwner;
		}
		
		public void withOriginal(com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjSubPolicy)
		{
			mbIsEmpty = false;
			mobjPolicy = null;
			mobjSubPolicy = pobjSubPolicy;

			mbForSubPolicy = true;
			marrSPData = new ArrayList<SubPolicyValueData>();
			marrPData = null;
			msetDeletia = new HashSet<UUID>();

			midOwner = pobjSubPolicy.getKey();
			midParent = pobjSubPolicy.GetOwner().getKey();
		}

		public void readValues(FieldContainer pobjContainer, UUID pidObject, int plngObject,
				UUID pidExercise, int plngExercise, boolean pbForceDelete)
			throws BigBangException
		{
			PolicyValue[] larrPValues;
			SubPolicyValue[] larrSPValues;
			int i;

			if ( !mbIsEmpty && ((plngObject < 0) == (pidObject == null)) && ((plngExercise < 0) == (pidExercise == null)) )
			{
				try
				{
					if ( mbForSubPolicy )
					{
						larrSPValues = mobjSubPolicy.GetCurrentKeyedValues(pidObject, pidExercise);
						for ( i = 0; i < larrSPValues.length; i++ )
							msetDeletia.add(larrSPValues[i].getKey());
					}
					else
					{
						larrPValues = mobjPolicy.GetCurrentKeyedValues(pidObject, pidExercise);
						for ( i = 0; i < larrPValues.length; i++ )
							msetDeletia.add(larrPValues[i].getKey());
					}
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}

			if ( !pbForceDelete )
			{
				for ( i = 0; i < pobjContainer.headerFields.length; i++ )
					readField(pobjContainer.headerFields[i], pidObject, plngObject, pidExercise, plngExercise);

				for ( i = 0; i < pobjContainer.columnFields.length; i++ )
					readField(pobjContainer.columnFields[i], pidObject, plngObject, pidExercise, plngExercise);

				for ( i = 0; i < pobjContainer.extraFields.length; i++ )
					readField(pobjContainer.extraFields[i], pidObject, plngObject, pidExercise, plngExercise);
			}
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

		private void readField(FieldContainer.HeaderField pobjField, UUID pidObject, int plngObject, UUID pidExercise, int plngExercise)
		{
			if ( mbForSubPolicy )
				readSubPolicyField(pobjField, pidObject, plngObject, pidExercise, plngExercise);
			else
				readPolicyField(pobjField, pidObject, plngObject, pidExercise, plngExercise);
		}

		private void readPolicyField(FieldContainer.HeaderField pobjField, UUID pidObject, int plngObject, UUID pidExercise, int plngExercise)
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
			lobjAux.midObject = pidObject;
			lobjAux.mlngObject = plngObject;
			lobjAux.midExercise = pidExercise;
			lobjAux.mlngExercise = plngExercise;

			marrPData.add(lobjAux);
		}

		private void readSubPolicyField(FieldContainer.HeaderField pobjField, UUID pidObject, int plngObject, UUID pidExercise, int plngExercise)
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
			lobjAux.midObject = pidObject;
			lobjAux.mlngObject = plngObject;
			lobjAux.midExercise = pidExercise;
			lobjAux.mlngExercise = plngExercise;

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

	private class ComplexFieldContainerReader
	{
		private boolean[] marrActive;
		private ArrayList<PolicyExerciseData> marrData;
		boolean mbFirst;

		public void withMaster(ComplexFieldContainer pobjContainer)
		{
			int i;

			if ( !pobjContainer.hasExercises )
			{
				marrData = null;
				return;
			}

			marrActive = new boolean[pobjContainer.exerciseData.length];
			marrData = new ArrayList<PolicyExerciseData>();

			for ( i = 0; i < pobjContainer.exerciseData.length; i ++ )
				marrActive[i] = pobjContainer.exerciseData[i].isActive;

			mbFirst = true;
		}

		public void readComplex(ComplexFieldContainer pobjContainer, UUID pidObject, int plngObject, boolean pbForceDelete)
			throws BigBangException
		{
			PolicyExerciseData lobjAux;
			int i, n;

			getBaseReader()
					.readValues(pobjContainer, pidObject, plngObject, null, -1, pbForceDelete);

			if ( !pobjContainer.hasExercises )
				return;

			n = 0;
			for ( i = 0; i < pobjContainer.exerciseData.length; i ++ )
			{
				if ( !marrActive[i] )
					continue;

				lobjAux = readExercise(pobjContainer.exerciseData[i], pidObject, plngObject, n, pbForceDelete);

				if ( mbFirst )
					marrData.add(lobjAux);

				n++;
			}

			mbFirst = false;
		}

		public PolicyExerciseData[] result()
		{
			return (marrData == null ? null : marrData.toArray(new PolicyExerciseData[marrData.size()]));
		}

		private PolicyExerciseData readExercise(ComplexFieldContainer.ExerciseData pobjExercise, UUID pidObject, int plngObject,
				int plngExercise, boolean pbForceDelete)
			throws BigBangException
		{
			PolicyExerciseData lobjOutExercise;
			UUID lidExercise;

			if ( ServerToClient.FORCEDID.equals(pobjExercise.id) )
				pobjExercise.id = null;
			lidExercise = (pobjExercise.id == null ? null : UUID.fromString(pobjExercise.id));

			getBaseReader()
					.readValues(pobjExercise, pidObject, plngObject, lidExercise, plngExercise, pbForceDelete);

			lobjOutExercise = new PolicyExerciseData();

			lobjOutExercise.mid = lidExercise;
			lobjOutExercise.mbNew = (lidExercise == null);
			lobjOutExercise.mbDeleted = false;
			lobjOutExercise.mstrLabel = pobjExercise.label;
			lobjOutExercise.midOwner = midParent;
			lobjOutExercise.mdtStart = ( pobjExercise.startDate == null ? null : Timestamp.valueOf(pobjExercise.startDate + " 00:00:00.0") );
			lobjOutExercise.mdtEnd = ( pobjExercise.endDate == null ? null : Timestamp.valueOf(pobjExercise.endDate + " 00:00:00.0") );

			return lobjOutExercise;
		}
	}

	private ComplexFieldContainerReader mobjComplexReader;

	private ComplexFieldContainerReader getComplexReader()
	{
		if ( mobjComplexReader == null )
			mobjComplexReader = new ComplexFieldContainerReader();
		return mobjComplexReader;
	}

	private class StructuredReader
	{
		private ArrayList<PolicyCoverageData> marrPCData;
		private ArrayList<PolicyObjectData> marrPOData;
		private ArrayList<SubPolicyCoverageData> marrSPCData;
		private ArrayList<SubPolicyObjectData> marrSPOData;
		private HashSet<UUID> msetCDeletia;
		private UUID midOwner;

		public void withOriginal(boolean pbForSubPolicy)
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
			midOwner = null;

			getBaseReader()
					.withOriginal(pbForSubPolicy);
		}

		public void withOriginal(Policy pobjPolicy)
			throws BigBangException
		{
			PolicyCoverage[] larrCovs;
			int i;

			try
			{
				larrCovs = pobjPolicy.GetCurrentCoverages();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			marrPCData = new ArrayList<PolicyCoverageData>();
			marrPOData = new ArrayList<PolicyObjectData>();
			msetCDeletia = new HashSet<UUID>();
			marrSPCData = null;
			marrSPOData = null;
			midOwner = pobjPolicy.getKey();

			for ( i = 0; i < larrCovs.length; i++ )
				msetCDeletia.add(larrCovs[i].getKey());

			getBaseReader()
					.withOriginal(pobjPolicy);
		}

		public void withOriginal(com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjSubPolicy)
			throws BigBangException
		{
			SubPolicyCoverage[] larrCovs;
			int i;

			try
			{
				larrCovs = pobjSubPolicy.GetCurrentCoverages();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			marrSPCData = new ArrayList<SubPolicyCoverageData>();
			marrSPOData = new ArrayList<SubPolicyObjectData>();
			msetCDeletia = new HashSet<UUID>();
			marrPCData = null;
			marrPOData = null;
			midOwner = pobjSubPolicy.getKey();

			for ( i = 0; i < larrCovs.length; i++ )
				msetCDeletia.add(larrCovs[i].getKey());

			getBaseReader()
					.withOriginal(pobjSubPolicy);
		}

		public void read(StructuredFieldContainer pobjContainer)
			throws BigBangException
		{
			ComplexFieldContainerReader lobjReader;

			lobjReader = getComplexReader();
			lobjReader.withMaster(pobjContainer);
			lobjReader.readComplex(pobjContainer, null, -1, false);

			if ( mbForSubPolicy )
			{
				readSubPolicyCoverages(pobjContainer);
				readSubPolicyObjects(pobjContainer);
			}
			else
			{
				readPolicyCoverages(pobjContainer);
				readPolicyObjects(pobjContainer);
			}
		}

		public PolicyCoverageData[] resultPC()
		{
			return marrPCData.toArray(new PolicyCoverageData[marrPCData.size()]);
		}

		public SubPolicyCoverageData[] resultSPC()
		{
			return marrSPCData.toArray(new SubPolicyCoverageData[marrSPCData.size()]);
		}

		public PolicyObjectData[] resultPO()
		{
			return marrPOData.toArray(new PolicyObjectData[marrPOData.size()]);
		}

		public SubPolicyObjectData[] resultSPO()
		{
			return marrSPOData.toArray(new SubPolicyObjectData[marrSPOData.size()]);
		}

		private void readPolicyCoverages(StructuredFieldContainer pobjContainer)
		{
			PolicyCoverageData lobjAux;
			int i;

			for ( i = 0; i < pobjContainer.coverages.length; i++ )
			{
				lobjAux = new PolicyCoverageData();

				if ( pobjContainer.coverages[i].serverId == null )
				{
					lobjAux.mid = null;
					lobjAux.mbNew = true;
				}
				else
				{
					lobjAux.mid = UUID.fromString(pobjContainer.coverages[i].serverId);
					lobjAux.mbNew = false;
					msetCDeletia.remove(lobjAux.mid);
				}
			
				lobjAux.midOwner = midOwner;
				lobjAux.midCoverage = UUID.fromString(pobjContainer.coverages[i].coverageId);
				lobjAux.mbPresent = pobjContainer.coverages[i].presentInPolicy;
				lobjAux.mbDeleted = false;

				marrPCData.add(lobjAux);
			}

			for (UUID lid: msetCDeletia)
			{
				lobjAux = new PolicyCoverageData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrPCData.add(lobjAux);
			}
		}

		private void readSubPolicyCoverages(StructuredFieldContainer pobjContainer)
		{
			SubPolicyCoverageData lobjAux;
			int i;

			for ( i = 0; i < pobjContainer.coverages.length; i++ )
			{
				lobjAux = new SubPolicyCoverageData();

				if ( pobjContainer.coverages[i].serverId == null )
				{
					lobjAux.mid = null;
					lobjAux.mbNew = true;
				}
				else
				{
					lobjAux.mid = UUID.fromString(pobjContainer.coverages[i].serverId);
					lobjAux.mbNew = true;
					msetCDeletia.remove(lobjAux.mid);
				}
			
				lobjAux.midOwner = midOwner;
				lobjAux.midCoverage = UUID.fromString(pobjContainer.coverages[i].coverageId);
				lobjAux.mbPresent = pobjContainer.coverages[i].presentInPolicy;
				lobjAux.mbDeleted = false;

				marrSPCData.add(lobjAux);
			}

			for (UUID lid: msetCDeletia)
			{
				lobjAux = new SubPolicyCoverageData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrSPCData.add(lobjAux);
			}
		}

		private void readPolicyObjects(StructuredFieldContainer pobjContainer)
			throws BigBangException
		{
			PolicyObjectData lobjAux;
			int i, n;

			n = 0;
			for ( i = 0; i < pobjContainer.changedObjects.length; i++ )
			{
				lobjAux = readPolicyObject(pobjContainer.changedObjects[i], n);

				if ( lobjAux != null )
				{
					marrPOData.add(lobjAux);
					n++;
				}
			}
		}

		private void readSubPolicyObjects(StructuredFieldContainer pobjContainer)
			throws BigBangException
		{
			SubPolicyObjectData lobjAux;
			int i, n;

			n = 0;
			for ( i = 0; i < pobjContainer.changedObjects.length; i++ )
			{
				lobjAux = readSubPolicyObject(pobjContainer.changedObjects[i], n);

				if ( lobjAux != null )
				{
					marrSPOData.add(lobjAux);
					n++;
				}
			}
		}

		private PolicyObjectData readPolicyObject(InsuredObject pobjObject, int plngObject)
			throws BigBangException
		{
			UUID lidObject;

			lidObject = (pobjObject.id == null ? null : UUID.fromString(pobjObject.id));

			getComplexReader()
					.readComplex(pobjObject, lidObject, plngObject, InsuredObjectStub.Change.DELETED.equals(pobjObject.change));

			if ( InsuredObjectStub.Change.DELETED.equals(pobjObject.change) )
				return readPDeleteHeader(pobjObject);

			return readPFullHeader(pobjObject);
		}

		private SubPolicyObjectData readSubPolicyObject(InsuredObject pobjObject, int plngObject)
			throws BigBangException
		{
			UUID lidObject;

			lidObject = (pobjObject.id == null ? null : UUID.fromString(pobjObject.id));

			getComplexReader()
					.readComplex(pobjObject, lidObject, plngObject, InsuredObjectStub.Change.DELETED.equals(pobjObject.change));

			if ( InsuredObjectStub.Change.DELETED.equals(pobjObject.change) )
				return readSPDeleteHeader(pobjObject);

			return readSPFullHeader(pobjObject);
		}

		private PolicyObjectData readPDeleteHeader(InsuredObject pobjObject)
		{
			PolicyObjectData lobjResult;

			if ( pobjObject.id == null )
				lobjResult = null;
			else
			{
				lobjResult = new PolicyObjectData();
				lobjResult.mid = UUID.fromString(pobjObject.id);
				lobjResult.mbNew = false;
				lobjResult.mbDeleted = true;
			}

			return lobjResult;
		}

		private SubPolicyObjectData readSPDeleteHeader(InsuredObject pobjObject)
		{
			SubPolicyObjectData lobjResult;

			if ( pobjObject.id == null )
				lobjResult = null;
			else
			{
				lobjResult = new SubPolicyObjectData();
				lobjResult.mid = UUID.fromString(pobjObject.id);
				lobjResult.mbNew = false;
				lobjResult.mbDeleted = true;
			}

			return lobjResult;
		}

		private PolicyObjectData readPFullHeader(InsuredObject pobjObject)
			throws BigBangException
		{
			UUID lidZipCode;
			PolicyObjectData lobjResult;

			try
			{
				if ( (pobjObject.address != null) && (pobjObject.address.zipCode != null) )
					lidZipCode = ZipCodeBridge.GetZipCode(pobjObject.address.zipCode.code, pobjObject.address.zipCode.city,
							pobjObject.address.zipCode.county, pobjObject.address.zipCode.district,
							pobjObject.address.zipCode.country);
				else
					lidZipCode = null;
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjResult = new PolicyObjectData();

			if ( InsuredObjectStub.Change.MODIFIED.equals(pobjObject.change) )
				lobjResult.mid = UUID.fromString(pobjObject.id);
			else
				lobjResult.mid = null;

			lobjResult.mstrName = pobjObject.unitIdentification;
			lobjResult.midOwner = midOwner;
			lobjResult.midType = UUID.fromString(pobjObject.typeId);
			if ( pobjObject.address != null )
			{
				lobjResult.mstrAddress1 = pobjObject.address.street1;
				lobjResult.mstrAddress2 = pobjObject.address.street2;
				if ( pobjObject.address.zipCode != null )
					lobjResult.midZipCode = lidZipCode;
				else
					lobjResult.midZipCode = null;
			}
			else
			{
				lobjResult.mstrAddress1 = null;
				lobjResult.mstrAddress2 = null;
				lobjResult.midZipCode = null;
			}
			lobjResult.mdtInclusion = ( pobjObject.inclusionDate == null ? null :
					Timestamp.valueOf(pobjObject.inclusionDate + " 00:00:00.0") );
			lobjResult.mdtExclusion = ( pobjObject.exclusionDate == null ? null :
					Timestamp.valueOf(pobjObject.exclusionDate + " 00:00:00.0") );

			lobjResult.mstrFiscalI = pobjObject.taxNumberPerson;
			lobjResult.midSex = ( pobjObject.genderId == null ? null : UUID.fromString(pobjObject.genderId) );
			lobjResult.mdtDateOfBirth = ( pobjObject.birthDate == null ? null :
					Timestamp.valueOf(pobjObject.birthDate + " 00:00:00.0") );
			lobjResult.mlngClientNumberI = ( pobjObject.clientNumberPerson == null ? null :
					Integer.decode(pobjObject.clientNumberPerson) );
			lobjResult.mstrInsurerIDI = pobjObject.insuranceCompanyInternalIdPerson;

			lobjResult.mstrFiscalC = pobjObject.taxNumberCompany;
			lobjResult.midPredomCAE = ( pobjObject.caeId == null ? null : UUID.fromString(pobjObject.caeId) );
			lobjResult.midGrievousCAE = ( pobjObject.grievousCaeId == null ? null : UUID.fromString(pobjObject.grievousCaeId) );
			lobjResult.mstrActivityNotes = pobjObject.activityNotes;
			lobjResult.mstrProductNotes = pobjObject.productNotes;
			lobjResult.midSales = ( pobjObject.businessVolumeId == null ? null : UUID.fromString(pobjObject.businessVolumeId) );
			lobjResult.mstrEUEntity = pobjObject.europeanUnionEntity;
			lobjResult.mlngClientNumberC = ( pobjObject.clientNumberGroup == null ? null :
					Integer.decode(pobjObject.clientNumberGroup) );

			lobjResult.mstrMakeAndModel = pobjObject.makeAndModel;
			lobjResult.mstrEquipmentNotes = pobjObject.equipmentDescription;
			lobjResult.mdtFirstRegistry = ( pobjObject.firstRegistryDate == null ? null :
					Timestamp.valueOf(pobjObject.firstRegistryDate + " 00:00:00.0") );
			lobjResult.mlngManufactureYear = ( pobjObject.manufactureYear == null ? null :
					Integer.decode(pobjObject.manufactureYear) );
			lobjResult.mstrClientIDE = pobjObject.clientInternalId;
			lobjResult.mstrInsurerIDE = pobjObject.insuranceCompanyInternalIdVehicle;

			lobjResult.mstrSiteNotes = pobjObject.siteDescription;

			lobjResult.mstrSpecies = pobjObject.species;
			lobjResult.mstrRace = pobjObject.race;
			lobjResult.mlngAge = ( pobjObject.birthYear == null ? null :
					Integer.decode(pobjObject.birthYear) );
			lobjResult.mstrCityNumber = pobjObject.cityRegistryNumber;
			lobjResult.mstrElectronicIDTag = pobjObject.electronicIdTag;

			return lobjResult;
		}

		private SubPolicyObjectData readSPFullHeader(InsuredObject pobjObject)
			throws BigBangException
		{
			UUID lidZipCode;
			SubPolicyObjectData lobjResult;

			try
			{
				if ( (pobjObject.address != null) && (pobjObject.address.zipCode != null) )
					lidZipCode = ZipCodeBridge.GetZipCode(pobjObject.address.zipCode.code, pobjObject.address.zipCode.city,
							pobjObject.address.zipCode.county, pobjObject.address.zipCode.district,
							pobjObject.address.zipCode.country);
				else
					lidZipCode = null;
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjResult = new SubPolicyObjectData();

			if ( InsuredObjectStub.Change.MODIFIED.equals(pobjObject.change) )
				lobjResult.mid = UUID.fromString(pobjObject.id);
			else
				lobjResult.mid = null;

			lobjResult.mstrName = pobjObject.unitIdentification;
			lobjResult.midOwner = midOwner;
			lobjResult.midType = UUID.fromString(pobjObject.typeId);
			if ( pobjObject.address != null )
			{
				lobjResult.mstrAddress1 = pobjObject.address.street1;
				lobjResult.mstrAddress2 = pobjObject.address.street2;
				if ( pobjObject.address.zipCode != null )
					lobjResult.midZipCode = lidZipCode;
				else
					lobjResult.midZipCode = null;
			}
			else
			{
				lobjResult.mstrAddress1 = null;
				lobjResult.mstrAddress2 = null;
				lobjResult.midZipCode = null;
			}
			lobjResult.mdtInclusion = ( pobjObject.inclusionDate == null ? null :
					Timestamp.valueOf(pobjObject.inclusionDate + " 00:00:00.0") );
			lobjResult.mdtExclusion = ( pobjObject.exclusionDate == null ? null :
					Timestamp.valueOf(pobjObject.exclusionDate + " 00:00:00.0") );

			lobjResult.mstrFiscalI = pobjObject.taxNumberPerson;
			lobjResult.midSex = ( pobjObject.genderId == null ? null : UUID.fromString(pobjObject.genderId) );
			lobjResult.mdtDateOfBirth = ( pobjObject.birthDate == null ? null :
					Timestamp.valueOf(pobjObject.birthDate + " 00:00:00.0") );
			lobjResult.mlngClientNumberI = ( pobjObject.clientNumberPerson == null ? null :
					Integer.decode(pobjObject.clientNumberPerson) );
			lobjResult.mstrInsurerIDI = pobjObject.insuranceCompanyInternalIdPerson;

			lobjResult.mstrFiscalC = pobjObject.taxNumberCompany;
			lobjResult.midPredomCAE = ( pobjObject.caeId == null ? null : UUID.fromString(pobjObject.caeId) );
			lobjResult.midGrievousCAE = ( pobjObject.grievousCaeId == null ? null : UUID.fromString(pobjObject.grievousCaeId) );
			lobjResult.mstrActivityNotes = pobjObject.activityNotes;
			lobjResult.mstrProductNotes = pobjObject.productNotes;
			lobjResult.midSales = ( pobjObject.businessVolumeId == null ? null : UUID.fromString(pobjObject.businessVolumeId) );
			lobjResult.mstrEUEntity = pobjObject.europeanUnionEntity;
			lobjResult.mlngClientNumberC = ( pobjObject.clientNumberGroup == null ? null :
					Integer.decode(pobjObject.clientNumberGroup) );

			lobjResult.mstrMakeAndModel = pobjObject.makeAndModel;
			lobjResult.mstrEquipmentNotes = pobjObject.equipmentDescription;
			lobjResult.mdtFirstRegistry = ( pobjObject.firstRegistryDate == null ? null :
					Timestamp.valueOf(pobjObject.firstRegistryDate + " 00:00:00.0") );
			lobjResult.mlngManufactureYear = ( pobjObject.manufactureYear == null ? null :
					Integer.decode(pobjObject.manufactureYear) );
			lobjResult.mstrClientIDE = pobjObject.clientInternalId;
			lobjResult.mstrInsurerIDE = pobjObject.insuranceCompanyInternalIdVehicle;

			lobjResult.mstrSiteNotes = pobjObject.siteDescription;

			lobjResult.mstrSpecies = pobjObject.species;
			lobjResult.mstrRace = pobjObject.race;
			lobjResult.mlngAge = ( pobjObject.birthYear == null ? null :
					Integer.decode(pobjObject.birthYear) );
			lobjResult.mstrCityNumber = pobjObject.cityRegistryNumber;
			lobjResult.mstrElectronicIDTag = pobjObject.electronicIdTag;

			return lobjResult;
		}
	}

	private class PolicyReader
	{
		public PolicyData readPolicy(InsurancePolicy pobjPolicy, Policy pobjOriginal)
			throws BigBangException
		{
			StructuredReader lobjReader;
			PolicyData lobjResult;

			lobjReader = new StructuredReader();
			if ( pobjOriginal == null )
				lobjReader.withOriginal(false);
			else
				lobjReader.withOriginal(pobjOriginal);
			lobjReader.read(pobjPolicy);

			lobjResult = new PolicyData();

			readHeader(lobjResult, pobjPolicy);

			lobjResult.marrCoInsurers =  readCoInsurers(pobjOriginal, pobjPolicy);
			lobjResult.marrCoverages = lobjReader.resultPC();
			lobjResult.marrObjects = lobjReader.resultPO();
			lobjResult.marrExercises = getComplexReader().result();
			lobjResult.marrValues = getBaseReader().resultP();

			return lobjResult;
		}

		private void readHeader(PolicyData pobjResult, InsurancePolicy pobjPolicy)
		{
			pobjResult.mid = ( pobjPolicy.id == null ? null : UUID.fromString(pobjPolicy.id) );

			pobjResult.midClient = ( pobjPolicy.clientId == null ? null : UUID.fromString(pobjPolicy.clientId) );
			pobjResult.mstrNumber = pobjPolicy.number;
			pobjResult.midCompany = UUID.fromString(pobjPolicy.insuranceAgencyId);
			pobjResult.midSubLine = UUID.fromString(pobjPolicy.subLineId);
			pobjResult.mdtBeginDate = ( pobjPolicy.startDate == null ? null :
					Timestamp.valueOf(pobjPolicy.startDate + " 00:00:00.0") );
			pobjResult.midDuration = UUID.fromString(pobjPolicy.durationId);
			pobjResult.midFractioning = UUID.fromString(pobjPolicy.fractioningId);
			pobjResult.mlngMaturityDay = pobjPolicy.maturityDay;
			pobjResult.mlngMaturityMonth = pobjPolicy.maturityMonth;
			pobjResult.mdtEndDate = ( pobjPolicy.expirationDate == null ? null :
					Timestamp.valueOf(pobjPolicy.expirationDate + " 00:00:00.0") );
			pobjResult.mstrNotes = pobjPolicy.notes;
			pobjResult.midMediator = ( pobjPolicy.mediatorId == null ? null : UUID.fromString(pobjPolicy.mediatorId) );
			pobjResult.mbCaseStudy = pobjPolicy.caseStudy;
			pobjResult.midStatus = null;
			pobjResult.mdblPremium = ( pobjPolicy.premium == null ? null : new BigDecimal(pobjPolicy.premium+"") );
			pobjResult.mstrDocuShare = pobjPolicy.docushare;
			pobjResult.midProfile = ( pobjPolicy.operationalProfileId == null ? null : UUID.fromString(pobjPolicy.operationalProfileId) );

			pobjResult.midManager = ( pobjPolicy.managerId == null ? null : UUID.fromString(pobjPolicy.managerId) );
			pobjResult.midProcess = ( pobjPolicy.processId == null ? null : UUID.fromString(pobjPolicy.processId) );

			pobjResult.mbModified = true;
		}

		private PolicyCoInsurerData[] readCoInsurers(Policy pobjOriginal, InsurancePolicy pobjPolicy)
			throws BigBangException
		{
			PolicyCoInsurer[] larrReference;
			HashMap<UUID, UUID> lmapCoInsurers;
			ArrayList<PolicyCoInsurerData> larrData;
			PolicyCoInsurerData lobjCoInsurer;
			int i;

			lmapCoInsurers = new HashMap<UUID, UUID>();

			if ( pobjOriginal != null )
			{
				try
				{
					larrReference = pobjOriginal.GetCurrentCoInsurers();
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
	
				for ( i = 0; i < larrReference.length; i++ )
					lmapCoInsurers.put((UUID)larrReference[i].getAt(1), larrReference[i].getKey());
			}

			larrData = new ArrayList<PolicyCoInsurerData>();

			if ( pobjPolicy.coInsurers != null )
			{
				for ( i = 0; i < pobjPolicy.coInsurers.length; i++ )
				{
					lobjCoInsurer = new PolicyCoInsurerData();
					lobjCoInsurer.midPolicy = (pobjOriginal == null ? null : pobjOriginal.getKey());
					lobjCoInsurer.midCompany = UUID.fromString(pobjPolicy.coInsurers[i].insuranceAgencyId);
					lobjCoInsurer.mdblPercent = new BigDecimal(pobjPolicy.coInsurers[i].percent+"");
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

			return larrData.toArray(new PolicyCoInsurerData[larrData.size()]);
		}
	}

	private class SubPolicyReader
	{
		public SubPolicyData readSubPolicy(SubPolicy pobjPolicy, com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjOriginal)
			throws BigBangException
		{
			StructuredReader lobjReader;
			SubPolicyData lobjResult;

			lobjReader = new StructuredReader();
			if ( pobjOriginal == null )
				lobjReader.withOriginal(false);
			else
				lobjReader.withOriginal(pobjOriginal);
			lobjReader.read(pobjPolicy);

			lobjResult = new SubPolicyData();

			readHeader(lobjResult, pobjPolicy);

			lobjResult.marrCoverages = lobjReader.resultSPC();
			lobjResult.marrObjects = lobjReader.resultSPO();
			lobjResult.marrValues = getBaseReader().resultS();

			return lobjResult;
		}

		private void readHeader(SubPolicyData pobjResult, SubPolicy pobjSubPolicy)
		{
			pobjResult = new SubPolicyData();

			pobjResult.mid = ( pobjSubPolicy.id == null ? null : UUID.fromString(pobjSubPolicy.id) );

			pobjResult.mstrNumber = pobjSubPolicy.number;
			pobjResult.midSubscriber = ( pobjSubPolicy.clientId == null ? null : UUID.fromString(pobjSubPolicy.clientId) );
			pobjResult.mdtBeginDate = ( pobjSubPolicy.startDate == null ? null :
					Timestamp.valueOf(pobjSubPolicy.startDate + " 00:00:00.0") );
			pobjResult.midFractioning = UUID.fromString(pobjSubPolicy.fractioningId);
			pobjResult.mdtEndDate = ( pobjSubPolicy.expirationDate == null ? null :
					Timestamp.valueOf(pobjSubPolicy.expirationDate + " 00:00:00.0") );
			pobjResult.mstrNotes = pobjSubPolicy.notes;
			pobjResult.midStatus = null;
			pobjResult.mdblPremium = ( pobjSubPolicy.premium == null ? null : new BigDecimal(pobjSubPolicy.premium+"") );
			pobjResult.mstrDocuShare = pobjSubPolicy.docushare;

			pobjResult.midManager = ( pobjSubPolicy.managerId == null ? null : UUID.fromString(pobjSubPolicy.managerId) );
			pobjResult.midProcess = ( pobjSubPolicy.processId == null ? null : UUID.fromString(pobjSubPolicy.processId) );

			pobjResult.mbModified = true;
		}
	}

	public PolicyData getPDataForCreate(InsurancePolicy pobjSource)
		throws BigBangException
	{
		return new PolicyReader().readPolicy(pobjSource, null);
	}

	public PolicyData getPDataForEdit(Policy pobjOriginal, InsurancePolicy pobjSource)
		throws BigBangException
	{
		return new PolicyReader().readPolicy(pobjSource, pobjOriginal);
	}

	public SubPolicyData getSPDataForCreate(SubPolicy pobjSource)
		throws BigBangException
	{
		return new SubPolicyReader().readSubPolicy(pobjSource, null);
	}

	public SubPolicyData getSPDataForEdit(com.premiumminds.BigBang.Jewel.Objects.SubPolicy pobjOriginal, SubPolicy pobjSource)
		throws BigBangException
	{
		return new SubPolicyReader().readSubPolicy(pobjSource, pobjOriginal);
	}
}
