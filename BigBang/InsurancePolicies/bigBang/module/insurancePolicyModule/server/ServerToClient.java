package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.Permission;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoInsurer;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public class ServerToClient
{
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

	private Policy mobjPolicy;
	private SubLine mobjSubLine;
	private Coverage[] marrCoverages;
	private Tax[][] marrFields;

	private class FieldContainerBuilder
	{
		private class FieldContents
		{
			public UUID midValue;
			public String mstrValue;
		}
		
		private class ContainerObject
		{
			private FieldContainer mobjContainer;
			private boolean mbForObject;
			private boolean mbForExercise;
		}

		private Stack<ContainerObject> marrStack;
		private FieldContainer mobjContainer;
		private boolean mbForObject;
		private boolean mbForExercise;
		private Map<UUID, FieldContents> mmapData;

		public FieldContainerBuilder withSource(SubLine pobjSubLine)
			throws BigBangException
		{
			int i;

			mobjSubLine = pobjSubLine;

			try
			{
				marrCoverages = mobjSubLine.GetCurrentCoverages();
				sortCoverages(marrCoverages);

				marrFields = new Tax[marrCoverages.length][];
				for ( i = 0; i < marrCoverages.length; i++ )
				{
					marrFields[i] = marrCoverages[i].GetCurrentTaxes();
					sortFields(marrFields[i]);
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			return this;
		}
		
		public FieldContainerBuilder withSource(Policy pobjPolicy)
			throws BigBangException
		{
			mobjPolicy = pobjPolicy;
			return withSource(mobjPolicy.GetSubLine());
		}

		public FieldContainerBuilder withContainer(FieldContainer pobjContainer, boolean pbForObject, boolean pbForExercise)
		{
			ContainerObject lobjAux;

			if ( marrStack == null )
				marrStack = new Stack<ContainerObject>();
			else
			{
				lobjAux = new ContainerObject();
				lobjAux.mobjContainer = mobjContainer;
				lobjAux.mbForObject = mbForObject;
				lobjAux.mbForExercise = mbForExercise;
				marrStack.push(lobjAux);
			}

			mobjContainer = pobjContainer;
			mbForObject = pbForObject;
			mbForExercise = pbForExercise;
			return this;
		}

		public FieldContainerBuilder build()
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

			for ( i = 0; i < marrCoverages.length; i++ )
			{
				if ( marrCoverages[i].IsHeader() )
				{
					for ( j = 0; j < marrFields[i].length; j++ )
					{
						if ( !marrFields[i][j].IsVisible() ||
								(marrFields[i][j].GetVariesByObject() != mbForObject) ||
								(marrFields[i][j].GetVariesByExercise() != mbForExercise) )
							continue;

						lobjHeader = new FieldContainer.HeaderField();
						buildField(lobjHeader, marrFields[i][j]);
						larrHeaders.add(lobjHeader);
					}
				}
				else
				{
					for ( j = 0; j < marrFields[i].length; j++ )
					{
						if ( !marrFields[i][j].IsVisible() ||
								(marrFields[i][j].GetVariesByObject() != mbForObject) ||
								(marrFields[i][j].GetVariesByExercise() != mbForExercise) )
							continue;

						if ( marrFields[i][j].GetColumnOrder() < 0 )
						{
							lobjExtra = new FieldContainer.ExtraField();
							buildField(lobjExtra, marrFields[i][j]);
							lobjExtra.coverageIndex = i;
							larrExtras.add(lobjExtra);
						}
						else
						{
							lobjColumn = new FieldContainer.ColumnField();
							buildField(lobjColumn, marrFields[i][j]);
							lobjColumn.coverageIndex = i;
							lobjColumn.columnIndex = marrFields[i][j].GetColumnOrder();
							larrColumns.add(lobjColumn);
						}
					}
				}
			}

			mobjContainer.headerFields = larrHeaders.toArray(new FieldContainer.HeaderField[larrHeaders.size()]);
			mobjContainer.columnFields = larrColumns.toArray(new FieldContainer.ColumnField[larrColumns.size()]);
			mobjContainer.extraFields = larrExtras.toArray(new FieldContainer.ExtraField[larrExtras.size()]);

			return this;
		}

		public FieldContainerBuilder withDataFor(UUID pidObject, UUID pidExercise)
			throws BigBangException
		{
			PolicyValue[] larrData;
			FieldContents lobjAux;
			int i;

			if ( ((pidObject == null) == mbForObject) || ((pidExercise == null) == mbForExercise) )
			{
				mmapData = null;
				return this;
			}

			try
			{
				larrData = mobjPolicy.GetCurrentKeyedValues(pidObject, pidExercise);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			mmapData = new HashMap<UUID, FieldContents>();

			for ( i = 0; i < larrData.length; i++ )
			{
				lobjAux = new FieldContents();
				lobjAux.midValue = larrData[i].getKey();
				lobjAux.mstrValue = larrData[i].getLabel();
				mmapData.put(larrData[i].GetTax().getKey(), lobjAux);
			}

			return this;
		}

		public FieldContainerBuilder fill()
		{
			int i;

			if ( mmapData == null )
				return this;

			for ( i = 0; i < mobjContainer.headerFields.length; i++ )
				fillField(mobjContainer.headerFields[i]);

			for ( i = 0; i < mobjContainer.columnFields.length; i++ )
				fillField(mobjContainer.columnFields[i]);

			for ( i = 0; i < mobjContainer.extraFields.length; i++ )
				fillField(mobjContainer.extraFields[i]);

			return this;
		}

		public FieldContainer result()
		{
			FieldContainer lobjResult;
			ContainerObject lobjAux;

			if ( (marrStack == null) || marrStack.empty() )
				return mobjContainer;

			lobjResult = mobjContainer;

			lobjAux = marrStack.pop();
			mobjContainer = lobjAux.mobjContainer;
			mbForObject = lobjAux.mbForObject;
			mbForExercise = lobjAux.mbForExercise;

			return lobjResult;
		}

		private void buildField(FieldContainer.HeaderField pobjResult, Tax pobjSource)
		{
			pobjResult.fieldId = pobjSource.getKey().toString();
			pobjResult.fieldName = pobjSource.getLabel();
			pobjResult.type = sGetFieldTypeByID(pobjSource.GetFieldType());
			pobjResult.unitsLabel = pobjSource.GetUnitsLabel();
			pobjResult.refersToId = ( pobjSource.GetRefersToID() == null ? null : pobjSource.GetRefersToID().toString() );
			pobjResult.order = pobjSource.GetColumnOrder();

			pobjResult.readOnly = false;

			pobjResult.value = pobjSource.GetDefaultValue();
		}

		private void fillField(FieldContainer.HeaderField pobjField)
		{
			FieldContents lobjAux;

			lobjAux = mmapData.get(UUID.fromString(pobjField.fieldId));

			if ( lobjAux != null )
			{
				pobjField.valueId = lobjAux.midValue.toString();
				pobjField.value = lobjAux.mstrValue;
			}
		}

		private void sortCoverages(Coverage[] parrCoverages)
		{
			Arrays.sort(parrCoverages, new Comparator<Coverage>()
			{
				public int compare(Coverage o1, Coverage o2)
				{
					if ( o1.IsMandatory() == o2.IsMandatory() )
					{
						if ( o1.GetOrder() == o2.GetOrder() )
							return o1.getLabel().compareTo(o2.getLabel());
						return o1.GetOrder() - o2.GetOrder();
					}
					if ( o1.IsMandatory() )
						return -1;
					return 1;
				}
			});
		}

		private void sortFields(Tax[] parrFields)
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
	}

	private FieldContainerBuilder mobjBaseBuilder;
	
	private FieldContainerBuilder getBaseBuilder()
	{
		if ( mobjBaseBuilder == null )
			mobjBaseBuilder = new FieldContainerBuilder();
		return mobjBaseBuilder;
	}

	private class ExerciseBuilder
	{
		private boolean mbIsEmpty;
		private PolicyExercise mobjExercise;
		private String mstrLabel;
		private boolean mbForObject;
		private boolean mbActive;
		private ComplexFieldContainer.ExerciseData mobjOutExercise;

		public ExerciseBuilder withSource(String pstrLabel, boolean pbForObject)
		{
			mbIsEmpty = true;
			mobjExercise = null;
			mstrLabel = pstrLabel;
			mbForObject = pbForObject;

			mobjOutExercise = (ComplexFieldContainer.ExerciseData)getBaseBuilder()
					.withContainer(new ComplexFieldContainer.ExerciseData(), mbForObject, true)
					.build()
					.result();

			return this;
		}

		public ExerciseBuilder withSource(PolicyExercise pobjExercise, boolean pbForObject, UUID pidObject)
			throws BigBangException
		{
			mbIsEmpty = false;
			mobjExercise = pobjExercise;
			mstrLabel = null;
			mbForObject = pbForObject;

			mobjOutExercise = (ComplexFieldContainer.ExerciseData)getBaseBuilder()
					.withContainer(new ComplexFieldContainer.ExerciseData(), mbForObject, true)
					.build()
					.withDataFor(pidObject, mobjExercise.getKey())
					.fill()
					.result();

			return this;
		}

		public ExerciseBuilder withContainer(boolean pbActive)
		{
			mbActive = pbActive;
			return this;
		}

		public ExerciseBuilder build()
		{
			if ( mbIsEmpty )
				buildEmptyHeader();
			else
				buildFullHeader();
			mobjOutExercise.isActive = mbActive;

			return this;
		}

		public ComplexFieldContainer.ExerciseData result()
		{
			return mobjOutExercise;
		}

		private void buildEmptyHeader()
		{
			mobjOutExercise.label = mstrLabel;
		}

		private void buildFullHeader()
		{
			mobjOutExercise.label = mobjExercise.getLabel();
			mobjOutExercise.startDate = ( mobjExercise.getAt(2) == null ? null :
					((Timestamp)mobjExercise.getAt(2)).toString().substring(0, 10) );
			mobjOutExercise.endDate = ( mobjExercise.getAt(3) == null ? null :
					((Timestamp)mobjExercise.getAt(3)).toString().substring(0, 10) );
		}
	}

	private class ComplexFieldContainerBuilder
	{
		private ComplexFieldContainer mobjContainer;
		private UUID midExerciseType;
		private boolean mbIsEmpty;
		private boolean mbForObject;
		private UUID midObject;
		private PolicyExercise[] marrExercises;

		public ComplexFieldContainerBuilder withSource(SubLine pobjSubLine)
			throws BigBangException
		{
			mbIsEmpty = true;
			mbForObject = false;
			midObject = null;
			midExerciseType = pobjSubLine.getExerciseType();

			getBaseBuilder().withSource(pobjSubLine);
			return this;
		}

		public ComplexFieldContainerBuilder withSource(Policy pobjPolicy)
			throws BigBangException
		{
			mbIsEmpty = false;
			mbForObject = false;
			midObject = null;
			midExerciseType = pobjPolicy.GetSubLine().getExerciseType();
			try
			{
				marrExercises = pobjPolicy.GetCurrentExercises();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			getBaseBuilder().withSource(pobjPolicy);
			return this;
		}

		public ComplexFieldContainerBuilder withSource()
			throws BigBangException
		{
			mbIsEmpty = true;
			mbForObject = true;
			midObject = null;
			return this;
		}

		public ComplexFieldContainerBuilder withSource(PolicyObject pobjObject)
			throws BigBangException
		{
			mbIsEmpty = false;
			mbForObject = true;
			midObject = pobjObject.getKey();
			midExerciseType = pobjObject.GetOwner().GetSubLine().getExerciseType();
			try
			{
				marrExercises = pobjObject.GetOwner().GetCurrentExercises();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			getBaseBuilder().withSource(pobjObject.GetOwner());
			return this;
		}

		public ComplexFieldContainerBuilder withContainer(ComplexFieldContainer pobjContainer)
		{
			mobjContainer = pobjContainer;
			return this;
		}

		public ComplexFieldContainerBuilder build()
			throws BigBangException
		{
			if ( mbIsEmpty )
			{
				mobjContainer = (ComplexFieldContainer)getBaseBuilder()
						.withContainer(mobjContainer, mbForObject, false)
						.build()
						.result();
			}
			else
			{
				mobjContainer = (ComplexFieldContainer)getBaseBuilder()
						.withContainer(mobjContainer, mbForObject, false)
						.build()
						.withDataFor(midObject, null)
						.fill()
						.result();
			}

			return this;
		}

		public ComplexFieldContainerBuilder fill()
			throws BigBangException
		{
			ExerciseBuilder lobjAux;
			int i;

			if ( Constants.ExID_None.equals(midExerciseType) )
			{
				mobjContainer.hasExercises = false;
				mobjContainer.exerciseData = null;
			}
			else
			{
				lobjAux = new ExerciseBuilder();

				mobjContainer.hasExercises = true;

				if ( (marrExercises == null) || (marrExercises.length == 0) )
				{
					mobjContainer.exerciseData = new ComplexFieldContainer.ExerciseData[2];

					mobjContainer.exerciseData[0] = lobjAux
							.withSource(Constants.ExID_Variable.equals(midExerciseType) ? "Prorrogação" : "(Novo Exercício)", mbForObject)
							.withContainer(false)
							.build()
							.result();

					mobjContainer.exerciseData[1] = lobjAux
							.withSource(Constants.ExID_Variable.equals(midExerciseType) ? "Período Inicial" : "(1º Ano)", mbForObject)
							.withContainer(true)
							.build()
							.result();
					mobjContainer.exerciseData[1].id = "forced";
				}
				else
				{
					mobjContainer.exerciseData = new ComplexFieldContainer.ExerciseData[marrExercises.length + 1];

					mobjContainer.exerciseData[0] = lobjAux
							.withSource(Constants.ExID_Variable.equals(midExerciseType) ? "(Nova Prorrogação)" : "(Novo Exercício)", mbForObject)
							.withContainer(false)
							.build()
							.result();

					for ( i = 1; i <= marrExercises.length; i++ )
					{
						mobjContainer.exerciseData[i] = lobjAux
								.withSource(marrExercises[i - 1], mbForObject, midObject)
								.withContainer(true)
								.build()
								.result();
					}
				}
			}
			return this;
		}

		public ComplexFieldContainer result()
		{
			return mobjContainer;
		}
	}

	private ComplexFieldContainerBuilder mobjComplexBuilder;

	private ComplexFieldContainerBuilder getComplexBuilder()
	{
		if ( mobjComplexBuilder == null )
			mobjComplexBuilder = new ComplexFieldContainerBuilder();
		return mobjComplexBuilder;
	}

	private class ObjectBuilder
	{
		private boolean mbIsEmpty;
		private PolicyObject mobjObject;
		private UUID midObjectType;
		private InsuredObject mobjOutObject;

		public ObjectBuilder withSource()
			throws BigBangException
		{
			mbIsEmpty = true;
			mobjObject = null;
			midObjectType = mobjSubLine.getObjectType();

			mobjOutObject = (InsuredObject)getComplexBuilder()
					.withSource()
					.withContainer(new InsuredObject())
					.build()
					.fill()
					.result();

			return this;
		}

		public ObjectBuilder withSource(PolicyObject pobjObject)
			throws BigBangException
		{
			mbIsEmpty = false;
			mobjObject = pobjObject;
			midObjectType = null;

			mobjOutObject = (InsuredObject)getComplexBuilder()
					.withSource(pobjObject)
					.withContainer(new InsuredObject())
					.build()
					.fill()
					.result();

			return this;
		}

		public ObjectBuilder build()
			throws BigBangException
		{
			if ( mbIsEmpty )
				buildEmptyHeader();
			else
				buildFullHeader();

			return this;
		}

		public InsuredObject result()
		{
			return mobjOutObject;
		}

		private void buildEmptyHeader()
			throws BigBangException
		{
			ObjectBase lobjType;
			Line lobjLine;
			Category lobjCategory;

			try
			{
				lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ObjectType), midObjectType);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjLine = mobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();

			mobjOutObject.subLineName = mobjSubLine.getLabel();
			mobjOutObject.lineName = lobjLine.getLabel();
			mobjOutObject.categoryName = lobjCategory.getLabel();

			mobjOutObject.typeId = midObjectType.toString();
			mobjOutObject.typeText = lobjType.getLabel();
		}

		private void buildFullHeader()
			throws BigBangException
		{
			ObjectBase lobjZipCode;
			ObjectBase lobjType;
			Line lobjLine;
			Category lobjCategory;

			try
			{
				if ( mobjObject.getAt(5) == null )
					lobjZipCode = null;
				else
					lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
							(UUID)mobjObject.getAt(5));
				lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ObjectType),
						(UUID)mobjObject.getAt(2));
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
			lobjLine = mobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();

			mobjOutObject.unitIdentification = mobjObject.getLabel();
			if ( (mobjObject.getAt(3) != null) || (mobjObject.getAt(4) != null) || (lobjZipCode != null) )
			{
				mobjOutObject.address = new Address();
				mobjOutObject.address.street1 = (String)mobjObject.getAt(3);
				mobjOutObject.address.street2 = (String)mobjObject.getAt(4);
				if ( lobjZipCode != null )
				{
					mobjOutObject.address.zipCode = new ZipCode();
					mobjOutObject.address.zipCode.code = (String)lobjZipCode.getAt(0);
					mobjOutObject.address.zipCode.city = (String)lobjZipCode.getAt(1);
					mobjOutObject.address.zipCode.county = (String)lobjZipCode.getAt(2);
					mobjOutObject.address.zipCode.district = (String)lobjZipCode.getAt(3);
					mobjOutObject.address.zipCode.country = (String)lobjZipCode.getAt(4);
				}
				else
					mobjOutObject.address.zipCode = null;
			}
			mobjOutObject.inclusionDate = ( mobjObject.getAt(6) == null ? null :
					((Timestamp)mobjObject.getAt(6)).toString().substring(0, 10) );
			mobjOutObject.exclusionDate = ( mobjObject.getAt(7) == null ? null :
					((Timestamp)mobjObject.getAt(7)).toString().substring(0, 10) );
			mobjOutObject.typeId = lobjType.getKey().toString();
			mobjOutObject.typeText = lobjType.getLabel();

			mobjOutObject.subLineName = mobjSubLine.getLabel();
			mobjOutObject.lineName = lobjLine.getLabel();
			mobjOutObject.categoryName = lobjCategory.getLabel();

			mobjOutObject.id = mobjObject.getKey().toString();

			if ( Constants.ObjTypeID_Person.equals(lobjType.getKey()) )
			{
				mobjOutObject.taxNumberPerson = (String)mobjObject.getAt(8);
				mobjOutObject.genderId = ( mobjObject.getAt(9) == null ? null : ((UUID)mobjObject.getAt(9)).toString() );
				mobjOutObject.birthDate = ( mobjObject.getAt(10) == null ? null :
						((Timestamp)mobjObject.getAt(10)).toString().substring(0, 10) );
				mobjOutObject.clientNumberPerson = ( mobjObject.getAt(11) == null ? null : ((Integer)mobjObject.getAt(11)).toString() );
				mobjOutObject.insuranceCompanyInternalIdPerson = (String)mobjObject.getAt(12);
			}

			if ( Constants.ObjTypeID_Group.equals(lobjType.getKey()) )
			{
				mobjOutObject.taxNumberCompany = (String)mobjObject.getAt(13);
				mobjOutObject.caeId = ( mobjObject.getAt(14) == null ? null : ((UUID)mobjObject.getAt(14)).toString() );
				mobjOutObject.grievousCaeId = ( mobjObject.getAt(15) == null ? null : ((UUID)mobjObject.getAt(15)).toString() );
				mobjOutObject.activityNotes = (String)mobjObject.getAt(16);
				mobjOutObject.productNotes = (String)mobjObject.getAt(17);
				mobjOutObject.businessVolumeId = ( mobjObject.getAt(18) == null ? null : ((UUID)mobjObject.getAt(18)).toString() );
				mobjOutObject.europeanUnionEntity = (String)mobjObject.getAt(19);
				mobjOutObject.clientNumberGroup = ( mobjObject.getAt(20) == null ? null : ((Integer)mobjObject.getAt(20)).toString() );
			}

			if ( Constants.ObjTypeID_Equipment.equals(lobjType.getKey()) )
			{
				mobjOutObject.makeAndModel = (String)mobjObject.getAt(21);
				mobjOutObject.equipmentDescription = (String)mobjObject.getAt(22);
				mobjOutObject.firstRegistryDate = ( mobjObject.getAt(23) == null ? null :
						((Timestamp)mobjObject.getAt(23)).toString().substring(0, 10) );
				mobjOutObject.manufactureYear = ( mobjObject.getAt(24) == null ? null : ((Integer)mobjObject.getAt(24)).toString() );
				mobjOutObject.clientInternalId = (String)mobjObject.getAt(25);
				mobjOutObject.insuranceCompanyInternalIdVehicle = (String)mobjObject.getAt(26);
			}

			if ( Constants.ObjTypeID_Site.equals(lobjType.getKey()) )
			{
				mobjOutObject.siteDescription = (String)mobjObject.getAt(27);
			}

			if ( Constants.ObjTypeID_Animal.equals(lobjType.getKey()) )
			{
				mobjOutObject.species = (String)mobjObject.getAt(28);
				mobjOutObject.race = (String)mobjObject.getAt(29);
				mobjOutObject.birthYear = ( mobjObject.getAt(30) == null ? null : ((Integer)mobjObject.getAt(30)).toString() );
				mobjOutObject.cityRegistryNumber = (String)mobjObject.getAt(31);
				mobjOutObject.electronicIdTag = (String)mobjObject.getAt(32);
			}
		}
	}

	private class PolicyBuilder
	{
		private boolean mbIsEmpty;
		private Client mobjClient;
		private InsurancePolicy mobjOutPolicy;

		public PolicyBuilder withSource(SubLine pobjSubLine, Client pobjClient)
			throws BigBangException
		{
			mbIsEmpty = true;
			mobjClient = pobjClient;

			mobjOutPolicy = (InsurancePolicy)getComplexBuilder()
					.withSource(pobjSubLine)
					.withContainer(new InsurancePolicy())
					.build()
					.fill()
					.result();

			return this;
		}

		public PolicyBuilder withSource(Policy pobjPolicy)
			throws BigBangException
		{
			mbIsEmpty = false;
			mobjClient = null;

			mobjOutPolicy = (InsurancePolicy)getComplexBuilder()
					.withSource(pobjPolicy)
					.withContainer(new InsurancePolicy())
					.build()
					.fill()
					.result();

			return this;
		}

		public PolicyBuilder buildHeader()
			throws BigBangException
		{
			if ( mbIsEmpty )
				buildEmptyHeader();
			else
				buildFullHeader();

			return this;
		}
		
		public PolicyBuilder buildGrid()
		{
			int llngMaxCol;
			int i, j, k, l;

			llngMaxCol = -1;
			for ( i = 0; i < marrCoverages.length; i++ )
			{
				for ( j = 0; j < marrFields[i].length; j++ )
				{
					if ( marrFields[i][j].IsVisible() && (marrFields[i][j].GetColumnOrder() > llngMaxCol) )
						llngMaxCol = marrFields[i][j].GetColumnOrder();
				}
			}

			mobjOutPolicy.coverages = new InsurancePolicy.Coverage[marrCoverages.length - 1];
			mobjOutPolicy.columns = new InsurancePolicy.ColumnHeader[llngMaxCol + 1];

			l = 0;
			for ( i = 0; i < marrCoverages.length; i++ )
			{
				if ( !marrCoverages[i].IsHeader() )
				{
					mobjOutPolicy.coverages[l] = new InsurancePolicy.Coverage();
					mobjOutPolicy.coverages[l].coverageId = marrCoverages[i].getKey().toString();
					mobjOutPolicy.coverages[l].coverageName = marrCoverages[i].getLabel();
					mobjOutPolicy.coverages[l].mandatory = marrCoverages[i].IsMandatory();
					mobjOutPolicy.coverages[l].order = l;
					mobjOutPolicy.coverages[l].presentInPolicy = ( mobjOutPolicy.coverages[l].mandatory ? true : null );
					l++;

					for ( j = 0; j < marrFields[i].length; j++ )
					{
						if ( !marrFields[i][j].IsVisible() )
							continue;

						k = marrFields[i][j].GetColumnOrder();
						if ( (k >= 0) && (mobjOutPolicy.columns[k] == null) )
						{
							mobjOutPolicy.columns[k] = new InsurancePolicy.ColumnHeader();
							mobjOutPolicy.columns[k].label = marrFields[i][j].getLabel();
							mobjOutPolicy.columns[k].type = sGetFieldTypeByID(marrFields[i][j].GetFieldType());
							mobjOutPolicy.columns[k].unitsLabel = marrFields[i][j].GetUnitsLabel();
							mobjOutPolicy.columns[k].refersToId = ( marrFields[i][j].GetRefersToID() == null ? null :
									marrFields[i][j].GetRefersToID().toString() );
						}
					}
				}
			}

			return this;
		}

		public PolicyBuilder buildEmptyObject()
			throws BigBangException
		{
			mobjOutPolicy.emptyObject = new ObjectBuilder()
					.withSource()
					.build()
					.result();
			return this;
		}

		public InsurancePolicy result()
		{
			return mobjOutPolicy;
		}

		private void buildEmptyHeader()
		{
			Line lobjLine;
			Category lobjCategory;

			lobjLine = mobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();

			mobjOutPolicy.clientId = mobjClient.getKey().toString();
			mobjOutPolicy.clientNumber = ((Integer)mobjClient.getAt(1)).toString();
			mobjOutPolicy.clientName = mobjClient.getLabel();
			mobjOutPolicy.categoryId = lobjCategory.getKey().toString();
			mobjOutPolicy.categoryName = lobjCategory.getLabel();
			mobjOutPolicy.lineId = lobjLine.getKey().toString();
			mobjOutPolicy.lineName = lobjLine.getLabel();
			mobjOutPolicy.subLineId = mobjSubLine.getKey().toString();
			mobjOutPolicy.subLineName = mobjSubLine.getLabel();
		}

		private void buildFullHeader()
			throws BigBangException
		{
			IProcess lobjProc;
			Permission[] larrPerms;
			Line lobjLine;
			Category lobjCategory;
			String lstrObject;
			ObjectBase lobjStatus;
			Mediator lobjMed;
			PolicyCoInsurer[] larrCoInsurers;
			int i;

			try
			{
				lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjPolicy.GetProcessID());
				larrPerms = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());
				mobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
				lobjLine = mobjSubLine.getLine();
				lobjCategory = lobjLine.getCategory();
				lstrObject = mobjPolicy.GetObjectFootprint();
				lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
						(UUID)mobjPolicy.getAt(13));
				lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)mobjClient.getAt(8));
				larrCoInsurers = mobjPolicy.GetCurrentCoInsurers();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}


			mobjOutPolicy.id = mobjPolicy.getKey().toString();

			mobjOutPolicy.processId = lobjProc.getKey().toString();
			mobjOutPolicy.permissions = larrPerms;

			mobjOutPolicy.number = (String)mobjPolicy.getAt(0);
			mobjOutPolicy.clientId = mobjClient.getKey().toString();
			mobjOutPolicy.clientNumber = ((Integer)mobjClient.getAt(1)).toString();
			mobjOutPolicy.clientName = mobjClient.getLabel();
			mobjOutPolicy.categoryId = lobjCategory.getKey().toString();
			mobjOutPolicy.categoryName = lobjCategory.getLabel();
			mobjOutPolicy.lineId = lobjLine.getKey().toString();
			mobjOutPolicy.lineName = lobjLine.getLabel();
			mobjOutPolicy.subLineId = mobjPolicy.getKey().toString();
			mobjOutPolicy.subLineName = mobjPolicy.getLabel();
			mobjOutPolicy.insuredObject = lstrObject;
			mobjOutPolicy.caseStudy = (Boolean)mobjPolicy.getAt(12);
			mobjOutPolicy.statusId = lobjStatus.getKey().toString();
			mobjOutPolicy.statusText = lobjStatus.getLabel();
			switch ( (Integer)lobjStatus.getAt(1) )
			{
			case 0:
				mobjOutPolicy.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
				break;

			case 1:
				mobjOutPolicy.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
				break;

			case 2:
				mobjOutPolicy.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
				break;
			}

			mobjOutPolicy.managerId = lobjProc.GetManagerID().toString();
			mobjOutPolicy.insuranceAgencyId = ((UUID)mobjPolicy.getAt(2)).toString();
			mobjOutPolicy.startDate = (mobjPolicy.getAt(4) == null ? null :
					((Timestamp)mobjPolicy.getAt(4)).toString().substring(0, 10));
			mobjOutPolicy.durationId = ((UUID)mobjPolicy.getAt(5)).toString();
			mobjOutPolicy.fractioningId = ((UUID)mobjPolicy.getAt(6)).toString();
			mobjOutPolicy.maturityDay = (mobjPolicy.getAt(7) == null ? 0 : (Integer)mobjPolicy.getAt(7));
			mobjOutPolicy.maturityMonth = (mobjPolicy.getAt(8) == null ? 0 : (Integer)mobjPolicy.getAt(8));
			mobjOutPolicy.expirationDate = (mobjPolicy.getAt(9) == null ? null :
					((Timestamp)mobjPolicy.getAt(9)).toString().substring(0, 10));
			mobjOutPolicy.notes = (String)mobjPolicy.getAt(10);
			mobjOutPolicy.mediatorId = (mobjPolicy.getAt(11) == null ? null : ((UUID)mobjPolicy.getAt(11)).toString());
			mobjOutPolicy.inheritMediatorId = lobjMed.getKey().toString();
			mobjOutPolicy.inheritMediatorName = lobjMed.getLabel();
			mobjOutPolicy.premium = (mobjPolicy.getAt(14) == null ? null : ((BigDecimal)mobjPolicy.getAt(14)).doubleValue());
			mobjOutPolicy.operationalProfileId = (mobjPolicy.getAt(18) == null ? null : ((UUID)mobjPolicy.getAt(18)).toString());
			mobjOutPolicy.docushare = (String)mobjPolicy.getAt(15);

			if ( larrCoInsurers.length > 0 )
			{
				mobjOutPolicy.coInsurers = new InsurancePolicy.CoInsurer[larrCoInsurers.length];
				for ( i = 0; i < larrCoInsurers.length; i++ )
				{
					mobjOutPolicy.coInsurers[i] = new InsurancePolicy.CoInsurer();
					mobjOutPolicy.coInsurers[i].insuranceAgencyId = ((UUID)larrCoInsurers[i].getAt(1)).toString();
					mobjOutPolicy.coInsurers[i].percent = ((BigDecimal)larrCoInsurers[i].getAt(2)).doubleValue();
				}
			}
			else
				mobjOutPolicy.coInsurers = null;
		}
	}

	public InsurancePolicy getEmptyPolicy(UUID pidSubLine, UUID pidClient)
		throws BigBangException
	{
		SubLine lobjSubLine;
		Client lobjClient;

		try
		{
			lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), pidSubLine);
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), pidClient);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new PolicyBuilder()
				.withSource(lobjSubLine, lobjClient)
				.buildHeader()
				.buildGrid()
				.buildEmptyObject()
				.result();
	}

	public InsurancePolicy getPolicy(UUID pidPolicy)
		throws BigBangException
	{
		Policy lobjPolicy;

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), pidPolicy);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new PolicyBuilder()
				.withSource(lobjPolicy)
				.buildHeader()
				.buildGrid()
				.buildEmptyObject()
				.result();
	}

	public InsuredObject getObject(UUID pidObject)
		throws BigBangException
	{
		PolicyObject lobjObject;

		try
		{
			lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), pidObject);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new ObjectBuilder()
				.withSource(lobjObject)
				.build()
				.result();
	}
}
