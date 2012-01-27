package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.CostCenter;

public class ManageCostCenters
	extends UndoableOperation
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
		return Constants.OPID_General_ManageCostCenters;
	}

	public String ShortDesc()
	{
		return "Gestão de Centros de Custo"; 
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
				lstrResult.append("Foi criado 1 centro de custo:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram criados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" centros de custo:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Centro de custo ");
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
				lstrResult.append("Foi modificado 1 centro de custo:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram modificados ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" centros de custo:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Centro de custo ");
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
				lstrResult.append("Foi apagado 1 centro de custo:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" centros de custo:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Centro de custo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
				}
			}
		}

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
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

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
				lstrResult.append("O centro de custo criado será apagado.");
			else
				lstrResult.append("Os centros de custo criados serão apagados.");
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
					lstrResult.append("Centro de custo ");
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
				lstrResult.append("O centro de custo apagado será reposto.");
			else
				lstrResult.append("Os centros de custo apagados serão repostos.");
			lstrResult.append(pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
			{
				lstrResult.append("Foi apagado 1 centro de custo:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" centros de custo:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Centro de custo ");
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
				lstrResult.append("Foram repostos os valores de 1 centro de custo:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos os valores de ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" centros de custo:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Centro de custo ");
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
			{
				lstrResult.append("Foi reposto 1 centro de custo:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" centros de custo:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Centro de custo ");
					lstrResult.append(i + 1);
					lstrResult.append(":");
					lstrResult.append(pstrLineBreak);
					Describe(lstrResult, marrDelete[i], pstrLineBreak);
				}
			}
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb) throws JewelPetriException
	{
		int i;
		CostCenter lobjAux;
		Entity lrefCostCenters;

		try
		{
			if ( marrCreate != null )
			{
				lrefCostCenters = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Constants.ObjID_CostCenter));

				for ( i = 0; i < marrCreate.length; i++ )
					lrefCostCenters.Delete(pdb, marrCreate[i].mid);
			}

			if ( marrModify != null )
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lobjAux = CostCenter.GetInstance(Engine.getCurrentNameSpace(), marrModify[i].mid);
					lobjAux.setAt(0, marrModify[i].mobjPrevValues.mstrCode);
					lobjAux.setAt(1, marrModify[i].mobjPrevValues.mstrName);
					lobjAux.SaveToDb(pdb);
				}
			}

			if ( marrDelete != null )
			{
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lobjAux = CostCenter.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAux.setAt(0, marrDelete[i].mstrCode);
					lobjAux.setAt(1, marrDelete[i].mstrName);
					lobjAux.SaveToDb(pdb);
					marrDelete[i].mid = lobjAux.getKey();
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		int llngCreates, llngModifies, llngDeletes;
		int i;

		llngCreates = ( marrCreate == null ? 0 : marrCreate.length );
		llngModifies = ( marrModify == null ? 0 : marrModify.length );
		llngDeletes = ( marrDelete == null ? 0 : marrDelete.length );

		if ( llngCreates + llngModifies + llngDeletes == 0 )
			return new UndoSet[0];

		larrResult = new UndoSet[1];
		larrResult[0] = new UndoSet();
		larrResult[0].midType = Constants.ObjID_CostCenter;

		larrResult[0].marrDeleted = new UUID[llngCreates];
		for ( i = 0; i < llngCreates; i ++ )
			larrResult[0].marrDeleted[i] = marrCreate[i].mid;

		larrResult[0].marrChanged = new UUID[llngModifies];
		for ( i = 0; i < llngModifies; i ++ )
			larrResult[0].marrChanged[i] = marrModify[i].mid;

		larrResult[0].marrCreated = new UUID[llngDeletes];
		for ( i = 0; i < llngDeletes; i ++ )
			larrResult[0].marrCreated[i] = marrDelete[i].mid;

		return larrResult;
	}

	private void Describe(StringBuilder pstrString, CostCenterData pobjData, String pstrLineBreak)
	{
		pstrString.append("Código: ");
		pstrString.append(pobjData.mstrCode);
		pstrString.append(pstrLineBreak);
		pstrString.append("Nome: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);
	}
}
