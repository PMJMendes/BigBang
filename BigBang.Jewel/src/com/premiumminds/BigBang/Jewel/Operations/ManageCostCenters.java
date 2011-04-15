package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

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
		public CostCenterData mobjPrevValues;
	}

	public CostCenterData[] marrCreate;
	public CostCenterData[] marrModify;
	public CostCenterData[] marrDelete;

	public ManageCostCenters(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageCostCenters;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		int i;
		CostCenter lobjAux;
		Entity lrefCostCenters;

		try
		{
			if ( marrCreate != null )
			{
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lobjAux = CostCenter.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(0, marrCreate[i].mstrCode);
					lobjAux.setAt(1, marrCreate[i].mstrName);
					lobjAux.SaveToDb(pdb);
					marrCreate[i].mid = lobjAux.getKey();
					marrCreate[i].mobjPrevValues = null;
				}
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = CostCenter.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					marrModify[i].mobjPrevValues = new CostCenterData();
					marrModify[i].mobjPrevValues.mid = lobjAux.getKey();
					marrModify[i].mobjPrevValues.mstrCode = (String)lobjAux.getAt(0);
					marrModify[i].mobjPrevValues.mstrName = (String)lobjAux.getAt(1);
					marrModify[i].mobjPrevValues.mobjPrevValues = null;
					lobjAux.setAt(0, marrModify[i].mstrCode);
					lobjAux.setAt(1, marrModify[i].mstrName);
					lobjAux.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				lrefCostCenters = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_CostCenter));

				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAux = CostCenter.GetInstance(Engine.getCurrentNameSpace(), marrDelete[i].mid);
					marrDelete[i].mstrCode = (String)lobjAux.getAt(0);
					marrDelete[i].mstrName = (String)lobjAux.getAt(1);
					marrDelete[i].mobjPrevValues = null;
					lrefCostCenters.Delete(pdb, marrDelete[i].mid);
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
