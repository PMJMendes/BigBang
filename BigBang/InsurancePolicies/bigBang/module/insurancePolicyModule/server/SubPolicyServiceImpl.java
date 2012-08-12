package bigBang.module.insurancePolicyModule.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;
import bigBang.definitions.shared.Address;
import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicy.TableSection;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.definitions.shared.ZipCode;
import bigBang.library.server.BigBangPermissionServiceImpl;
import bigBang.library.server.ContactsServiceImpl;
import bigBang.library.server.DocumentServiceImpl;
import bigBang.library.server.InfoOrDocumentRequestServiceImpl;
import bigBang.library.server.MessageBridge;
import bigBang.library.server.SearchServiceBase;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.expenseModule.server.ExpenseServiceImpl;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;
import bigBang.module.insurancePolicyModule.shared.SubPolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.SubPolicySortParameter;
import bigBang.module.receiptModule.server.ReceiptServiceImpl;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Data.ExpenseData;
import com.premiumminds.BigBang.Jewel.Data.PolicyExerciseData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyCoverageData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyValueData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.Tax;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateSubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateExpense;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateInfoRequest;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.DeleteSubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ManageData;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.PerformComputations;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.TransferToPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ValidateSubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.VoidSubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.ZipCodeBridge;

public class SubPolicyServiceImpl
	extends SearchServiceBase
	implements SubPolicyService
{
	private static final long serialVersionUID = 1L;

	private static class SubPolicyScratchPad
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
			public boolean mbVariesByExercise;
		}

		private static class PadCoverage
			extends SubPolicyCoverageData
		{
			private static final long serialVersionUID = 1L;

			public transient String mstrLabel;
			public transient boolean mbMandatory;
			public transient boolean mbIsHeader;
			public transient int mlngOrder;
			public transient PadField[] marrFields;
		}

		private static class PadObject
			extends SubPolicyObjectData
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
			extends SubPolicyValueData
		{
			private static final long serialVersionUID = 1L;

			public transient PadCoverage mrefCoverage;
			public transient PadField mrefField;
		}

		public final UUID mid;
		public boolean mbValid;

		public UUID midMainPolicy;
		public SubPolicyData mobjSubPolicy;
		public ArrayList<PadCoverage> marrCoverages;
		public ArrayList<PadObject> marrObjects;
		public ArrayList<PadExercise> marrExercises;
		public ArrayList<PadValue> marrValues;

		public SubPolicyScratchPad()
		{
			mid = UUID.randomUUID();
			mbValid = false;
		}
		
		public UUID GetID()
		{
			return mid;
		}

		public void InitNew(SubPolicy pobjSource)
			throws BigBangException
		{
			Policy lobjAuxPolicy;
			PolicyExercise[] larrAuxExercises;
			Hashtable<UUID, Integer> lmapExercises;
			PadExercise lobjExercise;
			com.premiumminds.BigBang.Jewel.Objects.Coverage[] larrAuxCoverages;
			PadCoverage lobjCoverage;
			Tax[] larrTaxes;
			ArrayList<PadField> larrFields;
			PadField lobjField;
			PadValue lobjValue;
			int i, j;

			if ( mbValid )
				throw new BigBangException("Erro: Não pode inicializar o mesmo espaço de trabalho duas vezes.");

			midMainPolicy = UUID.fromString(pobjSource.mainPolicyId);
			mobjSubPolicy = new SubPolicyData();
			mobjSubPolicy.mstrNumber = pobjSource.number;
			mobjSubPolicy.midSubscriber = ( pobjSource.clientId == null ? null : UUID.fromString(pobjSource.clientId) );
			mobjSubPolicy.mdtBeginDate = ( pobjSource.startDate == null ? null :
					Timestamp.valueOf(pobjSource.startDate + " 00:00:00.0") );
			mobjSubPolicy.midFractioning = ( pobjSource.fractioningId == null ? null : UUID.fromString(pobjSource.fractioningId) );
			mobjSubPolicy.mdtEndDate = ( pobjSource.expirationDate == null ? null :
					Timestamp.valueOf(pobjSource.expirationDate + " 00:00:00.0") );
			mobjSubPolicy.mstrNotes = pobjSource.notes;
			mobjSubPolicy.mdblPremium = ( pobjSource.premium == null ? null : new BigDecimal(pobjSource.premium+"") );

			marrCoverages = new ArrayList<PadCoverage>();
			marrObjects = new ArrayList<PadObject>();
			marrExercises = new ArrayList<PadExercise>();
			marrValues = new ArrayList<PadValue>();

			mobjSubPolicy.mbModified = false;

			try
			{
				lobjAuxPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midMainPolicy);

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

				larrAuxCoverages = lobjAuxPolicy.GetSubLine().GetCurrentCoverages();
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

						if ( lobjField.mbVariesByObject || lobjField.mbVariesByExercise )
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
						lobjValue.mlngExercise = -1;
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

			mbValid = true;
		}

		public void OpenForEdit(UUID pidSubPolicy)
			throws BigBangException
		{
			com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjAuxPolicy;
			SubPolicyCoverage[] larrLocalCoverages;
			com.premiumminds.BigBang.Jewel.Objects.Coverage[] larrAuxCoverages;
			Hashtable<UUID, PadCoverage> lmapCoverages;
			PadCoverage lobjCoverage;
			Tax[] larrTaxes;
			ArrayList<PadField> larrFields;
			Hashtable<UUID, PadField> lmapFields;
			PadField lobjField;
			SubPolicyObject[] larrAuxObjects;
			Hashtable<UUID, Integer> lmapObjects;
			PadObject lobjObject;
			PolicyExercise[] larrAuxExercises;
			Hashtable<UUID, Integer> lmapExercises;
			PadExercise lobjExercise;
			SubPolicyValue[] larrAuxValues;
			PadValue lobjValue;
			int i, j, k, l;

			if ( mbValid )
				throw new BigBangException("Erro: Não pode inicializar o mesmo espaço de trabalho duas vezes.");

			marrCoverages = new ArrayList<PadCoverage>();
			marrObjects = new ArrayList<PadObject>();
			marrExercises = new ArrayList<PadExercise>();
			marrValues = new ArrayList<PadValue>();

			try
			{
				lobjAuxPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(), pidSubPolicy);

				midMainPolicy = PNProcess.GetInstance(Engine.getCurrentNameSpace(),
						lobjAuxPolicy.GetProcessID()).GetParent().GetDataKey();

				mobjSubPolicy = new SubPolicyData();
				mobjSubPolicy.FromObject(lobjAuxPolicy);

				mobjSubPolicy.mbModified = false;

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

				larrAuxCoverages = lobjAuxPolicy.GetOwner().GetSubLine().GetCurrentCoverages();
				for ( i = 0 ; i < larrAuxCoverages.length; i++ )
				{
					if ( FindCoverage(larrAuxCoverages[i].getKey(), 0) >= 0 )
						continue;
					lobjCoverage = new PadCoverage();
					lobjCoverage.mid = null;
					lobjCoverage.midOwner = pidSubPolicy;
					lobjCoverage.midCoverage = larrAuxCoverages[i].getKey();
					lobjCoverage.mstrLabel = larrAuxCoverages[i].getLabel();
					lobjCoverage.mbIsHeader = larrAuxCoverages[i].IsHeader();
					lobjCoverage.mbMandatory = larrAuxCoverages[i].IsMandatory();
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
					if ( (larrAuxValues[i].getAt(4) != null) && (lmapExercises.get(larrAuxValues[i].getAt(4)) == null) )
						continue;
					lobjValue = new PadValue();
					lobjValue.FromObject(larrAuxValues[i]);
					lobjValue.mrefCoverage = lmapCoverages.get(lobjValue.midField);
					lobjValue.mrefField = lmapFields.get(lobjValue.midField);
					lobjValue.mlngObject = ( lobjValue.midObject == null ? -1 : lmapObjects.get(lobjValue.midObject) );
					lobjValue.mlngExercise = ( lobjValue.midExercise == null ? -1 : lmapExercises.get(lobjValue.midExercise) );
					lobjValue.mbDeleted = false;
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
										lobjValue.midOwner = pidSubPolicy;
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
									lobjValue.midOwner = pidSubPolicy;
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
								lobjValue.midOwner = pidSubPolicy;
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
							lobjValue.midOwner = pidSubPolicy;
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
				larrResult[0].typeId = Constants.ObjID_SubPolicy.toString();
				larrResult[0].remapIds = new Remap.RemapId[1];
				larrResult[0].remapIds[0] = new Remap.RemapId();
				larrResult[0].remapIds[0].oldId = null;
				larrResult[0].remapIds[0].newId = mid.toString();
				larrResult[0].remapIds[0].newIdIsInPad = true;

				return larrResult;
			}

			larrResult = new Remap[2];

			larrResult[0] = new Remap();
			larrResult[0].typeId = Constants.ObjID_SubPolicy.toString();
			larrResult[0].remapIds = new Remap.RemapId[1];
			larrResult[0].remapIds[0] = new Remap.RemapId();
			larrResult[0].remapIds[0].oldId = mobjSubPolicy.mid.toString();
			larrResult[0].remapIds[0].newId = mid.toString();
			larrResult[0].remapIds[0].newIdIsInPad = true;

			larrResult[1] = new Remap();
			larrResult[1].typeId = Constants.ObjID_SubPolicyObject.toString();
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
			larrResult[0].typeId = Constants.ObjID_SubPolicy.toString();
			larrResult[0].remapIds = new Remap.RemapId[1];
			larrResult[0].remapIds[0] = new Remap.RemapId();
			larrResult[0].remapIds[0].oldId = mid.toString();
			larrResult[0].remapIds[0].newId = (mobjSubPolicy.mid == null ? null : mobjSubPolicy.mid.toString());
			larrResult[0].remapIds[0].newIdIsInPad = false;

			larrResult[1] = new Remap();
			larrResult[1].typeId = Constants.ObjID_SubPolicyObject.toString();
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

		public SubPolicy WriteBasics()
			throws BigBangException, CorruptedPadException
		{
			Policy lobjPolicy;
			Mediator lobjMed;
			Company lobjCompany;
			Client lobjClient;
			IProcess lobjProc;
			ObjectBase lobjStatus;
			IUser lobjUser;
			SubPolicy lobjResult;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			try
			{
				lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midMainPolicy);
				lobjClient = (Client)PNProcess.GetInstance(Engine.getCurrentNameSpace(),
						lobjPolicy.GetProcessID()).GetParent().GetData();
				lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), (lobjPolicy.getAt(11) == null ?
						(UUID)lobjClient.getAt(8) : (UUID)lobjPolicy.getAt(11)));
				lobjCompany = lobjPolicy.GetCompany();
				if ( mobjSubPolicy.midSubscriber == null )
					lobjClient = null;
				else
					lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), mobjSubPolicy.midSubscriber);
				if ( mobjSubPolicy.midProcess == null )
					lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjPolicy.GetProcessID());
				else
					lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjSubPolicy.midProcess);
				lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetManagerID());
				if ( mobjSubPolicy.midProcess == null )
					lobjProc = null;
				lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
						(mobjSubPolicy.midStatus == null ? Constants.StatusID_InProgress : mobjSubPolicy.midStatus));
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjResult = new SubPolicy();

			lobjResult.id = mid.toString();

			lobjResult.number = mobjSubPolicy.mstrNumber;
			lobjResult.mainPolicyId = lobjPolicy.getKey().toString();
			lobjResult.mainPolicyNumber = lobjPolicy.getLabel();
			lobjResult.clientId = ( mobjSubPolicy.midSubscriber == null ? null : mobjSubPolicy.midSubscriber.toString() );
			lobjResult.clientNumber = ( lobjClient == null ? null : ((Integer)lobjClient.getAt(1)).toString() );
			lobjResult.clientName = ( lobjClient == null ? null : lobjClient.getLabel() );
			lobjResult.processId = ( mobjSubPolicy.midProcess == null ? null : mobjSubPolicy.midProcess.toString() );
			lobjResult.managerId = lobjUser.getKey().toString();
			lobjResult.managerName = lobjUser.getDisplayName();
			lobjResult.startDate = ( mobjSubPolicy.mdtBeginDate == null ? null :
					mobjSubPolicy.mdtBeginDate.toString().substring(0, 10) );
			lobjResult.fractioningId = ( mobjSubPolicy.midFractioning == null ? null : mobjSubPolicy.midFractioning.toString() );
			lobjResult.expirationDate = ( mobjSubPolicy.mdtEndDate == null ? null :
					mobjSubPolicy.mdtEndDate.toString().substring(0, 10));
			lobjResult.notes = mobjSubPolicy.mstrNotes;
			lobjResult.inheritMediatorId = lobjMed.getKey().toString();
			lobjResult.inheritMediatorName = lobjMed.getLabel();
			lobjResult.inheritCategoryName = lobjPolicy.GetSubLine().getLine().getCategory().getLabel();
			lobjResult.inheritLineName = lobjPolicy.GetSubLine().getLine().getLabel();
			lobjResult.inheritSubLineId = lobjPolicy.GetSubLine().getKey().toString();
			lobjResult.inheritSubLineName = lobjPolicy.GetSubLine().getLabel();
			lobjResult.inheritCompanyName = lobjCompany.getLabel();
			lobjResult.statusId = lobjStatus.getKey().toString();
			lobjResult.statusText = lobjStatus.getLabel();
			switch ( (Integer)lobjStatus.getAt(1) )
			{
			case 0:
				lobjResult.statusIcon = SubPolicyStub.PolicyStatus.PROVISIONAL;
				break;

			case 1:
				lobjResult.statusIcon = SubPolicyStub.PolicyStatus.VALID;
				break;

			case 2:
				lobjResult.statusIcon = SubPolicyStub.PolicyStatus.OBSOLETE;
				break;
			}
			lobjResult.premium = ( mobjSubPolicy.mdblPremium == null ? null : mobjSubPolicy.mdblPremium.doubleValue() );
			lobjResult.docushare = mobjSubPolicy.mstrDocuShare;

			return lobjResult;
		}

		public void WriteResult(SubPolicy pobjResult)
			throws CorruptedPadException
		{
			ArrayList<SubPolicy.HeaderField> larrHeaders;
			ArrayList<SubPolicy.Coverage> larrAuxCoverages;
			ArrayList<SubPolicy.Coverage.Variability> larrVariability;
			Hashtable<Integer, SubPolicy.ColumnHeader> larrColumns;
			ArrayList<SubPolicy.TableSection.TableField> larrTableFields;
			ArrayList<SubPolicy.ExtraField> larrExtraFields;
			SubPolicy.HeaderField lobjHeader;
			SubPolicy.Coverage lobjAuxCoverage;
			SubPolicy.Coverage.Variability lobjVariability;
			SubPolicy.ColumnHeader lobjColumn;
			SubPolicy.TableSection.TableField lobjTableField;
			SubPolicy.ExtraField lobjExtraField;
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			pobjResult.id = mid.toString();

			larrHeaders = new ArrayList<SubPolicy.HeaderField>();
			for ( i = 0; i < marrValues.size(); i++ )
			{
				if ( !marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mlngObject >= 0) ||
						(marrValues.get(i).mlngExercise >= 0))
					continue;

				lobjHeader = new SubPolicy.HeaderField();
				lobjHeader.fieldId = marrValues.get(i).midField.toString();
				lobjHeader.fieldName = marrValues.get(i).mrefField.mstrLabel;
				lobjHeader.type = InsurancePolicyServiceImpl.GetFieldTypeByID(marrValues.get(i).mrefField.midType);
				lobjHeader.unitsLabel = marrValues.get(i).mrefField.mstrUnits;
				lobjHeader.refersToId = ( marrValues.get(i).mrefField.midRefersTo == null ? null :
						marrValues.get(i).mrefField.midRefersTo.toString() );
				lobjHeader.value = marrValues.get(i).mstrValue;
				lobjHeader.order = marrValues.get(i).mrefField.mlngColIndex;
				larrHeaders.add(lobjHeader);
			}
			pobjResult.headerFields = larrHeaders.toArray(new SubPolicy.HeaderField[larrHeaders.size()]);

			larrAuxCoverages = new ArrayList<SubPolicy.Coverage>();
			for ( i = 0; i < marrCoverages.size(); i++ )
			{
				if ( marrCoverages.get(i).mbIsHeader )
					continue;

				lobjAuxCoverage = new SubPolicy.Coverage();
				lobjAuxCoverage.coverageId = marrCoverages.get(i).midCoverage.toString();
				lobjAuxCoverage.coverageName = marrCoverages.get(i).mstrLabel;
				lobjAuxCoverage.mandatory = marrCoverages.get(i).mbMandatory;
				lobjAuxCoverage.order = marrCoverages.get(i).mlngOrder;
				lobjAuxCoverage.presentInPolicy = marrCoverages.get(i).mbPresent;
				larrVariability = new ArrayList<SubPolicy.Coverage.Variability>();
				for ( j = 0; j < marrCoverages.get(i).marrFields.length; j++ )
				{
					if ( marrCoverages.get(i).marrFields[j].mlngColIndex < 0 )
						continue;

					lobjVariability = new SubPolicy.Coverage.Variability();
					lobjVariability.columnIndex = marrCoverages.get(i).marrFields[j].mlngColIndex;
					lobjVariability.variesByObject = marrCoverages.get(i).marrFields[j].mbVariesByObject;
					lobjVariability.variesByExercise = marrCoverages.get(i).marrFields[j].mbVariesByExercise;
					larrVariability.add(lobjVariability);
				}
				lobjAuxCoverage.variability =
						larrVariability.toArray(new SubPolicy.Coverage.Variability[larrVariability.size()]);
				larrAuxCoverages.add(lobjAuxCoverage);
			}
			pobjResult.coverages = larrAuxCoverages.toArray(new SubPolicy.Coverage[larrAuxCoverages.size()]);

			larrColumns = new Hashtable<Integer, SubPolicy.ColumnHeader>();
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

					lobjColumn = new SubPolicy.ColumnHeader();
					lobjColumn.label = marrCoverages.get(i).marrFields[j].mstrLabel;
					lobjColumn.type = InsurancePolicyServiceImpl.GetFieldTypeByID(marrCoverages.get(i).marrFields[j].midType);
					lobjColumn.unitsLabel = marrCoverages.get(i).marrFields[j].mstrUnits;
					lobjColumn.refersToId = ( marrCoverages.get(i).marrFields[j].midRefersTo == null ? null :
						marrCoverages.get(i).marrFields[j].midRefersTo.toString() );
					larrColumns.put(marrCoverages.get(i).marrFields[j].mlngColIndex, lobjColumn);
				}
			}
			pobjResult.columns = new SubPolicy.ColumnHeader[larrColumns.size()];
			for ( Integer ii: larrColumns.keySet() )
				pobjResult.columns[ii] = larrColumns.get(ii);

			if ( pobjResult.coverages.length * pobjResult.columns.length == 0 )
				pobjResult.tableData = new SubPolicy.TableSection[0];
			else
			{
				pobjResult.tableData = new SubPolicy.TableSection[1];
				pobjResult.tableData[0] = new SubPolicy.TableSection();
				pobjResult.tableData[0].pageId = mid.toString() + ":-1:-1";
				larrTableFields = new ArrayList<SubPolicy.TableSection.TableField>();
				for ( i = 0; i < marrValues.size(); i++ )
				{
					if ( marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mrefField.mlngColIndex < 0) ||
							(marrValues.get(i).mlngObject >= 0) || (marrValues.get(i).mlngExercise >= 0))
						continue;

					lobjTableField = new SubPolicy.TableSection.TableField();
					lobjTableField.fieldId = marrValues.get(i).midField.toString();
					lobjTableField.coverageId = marrValues.get(i).mrefCoverage.midCoverage.toString();
					lobjTableField.columnIndex = marrValues.get(i).mrefField.mlngColIndex;
					lobjTableField.value = marrValues.get(i).mstrValue;
					larrTableFields.add(lobjTableField);
				}
				pobjResult.tableData[0].data =
						larrTableFields.toArray(new SubPolicy.TableSection.TableField[larrTableFields.size()]);
			}

			larrExtraFields = new ArrayList<SubPolicy.ExtraField>();
			for ( i = 0; i < marrValues.size(); i++ )
			{
				if ( marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mrefField.mlngColIndex >= 0) ||
						(marrValues.get(i).mlngObject >= 0) || (marrValues.get(i).mlngExercise >= 0) )
					continue;

				lobjExtraField = new SubPolicy.ExtraField();
				lobjExtraField.fieldId = marrValues.get(i).midField.toString();
				lobjExtraField.fieldName = marrValues.get(i).mrefField.mstrLabel;
				lobjExtraField.type = InsurancePolicyServiceImpl.GetFieldTypeByID(marrValues.get(i).mrefField.midType);
				lobjExtraField.unitsLabel = marrValues.get(i).mrefField.mstrUnits;
				lobjExtraField.refersToId = ( marrValues.get(i).mrefField.midRefersTo == null ? null :
						marrValues.get(i).mrefField.midRefersTo.toString() );
				lobjExtraField.value = marrValues.get(i).mstrValue;
				lobjExtraField.order = marrValues.get(i).mrefField.mlngColIndex;
				lobjExtraField.coverageId = marrValues.get(i).mrefCoverage.midCoverage.toString();
				larrExtraFields.add(lobjExtraField);
			}
			pobjResult.extraData = larrExtraFields.toArray(new SubPolicy.ExtraField[larrExtraFields.size()]);

			java.util.Arrays.sort(pobjResult.headerFields, new Comparator<SubPolicy.HeaderField>()
			{
				public int compare(SubPolicy.HeaderField o1, SubPolicy.HeaderField o2)
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
			java.util.Arrays.sort(pobjResult.coverages, new Comparator<SubPolicy.Coverage>()
			{
				public int compare(SubPolicy.Coverage o1, SubPolicy.Coverage o2)
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
			java.util.Arrays.sort(pobjResult.extraData, new Comparator<SubPolicy.ExtraField>()
			{
				public int compare(SubPolicy.ExtraField o1, SubPolicy.ExtraField o2)
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

		public void WritePage(SubPolicy.TableSection pobjResult, int plngObject, int plngExercise)
			throws CorruptedPadException
		{
			int llngCoverages;
			int llngColumns;
			ArrayList<SubPolicy.TableSection.TableField> larrTableFields;
			SubPolicy.TableSection.TableField lobjTableField;
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
				pobjResult.data = new SubPolicy.TableSection.TableField[0];
			else
			{
				larrTableFields = new ArrayList<SubPolicy.TableSection.TableField>();
				for ( i = 0; i < marrValues.size(); i++ )
				{
					if ( marrValues.get(i).mrefCoverage.mbIsHeader || (marrValues.get(i).mrefField.mlngColIndex < 0) ||
							(marrValues.get(i).mlngObject != plngObject) || (marrValues.get(i).mlngExercise != plngExercise) )
						continue;

					lobjTableField = new SubPolicy.TableSection.TableField();
					lobjTableField.fieldId = marrValues.get(i).midField.toString();
					lobjTableField.coverageId = marrValues.get(i).mrefCoverage.midCoverage.toString();
					lobjTableField.columnIndex = marrValues.get(i).mrefField.mlngColIndex;
					lobjTableField.value = marrValues.get(i).mstrValue;
					larrTableFields.add(lobjTableField);
				}
				pobjResult.data = larrTableFields.toArray(new SubPolicy.TableSection.TableField[larrTableFields.size()]);
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

		public void WriteObject(InsuredObject pobjResult, int plngObject)
			throws BigBangException, CorruptedPadException
		{
			SubPolicyObjectData lobjObject;
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
						lobjVariable.type = InsurancePolicyServiceImpl.GetFieldTypeByID(lobjValue.mrefField.midType);
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
					lobjFixed.type = InsurancePolicyServiceImpl.GetFieldTypeByID(lobjValue.mrefField.midType);
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
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

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
						lobjVariable.type = InsurancePolicyServiceImpl.GetFieldTypeByID(lobjValue.mrefField.midType);
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
					lobjFixed.type = InsurancePolicyServiceImpl.GetFieldTypeByID(lobjValue.mrefField.midType);
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

		public void UpdateInvariants(SubPolicy pobjSource)
			throws BigBangException, CorruptedPadException
		{
			int i, j;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			midMainPolicy = ( pobjSource.mainPolicyId == null ? null : UUID.fromString(pobjSource.mainPolicyId) );
			mobjSubPolicy.mstrNumber = pobjSource.number;
			mobjSubPolicy.midSubscriber = ( pobjSource.clientId == null ? null : UUID.fromString(pobjSource.clientId) );
			mobjSubPolicy.mdtBeginDate = ( pobjSource.startDate == null ? null :
					Timestamp.valueOf(pobjSource.startDate + " 00:00:00.0") );
			mobjSubPolicy.midFractioning = ( pobjSource.fractioningId == null ? null :
					UUID.fromString(pobjSource.fractioningId) );
			mobjSubPolicy.mdtEndDate = ( pobjSource.expirationDate == null ? null :
					Timestamp.valueOf(pobjSource.expirationDate + " 00:00:00.0") );
			mobjSubPolicy.mstrNotes = pobjSource.notes;
			mobjSubPolicy.mdblPremium = ( pobjSource.premium == null ? null : new BigDecimal(pobjSource.premium+"") );

			mobjSubPolicy.mbModified = true;

			mbValid = false;

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

		public void UpdatePage(SubPolicy.TableSection pobjSource, int plngObject, int plngExercise)
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
				lobjObject.midType = (UUID)Policy.GetInstance(Engine.getCurrentNameSpace(), midMainPolicy).GetSubLine().getAt(2);
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
				lidType = (UUID)Policy.GetInstance(Engine.getCurrentNameSpace(), midMainPolicy).GetSubLine().getAt(2);
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
			lobjObject.midOwner = mobjSubPolicy.mid;
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

		public void CommitChanges()
			throws BigBangException, CorruptedPadException 
		{
			SubPolicyData lobjData;
			PolicyExerciseData[] larrAuxExercises;
			int i;

			if ( !mbValid )
				throw new CorruptedPadException("Ocorreu um erro interno. Os dados correntes não são válidos.");

			lobjData = new SubPolicyData();
			lobjData.Clone(mobjSubPolicy);

			lobjData.mbModified = mobjSubPolicy.mbModified;

			if ( marrCoverages.size() > 0 )
			{
				lobjData.marrCoverages = new SubPolicyCoverageData[marrCoverages.size()];
				for ( i = 0; i < marrCoverages.size(); i++ )
				{
					lobjData.marrCoverages[i] = new SubPolicyCoverageData();
					lobjData.marrCoverages[i].Clone(marrCoverages.get(i));
					if ( marrCoverages.get(i).mid == null )
						lobjData.marrCoverages[i].mbNew = true;
				}
			}
			else
				lobjData.marrCoverages = null;

			if ( marrObjects.size() > 0 )
			{
				lobjData.marrObjects = new SubPolicyObjectData[marrObjects.size()];
				for ( i = 0; i < marrObjects.size(); i++ )
				{
					lobjData.marrObjects[i] = new SubPolicyObjectData();
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
				larrAuxExercises = new PolicyExerciseData[marrExercises.size()];
				for ( i = 0; i < marrExercises.size(); i++ )
				{
					larrAuxExercises[i] = new PolicyExerciseData();
					larrAuxExercises[i].Clone(marrExercises.get(i));
				}
			}
			else
				larrAuxExercises = null;

			if ( marrValues.size() > 0 )
			{
				lobjData.marrValues = new SubPolicyValueData[marrValues.size()];
				for ( i = 0; i < marrValues.size(); i++ )
				{
					lobjData.marrValues[i] = new SubPolicyValueData();
					lobjData.marrValues[i].Clone(marrValues.get(i));
					if ( marrValues.get(i).mbDeleted )
						lobjData.marrValues[i].mbDeleted = true;
					else if ( marrValues.get(i).mid == null )
						lobjData.marrValues[i].mbNew = true;
				}
			}
			else
				lobjData.marrValues = null;

			if ( mobjSubPolicy.mid == null )
				CommitNew(lobjData, larrAuxExercises);
			else
				CommitEdit(lobjData, larrAuxExercises);
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

		private void CommitNew(SubPolicyData pobjData, PolicyExerciseData[] parrAuxExercises)
			throws BigBangException
		{
			CreateSubPolicy lopCSP;
			int i;

			if ( midMainPolicy == null )
				throw new BigBangException("Erro: Não preencheu o identificador da apólice-mãe.");

			try
			{
				lopCSP = new CreateSubPolicy(Policy.GetInstance(Engine.getCurrentNameSpace(), midMainPolicy).GetProcessID());
				lopCSP.mobjData = pobjData;
				lopCSP.marrAuxExercises = parrAuxExercises;

				lopCSP.mobjContactOps = null;
				lopCSP.mobjDocOps = null;

				lopCSP.Execute();

				mobjSubPolicy.mid = lopCSP.mobjData.mid;

				if ( lopCSP.mobjData.marrObjects != null )
				{
					for ( i = 0; i < lopCSP.mobjData.marrObjects.length; i++ )
						if ( lopCSP.mobjData.marrObjects[i].mbNew )
							marrObjects.get(i).mid = lopCSP.mobjData.marrObjects[i].mid;
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}

		private void CommitEdit(SubPolicyData pobjData, PolicyExerciseData[] parrAuxExercises)
			throws BigBangException
		{
			ManageData lopMD;
			int i;

			try
			{
				lopMD = new ManageData(mobjSubPolicy.midProcess);
				lopMD.mobjData = pobjData;
				lopMD.marrAuxExercises = parrAuxExercises;

				lopMD.mobjContactOps = null;
				lopMD.mobjDocOps = null;

				lopMD.Execute();

				if ( lopMD.mobjData.marrObjects != null )
				{
					for ( i = 0; i < lopMD.mobjData.marrObjects.length; i++ )
						if ( lopMD.mobjData.marrObjects[i].mbNew )
							marrObjects.get(i).mid = lopMD.mobjData.marrObjects[i].mid;
				}

			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Hashtable<UUID, SubPolicyScratchPad> GetScratchPadStorage()
	{
		Hashtable<UUID, SubPolicyScratchPad> larrAux;

        if (getSession() == null)
            return null;

        larrAux = (Hashtable<UUID, SubPolicyScratchPad>)getSession().getAttribute("BigBang_SubPolicy_ScratchPad_Storage");
        if (larrAux == null)
        {
        	larrAux = new Hashtable<UUID, SubPolicyScratchPad>();
            getSession().setAttribute("BigBang_SubPolicy_ScratchPad_Storage", larrAux);
        }

        return larrAux;
	}

	public static SubPolicy sGetSubPolicy(UUID pidSubPolicy)
		throws BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		Policy lobjMainPolicy;
		SubPolicy lobjResult;
		IProcess lobjProc;
		Client lobjClient;
		Mediator lobjMed;
		ObjectBase lobjStatus;
		SubPolicyValue[] larrLocalValues;
		SubPolicyCoverage[] larrLocalCoverages;
		Coverage[] larrCoverages;
		Company lobjCompany;
		IUser lobjUser;
		Hashtable<UUID, Tax> larrAuxFields;
		ArrayList<SubPolicy.HeaderField> larrOutHeaders;
		ArrayList<SubPolicy.TableSection.TableField> larrOutFields;
		ArrayList<SubPolicy.ExtraField> larrOutExtras;
		Tax lobjTax;
		SubPolicy.HeaderField lobjHeader;
		SubPolicy.TableSection.TableField lobjField;
		SubPolicy.ExtraField lobjExtra;
		Hashtable<UUID, Coverage> larrAuxCoverages;
		ArrayList<SubPolicy.Coverage> larrOutCoverages;
		Hashtable<Integer, SubPolicy.ColumnHeader> larrOutColumns;
		Coverage lobjCoverage;
		SubPolicy.ColumnHeader lobjColumnHeader;
		Tax[] larrTaxes;
		SubPolicy.Coverage lobjAuxCoverage;
		ArrayList<SubPolicy.Coverage.Variability> larrVariability;
		SubPolicy.Coverage.Variability lobjVariability;
		SubPolicy.TableSection lobjSection;
		int i, j;

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(), pidSubPolicy);
			if ( lobjSubPolicy.GetProcessID() == null )
				throw new BigBangException("Erro: Apólice adesão sem processo de suporte. (Apólice adesão n. "
						+ lobjSubPolicy.getLabel() + ")");
			lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjSubPolicy.GetProcessID());
			lobjMainPolicy = lobjSubPolicy.GetOwner();
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubPolicy.getAt(2));
			lobjMed = Mediator.GetInstance(Engine.getCurrentNameSpace(), ( lobjMainPolicy.getAt(11) == null ?
					(UUID)lobjClient.getAt(8) : (UUID)lobjMainPolicy.getAt(11) ));
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
					(UUID)lobjSubPolicy.getAt(7));
			larrLocalValues = lobjSubPolicy.GetCurrentKeyedValues(null, null);
			larrLocalCoverages = lobjSubPolicy.GetCurrentCoverages();
			larrCoverages = lobjMainPolicy.GetSubLine().GetCurrentCoverages();
			lobjCompany = lobjMainPolicy.GetCompany();
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), lobjProc.GetManagerID());
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new SubPolicy();

		lobjResult.id = lobjSubPolicy.getKey().toString();
		lobjResult.number = (String)lobjSubPolicy.getAt(0);
		lobjResult.mainPolicyId = lobjMainPolicy.getKey().toString();
		lobjResult.mainPolicyNumber = lobjMainPolicy.getLabel();
		lobjResult.clientId = lobjClient.getKey().toString();
		lobjResult.clientNumber = ((Integer)lobjClient.getAt(1)).toString();
		lobjResult.clientName = lobjClient.getLabel();
		lobjResult.processId = lobjProc.getKey().toString();
		lobjResult.startDate = (lobjSubPolicy.getAt(3) == null ? null :
				((Timestamp)lobjSubPolicy.getAt(3)).toString().substring(0, 10));
		lobjResult.fractioningId = ((UUID)lobjSubPolicy.getAt(5)).toString();
		lobjResult.expirationDate = (lobjSubPolicy.getAt(4) == null ? null :
				((Timestamp)lobjSubPolicy.getAt(4)).toString().substring(0, 10));
		lobjResult.notes = (String)lobjSubPolicy.getAt(6);
		lobjResult.inheritMediatorId = lobjMed.getKey().toString();
		lobjResult.inheritMediatorName = lobjMed.getLabel();
		lobjResult.inheritCategoryName = lobjMainPolicy.GetSubLine().getLine().getCategory().getLabel();
		lobjResult.inheritLineName = lobjMainPolicy.GetSubLine().getLine().getLabel();
		lobjResult.inheritSubLineId = lobjMainPolicy.GetSubLine().getKey().toString();
		lobjResult.inheritSubLineName = lobjMainPolicy.GetSubLine().getLabel();
		lobjResult.inheritCompanyName = lobjCompany.getLabel();
		lobjResult.statusId = lobjStatus.getKey().toString();
		lobjResult.statusText = lobjStatus.getLabel();
		switch ( (Integer)lobjStatus.getAt(1) )
		{
		case 0:
			lobjResult.statusIcon = SubPolicyStub.PolicyStatus.PROVISIONAL;
			break;

		case 1:
			lobjResult.statusIcon = SubPolicyStub.PolicyStatus.VALID;
			break;

		case 2:
			lobjResult.statusIcon = SubPolicyStub.PolicyStatus.OBSOLETE;
			break;
		}
		lobjResult.premium = (lobjSubPolicy.getAt(8) == null ? null : ((BigDecimal)lobjSubPolicy.getAt(8)).doubleValue());
		lobjResult.docushare = (String)lobjSubPolicy.getAt(9);
		lobjResult.managerId = lobjUser.getKey().toString();
		lobjResult.managerName = lobjUser.getDisplayName();

		larrAuxFields = new Hashtable<UUID, Tax>();
		larrOutHeaders = new ArrayList<SubPolicy.HeaderField>();
		larrOutFields = new ArrayList<SubPolicy.TableSection.TableField>();
		larrOutExtras = new ArrayList<SubPolicy.ExtraField>();
		for ( i = 0; i < larrLocalValues.length; i++ )
		{
			if ( (larrLocalValues[i].getAt(3) != null) || (larrLocalValues[i].getAt(4) != null) )
				continue;
			lobjTax = larrLocalValues[i].GetTax();

			if ( lobjTax.GetCoverage().IsHeader() )
			{
				lobjHeader = new SubPolicy.HeaderField();
				lobjHeader.fieldId = lobjTax.getKey().toString();
				lobjHeader.fieldName = lobjTax.getLabel();
				lobjHeader.type = InsurancePolicyServiceImpl.GetFieldTypeByID((UUID)lobjTax.getAt(2));
				lobjHeader.unitsLabel = (String)lobjTax.getAt(3);
				lobjHeader.refersToId = ( lobjTax.getAt(7) == null ? null : ((UUID)lobjTax.getAt(7)).toString() );
				lobjHeader.value = larrLocalValues[i].getLabel();
				lobjHeader.order = (Integer)lobjTax.getAt(8);
				larrOutHeaders.add(lobjHeader);
			}
			else if ( lobjTax.GetColumnOrder() >= 0 )
			{
				lobjField = new SubPolicy.TableSection.TableField();
				lobjField.fieldId = lobjTax.getKey().toString();
				lobjField.coverageId = lobjTax.GetCoverage().getKey().toString();
				lobjField.columnIndex = lobjTax.GetColumnOrder();
				lobjField.value = larrLocalValues[i].getLabel();
				larrOutFields.add(lobjField);
			}
			else
			{
				lobjExtra = new SubPolicy.ExtraField();
				lobjExtra.fieldId = lobjTax.getKey().toString();
				lobjExtra.fieldName = lobjTax.getLabel();
				lobjExtra.type = InsurancePolicyServiceImpl.GetFieldTypeByID((UUID)lobjTax.getAt(2));
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
		larrOutCoverages = new ArrayList<SubPolicy.Coverage>();
		larrOutColumns = new Hashtable<Integer, SubPolicy.ColumnHeader>();
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

			lobjAuxCoverage = new SubPolicy.Coverage();
			lobjAuxCoverage.coverageId = lobjCoverage.getKey().toString();
			lobjAuxCoverage.coverageName = lobjCoverage.getLabel();
			lobjAuxCoverage.mandatory = (Boolean)lobjCoverage.getAt(2);
			lobjAuxCoverage.order = ( lobjCoverage.getAt(5) == null ? 0 : (Integer)lobjCoverage.getAt(5) );
			lobjAuxCoverage.presentInPolicy = (Boolean)larrLocalCoverages[i].getAt(2);
			larrVariability = new ArrayList<SubPolicy.Coverage.Variability>();
			for ( j = 0; j < larrTaxes.length ; j++ )
			{
				if ( larrTaxes[j].GetColumnOrder() < 0 )
					continue;

				if ( !larrOutColumns.containsKey(larrTaxes[j].GetColumnOrder()) )
				{
					lobjColumnHeader = new SubPolicy.ColumnHeader();
					lobjColumnHeader.label = larrTaxes[j].getLabel();
					lobjColumnHeader.type = InsurancePolicyServiceImpl.GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
					lobjColumnHeader.unitsLabel = (String)larrTaxes[j].getAt(3);
					lobjColumnHeader.refersToId = ( larrTaxes[j].getAt(7) == null ? null : ((UUID)larrTaxes[j].getAt(7)).toString() );
					larrOutColumns.put(larrTaxes[j].GetColumnOrder(), lobjColumnHeader);
				}
				lobjVariability = new SubPolicy.Coverage.Variability();
				lobjVariability.columnIndex = larrTaxes[j].GetColumnOrder();
				lobjVariability.variesByObject = larrTaxes[j].GetVariesByObject();
				lobjVariability.variesByExercise = larrTaxes[j].GetVariesByExercise();
				larrVariability.add(lobjVariability);

			}
			lobjAuxCoverage.variability = larrVariability.toArray(new SubPolicy.Coverage.Variability[larrVariability.size()]);
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
					lobjColumnHeader = new SubPolicy.ColumnHeader();
					lobjColumnHeader.label = larrTaxes[j].getLabel();
					lobjColumnHeader.type = InsurancePolicyServiceImpl.GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
					lobjColumnHeader.unitsLabel = (String)larrTaxes[j].getAt(3);
					lobjColumnHeader.refersToId = ( larrTaxes[j].getAt(7) == null ? null : ((UUID)larrTaxes[j].getAt(7)).toString() );
					larrOutColumns.put(larrTaxes[j].GetColumnOrder(), lobjColumnHeader);
				}

				if ( larrAuxFields.get(larrTaxes[j].getKey()) != null )
					continue;

				if ( larrTaxes[j].GetVariesByObject() || larrTaxes[j].GetVariesByExercise() )
					continue;

				if ( larrCoverages[i].IsHeader() )
				{
					lobjHeader = new SubPolicy.HeaderField();
					lobjHeader.fieldId = larrTaxes[j].getKey().toString();
					lobjHeader.fieldName = larrTaxes[j].getLabel();
					lobjHeader.type = InsurancePolicyServiceImpl.GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
					lobjHeader.unitsLabel = (String)larrTaxes[j].getAt(3);
					lobjHeader.refersToId = ( larrTaxes[j].getAt(7) == null ? null : ((UUID)larrTaxes[j].getAt(7)).toString() );
					lobjHeader.value = (String)larrTaxes[j].getAt(4);
					lobjHeader.order = (Integer)larrTaxes[j].getAt(8);
					larrOutHeaders.add(lobjHeader);
				}
				else if ( larrTaxes[j].GetColumnOrder() >= 0 )
				{
					lobjField = new SubPolicy.TableSection.TableField();
					lobjField.fieldId = larrTaxes[j].getKey().toString();
					lobjField.coverageId = larrTaxes[j].GetCoverage().getKey().toString();
					lobjField.columnIndex = larrTaxes[j].GetColumnOrder();
					lobjField.value = (String)larrTaxes[j].getAt(4);
					larrOutFields.add(lobjField);
				}
				else
				{
					lobjExtra = new SubPolicy.ExtraField();
					lobjExtra.fieldId = larrTaxes[j].getKey().toString();
					lobjExtra.fieldName = larrTaxes[j].getLabel();
					lobjExtra.type = InsurancePolicyServiceImpl.GetFieldTypeByID((UUID)larrTaxes[j].getAt(2));
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

			lobjAuxCoverage = new SubPolicy.Coverage();
			lobjAuxCoverage.coverageId = larrCoverages[i].getKey().toString();
			lobjAuxCoverage.coverageName = larrCoverages[i].getLabel();
			lobjAuxCoverage.mandatory = (Boolean)larrCoverages[i].getAt(2);
			lobjAuxCoverage.presentInPolicy = null;
			larrVariability = new ArrayList<SubPolicy.Coverage.Variability>();
			for ( j = 0; j < larrTaxes.length ; j++ )
			{
				if ( larrTaxes[j].GetColumnOrder() < 0 )
					continue;

				lobjVariability = new SubPolicy.Coverage.Variability();
				lobjVariability.columnIndex = larrTaxes[j].GetColumnOrder();
				lobjVariability.variesByObject = larrTaxes[j].GetVariesByObject();
				lobjVariability.variesByExercise = larrTaxes[j].GetVariesByExercise();
				larrVariability.add(lobjVariability);

			}
			lobjAuxCoverage.variability = larrVariability.toArray(new SubPolicy.Coverage.Variability[larrVariability.size()]);
			larrOutCoverages.add(lobjAuxCoverage);
			larrAuxCoverages.put(larrCoverages[i].getKey(), larrCoverages[i]);
		}

		lobjSection = new SubPolicy.TableSection();
		lobjSection.pageId = null;
		lobjSection.data = larrOutFields.toArray(new SubPolicy.TableSection.TableField[larrOutFields.size()]);

		lobjResult.headerFields = larrOutHeaders.toArray(new SubPolicy.HeaderField[larrOutHeaders.size()]);
		lobjResult.coverages = larrOutCoverages.toArray(new SubPolicy.Coverage[larrOutCoverages.size()]);
		lobjResult.columns = new SubPolicy.ColumnHeader[larrOutColumns.size()];
		for ( Integer ii: larrOutColumns.keySet() )
			lobjResult.columns[ii] = larrOutColumns.get(ii);
		lobjResult.tableData = new SubPolicy.TableSection[] { lobjSection };
		lobjResult.extraData = larrOutExtras.toArray(new SubPolicy.ExtraField[larrOutExtras.size()]);

		java.util.Arrays.sort(lobjResult.headerFields, new Comparator<SubPolicy.HeaderField>()
		{
			public int compare(SubPolicy.HeaderField o1, SubPolicy.HeaderField o2)
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
		java.util.Arrays.sort(lobjResult.coverages, new Comparator<SubPolicy.Coverage>()
		{
			public int compare(SubPolicy.Coverage o1, SubPolicy.Coverage o2)
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
		java.util.Arrays.sort(lobjResult.extraData, new Comparator<SubPolicy.ExtraField>()
		{
			public int compare(SubPolicy.ExtraField o1, SubPolicy.ExtraField o2)
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

	public SubPolicy getSubPolicy(String subPolicyId)
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		return sGetSubPolicy(UUID.fromString(subPolicyId));
	}

	public TableSection getPage(String subPolicyId, String objectId, String exerciseId)
		throws SessionExpiredException, BigBangException
	{
		UUID lidObject;
		UUID lidExercise;
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		Coverage[] larrCoverages;
		SubPolicyValue[] larrValues;
		SubPolicy.TableSection lobjResult;
		Hashtable<UUID, Tax> larrAuxFields;
		ArrayList<SubPolicy.TableSection.TableField> larrFields;
		Tax lobjTax;
		SubPolicy.TableSection.TableField lobjField;
		Tax[] larrTaxes;
		int i, j;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lidObject = (objectId == null ? null : UUID.fromString(objectId));
		lidExercise = (exerciseId == null ? null : UUID.fromString(exerciseId));

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicyId));
			larrCoverages = lobjSubPolicy.GetOwner().GetSubLine().GetCurrentCoverages();

			larrValues = lobjSubPolicy.GetCurrentKeyedValues(lidObject, lidExercise);
			larrAuxFields = new Hashtable<UUID, Tax>();
			larrFields = new ArrayList<SubPolicy.TableSection.TableField>();
			for ( i = 0; i < larrValues.length; i++ )
			{
				lobjTax = larrValues[i].GetTax();

				if ( lobjTax.GetCoverage().IsHeader() || (lobjTax.GetColumnOrder() < 0) )
					continue;

				lobjField = new SubPolicy.TableSection.TableField();
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

					if ( (larrTaxes[j].GetVariesByObject() == (lidObject == null)) ||
							(larrTaxes[j].GetVariesByExercise() == (lidExercise == null)) )
						continue;

					lobjField = new SubPolicy.TableSection.TableField();
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

		lobjResult = new SubPolicy.TableSection();
		lobjResult.pageId = null;
		lobjResult.data = larrFields.toArray(new SubPolicy.TableSection.TableField[larrFields.size()]);
		return lobjResult;
	}

	public Remap[] openSubPolicyScratchPad(String subPolicyId)
		throws SessionExpiredException, BigBangException
	{
		SubPolicyScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjPad = new SubPolicyScratchPad();
		if ( subPolicyId != null )
			lobjPad.OpenForEdit(UUID.fromString(subPolicyId));
		GetScratchPadStorage().put(lobjPad.GetID(), lobjPad);

		return lobjPad.GetRemapIntoPad();
	}

	public SubPolicy initSubPolicyInPad(SubPolicy subPolicy)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		SubPolicyScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( subPolicy.id == null )
			throw new BigBangException("Erro: Não pode inicializar uma apólice adesão sem abrir o espaço de trabalho.");

		if ( subPolicy.mainPolicyId == null )
			throw new BigBangException("Erro: Não pode inicializar uma apólice adesão antes de preencher a apólice-mãe.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(subPolicy.id));
		lobjPad.InitNew(subPolicy);

		subPolicy = lobjPad.WriteBasics();
		lobjPad.WriteResult(subPolicy);
		return subPolicy;
	}

	public SubPolicy getSubPolicyInPad(String subPolicyId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		SubPolicyScratchPad lobjPad;
		SubPolicy lobjPolicy;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lobjPad = GetScratchPadStorage().get(UUID.fromString(subPolicyId));
		lobjPolicy = lobjPad.WriteBasics();
		lobjPad.WriteResult(lobjPolicy);
		return lobjPolicy;
	}

	public SubPolicy updateHeader(SubPolicy subPolicy)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		SubPolicyScratchPad lobjPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( subPolicy.id == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(subPolicy.id));
		lobjPad.UpdateInvariants(subPolicy);

		lobjPad.WriteResult(subPolicy);
		return subPolicy;
	}

	public SubPolicy.TableSection getPageForEdit(String subPolicyId, String objectId, String exerciseId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;
		int llngExercise;
		SubPolicyScratchPad lobjPad;
		SubPolicy.TableSection lobjResult;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( subPolicyId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");
		lidPad = UUID.fromString(subPolicyId);

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

		lobjResult = new SubPolicy.TableSection();
		lobjPad.WritePage(lobjResult, llngObject, llngExercise);
		return lobjResult;
	}

	public TableSection savePage(TableSection data)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		String[] larrAux;
		UUID lidPad;
		int llngObject;
		int llngExercise;
		SubPolicyScratchPad lobjPad;

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
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		SubPolicyCoverage[] larrCoverages;
		ArrayList<TipifiedListItem> larrResult;
		TipifiedListItem lobjAux;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( filterId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		try
		{
			if ( Constants.ObjID_SubPolicyObject.equals(UUID.fromString(listId)) )
				return GetScratchPadStorage().get(UUID.fromString(filterId)).GetObjects();

			if ( Constants.ObjID_PolicyExercise.equals(UUID.fromString(listId)) )
				return GetScratchPadStorage().get(UUID.fromString(filterId)).GetExercises();

			if ( Constants.ObjID_SubPolicyCoverage.equals(UUID.fromString(listId)) )
			{
				lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
						UUID.fromString(filterId));
				larrCoverages = lobjSubPolicy.GetCurrentCoverages();
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
		SubPolicyScratchPad lobjPad;
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

	public InsuredObject createObjectInPad(String subPolicyId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		SubPolicyScratchPad lobjPad;
		InsuredObject lobjResult;
		int llngObject;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( subPolicyId == null )
			throw new BigBangException("Erro: Espaço de trabalho não existente.");

		lobjPad = GetScratchPadStorage().get(UUID.fromString(subPolicyId));
		llngObject = lobjPad.CreateNewObject();

		lobjResult = new InsuredObject();
		lobjPad.WriteObject(lobjResult, llngObject);
		return lobjResult;
	}

	@Override
	public InsuredObject createObjectFromClientInPad(String subPolicyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	public InsuredObject updateObjectInPad(InsuredObject data)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		SubPolicyScratchPad lobjPad;
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
		SubPolicyScratchPad lobjPad;
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

	public Remap[] commitPad(String subPolicyId)
		throws SessionExpiredException, BigBangException, CorruptedPadException
	{
		SubPolicyScratchPad lrefPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefPad = GetScratchPadStorage().get(UUID.fromString(subPolicyId));
		lrefPad.CommitChanges();
		GetScratchPadStorage().remove(UUID.fromString(subPolicyId));
		return lrefPad.GetRemapFromPad(true);
	}

	public Remap[] discardPad(String subPolicyId)
		throws SessionExpiredException, BigBangException
	{
		SubPolicyScratchPad lrefPad;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		lrefPad = GetScratchPadStorage().get(UUID.fromString(subPolicyId));
		GetScratchPadStorage().remove(UUID.fromString(subPolicyId));
		return lrefPad.GetRemapFromPad(false);
	}

	public SubPolicy performCalculations(String subPolicyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyCalculationException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		PerformComputations lopPC;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopPC = new PerformComputations(lobjSubPolicy.GetProcessID());

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

		return sGetSubPolicy(lobjSubPolicy.getKey());
	}

	public void validateSubPolicy(String subPolicyId)
		throws SessionExpiredException, BigBangException, BigBangPolicyValidationException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		ValidateSubPolicy lopVSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVSP = new ValidateSubPolicy(lobjSubPolicy.GetProcessID());

		try
		{
			lopVSP.Execute();
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
	public InsuredObject includeObject(String subPolicyId, InsuredObject object)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsuredObject includeObjectFromClient(String subPolicyId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excludeObject(String subPolicyId, String objectId)
			throws SessionExpiredException, BigBangException {
		// TODO Auto-generated method stub
		
	}

	public SubPolicy transferToPolicy(String subPolicyId, String newPolicyId)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		TransferToPolicy lopTTP;
		Policy lobjPolicy;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(subPolicyId));
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(newPolicyId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopTTP = new TransferToPolicy(lobjSubPolicy.GetProcessID());
		lopTTP.midNewProcess = lobjPolicy.GetProcessID();

		try
		{
			lopTTP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getSubPolicy(subPolicyId);
	}

	public SubPolicy voidSubPolicy(PolicyVoiding voiding)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		ObjectBase lobjMotive;
		VoidSubPolicy lopVSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(voiding.policyId));
			lobjMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_PolicyVoidingMotives), UUID.fromString(voiding.motiveId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopVSP = new VoidSubPolicy(lobjSubPolicy.GetProcessID());
		lopVSP.mdtEffectDate = Timestamp.valueOf(voiding.effectDate + " 00:00:00.0");
		lopVSP.mstrMotive = lobjMotive.getLabel();
		lopVSP.mstrNotes = voiding.notes;

		try
		{
			lopVSP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return getSubPolicy(voiding.policyId);
	}

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		CreateInfoRequest lopCIR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(request.parentDataObjectId));

			lopCIR = new CreateInfoRequest(lobjSubPolicy.GetProcessID());
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

	public Receipt createReceipt(String policyId, Receipt receipt)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		CreateReceipt lopCR;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(policyId));

			lopCR = new CreateReceipt(lobjSubPolicy.GetProcessID());
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
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjSubPolicy;
		CreateExpense lopCE;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		if ( !Constants.ObjID_SubPolicy.equals(UUID.fromString(expense.referenceTypeId)) )
			throw new BigBangException("Erro: Tentativa de criar despesa de saúde de apólice a partir de uma apólice adesão.");

		try
		{
			lobjSubPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(expense.referenceId));
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lopCE = new CreateExpense(lobjSubPolicy.GetProcessID());
		lopCE.mobjData = new ExpenseData();
		lopCE.mobjData.mid = null;
		lopCE.mobjData.mstrNumber = null;
		lopCE.mobjData.mdtDate = Timestamp.valueOf(expense.expenseDate + " 00:00:00.0");
		lopCE.mobjData.midPolicyObject = null;
		lopCE.mobjData.midSubPolicyObject = (expense.insuredObjectId == null ? null : UUID.fromString(expense.insuredObjectId));
		lopCE.mobjData.midPolicyCoverage = null;
		lopCE.mobjData.midSubPolicyCoverage = (expense.coverageId == null ? null : UUID.fromString(expense.coverageId));
		lopCE.mobjData.mdblDamages = new BigDecimal(expense.value+"");
		lopCE.mobjData.mdblSettlement = (expense.settlement == null ? null : new BigDecimal(expense.settlement+""));
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

	public void deleteSubPolicy(String policyId, String reason)
		throws SessionExpiredException, BigBangException
	{
		com.premiumminds.BigBang.Jewel.Objects.SubPolicy lobjPolicy;
		DeleteSubPolicy lobjDSP;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjPolicy = com.premiumminds.BigBang.Jewel.Objects.SubPolicy.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(policyId));

			lobjDSP = new DeleteSubPolicy(lobjPolicy.GetProcessID());
			lobjDSP.midSubPolicy = UUID.fromString(policyId);
			lobjDSP.mstrReason = reason;
			lobjDSP.Execute();
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
	}

	protected UUID getObjectID()
	{
		return Constants.ObjID_SubPolicy;
	}

	protected String[] getColumns()
	{
		return new String[] {"[:Number]", "[:Process]", "[:Subscriber]", "[:Subscriber:Number]", "[:Subscriber:Name]",
				"[:Status]", "[:Status:Status]", "[:Status:Level]"};
	}

	protected boolean buildFilter(StringBuilder pstrBuffer, SearchParameter pParam)
		throws BigBangException
	{
		SubPolicySearchParameter lParam;
		String lstrAux;
		IEntity lrefPolicies;

		if ( !(pParam instanceof SubPolicySearchParameter) )
			return false;
		lParam = (SubPolicySearchParameter)pParam;

		if ( (lParam.freeText != null) && (lParam.freeText.trim().length() > 0) )
		{
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			pstrBuffer.append(" AND ([:Number] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR [:Subscriber:Name] LIKE N'%").append(lstrAux).append("%'")
					.append(" OR CAST([:Subscriber:Number] AS NVARCHAR(20)) LIKE N'%").append(lstrAux).append("%')");
		}

		if ( lParam.ownerId != null )
		{
			pstrBuffer.append(" AND [:Process:Parent] IN (SELECT [:Process] FROM (");
			try
			{
				lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
				pstrBuffer.append(lrefPolicies.SQLForSelectMulti());
			}
			catch (Throwable e)
			{
        		throw new BigBangException(e.getMessage(), e);
			}
			pstrBuffer.append(") [AuxOwner] WHERE [:Process:Data] = '").append(lParam.ownerId).append("')");
		}

		if ( lParam.clientId != null )
		{
			pstrBuffer.append(" AND [:Subscriber] = '").append(lParam.clientId).append("'");
		}

		return true;
	}

	protected boolean buildSort(StringBuilder pstrBuffer, SortParameter pParam, SearchParameter[] parrParams)
		throws BigBangException
	{
		SubPolicySortParameter lParam;

		if ( !(pParam instanceof SubPolicySortParameter) )
			return false;
		lParam = (SubPolicySortParameter)pParam;

		if ( lParam.field == SubPolicySortParameter.SortableField.RELEVANCE )
		{
			if ( !buildRelevanceSort(pstrBuffer, parrParams) )
				return false;
		}

		if ( lParam.field == SubPolicySortParameter.SortableField.NUMBER )
			pstrBuffer.append("[:Number]");

		if ( lParam.field == SubPolicySortParameter.SortableField.CLIENT_NAME )
		{
			pstrBuffer.append("[:Subscriber:Name]");
		}

		if ( lParam.field == SubPolicySortParameter.SortableField.CLIENT_NUMBER )
		{
			pstrBuffer.append("[:Subscriber:Number]");
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
		Policy lobjPolicy;
		Company lobjCompany;
		SubPolicyStub lobjResult;

		try
		{
			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), (UUID)parrValues[1]);
			try
			{
				lobjPolicy = (Policy)lobjProcess.GetParent().GetData();
				try
				{
					lobjCompany = lobjPolicy.GetCompany();
				}
				catch (Throwable e)
				{
					lobjCompany = null;
				}
			}
			catch (Throwable e)
			{
				lobjPolicy = null;
				lobjCompany = null;
			}
		}
		catch (Throwable e)
		{
			lobjProcess = null;
			lobjPolicy = null;
			lobjCompany = null;
		}

		lobjResult = new SubPolicyStub();

		lobjResult.id = pid.toString();
		lobjResult.number = (String)parrValues[0];
		lobjResult.mainPolicyId = (lobjPolicy == null ? null : lobjPolicy.getKey().toString());
		lobjResult.mainPolicyNumber = (lobjPolicy == null ? "(Erro)" : lobjPolicy.getLabel());
		lobjResult.clientId = ((UUID)parrValues[2]).toString();
		lobjResult.clientNumber = parrValues[3].toString();
		lobjResult.clientName = (String)parrValues[4];
		lobjResult.inheritCategoryName = (lobjPolicy == null ? "(Erro)" : lobjPolicy.GetSubLine().getLine().getCategory().getLabel());
		lobjResult.inheritLineName = (lobjPolicy == null ? "(Erro)" : lobjPolicy.GetSubLine().getLine().getLabel());
		lobjResult.inheritSubLineName = (lobjPolicy == null ? "(Erro)" : lobjPolicy.GetSubLine().getLabel());
		lobjResult.inheritCompanyName = (lobjCompany == null ? "(Erro)" : lobjCompany.getLabel());
		lobjResult.statusId = ((UUID)parrValues[5]).toString();
		lobjResult.statusText = (String)parrValues[6];
		switch ( (Integer)parrValues[7] )
		{
		case 0:
			lobjResult.statusIcon = SubPolicyStub.PolicyStatus.PROVISIONAL;
			break;

		case 1:
			lobjResult.statusIcon = SubPolicyStub.PolicyStatus.VALID;
			break;

		case 2:
			lobjResult.statusIcon = SubPolicyStub.PolicyStatus.OBSOLETE;
			break;
		}
		lobjResult.processId = (lobjProcess == null ? null : lobjProcess.getKey().toString());
		return lobjResult;
	}

	private boolean buildRelevanceSort(StringBuilder pstrBuffer, SearchParameter[] parrParams)
		throws BigBangException
	{
		SubPolicySearchParameter lParam;
		String lstrAux;
		boolean lbFound;
		int i;

		if ( (parrParams == null) || (parrParams.length == 0) )
			return false;

		lbFound = false;
		for ( i = 0; i < parrParams.length; i++ )
		{
			if ( !(parrParams[i] instanceof SubPolicySearchParameter) )
				continue;
			lParam = (SubPolicySearchParameter) parrParams[i];
			if ( (lParam.freeText == null) || (lParam.freeText.trim().length() == 0) )
				continue;
			lstrAux = lParam.freeText.trim().replace("'", "''").replace(" ", "%");
			if ( lbFound )
				pstrBuffer.append(" + ");
			lbFound = true;
			pstrBuffer.append("CASE WHEN [:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-PATINDEX(N'%").append(lstrAux).append("%', [:Number]) ELSE ")
					.append("CASE WHEN [:Subscriber:Name] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000*PATINDEX(N'%").append(lstrAux).append("%', [:Subscriber:Name]) ELSE ")
					.append("CASE WHEN [:Subscriber:Number] LIKE N'%").append(lstrAux).append("%' THEN ")
					.append("-1000000*PATINDEX(N'%").append(lstrAux).append("%', [:Subscriber:Number]) ELSE ")
					.append("0 END END END");
		}

		return lbFound;
	}
}
