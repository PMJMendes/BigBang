package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
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

	public TaxData[] marrCreate;
	public TaxData[] marrModify;
	public TaxData[] marrDelete;

	public ManageTaxes(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Gestão de Impostos e Coeficientes"; 
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				lstrResult.append("Foi criado 1 coeficiente:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram criados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" coeficientes:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Coeficiente ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrCreate[i], pstrLineBreak);
				}
			}
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			if ( marrModify.length == 1 )
			{
				lstrResult.append("Foi modificado 1 coeficiente:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram modificados ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" coeficientes:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Coeficiente ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrModify[i], pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
			{
				lstrResult.append("Foi apagado 1 coeficiente:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" coeficientes:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Coeficiente ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
				}
			}
		}

		return lstrResult.toString();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
				lstrResult.append("O coeficiente criado será apagado.");
			else
				lstrResult.append("Os coeficientes criados serão apagados.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			lstrResult.append("Serão repostos os valores anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModify.length == 1 )
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Coeficiente ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrModify[i].mobjPrevValues, pstrLineBreak);
				}
			}
		}

		if ( (marrDelete != null) && (marrDelete.length > 0) )
		{
			if ( marrDelete.length == 1 )
				lstrResult.append("O coeficiente apagado será reposto.");
			else
				lstrResult.append("Os coeficientes apagados serão repostos.");
			lstrResult.append(pstrLineBreak);
		}

		return lstrResult.toString();
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
			if ( marrCreate != null )
				CreateTaxes(pdb, marrCreate);

			if ( marrModify != null )
				ModifyTaxes(pdb, marrModify);

			if ( marrDelete != null )
				DeleteTaxes(pdb, marrDelete);
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

	private void Describe(StringBuilder pstrString, TaxData pobjData, String pstrLineBreak)
	{
		Coverage lobjOwner;
		SubLine lobjOwnerOwner;
		Line lobjOwnerOwnerOwner;
		ObjectBase lobjUnits;

		try
		{
			pstrString.append("Ramo: ");
			lobjOwner = Coverage.GetInstance(Engine.getCurrentNameSpace(), pobjData.midCoverage);
			try
			{
				lobjOwnerOwner = SubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjOwner.getAt(1));
				try
				{
					lobjOwnerOwnerOwner = Line.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjOwnerOwner.getAt(1));
					pstrString.append(lobjOwnerOwnerOwner.getLabel());
				}
				catch (Throwable e)
				{
					pstrString.append(" (Erro a obter o ramo.)");
				}
				pstrString.append(pstrLineBreak);
				pstrString.append("Modalidade: ");
				pstrString.append(lobjOwnerOwner.getLabel());
			}
			catch (Throwable e)
			{
				pstrString.append(" (Erro a obter a modalidade.)");
			}
			pstrString.append(pstrLineBreak);
			pstrString.append("Cobertura: ");
			pstrString.append(lobjOwner.getLabel());
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter a cobertura.)");
		}
		pstrString.append(pstrLineBreak);

		pstrString.append("Coeficiente: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);
		pstrString.append("Valor: ");
		pstrString.append(pobjData.mdblValue);
		pstrString.append(pstrLineBreak);
		pstrString.append("Unidades: ");

		try
		{
			lobjUnits = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ValueUnit), pobjData.midCurrency);
			pstrString.append((String)lobjUnits.getAt(0));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter a categoria.)");
		}
		pstrString.append(pstrLineBreak);
	}
}
