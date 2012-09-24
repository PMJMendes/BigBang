package com.premiumminds.BigBang.Jewel.Operations.Policy;

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
import com.premiumminds.BigBang.Jewel.Data.SubPolicyData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyValue;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class CreateSubPolicy
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public SubPolicyData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_CreateSubPolicy;
	}

	public String ShortDesc()
	{
		return "Criação de Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criada a seguinte apólice adesão:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

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
		SubPolicy lobjPolicy;
		int i;
		SubPolicyCoverage lobjCoverage;
		SubPolicyObject lobjObject;
		SubPolicyValue lobjValue;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;

		try
		{
			if ( mobjData.mstrNumber == null )
				mobjData.mstrNumber = GetSubPolicyNumber();
			if ( mobjData.midManager == null )
				mobjData.midManager = Engine.getCurrentUser();
			mobjData.midStatus = Constants.StatusID_InProgress;

			lobjPolicy = SubPolicy.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjPolicy);
			lobjPolicy.SaveToDb(pdb);
			mobjData.mid = lobjPolicy.getKey();

			if ( mobjData.marrCoverages != null )
			{
				for ( i = 0; i < mobjData.marrCoverages.length; i++ )
				{
					mobjData.marrCoverages[i].midOwner = mobjData.mid;
					lobjCoverage = SubPolicyCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
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
					lobjObject = SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrObjects[i].ToObject(lobjObject);
					lobjObject.SaveToDb(pdb);
					mobjData.marrObjects[i].mid = lobjObject.getKey();
				}
			}

			if ( mobjData.marrValues != null )
			{
				for ( i = 0; i < mobjData.marrValues.length; i++ )
				{
					mobjData.marrValues[i].midOwner = mobjData.mid;
					mobjData.marrValues[i].midObject = ( mobjData.marrValues[i].mlngObject < 0 ? null :
							mobjData.marrObjects[mobjData.marrValues[i].mlngObject].mid );
					lobjValue = SubPolicyValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrValues[i].ToObject(lobjValue);
					lobjValue.SaveToDb(pdb);
					mobjData.marrValues[i].mid = lobjValue.getKey();
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjPolicy.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjPolicy.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_SubPolicy);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjPolicy.getKey(), GetProcess().getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			ldtAux = new Timestamp(new java.util.Date().getTime());
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtAux.getTime());
	    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Validação de Apólice Adesão");
			lobjItem.setAt(1, mobjData.midManager);
			lobjItem.setAt(2, Constants.ProcID_SubPolicy);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Pending);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_SubPolicy_ValidateSubPolicy}, pdb);

			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private String GetSubPolicyNumber()
		throws BigBangJewelException
	{
		String lstrFilter;
		IEntity lrefSubPolicies;
        MasterDB ldb;
        ResultSet lrsSubPolicies;
        int llngResult;
        String lstrAux;
        int llngAux;

		try
		{
	        lstrFilter = GetProcess().GetData().getLabel() + ".%";
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubPolicies = lrefSubPolicies.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {lstrFilter},
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
			while ( lrsSubPolicies.next() )
			{
				lstrAux = lrsSubPolicies.getString(2).substring(lstrFilter.length() - 1);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsSubPolicies.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubPolicies.close();
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
