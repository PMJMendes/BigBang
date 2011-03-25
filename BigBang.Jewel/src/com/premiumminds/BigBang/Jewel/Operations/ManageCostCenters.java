package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.JewelPetriException;
import Jewel.Petri.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.CostCenter;

public class ManageCostCenters
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public class CostCenterData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrCode;
		public String mstrName;
	}

	public CostCenterData[] marrCreate;
	public CostCenterData[] marrModify;
	public CostCenterData[] marrDelete;

	public UUID[] marrNewIDs;

	public ManageCostCenters()
	{
		super(null);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageCostCenters;
	}

	protected void Run()
		throws JewelPetriException
	{
		int i;
		MasterDB ldb;
		CostCenter lobjAux;
		Entity lrefCostCenters;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			if ( marrCreate != null )
			{
				marrNewIDs = new UUID[marrCreate.length];

				for ( i = 0; i < marrCreate.length; i++ )
				{
					lobjAux = CostCenter.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(0, marrCreate[i].mstrCode);
					lobjAux.setAt(1, marrCreate[i].mstrName);
					lobjAux.SaveToDb(ldb);
					marrNewIDs[i] = lobjAux.getKey();
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = CostCenter.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					lobjAux.setAt(0, marrModify[i].mstrCode);
					lobjAux.setAt(1, marrModify[i].mstrName);
					lobjAux.SaveToDb(ldb);
				}
			}

			if ( marrDelete != null )
			{
				lrefCostCenters = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_CostCenter));

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lrefCostCenters.Delete(ldb, marrDelete[i].mid);
				}
			}

		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.Commit();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
