package bigBang.module.quoteRequestModule.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ConversationServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.server.TransferManagerServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.server.ServerToClient;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestCoverageData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestObjectData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestSubLineData;
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestValueData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestCoverage;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestSubLine;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestValue;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.Tax;
import com.premiumminds.BigBang.Jewel.Operations.Client.CreateQuoteRequest;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CloseProcess;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.CreateConversation;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.DeleteQuoteRequest;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ExecMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ManageData;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class QuoteRequestServiceImpl
	extends SearchServiceBase
	implements QuoteRequestService
{
	private static final long serialVersionUID = 1L;

	private static class QuoteRequestScratchPad
	{
		private static class PadField
		{
			public UUID midField;
			public int mlngColIndex;
			public String mstrLabel;
			public UUID midType;
			public String mstrUnits;
			public String mstrDefault;
			public UUID midRefersTo;
			public boolean mbVariesByObject;
		}

		private static class PadCoverage
			extends QuoteRequestCoverageData
		{
			private static final long serialVersionUID = 1L;

			public transient String mstrLabel;
			public transient boolean mbMandatory;
			public transient boolean mbIsHeader;
			public transient int mlngOrder;
			public transient PadField[] marrFields;
		}

		private static class PadSubLine
			extends QuoteRequestSubLineData
		{
			private static final long serialVersionUID = 1L;

			public transient SubLine mobjSubLine;
			public transient String mstrLabel;
			public transient UUID midObjectType;
			public transient ArrayList<PadCoverage> marrCoverages;
			public transient ArrayList<PadValue> marrValues;

			public int FindCoverage(UUID pidCoverage, int plngStart)
			{
				int i;

				for ( i = plngStart; i < marrCoverages.size(); i++ )
				{
					if ( marrCoverages.get(i).midCoverage.equals(pidCoverage) )
						return i;
				}

				for ( i = 0; i < plngStart; i++ )
				{
					if ( marrCoverages.get(i).midCoverage.equals(pidCoverage) )
						return i;
				}

				return -1;
			}

			public int FindValue(UUID pidField, int plngObject, int plngStart)
			{
				int i;

				for ( i = plngStart; i < marrValues.size(); i++ )
				{
					if ( (marrValues.get(i).midField.equals(pidField)) && (marrValues.get(i).mlngObject == plngObject) )
						return i;
				}

				for ( i = 0; i < plngStart; i++ )
				{
					if ( (marrValues.get(i).midField.equals(pidField)) && (marrValues.get(i).mlngObject == plngObject) )
						return i;
				}

				return -1;
			}
		}

		private static class PadObject
			extends QuoteRequestObjectData
		{
			private static final long serialVersionUID = 1L;
		}

		private static class PadValue
			extends QuoteRequestValueData
		{
			private static final long serialVersionUID = 1L;

			public transient PadCoverage mrefCoverage;
			public transient PadField mrefField;
		}

		public final UUID mid;
		public boolean mbValid;

		public UUID midClient;
		public QuoteRequestData mobjQuoteRequest;
		public ArrayList<PadSubLine> marrSubLines;
		public ArrayList<PadObject> marrObjects;

		public QuoteRequestScratchPad()
		{
			mid = UUID.randomUUID();
			mbValid = false;
		}
		
		public UUID GetID()
		{
			return mid;
		}

		public void InitNew(QuoteRequest pobjSource)
			throws BigBangException
		{
			if ( mbValid )
				throw new BigBangException("Erro: Não pode inicializar o mesmo espaço de trabalho duas vezes.");

			midClient = ( pobjSource.clientId == null ? null : UUID.fromString(pobjSource.clientId) );
			mobjQuoteRequest = new QuoteRequestData();
			mobjQuoteRequest.mstrNumber = pobjSource.processNumber;
			mobjQuoteRequest.midMediator = ( pobjSource.mediatorId == null ? null : UUID.fromString(pobjSource.mediatorId) );
			mobjQuoteRequest.mstrNotes = pobjSource.notes;
			mobjQuoteRequest.mbCaseStudy = pobjSource.caseStudy;

			marrSubLines = new ArrayList<PadSubLine>();
			marrObjects = new ArrayList<PadObject>();

			mobjQuoteRequest.mbModified = false;

			mbValid = true;
		}

		public void OpenForEdit(UUID pidRequest)
			throws BigBangException
		{
			com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjAuxRequest;
			QuoteRequestSubLine[] larrLocalSubLines;
			PadSubLine lobjSubLine;
			QuoteRequestCoverage[] larrLocalCoverages;
			com.premiumminds.BigBang.Jewel.Objects.Coverage[] larrAuxCoverages;
			HashMap<UUID, PadCoverage> lmapCoverages;
			PadCoverage lobjCoverage;
			Tax[] larrTaxes;
			ArrayList<PadField> larrFields;
			HashMap<UUID, PadField> lmapFields;
			PadField lobjField;
			com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject[] larrAuxObjects;
			HashMap<UUID, Integer> lmapObjects;
			PadObject lobjObject;
			QuoteRequestValue[] larrAuxValues;
			PadValue lobjValue;
			int i, j, k, l;

			if ( mbValid )
				throw new BigBangException("Erro: Não pode inicializar o mesmo espaço de trabalho duas vezes.");

			marrSubLines = new ArrayList<PadSubLine>();
			marrObjects = new ArrayList<PadObject>();

			try
			{
				lobjAuxRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
						pidRequest);

				midClient = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjAuxRequest.GetProcessID()).GetParent().GetDataKey();

				mobjQuoteRequest = new QuoteRequestData();
				mobjQuoteRequest.FromObject(lobjAuxRequest);

				mobjQuoteRequest.mbModified = false;

				larrAuxObjects = lobjAuxRequest.GetCurrentObjects();
				lmapObjects = new HashMap<UUID, Integer>();
				for ( i = 0 ; i < larrAuxObjects.length; i++ )
				{
					lobjObject = new PadObject();
					lobjObject.FromObject(larrAuxObjects[i]);
					lobjObject.mbDeleted = false;
					marrObjects.add(lobjObject);
					lmapObjects.put(lobjObject.mid, i);
				}

				lmapCoverages = new HashMap<UUID, PadCoverage>();
				lmapFields = new HashMap<UUID, PadField>();
				larrLocalSubLines = lobjAuxRequest.GetCurrentSubLines();
				for ( i = 0; i < larrLocalSubLines.length; i++ )
				{
					lobjSubLine = new PadSubLine();
					lobjSubLine.FromObject(larrLocalSubLines[i]);
					lobjSubLine.mobjSubLine = larrLocalSubLines[i].GetSubLine();
					lobjSubLine.mstrLabel = larrLocalSubLines[i].GetSubLine().getLine().getCategory().getLabel() + " / " +
							larrLocalSubLines[i].GetSubLine().getLine().getLabel() + " / " +
							larrLocalSubLines[i].GetSubLine().getLabel();
					lobjSubLine.midObjectType = larrLocalSubLines[i].GetSubLine().getObjectType();
					lobjSubLine.marrCoverages = new ArrayList<PadCoverage>();
					lobjSubLine.marrValues = new ArrayList<PadValue>();

					larrLocalCoverages = larrLocalSubLines[i].GetCurrentCoverages();
					for ( j = 0 ; j < larrLocalCoverages.length; j++ )
					{
						lobjCoverage = new PadCoverage();
						lobjCoverage.FromObject(larrLocalCoverages[j]);
						lobjCoverage.mstrLabel = larrLocalCoverages[j].GetCoverage().getLabel();
						lobjCoverage.mbIsHeader = larrLocalCoverages[j].GetCoverage().IsHeader();
						lobjCoverage.mbMandatory = larrLocalCoverages[j].GetCoverage().IsMandatory();
						lobjCoverage.mlngOrder = larrLocalCoverages[j].GetCoverage().GetOrder();
						larrTaxes = larrLocalCoverages[j].GetCoverage().GetCurrentTaxes();
						larrFields = new ArrayList<PadField>();
						for ( k = 0; k < larrTaxes.length; k++ )
						{
							lobjField = new PadField();
							lobjField.midField = larrTaxes[k].getKey();
							lobjField.mlngColIndex = larrTaxes[k].GetColumnOrder();
							lobjField.mstrLabel = larrTaxes[k].getLabel();
							lobjField.midType = (UUID)larrTaxes[k].getAt(2);
							lobjField.mstrUnits = (String)larrTaxes[k].getAt(3);
							lobjField.mstrDefault = (String)larrTaxes[k].getAt(4);
							lobjField.midRefersTo = (UUID)larrTaxes[k].getAt(7);
							lobjField.mbVariesByObject = larrTaxes[k].GetVariesByObject();
							larrFields.add(lobjField);
							lmapCoverages.put(larrTaxes[k].getKey(), lobjCoverage);
							lmapFields.put(larrTaxes[k].getKey(), lobjField);
						}
						lobjCoverage.marrFields = larrFields.toArray(new PadField[larrFields.size()]);
						lobjSubLine.marrCoverages.add(lobjCoverage);
					}

					larrAuxCoverages = SubLine.GetInstance(Engine.getCurrentNameSpace(),
							lobjSubLine.midSubLine).GetCurrentCoverages();
					for ( j = 0 ; j < larrAuxCoverages.length; j++ )
					{
						if ( lobjSubLine.FindCoverage(larrAuxCoverages[j].getKey(), 0) >= 0 )
							continue;
						lobjCoverage = new PadCoverage();
						lobjCoverage.mid = null;
						lobjCoverage.midQRSubLine = lobjSubLine.mid;
						lobjCoverage.midCoverage = larrAuxCoverages[j].getKey();
						lobjCoverage.mstrLabel = larrAuxCoverages[j].getLabel();
						lobjCoverage.mbIsHeader = larrAuxCoverages[j].IsHeader();
						lobjCoverage.mbMandatory = larrAuxCoverages[j].IsMandatory();
						if ( lobjCoverage.mbMandatory )
							lobjCoverage.mbPresent = true;
						else
							lobjCoverage.mbPresent = null;
						larrTaxes = larrAuxCoverages[j].GetCurrentTaxes();
						larrFields = new ArrayList<PadField>();
						for ( k = 0; k < larrTaxes.length; k++ )
						{
							lobjField = new PadField();
							lobjField.midField = larrTaxes[k].getKey();
							lobjField.mlngColIndex = larrTaxes[k].GetColumnOrder();
							lobjField.mstrLabel = larrTaxes[k].getLabel();
							lobjField.midType = (UUID)larrTaxes[k].getAt(2);
							lobjField.mstrUnits = (String)larrTaxes[k].getAt(3);
							lobjField.mstrDefault = (String)larrTaxes[k].getAt(4);
							lobjField.midRefersTo = (UUID)larrTaxes[k].getAt(7);
							lobjField.mbVariesByObject = larrTaxes[k].GetVariesByObject();
							larrFields.add(lobjField);
							lmapCoverages.put(larrTaxes[k].getKey(), lobjCoverage);
							lmapFields.put(larrTaxes[k].getKey(), lobjField);
						}
						lobjCoverage.marrFields = larrFields.toArray(new PadField[larrFields.size()]);
						lobjSubLine.marrCoverages.add(lobjCoverage);
					}

					larrAuxValues = larrLocalSubLines[i].GetCurrentValues();
					for ( j = 0 ; j < larrAuxValues.length; j++ )
					{
						lobjValue = new PadValue();
						lobjValue.FromObject(larrAuxValues[j]);
						lobjValue.mrefCoverage = lmapCoverages.get(lobjValue.midField);
						lobjValue.mrefField = lmapFields.get(lobjValue.midField);
						lobjValue.mlngObject = ( lobjValue.midObject == null ? -1 : lmapObjects.get(lobjValue.midObject) );
						lobjValue.mbDeleted = false;
						lobjSubLine.marrValues.add(lobjValue);
					}

					for ( j = 0 ; j < lobjSubLine.marrCoverages.size(); j++ )
					{
						for ( k = 0; k < lobjSubLine.marrCoverages.get(j).marrFields.length; k++ )
						{
							lobjField = lobjSubLine.marrCoverages.get(j).marrFields[k];
							if ( lobjField.mbVariesByObject )
							{
								for ( l = 0; l < marrObjects.size(); l++ )
								{
									if ( lobjSubLine.FindValue(lobjField.midField, l, 0) >= 0 )
										continue;
									lobjValue = new PadValue();
									lobjValue.mid = null;
									lobjValue.mstrValue = lobjField.mstrDefault;
									lobjValue.midQRSubLine = lobjSubLine.mid;
									lobjValue.midField = lobjField.midField;
									lobjValue.midObject = marrObjects.get(l).mid;
									lobjValue.mrefCoverage = lobjSubLine.marrCoverages.get(j);
									lobjValue.mrefField = lobjField;
									lobjValue.mlngObject = l;
									lobjValue.mbDeleted = false;
									lobjSubLine.marrValues.add(lobjValue);
								}
							}
							else
							{
								if ( lobjSubLine.FindValue(lobjField.midField, -1, 0) >= 0 )
									continue;
								lobjValue = new PadValue();
								lobjValue.mid = null;
								lobjValue.mstrValue = lobjField.mstrDefault;
								lobjValue.midQRSubLine = lobjSubLine.mid;
								lobjValue.midField = lobjField.midField;
								lobjValue.midObject = null;
								lobjValue.mrefCoverage = lobjSubLine.marrCoverages.get(j);
								lobjValue.mrefField = lobjField;
								lobjValue.mlngObject = -1;
								lobjValue.mbDeleted = false;
								lobjSubLine.marrValues.add(lobjValue);
							}
						}
					}

					marrSubLines.add(lobjSubLine);
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			mbValid = true;
		}

		public Remap[] GetRemapIntoPad()
		{
			Remap[] larrResult;
			int i;

			if ( !mbValid )
			{
				larrResult = new Remap[1];

				larrResult[0] = new Remap();
				larrResult[0].typeId = Constants.ObjID_QuoteRequest.toString();
				larrResult[0].remapIds = new Remap.RemapId[1];
				larrResult[0].remapIds[0] = new Remap.RemapId();
				larrResult[0].remapIds[0].oldId = null;
				larrResult[0].remapIds[0].newId = mid.toString();
				larrResult[0].remapIds[0].newIdIsInPad = true;

				return larrResult;
			}

			larrResult = new Remap[2];

			larrResult[0] = new Remap();
			larrResult[0].typeId = Constants.ObjID_QuoteRequest.toString();
			larrResult[0].remapIds = new Remap.RemapId[1];
			larrResult[0].remapIds[0] = new Remap.RemapId();
			larrResult[0].remapIds[0].oldId = mobjQuoteRequest.mid.toString();
			larrResult[0].remapIds[0].newId = mid.toString();
			larrResult[0].remapIds[0].newIdIsInPad = true;

			larrResult[1] = new Remap();
			larrResult[1].typeId = Constants.ObjID_QuoteRequestObject.toString();
			larrResult[1].remapIds = new Remap.RemapId[marrObjects.size()];
			for ( i = 0; i < larrResult[1].remapIds.length; i++ )
			{
				larrResult[1].remapIds[i] = new Remap.RemapId();
				larrResult[1].remapIds[i].oldId = marrObjects.get(i).mid.toString();
				larrResult[1].remapIds[i].newId = mid.toString() + ":" + i;
				larrResult[1].remapIds[i].newIdIsInPad = true;
			}

			return larrResult;
		}

		public Remap[] GetRemapFromPad(boolean pbWithCommit)
		{
			Remap[] larrResult;
			int i;

			larrResult = new Remap[2];

			larrResult[0] = new Remap();
			larrResult[0].typeId = Constants.ObjID_QuoteRequest.toString();
			larrResult[0].remapIds = new Remap.RemapId[1];
			larrResult[0].remapIds[0] = new Remap.RemapId();
			larrResult[0].remapIds[0].oldId = mid.toString();
			larrResult[0].remapIds[0].newId = (mobjQuoteRequest.mid == null ? null : mobjQuoteRequest.mid.toString());
			larrResult[0].remapIds[0].newIdIsInPad = false;

			larrResult[1] = new Remap();
			larrResult[1].typeId = Constants.ObjID_QuoteRequestObject.toString();
			larrResult[1].remapIds = new Remap.RemapId[marrObjects.size()];
			for ( i = 0; i < larrResult[1].remapIds.length; i++ )
			{
				larrResult[1].remapIds[i] = new Remap.RemapId();
				larrResult[1].remapIds[i].oldId = mid.toString() + ":" + i;
				if ( (pbWithCommit && marrObjects.get(i).mbDeleted) || (marrObjects.get(i).mid == null) )
					larrResult[1].remapIds[i].newId = null;
				else
					larrResult[1].remapIds[i].newId = marrObjects.get(i).mid.toString();
				larrResult[1].remapIds[i].newIdIsInPad = false;
			}

			return larrResult;
		}

		public QuoteRequest WriteBasics()
			throws BigBangException, CorruptedPadException
		{
			Client lobjClient;
			Mediator lobjMed;
			IProcess lobjProc;
			QuoteRequest lobjResult;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( midClient == null )
			{
				lobjClient = null;
				lobjMed = null;
			}
			else
			{
				try
				{
					lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
					lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjClient.getAt(8));
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}

			if ( mobjQuoteRequest.midProcess == null )
				lobjProc = null;
			else
			{
				try
				{
					lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjQuoteRequest.midProcess);
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}

			lobjResult = new QuoteRequest();

			lobjResult.id = mid.toString();
			lobjResult.processId = ( lobjProc == null ? null : lobjProc.getKey().toString() );
			lobjResult.managerId = ( lobjProc == null ? null : lobjProc.GetManagerID().toString() );
			lobjResult.processNumber = mobjQuoteRequest.mstrNumber;
			lobjResult.clientId = ( lobjClient == null ? null : lobjClient.getKey().toString() );
			lobjResult.clientName = ( lobjClient == null ? null : lobjClient.getLabel() );
			lobjResult.clientNumber = ( lobjClient == null ? null : ((Integer)lobjClient.getAt(1)).toString() );
			lobjResult.processId = ( mobjQuoteRequest.midProcess == null ? null : mobjQuoteRequest.midProcess.toString() );
			lobjResult.mediatorId = ( mobjQuoteRequest.midMediator == null ? null : mobjQuoteRequest.midMediator.toString() );
			lobjResult.inheritMediatorId = ( lobjMed == null ? null : lobjMed.getKey().toString() );
			lobjResult.inheritMediatorName = ( lobjMed == null ? null : lobjMed.getLabel() );
			lobjResult.notes = mobjQuoteRequest.mstrNotes;
			lobjResult.caseStudy = ( mobjQuoteRequest.mbCaseStudy == null ? false : mobjQuoteRequest.mbCaseStudy );
			lobjResult.docushare = mobjQuoteRequest.mstrDocuShare;
			lobjResult.isOpen = true;

			return lobjResult;
		}

		public void WriteResult(QuoteRequest pobjResult)
			throws CorruptedPadException
		{
			ArrayList<QuoteRequest.RequestSubLine> larrSubLines;
			ArrayList<QuoteRequest.HeaderField> larrHeaders;
			ArrayList<QuoteRequest.Coverage> larrAuxCoverages;
			ArrayList<QuoteRequest.Coverage.Variability> larrVariability;
			HashMap<Integer, QuoteRequest.ColumnHeader> larrColumns;
			ArrayList<QuoteRequest.TableSection.TableField> larrTableFields;
			ArrayList<QuoteRequest.ExtraField> larrExtraFields;
			QuoteRequest.RequestSubLine lobjSubLine;
			QuoteRequest.HeaderField lobjHeader;
			QuoteRequest.Coverage lobjAuxCoverage;
			QuoteRequest.Coverage.Variability lobjVariability;
			QuoteRequest.ColumnHeader lobjColumn;
			QuoteRequest.TableSection.TableField lobjTableField;
			QuoteRequest.ExtraField lobjExtraField;
			int i, j, k;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			pobjResult.id = mid.toString();

			larrSubLines = new ArrayList<QuoteRequest.RequestSubLine>();
			for ( i = 0; i < marrSubLines.size(); i++ )
			{
				if ( marrSubLines.get(i).mbDeleted )
					continue;

				lobjSubLine = new QuoteRequest.RequestSubLine();
				lobjSubLine.qrslId = mid.toString() + ":" + Integer.toString(i); //marrSubLines.get(i).mid.toString();
				lobjSubLine.categoryId = marrSubLines.get(i).mobjSubLine.getLine().getCategory().getKey().toString();
				lobjSubLine.lineId = marrSubLines.get(i).mobjSubLine.getLine().getKey().toString();
				lobjSubLine.sublineId = marrSubLines.get(i).mobjSubLine.getKey().toString();
				lobjSubLine.headerText = marrSubLines.get(i).mstrLabel;

				larrHeaders = new ArrayList<QuoteRequest.HeaderField>();
				for ( j = 0; j < marrSubLines.get(i).marrValues.size(); j++ )
				{
					if ( !marrSubLines.get(i).marrValues.get(j).mrefCoverage.mbIsHeader ||
							(marrSubLines.get(i).marrValues.get(j).mlngObject >= 0) )
						continue;

					lobjHeader = new QuoteRequest.HeaderField();
					lobjHeader.fieldId = marrSubLines.get(i).marrValues.get(j).midField.toString();
					lobjHeader.fieldName = marrSubLines.get(i).marrValues.get(j).mrefField.mstrLabel;
					lobjHeader.type = ServerToClient.sGetFieldTypeByID(marrSubLines.get(i).marrValues.get(j).mrefField.midType);
					lobjHeader.unitsLabel = marrSubLines.get(i).marrValues.get(j).mrefField.mstrUnits;
					lobjHeader.refersToId = ( marrSubLines.get(i).marrValues.get(j).mrefField.midRefersTo == null ? null :
						marrSubLines.get(i).marrValues.get(j).mrefField.midRefersTo.toString() );
					lobjHeader.value = marrSubLines.get(i).marrValues.get(j).mstrValue;
					lobjHeader.order = marrSubLines.get(i).marrValues.get(j).mrefField.mlngColIndex;
					larrHeaders.add(lobjHeader);
				}
				lobjSubLine.headerFields = larrHeaders.toArray(new QuoteRequest.HeaderField[larrHeaders.size()]);

				larrAuxCoverages = new ArrayList<QuoteRequest.Coverage>();
				for ( j = 0; j < marrSubLines.get(i).marrCoverages.size(); j++ )
				{
					if ( marrSubLines.get(i).marrCoverages.get(j).mbIsHeader )
						continue;

					lobjAuxCoverage = new QuoteRequest.Coverage();
					lobjAuxCoverage.coverageId = marrSubLines.get(i).marrCoverages.get(j).midCoverage.toString();
					lobjAuxCoverage.coverageName = marrSubLines.get(i).marrCoverages.get(j).mstrLabel;
					lobjAuxCoverage.mandatory = marrSubLines.get(i).marrCoverages.get(j).mbMandatory;
					lobjAuxCoverage.order = marrSubLines.get(i).marrCoverages.get(j).mlngOrder;
					lobjAuxCoverage.presentInRequestSubLine = marrSubLines.get(i).marrCoverages.get(j).mbPresent;
					larrVariability = new ArrayList<QuoteRequest.Coverage.Variability>();
					for ( k = 0; k < marrSubLines.get(i).marrCoverages.get(j).marrFields.length; k++ )
					{
						if ( marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mlngColIndex < 0 )
							continue;

						lobjVariability = new QuoteRequest.Coverage.Variability();
						lobjVariability.columnIndex = marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mlngColIndex;
						lobjVariability.variesByObject = marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mbVariesByObject;
						lobjVariability.variesByExercise = false;
						larrVariability.add(lobjVariability);
					}
					lobjAuxCoverage.variability =
							larrVariability.toArray(new QuoteRequest.Coverage.Variability[larrVariability.size()]);
					larrAuxCoverages.add(lobjAuxCoverage);
				}
				lobjSubLine.coverages = larrAuxCoverages.toArray(new QuoteRequest.Coverage[larrAuxCoverages.size()]);

				larrColumns = new HashMap<Integer, QuoteRequest.ColumnHeader>();
				for ( j = 0; j < marrSubLines.get(i).marrCoverages.size(); j++ )
				{
					if ( marrSubLines.get(i).marrCoverages.get(j).mbIsHeader )
						continue;

					for ( k = 0; k < marrSubLines.get(i).marrCoverages.get(j).marrFields.length; k++ )
					{
						if ( marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mlngColIndex < 0 )
							continue;
						if ( larrColumns.containsKey(marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mlngColIndex) )
							continue;

						lobjColumn = new QuoteRequest.ColumnHeader();
						lobjColumn.label = marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mstrLabel;
						lobjColumn.type = ServerToClient.sGetFieldTypeByID(marrSubLines.get(i).marrCoverages.get(j).marrFields[k].midType);
						lobjColumn.unitsLabel = marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mstrUnits;
						lobjColumn.refersToId = ( marrSubLines.get(i).marrCoverages.get(j).marrFields[k].midRefersTo == null ? null :
							marrSubLines.get(i).marrCoverages.get(j).marrFields[k].midRefersTo.toString() );
						larrColumns.put(marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mlngColIndex, lobjColumn);
					}
					break;
				}
				lobjSubLine.columns = new QuoteRequest.ColumnHeader[larrColumns.size()];
				for ( Integer ii: larrColumns.keySet() )
					lobjSubLine.columns[ii] = larrColumns.get(ii);

				if ( lobjSubLine.coverages.length == 0 )
					lobjSubLine.tableData = new QuoteRequest.TableSection[0];
				else
				{
					lobjSubLine.tableData = new QuoteRequest.TableSection[1];
					lobjSubLine.tableData[0] = new QuoteRequest.TableSection();
					lobjSubLine.tableData[0].pageId = mid.toString() + ":" + Integer.toString(i) + ":-1";
					larrTableFields = new ArrayList<QuoteRequest.TableSection.TableField>();
					for ( j = 0; j < marrSubLines.get(i).marrValues.size(); j++ )
					{
						if ( marrSubLines.get(i).marrValues.get(j).mrefCoverage.mbIsHeader ||
								(marrSubLines.get(i).marrValues.get(j).mrefField.mlngColIndex < 0) ||
								(marrSubLines.get(i).marrValues.get(j).mlngObject >= 0) )
							continue;

						lobjTableField = new QuoteRequest.TableSection.TableField();
						lobjTableField.fieldId = marrSubLines.get(i).marrValues.get(j).midField.toString();
						lobjTableField.coverageId = marrSubLines.get(i).marrValues.get(j).mrefCoverage.midCoverage.toString();
						lobjTableField.columnIndex = marrSubLines.get(i).marrValues.get(j).mrefField.mlngColIndex;
						lobjTableField.value = marrSubLines.get(i).marrValues.get(j).mstrValue;
						larrTableFields.add(lobjTableField);
					}
					lobjSubLine.tableData[0].data =
							larrTableFields.toArray(new QuoteRequest.TableSection.TableField[larrTableFields.size()]);
				}

				larrExtraFields = new ArrayList<QuoteRequest.ExtraField>();
				for ( j = 0; j < marrSubLines.get(i).marrValues.size(); j++ )
				{
					if ( marrSubLines.get(i).marrValues.get(j).mrefCoverage.mbIsHeader ||
							(marrSubLines.get(i).marrValues.get(j).mrefField.mlngColIndex >= 0) ||
							(marrSubLines.get(i).marrValues.get(j).mlngObject >= 0) )
						continue;

					lobjExtraField = new QuoteRequest.ExtraField();
					lobjExtraField.fieldId = marrSubLines.get(i).marrValues.get(j).midField.toString();
					lobjExtraField.fieldName = marrSubLines.get(i).marrValues.get(j).mrefField.mstrLabel;
					lobjExtraField.type = ServerToClient.sGetFieldTypeByID(marrSubLines.get(i).marrValues.get(j).mrefField.midType);
					lobjExtraField.unitsLabel = marrSubLines.get(i).marrValues.get(j).mrefField.mstrUnits;
					lobjExtraField.refersToId = ( marrSubLines.get(i).marrValues.get(j).mrefField.midRefersTo == null ? null :
						marrSubLines.get(i).marrValues.get(j).mrefField.midRefersTo.toString() );
					lobjExtraField.value = marrSubLines.get(i).marrValues.get(j).mstrValue;
					lobjExtraField.order = marrSubLines.get(i).marrValues.get(j).mrefField.mlngColIndex;
					lobjExtraField.coverageId = marrSubLines.get(i).marrValues.get(j).mrefCoverage.midCoverage.toString();
					lobjExtraField.coverageName = marrSubLines.get(i).marrValues.get(j).mrefCoverage.mstrLabel;
					lobjExtraField.mandatory = marrSubLines.get(i).marrValues.get(j).mrefCoverage.mbMandatory;
					lobjExtraField.covorder = marrSubLines.get(i).marrValues.get(j).mrefCoverage.mlngOrder;
					larrExtraFields.add(lobjExtraField);
				}
				lobjSubLine.extraData = larrExtraFields.toArray(new QuoteRequest.ExtraField[larrExtraFields.size()]);

				java.util.Arrays.sort(lobjSubLine.headerFields, new Comparator<QuoteRequest.HeaderField>()
				{
					public int compare(QuoteRequest.HeaderField o1, QuoteRequest.HeaderField o2)
					{
						if ( o1.order == o2.order )
						{
							if ( o1.type == o2.type )
							{
								if ( o1.refersToId == o1.refersToId )
									return o1.fieldName.compareTo(o2.fieldName);
								return o1.refersToId.compareTo(o2.refersToId);
							}
							return o1.type.compareTo(o2.type);
						}
						if ( (o1.order < 0) || (o2.order < 0) )
							return o2.order - o1.order;
						return o1.order - o2.order;
					}
				});
				java.util.Arrays.sort(lobjSubLine.coverages, new Comparator<QuoteRequest.Coverage>()
				{
					public int compare(QuoteRequest.Coverage o1, QuoteRequest.Coverage o2)
					{
						if ( o1.mandatory == o2.mandatory )
						{
							if ( o1.order == o2.order )
								return o1.coverageName.compareTo(o2.coverageName);
							return o1.order - o2.order;
						}
						if ( o1.mandatory )
							return -1;
						return 1;
					}
				});
				java.util.Arrays.sort(lobjSubLine.extraData, new Comparator<QuoteRequest.ExtraField>()
				{
					public int compare(QuoteRequest.ExtraField o1, QuoteRequest.ExtraField o2)
					{
						if ( o1.coverageId.equals(o2.coverageId) )
						{
							if ( o1.order == o2.order )
							{
								if ( o1.type == o2.type )
								{
									if ( o1.refersToId == o1.refersToId )
										return o1.fieldName.compareTo(o2.fieldName);
									return o1.refersToId.compareTo(o2.refersToId);
								}
								return o1.type.compareTo(o2.type);
							}
							if ( (o1.order < 0) || (o2.order < 0) )
								return o2.order - o1.order;
							return o1.order - o2.order;
						}
						if ( o1.mandatory == o2.mandatory )
						{
							if ( o1.covorder == o2.covorder )
								return o1.coverageName.compareTo(o2.coverageName);
							return o1.covorder - o2.covorder;
						}
						if ( o1.mandatory )
							return -1;
						return 1;
					}
				});

				larrSubLines.add(lobjSubLine);
			}

			pobjResult.requestData = larrSubLines.toArray(new QuoteRequest.RequestSubLine[larrSubLines.size()]);

			java.util.Arrays.sort(pobjResult.requestData, new Comparator<QuoteRequest.RequestSubLine>()
			{
				public int compare(QuoteRequest.RequestSubLine o1, QuoteRequest.RequestSubLine o2)
				{
					return o1.headerText.compareTo(o2.headerText);
				}
			});
		}

		public void WritePage(QuoteRequest.TableSection pobjResult, int plngSubLine, int plngObject)
			throws CorruptedPadException
		{
			PadSubLine lobjSubLine;
			int llngCoverages;
			int llngColumns;
			ArrayList<QuoteRequest.TableSection.TableField> larrTableFields;
			QuoteRequest.TableSection.TableField lobjTableField;
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			pobjResult.pageId = mid.toString() + ":" + Integer.toString(plngSubLine) + ":" + Integer.toString(plngObject);

			lobjSubLine = marrSubLines.get(plngSubLine);

			llngCoverages = 0;
			for ( i = 0; i < lobjSubLine.marrCoverages.size(); i++ )
			{
				if ( lobjSubLine.marrCoverages.get(i).mbIsHeader )
					continue;
				llngCoverages++;
			}

			llngColumns = -1;
			for ( i = 0; i < lobjSubLine.marrCoverages.size(); i++ )
			{
				if ( lobjSubLine.marrCoverages.get(i).mbIsHeader )
					continue;
				for ( j = 0; j < lobjSubLine.marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( lobjSubLine.marrCoverages.get(i).marrFields[j].mlngColIndex > llngColumns )
						llngColumns = lobjSubLine.marrCoverages.get(i).marrFields[j].mlngColIndex;
				}
			}
			llngColumns++;

			if ( llngCoverages * llngColumns == 0 )
				pobjResult.data = new QuoteRequest.TableSection.TableField[0];
			else
			{
				larrTableFields = new ArrayList<QuoteRequest.TableSection.TableField>();
				for ( i = 0; i < lobjSubLine.marrValues.size(); i++ )
				{
					if ( lobjSubLine.marrValues.get(i).mrefCoverage.mbIsHeader ||
							(lobjSubLine.marrValues.get(i).mrefField.mlngColIndex < 0) ||
							(lobjSubLine.marrValues.get(i).mlngObject != plngObject) )
						continue;

					lobjTableField = new QuoteRequest.TableSection.TableField();
					lobjTableField.fieldId = lobjSubLine.marrValues.get(i).midField.toString();
					lobjTableField.coverageId = lobjSubLine.marrValues.get(i).mrefCoverage.midCoverage.toString();
					lobjTableField.columnIndex = lobjSubLine.marrValues.get(i).mrefField.mlngColIndex;
					lobjTableField.value = lobjSubLine.marrValues.get(i).mstrValue;
					larrTableFields.add(lobjTableField);
				}
				pobjResult.data = larrTableFields.toArray(new QuoteRequest.TableSection.TableField[larrTableFields.size()]);
			}
		}

		public TipifiedListItem[] GetObjects()
			throws CorruptedPadException
		{
			ArrayList<TipifiedListItem> larrResult;
			TipifiedListItem lobjItem;
			int i;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			larrResult = new ArrayList<TipifiedListItem>();
			for ( i = 0; i < marrObjects.size(); i++ )
			{
				if ( marrObjects.get(i).mbDeleted )
					continue;
				lobjItem = new TipifiedListItem();
				lobjItem.id = mid + ":" + i;
				lobjItem.value = marrObjects.get(i).mstrName;
				larrResult.add(lobjItem);

			}
			return larrResult.toArray(new TipifiedListItem[larrResult.size()]);
		}

		public void WriteSubLine(QuoteRequest.RequestSubLine pobjResult, int plngSubLine)
		{
			ArrayList<QuoteRequest.HeaderField> larrHeaders;
			ArrayList<QuoteRequest.Coverage> larrAuxCoverages;
			ArrayList<QuoteRequest.Coverage.Variability> larrVariability;
			HashMap<Integer, QuoteRequest.ColumnHeader> larrColumns;
			ArrayList<QuoteRequest.TableSection.TableField> larrTableFields;
			ArrayList<QuoteRequest.ExtraField> larrExtraFields;
			QuoteRequest.HeaderField lobjHeader;
			QuoteRequest.Coverage lobjAuxCoverage;
			QuoteRequest.Coverage.Variability lobjVariability;
			QuoteRequest.ColumnHeader lobjColumn;
			QuoteRequest.TableSection.TableField lobjTableField;
			QuoteRequest.ExtraField lobjExtraField;
			int i, j;

			pobjResult.qrslId = mid.toString() + ":" + Integer.toString(plngSubLine); //marrSubLines.get(i).midSubLine.toString();
			pobjResult.categoryId = marrSubLines.get(plngSubLine).mobjSubLine.getLine().getCategory().getKey().toString();
			pobjResult.lineId = marrSubLines.get(plngSubLine).mobjSubLine.getLine().getKey().toString();
			pobjResult.sublineId = marrSubLines.get(plngSubLine).mobjSubLine.getKey().toString();
			pobjResult.headerText = marrSubLines.get(plngSubLine).mstrLabel;

			larrHeaders = new ArrayList<QuoteRequest.HeaderField>();
			for ( i = 0; i < marrSubLines.get(plngSubLine).marrValues.size(); i++ )
			{
				if ( !marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.mbIsHeader ||
						(marrSubLines.get(plngSubLine).marrValues.get(i).mlngObject >= 0) )
					continue;

				lobjHeader = new QuoteRequest.HeaderField();
				lobjHeader.fieldId = marrSubLines.get(plngSubLine).marrValues.get(i).midField.toString();
				lobjHeader.fieldName = marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mstrLabel;
				lobjHeader.type = ServerToClient.sGetFieldTypeByID(marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.midType);
				lobjHeader.unitsLabel = marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mstrUnits;
				lobjHeader.refersToId = ( marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.midRefersTo == null ? null :
					marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.midRefersTo.toString() );
				lobjHeader.value = marrSubLines.get(plngSubLine).marrValues.get(i).mstrValue;
				lobjHeader.order = marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mlngColIndex;
				larrHeaders.add(lobjHeader);
			}
			pobjResult.headerFields = larrHeaders.toArray(new QuoteRequest.HeaderField[larrHeaders.size()]);

			larrAuxCoverages = new ArrayList<QuoteRequest.Coverage>();
			for ( i = 0; i < marrSubLines.get(plngSubLine).marrCoverages.size(); i++ )
			{
				if ( marrSubLines.get(plngSubLine).marrCoverages.get(i).mbIsHeader )
					continue;

				lobjAuxCoverage = new QuoteRequest.Coverage();
				lobjAuxCoverage.coverageId = marrSubLines.get(plngSubLine).marrCoverages.get(i).midCoverage.toString();
				lobjAuxCoverage.coverageName = marrSubLines.get(plngSubLine).marrCoverages.get(i).mstrLabel;
				lobjAuxCoverage.mandatory = marrSubLines.get(plngSubLine).marrCoverages.get(i).mbMandatory;
				lobjAuxCoverage.order = marrSubLines.get(plngSubLine).marrCoverages.get(i).mlngOrder;
				lobjAuxCoverage.presentInRequestSubLine = marrSubLines.get(plngSubLine).marrCoverages.get(i).mbPresent;
				larrVariability = new ArrayList<QuoteRequest.Coverage.Variability>();
				for ( j = 0; j < marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mlngColIndex < 0 )
						continue;

					lobjVariability = new QuoteRequest.Coverage.Variability();
					lobjVariability.columnIndex = marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mlngColIndex;
					lobjVariability.variesByObject = marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mbVariesByObject;
					lobjVariability.variesByExercise = false;
					larrVariability.add(lobjVariability);
				}
				lobjAuxCoverage.variability =
						larrVariability.toArray(new QuoteRequest.Coverage.Variability[larrVariability.size()]);
				larrAuxCoverages.add(lobjAuxCoverage);
			}
			pobjResult.coverages = larrAuxCoverages.toArray(new QuoteRequest.Coverage[larrAuxCoverages.size()]);

			larrColumns = new HashMap<Integer, QuoteRequest.ColumnHeader>();
			for ( i = 0; i < marrSubLines.get(plngSubLine).marrCoverages.size(); i++ )
			{
				if ( marrSubLines.get(plngSubLine).marrCoverages.get(i).mbIsHeader )
					continue;

				for ( j = 0; j < marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mlngColIndex < 0 )
						continue;
					if ( larrColumns.get(marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mlngColIndex) != null )
						continue;

					lobjColumn = new QuoteRequest.ColumnHeader();
					lobjColumn.label = marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mstrLabel;
					lobjColumn.type = ServerToClient.sGetFieldTypeByID(marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].midType);
					lobjColumn.unitsLabel = marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mstrUnits;
					lobjColumn.refersToId = ( marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].midRefersTo == null ? null :
						marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].midRefersTo.toString() );
					larrColumns.put(marrSubLines.get(plngSubLine).marrCoverages.get(i).marrFields[j].mlngColIndex, lobjColumn);
				}
				break;
			}
			pobjResult.columns = new QuoteRequest.ColumnHeader[larrColumns.size()];
			for ( Integer ii: larrColumns.keySet() )
				pobjResult.columns[ii] = larrColumns.get(ii);

			if ( pobjResult.coverages.length == 0 )
				pobjResult.tableData = new QuoteRequest.TableSection[0];
			else
			{
				pobjResult.tableData = new QuoteRequest.TableSection[1];
				pobjResult.tableData[0] = new QuoteRequest.TableSection();
				pobjResult.tableData[0].pageId = mid.toString() + ":" + Integer.toString(plngSubLine) + ":-1";
				larrTableFields = new ArrayList<QuoteRequest.TableSection.TableField>();
				for ( i = 0; i < marrSubLines.get(plngSubLine).marrValues.size(); i++ )
				{
					if ( marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.mbIsHeader ||
							(marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mlngColIndex < 0) ||
							(marrSubLines.get(plngSubLine).marrValues.get(i).mlngObject >= 0) )
						continue;

					lobjTableField = new QuoteRequest.TableSection.TableField();
					lobjTableField.fieldId = marrSubLines.get(plngSubLine).marrValues.get(i).midField.toString();
					lobjTableField.coverageId = marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.midCoverage.toString();
					lobjTableField.columnIndex = marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mlngColIndex;
					lobjTableField.value = marrSubLines.get(plngSubLine).marrValues.get(i).mstrValue;
					larrTableFields.add(lobjTableField);
				}
				pobjResult.tableData[0].data =
						larrTableFields.toArray(new QuoteRequest.TableSection.TableField[larrTableFields.size()]);
			}

			larrExtraFields = new ArrayList<QuoteRequest.ExtraField>();
			for ( i = 0; i < marrSubLines.get(plngSubLine).marrValues.size(); i++ )
			{
				if ( marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.mbIsHeader ||
						(marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mlngColIndex >= 0) ||
						(marrSubLines.get(plngSubLine).marrValues.get(i).mlngObject >= 0) )
					continue;

				lobjExtraField = new QuoteRequest.ExtraField();
				lobjExtraField.fieldId = marrSubLines.get(plngSubLine).marrValues.get(i).midField.toString();
				lobjExtraField.fieldName = marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mstrLabel;
				lobjExtraField.type = ServerToClient.sGetFieldTypeByID(marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.midType);
				lobjExtraField.unitsLabel = marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mstrUnits;
				lobjExtraField.refersToId = ( marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.midRefersTo == null ? null :
					marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.midRefersTo.toString() );
				lobjExtraField.value = marrSubLines.get(plngSubLine).marrValues.get(i).mstrValue;
				lobjExtraField.order = marrSubLines.get(plngSubLine).marrValues.get(i).mrefField.mlngColIndex;
				lobjExtraField.coverageId = marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.midCoverage.toString();
				lobjExtraField.coverageName = marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.mstrLabel;
				lobjExtraField.mandatory = marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.mbMandatory;
				lobjExtraField.covorder = marrSubLines.get(plngSubLine).marrValues.get(i).mrefCoverage.mlngOrder;
				larrExtraFields.add(lobjExtraField);
			}
			pobjResult.extraData = larrExtraFields.toArray(new QuoteRequest.ExtraField[larrExtraFields.size()]);

			java.util.Arrays.sort(pobjResult.headerFields, new Comparator<QuoteRequest.HeaderField>()
			{
				public int compare(QuoteRequest.HeaderField o1, QuoteRequest.HeaderField o2)
				{
					if ( o1.order == o2.order )
					{
						if ( o1.type == o2.type )
						{
							if ( o1.refersToId == o1.refersToId )
								return o1.fieldName.compareTo(o2.fieldName);
							return o1.refersToId.compareTo(o2.refersToId);
						}
						return o1.type.compareTo(o2.type);
					}
					if ( (o1.order < 0) || (o2.order < 0) )
						return o2.order - o1.order;
					return o1.order - o2.order;
				}
			});
			java.util.Arrays.sort(pobjResult.coverages, new Comparator<QuoteRequest.Coverage>()
			{
				public int compare(QuoteRequest.Coverage o1, QuoteRequest.Coverage o2)
				{
					if ( o1.mandatory == o2.mandatory )
					{
						if ( o1.order == o2.order )
							return o1.coverageName.compareTo(o2.coverageName);
						return o1.order - o2.order;
					}
					if ( o1.mandatory )
						return -1;
					return 1;
				}
			});
			java.util.Arrays.sort(pobjResult.extraData, new Comparator<QuoteRequest.ExtraField>()
			{
				public int compare(QuoteRequest.ExtraField o1, QuoteRequest.ExtraField o2)
				{
					if ( o1.coverageId.equals(o2.coverageId) )
					{
						if ( o1.order == o2.order )
						{
							if ( o1.type == o2.type )
							{
								if ( o1.refersToId == o1.refersToId )
									return o1.fieldName.compareTo(o2.fieldName);
								return o1.refersToId.compareTo(o2.refersToId);
							}
							return o1.type.compareTo(o2.type);
						}
						if ( (o1.order < 0) || (o2.order < 0) )
							return o2.order - o1.order;
						return o1.order - o2.order;
					}
					if ( o1.mandatory == o2.mandatory )
					{
						if ( o1.covorder == o2.covorder )
							return o1.coverageName.compareTo(o2.coverageName);
						return o1.covorder - o2.covorder;
					}
					if ( o1.mandatory )
						return -1;
					return 1;
				}
			});
		}

		public void WriteObject(QuoteRequestObject pobjResult, int plngObject)
			throws BigBangException, CorruptedPadException
		{
			QuoteRequestObjectData lobjObject;
			ObjectBase lobjZipCode;
			int i, j;
			ArrayList<QuoteRequestObject.SubLineData> larrSubLines;
			QuoteRequestObject.SubLineData lobjSubLine;
			ArrayList<PadCoverage> larrCoverages;
			PadCoverage[] larrSortedCoverages;
			PadCoverage lobjHeaderCoverage;
			HashMap<UUID, ArrayList<QuoteRequestObject.CoverageData.FixedField>> larrFixed;
			QuoteRequestObject.CoverageData.FixedField lobjFixed;
			PadValue lobjValue;
			ArrayList<QuoteRequestObject.CoverageData.FixedField> larrAuxFixed;
			ArrayList<QuoteRequestObject.ColumnHeader> larrOutColumns;
			QuoteRequestObject.ColumnHeader lobjColumnHeader;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjObject = marrObjects.get(plngObject);
			pobjResult.id = mid.toString() + ":" + Integer.toString(plngObject);
//			pobjResult.ownerId = ( mobjQuoteRequest.mid == null ? mid.toString() : mobjQuoteRequest.mid.toString() );
			pobjResult.ownerId = mid.toString();
			pobjResult.unitIdentification = lobjObject.mstrName;
			if ( (lobjObject.mstrAddress1 == null) && (lobjObject.mstrAddress2 == null) && (lobjObject.midZipCode == null) )
				pobjResult.address = null;
			else
			{
				pobjResult.address = new Address();
				pobjResult.address.street1 = lobjObject.mstrAddress1;
				pobjResult.address.street2 = lobjObject.mstrAddress2;
				if ( lobjObject.midZipCode == null )
					pobjResult.address.zipCode = null;
				else
				{
					try
					{
						lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
								ObjectGUIDs.O_PostalCode), lobjObject.midZipCode);
					}
					catch (Throwable e)
					{
						throw new BigBangException(e.getMessage(), e);
					}
					pobjResult.address.zipCode = new ZipCode();
					pobjResult.address.zipCode.code = (String)lobjZipCode.getAt(0);
					pobjResult.address.zipCode.city = (String)lobjZipCode.getAt(1);
					pobjResult.address.zipCode.county = (String)lobjZipCode.getAt(2);
					pobjResult.address.zipCode.district = (String)lobjZipCode.getAt(3);
					pobjResult.address.zipCode.country = (String)lobjZipCode.getAt(4);
				}
			}
			pobjResult.typeId = lobjObject.midType.toString();
			try
			{
				pobjResult.typeText = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_ObjectType), lobjObject.midType).getLabel();
			}
			catch (Throwable e)
			{
				pobjResult.typeText = "(Erro a obter o nome do tipo.)";
			}

			pobjResult.taxNumberPerson = lobjObject.mstrFiscalI;
			pobjResult.genderId = ( lobjObject.midSex == null ? null : lobjObject.midSex.toString() );
			pobjResult.birthDate = ( lobjObject.mdtDateOfBirth == null ? null :
					lobjObject.mdtDateOfBirth.toString().substring(0, 10) );
			pobjResult.clientNumberPerson = ( lobjObject.mlngClientNumberI == null ? null : lobjObject.mlngClientNumberI.toString() );
			pobjResult.insuranceCompanyInternalIdPerson = lobjObject.mstrInsurerIDI;

			pobjResult.taxNumberCompany = lobjObject.mstrFiscalC;
			pobjResult.caeId = ( lobjObject.midPredomCAE == null ? null : lobjObject.midPredomCAE.toString() );
			pobjResult.grievousCaeId = ( lobjObject.midGrievousCAE == null ? null : lobjObject.midGrievousCAE.toString() );
			pobjResult.activityNotes = lobjObject.mstrActivityNotes;
			pobjResult.productNotes = lobjObject.mstrProductNotes;
			pobjResult.businessVolumeId = ( lobjObject.midSales == null ? null : lobjObject.midSales.toString() );
			pobjResult.europeanUnionEntity = lobjObject.mstrEUEntity;
			pobjResult.clientNumberGroup = ( lobjObject.mlngClientNumberC == null ? null : lobjObject.mlngClientNumberC.toString() );

			pobjResult.makeAndModel = lobjObject.mstrMakeAndModel;
			pobjResult.equipmentDescription = lobjObject.mstrEquipmentNotes;
			pobjResult.firstRegistryDate = ( lobjObject.mdtFirstRegistry == null ? null :
					lobjObject.mdtFirstRegistry.toString().substring(0, 10) );
			pobjResult.manufactureYear = ( lobjObject.mlngManufactureYear == null ? null :
					lobjObject.mlngManufactureYear.toString() );
			pobjResult.clientInternalId = lobjObject.mstrClientIDE;
			pobjResult.insuranceCompanyInternalIdVehicle = lobjObject.mstrInsurerIDE;

			pobjResult.siteDescription = lobjObject.mstrSiteNotes;

			pobjResult.species = lobjObject.mstrSpecies;
			pobjResult.race = lobjObject.mstrRace;
			pobjResult.birthYear = ( lobjObject.mlngAge == null ? null : lobjObject.mlngAge.toString() );
			pobjResult.cityRegistryNumber = lobjObject.mstrCityNumber;
			pobjResult.electronicIdTag = lobjObject.mstrElectronicIDTag;

			larrSubLines = new ArrayList<QuoteRequestObject.SubLineData>();
			for ( i = 0; i < marrSubLines.size(); i++ )
			{
				if ( marrSubLines.get(i).mbDeleted )
					continue;
				if ( !lobjObject.midType.equals(marrSubLines.get(i).midObjectType) )
					continue;

				lobjSubLine = new QuoteRequestObject.SubLineData();
				lobjSubLine.subLineId = mid.toString() + ":" + Integer.toString(i); //marrSubLines.get(i).midSubLine.toString();
				lobjSubLine.headerText = marrSubLines.get(i).mstrLabel;

				lobjHeaderCoverage = null;
				larrCoverages = new ArrayList<PadCoverage>();
				for ( j = 0; j < marrSubLines.get(i).marrCoverages.size(); j++ )
				{
					if ( marrSubLines.get(i).marrCoverages.get(j).mbIsHeader )
						lobjHeaderCoverage = marrSubLines.get(i).marrCoverages.get(j);
					else
						larrCoverages.add(marrSubLines.get(i).marrCoverages.get(j));
				}
				larrSortedCoverages = larrCoverages.toArray(new PadCoverage[larrCoverages.size()]);
				java.util.Arrays.sort(larrSortedCoverages, new Comparator<PadCoverage>()
				{
					public int compare(PadCoverage o1, PadCoverage o2)
					{
						if ( o1.mbMandatory == o2.mbMandatory )
						{
							if ( o1.mlngOrder == o2.mlngOrder )
								return o1.mstrLabel.compareTo(o2.mstrLabel);
							return o1.mlngOrder - o2.mlngOrder;
						}
						if ( o1.mbMandatory )
							return -1;
						return 1;
					}
				});
				lobjSubLine.coverageData = new QuoteRequestObject.CoverageData[larrSortedCoverages.length];
				larrFixed = new HashMap<UUID, ArrayList<QuoteRequestObject.CoverageData.FixedField>>();
				larrOutColumns = new ArrayList<QuoteRequestObject.ColumnHeader>();
				for ( j = 0; j < larrSortedCoverages.length; j++ )
				{
					lobjSubLine.coverageData[j] = new QuoteRequestObject.CoverageData();
					lobjSubLine.coverageData[j].coverageId = larrSortedCoverages[j].midCoverage.toString();
					try
					{
						lobjSubLine.coverageData[j].coverageLabel = Coverage.GetInstance(Engine.getCurrentNameSpace(),
								larrSortedCoverages[j].midCoverage).getLabel();
					}
					catch (Throwable e)
					{
						lobjSubLine.coverageData[j].coverageLabel = "(Erro a obter o nome da cobertura.)";
					}
					larrFixed.put(larrSortedCoverages[j].midCoverage, new ArrayList<QuoteRequestObject.CoverageData.FixedField>());
				}
				if ( lobjHeaderCoverage != null )
				{
					lobjSubLine.headerData = new QuoteRequestObject.HeaderData();
					larrFixed.put(lobjHeaderCoverage.midCoverage, new ArrayList<QuoteRequestObject.CoverageData.FixedField>());
				}

				for ( j = 0; j < marrSubLines.get(i).marrValues.size(); j++ )
				{
					lobjValue = marrSubLines.get(i).marrValues.get(j);
					if ( lobjValue.mbDeleted || (lobjValue.mlngObject != plngObject) )
						continue;

					lobjFixed = new QuoteRequestObject.CoverageData.FixedField();
					lobjFixed.fieldId = lobjValue.mrefField.midField.toString();
					lobjFixed.fieldName = lobjValue.mrefField.mstrLabel;
					lobjFixed.type = ServerToClient.sGetFieldTypeByID(lobjValue.mrefField.midType);
					lobjFixed.unitsLabel = lobjValue.mrefField.mstrUnits;
					lobjFixed.refersToId = ( lobjValue.mrefField.midRefersTo == null ? null :
							lobjValue.mrefField.midRefersTo.toString() );
					lobjFixed.columnIndex = lobjValue.mrefField.mlngColIndex;
					lobjFixed.value = lobjValue.mstrValue;
					larrFixed.get(lobjValue.mrefCoverage.midCoverage).add(lobjFixed);

					if ( lobjFixed.columnIndex >= 0 )
					{
						while ( larrOutColumns.size() <= lobjFixed.columnIndex )
							larrOutColumns.add(null);
						lobjColumnHeader = new QuoteRequestObject.ColumnHeader();
						lobjColumnHeader.index = lobjFixed.columnIndex;
						lobjColumnHeader.label = lobjFixed.fieldName;
						lobjColumnHeader.type = lobjFixed.type;
						lobjColumnHeader.unitsLabel = lobjFixed.unitsLabel;
						lobjColumnHeader.refersToId = lobjFixed.refersToId;
						larrOutColumns.set(lobjColumnHeader.index, lobjColumnHeader);
					}
				}

				for ( j = 0; j < lobjSubLine.coverageData.length; j++ )
				{
					larrAuxFixed = larrFixed.get(UUID.fromString(lobjSubLine.coverageData[j].coverageId));
					lobjSubLine.coverageData[j].fixedFields =
							larrAuxFixed.toArray(new QuoteRequestObject.CoverageData.FixedField[larrAuxFixed.size()]);
					java.util.Arrays.sort(lobjSubLine.coverageData[j].fixedFields,
							new Comparator<QuoteRequestObject.CoverageData.FixedField>()
					{
						public int compare(QuoteRequestObject.CoverageData.FixedField o1, QuoteRequestObject.CoverageData.FixedField o2)
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
				}

				for ( j = larrOutColumns.size() - 1; j >= 0; j-- )
					if ( larrOutColumns.get(j) == null )
						larrOutColumns.remove(j);
				lobjSubLine.columnHeaders = larrOutColumns.toArray(new QuoteRequestObject.ColumnHeader[larrOutColumns.size()]);

				if ( lobjHeaderCoverage != null )
				{
					larrAuxFixed = larrFixed.get(lobjHeaderCoverage.midCoverage);
					lobjSubLine.headerData.fixedFields =
							larrAuxFixed.toArray(new QuoteRequestObject.CoverageData.FixedField[larrAuxFixed.size()]);
					java.util.Arrays.sort(lobjSubLine.headerData.fixedFields,
							new Comparator<QuoteRequestObject.CoverageData.FixedField>()
					{
						public int compare(QuoteRequestObject.CoverageData.FixedField o1, QuoteRequestObject.CoverageData.FixedField o2)
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
				}

				larrSubLines.add(lobjSubLine);
			}
			pobjResult.objectData = larrSubLines.toArray(new QuoteRequestObject.SubLineData[larrSubLines.size()]);
		}

		public void UpdateInvariants(QuoteRequest pobjSource)
			throws BigBangException, CorruptedPadException
		{
			String[] larrAux;
			PadSubLine lobjSubLine;
			int i, j, k;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			midClient = ( pobjSource.clientId == null ? null : UUID.fromString(pobjSource.clientId) );
			mobjQuoteRequest.mstrNumber = pobjSource.processNumber;
			mobjQuoteRequest.midMediator = ( pobjSource.mediatorId == null ? null : UUID.fromString(pobjSource.mediatorId) );
			mobjQuoteRequest.mstrNotes = pobjSource.notes;
			mobjQuoteRequest.mbCaseStudy = pobjSource.caseStudy;

			mobjQuoteRequest.mbModified = true;

			mbValid = false;

			if ( pobjSource.requestData != null )
			{
				for ( i = 0; i < pobjSource.requestData.length; i++ )
				{
					larrAux = pobjSource.requestData[i].qrslId.split(":");
					if ( larrAux.length != 2 )
			            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " +
			            		pobjSource.requestData[i].qrslId);
					if ( !mid.equals(UUID.fromString(larrAux[0])) )
			            throw new IllegalArgumentException("Unexpected: Temporary ID string does not match pad ID.");
					lobjSubLine = marrSubLines.get(Integer.parseInt(larrAux[1]));

					k = -1;
					if ( pobjSource.requestData[i].coverages != null )
					{
						for ( j = 0; j < pobjSource.requestData[i].coverages.length; j++ )
						{
							k = lobjSubLine.FindCoverage(UUID.fromString(pobjSource.requestData[i].coverages[j].coverageId), k + 1);
							if ( k < 0 )
								throw new BigBangException("Inesperado: Cobertura não existente do lado do servidor.");
							lobjSubLine.marrCoverages.get(k).mbPresent = pobjSource.requestData[i].coverages[j].presentInRequestSubLine;
						}
					}

					k = -1;
					if ( pobjSource.requestData[i].headerFields != null )
					{
						for ( j = 0; j < pobjSource.requestData[i].headerFields.length; j++ )
						{
							k = lobjSubLine.FindValue(UUID.fromString(pobjSource.requestData[i].headerFields[j].fieldId), -1, k + 1);
							if ( k < 0 )
								throw new BigBangException("Inesperado: Valor de cabeçalho não existente do lado do servidor.");
							lobjSubLine.marrValues.get(k).mstrValue = ( "".equals(pobjSource.requestData[i].headerFields[j].value) ?
									null : pobjSource.requestData[i].headerFields[j].value );
						}
					}
					if ( pobjSource.requestData[i].extraData != null )
					{
						for ( j = 0; j < pobjSource.requestData[i].extraData.length; j++ )
						{
							k = lobjSubLine.FindValue(UUID.fromString(pobjSource.requestData[i].extraData[j].fieldId), -1, k + 1);
							if ( k < 0 )
								throw new BigBangException("Inesperado: Valor de cabeçalho não existente do lado do servidor.");
							lobjSubLine.marrValues.get(k).mstrValue = ( "".equals(pobjSource.requestData[i].extraData[j].value) ?
									null : pobjSource.requestData[i].extraData[j].value );
						}
					}
				}
			}

			mbValid = true;
		}

		public void UpdatePage(QuoteRequest.TableSection pobjSource, int plngSubLine, int plngObject)
			throws BigBangException, CorruptedPadException
		{
			PadSubLine lobjSubLine;
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( pobjSource.data == null )
				return;

			lobjSubLine = marrSubLines.get(plngSubLine);

			mbValid = false;

			j = -1;
			for ( i = 0; i < pobjSource.data.length; i++ )
			{
				j = lobjSubLine.FindValue(UUID.fromString(pobjSource.data[i].fieldId), plngObject, j + 1);
				if ( j < 0 )
					throw new BigBangException("Inesperado: Valor de tabela não existente do lado do servidor.");
				lobjSubLine.marrValues.get(j).mstrValue = ( "".equals(pobjSource.data[i].value) ? null : pobjSource.data[i].value );
			}

			mbValid = true;
		}

		public int CreateNewSubLine(UUID pidSubLine)
			throws BigBangException, CorruptedPadException
		{
			PadSubLine lobjSubLine;
			SubLine lobjSource;
			com.premiumminds.BigBang.Jewel.Objects.Coverage[] larrAuxCoverages;
			PadCoverage lobjCoverage;
			Tax[] larrTaxes;
			ArrayList<PadField> larrFields;
			PadField lobjField;
			PadValue lobjValue;
			int llngIndex;
			int i, j, k;

			for ( i = 0; i < marrSubLines.size(); i++ )
				if ( marrSubLines.get(i).midSubLine.equals(pidSubLine) && !marrSubLines.get(i).mbDeleted )
					throw new BigBangException("Não pode acrescentar uma cobertura já existente.");

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjSubLine = new PadSubLine();
			lobjSubLine.mid = null;
			lobjSubLine.midQuoteRequest = mobjQuoteRequest.mid;
			lobjSubLine.midSubLine = pidSubLine;
			lobjSubLine.marrCoverages = new ArrayList<PadCoverage>();
			lobjSubLine.marrValues = new ArrayList<PadValue>();

			try
			{
				lobjSource = SubLine.GetInstance(Engine.getCurrentNameSpace(), pidSubLine);
				lobjSubLine.mobjSubLine = lobjSource;
				lobjSubLine.mstrLabel = lobjSource.getLine().getCategory().getLabel() + " / " + lobjSource.getLine().getLabel() +
						" / " + lobjSource.getLabel();
				lobjSubLine.midObjectType = lobjSource.getObjectType();

				larrAuxCoverages = lobjSource.GetCurrentCoverages();

				for ( i = 0 ; i < larrAuxCoverages.length; i++ )
				{
					lobjCoverage = new PadCoverage();
					lobjCoverage.mid = null;
					lobjCoverage.midQRSubLine = null;
					lobjCoverage.midCoverage = larrAuxCoverages[i].getKey();
					lobjCoverage.mstrLabel = larrAuxCoverages[i].getLabel();
					lobjCoverage.mbIsHeader = larrAuxCoverages[i].IsHeader();
					lobjCoverage.mbMandatory = larrAuxCoverages[i].IsMandatory();
					lobjCoverage.mlngOrder = larrAuxCoverages[i].GetOrder();
					if ( lobjCoverage.mbMandatory )
						lobjCoverage.mbPresent = true;
					else
						lobjCoverage.mbPresent = null;
					larrTaxes = larrAuxCoverages[i].GetCurrentTaxes();
					larrFields = new ArrayList<PadField>();
					for ( j = 0; j < larrTaxes.length; j++ )
					{
						lobjField = new PadField();
						lobjField.midField = larrTaxes[j].getKey();
						lobjField.mlngColIndex = larrTaxes[j].GetColumnOrder();
						lobjField.mstrLabel = larrTaxes[j].getLabel();
						lobjField.midType = (UUID)larrTaxes[j].getAt(2);
						lobjField.mstrUnits = (String)larrTaxes[j].getAt(3);
						lobjField.mstrDefault = (String)larrTaxes[j].getAt(4);
						lobjField.midRefersTo = (UUID)larrTaxes[j].getAt(7);
						lobjField.mbVariesByObject = larrTaxes[j].GetVariesByObject();
						larrFields.add(lobjField);

						if ( lobjField.mbVariesByObject )
						{
							for ( k = 0; k < marrObjects.size(); k++ )
							{
								if ( marrObjects.get(k).mbDeleted )
									continue;

								if ( !marrObjects.get(k).midType.equals(lobjSubLine.midObjectType) )
									continue;

								lobjValue = new PadValue();
								lobjValue.mid = null;
								lobjValue.mstrValue = (String)larrTaxes[j].getAt(4);
								lobjValue.midQRSubLine = null;
								lobjValue.midField = larrTaxes[j].getKey();
								lobjValue.midObject = null;
								lobjValue.mrefCoverage = lobjCoverage;
								lobjValue.mrefField = lobjField;
								lobjValue.mlngObject = k;
								lobjValue.mbDeleted = false;
								lobjSubLine.marrValues.add(lobjValue);
							}
						}
						else
						{
							lobjValue = new PadValue();
							lobjValue.mid = null;
							lobjValue.mstrValue = (String)larrTaxes[j].getAt(4);
							lobjValue.midQRSubLine = null;
							lobjValue.midField = larrTaxes[j].getKey();
							lobjValue.midObject = null;
							lobjValue.mrefCoverage = lobjCoverage;
							lobjValue.mrefField = lobjField;
							lobjValue.mlngObject = -1;
							lobjValue.mbDeleted = false;
							lobjSubLine.marrValues.add(lobjValue);
						}
					}
					lobjCoverage.marrFields = larrFields.toArray(new PadField[larrFields.size()]);
					lobjSubLine.marrCoverages.add(lobjCoverage);
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			llngIndex = marrSubLines.size();
			marrSubLines.add(lobjSubLine);

			return llngIndex;
		}

		public void DeleteSubLine(int plngSubLine)
			throws BigBangException, CorruptedPadException
		{
			int i;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			mbValid = false;

			marrSubLines.get(plngSubLine).mbDeleted = true;

			for ( i = 0; i < marrSubLines.get(plngSubLine).marrCoverages.size(); i++ )
				marrSubLines.get(plngSubLine).marrCoverages.get(i).mbDeleted = true;

			for ( i = 0; i < marrSubLines.get(plngSubLine).marrValues.size(); i++ )
				marrSubLines.get(plngSubLine).marrValues.get(i).mbDeleted = true;

			mbValid = true;
		}

		public int CreateNewObject(UUID pidType)
			throws BigBangException, CorruptedPadException
		{
			PadObject lobjObject;
			PadValue lobjValue;
			int llngIndex;
			int i, j, k;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjObject = new PadObject();
			lobjObject.midType = pidType;

			mbValid = false;

			llngIndex = marrObjects.size();
			marrObjects.add(lobjObject);

			for ( i = 0; i < marrSubLines.size(); i++ )
			{
				if ( marrSubLines.get(i).mbDeleted )
					continue;

				if ( !lobjObject.midType.equals(marrSubLines.get(i).midObjectType) )
					continue;

				for ( j = 0; j < marrSubLines.get(i).marrCoverages.size(); j++ )
				{
					for ( k = 0; k < marrSubLines.get(i).marrCoverages.get(j).marrFields.length; k++ )
					{
						if ( marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mbVariesByObject )
						{
							lobjValue = new PadValue();
							lobjValue.mid = null;
							lobjValue.mstrValue = marrSubLines.get(i).marrCoverages.get(j).marrFields[k].mstrDefault;
							lobjValue.midQRSubLine = null;
							lobjValue.midField = marrSubLines.get(i).marrCoverages.get(j).marrFields[k].midField;
							lobjValue.midObject = null;
							lobjValue.mrefCoverage = marrSubLines.get(i).marrCoverages.get(j);
							lobjValue.mrefField = marrSubLines.get(i).marrCoverages.get(j).marrFields[k];
							lobjValue.mlngObject = llngIndex;
							marrSubLines.get(i).marrValues.add(lobjValue);
						}
					}
				}
			}

			mbValid = true;

			return llngIndex;
		}

		public void UpdateObject(QuoteRequestObject pobjSource, int plngObject)
			throws BigBangException, CorruptedPadException
		{
			UUID lidZipCode;
			PadObject lobjObject;
			PadSubLine lobjSubLine;
			int i, j, k, l;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( (marrObjects.get(plngObject) == null) || marrObjects.get(plngObject).mbDeleted )
				throw new BigBangException("Erro: Não pode alterar um objecto apagado.");

			try
			{
				if ( (pobjSource.address != null) && (pobjSource.address.zipCode != null) )
					lidZipCode = ZipCodeBridge.GetZipCode(pobjSource.address.zipCode.code, pobjSource.address.zipCode.city,
							pobjSource.address.zipCode.county, pobjSource.address.zipCode.district,
							pobjSource.address.zipCode.country);
				else
					lidZipCode = null;
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjObject = marrObjects.get(plngObject);

			lobjObject.mstrName = pobjSource.unitIdentification;
			if ( pobjSource.address != null )
			{
				lobjObject.mstrAddress1 = pobjSource.address.street1;
				lobjObject.mstrAddress2 = pobjSource.address.street2;
				if ( pobjSource.address.zipCode != null )
					lobjObject.midZipCode = lidZipCode;
				else
					lobjObject.midZipCode = null;
			}
			else
			{
				lobjObject.mstrAddress1 = null;
				lobjObject.mstrAddress2 = null;
				lobjObject.midZipCode = null;
			}

			lobjObject.mstrFiscalI = pobjSource.taxNumberPerson;
			lobjObject.midSex = ( pobjSource.genderId == null ? null : UUID.fromString(pobjSource.genderId) );
			lobjObject.mdtDateOfBirth = ( pobjSource.birthDate == null ? null :
					Timestamp.valueOf(pobjSource.birthDate + " 00:00:00.0") );
			lobjObject.mlngClientNumberI = ( pobjSource.clientNumberPerson == null ? null :
					Integer.decode(pobjSource.clientNumberPerson) );
			lobjObject.mstrInsurerIDI = pobjSource.insuranceCompanyInternalIdPerson;

			lobjObject.mstrFiscalC = pobjSource.taxNumberCompany;
			lobjObject.midPredomCAE = ( pobjSource.caeId == null ? null : UUID.fromString(pobjSource.caeId) );
			lobjObject.midGrievousCAE = ( pobjSource.grievousCaeId == null ? null : UUID.fromString(pobjSource.grievousCaeId) );
			lobjObject.mstrActivityNotes = pobjSource.activityNotes;
			lobjObject.mstrProductNotes = pobjSource.productNotes;
			lobjObject.midSales = ( pobjSource.businessVolumeId == null ? null : UUID.fromString(pobjSource.businessVolumeId) );
			lobjObject.mstrEUEntity = pobjSource.europeanUnionEntity;
			lobjObject.mlngClientNumberC = ( pobjSource.clientNumberGroup == null ? null :
					Integer.decode(pobjSource.clientNumberGroup) );

			lobjObject.mstrMakeAndModel = pobjSource.makeAndModel;
			lobjObject.mstrEquipmentNotes = pobjSource.equipmentDescription;
			lobjObject.mdtFirstRegistry = ( pobjSource.firstRegistryDate == null ? null :
					Timestamp.valueOf(pobjSource.firstRegistryDate + " 00:00:00.0") );
			lobjObject.mlngManufactureYear = ( pobjSource.manufactureYear == null ? null :
					Integer.decode(pobjSource.manufactureYear) );
			lobjObject.mstrClientIDE = pobjSource.clientInternalId;
			lobjObject.mstrInsurerIDE = pobjSource.insuranceCompanyInternalIdVehicle;

			lobjObject.mstrSiteNotes = pobjSource.siteDescription;

			lobjObject.mstrSpecies = pobjSource.species;
			lobjObject.mstrRace = pobjSource.race;
			lobjObject.mlngAge = ( pobjSource.birthYear == null ? null :
					Integer.decode(pobjSource.birthYear) );
			lobjObject.mstrCityNumber = pobjSource.cityRegistryNumber;
			lobjObject.mstrElectronicIDTag = pobjSource.electronicIdTag;

			mbValid = false;

			for ( i = 0; i < pobjSource.objectData.length; i++ )
			{
				lobjSubLine = marrSubLines.get(Integer.parseInt(pobjSource.objectData[i].subLineId.split(":")[1]));

				if ( !lobjObject.midType.equals(lobjSubLine.midObjectType) )
					continue;

				l = -1;
				if ( pobjSource.objectData[i].headerData != null )
				{
					for ( j = 0; j < pobjSource.objectData[i].headerData.fixedFields.length; j++ )
					{
						l = lobjSubLine.FindValue(UUID.fromString(pobjSource.objectData[i].headerData.fixedFields[j].fieldId),
								plngObject, l + 1);
						if ( l < 0 )
							throw new BigBangException("Inesperado: Valor fixo de objecto não existente do lado do servidor.");
						lobjSubLine.marrValues.get(l).mstrValue =
								( "".equals(pobjSource.objectData[i].headerData.fixedFields[j].value) ? null :
								pobjSource.objectData[i].headerData.fixedFields[j].value );
					}
				}

				if ( pobjSource.objectData[i].coverageData != null )
				{
					for ( j = 0; j < pobjSource.objectData[i].coverageData.length; j++ )
					{
						for ( k = 0; k < pobjSource.objectData[i].coverageData[j].fixedFields.length; k++ )
						{
							l = lobjSubLine.FindValue(UUID.fromString(pobjSource.objectData[i].coverageData[j].fixedFields[k].fieldId),
									plngObject, l + 1);
							if ( l < 0 )
								throw new BigBangException("Inesperado: Valor fixo de objecto não existente do lado do servidor.");
							lobjSubLine.marrValues.get(l).mstrValue =
									( "".equals(pobjSource.objectData[i].coverageData[j].fixedFields[k].value) ? null :
									pobjSource.objectData[i].coverageData[j].fixedFields[k].value );
						}
					}
				}
			}

			mbValid = true;
		}

		public void DeleteObject(int plngObject)
			throws CorruptedPadException
		{
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			mbValid = false;

			marrObjects.get(plngObject).mbDeleted = true;

			for ( i = 0; i < marrSubLines.size(); i++ )
			{
				for ( j = 0; j < marrSubLines.get(i).marrValues.size(); j++ )
				{
					if ( marrSubLines.get(i).marrValues.get(j).mlngObject == plngObject )
						marrSubLines.get(i).marrValues.get(j).mbDeleted = true;
				}
			}

			mbValid = true;
		}

		public void CommitChanges()
			throws BigBangException, CorruptedPadException 
		{
			QuoteRequestData lobjData;
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjData = new QuoteRequestData();
			lobjData.Clone(mobjQuoteRequest);

			lobjData.mbModified = mobjQuoteRequest.mbModified;

			if ( marrObjects.size() > 0 )
			{
				lobjData.marrObjects = new QuoteRequestObjectData[marrObjects.size()];
				for ( i = 0; i < marrObjects.size(); i++ )
				{
					lobjData.marrObjects[i] = new QuoteRequestObjectData();
					lobjData.marrObjects[i].Clone(marrObjects.get(i));
					if ( marrObjects.get(i).mbDeleted )
						lobjData.marrObjects[i].mbDeleted = true;
					else if ( marrObjects.get(i).mid == null )
						lobjData.marrObjects[i].mbNew = true;
				}
			}
			else
				lobjData.marrObjects = null;

			if ( marrSubLines.size() > 0 )
			{
				lobjData.marrSubLines = new QuoteRequestSubLineData[marrSubLines.size()];
				for ( i = 0; i < marrSubLines.size(); i++ )
				{
					lobjData.marrSubLines[i] = new QuoteRequestSubLineData();
					lobjData.marrSubLines[i].Clone(marrSubLines.get(i));
					if ( marrSubLines.get(i).mbDeleted )
						lobjData.marrSubLines[i].mbDeleted = true;
					else if ( marrSubLines.get(i).mid == null )
						lobjData.marrSubLines[i].mbNew = true;

					if ( marrSubLines.get(i).marrCoverages.size() > 0 )
					{
						lobjData.marrSubLines[i].marrCoverages = new QuoteRequestCoverageData[marrSubLines.get(i).marrCoverages.size()];
						for ( j = 0; j < marrSubLines.get(i).marrCoverages.size(); j++ )
						{
							lobjData.marrSubLines[i].marrCoverages[j] = new QuoteRequestCoverageData();
							lobjData.marrSubLines[i].marrCoverages[j].Clone(marrSubLines.get(i).marrCoverages.get(j));
							if ( marrSubLines.get(i).marrCoverages.get(j).mbDeleted || marrSubLines.get(i).mbDeleted )
								lobjData.marrSubLines[i].marrCoverages[j].mbDeleted = true;
							else if ( (marrSubLines.get(i).marrCoverages.get(j).mid == null) || (marrSubLines.get(i).mid == null) )
								lobjData.marrSubLines[i].marrCoverages[j].mbNew = true;
						}
					}
					else
						lobjData.marrSubLines[i].marrCoverages = null;

					if ( marrSubLines.get(i).marrValues.size() > 0 )
					{
						lobjData.marrSubLines[i].marrValues = new QuoteRequestValueData[marrSubLines.get(i).marrValues.size()];
						for ( j = 0; j < marrSubLines.get(i).marrValues.size(); j++ )
						{
							lobjData.marrSubLines[i].marrValues[j] = new QuoteRequestValueData();
							lobjData.marrSubLines[i].marrValues[j].Clone(marrSubLines.get(i).marrValues.get(j));
							if ( marrSubLines.get(i).marrValues.get(j).mbDeleted || marrSubLines.get(i).mbDeleted )
								lobjData.marrSubLines[i].marrValues[j].mbDeleted = true;
							else if ( (marrSubLines.get(i).marrValues.get(j).mid == null) || (marrSubLines.get(i).mid == null) )
								lobjData.marrSubLines[i].marrValues[j].mbNew = true;
						}
					}
					else
						lobjData.marrSubLines[i].marrCoverages = null;
				}
			}
			else
				lobjData.marrSubLines = null;

			if ( mobjQuoteRequest.mid == null )
				CommitNew(lobjData);
			else
				CommitEdit(lobjData);
		}

		private void CommitNew(QuoteRequestData pobjData)
			throws BigBangException
		{
			CreateQuoteRequest lopCQR;
			int i, j;

			if ( midClient == null )
				throw new BigBangException("Erro: Não preencheu o identificador do cliente.");

			try
			{
				lopCQR = new CreateQuoteRequest(Client.GetInstance(Engine.getCurrentNameSpace(), midClient).GetProcessID());
				lopCQR.mobjData = pobjData;

				lopCQR.mobjContactOps = null;
				lopCQR.mobjDocOps = null;

				lopCQR.Execute();

				mobjQuoteRequest.mid = lopCQR.mobjData.mid;

				if ( lopCQR.mobjData.marrObjects != null )
				{
					for ( i = 0; i < lopCQR.mobjData.marrObjects.length; i++ )
						if ( lopCQR.mobjData.marrObjects[i].mbNew )
							marrObjects.get(i).mid = lopCQR.mobjData.marrObjects[i].mid;
				}

				if ( lopCQR.mobjData.marrSubLines != null )
				{
					for ( i = 0; i < lopCQR.mobjData.marrSubLines.length; i++ )
					{
						if ( lopCQR.mobjData.marrSubLines[i].mbNew )
							marrSubLines.get(i).mid = lopCQR.mobjData.marrSubLines[i].mid;

						if ( lopCQR.mobjData.marrSubLines[i].marrCoverages != null )
						{
							for ( j = 0; j < lopCQR.mobjData.marrSubLines[i].marrCoverages.length; j++ )
								if ( lopCQR.mobjData.marrSubLines[i].marrCoverages[j].mbNew )
									marrSubLines.get(i).marrCoverages.get(j).mid = lopCQR.mobjData.marrSubLines[i].marrCoverages[j].mid;
						}

						if ( lopCQR.mobjData.marrSubLines[i].marrValues != null )
						{
							for ( j = 0; j < lopCQR.mobjData.marrSubLines[i].marrValues.length; j++ )
								if ( lopCQR.mobjData.marrSubLines[i].marrValues[j].mbNew )
									marrSubLines.get(i).marrValues.get(j).mid = lopCQR.mobjData.marrSubLines[i].marrValues[j].mid;
						}
					}
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}

		private void CommitEdit(QuoteRequestData pobjData)
			throws BigBangException
		{
			ManageData lopMD;
			int i, j;

			try
			{
				lopMD = new ManageData(mobjQuoteRequest.midProcess);
				lopMD.mobjData = pobjData;

				lopMD.mobjContactOps = null;
				lopMD.mobjDocOps = null;

				lopMD.Execute();

				if ( lopMD.mobjData.marrObjects != null )
				{
					for ( i = 0; i < lopMD.mobjData.marrObjects.length; i++ )
						if ( lopMD.mobjData.marrObjects[i].mbNew )
							marrObjects.get(i).mid = lopMD.mobjData.marrObjects[i].mid;
				}

				if ( lopMD.mobjData.marrSubLines != null )
				{
					for ( i = 0; i < lopMD.mobjData.marrSubLines.length; i++ )
					{
						if ( lopMD.mobjData.marrSubLines[i].mbNew )
							marrSubLines.get(i).mid = lopMD.mobjData.marrSubLines[i].mid;

						if ( lopMD.mobjData.marrSubLines[i].marrCoverages != null )
						{
							for ( j = 0; j < lopMD.mobjData.marrSubLines[i].marrCoverages.length; j++ )
								if ( lopMD.mobjData.marrSubLines[i].marrCoverages[j].mbNew )
									marrSubLines.get(i).marrCoverages.get(j).mid = lopMD.mobjData.marrSubLines[i].marrCoverages[j].mid;
						}

						if ( lopMD.mobjData.marrSubLines[i].marrValues != null )
						{
							for ( j = 0; j < lopMD.mobjData.marrSubLines[i].marrValues.length; j++ )
								if ( lopMD.mobjData.marrSubLines[i].marrValues[j].mbNew )
									marrSubLines.get(i).marrValues.get(j).mid = lopMD.mobjData.marrSubLines[i].marrValues[j].mid;
						}
					}
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Hashtable<UUID, QuoteRequestScratchPad> GetScratchPadStorage()
	{
		Hashtable<UUID, QuoteRequestScratchPad> larrAux;

        if (getSession() == null)
            return null;

        larrAux = (Hashtable<UUID, QuoteRequestScratchPad>)getSession().getAttribute("BigBang_QuoteRequest_ScratchPad_Storage");
        if (larrAux == null)
        {
        	larrAux = new Hashtable<UUID, QuoteRequestScratchPad>();
            getSession().setAttribute("BigBang_QuoteRequest_ScratchPad_Storage", larrAux);
        }

        return larrAux;
	}

	public static QuoteRequest sGetRequest(UUID pidRequest)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		IProcess lobjProc;
		Client lobjClient;
		Mediator lobjMed;
		QuoteRequestSubLine[] larrSubLines;
		QuoteRequest lobjResult;
		QuoteRequestValue[] larrLocalValues;
		QuoteRequestCoverage[] larrLocalCoverages;
		Coverage[] larrCoverages;
		HashMap<UUID, Tax> larrAuxFields;
		ArrayList<QuoteRequest.HeaderField> larrOutHeaders;
		ArrayList<QuoteRequest.TableSection.TableField> larrOutFields;
		ArrayList<QuoteRequest.ExtraField> larrOutExtras;
		Tax lobjTax;
		QuoteRequest.TableSection.TableField lobjField;
		QuoteRequest.HeaderField lobjHeader;
		QuoteRequest.ExtraField lobjExtra;
		HashMap<UUID, Coverage> larrAuxCoverages;
		ArrayList<QuoteRequest.Coverage> larrOutCoverages;
		HashMap<Integer, QuoteRequest.ColumnHeader> larrOutColumns;
		Coverage lobjCoverage;
		Tax[] larrTaxes;
		QuoteRequest.Coverage lobjAuxCoverage;
		ArrayList<QuoteRequest.Coverage.Variability> larrVariability;
		QuoteRequest.ColumnHeader lobjColumnHeader;
		QuoteRequest.Coverage.Variability lobjVariability;
		QuoteRequest.TableSection lobjSection;
		int i, j, k;

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), pidRequest);
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjRequest.GetProcessID());
			lobjClient = (Client)lobjProc.GetParent().GetData();
			lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjClient.getAt(8));
			larrSubLines = lobjRequest.GetCurrentSubLines();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new QuoteRequest();

		lobjResult.id = lobjRequest.getKey().toString();
		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.processNumber = (String)lobjRequest.getAt(0);
		lobjResult.isOpen = lobjProc.IsRunning();
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.caseStudy = (Boolean)lobjRequest.getAt(4);
		lobjResult.managerId = lobjProc.GetManagerID().toString();
		lobjResult.mediatorId = ((UUID)lobjRequest.getAt(2)).toString();
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.notes = (String)lobjRequest.getAt(3);
		lobjResult.docushare = (String)lobjRequest.getAt(5);

		lobjResult.requestData = new QuoteRequest.RequestSubLine[larrSubLines.length];
		for ( i = 0; i < larrSubLines.length; i++ )
		{
			lobjResult.requestData[i] = new QuoteRequest.RequestSubLine();
			lobjResult.requestData[i].qrslId = larrSubLines[i].getKey().toString();
			lobjResult.requestData[i].categoryId = larrSubLines[i].GetSubLine().getLine().getCategory().getKey().toString();
			lobjResult.requestData[i].lineId = larrSubLines[i].GetSubLine().getLine().getKey().toString();
			lobjResult.requestData[i].sublineId = larrSubLines[i].GetSubLine().getKey().toString();
			lobjResult.requestData[i].headerText = larrSubLines[i].GetSubLine().getLine().getCategory().getLabel() + " / " +
					larrSubLines[i].GetSubLine().getLine().getLabel() + " / " + larrSubLines[i].GetSubLine().getLabel();
			try
			{
				larrLocalValues = larrSubLines[i].GetCurrentKeyedValues(null);
				larrLocalCoverages = larrSubLines[i].GetCurrentCoverages();
				larrCoverages = larrSubLines[i].GetSubLine().GetCurrentCoverages();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			larrAuxFields = new HashMap<UUID, Tax>();
			larrOutHeaders = new ArrayList<QuoteRequest.HeaderField>();
			larrOutFields = new ArrayList<QuoteRequest.TableSection.TableField>();
			larrOutExtras = new ArrayList<QuoteRequest.ExtraField>();
			for ( j = 0; j < larrLocalValues.length; j++ )
			{
				if ( larrLocalValues[j].getAt(3) != null )
					continue;
				lobjTax = larrLocalValues[j].GetTax();
//				if ( lobjTax.GetVariesByObject() )
//					throw new BigBangException("Inesperado: Valor variável marcado como invariante.");

				if ( lobjTax.GetCoverage().IsHeader() )
				{
					lobjHeader = new QuoteRequest.HeaderField();
					lobjHeader.fieldId = lobjTax.getKey().toString();
					lobjHeader.fieldName = lobjTax.getLabel();
					lobjHeader.type = ServerToClient.sGetFieldTypeByID((UUID)lobjTax.getAt(2));
					lobjHeader.unitsLabel = (String)lobjTax.getAt(3);
					lobjHeader.refersToId = ( lobjTax.getAt(7) == null ? null : ((UUID)lobjTax.getAt(7)).toString() );
					lobjHeader.value = larrLocalValues[j].getLabel();
					lobjHeader.order = (Integer)lobjTax.getAt(8);
					larrOutHeaders.add(lobjHeader);
				}
				else if ( lobjTax.GetColumnOrder() >= 0 )
				{
					lobjField = new QuoteRequest.TableSection.TableField();
					lobjField.fieldId = lobjTax.getKey().toString();
					lobjField.coverageId = lobjTax.GetCoverage().getKey().toString();
					lobjField.columnIndex = lobjTax.GetColumnOrder();
					lobjField.value = larrLocalValues[j].getLabel();
					larrOutFields.add(lobjField);
				}
				else
				{
					lobjExtra = new QuoteRequest.ExtraField();
					lobjExtra.fieldId = lobjTax.getKey().toString();
					lobjExtra.fieldName = lobjTax.getLabel();
					lobjExtra.type = ServerToClient.sGetFieldTypeByID((UUID)lobjTax.getAt(2));
					lobjExtra.unitsLabel = (String)lobjTax.getAt(3);
					lobjExtra.refersToId = ( lobjTax.getAt(7) == null ? null : ((UUID)lobjTax.getAt(7)).toString() );
					lobjExtra.coverageId = lobjTax.GetCoverage().getKey().toString();
					lobjExtra.coverageName = lobjTax.GetCoverage().getLabel();
					lobjExtra.mandatory = lobjTax.GetCoverage().IsMandatory();
					lobjExtra.covorder = lobjTax.GetCoverage().GetOrder();
					lobjExtra.value = larrLocalValues[j].getLabel();
					lobjExtra.order = (Integer)lobjTax.getAt(8);
					larrOutExtras.add(lobjExtra);
				}
				larrAuxFields.put(lobjTax.getKey(), lobjTax);
			}

			larrAuxCoverages = new HashMap<UUID, Coverage>();
			larrOutCoverages = new ArrayList<QuoteRequest.Coverage>();
			larrOutColumns = new HashMap<Integer, QuoteRequest.ColumnHeader>();
			for ( j = 0; j < larrLocalCoverages.length; j++ )
			{
				lobjCoverage = larrLocalCoverages[j].GetCoverage();
				if ( lobjCoverage.IsHeader() )
					continue;

				try
				{
					larrTaxes = lobjCoverage.GetCurrentTaxes();
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}

				lobjAuxCoverage = new QuoteRequest.Coverage();
				lobjAuxCoverage.coverageId = lobjCoverage.getKey().toString();
				lobjAuxCoverage.coverageName = lobjCoverage.getLabel();
				lobjAuxCoverage.mandatory = (Boolean)lobjCoverage.getAt(2);
				lobjAuxCoverage.order = ( lobjCoverage.getAt(5) == null ? 0 : (Integer)lobjCoverage.getAt(5) );
				lobjAuxCoverage.presentInRequestSubLine = (Boolean)larrLocalCoverages[j].getAt(2);
				larrVariability = new ArrayList<QuoteRequest.Coverage.Variability>();
				for ( k = 0; k < larrTaxes.length ; k++ )
				{
					if ( larrTaxes[k].GetColumnOrder() < 0 )
						continue;

					if ( larrOutColumns.get(larrTaxes[k].GetColumnOrder()) == null )
					{
						lobjColumnHeader = new QuoteRequest.ColumnHeader();
						lobjColumnHeader.label = larrTaxes[k].getLabel();
						lobjColumnHeader.type = ServerToClient.sGetFieldTypeByID((UUID)larrTaxes[k].getAt(2));
						lobjColumnHeader.unitsLabel = (String)larrTaxes[k].getAt(3);
						lobjColumnHeader.refersToId = ( larrTaxes[k].getAt(7) == null ? null :
								((UUID)larrTaxes[k].getAt(7)).toString() );
						larrOutColumns.put(larrTaxes[k].GetColumnOrder(), lobjColumnHeader);
					}
					lobjVariability = new QuoteRequest.Coverage.Variability();
					lobjVariability.columnIndex = larrTaxes[k].GetColumnOrder();
					lobjVariability.variesByObject = larrTaxes[k].GetVariesByObject();
					lobjVariability.variesByExercise = larrTaxes[k].GetVariesByExercise();
					larrVariability.add(lobjVariability);
				}

				lobjAuxCoverage.variability = larrVariability.toArray(new QuoteRequest.Coverage.Variability[larrVariability.size()]);
				larrOutCoverages.add(lobjAuxCoverage);
				larrAuxCoverages.put(lobjCoverage.getKey(), lobjCoverage);
			}

			for ( j = 0; j < larrCoverages.length; j++ )
			{
				try
				{
					larrTaxes = larrCoverages[j].GetCurrentTaxes();
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}

				for ( k = 0; k < larrTaxes.length; k++ )
				{
					if ( !larrCoverages[j].IsHeader() && larrTaxes[k].GetColumnOrder() >= 0 &&
							(larrOutColumns.get(larrTaxes[k].GetColumnOrder()) != null) )
					{
						lobjColumnHeader = new QuoteRequest.ColumnHeader();
						lobjColumnHeader.label = larrTaxes[k].getLabel();
						lobjColumnHeader.type = ServerToClient.sGetFieldTypeByID((UUID)larrTaxes[k].getAt(2));
						lobjColumnHeader.unitsLabel = (String)larrTaxes[k].getAt(3);
						lobjColumnHeader.refersToId = ( larrTaxes[k].getAt(7) == null ? null :
								((UUID)larrTaxes[k].getAt(7)).toString() );
						larrOutColumns.put(larrTaxes[k].GetColumnOrder(), lobjColumnHeader);
					}

					if ( larrAuxFields.get(larrTaxes[k].getKey()) != null )
						continue;

					if ( larrTaxes[k].GetVariesByObject() || larrTaxes[k].GetVariesByExercise() )
						continue;

					if ( larrCoverages[j].IsHeader() )
					{
						lobjHeader = new QuoteRequest.HeaderField();
						lobjHeader.fieldId = larrTaxes[k].getKey().toString();
						lobjHeader.fieldName = larrTaxes[k].getLabel();
						lobjHeader.type = ServerToClient.sGetFieldTypeByID((UUID)larrTaxes[k].getAt(2));
						lobjHeader.unitsLabel = (String)larrTaxes[k].getAt(3);
						lobjHeader.refersToId = ( larrTaxes[k].getAt(7) == null ? null :
								((UUID)larrTaxes[k].getAt(7)).toString() );
						lobjHeader.value = (String)larrTaxes[k].getAt(4);
						lobjHeader.order = (Integer)larrTaxes[k].getAt(8);
						larrOutHeaders.add(lobjHeader);
					}
					else if ( larrTaxes[k].GetColumnOrder() >= 0 )
					{
						lobjField = new QuoteRequest.TableSection.TableField();
						lobjField.fieldId = larrTaxes[k].getKey().toString();
						lobjField.coverageId = larrTaxes[k].GetCoverage().getKey().toString();
						lobjField.columnIndex = larrTaxes[k].GetColumnOrder();
						lobjField.value = (String)larrTaxes[k].getAt(4);
						larrOutFields.add(lobjField);
					}
					else
					{
						lobjExtra = new QuoteRequest.ExtraField();
						lobjExtra.fieldId = larrTaxes[k].getKey().toString();
						lobjExtra.fieldName = larrTaxes[k].getLabel();
						lobjExtra.type = ServerToClient.sGetFieldTypeByID((UUID)larrTaxes[k].getAt(2));
						lobjExtra.unitsLabel = (String)larrTaxes[k].getAt(3);
						lobjExtra.refersToId = ( larrTaxes[k].getAt(7) == null ? null : ((UUID)larrTaxes[k].getAt(7)).toString() );
						lobjExtra.coverageId = larrTaxes[k].GetCoverage().getKey().toString();
						lobjExtra.coverageName = larrTaxes[k].GetCoverage().getLabel();
						lobjExtra.mandatory = larrTaxes[k].GetCoverage().IsMandatory();
						lobjExtra.covorder = larrTaxes[k].GetCoverage().GetOrder();
						lobjExtra.value = (String)larrTaxes[k].getAt(4);
						lobjExtra.order = (Integer)larrTaxes[k].getAt(8);
						larrOutExtras.add(lobjExtra);
					}
					larrAuxFields.put(larrTaxes[k].getKey(), larrTaxes[k]);
				}

				if ( larrAuxCoverages.get(larrCoverages[j].getKey()) != null )
					continue;

				if ( larrCoverages[j].IsHeader() )
					continue;

				lobjAuxCoverage = new QuoteRequest.Coverage();
				lobjAuxCoverage.coverageId = larrCoverages[j].getKey().toString();
				lobjAuxCoverage.coverageName = larrCoverages[j].getLabel();
				lobjAuxCoverage.mandatory = (Boolean)larrCoverages[j].getAt(2);
				lobjAuxCoverage.presentInRequestSubLine = null;
				larrVariability = new ArrayList<QuoteRequest.Coverage.Variability>();
				for ( k = 0; k < larrTaxes.length ; k++ )
				{
					if ( larrTaxes[k].GetColumnOrder() < 0 )
						continue;

					lobjVariability = new QuoteRequest.Coverage.Variability();
					lobjVariability.columnIndex = larrTaxes[k].GetColumnOrder();
					lobjVariability.variesByObject = larrTaxes[k].GetVariesByObject();
					lobjVariability.variesByExercise = larrTaxes[k].GetVariesByExercise();
					larrVariability.add(lobjVariability);

				}
				lobjAuxCoverage.variability = larrVariability.toArray(new QuoteRequest.Coverage.Variability[larrVariability.size()]);
				larrOutCoverages.add(lobjAuxCoverage);
				larrAuxCoverages.put(larrCoverages[j].getKey(), larrCoverages[j]);
			}

			lobjSection = new QuoteRequest.TableSection();
			lobjSection.pageId = null;
			lobjSection.data = larrOutFields.toArray(new QuoteRequest.TableSection.TableField[larrOutFields.size()]);

			lobjResult.requestData[i].headerFields = larrOutHeaders.toArray(new QuoteRequest.HeaderField[larrOutHeaders.size()]);
			lobjResult.requestData[i].coverages = larrOutCoverages.toArray(new QuoteRequest.Coverage[larrOutCoverages.size()]);
			lobjResult.requestData[i].columns = new QuoteRequest.ColumnHeader[larrOutColumns.size()];
			for ( Integer ii: larrOutColumns.keySet() )
				lobjResult.requestData[i].columns[ii] = larrOutColumns.get(ii);
			lobjResult.requestData[i].tableData = new QuoteRequest.TableSection[] { lobjSection };
			lobjResult.requestData[i].extraData = larrOutExtras.toArray(new QuoteRequest.ExtraField[larrOutExtras.size()]);

			java.util.Arrays.sort(lobjResult.requestData[i].headerFields, new Comparator<QuoteRequest.HeaderField>()
			{
				public int compare(QuoteRequest.HeaderField o1, QuoteRequest.HeaderField o2)
				{
					if ( o1.order == o2.order )
					{
						if ( o1.type == o2.type )
						{
							if ( o1.refersToId == o1.refersToId )
								return o1.fieldName.compareTo(o2.fieldName);
							return o1.refersToId.compareTo(o2.refersToId);
						}
						return o1.type.compareTo(o2.type);
					}
					if ( (o1.order < 0) || (o2.order < 0) )
						return o2.order - o1.order;
					return o1.order - o2.order;
				}
			});
			java.util.Arrays.sort(lobjResult.requestData[i].coverages, new Comparator<QuoteRequest.Coverage>()
			{
				public int compare(QuoteRequest.Coverage o1, QuoteRequest.Coverage o2)
				{
					if ( o1.mandatory == o2.mandatory )
					{
						if ( o1.order == o2.order )
							return o1.coverageName.compareTo(o2.coverageName);
						return o1.order - o2.order;
					}
					if ( o1.mandatory )
						return -1;
					return 1;
				}
			});
			java.util.Arrays.sort(lobjResult.requestData[i].extraData, new Comparator<QuoteRequest.ExtraField>()
			{
				public int compare(QuoteRequest.ExtraField o1, QuoteRequest.ExtraField o2)
				{
					if ( o1.coverageId.equals(o2.coverageId) )
					{
						if ( o1.order == o2.order )
						{
							if ( o1.type == o2.type )
							{
								if ( o1.refersToId == o1.refersToId )
									return o1.fieldName.compareTo(o2.fieldName);
								return o1.refersToId.compareTo(o2.refersToId);
							}
							return o1.type.compareTo(o2.type);
						}
						if ( (o1.order < 0) || (o2.order < 0) )
							return o2.order - o1.order;
						return o1.order - o2.order;
					}
					if ( o1.mandatory == o2.mandatory )
					{
						if ( o1.covorder == o2.covorder )
							return o1.coverageName.compareTo(o2.coverageName);
						return o1.covorder - o2.covorder;
					}
					if ( o1.mandatory )
						return -1;
					return 1;
				}
			});

		}

		java.util.Arrays.sort(lobjResult.requestData, new Comparator<QuoteRequest.RequestSubLine>()
		{
			public int compare(QuoteRequest.RequestSubLine o1, QuoteRequest.RequestSubLine o2)
			{
				return o1.headerText.compareTo(o2.headerText);
			}
		});

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

		return lobjResult;
	}

	public QuoteRequest getRequest(String requestId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetRequest(UUID.fromString(requestId));
	}

	@Override
	public QuoteRequest.TableSection getPage(String requestId, String subLineId, String objectId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidObject;
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		QuoteRequestSubLine lobjSubLine;
		Coverage[] larrCoverages;
		QuoteRequestValue[] larrValues;
		QuoteRequest.TableSection lobjResult;
		HashMap<UUID, Tax> larrAuxFields;
		ArrayList<QuoteRequest.TableSection.TableField> larrFields;
		Tax lobjTax;
		QuoteRequest.TableSection.TableField lobjField;
		Tax[] larrTaxes;
		int i, j;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidObject = (objectId == null ? null : UUID.fromString(objectId));

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(requestId));
			lobjSubLine = lobjRequest.GetSubLineByID(UUID.fromString(subLineId));
			larrCoverages = lobjSubLine.GetSubLine().GetCurrentCoverages();

			larrValues = lobjSubLine.GetCurrentKeyedValues(lidObject);
			larrAuxFields = new HashMap<UUID, Tax>();
			larrFields = new ArrayList<QuoteRequest.TableSection.TableField>();
			for ( i = 0; i < larrValues.length; i++ )
			{
				lobjTax = larrValues[i].GetTax();
//				if ( lobjTax.GetVariesByObject() || lobjTax.GetVariesByExercise() )
//					throw new BigBangException("Inesperado: Valor variável marcado como invariante.");

				if ( lobjTax.GetCoverage().IsHeader() || (lobjTax.GetColumnOrder() < 0) )
					continue;

				lobjField = new QuoteRequest.TableSection.TableField();
				lobjField.fieldId = lobjTax.getKey().toString();
				lobjField.coverageId = lobjTax.GetCoverage().getKey().toString();
				lobjField.columnIndex = lobjTax.GetColumnOrder();
				lobjField.value = larrValues[i].getLabel();
				larrFields.add(lobjField);
				larrAuxFields.put(lobjTax.getKey(), lobjTax);
			}

			for ( i = 0; i < larrCoverages.length; i++ )
			{
				if ( larrCoverages[i].IsHeader() )
					continue;

				larrTaxes = larrCoverages[i].GetCurrentTaxes();
				for ( j = 0; j < larrTaxes.length; j++ )
				{
					if ( larrAuxFields.get(larrTaxes[j].getKey()) != null )
						continue;

					if ( larrTaxes[j].GetVariesByObject() == (lidObject == null) )
						continue;

					lobjField = new QuoteRequest.TableSection.TableField();
					lobjField.fieldId = larrTaxes[j].getKey().toString();
					lobjField.coverageId = larrTaxes[j].GetCoverage().getKey().toString();
					lobjField.columnIndex = larrTaxes[j].GetColumnOrder();
					lobjField.value = (String)larrTaxes[j].getAt(4);
					larrFields.add(lobjField);
					larrAuxFields.put(larrTaxes[j].getKey(), larrTaxes[j]);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new QuoteRequest.TableSection();
		lobjResult.pageId = null;
		lobjResult.data = larrFields.toArray(new QuoteRequest.TableSection.TableField[larrFields.size()]);
		return lobjResult;
	}

	public Remap[] openRequestScratchPad(String requestId)
		throws SessionExpiredException, BigBangException
	{
		QuoteRequestScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjPad = new QuoteRequestScratchPad();
		if ( requestId != null )
			lobjPad.OpenForEdit(UUID.fromString(requestId));
		GetScratchPadStorage().put(lobjPad.GetID(), lobjPad);

		return lobjPad.GetRemapIntoPad();
	}

	public QuoteRequest initRequestInPad(QuoteRequest request)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		QuoteRequestScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( request.id == null )
			throw new BigBangException("Erro: Não pode inicializar uma apólice sem abrir o espaço de trabalho.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(request.id));
		lobjPad.InitNew(request);

		request = lobjPad.WriteBasics();
		lobjPad.WriteResult(request);
		return request;
	}

	public QuoteRequest getRequestInPad(String requestId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		QuoteRequestScratchPad lobjPad;
		QuoteRequest lobjRequest;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjPad = GetScratchPadStorage().get(UUID.fromString(requestId));
		lobjRequest = lobjPad.WriteBasics();
		lobjPad.WriteResult(lobjRequest);
		return lobjRequest;
	}

	public QuoteRequest updateHeader(QuoteRequest request)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		QuoteRequestScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( request.id == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(request.id));
		lobjPad.UpdateInvariants(request);

		lobjPad.WriteResult(request);
		return request;
	}

	public QuoteRequest.RequestSubLine addSubLineToPad(String requestId, String subLineId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		QuoteRequestScratchPad lobjPad;
		QuoteRequest.RequestSubLine lobjResult;
		int llngSubLine;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( requestId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");
		if ( subLineId == null )
			throw new BigBangException("Erro: Tem que indicar a modalidade a acrescentar.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(requestId));
		llngSubLine = lobjPad.CreateNewSubLine(UUID.fromString(subLineId));

		lobjResult = new QuoteRequest.RequestSubLine();
		lobjPad.WriteSubLine(lobjResult, llngSubLine);
		return lobjResult;
	}

	public void deleteSubLineFromPad(String subLineId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngSubLine;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = subLineId.split(":");
		if ( larrAux.length != 2 )
            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + subLineId);
		lidPad = UUID.fromString(larrAux[0]);
		llngSubLine = Integer.parseInt(larrAux[1]);

		GetScratchPadStorage().get(lidPad).DeleteSubLine(llngSubLine);
	}

	public QuoteRequest.TableSection getPageForEdit(String requestId, String subLineId, String objectId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngSubLine;
		int llngObject;
		QuoteRequestScratchPad lobjPad;
		QuoteRequest.TableSection lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( requestId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");
		lidPad = UUID.fromString(requestId);

		if ( (subLineId == null) || (subLineId.length() == 0) )
			llngSubLine = -1;
		else
		{
			larrAux = subLineId.split(":");
			if ( larrAux.length != 2 )
	            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + subLineId);
			if ( !lidPad.equals(UUID.fromString(larrAux[0])) )
				throw new BigBangException("Inesperado: modalidade não pertence ao espaço de trabalho.");
			llngSubLine = Integer.parseInt(larrAux[1]);
		}

		if ( (objectId == null) || (objectId.length() == 0) )
			llngObject = -1;
		else
		{
			larrAux = objectId.split(":");
			if ( larrAux.length != 2 )
	            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + objectId);
			if ( !lidPad.equals(UUID.fromString(larrAux[0])) )
				throw new BigBangException("Inesperado: objecto não pertence ao espaço de trabalho.");
			llngObject = Integer.parseInt(larrAux[1]);
		}

		lobjPad = GetScratchPadStorage().get(lidPad);

		lobjResult = new QuoteRequest.TableSection();
		lobjPad.WritePage(lobjResult, llngSubLine, llngObject);
		return lobjResult;
	}

	@Override
	public QuoteRequest.TableSection savePage(QuoteRequest.TableSection data)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngSubLine;
		int llngObject;
		QuoteRequestScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = data.pageId.split(":");
		if ( larrAux.length != 3 )
            throw new IllegalArgumentException("Unexpected: Invalid page ID string: " + data.pageId);
		lidPad = UUID.fromString(larrAux[0]);
		llngSubLine = Integer.parseInt(larrAux[1]);
		llngObject = Integer.parseInt(larrAux[2]);

		lobjPad = GetScratchPadStorage().get(lidPad);
		lobjPad.UpdatePage(data, llngSubLine, llngObject);

		lobjPad.WritePage(data, llngSubLine, llngObject);
		return data;
	}

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( filterId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		try
		{
			if ( Constants.ObjID_QuoteRequestObject.equals(UUID.fromString(listId)) )
				return GetScratchPadStorage().get(UUID.fromString(filterId)).GetObjects();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		throw new BigBangException("Erro: Lista inválida para o espaço de trabalho.");
	}

	public QuoteRequestObject getObjectInPad(String objectId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;
		QuoteRequestScratchPad lobjPad;
		QuoteRequestObject lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = objectId.split(":");
		if ( larrAux.length != 2 )
            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + objectId);
		lidPad = UUID.fromString(larrAux[0]);
		llngObject = Integer.parseInt(larrAux[1]);

		lobjPad = GetScratchPadStorage().get(lidPad);

		lobjResult = new QuoteRequestObject();
		lobjPad.WriteObject(lobjResult, llngObject);
		return lobjResult;
	}

	public QuoteRequestObject createObjectInPad(String requestId, String objectTypeId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		QuoteRequestScratchPad lobjPad;
		QuoteRequestObject lobjResult;
		int llngObject;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( requestId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");
		if ( objectTypeId == null )
			throw new BigBangException("Erro: Tem que indicar o tipo de objecto.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(requestId));
		llngObject = lobjPad.CreateNewObject(UUID.fromString(objectTypeId));

		lobjResult = new QuoteRequestObject();
		lobjPad.WriteObject(lobjResult, llngObject);
		return lobjResult;
	}

	@Override
	public QuoteRequestObject createObjectFromClientInPad(String requestId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public QuoteRequestObject updateObjectInPad(QuoteRequestObject data)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		QuoteRequestScratchPad lobjPad;
		String[] larrAux;
		UUID lidPad;
		int llngObject;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = data.id.split(":");
		if ( larrAux.length != 2 )
            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + data.id);
		lidPad = UUID.fromString(larrAux[0]);
		llngObject = Integer.parseInt(larrAux[1]);

		lobjPad = GetScratchPadStorage().get(lidPad);
		lobjPad.UpdateObject(data, llngObject);

		lobjPad.WriteObject(data, llngObject);
		return data;
	}

	public void deleteObjectInPad(String objectId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = objectId.split(":");
		if ( larrAux.length != 2 )
            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + objectId);
		lidPad = UUID.fromString(larrAux[0]);
		llngObject = Integer.parseInt(larrAux[1]);

		GetScratchPadStorage().get(lidPad).DeleteObject(llngObject);
	}

	public Remap[] commitPad(String requestId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		QuoteRequestScratchPad lrefPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefPad = GetScratchPadStorage().get(UUID.fromString(requestId));
		lrefPad.CommitChanges();
		GetScratchPadStorage().remove(UUID.fromString(requestId));
		return lrefPad.GetRemapFromPad(true);
	}

	public Remap[] discardPad(String requestId)
		throws SessionExpiredException, BigBangException
	{
		QuoteRequestScratchPad lrefPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefPad = GetScratchPadStorage().get(UUID.fromString(requestId));
		GetScratchPadStorage().remove(UUID.fromString(requestId));
		return lrefPad.GetRemapFromPad(false);
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		ExecMgrXFer lobjEMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(transfer.dataObjectIds[0]));

			lobjEMX = new ExecMgrXFer(lobjRequest.GetProcessID());
			lobjEMX.midNewManager = UUID.fromString(transfer.newManagerId);
			lobjEMX.midMassProcess = null;

			lobjEMX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return transfer;
	}

	public Conversation sendMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjQReq;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjQReq = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjQReq.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.messages[0].subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Outgoing;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Incoming );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_QuoteRequest,
				lobjQReq.getKey());

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public Conversation receiveMessage(Conversation conversation)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjQReq;
		Timestamp ldtAux, ldtLimit;
		Calendar ldtAux2;
		CreateConversation lopCC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( conversation.replylimit == null )
			ldtLimit = null;
		else
		{
			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, conversation.replylimit);
	    	ldtLimit = new Timestamp(ldtAux2.getTimeInMillis());
		}

		try
		{
			lobjQReq = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(conversation.parentDataObjectId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCC = new CreateConversation(lobjQReq.GetProcessID());
		lopCC.mobjData = new ConversationData();
		lopCC.mobjData.mid = null;
		lopCC.mobjData.mstrSubject = conversation.messages[0].subject;
		lopCC.mobjData.midType = UUID.fromString(conversation.requestTypeId);
		lopCC.mobjData.midProcess = null;
		lopCC.mobjData.midStartDir = Constants.MsgDir_Incoming;
		lopCC.mobjData.midPendingDir = ( conversation.replylimit == null ? null : Constants.MsgDir_Outgoing );
		lopCC.mobjData.mdtDueDate = ldtLimit;

		lopCC.mobjData.marrMessages = new MessageData[1];
		lopCC.mobjData.marrMessages[0] = MessageBridge.clientToServer(conversation.messages[0], Constants.ObjID_QuoteRequest,
				lobjQReq.getKey());

		try
		{
			lopCC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ConversationServiceImpl.sGetConversation(lopCC.mobjData.mid);
	}

	public QuoteRequest closeProcess(String requestId, String notes)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		CloseProcess lopCP;
		
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(requestId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCP = new CloseProcess(lobjRequest.GetProcessID());
		lopCP.mstrNotes = notes;

		try
		{
			lopCP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetRequest(lobjRequest.getKey());
	}

	public void deleteRequest(String requestId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.QuoteRequest lobjRequest;
		DeleteQuoteRequest lobjDQR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjRequest = com.premiumminds.BigBang.Jewel.Objects.QuoteRequest.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(requestId));

			lobjDQR = new DeleteQuoteRequest(lobjRequest.GetProcessID());
			lobjDQR.midRequest = UUID.fromString(requestId);
			lobjDQR.mstrReason = reason;
			lobjDQR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		transfer.objectTypeId = Constants.ObjID_QuoteRequest.toString();

		return TransferManagerServiceImpl.sCreateMassTransfer(transfer, Constants.ProcID_QuoteRequest);
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_QuoteRequest;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Case Study]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		QuoteRequestSearchParameter lParam;
		String lstrAux;
		IEntity lrefClients;
		IEntity lrefQRSubLines;
		IEntity lrefNegotiations;

		if ( !(pParam instanceof QuoteRequestSearchParameter) )
			return false;
		lParam = (QuoteRequestSearchParameter)pParam;

		if ( !lParam.includeClosed )
		{
			pstrBuffer.append(" AND [:Process:Running] = 1");
		}

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND [:Number] LIKE N'%").append(lstrAux).append("%'");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
		}

		if ( lParam.subLineId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Quote Request] FROM (");
			try
			{
				lrefQRSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_QuoteRequestSubLine));
				pstrBuffer.append(lrefQRSubLines.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubLines] WHERE [:Sub Line] = '").append(lParam.subLineId).append("')");
		}
		else if ( lParam.lineId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Quote Request] FROM (");
			try
			{
				lrefQRSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_QuoteRequestSubLine));
				pstrBuffer.append(lrefQRSubLines.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubLines] WHERE [:Sub Line:Line] = '").append(lParam.lineId).append("'");
		}
		else if ( lParam.categoryId != null )
		{
			pstrBuffer.append(" AND [PK] IN (SELECT [:Quote Request] FROM (");
			try
			{
				lrefQRSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_QuoteRequestSubLine));
				pstrBuffer.append(lrefQRSubLines.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxSubLines] WHERE [:Sub Line:Line:Category] = '").append(lParam.categoryId).append("'");
		}

		if ( lParam.insuranceAgencyId != null )
		{
			pstrBuffer.append(" AND [:Process] IN (SELECT [:Process:Parent] FROM (");
			try
			{
				lrefNegotiations = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_Negotiation));
				pstrBuffer.append(lrefNegotiations.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxNegotiations] WHERE [:Company] = '").append(lParam.insuranceAgencyId).append("'");
		}

		if ( lParam.mediatorId != null )
		{
			pstrBuffer.append(" AND [:Mediator] = '").append(lParam.mediatorId).append("'");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( (lParam.caseStudy != null) && lParam.caseStudy )
		{
			pstrBuffer.append(" AND [:Case Study] = 1");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		QuoteRequestSortParameter lParam;
		IEntity lrefClients;

		if ( !(pParam instanceof QuoteRequestSortParameter) )
			return false;
		lParam = (QuoteRequestSortParameter)pParam;

		if ( lParam.field == QuoteRequestSortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == QuoteRequestSortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == QuoteRequestSortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("(SELECT [:Name] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.field == QuoteRequestSortParameter.SortableField.CLIENT_NUMBER )
		{
			pstrBuffer.append("(SELECT [:Number] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Process] = [Aux].[:Process:Parent])");
		}

		if ( lParam.order == SortOrder.ASC )
			pstrBuffer.append(" ASC");

		if ( lParam.order == SortOrder.DESC )
			pstrBuffer.append(" DESC");

		return true;
	}

	protected SearchResult buildResult(UUID pid, Object[] parrValues)
	{
		IProcess lobjProcess;
		QuoteRequestStub lobjResult;
		Client lobjClient;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				lobjClient = (Client)lobjProcess.GetParent().GetData();
			}
			catch (Throwable e)
			{
				lobjClient = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjClient = null;
		}

		lobjResult = new QuoteRequestStub();

		lobjResult.id = pid.toString();
		lobjResult.processNumber = (String)parrValues[0];
		lobjResult.clientId = (lobjClient == null ? null : lobjClient.getKey().toString());
		lobjResult.clientNumber = (lobjClient == null ? "" : ((Integer)lobjClient.getAt(1)).toString());
		lobjResult.clientName = (lobjClient == null ? "(Erro)" : lobjClient.getLabel());
		lobjResult.caseStudy = (Boolean)parrValues[2];
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		lobjResult.isOpen = (lobjProcess == null ? false : lobjProcess.IsRunning());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		QuoteRequestSearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof QuoteRequestSearchParameter) )
				continue;
			lParam = (QuoteRequestSearchParameter)parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("0 END");
		}

		return lbFound;
	}
}
