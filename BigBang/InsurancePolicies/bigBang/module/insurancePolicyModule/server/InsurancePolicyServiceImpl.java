package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IStep;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.InfoOrDocumentRequestServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.server.TransferManagerServiceImpl;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.expenseModule.server.ExpenseServiceImpl;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;
import bigBang.module.quoteRequestModule.server.NegotiationServiceImpl;
import bigBang.module.receiptModule.server.ReceiptServiceImpl;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Data.DebitNoteData;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Data.NegotiationData;
import com.premiumminds.BigBang.Jewel.Data.PolicyCoInsurerData;
import com.premiumminds.BigBang.Jewel.Data.PolicyCoverageData;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.PolicyExerciseData;
import com.premiumminds.BigBang.Jewel.Data.PolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.PolicyValueData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoInsurer;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.Tax;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Client.CreatePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateDebitNote;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateExpense;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateInfoRequest;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateNegotiation;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Policy.DeletePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ExecMgrXFer;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.Policy.PerformComputations;
import com.premiumminds.BigBang.Jewel.Operations.Policy.TransferToClient;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ValidatePolicy;
import com.premiumminds.BigBang.Jewel.Operations.Policy.VoidPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class InsurancePolicyServiceImpl
	extends SearchServiceBase
	implements InsurancePolicyService
{
	private static final long serialVersionUID = 1L;

	private static class PolicyScratchPad
	{
		private static class PadCoInsurer
			extends PolicyCoInsurerData
		{
			private static final long serialVersionUID = 1L;
		}

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
			public boolean mbVariesByExercise;
		}

		private static class PadCoverage
			extends PolicyCoverageData
		{
			private static final long serialVersionUID = 1L;

			public transient String mstrLabel;
			public transient boolean mbMandatory;
			public transient boolean mbIsHeader;
			public transient int mlngOrder;
			public transient PadField[] marrFields;
		}

		private static class PadObject
			extends PolicyObjectData
		{
			private static final long serialVersionUID = 1L;

			public int mlngOrigIndex;
		}

		private static class PadExercise
			extends PolicyExerciseData
		{
			private static final long serialVersionUID = 1L;

			public int mlngOrigIndex;
		}

		private static class PadValue
			extends PolicyValueData
		{
			private static final long serialVersionUID = 1L;

			public transient PadCoverage mrefCoverage;
			public transient PadField mrefField;
		}

		public final UUID mid;
		public boolean mbCreated;
		public boolean mbValid;
		public boolean mbIsNew;

		public UUID midClient;
		public PolicyData mobjPolicy;
		public ArrayList<PadCoInsurer> marrCoInsurers;
		public ArrayList<PadCoverage> marrCoverages;
		public ArrayList<PadObject> marrObjects;
		public ArrayList<PadExercise> marrExercises;
		public ArrayList<PadValue> marrValues;

		public PolicyScratchPad()
		{
			mid = UUID.randomUUID();
			mbCreated = false;
			mbValid = false;
			mbIsNew = true;
		}
		
		public UUID GetID()
		{
			return mid;
		}

		public void InitNew(InsurancePolicy pobjSource)
			throws BigBangException
		{
			PadCoInsurer lobjCoInsurer;
			SubLine lobjSubLine;
			com.premiumminds.BigBang.Jewel.Objects.Coverage[] larrAuxCoverages;
			PadCoverage lobjCoverage;
			Tax[] larrTaxes;
			ArrayList<PadField> larrFields;
			PadExercise lobjExercise;
			PadField lobjField;
			PadValue lobjValue;
			int i, j;

			if ( mbCreated && !mbIsNew )
				throw new BigBangException("Erro: Não pode inicializar o espaço de trabalho numa edição de uma apólice existente.");

			midClient = ( pobjSource.clientId == null ? null : UUID.fromString(pobjSource.clientId) );
			mobjPolicy = new PolicyData();
			mobjPolicy.mstrNumber = pobjSource.number;
			mobjPolicy.midCompany = ( pobjSource.insuranceAgencyId == null ? null : UUID.fromString(pobjSource.insuranceAgencyId) );
			mobjPolicy.midSubLine = UUID.fromString(pobjSource.subLineId);
			mobjPolicy.mdtBeginDate = ( pobjSource.startDate == null ? null :
					Timestamp.valueOf(pobjSource.startDate + " 00:00:00.0") );
			mobjPolicy.midDuration = ( pobjSource.durationId == null ? null : UUID.fromString(pobjSource.durationId) );
			mobjPolicy.midFractioning = ( pobjSource.fractioningId == null ? null : UUID.fromString(pobjSource.fractioningId) );
			mobjPolicy.mlngMaturityDay = pobjSource.maturityDay;
			mobjPolicy.mlngMaturityMonth = pobjSource.maturityMonth;
			mobjPolicy.mdtEndDate = ( pobjSource.expirationDate == null ? null :
					Timestamp.valueOf(pobjSource.expirationDate + " 00:00:00.0") );
			mobjPolicy.mstrNotes = pobjSource.notes;
			mobjPolicy.midMediator = ( pobjSource.mediatorId == null ? null : UUID.fromString(pobjSource.mediatorId) );
			mobjPolicy.mbCaseStudy = pobjSource.caseStudy;
			mobjPolicy.mdblPremium = ( pobjSource.premium == null ? null : new BigDecimal(pobjSource.premium+"") );
			mobjPolicy.mdblRetrocession = ( pobjSource.agentPercentage == null ? null : new BigDecimal(pobjSource.agentPercentage+"") );
			mobjPolicy.midProfile = ( pobjSource.operationalProfileId == null ? null : UUID.fromString(pobjSource.operationalProfileId) );
			mobjPolicy.midStatus = Constants.StatusID_InProgress;

			mbValid = false;

			marrCoInsurers = new ArrayList<PadCoInsurer>();
			marrCoverages = new ArrayList<PadCoverage>();
			marrObjects = new ArrayList<PadObject>();
			marrExercises = new ArrayList<PadExercise>();
			marrValues = new ArrayList<PadValue>();

			mobjPolicy.mbModified = false;

			if ( pobjSource.coInsurers != null )
			{
				for ( i = 0; i < pobjSource.coInsurers.length; i++ )
				{
					lobjCoInsurer = new PadCoInsurer();
					lobjCoInsurer.midPolicy = mobjPolicy.mid;
					lobjCoInsurer.midCompany = UUID.fromString(pobjSource.coInsurers[i].insuranceAgencyId);
					lobjCoInsurer.mdblPercent = new BigDecimal(pobjSource.coInsurers[i].percent+"");
					marrCoInsurers.add(lobjCoInsurer);
				}
			}

			try
			{
				lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), mobjPolicy.midSubLine);

				lobjExercise = null;
				if ( !Constants.ExID_None.equals(lobjSubLine.getExerciseType()) )
				{
					lobjExercise = new PadExercise();
					lobjExercise.mstrLabel = "Inicial";
					marrExercises.add(lobjExercise);
				}

				larrAuxCoverages = lobjSubLine.GetCurrentCoverages();
				for ( i = 0 ; i < larrAuxCoverages.length; i++ )
				{
					lobjCoverage = new PadCoverage();
					lobjCoverage.mid = null;
					lobjCoverage.midOwner = null;
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
						if ( !larrTaxes[j].IsVisible() )
							continue;

						lobjField = new PadField();
						lobjField.midField = larrTaxes[j].getKey();
						lobjField.mlngColIndex = larrTaxes[j].GetColumnOrder();
						lobjField.mstrLabel = larrTaxes[j].getLabel();
						lobjField.midType = (UUID)larrTaxes[j].getAt(2);
						lobjField.mstrUnits = (String)larrTaxes[j].getAt(3);
						lobjField.mstrDefault = (String)larrTaxes[j].getAt(4);
						lobjField.midRefersTo = (UUID)larrTaxes[j].getAt(7);
						lobjField.mbVariesByObject = larrTaxes[j].GetVariesByObject();
						lobjField.mbVariesByExercise = larrTaxes[j].GetVariesByExercise();
						larrFields.add(lobjField);

						if ( lobjField.mbVariesByObject || (lobjField.mbVariesByExercise && (lobjExercise == null)) )
							continue;

						lobjValue = new PadValue();
						lobjValue.mid = null;
						lobjValue.mstrValue = (String)larrTaxes[j].getAt(4);
						lobjValue.midOwner = null;
						lobjValue.midField = larrTaxes[j].getKey();
						lobjValue.midObject = null;
						lobjValue.midExercise = null;
						lobjValue.mrefCoverage = lobjCoverage;
						lobjValue.mrefField = lobjField;
						lobjValue.mlngObject = -1;
						lobjValue.mlngExercise = ( lobjField.mbVariesByExercise ? 0 : -1 );
						lobjValue.mbDeleted = false;
						marrValues.add(lobjValue);
					}
					lobjCoverage.marrFields = larrFields.toArray(new PadField[larrFields.size()]);
					marrCoverages.add(lobjCoverage);
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			mbCreated = true;
			mbValid = true;
			mbIsNew = true;
		}

		public void OpenForEdit(UUID pidPolicy)
			throws BigBangException
		{
			Policy lobjAuxPolicy;
			PolicyCoInsurer[] larrLocalCoInsurers;
			PadCoInsurer lobjCoInsurer;
			PolicyCoverage[] larrLocalCoverages;
			com.premiumminds.BigBang.Jewel.Objects.Coverage[] larrAuxCoverages;
			Hashtable<UUID, PadCoverage> lmapCoverages;
			PadCoverage lobjCoverage;
			Tax[] larrTaxes;
			ArrayList<PadField> larrFields;
			Hashtable<UUID, PadField> lmapFields;
			PadField lobjField;
			PolicyObject[] larrAuxObjects;
			Hashtable<UUID, Integer> lmapObjects;
			PadObject lobjObject;
			PolicyExercise[] larrAuxExercises;
			Hashtable<UUID, Integer> lmapExercises;
			PadExercise lobjExercise;
			PolicyValue[] larrAuxValues;
			PadValue lobjValue;
			int i, j, k, l;

			if ( mbCreated )
				throw new BigBangException("Erro: Não pode inicializar o mesmo espaço de trabalho duas vezes.");

			marrCoInsurers = new ArrayList<PadCoInsurer>();
			marrCoverages = new ArrayList<PadCoverage>();
			marrObjects = new ArrayList<PadObject>();
			marrExercises = new ArrayList<PadExercise>();
			marrValues = new ArrayList<PadValue>();

			try
			{
				lobjAuxPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), pidPolicy);

				midClient = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjAuxPolicy.GetProcessID()).GetParent().GetDataKey();

				mobjPolicy = new PolicyData();
				mobjPolicy.FromObject(lobjAuxPolicy);

				mobjPolicy.mbModified = false;

				larrLocalCoInsurers = lobjAuxPolicy.GetCurrentCoInsurers();
				for ( i = 0; i < larrLocalCoInsurers.length; i++ )
				{
					lobjCoInsurer = new PadCoInsurer();
					lobjCoInsurer.FromObject(larrLocalCoInsurers[i]);
					marrCoInsurers.add(lobjCoInsurer);
				}

				larrLocalCoverages = lobjAuxPolicy.GetCurrentCoverages();
				lmapCoverages = new Hashtable<UUID, PadCoverage>();
				lmapFields = new Hashtable<UUID, PadField>();
				for ( i = 0 ; i < larrLocalCoverages.length; i++ )
				{
					lobjCoverage = new PadCoverage();
					lobjCoverage.FromObject(larrLocalCoverages[i]);
					lobjCoverage.mstrLabel = larrLocalCoverages[i].GetCoverage().getLabel();
					lobjCoverage.mbIsHeader = larrLocalCoverages[i].GetCoverage().IsHeader();
					lobjCoverage.mbMandatory = larrLocalCoverages[i].GetCoverage().IsMandatory();
					lobjCoverage.mlngOrder = larrLocalCoverages[i].GetCoverage().GetOrder();
					larrTaxes = larrLocalCoverages[i].GetCoverage().GetCurrentTaxes();
					larrFields = new ArrayList<PadField>();
					for ( j = 0; j < larrTaxes.length; j++ )
					{
						if ( !larrTaxes[j].IsVisible() )
							continue;

						lobjField = new PadField();
						lobjField.midField = larrTaxes[j].getKey();
						lobjField.mlngColIndex = larrTaxes[j].GetColumnOrder();
						lobjField.mstrLabel = larrTaxes[j].getLabel();
						lobjField.midType = (UUID)larrTaxes[j].getAt(2);
						lobjField.mstrUnits = (String)larrTaxes[j].getAt(3);
						lobjField.mstrDefault = (String)larrTaxes[j].getAt(4);
						lobjField.midRefersTo = (UUID)larrTaxes[j].getAt(7);
						lobjField.mbVariesByObject = larrTaxes[j].GetVariesByObject();
						lobjField.mbVariesByExercise = larrTaxes[j].GetVariesByExercise();
						larrFields.add(lobjField);
						lmapCoverages.put(larrTaxes[j].getKey(), lobjCoverage);
						lmapFields.put(larrTaxes[j].getKey(), lobjField);
					}
					lobjCoverage.marrFields = larrFields.toArray(new PadField[larrFields.size()]);
					marrCoverages.add(lobjCoverage);
				}

				larrAuxCoverages = SubLine.GetInstance(Engine.getCurrentNameSpace(), mobjPolicy.midSubLine).GetCurrentCoverages();
				for ( i = 0 ; i < larrAuxCoverages.length; i++ )
				{
					if ( FindCoverage(larrAuxCoverages[i].getKey(), 0) >= 0 )
						continue;
					lobjCoverage = new PadCoverage();
					lobjCoverage.mid = null;
					lobjCoverage.midOwner = pidPolicy;
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
						if ( !larrTaxes[j].IsVisible() )
							continue;

						lobjField = new PadField();
						lobjField.midField = larrTaxes[j].getKey();
						lobjField.mlngColIndex = larrTaxes[j].GetColumnOrder();
						lobjField.mstrLabel = larrTaxes[j].getLabel();
						lobjField.midType = (UUID)larrTaxes[j].getAt(2);
						lobjField.mstrUnits = (String)larrTaxes[j].getAt(3);
						lobjField.mstrDefault = (String)larrTaxes[j].getAt(4);
						lobjField.midRefersTo = (UUID)larrTaxes[j].getAt(7);
						lobjField.mbVariesByObject = larrTaxes[j].GetVariesByObject();
						lobjField.mbVariesByExercise = larrTaxes[j].GetVariesByExercise();
						larrFields.add(lobjField);
						lmapCoverages.put(larrTaxes[j].getKey(), lobjCoverage);
						lmapFields.put(larrTaxes[j].getKey(), lobjField);
					}
					lobjCoverage.marrFields = larrFields.toArray(new PadField[larrFields.size()]);
					marrCoverages.add(lobjCoverage);
				}

				larrAuxObjects = lobjAuxPolicy.GetCurrentObjects();
				lmapObjects = new Hashtable<UUID, Integer>();
				for ( i = 0 ; i < larrAuxObjects.length; i++ )
				{
					lobjObject = new PadObject();
					lobjObject.FromObject(larrAuxObjects[i]);
					lobjObject.mbDeleted = false;
					marrObjects.add(lobjObject);
					lmapObjects.put(lobjObject.mid, i);
				}

				larrAuxExercises = lobjAuxPolicy.GetCurrentExercises();
				lmapExercises = new Hashtable<UUID, Integer>();
				for ( i = 0 ; i < larrAuxExercises.length; i++ )
				{
					lobjExercise = new PadExercise();
					lobjExercise.FromObject(larrAuxExercises[i]);
					lobjExercise.mbDeleted = false;
					marrExercises.add(lobjExercise);
					lmapExercises.put(lobjExercise.mid, i);
				}

				larrAuxValues = lobjAuxPolicy.GetCurrentValues();
				for ( i = 0 ; i < larrAuxValues.length; i++ )
				{
					lobjValue = new PadValue();
					lobjValue.FromObject(larrAuxValues[i]);
					lobjValue.mrefCoverage = lmapCoverages.get(lobjValue.midField);
					lobjValue.mrefField = lmapFields.get(lobjValue.midField);
					lobjValue.mlngObject = ( lobjValue.midObject == null ? -1 : lmapObjects.get(lobjValue.midObject) );
					lobjValue.mlngExercise = ( lobjValue.midExercise == null ? -1 : lmapExercises.get(lobjValue.midExercise) );
					lobjValue.mbDeleted = !larrAuxValues[i].GetTax().IsVisible();
					marrValues.add(lobjValue);
				}

				for ( i = 0 ; i < marrCoverages.size(); i++ )
				{
					for ( j = 0; j < marrCoverages.get(i).marrFields.length; j++ )
					{
						lobjField = marrCoverages.get(i).marrFields[j];
						if ( lobjField.mbVariesByObject )
						{
							for ( k = 0; k < marrObjects.size(); k++ )
							{
								if ( lobjField.mbVariesByExercise )
								{
									for ( l = 0; l < marrExercises.size(); l++ )
									{
										if ( FindValue(lobjField.midField, k, l, 0) >= 0 )
											continue;
										lobjValue = new PadValue();
										lobjValue.mid = null;
										lobjValue.mstrValue = lobjField.mstrDefault;
										lobjValue.midOwner = pidPolicy;
										lobjValue.midField = lobjField.midField;
										lobjValue.midObject = marrObjects.get(k).mid;
										lobjValue.midExercise = marrExercises.get(l).mid;
										lobjValue.mrefCoverage = marrCoverages.get(i);
										lobjValue.mrefField = lobjField;
										lobjValue.mlngObject = k;
										lobjValue.mlngExercise = l;
										lobjValue.mbDeleted = false;
										marrValues.add(lobjValue);
									}
								}
								else
								{
									if ( FindValue(lobjField.midField, k, -1, 0) >= 0 )
										continue;
									lobjValue = new PadValue();
									lobjValue.mid = null;
									lobjValue.mstrValue = lobjField.mstrDefault;
									lobjValue.midOwner = pidPolicy;
									lobjValue.midField = lobjField.midField;
									lobjValue.midObject = marrObjects.get(k).mid;
									lobjValue.midExercise = null;
									lobjValue.mrefCoverage = marrCoverages.get(i);
									lobjValue.mrefField = lobjField;
									lobjValue.mlngObject = k;
									lobjValue.mlngExercise = -1;
									lobjValue.mbDeleted = false;
									marrValues.add(lobjValue);
								}
							}
						}
						else if ( lobjField.mbVariesByExercise )
						{
							for ( l = 0; l < marrExercises.size(); l++ )
							{
								if ( FindValue(lobjField.midField, -1, l, 0) >= 0 )
									continue;
								lobjValue = new PadValue();
								lobjValue.mid = null;
								lobjValue.mstrValue = lobjField.mstrDefault;
								lobjValue.midOwner = pidPolicy;
								lobjValue.midField = lobjField.midField;
								lobjValue.midObject = null;
								lobjValue.midExercise = marrExercises.get(l).mid;
								lobjValue.mrefCoverage = marrCoverages.get(i);
								lobjValue.mrefField = lobjField;
								lobjValue.mlngObject = -1;
								lobjValue.mlngExercise = l;
								lobjValue.mbDeleted = false;
								marrValues.add(lobjValue);
							}
						}
						else
						{
							if ( FindValue(lobjField.midField, -1, -1, 0) >= 0 )
								continue;
							lobjValue = new PadValue();
							lobjValue.mid = null;
							lobjValue.mstrValue = lobjField.mstrDefault;
							lobjValue.midOwner = pidPolicy;
							lobjValue.midField = lobjField.midField;
							lobjValue.midObject = null;
							lobjValue.midExercise = null;
							lobjValue.mrefCoverage = marrCoverages.get(i);
							lobjValue.mrefField = lobjField;
							lobjValue.mlngObject = -1;
							lobjValue.mlngExercise = -1;
							lobjValue.mbDeleted = false;
							marrValues.add(lobjValue);
						}
					}
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			mbCreated = true;
			mbValid = true;
			mbIsNew = false;
		}

		public Remap[] GetRemapIntoPad()
		{
			Remap[] larrResult;
			int i;

			if ( !mbValid )
			{
				larrResult = new Remap[1];

				larrResult[0] = new Remap();
				larrResult[0].typeId = Constants.ObjID_Policy.toString();
				larrResult[0].remapIds = new Remap.RemapId[1];
				larrResult[0].remapIds[0] = new Remap.RemapId();
				larrResult[0].remapIds[0].oldId = null;
				larrResult[0].remapIds[0].newId = mid.toString();
				larrResult[0].remapIds[0].newIdIsInPad = true;

				return larrResult;
			}

			larrResult = new Remap[3];

			larrResult[0] = new Remap();
			larrResult[0].typeId = Constants.ObjID_Policy.toString();
			larrResult[0].remapIds = new Remap.RemapId[1];
			larrResult[0].remapIds[0] = new Remap.RemapId();
			larrResult[0].remapIds[0].oldId = mobjPolicy.mid.toString();
			larrResult[0].remapIds[0].newId = mid.toString();
			larrResult[0].remapIds[0].newIdIsInPad = true;

			larrResult[1] = new Remap();
			larrResult[1].typeId = Constants.ObjID_PolicyObject.toString();
			larrResult[1].remapIds = new Remap.RemapId[marrObjects.size()];
			for ( i = 0; i < larrResult[1].remapIds.length; i++ )
			{
				larrResult[1].remapIds[i] = new Remap.RemapId();
				larrResult[1].remapIds[i].oldId = marrObjects.get(i).mid.toString();
				larrResult[1].remapIds[i].newId = mid.toString() + ":" + i;
				larrResult[1].remapIds[i].newIdIsInPad = true;
			}

			larrResult[2] = new Remap();
			larrResult[2].typeId = Constants.ObjID_PolicyExercise.toString();
			larrResult[2].remapIds = new Remap.RemapId[marrExercises.size()];
			for ( i = 0; i < larrResult[2].remapIds.length; i++ )
			{
				larrResult[2].remapIds[i] = new Remap.RemapId();
				larrResult[2].remapIds[i].oldId = marrExercises.get(i).mid.toString();
				larrResult[2].remapIds[i].newId = mid.toString() + ":" + i;
				larrResult[2].remapIds[i].newIdIsInPad = true;
			}

			return larrResult;
		}

		public Remap[] GetRemapFromPad(boolean pbWithCommit)
		{
			Remap[] larrResult;
			int i;

			if ( !mbValid )
			{
				larrResult = new Remap[1];

				larrResult[0] = new Remap();
				larrResult[0].typeId = Constants.ObjID_Policy.toString();
				larrResult[0].remapIds = new Remap.RemapId[1];
				larrResult[0].remapIds[0] = new Remap.RemapId();
				larrResult[0].remapIds[0].oldId = mid.toString();
				larrResult[0].remapIds[0].newId = null;
				larrResult[0].remapIds[0].newIdIsInPad = true;

				return larrResult;
			}

			larrResult = new Remap[3];

			larrResult[0] = new Remap();
			larrResult[0].typeId = Constants.ObjID_Policy.toString();
			larrResult[0].remapIds = new Remap.RemapId[1];
			larrResult[0].remapIds[0] = new Remap.RemapId();
			larrResult[0].remapIds[0].oldId = mid.toString();
			larrResult[0].remapIds[0].newId = (mobjPolicy.mid == null ? null : mobjPolicy.mid.toString());
			larrResult[0].remapIds[0].newIdIsInPad = false;

			larrResult[1] = new Remap();
			larrResult[1].typeId = Constants.ObjID_PolicyObject.toString();
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

			larrResult[2] = new Remap();
			larrResult[2].typeId = Constants.ObjID_PolicyExercise.toString();
			larrResult[2].remapIds = new Remap.RemapId[marrExercises.size()];
			for ( i = 0; i < larrResult[2].remapIds.length; i++ )
			{
				larrResult[2].remapIds[i] = new Remap.RemapId();
				larrResult[2].remapIds[i].oldId = mid.toString() + ":" + i;
				if ( (pbWithCommit && marrExercises.get(i).mbDeleted) || (marrExercises.get(i).mid == null) )
					larrResult[2].remapIds[i].newId = null;
				else
					larrResult[2].remapIds[i].newId = marrExercises.get(i).mid.toString();
				larrResult[2].remapIds[i].newIdIsInPad = false;
			}

			return larrResult;
		}

		public InsurancePolicy WriteBasics()
			throws BigBangException, CorruptedPadException
		{
			InsurancePolicy lobjResult;
			SubLine lobjSubLine;
			ArrayList<InsurancePolicy.CoInsurer> larrCoInsurers;
			InsurancePolicy.CoInsurer lobjCoInsurer;
			int i;

			if ( !mbCreated )
			{
				lobjResult = new InsurancePolicy();
				lobjResult.id = mid.toString();
				return lobjResult;
			}

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( mobjPolicy.mid == null )
				lobjResult = new InsurancePolicy();
			else
				lobjResult = sGetPolicy(mobjPolicy.mid);

			lobjResult.id = mid.toString();
			lobjResult.number = mobjPolicy.mstrNumber;
			lobjResult.clientId = ( midClient == null ? null : midClient.toString() );
			lobjResult.subLineId = ( mobjPolicy.midSubLine == null ? null : mobjPolicy.midSubLine.toString() );
			lobjResult.processId = ( mobjPolicy.midProcess == null ? null : mobjPolicy.midProcess.toString() );
			lobjResult.insuranceAgencyId = ( mobjPolicy.midCompany == null ? null : mobjPolicy.midCompany.toString() );
			lobjResult.startDate = ( mobjPolicy.mdtBeginDate == null ? null :
					mobjPolicy.mdtBeginDate.toString().substring(0, 10) );
			lobjResult.durationId = ( mobjPolicy.midDuration == null ? null : mobjPolicy.midDuration.toString() );
			lobjResult.fractioningId = ( mobjPolicy.midFractioning == null ? null : mobjPolicy.midFractioning.toString() );
			lobjResult.maturityDay = mobjPolicy.mlngMaturityDay;
			lobjResult.maturityMonth = mobjPolicy.mlngMaturityMonth;
			lobjResult.expirationDate = ( mobjPolicy.mdtEndDate == null ? null :
					mobjPolicy.mdtEndDate.toString().substring(0, 10));
			lobjResult.notes = mobjPolicy.mstrNotes;
			lobjResult.mediatorId = ( mobjPolicy.midMediator == null ? null : mobjPolicy.midMediator.toString() );
			lobjResult.caseStudy = ( mobjPolicy.mbCaseStudy == null ? false : mobjPolicy.mbCaseStudy );
			lobjResult.statusId = ( mobjPolicy.midStatus == null ? null : mobjPolicy.midStatus.toString() );
			lobjResult.premium = ( mobjPolicy.mdblPremium == null ? null : mobjPolicy.mdblPremium.doubleValue() );
			lobjResult.agentPercentage = ( mobjPolicy.mdblRetrocession == null ? null : mobjPolicy.mdblRetrocession.doubleValue() );
			lobjResult.operationalProfileId = ( mobjPolicy.midProfile == null ? null : mobjPolicy.midProfile.toString() );
			lobjResult.docushare = mobjPolicy.mstrDocuShare;

			larrCoInsurers = new ArrayList<InsurancePolicy.CoInsurer>();
			for ( i = 0; i < marrCoInsurers.size(); i++ )
			{
				if ( marrCoInsurers.get(i).mbDeleted )
					continue;
				lobjCoInsurer = new InsurancePolicy.CoInsurer();
				lobjCoInsurer.insuranceAgencyId = marrCoInsurers.get(i).midCompany.toString();
				lobjCoInsurer.percent = marrCoInsurers.get(i).mdblPercent.doubleValue();
				larrCoInsurers.add(lobjCoInsurer);
			}
			if ( larrCoInsurers.size() == 0 )
				lobjResult.coInsurers = null;
			else
				lobjResult.coInsurers = larrCoInsurers.toArray(new InsurancePolicy.CoInsurer[larrCoInsurers.size()]);

			if ( mobjPolicy.midStatus != null )
			{
				try
				{
					lobjResult.statusText = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
							Constants.ObjID_PolicyStatus), mobjPolicy.midStatus).getLabel();
				}
				catch (Throwable e)
				{
					lobjResult.statusText = "(Erro a obter o estado da apólice.)";
				}
			}

			if ( mobjPolicy.midSubLine != null )
			{
				try
				{
					lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), mobjPolicy.midSubLine);
					lobjResult.lineId = lobjSubLine.getLine().getKey().toString();
					lobjResult.categoryId = lobjSubLine.getLine().getCategory().getKey().toString();
					lobjResult.categoryName = lobjSubLine.getLine().getCategory().getLabel();
					lobjResult.lineName = lobjSubLine.getLine().getLabel();
					lobjResult.subLineName = lobjSubLine.getLabel();
				}
				catch (Throwable e)
				{
				}
			}

			return lobjResult;
		}

		public void WriteResult(InsurancePolicy pobjResult)
			throws CorruptedPadException
		{
			ArrayList<InsurancePolicy.HeaderField> larrHeaders;
			ArrayList<InsurancePolicy.Coverage> larrAuxCoverages;
			ArrayList<InsurancePolicy.Coverage.Variability> larrVariability;
			Hashtable<Integer, InsurancePolicy.ColumnHeader> larrColumns;
			ArrayList<InsurancePolicy.TableSection.TableField> larrTableFields;
			ArrayList<InsurancePolicy.ExtraField> larrExtraFields;
			InsurancePolicy.HeaderField lobjHeader;
			InsurancePolicy.Coverage lobjAuxCoverage;
			InsurancePolicy.Coverage.Variability lobjVariability;
			InsurancePolicy.ColumnHeader lobjColumn;
			InsurancePolicy.TableSection.TableField lobjTableField;
			InsurancePolicy.ExtraField lobjExtraField;
			int i, j;

			if ( !mbCreated )
				return;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			pobjResult.id = mid.toString();

			larrHeaders = new ArrayList<InsurancePolicy.HeaderField>();
			for ( i = 0; i < marrValues.size(); i++ )
			{
				if ( !marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mlngObject >= 0) ||
						(marrValues.get(i).mlngExercise >= 0))
					continue;

				lobjHeader = new InsurancePolicy.HeaderField();
				lobjHeader.fieldId = marrValues.get(i).midField.toString();
				lobjHeader.fieldName = marrValues.get(i).mrefField.mstrLabel;
				lobjHeader.type = GetFieldTypeByID(marrValues.get(i).mrefField.midType);
				lobjHeader.unitsLabel = marrValues.get(i).mrefField.mstrUnits;
				lobjHeader.refersToId = ( marrValues.get(i).mrefField.midRefersTo == null ? null :
						marrValues.get(i).mrefField.midRefersTo.toString() );
				lobjHeader.value = marrValues.get(i).mstrValue;
				lobjHeader.order = marrValues.get(i).mrefField.mlngColIndex;
				larrHeaders.add(lobjHeader);
			}
			pobjResult.headerFields = larrHeaders.toArray(new InsurancePolicy.HeaderField[larrHeaders.size()]);

			larrAuxCoverages = new ArrayList<InsurancePolicy.Coverage>();
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				if ( marrCoverages.get(i).mbIsHeader )
					continue;

				lobjAuxCoverage = new InsurancePolicy.Coverage();
				lobjAuxCoverage.coverageId = marrCoverages.get(i).midCoverage.toString();
				lobjAuxCoverage.coverageName = marrCoverages.get(i).mstrLabel;
				lobjAuxCoverage.mandatory = marrCoverages.get(i).mbMandatory;
				lobjAuxCoverage.order = marrCoverages.get(i).mlngOrder;
				lobjAuxCoverage.presentInPolicy = marrCoverages.get(i).mbPresent;
				larrVariability = new ArrayList<InsurancePolicy.Coverage.Variability>();
				for ( j = 0; j < marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrCoverages.get(i).marrFields[j].mlngColIndex < 0 )
						continue;

					lobjVariability = new InsurancePolicy.Coverage.Variability();
					lobjVariability.columnIndex = marrCoverages.get(i).marrFields[j].mlngColIndex;
					lobjVariability.variesByObject = marrCoverages.get(i).marrFields[j].mbVariesByObject;
					lobjVariability.variesByExercise = marrCoverages.get(i).marrFields[j].mbVariesByExercise;
					larrVariability.add(lobjVariability);
				}
				lobjAuxCoverage.variability =
						larrVariability.toArray(new InsurancePolicy.Coverage.Variability[larrVariability.size()]);
				larrAuxCoverages.add(lobjAuxCoverage);
			}
			pobjResult.coverages = larrAuxCoverages.toArray(new InsurancePolicy.Coverage[larrAuxCoverages.size()]);

			larrColumns = new Hashtable<Integer, InsurancePolicy.ColumnHeader>();
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				if ( marrCoverages.get(i).mbIsHeader )
					continue;

				for ( j = 0; j < marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrCoverages.get(i).marrFields[j].mlngColIndex < 0 )
						continue;
					if ( larrColumns.containsKey(marrCoverages.get(i).marrFields[j].mlngColIndex) )
						continue;

					lobjColumn = new InsurancePolicy.ColumnHeader();
					lobjColumn.label = marrCoverages.get(i).marrFields[j].mstrLabel;
					lobjColumn.type = GetFieldTypeByID(marrCoverages.get(i).marrFields[j].midType);
					lobjColumn.unitsLabel = marrCoverages.get(i).marrFields[j].mstrUnits;
					lobjColumn.refersToId = ( marrCoverages.get(i).marrFields[j].midRefersTo == null ? null :
						marrCoverages.get(i).marrFields[j].midRefersTo.toString() );
					larrColumns.put(marrCoverages.get(i).marrFields[j].mlngColIndex, lobjColumn);
				}
			}
			pobjResult.columns = new InsurancePolicy.ColumnHeader[larrColumns.size()];
			for ( Integer ii: larrColumns.keySet() )
				pobjResult.columns[ii] = larrColumns.get(ii);

			if ( pobjResult.coverages.length + pobjResult.columns.length == 0 )
				pobjResult.tableData = new InsurancePolicy.TableSection[0];
			else
			{
				pobjResult.tableData = new InsurancePolicy.TableSection[1];
				pobjResult.tableData[0] = new InsurancePolicy.TableSection();
				pobjResult.tableData[0].pageId = mid.toString() + ":-1:-1";
				larrTableFields = new ArrayList<InsurancePolicy.TableSection.TableField>();
				for ( i = 0; i < marrValues.size(); i++ )
				{
					if ( marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mrefField.mlngColIndex < 0) ||
							(marrValues.get(i).mlngObject >= 0) || (marrValues.get(i).mlngExercise >= 0))
						continue;

					lobjTableField = new InsurancePolicy.TableSection.TableField();
					lobjTableField.fieldId = marrValues.get(i).midField.toString();
					lobjTableField.coverageId = marrValues.get(i).mrefCoverage.midCoverage.toString();
					lobjTableField.columnIndex = marrValues.get(i).mrefField.mlngColIndex;
					lobjTableField.value = marrValues.get(i).mstrValue;
					larrTableFields.add(lobjTableField);
				}
				pobjResult.tableData[0].data =
						larrTableFields.toArray(new InsurancePolicy.TableSection.TableField[larrTableFields.size()]);
			}

			larrExtraFields = new ArrayList<InsurancePolicy.ExtraField>();
			for ( i = 0; i < marrValues.size(); i++ )
			{
				if ( marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mrefField.mlngColIndex >= 0) ||
						(marrValues.get(i).mlngObject >= 0) || (marrValues.get(i).mlngExercise >= 0) )
					continue;

				lobjExtraField = new InsurancePolicy.ExtraField();
				lobjExtraField.fieldId = marrValues.get(i).midField.toString();
				lobjExtraField.fieldName = marrValues.get(i).mrefField.mstrLabel;
				lobjExtraField.type = GetFieldTypeByID(marrValues.get(i).mrefField.midType);
				lobjExtraField.unitsLabel = marrValues.get(i).mrefField.mstrUnits;
				lobjExtraField.refersToId = ( marrValues.get(i).mrefField.midRefersTo == null ? null :
						marrValues.get(i).mrefField.midRefersTo.toString() );
				lobjExtraField.value = marrValues.get(i).mstrValue;
				lobjExtraField.order = marrValues.get(i).mrefField.mlngColIndex;
				lobjExtraField.coverageId = marrValues.get(i).mrefCoverage.midCoverage.toString();
				larrExtraFields.add(lobjExtraField);
			}
			pobjResult.extraData = larrExtraFields.toArray(new InsurancePolicy.ExtraField[larrExtraFields.size()]);

			java.util.Arrays.sort(pobjResult.headerFields, new Comparator<InsurancePolicy.HeaderField>()
			{
				public int compare(InsurancePolicy.HeaderField o1, InsurancePolicy.HeaderField o2)
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
			java.util.Arrays.sort(pobjResult.coverages, new Comparator<InsurancePolicy.Coverage>()
			{
				public int compare(InsurancePolicy.Coverage o1, InsurancePolicy.Coverage o2)
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
			java.util.Arrays.sort(pobjResult.extraData, new Comparator<InsurancePolicy.ExtraField>()
			{
				public int compare(InsurancePolicy.ExtraField o1, InsurancePolicy.ExtraField o2)
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
		}

		public void WritePage(InsurancePolicy.TableSection pobjResult, int plngObject, int plngExercise)
			throws CorruptedPadException
		{
			int llngCoverages;
			int llngColumns;
			ArrayList<InsurancePolicy.TableSection.TableField> larrTableFields;
			InsurancePolicy.TableSection.TableField lobjTableField;
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			pobjResult.pageId = mid.toString() + ":" + Integer.toString(plngObject) + ":" + Integer.toString(plngExercise);

			llngCoverages = 0;
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				if ( marrCoverages.get(i).mbIsHeader )
					continue;
				llngCoverages++;
			}

			llngColumns = 0;
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				if ( marrCoverages.get(i).mbIsHeader )
					continue;
				for ( j = 0; j < marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrCoverages.get(i).marrFields[j].mlngColIndex < 0 )
						continue;
					llngColumns++;
				}
				break;
			}

			if ( llngCoverages * llngColumns == 0 )
				pobjResult.data = new InsurancePolicy.TableSection.TableField[0];
			else
			{
				larrTableFields = new ArrayList<InsurancePolicy.TableSection.TableField>();
				for ( i = 0; i < marrValues.size(); i++ )
				{
					if ( marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mrefField.mlngColIndex < 0) ||
							(marrValues.get(i).mlngObject != plngObject) || (marrValues.get(i).mlngExercise != plngExercise) )
						continue;

					lobjTableField = new InsurancePolicy.TableSection.TableField();
					lobjTableField.fieldId = marrValues.get(i).midField.toString();
					lobjTableField.coverageId = marrValues.get(i).mrefCoverage.midCoverage.toString();
					lobjTableField.columnIndex = marrValues.get(i).mrefField.mlngColIndex;
					lobjTableField.value = marrValues.get(i).mstrValue;
					larrTableFields.add(lobjTableField);
				}
				pobjResult.data = larrTableFields.toArray(new InsurancePolicy.TableSection.TableField[larrTableFields.size()]);
			}
		}

		public TipifiedListItem[] GetObjects()
			throws CorruptedPadException
		{
			ArrayList<TipifiedListItem> larrResult;
			TipifiedListItem lobjItem;
			int i;

			if ( !mbValid )
				return new TipifiedListItem[0];

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

		public void WriteObject(InsuredObject pobjResult, int plngObject)
			throws BigBangException, CorruptedPadException
		{
			PolicyObjectData lobjObject;
			ObjectBase lobjZipCode;
			int i, j, k;
			ArrayList<PadExercise> larrExercises;
			PadExercise[] larrSortedExercises;
			final Hashtable<Integer, Integer> larrExerciseMap;
			ArrayList<PadCoverage> larrCoverages;
			PadCoverage[] larrSortedCoverages;
			PadCoverage lobjHeaderCoverage;
			Hashtable<UUID, ArrayList<InsuredObject.CoverageData.FixedField>> larrFixed;
			Hashtable<UUID, ArrayList<InsuredObject.CoverageData.VariableField>> larrVarRef;
			Hashtable<UUID, ArrayList<PadValue>> larrVariable;
			InsuredObject.CoverageData.FixedField lobjFixed;
			InsuredObject.CoverageData.VariableField lobjVariable;
			PadValue lobjValue;
			ArrayList<InsuredObject.CoverageData.FixedField> larrAuxFixed;
			ArrayList<InsuredObject.CoverageData.VariableField> larrAuxVarRef;
			ArrayList<PadValue> larrAuxVariable;
			PadValue[] larrSortedVariable;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjObject = marrObjects.get(plngObject);
			pobjResult.id = mid.toString() + ":" + Integer.toString(plngObject);
//			pobjResult.ownerId = ( mobjPolicy.mid == null ? mid.toString() : mobjPolicy.mid.toString() );
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
			pobjResult.inclusionDate = ( lobjObject.mdtInclusion == null ? null :
					lobjObject.mdtInclusion.toString().substring(0, 10) ); 
			pobjResult.exclusionDate = ( lobjObject.mdtExclusion == null ? null :
				lobjObject.mdtExclusion.toString().substring(0, 10) );

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

			larrExercises = new ArrayList<PadExercise>();
			for ( i = 0; i < marrExercises.size(); i++ )
			{
				if ( marrExercises.get(i).mbDeleted )
					continue;
				marrExercises.get(i).mlngOrigIndex = i;
				larrExercises.add(marrExercises.get(i));
			}
			larrSortedExercises = larrExercises.toArray(new PadExercise[larrExercises.size()]);
			java.util.Arrays.sort(larrSortedExercises, new Comparator<PadExercise>()
			{
				public int compare(PadExercise o1, PadExercise o2)
				{
					if ( o1.mdtStart == null )
					{
						if ( o2.mdtStart == null )
							return 0;
						return 1;
					}
					if ( o2.mdtStart == null )
						return -1;

					return o1.mdtStart.compareTo(o2.mdtStart);
				}
			});
			pobjResult.exercises = new InsuredObject.Exercise[larrSortedExercises.length];
			larrExerciseMap = new Hashtable<Integer, Integer>();
			for ( i = 0; i < larrSortedExercises.length; i++ )
			{
				pobjResult.exercises[i] = new InsuredObject.Exercise();
				pobjResult.exercises[i].id = mid.toString() + ":" + Integer.toString(larrSortedExercises[i].mlngOrigIndex);
				pobjResult.exercises[i].label = larrSortedExercises[i].mstrLabel;
				larrExerciseMap.put(larrSortedExercises[i].mlngOrigIndex, i);
			}

			lobjHeaderCoverage = null;
			larrCoverages = new ArrayList<PadCoverage>();
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				if ( marrCoverages.get(i).mbIsHeader )
					lobjHeaderCoverage = marrCoverages.get(i);
				else
					larrCoverages.add(marrCoverages.get(i));
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
			pobjResult.coverageData = new InsuredObject.CoverageData[larrSortedCoverages.length];
			larrFixed = new Hashtable<UUID, ArrayList<InsuredObject.CoverageData.FixedField>>();
			larrVarRef = new Hashtable<UUID, ArrayList<InsuredObject.CoverageData.VariableField>>();
			for ( i = 0; i < larrSortedCoverages.length; i++ )
			{
				pobjResult.coverageData[i] = new InsuredObject.CoverageData();
				pobjResult.coverageData[i].coverageId = larrSortedCoverages[i].midCoverage.toString();
				try
				{
					pobjResult.coverageData[i].coverageLabel = Coverage.GetInstance(Engine.getCurrentNameSpace(),
							larrSortedCoverages[i].midCoverage).getLabel();
				}
				catch (Throwable e)
				{
					pobjResult.coverageData[i].coverageLabel = "(Erro a obter o nome da cobertura.)";
				}
				larrFixed.put(larrSortedCoverages[i].midCoverage, new ArrayList<InsuredObject.CoverageData.FixedField>());
				larrVarRef.put(larrSortedCoverages[i].midCoverage, new ArrayList<InsuredObject.CoverageData.VariableField>());
			}
			if ( lobjHeaderCoverage != null )
			{
				pobjResult.headerData = new InsuredObject.HeaderData();
				larrFixed.put(lobjHeaderCoverage.midCoverage, new ArrayList<InsuredObject.CoverageData.FixedField>());
				larrVarRef.put(lobjHeaderCoverage.midCoverage, new ArrayList<InsuredObject.CoverageData.VariableField>());
			}

			larrVariable = new Hashtable<UUID, ArrayList<PadValue>>();
			for ( i = 0; i < marrValues.size(); i++ )
			{
				lobjValue = marrValues.get(i);
				if ( lobjValue.mbDeleted || (lobjValue.mlngObject != plngObject) )
					continue;

				if ( lobjValue.mrefField.mbVariesByExercise )
				{
					if ( larrVariable.get(lobjValue.mrefField.midField) == null )
					{
						lobjVariable = new InsuredObject.CoverageData.VariableField();
						lobjVariable.fieldId = lobjValue.mrefField.midField.toString();
						lobjVariable.fieldName = lobjValue.mrefField.mstrLabel;
						lobjVariable.type = GetFieldTypeByID(lobjValue.mrefField.midType);
						lobjVariable.unitsLabel = lobjValue.mrefField.mstrUnits;
						lobjVariable.refersToId = ( lobjValue.mrefField.midRefersTo == null ? null :
							lobjValue.mrefField.midRefersTo.toString() );
						lobjVariable.columnIndex = lobjValue.mrefField.mlngColIndex;
						lobjVariable.data = new InsuredObject.CoverageData.VariableField.VariableValue[pobjResult.exercises.length];
						larrVarRef.get(lobjValue.mrefCoverage.midCoverage).add(lobjVariable);
						larrVariable.put(lobjValue.mrefField.midField, new ArrayList<PadValue>());
					}
					larrVariable.get(lobjValue.mrefField.midField).add(lobjValue);
				}
				else
				{
					lobjFixed = new InsuredObject.CoverageData.FixedField();
					lobjFixed.fieldId = lobjValue.mrefField.midField.toString();
					lobjFixed.fieldName = lobjValue.mrefField.mstrLabel;
					lobjFixed.type = GetFieldTypeByID(lobjValue.mrefField.midType);
					lobjFixed.unitsLabel = lobjValue.mrefField.mstrUnits;
					lobjFixed.refersToId = ( lobjValue.mrefField.midRefersTo == null ? null :
							lobjValue.mrefField.midRefersTo.toString() );
					lobjFixed.columnIndex = lobjValue.mrefField.mlngColIndex;
					lobjFixed.value = lobjValue.mstrValue;
					larrFixed.get(lobjValue.mrefCoverage.midCoverage).add(lobjFixed);
				}
			}

			for ( i = 0; i < pobjResult.coverageData.length; i++ )
			{
				larrAuxFixed = larrFixed.get(UUID.fromString(pobjResult.coverageData[i].coverageId));
				pobjResult.coverageData[i].fixedFields =
						larrAuxFixed.toArray(new InsuredObject.CoverageData.FixedField[larrAuxFixed.size()]);
				java.util.Arrays.sort(pobjResult.coverageData[i].fixedFields,
						new Comparator<InsuredObject.CoverageData.FixedField>()
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

				larrAuxVarRef = larrVarRef.get(UUID.fromString(pobjResult.coverageData[i].coverageId));
				pobjResult.coverageData[i].variableFields =
						larrAuxVarRef.toArray(new InsuredObject.CoverageData.VariableField[larrAuxVarRef.size()]);
				java.util.Arrays.sort(pobjResult.coverageData[i].variableFields,
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

				for ( j = 0; j < pobjResult.coverageData[i].variableFields.length; j++ )
				{
					larrAuxVariable = larrVariable.get(UUID.fromString(pobjResult.coverageData[i].variableFields[j].fieldId));
					larrSortedVariable = larrAuxVariable.toArray(new PadValue[larrAuxVariable.size()]);
					java.util.Arrays.sort(larrSortedVariable, new Comparator<PadValue>()
					{
						public int compare(PadValue o1, PadValue o2)
						{
							return larrExerciseMap.get(o1.mlngExercise) - larrExerciseMap.get(o2.mlngExercise);
						}
					});
					for ( k = 0; k < larrSortedVariable.length; k++ )
					{
						pobjResult.coverageData[i].variableFields[j].data[k] =
								new InsuredObject.HeaderData.VariableField.VariableValue();
						pobjResult.coverageData[i].variableFields[j].data[k].exerciseIndex =
								larrExerciseMap.get(larrSortedVariable[k].mlngExercise);
						pobjResult.coverageData[i].variableFields[j].data[k].value = larrSortedVariable[k].mstrValue;
					}
				}
			}
			if ( lobjHeaderCoverage != null )
			{
				larrAuxFixed = larrFixed.get(lobjHeaderCoverage.midCoverage);
				pobjResult.headerData.fixedFields =
						larrAuxFixed.toArray(new InsuredObject.CoverageData.FixedField[larrAuxFixed.size()]);
				java.util.Arrays.sort(pobjResult.headerData.fixedFields,
						new Comparator<InsuredObject.CoverageData.FixedField>()
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

				larrAuxVarRef = larrVarRef.get(lobjHeaderCoverage.midCoverage);
				pobjResult.headerData.variableFields =
						larrAuxVarRef.toArray(new InsuredObject.CoverageData.VariableField[larrAuxVarRef.size()]);
				java.util.Arrays.sort(pobjResult.headerData.variableFields,
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

				for ( i = 0; i < pobjResult.headerData.variableFields.length; i++ )
				{
					larrAuxVariable = larrVariable.get(UUID.fromString(pobjResult.headerData.variableFields[i].fieldId));
					larrSortedVariable = larrAuxVariable.toArray(new PadValue[larrAuxVariable.size()]);
					java.util.Arrays.sort(larrSortedVariable, new Comparator<PadValue>()
					{
						public int compare(PadValue o1, PadValue o2)
						{
							return larrExerciseMap.get(o1.mlngExercise) - larrExerciseMap.get(o2.mlngExercise);
						}
					});
					for ( j = 0; j < larrSortedVariable.length; j++ )
					{
						pobjResult.headerData.variableFields[i].data[j] = new InsuredObject.HeaderData.VariableField.VariableValue();
						pobjResult.headerData.variableFields[i].data[j].exerciseIndex =
								larrExerciseMap.get(larrSortedVariable[j].mlngExercise);
						pobjResult.headerData.variableFields[i].data[j].value = larrSortedVariable[j].mstrValue;
					}
				}
			}
		}

		public TipifiedListItem[] GetExercises()
			throws CorruptedPadException
		{
			ArrayList<TipifiedListItem> larrResult;
			TipifiedListItem lobjItem;
			int i;

			if ( !mbValid )
				return new TipifiedListItem[0];

			larrResult = new ArrayList<TipifiedListItem>();
			for ( i = 0; i < marrExercises.size(); i++ )
			{
				if ( marrExercises.get(i).mbDeleted )
					continue;
				lobjItem = new TipifiedListItem();
				lobjItem.id = mid + ":" + i;
				lobjItem.value = marrExercises.get(i).mstrLabel;
				larrResult.add(lobjItem);

			}
			return larrResult.toArray(new TipifiedListItem[larrResult.size()]);
		}

		public void WriteExercise(Exercise pobjResult, int plngExercise)
			throws CorruptedPadException
		{
			PolicyExerciseData lobjExercise;
			int i, j, k;
			ArrayList<PadObject> larrObjects;
			PadObject[] larrSortedObjects;
			final Hashtable<Integer, Integer> larrObjectMap;
			ArrayList<PadCoverage> larrCoverages;
			PadCoverage[] larrSortedCoverages;
			PadCoverage lobjHeaderCoverage;
			Hashtable<UUID, ArrayList<Exercise.CoverageData.FixedField>> larrFixed;
			Hashtable<UUID, ArrayList<Exercise.CoverageData.VariableField>> larrVarRef;
			Hashtable<UUID, ArrayList<PadValue>> larrVariable;
			Exercise.CoverageData.FixedField lobjFixed;
			Exercise.CoverageData.VariableField lobjVariable;
			PadValue lobjValue;
			ArrayList<Exercise.CoverageData.FixedField> larrAuxFixed;
			ArrayList<Exercise.CoverageData.VariableField> larrAuxVarRef;
			ArrayList<PadValue> larrAuxVariable;
			PadValue[] larrSortedVariable;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjExercise = marrExercises.get(plngExercise);
			pobjResult.id = mid.toString() + ":" + Integer.toString(plngExercise);
			pobjResult.label = lobjExercise.mstrLabel;
//			pobjResult.ownerId = ( mobjPolicy.mid == null ? mid.toString() : mobjPolicy.mid.toString() );
			pobjResult.ownerId = mid.toString();
			pobjResult.startDate = ( lobjExercise.mdtStart == null ? null : lobjExercise.mdtStart.toString().substring(0, 10) );
			pobjResult.endDate = ( lobjExercise.mdtEnd == null ? null : lobjExercise.mdtEnd.toString().substring(0, 10) );

			larrObjects = new ArrayList<PadObject>();
			for ( i = 0; i < marrObjects.size(); i++ )
			{
				if ( marrObjects.get(i).mbDeleted )
					continue;
				marrObjects.get(i).mlngOrigIndex = i;
				larrObjects.add(marrObjects.get(i));
			}
			larrSortedObjects = larrObjects.toArray(new PadObject[larrObjects.size()]);
			java.util.Arrays.sort(larrSortedObjects, new Comparator<PadObject>()
			{
				public int compare(PadObject o1, PadObject o2)
				{
					return o1.mstrName.compareTo(o2.mstrName);
				}
			});
			pobjResult.objects = new Exercise.InsuredObject[larrSortedObjects.length];
			larrObjectMap = new Hashtable<Integer, Integer>();
			for ( i = 0; i < larrSortedObjects.length; i++ )
			{
				pobjResult.objects[i] = new Exercise.InsuredObject();
				pobjResult.objects[i].id = mid.toString() + ":" + Integer.toString(larrSortedObjects[i].mlngOrigIndex);
				pobjResult.objects[i].label = larrSortedObjects[i].mstrName;
				larrObjectMap.put(larrSortedObjects[i].mlngOrigIndex, i);
			}

			lobjHeaderCoverage = null;
			larrCoverages = new ArrayList<PadCoverage>();
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				if ( marrCoverages.get(i).mbIsHeader )
					lobjHeaderCoverage = marrCoverages.get(i);
				else
					larrCoverages.add(marrCoverages.get(i));
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
			pobjResult.coverageData = new Exercise.CoverageData[larrSortedCoverages.length];
			larrFixed = new Hashtable<UUID, ArrayList<Exercise.CoverageData.FixedField>>();
			larrVarRef = new Hashtable<UUID, ArrayList<Exercise.CoverageData.VariableField>>();
			for ( i = 0; i < larrSortedCoverages.length; i++ )
			{
				pobjResult.coverageData[i] = new Exercise.CoverageData();
				pobjResult.coverageData[i].coverageId = larrSortedCoverages[i].midCoverage.toString();
				try
				{
					pobjResult.coverageData[i].coverageLabel = Coverage.GetInstance(Engine.getCurrentNameSpace(),
							larrSortedCoverages[i].midCoverage).getLabel();
				}
				catch (Throwable e)
				{
					pobjResult.coverageData[i].coverageLabel = "(Erro a obter o nome da cobertura.)";
				}
				larrFixed.put(larrSortedCoverages[i].midCoverage, new ArrayList<Exercise.CoverageData.FixedField>());
				larrVarRef.put(larrSortedCoverages[i].midCoverage, new ArrayList<Exercise.CoverageData.VariableField>());
			}
			if ( lobjHeaderCoverage != null )
			{
				pobjResult.headerData = new Exercise.HeaderData();
				larrFixed.put(lobjHeaderCoverage.midCoverage, new ArrayList<Exercise.CoverageData.FixedField>());
				larrVarRef.put(lobjHeaderCoverage.midCoverage, new ArrayList<Exercise.CoverageData.VariableField>());
			}

			larrVariable = new Hashtable<UUID, ArrayList<PadValue>>();
			for ( i = 0; i < marrValues.size(); i++ )
			{
				lobjValue = marrValues.get(i);
				if ( lobjValue.mbDeleted || (lobjValue.mlngExercise != plngExercise) )
					continue;

				if ( lobjValue.mrefField.mbVariesByObject )
				{
					if ( larrVariable.get(lobjValue.mrefField.midField) == null )
					{
						lobjVariable = new Exercise.CoverageData.VariableField();
						lobjVariable.fieldId = lobjValue.mrefField.midField.toString();
						lobjVariable.fieldName = lobjValue.mrefField.mstrLabel;
						lobjVariable.type = GetFieldTypeByID(lobjValue.mrefField.midType);
						lobjVariable.unitsLabel = lobjValue.mrefField.mstrUnits;
						lobjVariable.refersToId = ( lobjValue.mrefField.midRefersTo == null ? null :
							lobjValue.mrefField.midRefersTo.toString() );
						lobjVariable.columnIndex = lobjValue.mrefField.mlngColIndex;
						lobjVariable.data = new Exercise.CoverageData.VariableField.VariableValue[pobjResult.objects.length];
						larrVarRef.get(lobjValue.mrefCoverage.midCoverage).add(lobjVariable);
						larrVariable.put(lobjValue.mrefField.midField, new ArrayList<PadValue>());
					}
					larrVariable.get(lobjValue.mrefField.midField).add(lobjValue);
				}
				else
				{
					lobjFixed = new Exercise.CoverageData.FixedField();
					lobjFixed.fieldId = lobjValue.mrefField.midField.toString();
					lobjFixed.fieldName = lobjValue.mrefField.mstrLabel;
					lobjFixed.type = GetFieldTypeByID(lobjValue.mrefField.midType);
					lobjFixed.unitsLabel = lobjValue.mrefField.mstrUnits;
					lobjFixed.refersToId = ( lobjValue.mrefField.midRefersTo == null ? null :
							lobjValue.mrefField.midRefersTo.toString() );
					lobjFixed.columnIndex = lobjValue.mrefField.mlngColIndex;
					lobjFixed.value = lobjValue.mstrValue;
					larrFixed.get(lobjValue.mrefCoverage.midCoverage).add(lobjFixed);
				}
			}

			for ( i = 0; i < pobjResult.coverageData.length; i++ )
			{
				larrAuxFixed = larrFixed.get(UUID.fromString(pobjResult.coverageData[i].coverageId));
				pobjResult.coverageData[i].fixedFields =
						larrAuxFixed.toArray(new Exercise.CoverageData.FixedField[larrAuxFixed.size()]);
				java.util.Arrays.sort(pobjResult.coverageData[i].fixedFields,
						new Comparator<Exercise.CoverageData.FixedField>()
				{
					public int compare(Exercise.CoverageData.FixedField o1, Exercise.CoverageData.FixedField o2)
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

				larrAuxVarRef = larrVarRef.get(UUID.fromString(pobjResult.coverageData[i].coverageId));
				pobjResult.coverageData[i].variableFields =
						larrAuxVarRef.toArray(new Exercise.CoverageData.VariableField[larrAuxVarRef.size()]);
				java.util.Arrays.sort(pobjResult.coverageData[i].variableFields,
						new Comparator<Exercise.CoverageData.VariableField>()
				{
					public int compare(Exercise.CoverageData.VariableField o1, Exercise.CoverageData.VariableField o2)
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

				for ( j = 0; j < pobjResult.coverageData[i].variableFields.length; j++ )
				{
					larrAuxVariable = larrVariable.get(UUID.fromString(pobjResult.coverageData[i].variableFields[j].fieldId));
					larrSortedVariable = larrAuxVariable.toArray(new PadValue[larrAuxVariable.size()]);
					java.util.Arrays.sort(larrSortedVariable, new Comparator<PadValue>()
					{
						public int compare(PadValue o1, PadValue o2)
						{
							return larrObjectMap.get(o1.mlngObject) - larrObjectMap.get(o2.mlngObject);
						}
					});
					for ( k = 0; k < larrSortedVariable.length; k++ )
					{
						pobjResult.coverageData[i].variableFields[j].data[k] =
								new Exercise.HeaderData.VariableField.VariableValue(); 
						pobjResult.coverageData[i].variableFields[j].data[k].objectIndex =
								larrObjectMap.get(larrSortedVariable[k].mlngObject);
						pobjResult.coverageData[i].variableFields[j].data[k].value = larrSortedVariable[k].mstrValue;
					}
				}
			}
			if ( lobjHeaderCoverage != null )
			{
				larrAuxFixed = larrFixed.get(lobjHeaderCoverage.midCoverage);
				pobjResult.headerData.fixedFields =
						larrAuxFixed.toArray(new Exercise.CoverageData.FixedField[larrAuxFixed.size()]);
				java.util.Arrays.sort(pobjResult.headerData.fixedFields,
						new Comparator<Exercise.CoverageData.FixedField>()
				{
					public int compare(Exercise.CoverageData.FixedField o1, Exercise.CoverageData.FixedField o2)
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

				larrAuxVarRef = larrVarRef.get(lobjHeaderCoverage.midCoverage);
				pobjResult.headerData.variableFields =
						larrAuxVarRef.toArray(new Exercise.CoverageData.VariableField[larrAuxVarRef.size()]);
				java.util.Arrays.sort(pobjResult.headerData.variableFields,
						new Comparator<Exercise.CoverageData.VariableField>()
				{
					public int compare(Exercise.CoverageData.VariableField o1, Exercise.CoverageData.VariableField o2)
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

				for ( i = 0; i < pobjResult.headerData.variableFields.length; i++ )
				{
					larrAuxVariable = larrVariable.get(UUID.fromString(pobjResult.headerData.variableFields[i].fieldId));
					larrSortedVariable = larrAuxVariable.toArray(new PadValue[larrAuxVariable.size()]);
					java.util.Arrays.sort(larrSortedVariable, new Comparator<PadValue>()
					{
						public int compare(PadValue o1, PadValue o2)
						{
							return larrObjectMap.get(o1.mlngObject) - larrObjectMap.get(o2.mlngObject);
						}
					});
					for ( j = 0; j < larrSortedVariable.length; j++ )
					{
						pobjResult.headerData.variableFields[i].data[j] = new Exercise.HeaderData.VariableField.VariableValue();
						pobjResult.headerData.variableFields[i].data[j].objectIndex =
								larrObjectMap.get(larrSortedVariable[j].mlngObject);
						pobjResult.headerData.variableFields[i].data[j].value = larrSortedVariable[j].mstrValue;
					}
				}
			}
		}

		public void UpdateInvariants(InsurancePolicy pobjSource)
			throws BigBangException, CorruptedPadException
		{
			Hashtable<UUID, PadCoInsurer> lmapCoInsurers;
			UUID lidCompany;
			PadCoInsurer lobjCoInsurer;
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( !mobjPolicy.midSubLine.equals(UUID.fromString(pobjSource.subLineId)) )
				throw new BigBangException("Erro: Não pode alterar a modalidade da apólice.");

			midClient = ( pobjSource.clientId == null ? null : UUID.fromString(pobjSource.clientId) );
			mobjPolicy.mstrNumber = pobjSource.number;
			mobjPolicy.midCompany = UUID.fromString(pobjSource.insuranceAgencyId);
			mobjPolicy.mdtBeginDate = ( pobjSource.startDate == null ? null :
					Timestamp.valueOf(pobjSource.startDate + " 00:00:00.0") );
			mobjPolicy.midDuration = UUID.fromString(pobjSource.durationId);
			mobjPolicy.midFractioning = UUID.fromString(pobjSource.fractioningId);
			mobjPolicy.mlngMaturityDay = pobjSource.maturityDay;
			mobjPolicy.mlngMaturityMonth = pobjSource.maturityMonth;
			mobjPolicy.mdtEndDate = ( pobjSource.expirationDate == null ? null :
					Timestamp.valueOf(pobjSource.expirationDate + " 00:00:00.0") );
			mobjPolicy.mstrNotes = pobjSource.notes;
			mobjPolicy.midManager = ( pobjSource.managerId == null ? null : UUID.fromString(pobjSource.managerId) );
			mobjPolicy.midMediator = ( pobjSource.mediatorId == null ? null : UUID.fromString(pobjSource.mediatorId) );
			mobjPolicy.mbCaseStudy = pobjSource.caseStudy;
			mobjPolicy.mdblPremium = ( pobjSource.premium == null ? null : new BigDecimal(pobjSource.premium+"") );
			mobjPolicy.mdblRetrocession = ( pobjSource.agentPercentage == null ? null : new BigDecimal(pobjSource.agentPercentage+"") );
			mobjPolicy.midProfile = ( pobjSource.operationalProfileId == null ? null : UUID.fromString(pobjSource.operationalProfileId) );

			mobjPolicy.mbModified = true;

			mbValid = false;

			j = -1;
			lmapCoInsurers = new Hashtable<UUID, PadCoInsurer>();
			if ( pobjSource.coInsurers != null )
			{
				for ( i = 0; i < pobjSource.coInsurers.length; i++ )
				{
					lidCompany = UUID.fromString(pobjSource.coInsurers[i].insuranceAgencyId);
					j = FindCoInsurer(lidCompany, j + 1);
					if ( j < 0 )
					{
						lobjCoInsurer = new PadCoInsurer();
						lobjCoInsurer.midPolicy = mobjPolicy.mid;
						lobjCoInsurer.midCompany = lidCompany;
						lobjCoInsurer.mdblPercent = new BigDecimal(pobjSource.coInsurers[i].percent+"");
						marrCoInsurers.add(lobjCoInsurer);
					}
					else
					{
						lobjCoInsurer = marrCoInsurers.get(j);
						lobjCoInsurer.mdblPercent = new BigDecimal(pobjSource.coInsurers[i].percent+"");
						lobjCoInsurer.mbDeleted = false;
					}
					lmapCoInsurers.put(lidCompany, lobjCoInsurer);
				}
			}
			for ( i = 0; i < marrCoInsurers.size(); i++ )
			{
				if ( lmapCoInsurers.get(marrCoInsurers.get(i).midCompany) == null )
					marrCoInsurers.get(i).mbDeleted = true;
			}

			j = -1;
			if ( pobjSource.coverages != null )
			{
				for ( i = 0; i < pobjSource.coverages.length; i++ )
				{
					j = FindCoverage(UUID.fromString(pobjSource.coverages[i].coverageId), j + 1);
					if ( j < 0 )
						throw new BigBangException("Inesperado: Cobertura não existente do lado do servidor.");
					marrCoverages.get(j).mbPresent = pobjSource.coverages[i].presentInPolicy;
				}
			}

			j = -1;
			if ( pobjSource.headerFields != null )
			{
				for ( i = 0; i < pobjSource.headerFields.length; i++ )
				{
					j = FindValue(UUID.fromString(pobjSource.headerFields[i].fieldId), -1, -1, j + 1);
					if ( j < 0 )
						throw new BigBangException("Inesperado: Valor de cabeçalho não existente do lado do servidor.");
					marrValues.get(j).mstrValue = ( "".equals(pobjSource.headerFields[i].value) ? null :
							pobjSource.headerFields[i].value );
				}
			}
			if ( pobjSource.extraData != null )
			{
				for ( i = 0; i < pobjSource.extraData.length; i++ )
				{
					j = FindValue(UUID.fromString(pobjSource.extraData[i].fieldId), -1, -1, j + 1);
					if ( j < 0 )
						throw new BigBangException("Inesperado: Valor de cabeçalho não existente do lado do servidor.");
					marrValues.get(j).mstrValue = ( "".equals(pobjSource.extraData[i].value) ? null :
							pobjSource.extraData[i].value );
				}
			}

			mbValid = true;
		}

		public void UpdatePage(InsurancePolicy.TableSection pobjSource, int plngObject, int plngExercise)
			throws BigBangException, CorruptedPadException
		{
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( pobjSource.data == null )
				return;

			mbValid = false;

			j = -1;
			for ( i = 0; i < pobjSource.data.length; i++ )
			{
				j = FindValue(UUID.fromString(pobjSource.data[i].fieldId), plngObject, plngExercise, j + 1);
				if ( j < 0 )
					throw new BigBangException("Inesperado: Valor de tabela não existente do lado do servidor.");
				marrValues.get(j).mstrValue = ( "".equals(pobjSource.data[i].value) ? null : pobjSource.data[i].value );
			}

			mbValid = true;
		}

		public int CreateNewObject()
			throws BigBangException, CorruptedPadException
		{
			PadObject lobjObject;
			PadValue lobjValue;
			int i, j, k;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

//			if ( !Constants.StatusID_InProgress.equals(mobjPolicy.midStatus) )
//				throw new BigBangException("Erro: Operação não suportada para apólices já validadas.");

			lobjObject = new PadObject();
			try
			{
				lobjObject.midType = (UUID)SubLine.GetInstance(Engine.getCurrentNameSpace(), mobjPolicy.midSubLine).getAt(2);
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			mbValid = false;

			int llngIndex = marrObjects.size();
			marrObjects.add(lobjObject);
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				for ( j = 0; j < marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrCoverages.get(i).marrFields[j].mbVariesByObject )
					{
						if ( !marrCoverages.get(i).marrFields[j].mbVariesByExercise )
						{
							lobjValue = new PadValue();
							lobjValue.mid = null;
							lobjValue.mstrValue = marrCoverages.get(i).marrFields[j].mstrDefault;
							lobjValue.midOwner = null;
							lobjValue.midField = marrCoverages.get(i).marrFields[j].midField;
							lobjValue.midObject = null;
							lobjValue.midExercise = null;
							lobjValue.mrefCoverage = marrCoverages.get(i);
							lobjValue.mrefField = marrCoverages.get(i).marrFields[j];
							lobjValue.mlngObject = llngIndex;
							lobjValue.mlngExercise = -1;
							marrValues.add(lobjValue);
						}
						else
						{
							for ( k = 0; k < marrExercises.size(); k++ )
							{
								if ( marrExercises.get(k).mbDeleted )
									continue;

								lobjValue = new PadValue();
								lobjValue.mid = null;
								lobjValue.mstrValue = marrCoverages.get(i).marrFields[j].mstrDefault;
								lobjValue.midOwner = null;
								lobjValue.midField = marrCoverages.get(i).marrFields[j].midField;
								lobjValue.midObject = null;
								lobjValue.midExercise = null;
								lobjValue.mrefCoverage = marrCoverages.get(i);
								lobjValue.mrefField = marrCoverages.get(i).marrFields[j];
								lobjValue.mlngObject = llngIndex;
								lobjValue.mlngExercise = k;
								marrValues.add(lobjValue);
							}
						}
					}
				}
			}

			mbValid = true;

			return llngIndex;
		}

		public void UpdateObject(InsuredObject pobjSource, int plngObject)
			throws BigBangException, CorruptedPadException
		{
			UUID lidType;
			UUID lidZipCode;
			PadObject lobjObject;
			int i, j, k, l;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( (marrObjects.get(plngObject) == null) || marrObjects.get(plngObject).mbDeleted )
				throw new BigBangException("Erro: Não pode alterar um objecto apagado.");

			try
			{
				lidType = (UUID)SubLine.GetInstance(Engine.getCurrentNameSpace(), mobjPolicy.midSubLine).getAt(2);
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
			lobjObject.midOwner = mobjPolicy.mid;
			lobjObject.midType = lidType;
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
			lobjObject.mdtInclusion = ( pobjSource.inclusionDate == null ? null :
					Timestamp.valueOf(pobjSource.inclusionDate + " 00:00:00.0") );
			lobjObject.mdtExclusion = ( pobjSource.exclusionDate == null ? null :
					Timestamp.valueOf(pobjSource.exclusionDate + " 00:00:00.0") );

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

			l = -1;

			if ( pobjSource.headerData != null )
			{
				for ( i = 0; i < pobjSource.headerData.fixedFields.length; i++ )
				{
					l = FindValue(UUID.fromString(pobjSource.headerData.fixedFields[i].fieldId), plngObject, -1, l + 1);
					if ( l < 0 )
						throw new BigBangException("Inesperado: Valor fixo de objecto não existente do lado do servidor.");
					marrValues.get(l).mstrValue = ( "".equals(pobjSource.headerData.fixedFields[i].value) ? null :
							pobjSource.headerData.fixedFields[i].value );
				}

				for ( i = 0; i < pobjSource.headerData.variableFields.length; i++ )
				{
					for ( j = 0; j < pobjSource.headerData.variableFields[i].data.length; j++ )
					{
						l = FindValue(UUID.fromString(pobjSource.headerData.variableFields[i].fieldId), plngObject,
								Integer.parseInt(pobjSource.exercises[pobjSource.headerData.variableFields[i].data[j]
										.exerciseIndex].id.split(":")[1]), l + 1);
						if ( l < 0 )
							throw new BigBangException("Inesperado: Valor variável de objecto não existente do lado do servidor.");
						marrValues.get(l).mstrValue = ( "".equals(pobjSource.headerData.variableFields[i].data[j].value) ?
								null : pobjSource.headerData.variableFields[i].data[j].value );
					}
				}
			}

			if ( pobjSource.coverageData != null )
			{
				for ( i = 0; i < pobjSource.coverageData.length; i++ )
				{
					for ( j = 0; j < pobjSource.coverageData[i].fixedFields.length; j++ )
					{
						l = FindValue(UUID.fromString(pobjSource.coverageData[i].fixedFields[j].fieldId), plngObject, -1, l + 1);
						if ( l < 0 )
							throw new BigBangException("Inesperado: Valor fixo de objecto não existente do lado do servidor.");
						marrValues.get(l).mstrValue = ( "".equals(pobjSource.coverageData[i].fixedFields[j].value) ? null :
								pobjSource.coverageData[i].fixedFields[j].value );
					}

					for ( j = 0; j < pobjSource.coverageData[i].variableFields.length; j++ )
					{
						for ( k = 0; k < pobjSource.coverageData[i].variableFields[j].data.length; k++ )
						{
							l = FindValue(UUID.fromString(pobjSource.coverageData[i].variableFields[j].fieldId), plngObject,
									Integer.parseInt(pobjSource.exercises[pobjSource.coverageData[i].variableFields[j].data[k]
											.exerciseIndex].id.split(":")[1]), l + 1);
							if ( l < 0 )
								throw new BigBangException("Inesperado: Valor variável de objecto não existente do lado do servidor.");
							marrValues.get(l).mstrValue = ( "".equals(pobjSource.coverageData[i].variableFields[j].data[k].value) ?
									null : pobjSource.coverageData[i].variableFields[j].data[k].value );
						}
					}
				}
			}

			mbValid = true;
		}

		public void DeleteObject(int plngObject)
			throws CorruptedPadException
		{
			int i;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			mbValid = false;

			marrObjects.get(plngObject).mbDeleted = true;
			for ( i = 0; i < marrValues.size(); i++ )
			{
				if ( marrValues.get(i).mlngObject == plngObject )
					marrValues.get(i).mbDeleted = true;
			}

			mbValid = true;
		}

		public int CreateNewExercise()
			throws BigBangException, CorruptedPadException
		{
			PadExercise lobjExercise;
			int llngIndex;
			PadValue lobjValue;
			int i, j, k;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

//			if ( !Constants.StatusID_InProgress.equals(mobjPolicy.midStatus) )
//				throw new BigBangException("Erro: Operação não suportada para apólices já validadas.");

			lobjExercise = new PadExercise();

			mbValid = false;

			llngIndex = marrExercises.size();
			marrExercises.add(lobjExercise);
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				for ( j = 0; j < marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrCoverages.get(i).marrFields[j].mbVariesByExercise )
					{
						if ( !marrCoverages.get(i).marrFields[j].mbVariesByObject )
						{
							lobjValue = new PadValue();
							lobjValue.mid = null;
							lobjValue.mstrValue = marrCoverages.get(i).marrFields[j].mstrDefault;
							lobjValue.midOwner = null;
							lobjValue.midField = marrCoverages.get(i).marrFields[j].midField;
							lobjValue.midObject = null;
							lobjValue.midExercise = null;
							lobjValue.mrefCoverage = marrCoverages.get(i);
							lobjValue.mrefField = marrCoverages.get(i).marrFields[j];
							lobjValue.mlngObject = -1;
							lobjValue.mlngExercise = llngIndex;
							marrValues.add(lobjValue);
						}
						else
						{
							for ( k = 0; k < marrObjects.size(); k++ )
							{
								if ( marrObjects.get(k).mbDeleted )
									continue;

								lobjValue = new PadValue();
								lobjValue.mid = null;
								lobjValue.mstrValue = marrCoverages.get(i).marrFields[j].mstrDefault;
								lobjValue.midOwner = null;
								lobjValue.midField = marrCoverages.get(i).marrFields[j].midField;
								lobjValue.midObject = null;
								lobjValue.midExercise = null;
								lobjValue.mrefCoverage = marrCoverages.get(i);
								lobjValue.mrefField = marrCoverages.get(i).marrFields[j];
								lobjValue.mlngObject = k;
								lobjValue.mlngExercise = llngIndex;
								marrValues.add(lobjValue);
							}
						}
					}
				}
			}

			mbValid = true;

			return llngIndex;
		}

		public void UpdateExercise(Exercise pobjSource, int plngExercise)
			throws BigBangException, CorruptedPadException
		{
			Timestamp ldtStart;
			Timestamp ldtEnd;
			PadExercise lobjExercise;
			int i, j, k, l;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			if ( (marrExercises.get(plngExercise) == null) || marrExercises.get(plngExercise).mbDeleted )
				throw new BigBangException("Erro: Não pode alterar um exercício apagado.");

			try
			{
				ldtStart = ( pobjSource.startDate == null ? null : Timestamp.valueOf(pobjSource.startDate + " 00:00:00.0") );
				ldtEnd = ( pobjSource.endDate == null ? null : Timestamp.valueOf(pobjSource.endDate + " 00:00:00.0") );
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjExercise = marrExercises.get(plngExercise);

			lobjExercise.mstrLabel = pobjSource.label;
			lobjExercise.midOwner = mobjPolicy.mid;
			lobjExercise.mdtStart = ldtStart;
			lobjExercise.mdtEnd = ldtEnd;

			mbValid = false;

			l = -1;

			if ( pobjSource.headerData != null )
			{
				for ( i = 0; i < pobjSource.headerData.fixedFields.length; i++ )
				{
					l = FindValue(UUID.fromString(pobjSource.headerData.fixedFields[i].fieldId), -1, plngExercise, l + 1);
					if ( l < 0 )
						throw new BigBangException("Inesperado: Valor fixo de exercício não existente do lado do servidor.");
					marrValues.get(l).mstrValue = ( "".equals(pobjSource.headerData.fixedFields[i].value) ? null :
							pobjSource.headerData.fixedFields[i].value );
				}

				for ( i = 0; i < pobjSource.headerData.variableFields.length; i++ )
				{
					for ( j = 0; j < pobjSource.headerData.variableFields[i].data.length; j++ )
					{
						l = FindValue(UUID.fromString(pobjSource.headerData.variableFields[i].fieldId),
								Integer.parseInt(pobjSource.objects[pobjSource.headerData.variableFields[i].data[j]
										.objectIndex].id.split(":")[1]), plngExercise, l + 1);
						if ( l < 0 )
							throw new BigBangException("Inesperado: Valor variável de exercício não existente do lado do servidor.");
						marrValues.get(l).mstrValue = ( "".equals(pobjSource.headerData.variableFields[i].data[j].value) ?
								null : pobjSource.headerData.variableFields[i].data[j].value );
					}
				}
			}

			if ( pobjSource.coverageData != null )
			{
				for ( i = 0; i < pobjSource.coverageData.length; i++ )
				{
					for ( j = 0; j < pobjSource.coverageData[i].fixedFields.length; j++ )
					{
						l = FindValue(UUID.fromString(pobjSource.coverageData[i].fixedFields[j].fieldId), -1, plngExercise, l + 1);
						if ( l < 0 )
							throw new BigBangException("Inesperado: Valor fixo de exercício não existente do lado do servidor.");
						marrValues.get(l).mstrValue = ( "".equals(pobjSource.coverageData[i].fixedFields[j].value) ? null :
								pobjSource.coverageData[i].fixedFields[j].value );
					}

					for ( j = 0; j < pobjSource.coverageData[i].variableFields.length; j++ )
					{
						for ( k = 0; k < pobjSource.coverageData[i].variableFields[j].data.length; k++ )
						{
							l = FindValue(UUID.fromString(pobjSource.coverageData[i].variableFields[j].fieldId),
									Integer.parseInt(pobjSource.objects[pobjSource.coverageData[i].variableFields[j].data[k]
											.objectIndex].id.split(":")[1]), plngExercise, l + 1);
							if ( l < 0 )
								throw new BigBangException("Inesperado: Valor variável de exercício não existente do lado do servidor.");
							marrValues.get(l).mstrValue = ( "".equals(pobjSource.coverageData[i].variableFields[j].data[k].value) ?
									null : pobjSource.coverageData[i].variableFields[j].data[k].value );
						}
					}
				}
			}

			mbValid = true;
		}

		public void DeleteExercise(int plngExercise)
			throws CorruptedPadException
		{
			int i;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			mbValid = false;

			marrExercises.get(plngExercise).mbDeleted = true;
			for ( i = 0; i < marrValues.size(); i++ )
			{
				if ( marrValues.get(i).mlngExercise == plngExercise )
					marrValues.get(i).mbDeleted = true;
			}

			mbValid = true;
		}

		public void CommitChanges()
			throws BigBangException, CorruptedPadException 
		{
			PolicyData lobjData;
			int i;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjData = new PolicyData();
			lobjData.Clone(mobjPolicy);

			lobjData.mbModified = mobjPolicy.mbModified;

			if ( marrCoInsurers.size() > 0 )
			{
				lobjData.marrCoInsurers = new PolicyCoInsurerData[marrCoInsurers.size()];
				for ( i = 0; i < marrCoInsurers.size(); i++ )
				{
					lobjData.marrCoInsurers[i] = new PolicyCoInsurerData();
					lobjData.marrCoInsurers[i].Clone(marrCoInsurers.get(i));
					if ( marrCoInsurers.get(i).mbDeleted )
						lobjData.marrCoInsurers[i].mbDeleted = true;
					else if ( marrCoInsurers.get(i).mid == null )
						lobjData.marrCoInsurers[i].mbNew = true;
				}
			}
			else
				lobjData.marrCoInsurers = null;

			if ( marrCoverages.size() > 0 )
			{
				lobjData.marrCoverages = new PolicyCoverageData[marrCoverages.size()];
				for ( i = 0; i < marrCoverages.size(); i++ )
				{
					lobjData.marrCoverages[i] = new PolicyCoverageData();
					lobjData.marrCoverages[i].Clone(marrCoverages.get(i));
					if ( marrCoverages.get(i).mid == null )
						lobjData.marrCoverages[i].mbNew = true;
				}
			}
			else
				lobjData.marrCoverages = null;

			if ( marrObjects.size() > 0 )
			{
				lobjData.marrObjects = new PolicyObjectData[marrObjects.size()];
				for ( i = 0; i < marrObjects.size(); i++ )
				{
					lobjData.marrObjects[i] = new PolicyObjectData();
					lobjData.marrObjects[i].Clone(marrObjects.get(i));
					if ( marrObjects.get(i).mbDeleted )
						lobjData.marrObjects[i].mbDeleted = true;
					else if ( marrObjects.get(i).mid == null )
						lobjData.marrObjects[i].mbNew = true;
				}
			}
			else
				lobjData.marrObjects = null;

			if ( marrExercises.size() > 0 )
			{
				lobjData.marrExercises = new PolicyExerciseData[marrExercises.size()];
				for ( i = 0; i < marrExercises.size(); i++ )
				{
					if ( mbIsNew )
					{
						if ( marrExercises.get(i).mdtStart == null )
							marrExercises.get(i).mdtStart = mobjPolicy.mdtBeginDate;
					}

					lobjData.marrExercises[i] = new PolicyExerciseData();
					lobjData.marrExercises[i].Clone(marrExercises.get(i));
					if ( marrExercises.get(i).mbDeleted )
						lobjData.marrExercises[i].mbDeleted = true;
					else if ( marrExercises.get(i).mid == null )
						lobjData.marrExercises[i].mbNew = true;
				}
			}
			else
				lobjData.marrExercises = null;

			if ( marrValues.size() > 0 )
			{
				lobjData.marrValues = new PolicyValueData[marrValues.size()];
				for ( i = 0; i < marrValues.size(); i++ )
				{
					lobjData.marrValues[i] = new PolicyValueData();
					lobjData.marrValues[i].Clone(marrValues.get(i));
					if ( marrValues.get(i).mbDeleted )
						lobjData.marrValues[i].mbDeleted = true;
					else if ( marrValues.get(i).mid == null )
						lobjData.marrValues[i].mbNew = true;
				}
			}
			else
				lobjData.marrValues = null;

			if ( mobjPolicy.mid == null )
				CommitNew(lobjData);
			else
				CommitEdit(lobjData);
		}

		private int FindCoInsurer(UUID pidCompany, int plngStart)
		{
			int i;

			for ( i = plngStart; i < marrCoInsurers.size(); i++ )
			{
				if ( marrCoInsurers.get(i).midCompany.equals(pidCompany) )
					return i;
			}

			for ( i = 0; i < plngStart; i++ )
			{
				if ( marrCoInsurers.get(i).midCompany.equals(pidCompany) )
					return i;
			}

			return -1;
		}

		private int FindCoverage(UUID pidCoverage, int plngStart)
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

		private int FindValue(UUID pidField, int plngObject, int plngExercise, int plngStart)
		{
			int i;

			for ( i = plngStart; i < marrValues.size(); i++ )
			{
				if ( (marrValues.get(i).midField.equals(pidField)) && (marrValues.get(i).mlngObject == plngObject) &&
						(marrValues.get(i).mlngExercise == plngExercise) )
					return i;
			}

			for ( i = 0; i < plngStart; i++ )
			{
				if ( (marrValues.get(i).midField.equals(pidField)) && (marrValues.get(i).mlngObject == plngObject) &&
						(marrValues.get(i).mlngExercise == plngExercise) )
					return i;
			}

			return -1;
		}

		private void CommitNew(PolicyData pobjData)
			throws BigBangException
		{
			CreatePolicy lopCC;
			int i;

			if ( midClient == null )
				throw new BigBangException("Erro: Não preencheu o identificador do cliente.");

			try
			{
				lopCC = new CreatePolicy(Client.GetInstance(Engine.getCurrentNameSpace(), midClient).GetProcessID());
				lopCC.mobjData = pobjData;

				lopCC.mobjContactOps = null;
				lopCC.mobjDocOps = null;

				lopCC.Execute();

				mobjPolicy.mid = lopCC.mobjData.mid;

				if ( lopCC.mobjData.marrObjects != null )
				{
					for ( i = 0; i < lopCC.mobjData.marrObjects.length; i++ )
						if ( lopCC.mobjData.marrObjects[i].mbNew )
							marrObjects.get(i).mid = lopCC.mobjData.marrObjects[i].mid;
				}

				if ( lopCC.mobjData.marrExercises != null )
				{
					for ( i = 0; i < lopCC.mobjData.marrExercises.length; i++ )
						if ( lopCC.mobjData.marrExercises[i].mbNew )
							marrExercises.get(i).mid = lopCC.mobjData.marrExercises[i].mid;
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}

		private void CommitEdit(PolicyData pobjData)
			throws BigBangException
		{
			ManageData lopMPD;
			int i;

			try
			{
				lopMPD = new ManageData(mobjPolicy.midProcess);
				lopMPD.mobjData = pobjData;

				lopMPD.mobjContactOps = null;
				lopMPD.mobjDocOps = null;

				lopMPD.Execute();

				if ( lopMPD.mobjData.marrObjects != null )
				{
					for ( i = 0; i < lopMPD.mobjData.marrObjects.length; i++ )
						if ( lopMPD.mobjData.marrObjects[i].mbNew )
							marrObjects.get(i).mid = lopMPD.mobjData.marrObjects[i].mid;
				}

				if ( lopMPD.mobjData.marrExercises != null )
				{
					for ( i = 0; i < lopMPD.mobjData.marrExercises.length; i++ )
						if ( lopMPD.mobjData.marrExercises[i].mbNew )
							marrExercises.get(i).mid = lopMPD.mobjData.marrExercises[i].mid;
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Hashtable<UUID, PolicyScratchPad> GetScratchPadStorage()
	{
		Hashtable<UUID, PolicyScratchPad> larrAux;

        if (getSession() == null)
            return null;

        larrAux = (Hashtable<UUID, PolicyScratchPad>)getSession().getAttribute("BigBang_Policy_ScratchPad_Storage");
        if (larrAux == null)
        {
        	larrAux = new Hashtable<UUID, PolicyScratchPad>();
            getSession().setAttribute("BigBang_Policy_ScratchPad_Storage", larrAux);
        }

        return larrAux;
	}

	public static InsurancePolicy sGetPolicy(UUID pidPolicy)
		throws BigBangException
	{
		Policy lobjPolicy;
		InsurancePolicy lobjResult;
		IProcess lobjProc;
		Client lobjClient;
		Mediator lobjMed;
		SubLine lobjSubLine;
		Line lobjLine;
		Category lobjCategory;
		ObjectBase lobjStatus;
		PolicyCoInsurer[] larrCoInsurers;
		PolicyValue[] larrLocalValues;
		PolicyCoverage[] larrLocalCoverages;
		Coverage[] larrCoverages;
		Hashtable<UUID, Tax> larrAuxFields;
		ArrayList<InsurancePolicy.HeaderField> larrOutHeaders;
		ArrayList<InsurancePolicy.TableSection.TableField> larrOutFields;
		ArrayList<InsurancePolicy.ExtraField> larrOutExtras;
		Tax lobjTax;
		InsurancePolicy.HeaderField lobjHeader;
		InsurancePolicy.TableSection.TableField lobjField;
		InsurancePolicy.ExtraField lobjExtra;
		Hashtable<UUID, Coverage> larrAuxCoverages;
		ArrayList<InsurancePolicy.Coverage> larrOutCoverages;
		Hashtable<Integer, InsurancePolicy.ColumnHeader> larrOutColumns;
		Coverage lobjCoverage;
		InsurancePolicy.ColumnHeader lobjColumnHeader;
		Tax[] larrTaxes;
		InsurancePolicy.Coverage lobjAuxCoverage;
		ArrayList<InsurancePolicy.Coverage.Variability> larrVariability;
		InsurancePolicy.Coverage.Variability lobjVariability;
		InsurancePolicy.TableSection lobjSection;
		int i, j;

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), pidPolicy);
			if ( lobjPolicy.GetProcessID() == null )
				throw new BigBangException("Erro: Apólice sem processo de suporte. (Apólice n. "
						+ lobjPolicy.getAt(0).toString() + ")");
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjPolicy.GetProcessID());
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
			lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjClient.getAt(8));
			lobjSubLine = lobjPolicy.GetSubLine();
			lobjLine = lobjSubLine.getLine();
			lobjCategory = lobjLine.getCategory();
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
					(UUID)lobjPolicy.getAt(13));
			larrCoInsurers = lobjPolicy.GetCurrentCoInsurers();
			larrLocalValues = lobjPolicy.GetCurrentKeyedValues(null, null);
			larrLocalCoverages = lobjPolicy.GetCurrentCoverages();
			larrCoverages = lobjSubLine.GetCurrentCoverages();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new InsurancePolicy();

		lobjResult.id = lobjPolicy.getKey().toString();
		lobjResult.number = (String)lobjPolicy.getAt(0);
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.categoryId = lobjCategory.getKey().toString();
		lobjResult.categoryName = lobjCategory.getLabel();
		lobjResult.lineId = lobjLine.getKey().toString();
		lobjResult.lineName = lobjLine.getLabel();
		lobjResult.subLineId = lobjSubLine.getKey().toString();
		lobjResult.subLineName = lobjSubLine.getLabel();
		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.insuranceAgencyId = ((UUID)lobjPolicy.getAt(2)).toString();
		lobjResult.startDate = (lobjPolicy.getAt(4) == null ? null :
				((Timestamp)lobjPolicy.getAt(4)).toString().substring(0, 10));
		lobjResult.durationId = ((UUID)lobjPolicy.getAt(5)).toString();
		lobjResult.fractioningId = ((UUID)lobjPolicy.getAt(6)).toString();
		lobjResult.maturityDay = (lobjPolicy.getAt(7) == null ? 0 : (Integer)lobjPolicy.getAt(7));
		lobjResult.maturityMonth = (lobjPolicy.getAt(8) == null ? 0 : (Integer)lobjPolicy.getAt(8));
		lobjResult.expirationDate = (lobjPolicy.getAt(9) == null ? null :
				((Timestamp)lobjPolicy.getAt(9)).toString().substring(0, 10));
		lobjResult.notes = (String)lobjPolicy.getAt(10);
		lobjResult.mediatorId = (lobjPolicy.getAt(11) == null ? null : ((UUID)lobjPolicy.getAt(11)).toString());
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.caseStudy = (Boolean)lobjPolicy.getAt(12);
		lobjResult.statusId = lobjStatus.getKey().toString();
		lobjResult.statusText = lobjStatus.getLabel();
		switch ( (Integer)lobjStatus.getAt(1) )
		{
		case 0:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
			break;

		case 1:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
			break;

		case 2:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
			break;
		}
		lobjResult.premium = (lobjPolicy.getAt(14) == null ? null : ((BigDecimal)lobjPolicy.getAt(14)).doubleValue());
		lobjResult.agentPercentage = (lobjPolicy.getAt(18) == null ? null : ((BigDecimal)lobjPolicy.getAt(18)).doubleValue());
		lobjResult.operationalProfileId = (lobjPolicy.getAt(19) == null ? null : ((UUID)lobjPolicy.getAt(19)).toString());
		lobjResult.docushare = (String)lobjPolicy.getAt(15);
		lobjResult.managerId = lobjProc.GetManagerID().toString();

		if ( larrCoInsurers.length > 0 )
		{
			lobjResult.coInsurers = new InsurancePolicy.CoInsurer[larrCoInsurers.length];
			for ( i = 0; i < larrCoInsurers.length; i++ )
			{
				lobjResult.coInsurers[i] = new InsurancePolicy.CoInsurer();
				lobjResult.coInsurers[i].insuranceAgencyId = ((UUID)larrCoInsurers[i].getAt(1)).toString();
				lobjResult.coInsurers[i].percent = ((BigDecimal)larrCoInsurers[i].getAt(2)).doubleValue();
			}
		}
		else
			lobjResult.coInsurers = null;

		larrAuxFields = new Hashtable<UUID, Tax>();
		larrOutHeaders = new ArrayList<InsurancePolicy.HeaderField>();
		larrOutFields = new ArrayList<InsurancePolicy.TableSection.TableField>();
		larrOutExtras = new ArrayList<InsurancePolicy.ExtraField>();
		for ( i = 0; i < larrLocalValues.length; i++ )
		{
			if ( (larrLocalValues[i].getAt(3) != null) || (larrLocalValues[i].getAt(4) != null) )
				continue;
			lobjTax = larrLocalValues[i].GetTax();
			if ( !lobjTax.IsVisible() )
				continue;
//			if ( lobjTax.GetVariesByObject() || lobjTax.GetVariesByExercise() )
//				throw new BigBangException("Inesperado: Valor variável marcado como invariante.");

			if ( lobjTax.GetCoverage().IsHeader() )
			{
				lobjHeader = new InsurancePolicy.HeaderField();
				lobjHeader.fieldId = lobjTax.getKey().toString();
				lobjHeader.fieldName = lobjTax.getLabel();
				lobjHeader.type = GetFieldTypeByID((UUID)lobjTax.getAt(2));
				lobjHeader.unitsLabel = (String)lobjTax.getAt(3);
				lobjHeader.refersToId = ( lobjTax.getAt(7) == null ? null : ((UUID)lobjTax.getAt(7)).toString() );
				lobjHeader.value = larrLocalValues[i].getLabel();
				lobjHeader.order = (Integer)lobjTax.getAt(8);
				larrOutHeaders.add(lobjHeader);
			}
			else if ( lobjTax.GetColumnOrder() >= 0 )
			{
				lobjField = new InsurancePolicy.TableSection.TableField();
				lobjField.fieldId = lobjTax.getKey().toString();
				lobjField.coverageId = lobjTax.GetCoverage().getKey().toString();
				lobjField.columnIndex = lobjTax.GetColumnOrder();
				lobjField.value = larrLocalValues[i].getLabel();
				larrOutFields.add(lobjField);
			}
			else
			{
				lobjExtra = new InsurancePolicy.ExtraField();
				lobjExtra.fieldId = lobjTax.getKey().toString();
				lobjExtra.fieldName = lobjTax.getLabel();
				lobjExtra.type = GetFieldTypeByID((UUID)lobjTax.getAt(2));
				lobjExtra.unitsLabel = (String)lobjTax.getAt(3);
				lobjExtra.refersToId = ( lobjTax.getAt(7) == null ? null : ((UUID)lobjTax.getAt(7)).toString() );
				lobjExtra.coverageId = lobjTax.GetCoverage().getKey().toString();
				lobjExtra.value = larrLocalValues[i].getLabel();
				lobjExtra.order = (Integer)lobjTax.getAt(8);
				larrOutExtras.add(lobjExtra);
			}
			larrAuxFields.put(lobjTax.getKey(), lobjTax);
		}

		larrAuxCoverages = new Hashtable<UUID, Coverage>();
		larrOutCoverages = new ArrayList<InsurancePolicy.Coverage>();
		larrOutColumns = new Hashtable<Integer, InsurancePolicy.ColumnHeader>();
		for ( i = 0; i < larrLocalCoverages.length; i++ )
		{
			lobjCoverage = larrLocalCoverages[i].GetCoverage();
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

			lobjAuxCoverage = new InsurancePolicy.Coverage();
			lobjAuxCoverage.coverageId = lobjCoverage.getKey().toString();
			lobjAuxCoverage.coverageName = lobjCoverage.getLabel();
			lobjAuxCoverage.mandatory = (Boolean)lobjCoverage.getAt(2);
			lobjAuxCoverage.order = ( lobjCoverage.getAt(5) == null ? 0 : (Integer)lobjCoverage.getAt(5) );
			lobjAuxCoverage.presentInPolicy = (Boolean)larrLocalCoverages[i].getAt(2);
			larrVariability = new ArrayList<InsurancePolicy.Coverage.Variability>();
			for ( j = 0; j < larrTaxes.length ; j++ )
			{
				if ( larrTaxes[j].GetColumnOrder() < 0 )
					continue;

				if ( !larrOutColumns.containsKey(larrTaxes[j].GetColumnOrder()) )
				{
					lobjColumnHeader = new InsurancePolicy.ColumnHeader();
					lobjColumnHeader.label = larrTaxes[j].getLabel();
					lobjColumnHeader.type = GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
					lobjColumnHeader.unitsLabel = (String)larrTaxes[j].getAt(3);
					lobjColumnHeader.refersToId = ( larrTaxes[j].getAt(7) == null ? null : ((UUID)larrTaxes[j].getAt(7)).toString() );
					larrOutColumns.put(larrTaxes[j].GetColumnOrder(), lobjColumnHeader);
				}

				lobjVariability = new InsurancePolicy.Coverage.Variability();
				lobjVariability.columnIndex = larrTaxes[j].GetColumnOrder();
				lobjVariability.variesByObject = larrTaxes[j].GetVariesByObject();
				lobjVariability.variesByExercise = larrTaxes[j].GetVariesByExercise();
				larrVariability.add(lobjVariability);

			}
			lobjAuxCoverage.variability = larrVariability.toArray(new InsurancePolicy.Coverage.Variability[larrVariability.size()]);
			larrOutCoverages.add(lobjAuxCoverage);
			larrAuxCoverages.put(lobjCoverage.getKey(), lobjCoverage);
		}

		for ( i = 0; i < larrCoverages.length; i++ )
		{
			try
			{
				larrTaxes = larrCoverages[i].GetCurrentTaxes();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			for ( j = 0; j < larrTaxes.length; j++ )
			{
				if ( !larrCoverages[i].IsHeader() && larrTaxes[j].GetColumnOrder() >= 0 &&
						!larrOutColumns.containsKey(larrTaxes[j].GetColumnOrder()) )
				{
					lobjColumnHeader = new InsurancePolicy.ColumnHeader();
					lobjColumnHeader.label = larrTaxes[j].getLabel();
					lobjColumnHeader.type = GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
					lobjColumnHeader.unitsLabel = (String)larrTaxes[j].getAt(3);
					lobjColumnHeader.refersToId = ( larrTaxes[j].getAt(7) == null ? null : ((UUID)larrTaxes[j].getAt(7)).toString() );
					larrOutColumns.put(larrTaxes[j].GetColumnOrder(), lobjColumnHeader);
				}

				if ( !larrTaxes[j].IsVisible() )
					continue;

				if ( larrAuxFields.get(larrTaxes[j].getKey()) != null )
					continue;

				if ( larrTaxes[j].GetVariesByObject() || larrTaxes[j].GetVariesByExercise() )
					continue;

				if ( larrCoverages[i].IsHeader() )
				{
					lobjHeader = new InsurancePolicy.HeaderField();
					lobjHeader.fieldId = larrTaxes[j].getKey().toString();
					lobjHeader.fieldName = larrTaxes[j].getLabel();
					lobjHeader.type = GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
					lobjHeader.unitsLabel = (String)larrTaxes[j].getAt(3);
					lobjHeader.refersToId = ( larrTaxes[j].getAt(7) == null ? null : ((UUID)larrTaxes[j].getAt(7)).toString() );
					lobjHeader.value = (String)larrTaxes[j].getAt(4);
					lobjHeader.order = (Integer)larrTaxes[j].getAt(8);
					larrOutHeaders.add(lobjHeader);
				}
				else if ( larrTaxes[j].GetColumnOrder() >= 0 )
				{
					lobjField = new InsurancePolicy.TableSection.TableField();
					lobjField.fieldId = larrTaxes[j].getKey().toString();
					lobjField.coverageId = larrTaxes[j].GetCoverage().getKey().toString();
					lobjField.columnIndex = larrTaxes[j].GetColumnOrder();
					lobjField.value = (String)larrTaxes[j].getAt(4);
					larrOutFields.add(lobjField);
				}
				else
				{
					lobjExtra = new InsurancePolicy.ExtraField();
					lobjExtra.fieldId = larrTaxes[j].getKey().toString();
					lobjExtra.fieldName = larrTaxes[j].getLabel();
					lobjExtra.type = GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
					lobjExtra.unitsLabel = (String)larrTaxes[j].getAt(3);
					lobjExtra.refersToId = ( larrTaxes[j].getAt(7) == null ? null : ((UUID)larrTaxes[j].getAt(7)).toString() );
					lobjExtra.coverageId = larrTaxes[j].GetCoverage().getKey().toString();
					lobjExtra.value = (String)larrTaxes[j].getAt(4);
					lobjExtra.order = (Integer)larrTaxes[j].getAt(8);
					larrOutExtras.add(lobjExtra);
				}
				larrAuxFields.put(larrTaxes[j].getKey(), larrTaxes[j]);
			}

			if ( larrAuxCoverages.get(larrCoverages[i].getKey()) != null )
				continue;

			if ( larrCoverages[i].IsHeader() )
				continue;

			lobjAuxCoverage = new InsurancePolicy.Coverage();
			lobjAuxCoverage.coverageId = larrCoverages[i].getKey().toString();
			lobjAuxCoverage.coverageName = larrCoverages[i].getLabel();
			lobjAuxCoverage.mandatory = (Boolean)larrCoverages[i].getAt(2);
			lobjAuxCoverage.presentInPolicy = null;
			larrVariability = new ArrayList<InsurancePolicy.Coverage.Variability>();
			for ( j = 0; j < larrTaxes.length ; j++ )
			{
				if ( larrTaxes[j].GetColumnOrder() < 0 )
					continue;

				lobjVariability = new InsurancePolicy.Coverage.Variability();
				lobjVariability.columnIndex = larrTaxes[j].GetColumnOrder();
				lobjVariability.variesByObject = larrTaxes[j].GetVariesByObject();
				lobjVariability.variesByExercise = larrTaxes[j].GetVariesByExercise();
				larrVariability.add(lobjVariability);

			}
			lobjAuxCoverage.variability = larrVariability.toArray(new InsurancePolicy.Coverage.Variability[larrVariability.size()]);
			larrOutCoverages.add(lobjAuxCoverage);
			larrAuxCoverages.put(larrCoverages[i].getKey(), larrCoverages[i]);
		}

		lobjSection = new InsurancePolicy.TableSection();
		lobjSection.pageId = null;
		lobjSection.data = larrOutFields.toArray(new InsurancePolicy.TableSection.TableField[larrOutFields.size()]);

		lobjResult.headerFields = larrOutHeaders.toArray(new InsurancePolicy.HeaderField[larrOutHeaders.size()]);
		lobjResult.coverages = larrOutCoverages.toArray(new InsurancePolicy.Coverage[larrOutCoverages.size()]);
		lobjResult.columns = new InsurancePolicy.ColumnHeader[larrOutColumns.size()];
		for ( Integer ii: larrOutColumns.keySet() )
			lobjResult.columns[ii] = larrOutColumns.get(ii);
		lobjResult.tableData = new InsurancePolicy.TableSection[] { lobjSection };
		lobjResult.extraData = larrOutExtras.toArray(new InsurancePolicy.ExtraField[larrOutExtras.size()]);

		java.util.Arrays.sort(lobjResult.headerFields, new Comparator<InsurancePolicy.HeaderField>()
		{
			public int compare(InsurancePolicy.HeaderField o1, InsurancePolicy.HeaderField o2)
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
		java.util.Arrays.sort(lobjResult.coverages, new Comparator<InsurancePolicy.Coverage>()
		{
			public int compare(InsurancePolicy.Coverage o1, InsurancePolicy.Coverage o2)
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
		java.util.Arrays.sort(lobjResult.extraData, new Comparator<InsurancePolicy.ExtraField>()
		{
			public int compare(InsurancePolicy.ExtraField o1, InsurancePolicy.ExtraField o2)
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

		lobjResult.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

		return lobjResult;
	}

	public InsurancePolicy getPolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetPolicy(UUID.fromString(policyId));
	}

	public SearchResult[] getExactResults(String label)
		throws SessionExpiredException, BigBangException
	{
		return getExactResultsByOp(label, null);
	}

	public SearchResult[] getExactResultsByOp(String label, String operationId)
		throws SessionExpiredException, BigBangException
	{
		ArrayList<SearchResult> larrResult;
        IEntity lrefPolicies;
        MasterDB ldb;
        ResultSet lrsPolicies;
        Policy lobjPolicy;
		IProcess lobjProc;
		IStep lobjStep;
		Client lobjClient;
		SubLine lobjSubLine;
		Line lobjLine;
		Category lobjCategory;
		ObjectBase lobjStatus;
		InsurancePolicyStub lobjStub;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrResult = new ArrayList<SearchResult>();

		try
        {
            lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
            ldb = new MasterDB();
        }
        catch (Throwable e)
        {
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            lrsPolicies = lrefPolicies.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {"!" + label}, null);
        }
        catch (Throwable e)
        {
        	try {ldb.Disconnect();} catch (Throwable e1) {}
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            while (lrsPolicies.next())
            {
            	lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), lrsPolicies);
    			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjPolicy.GetProcessID());

    			if ( operationId != null )
    			{
    				lobjStep = lobjProc.GetOperation(UUID.fromString(operationId), ldb);
    				if ( (lobjStep == null) || (Jewel.Petri.Constants.LevelID_Invalid.equals(lobjStep.GetLevel())) )
    					continue;
    			}

    			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetParent().GetData().getKey());
    			lobjSubLine = lobjPolicy.GetSubLine();
    			lobjLine = lobjSubLine.getLine();
    			lobjCategory = lobjLine.getCategory();
    			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
    					(UUID)lobjPolicy.getAt(13));

            	lobjStub = new InsurancePolicyStub();

            	lobjStub.id = lobjPolicy.getKey().toString();
            	lobjStub.processId = lobjProc.getKey().toString();
            	lobjStub.number = (String)lobjPolicy.getAt(0);
            	lobjStub.clientId = lobjClient.getKey().toString();
            	lobjStub.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
            	lobjStub.clientName = lobjClient.getLabel();
            	lobjStub.categoryId = lobjCategory.getKey().toString();
            	lobjStub.categoryName = lobjCategory.getLabel();
            	lobjStub.lineId = lobjLine.getKey().toString();
            	lobjStub.lineName = lobjLine.getLabel();
            	lobjStub.subLineId = lobjSubLine.getKey().toString();
            	lobjStub.subLineName = lobjSubLine.getLabel();
            	lobjStub.caseStudy = (Boolean)lobjPolicy.getAt(12);
            	lobjStub.statusId = lobjStatus.getKey().toString();
            	lobjStub.statusText = lobjStatus.getLabel();
        		switch ( (Integer)lobjStatus.getAt(1) )
        		{
        		case 0:
        			lobjStub.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
        			break;

        		case 1:
        			lobjStub.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
        			break;

        		case 2:
        			lobjStub.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
        			break;
        		}

    			lobjStub.permissions = BigBangPermissionServiceImpl.sGetProcessPermissions(lobjProc.getKey());

            	larrResult.add(lobjStub);
            }
        }
        catch (Throwable e)
        {
        	try {lrsPolicies.close();} catch (Throwable e2) {}
        	try {ldb.Disconnect();} catch (Throwable e1) {}
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            lrsPolicies.close();
        }
        catch (Throwable e)
        {
        	try {ldb.Disconnect();} catch (Throwable e1) {}
            throw new BigBangException(e.getMessage(), e);
        }

        try
        {
            ldb.Disconnect();
        }
        catch (Throwable e)
        {
            throw new BigBangException(e.getMessage(), e);
        }

		return larrResult.toArray(new SearchResult[larrResult.size()]);
	}

	public InsurancePolicy.TableSection getPage(String policyId, String insuredObjectId, String exerciseId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidObject;
		UUID lidExercise;
		Policy lobjPolicy;
		Coverage[] larrCoverages;
		PolicyValue[] larrValues;
		InsurancePolicy.TableSection lobjResult;
		Hashtable<UUID, Tax> larrAuxFields;
		ArrayList<InsurancePolicy.TableSection.TableField> larrFields;
		Tax lobjTax;
		InsurancePolicy.TableSection.TableField lobjField;
		Tax[] larrTaxes;
		int i, j;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidObject = (insuredObjectId == null ? null : UUID.fromString(insuredObjectId));
		lidExercise = (exerciseId == null ? null : UUID.fromString(exerciseId));

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
			larrCoverages = lobjPolicy.GetSubLine().GetCurrentCoverages();

			larrValues = lobjPolicy.GetCurrentKeyedValues(lidObject, lidExercise);
			larrAuxFields = new Hashtable<UUID, Tax>();
			larrFields = new ArrayList<InsurancePolicy.TableSection.TableField>();
			for ( i = 0; i < larrValues.length; i++ )
			{
				lobjTax = larrValues[i].GetTax();
//				if ( lobjTax.GetVariesByObject() || lobjTax.GetVariesByExercise() )
//					throw new BigBangException("Inesperado: Valor variável marcado como invariante.");

				if ( lobjTax.GetCoverage().IsHeader() || (lobjTax.GetColumnOrder() < 0) ||
						!lobjTax.IsVisible() )
					continue;

				lobjField = new InsurancePolicy.TableSection.TableField();
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
					if ( (larrTaxes[j].GetColumnOrder() < 0) || !larrTaxes[j].IsVisible() )
						continue;

					if ( larrAuxFields.get(larrTaxes[j].getKey()) != null )
						continue;

					if ( (larrTaxes[j].GetVariesByObject() == (lidObject == null)) ||
							(larrTaxes[j].GetVariesByExercise() == (lidExercise == null)) )
						continue;

					lobjField = new InsurancePolicy.TableSection.TableField();
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

		lobjResult = new InsurancePolicy.TableSection();
		lobjResult.pageId = null;
		lobjResult.data = larrFields.toArray(new InsurancePolicy.TableSection.TableField[larrFields.size()]);
		return lobjResult;
	}

	public Remap[] openPolicyScratchPad(String policyId)
		throws SessionExpiredException, BigBangException 
	{
		PolicyScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjPad = new PolicyScratchPad();
		if ( policyId != null )
			lobjPad.OpenForEdit(UUID.fromString(policyId));
		GetScratchPadStorage().put(lobjPad.GetID(), lobjPad);

		return lobjPad.GetRemapIntoPad();
	}

	public InsurancePolicy initPolicyInPad(InsurancePolicy policy)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( policy.id == null )
			throw new BigBangException("Erro: Não pode inicializar uma apólice sem abrir o espaço de trabalho.");

		if ( policy.subLineId == null )
			throw new BigBangException("Erro: Não pode inicializar uma apólice antes de preencher a categoria, ramo e modalidade.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(policy.id));
		lobjPad.InitNew(policy);

		lobjPad.WriteResult(policy);
		return policy;
	}

	public InsurancePolicy getPolicyInPad(String policyId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lobjPad;
		InsurancePolicy lobjPolicy;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjPad = GetScratchPadStorage().get(UUID.fromString(policyId));
		lobjPolicy = lobjPad.WriteBasics();
		lobjPad.WriteResult(lobjPolicy);
		return lobjPolicy;
	}

	public InsurancePolicy updateHeader(InsurancePolicy policy)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( policy.id == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(policy.id));
		lobjPad.UpdateInvariants(policy);

		lobjPad.WriteResult(policy);
		return policy;
	}

	public InsurancePolicy.TableSection getPageForEdit(String policyId, String objectId, String exerciseId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;
		int llngExercise;
		PolicyScratchPad lobjPad;
		InsurancePolicy.TableSection lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( policyId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");
		lidPad = UUID.fromString(policyId);

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

		if ( (exerciseId == null) || (exerciseId.length() == 0) )
			llngExercise = -1;
		else
		{
			larrAux = exerciseId.split(":");
			if ( larrAux.length != 2 )
	            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + exerciseId);
			if ( !lidPad.equals(UUID.fromString(larrAux[0])) )
				throw new BigBangException("Inesperado: exercício não pertence ao espaço de trabalho.");
			llngExercise = Integer.parseInt(larrAux[1]);
		}

		lobjPad = GetScratchPadStorage().get(lidPad);

		lobjResult = new InsurancePolicy.TableSection();
		lobjPad.WritePage(lobjResult, llngObject, llngExercise);
		return lobjResult;
	}

	public InsurancePolicy.TableSection savePage(InsurancePolicy.TableSection data)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;
		int llngExercise;
		PolicyScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = data.pageId.split(":");
		if ( larrAux.length != 3 )
            throw new IllegalArgumentException("Unexpected: Invalid page ID string: " + data.pageId);
		lidPad = UUID.fromString(larrAux[0]);
		llngObject = Integer.parseInt(larrAux[1]);
		llngExercise = Integer.parseInt(larrAux[2]);

		lobjPad = GetScratchPadStorage().get(lidPad);
		lobjPad.UpdatePage(data, llngObject, llngExercise);

		lobjPad.WritePage(data, llngObject, llngExercise);
		return data;
	}

	public TipifiedListItem[] getListItemsFilter(String listId, String filterId)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		PolicyCoverage[] larrCoverages;
		ArrayList<TipifiedListItem> larrResult;
		TipifiedListItem lobjAux;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( filterId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		try
		{
			if ( Constants.ObjID_PolicyObject.equals(UUID.fromString(listId)) )
				return GetScratchPadStorage().get(UUID.fromString(filterId)).GetObjects();

			if ( Constants.ObjID_PolicyExercise.equals(UUID.fromString(listId)) )
				return GetScratchPadStorage().get(UUID.fromString(filterId)).GetExercises();

			if ( Constants.ObjID_PolicyCoverage.equals(UUID.fromString(listId)) )
			{
				lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(filterId));
				larrCoverages = lobjPolicy.GetCurrentCoverages();
				larrResult = new ArrayList<TipifiedListItem>();
				for ( i = 0; i < larrCoverages.length; i++ )
				{
					if ( (larrCoverages[i].GetCoverage().IsHeader()) || (larrCoverages[i].IsPresent() == null) || !larrCoverages[i].IsPresent() )
						continue;
					lobjAux = new TipifiedListItem();
					lobjAux.id = larrCoverages[i].getKey().toString();
					lobjAux.value = larrCoverages[i].GetCoverage().getLabel();
					larrResult.add(lobjAux);
				}
				return larrResult.toArray(new TipifiedListItem[larrResult.size()]);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		throw new BigBangException("Erro: Lista inválida para o espaço de trabalho.");
	}

	public InsuredObject getObjectInPad(String objectId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;
		PolicyScratchPad lobjPad;
		InsuredObject lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = objectId.split(":");
		if ( larrAux.length != 2 )
            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + objectId);
		lidPad = UUID.fromString(larrAux[0]);
		llngObject = Integer.parseInt(larrAux[1]);

		lobjPad = GetScratchPadStorage().get(lidPad);

		lobjResult = new InsuredObject();
		lobjPad.WriteObject(lobjResult, llngObject);
		return lobjResult;
	}

	public InsuredObject createObjectInPad(String policyId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lobjPad;
		InsuredObject lobjResult;
		int llngObject;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( policyId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(policyId));
		llngObject = lobjPad.CreateNewObject();

		lobjResult = new InsuredObject();
		lobjPad.WriteObject(lobjResult, llngObject);
		return lobjResult;
	}

	@Override
	public InsuredObject createObjectFromClientInPad(String scratchPadId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public InsuredObject updateObjectInPad(InsuredObject data)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lobjPad;
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

	public Exercise getExerciseInPad(String exerciseId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;
		PolicyScratchPad lobjPad;
		Exercise lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = exerciseId.split(":");
		if ( larrAux.length != 2 )
            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + exerciseId);
		lidPad = UUID.fromString(larrAux[0]);
		llngObject = Integer.parseInt(larrAux[1]);

		lobjPad = GetScratchPadStorage().get(lidPad);

		lobjResult = new Exercise();
		lobjPad.WriteExercise(lobjResult, llngObject);
		return lobjResult;
	}

	public Exercise createFirstExercise(String policyId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lobjPad;
		Exercise lobjResult;
		int llngObject;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( policyId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(policyId));
		llngObject = lobjPad.CreateNewExercise();

		lobjResult = new Exercise();
		lobjPad.WriteExercise(lobjResult, llngObject);
		return lobjResult;
	}

	public Exercise updateExerciseInPad(Exercise data)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lobjPad;
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
		lobjPad.UpdateExercise(data, llngObject);

		lobjPad.WriteExercise(data, llngObject);
		return data;
	}

	public void deleteExerciseInPad(String exerciseId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		larrAux = exerciseId.split(":");
		if ( larrAux.length != 2 )
            throw new IllegalArgumentException("Unexpected: Invalid temporary ID string: " + exerciseId);
		lidPad = UUID.fromString(larrAux[0]);
		llngObject = Integer.parseInt(larrAux[1]);

		GetScratchPadStorage().get(lidPad).DeleteExercise(llngObject);
	}

	public Remap[] commitPad(String policyId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		PolicyScratchPad lrefPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefPad = GetScratchPadStorage().get(UUID.fromString(policyId));
		lrefPad.CommitChanges();
		GetScratchPadStorage().remove(UUID.fromString(policyId));
		return lrefPad.GetRemapFromPad(true);
	}

	public Remap[] discardPad(String policyId)
		throws SessionExpiredException, BigBangException
	{
		PolicyScratchPad lrefPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefPad = GetScratchPadStorage().get(UUID.fromString(policyId));
		GetScratchPadStorage().remove(UUID.fromString(policyId));
		return lrefPad.GetRemapFromPad(false);
	}

	public InsurancePolicy performCalculations(String policyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyCalculationException
	{
		Policy lobjPolicy;
		PerformComputations lopPC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopPC = new PerformComputations(lobjPolicy.GetProcessID());

		try
		{
			lopPC.Execute();
		}
		catch (PolicyCalculationException e)
		{
			throw new BigBangPolicyCalculationException(e.getMessage());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sGetPolicy(lobjPolicy.getKey());
	}

	public void validatePolicy(String policyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyValidationException
	{
		Policy lobjPolicy;
		ValidatePolicy lopVP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVP = new ValidatePolicy(lobjPolicy.GetProcessID());

		try
		{
			lopVP.Execute();
		}
		catch (PolicyValidationException e)
		{
			throw new BigBangPolicyValidationException(e.getMessage());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	@Override
	public InsuredObject includeObject(String policyId, InsuredObject object)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject includeObjectFromClient(String policyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excludeObject(String policyId, String objectId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Exercise openNewExercise(String policyId, Exercise exercise)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public InsurancePolicy transferToClient(String policyId, String newClientId)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		TransferToClient lopTTC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopTTC = new TransferToClient(lobjPolicy.GetProcessID());
		lopTTC.midNewClient = UUID.fromString(newClientId);

		try
		{
			lopTTC.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getPolicy(policyId);
	}

	public void createDebitNote(String policyId, DebitNote note)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateDebitNote lopCDN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCDN = new CreateDebitNote(lobjPolicy.GetProcessID());
		lopCDN.mobjData = new DebitNoteData();
		lopCDN.mobjData.midProcess = lobjPolicy.GetProcessID();
		lopCDN.mobjData.mdblValue = new BigDecimal(note.value+"");
		lopCDN.mobjData.mdtMaturity = Timestamp.valueOf(note.maturityDate + " 00:00:00.0");

		try
		{
			lopCDN.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateInfoRequest lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(request.parentDataObjectId));

			lopCIR = new CreateInfoRequest(lobjPolicy.GetProcessID());
			lopCIR.midRequestType = UUID.fromString(request.requestTypeId);
			lopCIR.mobjMessage = MessageBridge.outgoingToServer(request.message);
			lopCIR.mlngDays = request.replylimit;

			lopCIR.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return InfoOrDocumentRequestServiceImpl.sGetRequest(lopCIR.midRequestObject);
	}

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		ExecMgrXFer lobjCMX;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(transfer.dataObjectIds[0]));

			lobjCMX = new ExecMgrXFer(lobjPolicy.GetProcessID());
			lobjCMX.midNewManager = UUID.fromString(transfer.newManagerId);
			lobjCMX.midMassProcess = null;

			lobjCMX.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return transfer;
	}

	public Receipt createReceipt(String policyId, Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateReceipt lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));

			lopCR = new CreateReceipt(lobjPolicy.GetProcessID());
			lopCR.mobjData = new ReceiptData();

			lopCR.mobjData.mid = null;

			lopCR.mobjData.mstrNumber = receipt.number;
			lopCR.mobjData.midType = UUID.fromString(receipt.typeId);
			lopCR.mobjData.mdblTotal = new BigDecimal(receipt.totalPremium);
			lopCR.mobjData.mdblCommercial = (receipt.salesPremium == null ? null : new BigDecimal(receipt.salesPremium));
			lopCR.mobjData.mdblCommissions = (receipt.comissions == null ? new BigDecimal(0) : new BigDecimal(receipt.comissions));
			lopCR.mobjData.mdblRetrocessions = (receipt.retrocessions == null ? null : new BigDecimal(receipt.retrocessions));
			lopCR.mobjData.mdblFAT = (receipt.FATValue == null ? null : new BigDecimal(receipt.FATValue));
			lopCR.mobjData.mdblBonusMalus = (receipt.bonusMalus == null ? null : new BigDecimal(receipt.bonusMalus));
			lopCR.mobjData.mbIsMalus = receipt.isMalus;
			lopCR.mobjData.mdtIssue = Timestamp.valueOf(receipt.issueDate + " 00:00:00.0");
			lopCR.mobjData.mdtMaturity = (receipt.maturityDate == null ? null :
					Timestamp.valueOf(receipt.maturityDate + " 00:00:00.0"));
			lopCR.mobjData.mdtEnd = (receipt.endDate == null ? null : Timestamp.valueOf(receipt.endDate + " 00:00:00.0"));
			lopCR.mobjData.mdtDue = (receipt.dueDate == null ? null : Timestamp.valueOf(receipt.dueDate + " 00:00:00.0"));
			lopCR.mobjData.midMediator = (receipt.mediatorId == null ? null : UUID.fromString(receipt.mediatorId));
			lopCR.mobjData.mstrNotes = receipt.notes;
			lopCR.mobjData.mstrDescription = receipt.description;

			lopCR.mobjData.midManager = ( receipt.managerId == null ? null : UUID.fromString(receipt.managerId) );
			lopCR.mobjData.midProcess = null;

			lopCR.mobjData.mobjPrevValues = null;

			lopCR.mobjImage = null;

			if ( (receipt.contacts != null) && (receipt.contacts.length > 0) )
			{
				lopCR.mobjContactOps = new ContactOps();
				lopCR.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(receipt.contacts);
			}
			else
				lopCR.mobjContactOps = null;
			if ( (receipt.documents != null) && (receipt.documents.length > 0) )
			{
				lopCR.mobjDocOps = new DocOps();
				lopCR.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(receipt.documents);
			}
			else
				lopCR.mobjDocOps = null;

			lopCR.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ReceiptServiceImpl.sGetReceipt(lopCR.mobjData.mid);
	}

	public Expense createExpense(Expense expense)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateExpense lopCE;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( !Constants.ObjID_Policy.equals(UUID.fromString(expense.referenceTypeId)) )
			throw new BigBangException("Erro: Tentativa de criar despesa de saúde de apólice adesão a partir de uma apólice.");

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(expense.referenceId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCE = new CreateExpense(lobjPolicy.GetProcessID());
		lopCE.mobjData = new ExpenseData();
		lopCE.mobjData.mid = null;
		lopCE.mobjData.mstrNumber = null;
		lopCE.mobjData.mdtDate = Timestamp.valueOf(expense.expenseDate + " 00:00:00.0");
		lopCE.mobjData.midPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
		lopCE.mobjData.midSubPolicyObject = null;
		lopCE.mobjData.midPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
		lopCE.mobjData.midSubPolicyCoverage = null;
		lopCE.mobjData.mdblDamages = new BigDecimal(expense.value+"");
		lopCE.mobjData.mdblSettlement = (expense.settlement == null ? null : new BigDecimal(expense.settlement));
		lopCE.mobjData.mbIsManual = expense.isManual;
		lopCE.mobjData.mstrNotes = expense.notes;
		lopCE.mobjData.midManager = null;
		lopCE.mobjData.midProcess = null;
		lopCE.mobjData.mobjPrevValues = null;
		lopCE.mobjContactOps = null;
		lopCE.mobjDocOps = null;

		try
		{
			lopCE.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return ExpenseServiceImpl.sGetExpense(lopCE.mobjData.mid);
	}

	public Negotiation createNegotiation(Negotiation negotiation)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		CreateNegotiation lopCN;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(negotiation.ownerId));

			lopCN = new CreateNegotiation(lobjPolicy.GetProcessID());
			lopCN.mobjData = new NegotiationData();

			lopCN.mobjData.mid = null;
			lopCN.mobjData.mstrNotes = negotiation.notes;
			lopCN.mobjData.mdtLimitDate = (negotiation.limitDate == null ? null :
					Timestamp.valueOf(negotiation.limitDate + " 00:00:00.0"));

			lopCN.mobjData.midManager = null;
			lopCN.mobjData.midProcess = null;

			lopCN.mobjData.mobjPrevValues = null;

			if ( (negotiation.contacts != null) && (negotiation.contacts.length > 0) )
			{
				lopCN.mobjContactOps = new ContactOps();
				lopCN.mobjContactOps.marrCreate = ContactsServiceImpl.BuildContactTree(negotiation.contacts);
			}
			else
				lopCN.mobjContactOps = null;
			if ( (negotiation.documents != null) && (negotiation.documents.length > 0) )
			{
				lopCN.mobjDocOps = new DocOps();
				lopCN.mobjDocOps.marrCreate = DocumentServiceImpl.BuildDocTree(negotiation.documents);
			}
			else
				lopCN.mobjDocOps = null;

			lopCN.Execute();

		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return NegotiationServiceImpl.sGetNegotiation(lopCN.mobjData.mid);
	}

	public InsurancePolicy voidPolicy(PolicyVoiding voiding)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		ObjectBase lobjMotive;
		VoidPolicy lopVP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(voiding.policyId));
			lobjMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_PolicyVoidingMotives), UUID.fromString(voiding.motiveId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVP = new VoidPolicy(lobjPolicy.GetProcessID());
		lopVP.mdtEffectDate = Timestamp.valueOf(voiding.effectDate + " 00:00:00.0");
		lopVP.mstrMotive = lobjMotive.getLabel();
		lopVP.mstrNotes = voiding.notes;

		try
		{
			lopVP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getPolicy(voiding.policyId);
	}

	public void deletePolicy(String policyId)
		throws SessionExpiredException, BigBangException
	{
		Policy lobjPolicy;
		DeletePolicy lobjDP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(policyId));

			lobjDP = new DeletePolicy(lobjPolicy.GetProcessID());
			lobjDP.midPolicy = UUID.fromString(policyId);
			lobjDP.Execute();
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

		transfer.objectTypeId = Constants.ObjID_Policy.toString();

		return TransferManagerServiceImpl.sCreateMassTransfer(transfer, Constants.ProcID_Policy);
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_Policy;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:SubLine:Line:Category]", "[:SubLine:Line:Category:Name]",
				"[:SubLine:Line]", "[:SubLine:Line:Name]", "[:SubLine]", "[:SubLine:Name]", "[:Case Study]", "[:Status]",
				"[:Status:Status]", "[:Status:Level], [:Client], [:Client:Number], [:Client:Name]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		InsurancePolicySearchParameter lParam;
		String lstrAux;
		IEntity lrefClients, lrefObjects;

		if ( !(pParam instanceof InsurancePolicySearchParameter) )
			return false;
		lParam = (InsurancePolicySearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
				pstrBuffer.append(lrefClients.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxClients] WHERE [:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR CAST([:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%'))");
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
			pstrBuffer.append(" AND [:SubLine] = '").append(lParam.subLineId).append("'");
		}
		else if ( lParam.lineId != null )
		{
			pstrBuffer.append(" AND [:SubLine:Line] = '").append(lParam.lineId).append("'");
		}
		else if ( lParam.categoryId != null )
		{
			pstrBuffer.append(" AND [:SubLine:Line:Category] = '").append(lParam.categoryId).append("'");
		}

		if ( lParam.insuranceAgencyId != null )
		{
			pstrBuffer.append(" AND [:Company] = '").append(lParam.insuranceAgencyId).append("'");
		}

		if ( lParam.mediatorId != null )
		{
			pstrBuffer.append(" AND [:Mediator] = '").append(lParam.mediatorId).append("'");
		}

		if ( lParam.managerId != null )
		{
			pstrBuffer.append(" AND [:Process:Manager] = '").append(lParam.managerId).append("'");
		}

		if ( lParam.insuredObject != null )
		{
			lstrAux = lParam.insuredObject.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([PK] IN (SELECT [:Policy] FROM (");
			try
			{
				lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
				pstrBuffer.append(lrefObjects.SQLForSelectSingle());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxObj1] WHERE [:Name] LIKE N'%").append(lstrAux).append("%')");
			pstrBuffer.append(" OR [:Process] IN (SELECT [:Sub Policy:Process:Parent] FROM (");
			try
			{
				lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicyObject));
				pstrBuffer.append(lrefObjects.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxObj2] WHERE [:Name] LIKE N'%").append(lstrAux).append("%'))");
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
		InsurancePolicySortParameter lParam;
		IEntity lrefClients;

		if ( !(pParam instanceof InsurancePolicySortParameter) )
			return false;
		lParam = (InsurancePolicySortParameter)pParam;

		if ( lParam.field == InsurancePolicySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == InsurancePolicySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == InsurancePolicySortParameter.SortableField.CATEGORY_LINE_SUBLINE )
		{
			pstrBuffer.append("[:SubLine:Name]");
			if ( lParam.order == SortOrder.ASC )
				pstrBuffer.append(" ASC");
			if ( lParam.order == SortOrder.DESC )
				pstrBuffer.append(" DESC");
			pstrBuffer.append(", [:SubLine:Line:Name]");
			if ( lParam.order == SortOrder.ASC )
				pstrBuffer.append(" ASC");
			if ( lParam.order == SortOrder.DESC )
				pstrBuffer.append(" DESC");
			pstrBuffer.append(", [:SubLine:Line:Category:Name]");
		}

		if ( lParam.field == InsurancePolicySortParameter.SortableField.CLIENT_NAME )
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

		if ( lParam.field == InsurancePolicySortParameter.SortableField.CLIENT_NUMBER )
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
		InsurancePolicyStub lobjResult;

		lobjResult = new InsurancePolicyStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.processId = ((UUID)parrValues[1]).toString();
		lobjResult.clientId = ((UUID)parrValues[12]).toString();
		lobjResult.clientNumber = ((Integer)parrValues[13]).toString();
		lobjResult.clientName = (String)parrValues[14];
		lobjResult.categoryId = parrValues[2].toString();
		lobjResult.categoryName = (String)parrValues[3];
		lobjResult.lineId = parrValues[4].toString();
		lobjResult.lineName = (String)parrValues[5];
		lobjResult.subLineId = parrValues[6].toString();
		lobjResult.subLineName = (String)parrValues[7];
		lobjResult.caseStudy = (Boolean)parrValues[8];
		lobjResult.statusId = ((UUID)parrValues[9]).toString();
		lobjResult.statusText = (String)parrValues[10];
		switch ( (Integer)parrValues[11] )
		{
		case 0:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.PROVISIONAL;
			break;

		case 1:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.VALID;
			break;

		case 2:
			lobjResult.statusIcon = InsurancePolicyStub.PolicyStatus.OBSOLETE;
			break;
		}
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		InsurancePolicySearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof InsurancePolicySearchParameter) )
				continue;
			lParam = (InsurancePolicySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ")
					.append("WHEN [:Client:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', [:Client:Name]) ")
					.append("WHEN CAST([:Client:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', CAST([:Client:Number] AS NVARCHAR(20))) ")
					.append("WHEN [:SubLine:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Name]) ")
					.append("WHEN [:SubLine:Line:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Line:Name]) ")
					.append("WHEN [:SubLine:Line:Category:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000000000000*PATINDEX(N'%").append(lstrAux).append("%', [:SubLine:Line:Category:Name]) ")
					.append("ELSE 0 END");
		}

		return lbFound;
	}

	public static InsurancePolicy.FieldType GetFieldTypeByID(UUID pidFieldType)
	{
		if ( Constants.FieldID_Boolean.equals(pidFieldType) )
			return InsurancePolicy.FieldType.BOOLEAN;
		if ( Constants.FieldID_Date.equals(pidFieldType) )
			return InsurancePolicy.FieldType.DATE;
		if ( Constants.FieldID_List.equals(pidFieldType) )
			return InsurancePolicy.FieldType.LIST;
		if ( Constants.FieldID_Number.equals(pidFieldType) )
			return InsurancePolicy.FieldType.NUMERIC;
		if ( Constants.FieldID_Reference.equals(pidFieldType) )
			return InsurancePolicy.FieldType.REFERENCE;
		if ( Constants.FieldID_Text.equals(pidFieldType) )
			return InsurancePolicy.FieldType.TEXT;
		return null;
	}
}
