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
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.Data.PolicyCoInsurerData;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.PolicyExerciseData;
import com.premiumminds.BigBang.Jewel.Data.PolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.PolicyValueData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoInsurer;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class ClientToServer
{
	private UUID midOwner;

	private class FieldContainerReader
	{
		private FieldContainer mobjContainer;
		private int mlngObject;
		private int mlngExercise;
		private ArrayList<PolicyValueData> marrData;
		private HashSet<UUID> msetDeletia;

		public FieldContainerReader withSource()
			throws BigBangException
		{
			marrData = new ArrayList<PolicyValueData>();
			msetDeletia = new HashSet<UUID>();
			midOwner = null;

			return this;
		}
		
		public FieldContainerReader withSource(Policy pobjPolicy)
			throws BigBangException
		{
			PolicyValue[] larrValues;
			int i;

			marrData = new ArrayList<PolicyValueData>();
			msetDeletia = new HashSet<UUID>();

			midOwner = pobjPolicy.getKey();
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

		public PolicyValueData[] result()
		{
			PolicyValueData lobjAux;

			for (UUID lid: msetDeletia)
			{
				lobjAux = new PolicyValueData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrData.add(lobjAux);
			}

			return marrData.toArray(new PolicyValueData[marrData.size()]);
		}

		private void readField(FieldContainer.HeaderField pobjField)
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

			marrData.add(lobjAux);
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
			mobjOutExercise.midOwner = midOwner;
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
		private PolicyObjectData mobjOutObject;
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
			if ( InsuredObject.Change.DELETED.equals(mobjObject.change) )
				readDeleteHeader();
			else
			{
				getComplexReader()
						.withContainer(mobjObject, mlngObject)
						.read();

				readFullHeader();
			}

			return this;
		}

		public PolicyObjectData result()
		{
			return mobjOutObject;
		}

		private void readDeleteHeader()
		{
			if ( mobjObject.id == null )
				mobjOutObject = null;
			else
			{
				mobjOutObject = new PolicyObjectData();
				mobjOutObject.mid = UUID.fromString(mobjObject.id);
				mobjOutObject.mbNew = false;
				mobjOutObject.mbDeleted = true;
			}
		}

		private void readFullHeader()
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
				mobjOutObject.mid = UUID.fromString(mobjObject.id);
			else
				mobjOutObject.mid = null;

			mobjOutObject.mstrName = mobjObject.unitIdentification;
			mobjOutObject.midOwner = midOwner;
			mobjOutObject.midType = UUID.fromString(mobjObject.typeId);
			if ( mobjObject.address != null )
			{
				mobjOutObject.mstrAddress1 = mobjObject.address.street1;
				mobjOutObject.mstrAddress2 = mobjObject.address.street2;
				if ( mobjObject.address.zipCode != null )
					mobjOutObject.midZipCode = lidZipCode;
				else
					mobjOutObject.midZipCode = null;
			}
			else
			{
				mobjOutObject.mstrAddress1 = null;
				mobjOutObject.mstrAddress2 = null;
				mobjOutObject.midZipCode = null;
			}
			mobjOutObject.mdtInclusion = ( mobjObject.inclusionDate == null ? null :
					Timestamp.valueOf(mobjObject.inclusionDate + " 00:00:00.0") );
			mobjOutObject.mdtExclusion = ( mobjObject.exclusionDate == null ? null :
					Timestamp.valueOf(mobjObject.exclusionDate + " 00:00:00.0") );

			mobjOutObject.mstrFiscalI = mobjObject.taxNumberPerson;
			mobjOutObject.midSex = ( mobjObject.genderId == null ? null : UUID.fromString(mobjObject.genderId) );
			mobjOutObject.mdtDateOfBirth = ( mobjObject.birthDate == null ? null :
					Timestamp.valueOf(mobjObject.birthDate + " 00:00:00.0") );
			mobjOutObject.mlngClientNumberI = ( mobjObject.clientNumberPerson == null ? null :
					Integer.decode(mobjObject.clientNumberPerson) );
			mobjOutObject.mstrInsurerIDI = mobjObject.insuranceCompanyInternalIdPerson;

			mobjOutObject.mstrFiscalC = mobjObject.taxNumberCompany;
			mobjOutObject.midPredomCAE = ( mobjObject.caeId == null ? null : UUID.fromString(mobjObject.caeId) );
			mobjOutObject.midGrievousCAE = ( mobjObject.grievousCaeId == null ? null : UUID.fromString(mobjObject.grievousCaeId) );
			mobjOutObject.mstrActivityNotes = mobjObject.activityNotes;
			mobjOutObject.mstrProductNotes = mobjObject.productNotes;
			mobjOutObject.midSales = ( mobjObject.businessVolumeId == null ? null : UUID.fromString(mobjObject.businessVolumeId) );
			mobjOutObject.mstrEUEntity = mobjObject.europeanUnionEntity;
			mobjOutObject.mlngClientNumberC = ( mobjObject.clientNumberGroup == null ? null :
					Integer.decode(mobjObject.clientNumberGroup) );

			mobjOutObject.mstrMakeAndModel = mobjObject.makeAndModel;
			mobjOutObject.mstrEquipmentNotes = mobjObject.equipmentDescription;
			mobjOutObject.mdtFirstRegistry = ( mobjObject.firstRegistryDate == null ? null :
					Timestamp.valueOf(mobjObject.firstRegistryDate + " 00:00:00.0") );
			mobjOutObject.mlngManufactureYear = ( mobjObject.manufactureYear == null ? null :
					Integer.decode(mobjObject.manufactureYear) );
			mobjOutObject.mstrClientIDE = mobjObject.clientInternalId;
			mobjOutObject.mstrInsurerIDE = mobjObject.insuranceCompanyInternalIdVehicle;

			mobjOutObject.mstrSiteNotes = mobjObject.siteDescription;

			mobjOutObject.mstrSpecies = mobjObject.species;
			mobjOutObject.mstrRace = mobjObject.race;
			mobjOutObject.mlngAge = ( mobjObject.birthYear == null ? null :
					Integer.decode(mobjObject.birthYear) );
			mobjOutObject.mstrCityNumber = mobjObject.cityRegistryNumber;
			mobjOutObject.mstrElectronicIDTag = mobjObject.electronicIdTag;
		}
	}

	private class PolicyReader
	{
		private PolicyData mobjOutPolicy;
		private PolicyCoInsurer[] marrReference;
		private InsurancePolicy mobjPolicy;

		public PolicyReader withSource()
			throws BigBangException
		{
			getBaseReader().withSource();
			return this;
		}

		public PolicyReader withSource(Policy pobjPolicy)
			throws BigBangException
		{
			getBaseReader().withSource(pobjPolicy);
			return this;
		}

		public PolicyReader withContainer(InsurancePolicy pobjPolicy)
		{
			mobjPolicy = pobjPolicy;
			return this;
		}

		public PolicyReader read()
			throws BigBangException
		{
			readHeader();
			readCoInsurers();

			mobjOutPolicy.marrExercises = getComplexReader()
					.withContainer(mobjPolicy, -1)
					.read()
					.result();

			readObjects();

			mobjOutPolicy.marrValues = getBaseReader()
					.result();

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

		private void readObjects()
			throws BigBangException
		{
			ArrayList<PolicyObjectData> larrData;
			PolicyObjectData lobjAux;
			ObjectReader lobjReader;
			int i, n;

			lobjReader = new ObjectReader();

			larrData = new ArrayList<PolicyObjectData>();

			n = 0;
			for ( i = 0; i < mobjPolicy.changedObjects.length; i++ )
			{
				lobjAux = lobjReader
						.withContainer(mobjPolicy.changedObjects[i], n)
						.read()
						.result();

				if ( lobjAux != null )
				{
					larrData.add(lobjAux);
					n++;
				}
			}

			mobjOutPolicy.marrObjects = larrData.toArray(new PolicyObjectData[larrData.size()]);
		}
	}

	public PolicyData getDataForCreate(InsurancePolicy pobjSource)
		throws BigBangException
	{
		return new PolicyReader()
				.withSource()
				.withContainer(pobjSource)
				.read()
				.result();
	}

	public PolicyData getDataForEdit(Policy pobjOriginal, InsurancePolicy pobjSource)
		throws BigBangException
	{
		return new PolicyReader()
				.withSource(pobjOriginal)
				.withContainer(pobjSource)
				.read()
				.result();
	}
}
