package bigBang.module.quoteRequestModule.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.GlobalFieldContainer;
import bigBang.definitions.shared.Permission;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestCoverage;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestSubLine;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestValue;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;

public class ServerToClient
	extends bigBang.module.insurancePolicyModule.server.ServerToClient
{
	public static void buildQuoteRequestStub(QuoteRequestStub mobjDest, com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjSource)
		throws BigBangException
	{
		IProcess lobjProc;
		Permission[] larrPerms;
		Client lobjClient;

		try
		{
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), pobjSource.GetProcessID());
			larrPerms = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		mobjDest.id = pobjSource.getKey().toString();

		mobjDest.processId = lobjProc.getKey().toString();
		mobjDest.permissions = larrPerms;

		mobjDest.processNumber = (String)pobjSource.getAt(0);
		mobjDest.clientId = lobjClient.getKey().toString();
		mobjDest.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		mobjDest.clientName = lobjClient.getLabel();
		mobjDest.caseStudy = (Boolean)pobjSource.getAt(4);
		mobjDest.isOpen = lobjProc.IsRunning();
	}

	private QuoteRequestSubLine mobjQRSubLine;

	protected class FieldContainerBuilder
		extends bigBang.module.insurancePolicyModule.server.ServerToClient.FieldContainerBuilder
	{
		public FieldContainerBuilder withSource(SubLine pobjSubLine)
			throws BigBangException
		{
			withSource(pobjSubLine, false);

			return this;
		}

		public FieldContainerBuilder withSource(QuoteRequestSubLine pobjQRSubLine)
			throws BigBangException
		{
			mobjQRSubLine = pobjQRSubLine;
			return withSource(mobjQRSubLine.GetSubLine());
		}

		public FieldContainerBuilder withContainer(FieldContainer pobjContainer, boolean pbForObject)
		{
			withContainer(pobjContainer, pbForObject, false);
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
			int i, j, l;

			larrHeaders = new ArrayList<FieldContainer.HeaderField>();
			larrColumns = new ArrayList<FieldContainer.ColumnField>();
			larrExtras = new ArrayList<FieldContainer.ExtraField>();

			l = 0;
			for ( i = 0; i < marrCoverages.length; i++ )
			{
				if ( marrCoverages[i].IsHeader() )
				{
					for ( j = 0; j < marrFields[i].length; j++ )
					{
						if ( !marrFields[i][j].IsVisible() ||
								(marrFields[i][j].GetVariesByObject() != mbForObject) )
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
								(marrFields[i][j].GetVariesByObject() != mbForObject) )
							continue;

						if ( marrFields[i][j].GetColumnOrder() < 0 )
						{
							lobjExtra = new FieldContainer.ExtraField();
							buildField(lobjExtra, marrFields[i][j]);
							lobjExtra.coverageIndex = l;
							larrExtras.add(lobjExtra);
						}
						else
						{
							lobjColumn = new FieldContainer.ColumnField();
							buildField(lobjColumn, marrFields[i][j]);
							lobjColumn.coverageIndex = l;
							lobjColumn.columnIndex = marrFields[i][j].GetColumnOrder();
							larrColumns.add(lobjColumn);
						}
					}
					l++;
				}
			}

			mobjContainer.headerFields = larrHeaders.toArray(new FieldContainer.HeaderField[larrHeaders.size()]);
			mobjContainer.columnFields = larrColumns.toArray(new FieldContainer.ColumnField[larrColumns.size()]);
			mobjContainer.extraFields = larrExtras.toArray(new FieldContainer.ExtraField[larrExtras.size()]);

			return this;
		}

		public FieldContainerBuilder withDataFor(UUID pidObject)
			throws BigBangException
		{
			getRequestDataFor(pidObject);

			return this;
		}

		private void getRequestDataFor(UUID pidObject)
			throws BigBangException
		{
			QuoteRequestValue[] larrData;
			FieldContents lobjAux;
			int i;

			if ( ((pidObject == null) == mbForObject) )
			{
				mmapData = null;
				return;
			}

			try
			{
				larrData = mobjQRSubLine.GetCurrentKeyedValues(pidObject);
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
		}
	}

	protected bigBang.module.insurancePolicyModule.server.ServerToClient.FieldContainerBuilder getBaseBuilder()
	{
		if ( mobjBaseBuilder == null )
			mobjBaseBuilder = new FieldContainerBuilder();
		return mobjBaseBuilder;
	}

	private FieldContainerBuilder getLocalBaseBuilder()
	{
		return (FieldContainerBuilder)getBaseBuilder();
	}

	protected class ComplexFieldContainerBuilder
		extends bigBang.module.insurancePolicyModule.server.ServerToClient.ComplexFieldContainerBuilder
	{
		public ComplexFieldContainerBuilder withSource(SubLine pobjSubLine)
			throws BigBangException
		{
			mbIsEmpty = true;
			mbForObject = false;
			midObject = null;
			midExerciseType = Constants.ExID_None;

			getLocalBaseBuilder().withSource(pobjSubLine);
			return this;
		}

		public ComplexFieldContainerBuilder withSource(QuoteRequestSubLine pobjQRSubLine, UUID pidObject)
			throws BigBangException
		{
			mbIsEmpty = false;
			mbForObject = (pidObject != null);
			midObject = pidObject;
			midExerciseType = Constants.ExID_None;

			getLocalBaseBuilder().withSource(pobjQRSubLine);
			return this;
		}

		public ComplexFieldContainerBuilder build()
			throws BigBangException
		{
			if ( mbIsEmpty )
			{
				mobjContainer = (ComplexFieldContainer)getLocalBaseBuilder()
						.withContainer(mobjContainer, mbForObject)
						.build()
						.result();
			}
			else
			{
				mobjContainer = (ComplexFieldContainer)getLocalBaseBuilder()
						.withContainer(mobjContainer, mbForObject)
						.build()
						.withDataFor(midObject)
						.fill()
						.result();
			}

			return this;
		}

		public ComplexFieldContainerBuilder fill()
			throws BigBangException
		{
			mobjContainer.hasExercises = false;
			mobjContainer.exerciseData = null;
			return this;
		}
	}

	protected bigBang.module.insurancePolicyModule.server.ServerToClient.ComplexFieldContainerBuilder getComplexBuilder()
	{
		if ( mobjComplexBuilder == null )
			mobjComplexBuilder = new ComplexFieldContainerBuilder();
		return mobjComplexBuilder;
	}

	private ComplexFieldContainerBuilder getLocalComplexBuilder()
	{
		return (ComplexFieldContainerBuilder)getComplexBuilder();
	}

	protected class StructuredBuilder
		extends bigBang.module.insurancePolicyModule.server.ServerToClient.StructuredBuilder
	{
		public StructuredBuilder withSource(QuoteRequestSubLine pobjQRSubLine, UUID pidObject)
			throws BigBangException
		{
			getLocalComplexBuilder()
					.withSource(pobjQRSubLine, pidObject);

			return this;
		}

		public StructuredBuilder fill()
			throws BigBangException
		{
			CoverageContents lobjAux;
			int i;

			getRequestCoverages();

			for ( i = 0; i < mobjContainer.coverages.length; i++ )
			{
				lobjAux = mmapCoverages.get(UUID.fromString(mobjContainer.coverages[i].coverageId));
				if ( lobjAux != null )
				{
					mobjContainer.coverages[i].serverId = lobjAux.cid.toString();
					mobjContainer.coverages[i].presentInPolicy = lobjAux.present;
				}
			}

			return this;
		}

		private void getRequestCoverages()
			throws BigBangException
		{
			QuoteRequestCoverage[] larrCoverages;
			CoverageContents lobjAux;
			int i;

			try
			{
				larrCoverages = mobjQRSubLine.GetCurrentCoverages();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			mmapCoverages = new HashMap<UUID, CoverageContents>();

			for ( i = 0; i < larrCoverages.length; i++ )
			{
				if ( !larrCoverages[i].GetCoverage().IsHeader() )
				{
					lobjAux = new CoverageContents();
					lobjAux.cid = larrCoverages[i].getKey();
					lobjAux.present = larrCoverages[i].IsPresent();
					mmapCoverages.put(larrCoverages[i].GetCoverage().getKey(), lobjAux);
				}
			}
		}
	}

	private StructuredBuilder mobjStructuredBuilder;

	protected StructuredBuilder getStructuredBuilder()
	{
		if ( mobjStructuredBuilder == null )
			mobjStructuredBuilder = new StructuredBuilder();
		return mobjStructuredBuilder;
	}

	private class SubLineFieldContainerBuilder
	{
		private CompositeFieldContainer.SubLineFieldContainer mobjContainer;
		private boolean mbIsEmpty;

		public SubLineFieldContainerBuilder withSource(SubLine pobjSubLine)
			throws BigBangException
		{
			mbIsEmpty = true;
			getStructuredBuilder()
					.withSource(pobjSubLine);
			return this;
		}

		public SubLineFieldContainerBuilder withSource(QuoteRequestSubLine pobjQRSubLine, UUID pidObject)
			throws BigBangException
		{
			mbIsEmpty = false;
			getStructuredBuilder()
					.withSource(pobjQRSubLine, pidObject);
			return this;
		}

		public SubLineFieldContainerBuilder withContainer(CompositeFieldContainer.SubLineFieldContainer pobjContainer)
			throws BigBangException
		{
			if ( mbIsEmpty )
			{
				mobjContainer = (CompositeFieldContainer.SubLineFieldContainer)getStructuredBuilder()
						.withContainer(pobjContainer)
						.build()
						.result();
			}
			else
			{
				mobjContainer = (CompositeFieldContainer.SubLineFieldContainer)getStructuredBuilder()
						.withContainer(pobjContainer)
						.build()
						.fill()
						.result();
			}
			return this;
		}

		public SubLineFieldContainerBuilder build()
		{
			if ( mbIsEmpty )
				mobjContainer.id = null;
			else
				mobjContainer.id = mobjQRSubLine.getKey().toString();

			mobjContainer.subLineId = mobjSubLine.getKey().toString();
			mobjContainer.categoryName = mobjSubLine.getLine().getCategory().getLabel();
			mobjContainer.lineName = mobjSubLine.getLine().getLabel();
			mobjContainer.subLineName = mobjSubLine.getLabel();
			mobjContainer.objectTypeId = mobjSubLine.getObjectType().toString();
			mobjContainer.change = CompositeFieldContainer.SubLineFieldContainer.Change.NONE;
			return this;
		}

		public CompositeFieldContainer.SubLineFieldContainer result()
		{
			return mobjContainer;
		}
	}

	private class CompositeBuilder
	{
		private CompositeFieldContainer mobjContainer;
		private QuoteRequestSubLine[] marrSubLines;
		private UUID midObject;

		public CompositeBuilder withSource()
			throws BigBangException
		{
			marrSubLines = new QuoteRequestSubLine[0];
			midObject = null;
			return this;
		}

		public CompositeBuilder withSource(com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjRequest)
			throws BigBangException
		{
			midObject = null;

			try
			{
				marrSubLines = pobjRequest.GetCurrentSubLines();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			return this;
		}

		public CompositeBuilder withSource(com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject pobjObject)
			throws BigBangException
		{
			midObject = pobjObject.getKey();

			try
			{
				marrSubLines = pobjObject.GetOwner().GetCurrentSubLines();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			return this;
		}

		public CompositeBuilder withContainer(CompositeFieldContainer pobjContainer)
		{
			mobjContainer = pobjContainer;
			return this;
		}

		public CompositeBuilder build()
			throws BigBangException
		{
			SubLineFieldContainerBuilder lobjAux;
			int i;

			lobjAux = new SubLineFieldContainerBuilder();

			mobjContainer.subLineData = new CompositeFieldContainer.SubLineFieldContainer[marrSubLines.length];
			for ( i = 0; i < marrSubLines.length; i++ )
			{
				mobjContainer.subLineData[i] = lobjAux
						.withSource(marrSubLines[i], midObject)
						.withContainer(new CompositeFieldContainer.SubLineFieldContainer())
						.build()
						.result();
			}

			return this;
		}

		public CompositeFieldContainer result()
		{
			return mobjContainer;
		}
	}

	private CompositeBuilder mobjCompositeBuilder;

	protected CompositeBuilder getCompositeBuilder()
	{
		if ( mobjCompositeBuilder == null )
			mobjCompositeBuilder = new CompositeBuilder();
		return mobjCompositeBuilder;
	}

	private class ObjectBuilder
	{
		private com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject mobjObject;
		private QuoteRequestObject mobjOutObject;

		public ObjectBuilder withSource(com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject pobjObject)
			throws BigBangException
		{
			mobjObject = pobjObject;

			mobjOutObject = (QuoteRequestObject)getCompositeBuilder()
					.withSource(pobjObject)
					.withContainer(new QuoteRequestObject())
					.build()
					.result();

			return this;
		}

		public ObjectBuilder build()
			throws BigBangException
		{
			buildHeader();
			return this;
		}

		public QuoteRequestObject result()
		{
			return mobjOutObject;
		}

		private void buildHeader()
			throws BigBangException
		{
			ObjectBase lobjZipCode;
			ObjectBase lobjType;

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

			mobjOutObject.id = mobjObject.getKey().toString();

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
			mobjOutObject.typeId = lobjType.getKey().toString();
			mobjOutObject.typeText = lobjType.getLabel();

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

	private class GlobalBuilder
	{
		private GlobalFieldContainer mobjContainer;

		public GlobalBuilder withSource()
			throws BigBangException
		{
			getCompositeBuilder()
					.withSource();

			return this;
		}

		public GlobalBuilder withSource(com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjRequest)
			throws BigBangException
		{
			getCompositeBuilder()
					.withSource(pobjRequest);

			return this;
		}

		public GlobalBuilder withContainer(QuoteRequest quoteRequest)
			throws BigBangException
		{
			mobjContainer = (GlobalFieldContainer)getCompositeBuilder()
					.withContainer(quoteRequest)
					.build()
					.result();

			return this;
		}

		public GlobalFieldContainer result()
		{
			return mobjContainer;
		}
	}

	private class RequestBuilder
	{
		private com.premiumminds.BigBang.Jewel.Objects.QuoteRequest mobjRequest;
		private boolean mbIsEmpty;
		private Client mobjClient;
		private QuoteRequest mobjOutRequest;

		public RequestBuilder withSource(Client pobjClient)
			throws BigBangException
		{
			mobjRequest = null;
			mbIsEmpty = true;
			mobjClient = pobjClient;

			mobjOutRequest = (QuoteRequest)new GlobalBuilder()
					.withSource()
					.withContainer(new QuoteRequest())
					.result();

			return this;
		}

		public RequestBuilder withSource(com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjRequest)
			throws BigBangException
		{
			mobjRequest = pobjRequest;
			mbIsEmpty = false;
			mobjClient = null;

			mobjOutRequest = (QuoteRequest)new GlobalBuilder()
					.withSource(pobjRequest)
					.withContainer(new QuoteRequest())
					.result();

			return this;
		}

		public RequestBuilder build()
			throws BigBangException
		{
			if ( mbIsEmpty )
				buildEmptyHeader();
			else
				buildFullHeader();

			return this;
		}

		public QuoteRequest result()
		{
			return mobjOutRequest;
		}

		private void buildEmptyHeader()
			throws BigBangException
		{
			Mediator lobjMed;

			try
			{
				lobjMed = mobjClient.getMediator();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			mobjOutRequest.clientId = mobjClient.getKey().toString();
			mobjOutRequest.clientNumber = ((Integer)mobjClient.getAt(1)).toString();
			mobjOutRequest.clientName = mobjClient.getLabel();
			mobjOutRequest.isOpen = true;

			mobjOutRequest.inheritMediatorId = lobjMed.getKey().toString();
			mobjOutRequest.inheritMediatorName = lobjMed.getLabel();
		}

		private void buildFullHeader()
			throws BigBangException
		{
			IProcess lobjProc;
			Mediator lobjMed;

			try
			{
				lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjRequest.GetProcessID());
				lobjMed = mobjRequest.getMediator();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			buildQuoteRequestStub(mobjOutRequest, mobjRequest);

			mobjOutRequest.managerId = lobjProc.GetManagerID().toString();
			mobjOutRequest.mediatorId = (mobjRequest.getAt(2) == null ? null : ((UUID)mobjRequest.getAt(2)).toString());
			mobjOutRequest.inheritMediatorId = lobjMed.getKey().toString();
			mobjOutRequest.inheritMediatorName = lobjMed.getLabel();
			mobjOutRequest.notes = (String)mobjRequest.getAt(3);
			mobjOutRequest.docushare = (String)mobjRequest.getAt(5);
			mobjOutRequest.clientId = ((UUID)mobjRequest.getAt(6)).toString();
		}
	}

	public QuoteRequest getEmptyRequest(UUID pidClient)
		throws BigBangException
	{
		Client lobjClient;

		try
		{
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), pidClient);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new RequestBuilder()
				.withSource(lobjClient)
				.build()
				.result();
	}

	public QuoteRequest getRequest(UUID pidRequest)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), pidRequest);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new RequestBuilder()
				.withSource(lobjRequest)
				.build()
				.result();
	}

	public QuoteRequestObject getRequestObject(UUID pidObject)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject lobjObject;

		try
		{
			lobjObject = com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), pidObject);
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

	public CompositeFieldContainer.SubLineFieldContainer getEmptySubLine(UUID pidSubLine)
		throws BigBangException
	{
		SubLine lobjSubLine;

		try
		{
			lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), pidSubLine);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return new SubLineFieldContainerBuilder()
				.withSource(lobjSubLine)
				.withContainer(new CompositeFieldContainer.SubLineFieldContainer())
				.build()
				.result();
	}
}
