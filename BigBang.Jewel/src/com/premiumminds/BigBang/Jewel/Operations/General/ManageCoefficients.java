package com.premiumminds.BigBang.Jewel.Operations.General;

import java.io.Serializable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public class ManageCoefficients
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public class TaxData
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public UUID mid;
		public String mstrName;
		public UUID midCoverage;
		public UUID midType;
		public String mstrUnits;
		public String mstrDefault;
		public boolean mbVariesByObject;
		public boolean mbVariesByExercise;
		public UUID midReferenceTo;
		public int mlngColumn;
		public boolean mbMandatory;
		public TaxData mobjPrevValues;
	}

	public TaxData[] marrCreate;
	public TaxData[] marrModify;
	public TaxData[] marrDelete;

	public ManageCoefficients(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_ManageCoefficients;
	}

	public String ShortDesc()
	{
		return "Gestão de Valores"; 
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
				lstrResult.append("Foi criado 1 valor:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram criados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" valores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Valor ");
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
				lstrResult.append("Foi modificado 1 valor:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram modificados ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" valores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Valor ");
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
				lstrResult.append("Foi apagado 1 valor:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" valores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Valor ");
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

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i;

		lstrResult = new StringBuilder();

		if ( (marrCreate != null) && (marrCreate.length > 0) )
		{
			if ( marrCreate.length == 1 )
				lstrResult.append("O valor criado será apagado.");
			else
				lstrResult.append("Os valores criados serão apagados.");
			lstrResult.append(pstrLineBreak);
		}

		if ( (marrModify != null) && (marrModify.length > 0) )
		{
			lstrResult.append("Serão repostas as definições anteriores:");
			lstrResult.append(pstrLineBreak);
			if ( marrModify.length == 1 )
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
			else
			{
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Valor ");
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
				lstrResult.append("O valor apagado será reposto.");
			else
				lstrResult.append("Os valores apagados serão repostos.");
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
				lstrResult.append("Foi apagado 1 valor:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrCreate[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram apagados ");
				lstrResult.append(marrCreate.length);
				lstrResult.append(" valores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrCreate.length; i++ )
				{
					lstrResult.append("Valor ");
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
				lstrResult.append("Foi reposta a definição de 1 valor:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrModify[0].mobjPrevValues, pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostas as definições de ");
				lstrResult.append(marrModify.length);
				lstrResult.append(" valores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrModify.length; i++ )
				{
					lstrResult.append("Valor ");
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
				lstrResult.append("Foi reposto 1 valor:");
				lstrResult.append(pstrLineBreak);
				Describe(lstrResult, marrDelete[0], pstrLineBreak);
			}
			else
			{
				lstrResult.append("Foram repostos ");
				lstrResult.append(marrDelete.length);
				lstrResult.append(" valores:");
				lstrResult.append(pstrLineBreak);
				for ( i = 0; i < marrDelete.length; i++ )
				{
					lstrResult.append("Valor ");
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
		try
		{
			if ( marrCreate != null )
				UndoCreateTaxes(pdb, marrCreate);

			if ( marrModify != null )
				UndoModifyTaxes(pdb, marrModify);

			if ( marrDelete != null )
				UndoDeleteTaxes(pdb, marrDelete);
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
		larrResult[0].midType = Constants.ObjID_Tax;

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
				lobjAuxTax.setAt(2, parrData[i].midType);
				lobjAuxTax.setAt(3, parrData[i].mstrUnits);
				lobjAuxTax.setAt(4, parrData[i].mstrDefault);
				lobjAuxTax.setAt(5, parrData[i].mbVariesByObject);
				lobjAuxTax.setAt(6, parrData[i].mbVariesByExercise);
				lobjAuxTax.setAt(7, parrData[i].midReferenceTo);
				lobjAuxTax.setAt(8, parrData[i].mlngColumn);
				lobjAuxTax.setAt(9, parrData[i].mbMandatory);
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
			parrData[i].mobjPrevValues.midType = (UUID)lobjAuxTax.getAt(2);
			parrData[i].mobjPrevValues.mstrUnits = (String)lobjAuxTax.getAt(3);
			parrData[i].mobjPrevValues.mstrDefault = (String)lobjAuxTax.getAt(4);
			parrData[i].mobjPrevValues.mbVariesByObject = (Boolean)lobjAuxTax.getAt(5);
			parrData[i].mobjPrevValues.mbVariesByExercise = (Boolean)lobjAuxTax.getAt(6);
			parrData[i].mobjPrevValues.midReferenceTo = (UUID)lobjAuxTax.getAt(7);
			parrData[i].mobjPrevValues.mlngColumn = (Integer)lobjAuxTax.getAt(8);
			parrData[i].mobjPrevValues.mbMandatory = (Boolean)lobjAuxTax.getAt(9);
			parrData[i].mobjPrevValues.mobjPrevValues = null;

			try
			{
				lobjAuxTax.setAt(0, parrData[i].mstrName);
				lobjAuxTax.setAt(1, parrData[i].midCoverage);
				lobjAuxTax.setAt(2, parrData[i].midType);
				lobjAuxTax.setAt(3, parrData[i].mstrUnits);
				lobjAuxTax.setAt(4, parrData[i].mstrDefault);
				lobjAuxTax.setAt(5, parrData[i].mbVariesByObject);
				lobjAuxTax.setAt(6, parrData[i].mbVariesByExercise);
				lobjAuxTax.setAt(7, parrData[i].midReferenceTo);
				lobjAuxTax.setAt(8, parrData[i].mlngColumn);
				lobjAuxTax.setAt(9, parrData[i].mbMandatory);
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
			parrData[i].midType = (UUID)lobjAuxTax.getAt(2);
			parrData[i].mstrUnits = (String)lobjAuxTax.getAt(3);
			parrData[i].mstrDefault = (String)lobjAuxTax.getAt(4);
			parrData[i].mbVariesByObject = (Boolean)lobjAuxTax.getAt(5);
			parrData[i].mbVariesByExercise = (Boolean)lobjAuxTax.getAt(6);
			parrData[i].midReferenceTo = (UUID)lobjAuxTax.getAt(7);
			parrData[i].mlngColumn = (Integer)lobjAuxTax.getAt(8);
			parrData[i].mbMandatory = (Boolean)lobjAuxTax.getAt(9);
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

	private void UndoCreateTaxes(SQLServer pdb, TaxData[] parrData)
		throws BigBangJewelException
	{
		Entity lrefTaxes;
		int i;

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

	private void UndoModifyTaxes(SQLServer pdb, TaxData[] parrData)
		throws BigBangJewelException
	{
		int i;
		Tax lobjAuxTax;

		for ( i = 0; i < parrData.length; i++ )
		{
			lobjAuxTax = Tax.GetInstance(Engine.getCurrentNameSpace(), parrData[i].mid);

			try
			{
				lobjAuxTax.setAt(0, parrData[i].mobjPrevValues.mstrName);
				lobjAuxTax.setAt(1, parrData[i].mobjPrevValues.midCoverage);
				lobjAuxTax.setAt(2, parrData[i].mobjPrevValues.midType);
				lobjAuxTax.setAt(3, parrData[i].mobjPrevValues.mstrUnits);
				lobjAuxTax.setAt(4, parrData[i].mobjPrevValues.mstrDefault);
				lobjAuxTax.setAt(5, parrData[i].mobjPrevValues.mbVariesByObject);
				lobjAuxTax.setAt(6, parrData[i].mobjPrevValues.mbVariesByExercise);
				lobjAuxTax.setAt(7, parrData[i].mobjPrevValues.midReferenceTo);
				lobjAuxTax.setAt(8, parrData[i].mobjPrevValues.mlngColumn);
				lobjAuxTax.setAt(9, parrData[i].mobjPrevValues.mbMandatory);
				lobjAuxTax.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	private void UndoDeleteTaxes(SQLServer pdb, TaxData[] parrData)
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
				lobjAuxTax.setAt(2, parrData[i].midType);
				lobjAuxTax.setAt(3, parrData[i].mstrUnits);
				lobjAuxTax.setAt(4, parrData[i].mstrDefault);
				lobjAuxTax.setAt(5, parrData[i].mbVariesByObject);
				lobjAuxTax.setAt(6, parrData[i].mbVariesByExercise);
				lobjAuxTax.setAt(7, parrData[i].midReferenceTo);
				lobjAuxTax.setAt(8, parrData[i].mlngColumn);
				lobjAuxTax.setAt(9, parrData[i].mbMandatory);
				lobjAuxTax.SaveToDb(pdb);
				parrData[i].mid = lobjAuxTax.getKey();
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
		ObjectBase lobjAux;

		try
		{
			pstrString.append("Ramo: ");
			lobjOwner = Coverage.GetInstance(Engine.getCurrentNameSpace(), pobjData.midCoverage);
			lobjOwnerOwner = lobjOwner.GetSubLine();
			lobjOwnerOwnerOwner = lobjOwnerOwner.getLine();
			pstrString.append(lobjOwnerOwnerOwner.getLabel());
			pstrString.append(pstrLineBreak);
			pstrString.append("Modalidade: ");
			pstrString.append(lobjOwnerOwner.getLabel());
			pstrString.append(pstrLineBreak);
			pstrString.append("Cobertura: ");
			pstrString.append(lobjOwner.getLabel());
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o ramo.)");
		}
		pstrString.append(pstrLineBreak);

		pstrString.append("Coeficiente: ");
		pstrString.append(pobjData.mstrName);
		pstrString.append(pstrLineBreak);

		pstrString.append("Tipo de valor: ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_FieldType),
					pobjData.midType);
			pstrString.append((String)lobjAux.getAt(0));
		}
		catch (Throwable e)
		{
			pstrString.append("(Erro a obter o tipo de valor.)");
		}
		pstrString.append(pstrLineBreak);

		if ( pobjData.mstrUnits != null )
		{
			pstrString.append("Unidades: ");
			pstrString.append(pobjData.mstrUnits);
			pstrString.append(pstrLineBreak);
		}

		if ( pobjData.mstrDefault != null )
		{
			pstrString.append("Valor de referência: ");
			pstrString.append(pobjData.mstrDefault);
			pstrString.append(pstrLineBreak);
		}

		if ( pobjData.mbVariesByObject )
		{
			pstrString.append("Varia por objecto seguro.");
			pstrString.append(pstrLineBreak);
		}

		if ( pobjData.mbVariesByExercise )
		{
			pstrString.append("Varia por exercício.");
			pstrString.append(pstrLineBreak);
		}

		if ( pobjData.mbMandatory )
		{
			pstrString.append("Valor obrigatório.");
			pstrString.append(pstrLineBreak);
		}

		if ( pobjData.midReferenceTo != null )
		{
			pstrString.append("Referência externa: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_Object),
						pobjData.midReferenceTo);
				pstrString.append((String)lobjAux.getAt(0));
			}
			catch (Throwable e)
			{
				pstrString.append("(Erro a obter a categoria.)");
			}
			pstrString.append(pstrLineBreak);
		}

		if ( pobjData.mlngColumn >= 0 )
		{
			pstrString.append("Coluna na tabela: ");
			pstrString.append(pobjData.mlngColumn);
			pstrString.append(pstrLineBreak);
		}
	}
}
