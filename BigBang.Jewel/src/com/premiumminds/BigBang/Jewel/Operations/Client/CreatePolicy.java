package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoInsurer;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class CreatePolicy
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public PolicyData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreatePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_CreatePolicy;
	}

	public String ShortDesc()
	{
		return "Criação de Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criada a seguinte apólice:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjData.marrCoInsurers != null )
		{
			lstrResult.append(pstrLineBreak).append("Co-Seguro:").append(pstrLineBreak);
			for ( i = 0; i < mobjData.marrCoInsurers.length; i++ )
			{
				lstrResult.append("- ");
				mobjData.marrValues[i].Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}
			lstrResult.append(pstrLineBreak);
		}

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;
		int i;
		PolicyCoInsurer lobjCoInsurer;
		PolicyCoverage lobjCoverage;
		PolicyObject lobjObject;
		PolicyExercise lobjExercise;
		PolicyValue lobjValue;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;

		try
		{
			if ( Constants.DurID_Temporary.equals(mobjData.midDuration) && (mobjData.mdtEndDate == null) )
				throw new BigBangJewelException("Erro: Numa apólice temporária, tem que especificar a data de fim.");

			if ( mobjData.mstrNumber == null )
				mobjData.mstrNumber = GetTmpPolicyNumber();
			if ( mobjData.midManager == null )
				mobjData.midManager = Engine.getCurrentUser();
			if ( mobjData.midMediator == null )
				mobjData.midMediator = (UUID)GetProcess().GetData().getAt(8);
			mobjData.midStatus = Constants.StatusID_InProgress;
			mobjData.midClient = GetProcess().GetDataKey();

			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjPolicy);
			lobjPolicy.SaveToDb(pdb);
			mobjData.mid = lobjPolicy.getKey();

			if ( mobjData.marrCoInsurers != null )
			{
				for ( i = 0; i < mobjData.marrCoInsurers.length; i++ )
				{
					mobjData.marrCoInsurers[i].midPolicy = mobjData.mid;
					lobjCoInsurer = PolicyCoInsurer.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrCoInsurers[i].ToObject(lobjCoInsurer);
					lobjCoInsurer.SaveToDb(pdb);
					mobjData.marrCoInsurers[i].mid = lobjCoInsurer.getKey();
				}
			}

			if ( mobjData.marrCoverages != null )
			{
				for ( i = 0; i < mobjData.marrCoverages.length; i++ )
				{
					mobjData.marrCoverages[i].midOwner = mobjData.mid;
					lobjCoverage = PolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrCoverages[i].ToObject(lobjCoverage);
					lobjCoverage.SaveToDb(pdb);
					mobjData.marrCoverages[i].mid = lobjCoverage.getKey();
				}
			}

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					mobjData.marrObjects[i].midOwner = mobjData.mid;
					lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrObjects[i].ToObject(lobjObject);
					lobjObject.SaveToDb(pdb);
					mobjData.marrObjects[i].mid = lobjObject.getKey();
				}
			}

			if ( mobjData.marrExercises != null )
			{
				for ( i = 0; i < mobjData.marrExercises.length; i++ )
				{
					mobjData.marrExercises[i].midOwner = mobjData.mid;
					lobjExercise = PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrExercises[i].ToObject(lobjExercise);
					lobjExercise.SaveToDb(pdb);
					mobjData.marrExercises[i].mid = lobjExercise.getKey();
				}
			}

			if ( mobjData.marrValues != null )
			{
				for ( i = 0; i < mobjData.marrValues.length; i++ )
				{
					mobjData.marrValues[i].midOwner = mobjData.mid;
					mobjData.marrValues[i].midObject = ( mobjData.marrValues[i].mlngObject < 0 ? null :
							mobjData.marrObjects[mobjData.marrValues[i].mlngObject].mid );
					mobjData.marrValues[i].midExercise = ( mobjData.marrValues[i].mlngExercise < 0 ? null :
							mobjData.marrExercises[mobjData.marrValues[i].mlngExercise].mid );
					lobjValue = PolicyValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrValues[i].ToObject(lobjValue);
					lobjValue.SaveToDb(pdb);
					mobjData.marrValues[i].mid = lobjValue.getKey();
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjPolicy.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjPolicy.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Policy);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjPolicy.getKey(), GetProcess().getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Validação de Apólice");
			lobjItem.setAt(1, mobjData.midManager);
			lobjItem.setAt(2, Constants.ProcID_Policy);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_Policy_ValidatePolicy}, pdb);

			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private String GetTmpPolicyNumber()
		throws BigBangJewelException
	{
		String lstrFilter;
		IEntity lrefPolicies;
        MasterDB ldb;
        ResultSet lrsPolicies;
        int llngResult;
        String lstrAux;
        int llngAux;

		try
		{
	        lstrFilter = "-" + ((Integer)GetProcess().GetData().getAt(1)).toString() + ".%";
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsPolicies = lrefPolicies.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {lstrFilter},
					new int[] {Integer.MIN_VALUE});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			while ( lrsPolicies.next() )
			{
				lstrAux = lrsPolicies.getString(2).substring(lstrFilter.length() - 1);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsPolicies.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsPolicies.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lstrFilter.substring(0, lstrFilter.length() - 1) + llngResult;
	}
}
