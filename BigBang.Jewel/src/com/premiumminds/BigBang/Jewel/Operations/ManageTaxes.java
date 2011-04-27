package com.premiumminds.BigBang.Jewel.Operations;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public class ManageTaxes
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public class TaxData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public UUID midCoverage;
		public UUID midCurrency;
		public double mdblValue;
		public TaxData mobjPrevValues;
	}

	public TaxData[] marrCreateTaxes;
	public TaxData[] marrModifyTaxes;
	public TaxData[] marrDeleteTaxes;

	public ManageTaxes(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ManageCoefficients;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		try
		{
			if ( marrCreateTaxes != null )
				CreateTaxes(pdb, marrCreateTaxes);

			if ( marrModifyTaxes != null )
				ModifyTaxes(pdb, marrModifyTaxes);

			if ( marrDeleteTaxes != null )
				DeleteTaxes(pdb, marrDeleteTaxes);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private void CreateTaxes(SQLServer pdb, TaxData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Tax lobjAuxTax;

		for ( i = 0; i < parrData.length; i++ )
		{
			try
			{
				lobjAuxTax = Tax.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjAuxTax.setAt(0, parrData[i].mstrName);
				lobjAuxTax.setAt(1, parrData[i].midCoverage);
				lobjAuxTax.setAt(2, parrData[i].midCurrency);
				lobjAuxTax.setAt(3, BigDecimal.valueOf(parrData[i].mdblValue));
				lobjAuxTax.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			parrData[i].mid = lobjAuxTax.getKey();
			parrData[i].mobjPrevValues = null;
		}
	}

	private void ModifyTaxes(SQLServer pdb, TaxData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Tax lobjAuxTax;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxTax = Tax.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mobjPrevValues = new TaxData();
			parrData[i].mobjPrevValues.mid = lobjAuxTax.getKey();
			parrData[i].mobjPrevValues.mstrName = (String)lobjAuxTax.getAt(0);
			parrData[i].mobjPrevValues.midCoverage = (UUID)lobjAuxTax.getAt(1);
			parrData[i].mobjPrevValues.midCurrency = (UUID)lobjAuxTax.getAt(2);
			parrData[i].mobjPrevValues.mdblValue = ((BigDecimal)lobjAuxTax.getAt(3)).doubleValue();
			parrData[i].mobjPrevValues.mobjPrevValues = null;

			try
			{
				lobjAuxTax.setAt(0, parrData[i].mstrName);
				lobjAuxTax.setAt(1, parrData[i].midCoverage);
				lobjAuxTax.setAt(2, parrData[i].midCurrency);
				lobjAuxTax.setAt(3, BigDecimal.valueOf(parrData[i].mdblValue));
				lobjAuxTax.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void DeleteTaxes(SQLServer pdb, TaxData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefTaxes;
		int i;
		Tax lobjAuxTax;

		try
		{
			lrefTaxes = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Constants.ObjID_Tax));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxTax = Tax.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);
			parrData[i].mstrName = (String)lobjAuxTax.getAt(0);
			parrData[i].midCoverage = (UUID)lobjAuxTax.getAt(1);
			parrData[i].midCurrency = (UUID)lobjAuxTax.getAt(2);
			parrData[i].mdblValue = ((BigDecimal)lobjAuxTax.getAt(3)).doubleValue();
			parrData[i].mobjPrevValues = null;

			try
			{
				lrefTaxes.Delete(pdb, parrData[i].mid);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}
}
