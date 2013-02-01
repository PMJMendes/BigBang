package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.PolicyExerciseData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyCoverageData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.SubPolicyValueData;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyValue;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternResumeSubPolicy;

public class ExternDeleteSubPolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midSubPolicy;
	public String mstrReason;
	private SubPolicyData mobjData;
	private ContactOps mobjContactOps;
	private DocOps mobjDocOps;
	private PolicyExerciseData[] marrAuxExercises;

	public ExternDeleteSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ExternDeleteSubPolicy;
	}

	public String ShortDesc()
	{
		return "Eliminação de Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi eliminada a seguinte apólice adesão:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		lstrResult.append("Razão: ");
		if ( mstrReason != null )
			lstrResult.append(mstrReason);
		else
			lstrResult.append("(não indicada)");
		lstrResult.append(pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
		SubPolicy lobjAux;
		PNProcess lobjProcess;
		Contact[] larrContacts;
		Document[] larrDocs;
		SubPolicyValue[] larrValues;
		PolicyExercise[] larrExercises;
		SubPolicyObject[] larrObjects;
		SubPolicyCoverage[] larrCoverages;
		int i, j;

		try
		{
			lobjAux = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), midSubPolicy);
			mobjData = new SubPolicyData();
			mobjData.FromObject(lobjAux);
			mobjData.mobjPrevValues = null;

			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess);
			lobjProcess.Stop(pdb);
			lobjProcess.SetDataObjectID(null, pdb);

			larrContacts = lobjAux.GetCurrentContacts();
			if ( (larrContacts == null) || (larrContacts.length == 0) )
				mobjContactOps = null;
			else
			{
				mobjContactOps = new ContactOps();
				mobjContactOps.marrDelete = new ContactData[larrContacts.length];
				for ( i = 0; i < larrContacts.length; i++ )
				{
					mobjContactOps.marrDelete[i] = new ContactData();
					mobjContactOps.marrDelete[i].mid = larrContacts[i].getKey();
				}
				mobjContactOps.RunSubOp(pdb, null);
			}

			larrDocs = lobjAux.GetCurrentDocs();
			if ( (larrDocs == null) || (larrDocs.length == 0) )
				mobjDocOps = null;
			else
			{
				mobjDocOps = new DocOps();
				mobjDocOps.marrDelete = new DocumentData[larrDocs.length];
				for ( i = 0; i < larrDocs.length; i++ )
				{
					mobjDocOps.marrDelete[i] = new DocumentData();
					mobjDocOps.marrDelete[i].mid = larrDocs[i].getKey();
				}
				mobjDocOps.RunSubOp(pdb, null);
			}

			larrValues = lobjAux.GetCurrentValues(pdb);
			larrExercises = lobjAux.GetCurrentExercises(pdb);
			larrObjects = lobjAux.GetCurrentObjects(pdb);
			larrCoverages = lobjAux.GetCurrentCoverages(pdb);

			if ( (larrValues == null) || (larrValues.length == 0) )
				mobjData.marrValues = null;
			else
			{
				mobjData.marrValues = new SubPolicyValueData[larrValues.length];
				for ( i = 0; i < larrValues.length; i++ )
				{
					mobjData.marrValues[i] = new SubPolicyValueData();
					mobjData.marrValues[i].FromObject(larrValues[i]);
					mobjData.marrValues[i].mlngExercise = -1;
					if ( mobjData.marrValues[i].midExercise != null )
					{
						for ( j = 0; j < larrExercises.length; j++ )
						{
							if ( larrExercises[j].getKey().equals(mobjData.marrValues[i].midExercise) )
							{
								mobjData.marrValues[i].mlngExercise = j;
								break;
							}
						}
					}
					mobjData.marrValues[i].mlngObject = -1;
					if ( mobjData.marrValues[i].midObject != null )
					{
						for ( j = 0; j < larrObjects.length; j++ )
						{
							if ( larrObjects[j].getKey().equals(mobjData.marrValues[i].midObject) )
							{
								mobjData.marrValues[i].mlngObject = j;
								break;
							}
						}
					}
					larrValues[i].getDefinition().Delete(pdb, larrValues[i].getKey());
				}
			}

			if ( (larrExercises == null) || (larrExercises.length == 0) )
				marrAuxExercises = null;
			else
			{
				marrAuxExercises = new PolicyExerciseData[larrExercises.length];
				for ( i = 0; i < larrExercises.length; i++ )
				{
					marrAuxExercises[i] = new PolicyExerciseData();
					marrAuxExercises[i].FromObject(larrExercises[i]);
				}
			}

			if ( (larrObjects == null) || (larrObjects.length == 0) )
				mobjData.marrObjects = null;
			{
				mobjData.marrObjects = new SubPolicyObjectData[larrObjects.length];
				for ( i = 0; i < larrObjects.length; i++ )
				{
					mobjData.marrObjects[i] = new SubPolicyObjectData();
					mobjData.marrObjects[i].FromObject(larrObjects[i]);
					larrObjects[i].getDefinition().Delete(pdb, larrObjects[i].getKey());
				}
			}

			if ( (larrCoverages == null) || (larrCoverages.length == 0) )
				mobjData.marrCoverages = null;
			else
			{
				mobjData.marrCoverages = new SubPolicyCoverageData[larrCoverages.length];
				for ( i = 0; i < larrCoverages.length; i++ )
				{
					mobjData.marrCoverages[i] = new SubPolicyCoverageData();
					mobjData.marrCoverages[i].FromObject(larrCoverages[i]);
					larrCoverages[i].getDefinition().Delete(pdb, larrCoverages[i].getKey());
				}
			}

			lobjAux.getDefinition().Delete(pdb, lobjAux.getKey());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A apólice adesão apagada será reposta. O histórico de operações será recuperado.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi reposta a seguinte apólice adesão:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		SubPolicy lobjAux;
		PNProcess lobjProcess;
		SubPolicyCoverage lobjCoverage;
		SubPolicyObject lobjObject;
		SubPolicyValue lobjValue;
		int i;
		ExternResumeSubPolicy lopERSP;

		try
		{
			lobjAux = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
			mobjData.mid = lobjAux.getKey();

			if ( mobjData.marrCoverages != null )
			{
				for ( i = 0; i < mobjData.marrCoverages.length; i++ )
				{
					lobjCoverage = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrCoverages[i].midOwner = mobjData.mid;
					mobjData.marrCoverages[i].ToObject(lobjCoverage);
					lobjCoverage.SaveToDb(pdb);
					mobjData.marrCoverages[i].mid = lobjCoverage.getKey();
				}
			}

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					lobjObject = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrObjects[i].midOwner = mobjData.mid;
					mobjData.marrObjects[i].ToObject(lobjObject);
					lobjObject.SaveToDb(pdb);
					mobjData.marrObjects[i].mid = lobjObject.getKey();
				}
			}

			if ( mobjData.marrValues != null )
			{
				for ( i = 0; i < mobjData.marrValues.length; i++ )
				{
					lobjValue = SubPolicyValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrValues[i].midOwner = mobjData.mid;
					if ( mobjData.marrValues[i].mlngObject < 0 )
						mobjData.marrValues[i].midObject = null;
					else
						mobjData.marrValues[i].midObject = mobjData.marrObjects[mobjData.marrValues[i].mlngObject].mid;
					if ( mobjData.marrValues[i].mlngExercise < 0 )
						mobjData.marrValues[i].midExercise = null;
					else
						mobjData.marrValues[i].midExercise = marrAuxExercises[mobjData.marrValues[i].mlngExercise].mid;
					mobjData.marrValues[i].ToObject(lobjValue);
					lobjValue.SaveToDb(pdb);
					mobjData.marrValues[i].mid = lobjValue.getKey();
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.UndoSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.UndoSubOp(pdb, lobjAux.getKey());

			lobjProcess = PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess);
			lobjProcess.SetDataObjectID(lobjAux.getKey(), pdb);
			lobjProcess.Restart(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopERSP = new ExternResumeSubPolicy(lobjProcess.getKey());
		TriggerOp(lopERSP, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		UndoSet lobjContacts, lobjDocs;
		int llngSize;
		int i;

		lobjContacts = GetContactSet();
		lobjDocs = GetDocSet();

		llngSize = 1;
		if ( lobjContacts != null )
			llngSize++;
		if ( lobjDocs != null )
			llngSize++;

		larrResult = new UndoSet[llngSize];

		larrResult[0] = new UndoSet();
		larrResult[0].midType = Constants.ObjID_SubPolicy;
		larrResult[0].marrDeleted = new UUID[0];
		larrResult[0].marrChanged = new UUID[0];
		larrResult[0].marrCreated = new UUID[1];
		larrResult[0].marrCreated[0] = mobjData.mid;
		i = 1;

		if ( lobjContacts != null )
		{
			larrResult[i] = lobjContacts;
			i++;
		}

		if ( lobjDocs != null )
		{
			larrResult[i] = lobjDocs;
			i++;
		}

		return larrResult;
	}

	private UndoSet GetContactSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjContactOps != null )
		{
			larrAux = mobjContactOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Contact.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Contact;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}

	private UndoSet GetDocSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjDocOps != null )
		{
			larrAux = mobjDocOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Document.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Document;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}

}
