package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;

public class ValidatePolicy
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midPolicy;
	public String mstrErrors;

	public ValidatePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ForceValidatePolicy;
	}

	public String ShortDesc()
	{
		return "Validação em Migração";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Esta apólice foi migrada do Gescar e a sua validação foi forçada.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;
		StringBuilder lstrBuilder;
		int i;
		PolicyCoverage[] larrCoverages;
		PolicyValue[] larrValues;

		midPolicy = GetProcess().GetDataKey();

		lstrBuilder = new StringBuilder();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);

			larrCoverages = lobjPolicy.GetCurrentCoverages();
			larrValues = lobjPolicy.GetCurrentValues();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		for ( i = 0; i < larrCoverages.length; i++ )
		{
			if ( larrCoverages[i].GetCoverage().IsHeader() )
				continue;
			if ( larrCoverages[i].IsPresent() == null )
				lstrBuilder.append("O indicador de presença da cobertura '").append(larrCoverages[i].GetCoverage().getLabel()).
						append("' não está preenchido.\n");
			else if ( larrCoverages[i].GetCoverage().IsMandatory() && !larrCoverages[i].IsPresent())
				lstrBuilder.append("A cobertura '").append(larrCoverages[i].GetCoverage().getLabel()).
						append("' é obrigatória mas não está presente.\n");
		}

		for ( i = 0; i < larrValues.length; i++ )
		{
			if ( larrValues[i].GetValue() == null )
			{
				if ( larrValues[i].GetTax().IsMandatory() )
				{
					AppendTag(lstrBuilder, larrValues[i]);
					lstrBuilder.append("é de preenchimento obrigatório, mas não está preenchido.\n");
				}
			}
			else
				CheckFormat(lstrBuilder, larrValues[i]);
		}

		mstrErrors = lstrBuilder.toString();

		if ( (mstrErrors != null) && (mstrErrors.length() != 0) )
			throw new PolicyValidationException(mstrErrors);

		try
		{
			lobjPolicy.setAt(13, Constants.StatusID_Valid);
			lobjPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A validação será retirada e passará a ser possível fazer alterações em modo de trabalho.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A validação forçada foi retirada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;

		midPolicy = GetProcess().GetDataKey();

		try
		{
			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);
			lobjPolicy.setAt(13, Constants.StatusID_InProgress);
			lobjPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Policy;
		lobjSet.marrChanged = new UUID[] {midPolicy};
		lobjSet.marrCreated = new UUID[] {};
		lobjSet.marrDeleted = new UUID[] {};

		return new UndoSet[] {lobjSet};
	}

	private void CheckFormat(StringBuilder pstrBuilder, PolicyValue pobjValue)
	{
		if ( Constants.FieldID_Text.equals(pobjValue.GetTax().GetFieldType()) )
			return;

		if ( Constants.FieldID_Number.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				new BigDecimal(pobjValue.GetValue());
			}
			catch (NumberFormatException e)
			{
				AppendTag(pstrBuilder, pobjValue);
				pstrBuilder.append("é do tipo Número, e está incorrectamente preenchido.\n");
			}
		}

		if ( Constants.FieldID_Boolean.equals(pobjValue.GetTax().GetFieldType()) )
		{
			if ( !("1".equals(pobjValue.GetValue())) && !("0".equals(pobjValue.GetValue())) )
				pstrBuilder.append("é do tipo Sim ou Não, e está incorrectamente preenchido.\n");
		}

		if ( Constants.FieldID_Date.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				Timestamp.valueOf(pobjValue.GetValue() + " 00:00:00.0");
			}
			catch (IllegalArgumentException e)
			{
				AppendTag(pstrBuilder, pobjValue);
				pstrBuilder.append("é do tipo Data, e está incorrectamente preenchido.\n");
			}
		}

		if ( Constants.FieldID_List.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				UUID.fromString(pobjValue.GetValue());
			}
			catch (IllegalArgumentException e)
			{
				AppendTag(pstrBuilder, pobjValue);
				pstrBuilder.append("é do tipo Lista, e está incorrectamente preenchido.\n");
			}
		}

		if ( Constants.FieldID_Reference.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				UUID.fromString(pobjValue.GetValue());
			}
			catch (IllegalArgumentException e)
			{
				AppendTag(pstrBuilder, pobjValue);
				pstrBuilder.append("é do tipo Referência, e está incorrectamente preenchido.\n");
			}
		}
	}

	private void AppendTag(StringBuilder pstrBuilder, PolicyValue pobjValue)
	{
		UUID lidObject;
		UUID lidExercise;

		pstrBuilder.append("O campo '").append(pobjValue.GetTax().getLabel()).append("' ");
		if ( (lidObject = pobjValue.GetObjectID()) != null )
		{
			pstrBuilder.append("do objecto seguro '");
			try
			{
				pstrBuilder.append(PolicyObject.GetInstance(Engine.getCurrentNameSpace(), lidObject).getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o nome do objecto.)");
			}
			pstrBuilder.append("' ");
		}
		if ( (lidExercise = pobjValue.GetExerciseID()) != null )
		{
			pstrBuilder.append("para o exercício de '");
			try
			{
				pstrBuilder.append(PolicyExercise.GetInstance(Engine.getCurrentNameSpace(), lidExercise).getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o nome do exercício.)");
			}
			pstrBuilder.append("' ");
		}
	}
}
