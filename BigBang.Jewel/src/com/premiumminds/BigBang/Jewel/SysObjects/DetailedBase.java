package com.premiumminds.BigBang.Jewel.SysObjects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;

public abstract class DetailedBase
{
	protected Policy mobjPolicy;

	public DetailedBase(Policy pobjPolicy)
	{
		mobjPolicy = pobjPolicy;
	}

	public static void DefaultValidate(Policy pobjPolicy)
		throws BigBangJewelException, PolicyValidationException
	{
		StringBuilder lstrBuilder;
		String lstrErrors;

		lstrBuilder = new StringBuilder();
		InnerDefaultValidate(lstrBuilder, pobjPolicy);
		lstrErrors = lstrBuilder.toString();

		if ( (lstrErrors != null) && (lstrErrors.length() != 0) )
			throw new PolicyValidationException(lstrErrors);
	}

	protected abstract void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
			throws BigBangJewelException, PolicyValidationException;
	protected abstract String InnerDoCalc() throws BigBangJewelException, PolicyCalculationException;

	public Policy getData()
	{
		return mobjPolicy;
	}

	public void Validate()
		throws BigBangJewelException, PolicyValidationException
	{
		StringBuilder lstrBuilder;
		String lstrErrors;

		lstrBuilder = new StringBuilder();
		InnerDefaultValidate(lstrBuilder, mobjPolicy);
		InnerValidate(lstrBuilder, "\n");
		lstrErrors = lstrBuilder.toString();

		if ( (lstrErrors != null) && (lstrErrors.length() != 0) )
			throw new PolicyValidationException(lstrErrors);
	}

	public String DoCalc()
		throws BigBangJewelException, PolicyCalculationException
	{
		return InnerDoCalc();
	}

	protected static void InnerDefaultValidate(StringBuilder pstrBuilder, Policy pobjPolicy)
		throws BigBangJewelException
	{
		int i;
		PolicyCoverage[] larrCoverages;
		Hashtable<UUID, UUID> larrTrueCoverages;
		PolicyValue[] larrValues;

		pstrBuilder = new StringBuilder();

		try
		{
			larrCoverages = pobjPolicy.GetCurrentCoverages();
			larrValues = pobjPolicy.GetCurrentValues();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrTrueCoverages = new Hashtable<UUID, UUID>();
		for ( i = 0; i < larrCoverages.length; i++ )
		{
			if ( larrCoverages[i].GetCoverage().IsHeader() ||
					((larrCoverages[i].IsPresent() != null) && larrCoverages[i].IsPresent()) )
				larrTrueCoverages.put(larrCoverages[i].GetCoverage().getKey(), larrCoverages[i].getKey());

			if ( larrCoverages[i].GetCoverage().IsHeader() )
				continue;
			if ( larrCoverages[i].IsPresent() == null )
				pstrBuilder.append("O indicador de presença da cobertura '").append(larrCoverages[i].GetCoverage().getLabel()).
						append("' não está preenchido.\n");
			else if ( larrCoverages[i].GetCoverage().IsMandatory() && !larrCoverages[i].IsPresent())
				pstrBuilder.append("A cobertura '").append(larrCoverages[i].GetCoverage().getLabel()).
						append("' é obrigatória mas não está presente.\n");
		}

		for ( i = 0; i < larrValues.length; i++ )
		{
			if ( larrTrueCoverages.get(larrValues[i].GetTax().GetCoverage().getKey()) == null )
				continue;

			if ( larrValues[i].GetValue() == null )
			{
				if ( larrValues[i].GetTax().IsMandatory() )
				{
					AppendTag(pstrBuilder, larrValues[i]);
					pstrBuilder.append("é de preenchimento obrigatório, mas não está preenchido.\n");
				}
			}
			else
				CheckFormat(pstrBuilder, larrValues[i]);
		}
	}

	protected static void CheckFormat(StringBuilder pstrBuilder, PolicyValue pobjValue)
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

	protected static void AppendTag(StringBuilder pstrBuilder, PolicyValue pobjValue)
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
