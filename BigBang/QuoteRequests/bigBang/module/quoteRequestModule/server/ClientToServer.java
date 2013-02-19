package bigBang.module.quoteRequestModule.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import bigBang.definitions.shared.CompositeFieldContainer;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.GlobalFieldContainer;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.Data.QuoteRequestCoverageData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestObjectData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestSubLineData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestValueData;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestCoverage;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestSubLine;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestValue;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class ClientToServer
	extends bigBang.module.insurancePolicyModule.server.ClientToServer
{
	private class FieldContainerReader
		extends bigBang.module.insurancePolicyModule.server.ClientToServer.FieldContainerReader
	{
		private QuoteRequestSubLine mobjQRSubLine;
		private UUID midOwner;
		private int mlngOwner;
		private ArrayList<QuoteRequestValueData> marrQRData;

		public void withOriginal(int plngQRSubLine)
		{
			mbIsEmpty = true;
			msetDeletia = new HashSet<UUID>();

			mobjQRSubLine = null;
			midOwner = null;
			mlngOwner = plngQRSubLine;

			marrQRData = new ArrayList<QuoteRequestValueData>();
		}

		public void withOriginal(QuoteRequestSubLine pobjQRSubLine, int plngQRSubLine)
		{
			mbIsEmpty = false;
			msetDeletia = new HashSet<UUID>();

			mobjQRSubLine = pobjQRSubLine;
			midOwner = pobjQRSubLine.getKey();
			mlngOwner = plngQRSubLine;

			marrQRData = new ArrayList<QuoteRequestValueData>();
		}

		public void readValues(FieldContainer pobjContainer, UUID pidObject, int plngObject, boolean pbForceDelete)
			throws BigBangException
		{
			QuoteRequestValue[] larrQRValues;
			int i;

			if ( !mbIsEmpty && ((plngObject < 0) == (pidObject == null)) )
			{
				try
				{
					larrQRValues = mobjQRSubLine.GetCurrentKeyedValues(pidObject);
					for ( i = 0; i < larrQRValues.length; i++ )
						msetDeletia.add(larrQRValues[i].getKey());
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}

			if ( !pbForceDelete )
			{
				for ( i = 0; i < pobjContainer.headerFields.length; i++ )
					readField(pobjContainer.headerFields[i], pidObject, plngObject);

				for ( i = 0; i < pobjContainer.columnFields.length; i++ )
					readField(pobjContainer.columnFields[i], pidObject, plngObject);

				for ( i = 0; i < pobjContainer.extraFields.length; i++ )
					readField(pobjContainer.extraFields[i], pidObject, plngObject);
			}
		}

		public QuoteRequestValueData[] resultQR()
		{
			QuoteRequestValueData lobjAux;

			for (UUID lid: msetDeletia)
			{
				lobjAux = new QuoteRequestValueData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrQRData.add(lobjAux);
			}

			return marrQRData.toArray(new QuoteRequestValueData[marrQRData.size()]);
		}

		private void readField(FieldContainer.HeaderField pobjField, UUID pidObject, int plngObject)
		{
			readRequestField(pobjField, pidObject, plngObject);
		}

		private void readRequestField(FieldContainer.HeaderField pobjField, UUID pidObject, int plngObject)
		{
			QuoteRequestValueData lobjAux;

			lobjAux = new QuoteRequestValueData();

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
			lobjAux.midQRSubLine = midOwner;
			lobjAux.mlngQRSubLine = mlngOwner;
			lobjAux.midField = UUID.fromString(pobjField.fieldId);
			lobjAux.midObject = pidObject;
			lobjAux.mlngObject = plngObject;

			marrQRData.add(lobjAux);
		}
	}

	private class StructuredReader
		extends bigBang.module.insurancePolicyModule.server.ClientToServer.StructuredReader
	{
		private FieldContainerReader mrefLocalBase;

		private UUID midOwner;
		private int mlngOwner;
		private boolean mbForDelete;
		private ArrayList<QuoteRequestCoverageData> marrQRCData;

		public void withOriginal(int plngQRSubLine, boolean pbForDelete)
		{
			midOwner = null;
			mlngOwner = plngQRSubLine;
			mbForDelete = pbForDelete;

			marrQRCData = new ArrayList<QuoteRequestCoverageData>();
			msetCDeletia = new HashSet<UUID>();

			getLocalBaseReader().withOriginal(plngQRSubLine);
		}

		public void withOriginal(QuoteRequestSubLine pobjOriginal, int plngQRSubLine, boolean pbForDelete)
			throws BigBangException
		{
			QuoteRequestCoverage[] larrCovs;
			int i;

			midOwner = pobjOriginal.getKey();
			mlngOwner = plngQRSubLine;
			mbForDelete = pbForDelete;

			try
			{
				larrCovs = pobjOriginal.GetCurrentCoverages();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			marrQRCData = new ArrayList<QuoteRequestCoverageData>();
			msetCDeletia = new HashSet<UUID>();

			for ( i = 0; i < larrCovs.length; i++ )
				msetCDeletia.add(larrCovs[i].getKey());

			getLocalBaseReader().withOriginal(pobjOriginal, plngQRSubLine);
		}

		public void withMaster(StructuredFieldContainer pobjContainer)
		{
			readRequestCoverages(pobjContainer);
		}

		public void read(StructuredFieldContainer pobjContainer, UUID pidObject, int plngObject, boolean pbForceDelete)
			throws BigBangException
		{
			getLocalBaseReader().readValues(pobjContainer, pidObject, plngObject, mbForDelete || pbForceDelete);
		}

		public QuoteRequestCoverageData[] resultQRC()
		{
			return marrQRCData.toArray(new QuoteRequestCoverageData[marrQRCData.size()]);
		}

		public QuoteRequestValueData[] resultQRV()
		{
			return getLocalBaseReader().resultQR();
		}
		
		private FieldContainerReader getLocalBaseReader()
		{
			if ( mrefLocalBase == null )
				mrefLocalBase = new FieldContainerReader();
			return mrefLocalBase;
		}

		private void readRequestCoverages(StructuredFieldContainer pobjContainer)
		{
			QuoteRequestCoverageData lobjAux;
			int i;

			for ( i = 0; i < pobjContainer.coverages.length; i++ )
			{
				lobjAux = new QuoteRequestCoverageData();

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

				lobjAux.midQRSubLine = midOwner;
				lobjAux.mlngQRSubLine = mlngOwner;
				lobjAux.midCoverage = UUID.fromString(pobjContainer.coverages[i].coverageId);
				lobjAux.mbPresent = pobjContainer.coverages[i].presentInPolicy;
				lobjAux.mbDeleted = false;

				marrQRCData.add(lobjAux);
			}

			for (UUID lid: msetCDeletia)
			{
				lobjAux = new QuoteRequestCoverageData();
				lobjAux.mid = lid;
				lobjAux.mbNew = false;
				lobjAux.mbDeleted = true;
				marrQRCData.add(lobjAux);
			}
		}
	}

	private class SubLineFieldContainerReader
	{
		private StructuredReader mrefLocalStructure;

		private QuoteRequestSubLine mobjQRSubLine;
		private int mlngOwner;
		private QuoteRequestSubLineData mobjQRSLData;
		CompositeFieldContainer.SubLineFieldContainer mobjTmp;

		public void withOriginal(int plngQRSubLine)
		{
			mobjQRSubLine = null;
			mlngOwner = plngQRSubLine;
		}

		public void withOriginal(QuoteRequestSubLine pobjOriginal, int plngQRSubLine)
			throws BigBangException
		{
			mobjQRSubLine = pobjOriginal;
			mlngOwner = plngQRSubLine;
		}

		public void readSubLine(CompositeFieldContainer.SubLineFieldContainer pobjContainer, UUID pidObject, int plngObject, boolean pbForceDelete)
			throws BigBangException
		{
			boolean lbForDelete;

			if ( plngObject < 0 )
			{
				lbForDelete = ( CompositeFieldContainer.SubLineFieldContainer.Change.DELETED.equals(pobjContainer.change) );

				if ( lbForDelete )
					readDeleteHeader(pobjContainer);
				else
					readFullHeader(pobjContainer);

				if ( mobjQRSubLine == null )
					getLocalStructuredReader().withOriginal(mlngOwner, lbForDelete);
				else
					getLocalStructuredReader().withOriginal(mobjQRSubLine, mlngOwner, lbForDelete);
				getLocalStructuredReader().withMaster(pobjContainer);
			}
			else if ( mobjQRSLData == null )
			{
				delayedReadHeader();
			}

			getLocalStructuredReader().read(pobjContainer, pidObject, plngObject, pbForceDelete);
		}

		public QuoteRequestSubLineData result()
		{
			if ( mobjQRSLData == null )
				return null;

			mobjQRSLData.marrCoverages = getLocalStructuredReader().resultQRC();
			mobjQRSLData.marrValues = getLocalStructuredReader().resultQRV();
			return mobjQRSLData;
		}
		
		private StructuredReader getLocalStructuredReader()
		{
			if ( mrefLocalStructure == null )
				mrefLocalStructure = new StructuredReader();
			return mrefLocalStructure;
		}

		private void readDeleteHeader(CompositeFieldContainer.SubLineFieldContainer pobjContainer)
		{
			mobjQRSLData = new QuoteRequestSubLineData();
			mobjQRSLData.mid = UUID.fromString(pobjContainer.id);
			mobjQRSLData.mbDeleted = true;
			mobjQRSLData.mbNew = false;
		}

		private void readFullHeader(CompositeFieldContainer.SubLineFieldContainer pobjContainer)
		{
			mobjQRSLData = null;

			if ( CompositeFieldContainer.SubLineFieldContainer.Change.NONE.equals(pobjContainer.change) )
			{
				mobjTmp = pobjContainer;
				return;
			}

			mobjQRSLData = new QuoteRequestSubLineData();
			mobjQRSLData.mbNew = ( CompositeFieldContainer.SubLineFieldContainer.Change.CREATED.equals(pobjContainer.change) );
			mobjQRSLData.mbDeleted = false;

			if ( mobjQRSLData.mbNew )
				mobjQRSLData.mid = null;
			else
				mobjQRSLData.mid = UUID.fromString(pobjContainer.id);

			mobjQRSLData.midQuoteRequest = midParent;
			mobjQRSLData.midSubLine = UUID.fromString(pobjContainer.subLineId);
		}

		private void delayedReadHeader()
		{
			mobjQRSLData = new QuoteRequestSubLineData();
			mobjQRSLData.mbNew = false;
			mobjQRSLData.mbDeleted = false;

			mobjQRSLData.mid = UUID.fromString(mobjTmp.id);
			mobjQRSLData.midQuoteRequest = midParent;
			mobjQRSLData.midSubLine = UUID.fromString(mobjTmp.subLineId);

			mobjTmp = null;
		}
	}

	private class CompositeReader
	{
		private HashMap<String, Integer> marrMap;
		SubLineFieldContainerReader[] marrReaders;

		private HashMap<UUID, QuoteRequestSubLine> marrSLines;

		public void withOriginal()
		{
			marrSLines = new HashMap<UUID, QuoteRequestSubLine>();
		}

		public void withOriginal(com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjOriginal)
			throws BigBangException
		{
			QuoteRequestSubLine[] larrSLines;
			int i;

			marrSLines = new HashMap<UUID, QuoteRequestSubLine>();

			try
			{
				larrSLines = pobjOriginal.GetCurrentSubLines();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			for ( i = 0; i < larrSLines.length; i++ )
				marrSLines.put(larrSLines[i].GetSubLine().getKey(), larrSLines[i]);
		}

		public void readComposite(CompositeFieldContainer pobjContainer, UUID pidObject, int plngObject, boolean pbForceDelete)
			throws BigBangException
		{
			Integer llngIndex;
			int i;

			if ( plngObject < 0 )
				readRequestSubLines(pobjContainer);

			if ( pobjContainer.subLineData == null )
				return;

			for ( i = 0; i < pobjContainer.subLineData.length; i++ )
			{
				if ( pobjContainer.subLineData[i] == null )
					continue;

				llngIndex = marrMap.get(pobjContainer.subLineData[i].subLineId);
				if ( llngIndex == null )
					continue;

				marrReaders[llngIndex].readSubLine(pobjContainer.subLineData[i], pidObject, plngObject, pbForceDelete);
			}
		}

		public QuoteRequestSubLineData[] resultSL()
		{
			QuoteRequestSubLineData[] larrAux;
			int i;

			larrAux = new QuoteRequestSubLineData[marrReaders.length];

			for ( i = 0; i < marrReaders.length; i++ )
				larrAux[i] = marrReaders[i].result();

			return larrAux;
		}

		private void readRequestSubLines(CompositeFieldContainer pobjContainer)
			throws BigBangException
		{
			QuoteRequestSubLine lobjSLine;
			int i;

			marrMap = new HashMap<String, Integer>();

			if ( pobjContainer.subLineData == null )
			{
				marrReaders = new SubLineFieldContainerReader[0];
				return;
			}

			marrReaders = new SubLineFieldContainerReader[pobjContainer.subLineData.length];

			for ( i = 0; i < marrReaders.length; i++ )
			{
				marrReaders[i] = new SubLineFieldContainerReader();
				marrMap.put(pobjContainer.subLineData[i].subLineId, i);

				if ( pobjContainer.subLineData[i] == null )
					continue;

				lobjSLine = marrSLines.get(UUID.fromString(pobjContainer.subLineData[i].subLineId));
				if ( lobjSLine == null )
					marrReaders[i].withOriginal(i);
				else
					marrReaders[i].withOriginal(lobjSLine, i);
			}
		}
	}

	private CompositeReader mobjCompositeReader;

	private CompositeReader getCompositeReader()
	{
		if ( mobjCompositeReader == null )
			mobjCompositeReader = new CompositeReader();
		return mobjCompositeReader;
	}

	private class GlobalReader
	{
		private ArrayList<QuoteRequestObjectData> marrOData;

		public void withOriginal()
		{
			marrOData = new ArrayList<QuoteRequestObjectData>();

			getCompositeReader().withOriginal();
		}

		public void withOriginal(com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjOriginal)
			throws BigBangException
		{
			marrOData = new ArrayList<QuoteRequestObjectData>();

			getCompositeReader().withOriginal(pobjOriginal);
		}

		public void read(GlobalFieldContainer pobjContainer)
			throws BigBangException
		{
			CompositeReader lobjReader;

			lobjReader = getCompositeReader();
			lobjReader.readComposite(pobjContainer, null, -1, false);

			readRequestObjects(pobjContainer);
		}

		public QuoteRequestSubLineData[] resultSL()
		{
			return getCompositeReader().resultSL();
		}

		public QuoteRequestObjectData[] resultO()
		{
			return marrOData.toArray(new QuoteRequestObjectData[marrOData.size()]);
		}

		private void readRequestObjects(GlobalFieldContainer pobjContainer)
			throws BigBangException
		{
			QuoteRequestObjectData lobjAux;
			int i, n;

			n = 0;
			for ( i = 0; i < pobjContainer.changedObjects.length; i++ )
			{
				lobjAux = readRequestObject(pobjContainer.changedObjects[i], n);

				if ( lobjAux != null )
				{
					marrOData.add(lobjAux);
					n++;
				}
			}
		}

		private QuoteRequestObjectData readRequestObject(QuoteRequestObject pobjObject, int plngObject)
			throws BigBangException
		{
			UUID lidObject;
			boolean lbDeleted;

			if ( InsuredObjectStub.Change.NONE.equals(pobjObject.change) )
				return null;

			try
			{
				lidObject = UUID.fromString(pobjObject.id);
			}
			catch (Throwable e)
			{
				lidObject = null;
			}

			lbDeleted = ( InsuredObjectStub.Change.DELETED.equals(pobjObject.change) );

			getCompositeReader()
					.readComposite(pobjObject, lidObject, plngObject, lbDeleted);

			if ( lbDeleted )
				return readPDeleteHeader(pobjObject);
			else
				return readPFullHeader(pobjObject);
		}

		private QuoteRequestObjectData readPDeleteHeader(QuoteRequestObject pobjObject)
		{
			QuoteRequestObjectData lobjResult;
			UUID lidObject;

			try
			{
				lidObject = UUID.fromString(pobjObject.id);
			}
			catch (Throwable e)
			{
				lidObject = null;
			}

			if ( lidObject == null )
				lobjResult = null;
			else
			{
				lobjResult = new QuoteRequestObjectData();
				lobjResult.mid = lidObject;
				lobjResult.mbNew = false;
				lobjResult.mbDeleted = true;
			}

			return lobjResult;
		}

		private QuoteRequestObjectData readPFullHeader(QuoteRequestObject pobjObject)
			throws BigBangException
		{
			UUID lidZipCode;
			QuoteRequestObjectData lobjResult;

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

			lobjResult = new QuoteRequestObjectData();

			if ( InsuredObjectStub.Change.MODIFIED.equals(pobjObject.change) )
			{
				lobjResult.mid = UUID.fromString(pobjObject.id);
				lobjResult.mbNew = false;
			}
			else
			{
				lobjResult.mid = null;
				lobjResult.mbNew = true;
			}
			lobjResult.mbDeleted = false;

			lobjResult.mstrName = pobjObject.unitIdentification;
			lobjResult.midOwner = midParent;
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

	private class QuoteRequestReader
	{
		public QuoteRequestData readRequest(QuoteRequest pobjRequest, com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjOriginal)
			throws BigBangException
		{
			GlobalReader lobjReader;
			QuoteRequestData lobjResult;

			lobjReader = new GlobalReader();
			if ( pobjOriginal == null )
				lobjReader.withOriginal();
			else
				lobjReader.withOriginal(pobjOriginal);
			lobjReader.read(pobjRequest);

			lobjResult = new QuoteRequestData();

			readHeader(lobjResult, pobjRequest);

			lobjResult.marrSubLines = lobjReader.resultSL();
			lobjResult.marrObjects = lobjReader.resultO();

			return lobjResult;
		}

		private void readHeader(QuoteRequestData pobjResult, QuoteRequest pobjRequest)
		{
			pobjResult.mid = ( pobjRequest.id == null ? null : UUID.fromString(pobjRequest.id) );

			pobjResult.midClient = ( pobjRequest.clientId == null ? null : UUID.fromString(pobjRequest.clientId) );
			pobjResult.mstrNumber = pobjRequest.processNumber;
			pobjResult.midMediator = ( pobjRequest.mediatorId == null ? null : UUID.fromString(pobjRequest.mediatorId) );
			pobjResult.mstrNotes = pobjRequest.notes;
			pobjResult.mbCaseStudy = pobjRequest.caseStudy;
			pobjResult.mstrDocuShare = pobjRequest.docushare;

			pobjResult.midManager = ( pobjRequest.managerId == null ? null : UUID.fromString(pobjRequest.managerId) );
			pobjResult.midProcess = ( pobjRequest.processId == null ? null : UUID.fromString(pobjRequest.processId) );

			pobjResult.mbModified = true;
		}
	}

	public QuoteRequestData getQRDataForCreate(QuoteRequest pobjSource)
		throws BigBangException
	{
		return new QuoteRequestReader().readRequest(pobjSource, null);
	}

	public QuoteRequestData getQRDataForEdit(com.premiumminds.BigBang.Jewel.Objects.QuoteRequest pobjOriginal, QuoteRequest pobjSource)
		throws BigBangException
	{
		return new QuoteRequestReader().readRequest(pobjSource, pobjOriginal);
	}
}
