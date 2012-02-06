package com.premiumminds.BigBang.Jewel.SysObjects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Coverage;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public abstract class DetailedBase
{
	protected Policy mobjPolicy;
	protected PolicyCoverage[] marrCoverages;
	protected PolicyObject[] marrObjects;
	protected PolicyExercise[] marrExercises;
	protected PolicyValue[] marrValues;
	protected Coverage[] marrCoverageDefs;
	protected Tax[][] marrFieldDefs;

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
	protected abstract String InnerDoCalc(SQLServer pdb) throws BigBangJewelException, PolicyCalculationException;

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

	public String DoCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		int i;

		marrCoverages = mobjPolicy.GetCurrentCoverages();
		marrObjects = mobjPolicy.GetCurrentObjects();
		marrExercises = mobjPolicy.GetCurrentExercises();
		marrValues = mobjPolicy.GetCurrentValues();

		marrCoverageDefs = mobjPolicy.GetSubLine().GetCurrentCoverages();
		marrFieldDefs = new Tax[marrCoverageDefs.length][];
		for ( i = 0; i < marrCoverageDefs.length; i++ )
			marrFieldDefs[i] = marrCoverageDefs[i].GetCurrentTaxes();

		return InnerDoCalc(pdb);
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

	protected UUID FindCoverageID(String pstrCoverageTag)
	{
		int i;

		for ( i = 0; i < marrCoverageDefs.length; i++ )
		{
			if ( pstrCoverageTag.equals(marrCoverageDefs[i].GetTag()) )
				return marrCoverageDefs[i].getKey();
		}

		return null;
	}

	protected UUID FindFieldID(String pstrCoverageTag, String pstrFieldTag)
	{
		int i, j;

		for ( i = 0; i < marrCoverageDefs.length; i++ )
		{
			if ( pstrCoverageTag.equals(marrCoverageDefs[i].GetTag()) )
			{
				for ( j = 0; j < marrFieldDefs[i].length; j++ )
				{
					if ( pstrFieldTag.equals(marrFieldDefs[i][j].GetTag()) )
						return marrFieldDefs[i][j].getKey();
				}
			}
		}

		return null;
	}

	protected int FindCoverage(UUID pidCoverage, int plngStart)
	{
		int i;

		for ( i = plngStart; i < marrCoverages.length; i++ )
		{
			if ( marrCoverages[i].GetCoverage().getKey().equals(pidCoverage) )
				return i;
		}

		for ( i = 0; i < plngStart; i++ )
		{
			if ( marrCoverages[i].GetCoverage().getKey().equals(pidCoverage) )
				return i;
		}

		return -1;
	}

	protected int FindValue(UUID pidField, UUID pidObject, UUID pidExercise, int plngStart)
	{
		int i;

		for ( i = plngStart; i < marrValues.length; i++ )
		{
			if ( (marrValues[i].GetTax().getKey().equals(pidField)) && 
					(((marrValues[i].GetObjectID() == null) && (pidObject == null)) ||
							(marrValues[i].GetObjectID().equals(pidObject))) &&
					(((marrValues[i].GetExerciseID() == null) && (pidExercise == null)) ||
							(marrValues[i].GetExerciseID().equals(pidExercise))) )
				return i;
		}

		for ( i = 0; i < plngStart; i++ )
		{
			if ( (marrValues[i].GetTax().getKey().equals(pidField)) && 
					(((marrValues[i].GetObjectID() == null) && (pidObject == null)) ||
							(marrValues[i].GetObjectID().equals(pidObject))) &&
					(((marrValues[i].GetExerciseID() == null) && (pidExercise == null)) ||
							(marrValues[i].GetExerciseID().equals(pidExercise))) )
				return i;
		}

		return -1;
	}
}
