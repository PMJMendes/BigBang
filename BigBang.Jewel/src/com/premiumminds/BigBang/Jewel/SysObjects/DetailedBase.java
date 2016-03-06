package com.premiumminds.BigBang.Jewel.SysObjects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
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
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public abstract class DetailedBase
{
	protected Policy mobjPolicy;
	protected SubPolicy mobjSubPolicy;
	protected Coverage[] marrCoverageDefs;
	protected Tax[][] marrFieldDefs;
	protected PolicyCoverage[] marrCoverages;
	protected PolicyObject[] marrObjects;
	protected PolicyExercise[] marrExercises;
	protected PolicyValue[] marrValues;
	protected SubPolicyCoverage[] marrSubCoverages;
	protected SubPolicyObject[] marrSubObjects;
	protected SubPolicyValue[] marrSubValues;

	public static void DefaultValidate(Policy pobjPolicy, SQLServer pdb)
		throws BigBangJewelException, PolicyValidationException
	{
		buildDefaultObject(pobjPolicy, null).Validate(pdb);
	}

	public static void DefaultSubValidate(SubPolicy pobjSubPolicy, SQLServer pdb)
		throws BigBangJewelException, PolicyValidationException
	{
		buildDefaultObject(pobjSubPolicy.GetOwner(), pobjSubPolicy).SubValidate(pdb);
	}

	public DetailedBase(Policy pobjPolicy, SubPolicy pobjSubPolicy)
	{
		mobjPolicy = pobjPolicy;
		mobjSubPolicy = pobjSubPolicy;
	}

	protected abstract void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
			throws BigBangJewelException, PolicyValidationException;
	protected abstract void InnerSubValidate(StringBuilder pstrBuilder, String pstrLineBreak)
			throws BigBangJewelException, PolicyValidationException;
	protected abstract String InnerDoCalc(SQLServer pdb) throws BigBangJewelException, PolicyCalculationException;
	protected abstract String InnerDoSubCalc(SQLServer pdb) throws BigBangJewelException, PolicyCalculationException;

	public Policy getPolicy()
	{
		return mobjPolicy;
	}

	public SubPolicy getSubPolicy()
	{
		return mobjSubPolicy;
	}

	public final void Validate(SQLServer pdb)
		throws BigBangJewelException, PolicyValidationException
	{
		StringBuilder lstrBuilder;
		String lstrErrors;

		ScanPolicy(pdb);

		lstrBuilder = new StringBuilder();
		InnerDefaultValidate(pdb, lstrBuilder);
		InnerValidate(lstrBuilder, "\n");
		lstrErrors = lstrBuilder.toString();

		if ( (lstrErrors != null) && (lstrErrors.length() != 0) )
			throw new PolicyValidationException(lstrErrors);
	}

	public final void SubValidate(SQLServer pdb)
		throws BigBangJewelException, PolicyValidationException
	{
		StringBuilder lstrBuilder;
		String lstrErrors;

		ScanSubPolicy(pdb);

		lstrBuilder = new StringBuilder();
		InnerDefaultSubValidate(pdb, lstrBuilder);
		InnerSubValidate(lstrBuilder, "\n");
		lstrErrors = lstrBuilder.toString();

		if ( (lstrErrors != null) && (lstrErrors.length() != 0) )
			throw new PolicyValidationException(lstrErrors);
	}

	public final String DoCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		ScanPolicy(pdb);

		return InnerDoCalc(pdb);
	}

	public final String DoSubCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		ScanSubPolicy(pdb);

		return InnerDoSubCalc(pdb);
	}

	protected Coverage FindCoverageDef(String pstrCoverageTag)
		throws BigBangJewelException
	{
		int i;

		for ( i = 0; i < marrCoverageDefs.length; i++ )
		{
			if ( pstrCoverageTag.equals(marrCoverageDefs[i].GetTag()) )
				return marrCoverageDefs[i];
		}

		throw new BigBangJewelException("Erro: Tag de cobertura não encontrada.");
	}

	protected Tax FindFieldDef(Coverage pobjCoverage, String pstrFieldTag)
		throws BigBangJewelException
	{
		int i, j;

		for ( i = 0; i < marrCoverageDefs.length; i++ )
		{
			if ( pobjCoverage.getKey().equals(marrCoverageDefs[i].getKey()) )
			{
				for ( j = 0; j < marrFieldDefs[i].length; j++ )
				{
					if ( pstrFieldTag.equals(marrFieldDefs[i][j].GetTag()) )
						return marrFieldDefs[i][j];
				}

				throw new BigBangJewelException("Erro: Tag de campo não encontrada.");
			}
		}

		throw new BigBangJewelException("Erro: Tag de cobertura não encontrada.");
	}

	protected Tax FindFieldDef(String pstrCoverageTag, String pstrFieldTag)
		throws BigBangJewelException
	{
		int i, j;

		for ( i = 0; i < marrCoverageDefs.length; i++ )
		{
			if ( pstrCoverageTag.equals(marrCoverageDefs[i].GetTag()) )
			{
				for ( j = 0; j < marrFieldDefs[i].length; j++ )
				{
					if ( pstrFieldTag.equals(marrFieldDefs[i][j].GetTag()) )
						return marrFieldDefs[i][j];
				}

				throw new BigBangJewelException("Erro: Tag de campo não encontrada.");
			}
		}

		throw new BigBangJewelException("Erro: Tag de cobertura não encontrada.");
	}

	protected int FindCoverage(Coverage pobjCoverage, int plngStart)
	{
		UUID lidCoverage;
		int i;

		lidCoverage = pobjCoverage.getKey();

		for ( i = plngStart; i < marrCoverages.length; i++ )
		{
			if ( marrCoverages[i].GetCoverage().getKey().equals(lidCoverage) )
				return i;
		}

		for ( i = 0; i < plngStart; i++ )
		{
			if ( marrCoverages[i].GetCoverage().getKey().equals(lidCoverage) )
				return i;
		}

		return -1;
	}

	protected int FindValue(Tax pobjField, UUID pidObject, UUID pidExercise, int plngStart)
	{
		UUID lidField;
		int i;

		lidField = pobjField.getKey();

		for ( i = plngStart; i < marrValues.length; i++ )
		{
			if ( (marrValues[i].GetTax().getKey().equals(lidField)) && 
					(((marrValues[i].GetObjectID() == null) && (pidObject == null)) ||
							(marrValues[i].GetObjectID().equals(pidObject))) &&
					(((marrValues[i].GetExerciseID() == null) && (pidExercise == null)) ||
							(marrValues[i].GetExerciseID().equals(pidExercise))) )
				return i;
		}

		for ( i = 0; i < plngStart; i++ )
		{
			if ( (marrValues[i].GetTax().getKey().equals(lidField)) && 
					(((marrValues[i].GetObjectID() == null) && (pidObject == null)) ||
							(marrValues[i].GetObjectID().equals(pidObject))) &&
					(((marrValues[i].GetExerciseID() == null) && (pidExercise == null)) ||
							(marrValues[i].GetExerciseID().equals(pidExercise))) )
				return i;
		}

		return -1;
	}

	protected int FindSubCoverage(Coverage pobjCoverage, int plngStart)
	{
		UUID lidCoverage;
		int i;

		lidCoverage = pobjCoverage.getKey();

		for ( i = plngStart; i < marrSubCoverages.length; i++ )
		{
			if ( marrSubCoverages[i].GetCoverage().getKey().equals(lidCoverage) )
				return i;
		}

		for ( i = 0; i < plngStart; i++ )
		{
			if ( marrSubCoverages[i].GetCoverage().getKey().equals(lidCoverage) )
				return i;
		}

		return -1;
	}

	protected int FindSubValue(Tax pobjField, UUID pidObject, UUID pidExercise, int plngStart)
	{
		UUID lidField;
		int i;

		lidField = pobjField.getKey();

		for ( i = plngStart; i < marrSubValues.length; i++ )
		{
			if ( (marrSubValues[i].GetTax().getKey().equals(lidField)) && 
					(((marrSubValues[i].GetObjectID() == null) && (pidObject == null)) ||
							(marrSubValues[i].GetObjectID().equals(pidObject))) &&
					(((marrSubValues[i].GetExerciseID() == null) && (pidExercise == null)) ||
							(marrSubValues[i].GetExerciseID().equals(pidExercise))) )
				return i;
		}

		for ( i = 0; i < plngStart; i++ )
		{
			if ( (marrSubValues[i].GetTax().getKey().equals(lidField)) && 
					(((marrSubValues[i].GetObjectID() == null) && (pidObject == null)) ||
							(marrSubValues[i].GetObjectID().equals(pidObject))) &&
					(((marrSubValues[i].GetExerciseID() == null) && (pidExercise == null)) ||
							(marrSubValues[i].GetExerciseID().equals(pidExercise))) )
				return i;
		}

		return -1;
	}

	protected boolean CheckFormat(StringBuilder pstrBuilder, PolicyValue pobjValue, boolean pbVerbose)
	{
		if ( Constants.FieldID_Text.equals(pobjValue.GetTax().GetFieldType()) )
			return true;

		if ( Constants.FieldID_Number.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				new BigDecimal(pobjValue.GetValue().replaceAll("\\.", "").replaceAll(",", "."));
			}
			catch (NumberFormatException e)
			{
				if ( pbVerbose )
				{
					AppendTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Número, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_Boolean.equals(pobjValue.GetTax().GetFieldType()) )
		{
			if ( !("1".equals(pobjValue.GetValue())) && !("0".equals(pobjValue.GetValue())) )
			{
				if ( pbVerbose )
				{
					AppendTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Sim ou Não, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_Date.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				Timestamp.valueOf(pobjValue.GetValue() + " 00:00:00.0");
			}
			catch (IllegalArgumentException e)
			{
				if ( pbVerbose )
				{
					AppendTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Data, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_List.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				UUID.fromString(pobjValue.GetValue());
			}
			catch (IllegalArgumentException e)
			{
				if ( pbVerbose )
				{
					AppendTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Lista, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_Reference.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				UUID.fromString(pobjValue.GetValue());
			}
			catch (IllegalArgumentException e)
			{
				if ( pbVerbose )
				{
					AppendTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Referência, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		return true;
	}

	protected void AppendTag(StringBuilder pstrBuilder, PolicyValue pobjValue)
	{
		UUID lidObject;
		UUID lidExercise;

		pstrBuilder.append("O campo '").append(pobjValue.GetTax().getLabel()).append("' ");
		pstrBuilder.append("da cobertura '").append(pobjValue.GetTax().GetCoverage().getLabel()).append("' ");
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

	protected boolean CheckSubFormat(StringBuilder pstrBuilder, SubPolicyValue pobjValue, boolean pbVerbose)
	{
		if ( Constants.FieldID_Text.equals(pobjValue.GetTax().GetFieldType()) )
			return true;

		if ( Constants.FieldID_Number.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				new BigDecimal(pobjValue.GetValue().replaceAll("\\.", "").replaceAll(",", "."));
			}
			catch (NumberFormatException e)
			{
				if ( pbVerbose )
				{
					AppendSubTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Número, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_Boolean.equals(pobjValue.GetTax().GetFieldType()) )
		{
			if ( !("1".equals(pobjValue.GetValue())) && !("0".equals(pobjValue.GetValue())) )
			{
				if ( pbVerbose )
				{
					AppendSubTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Sim ou Não, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_Date.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				Timestamp.valueOf(pobjValue.GetValue() + " 00:00:00.0");
			}
			catch (IllegalArgumentException e)
			{
				if ( pbVerbose )
				{
					AppendSubTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Data, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_List.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				UUID.fromString(pobjValue.GetValue());
			}
			catch (IllegalArgumentException e)
			{
				if ( pbVerbose )
				{
					AppendSubTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Lista, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		if ( Constants.FieldID_Reference.equals(pobjValue.GetTax().GetFieldType()) )
		{
			try
			{
				UUID.fromString(pobjValue.GetValue());
			}
			catch (IllegalArgumentException e)
			{
				if ( pbVerbose )
				{
					AppendSubTag(pstrBuilder, pobjValue);
					pstrBuilder.append("é do tipo Referência, e está incorrectamente preenchido.\n");
				}
				return false;
			}
			return true;
		}

		return true;
	}

	protected void AppendSubTag(StringBuilder pstrBuilder, SubPolicyValue pobjValue)
	{
		UUID lidObject;
		UUID lidExercise;

		pstrBuilder.append("O campo '").append(pobjValue.GetTax().getLabel()).append("' ");
		if ( (lidObject = pobjValue.GetObjectID()) != null )
		{
			pstrBuilder.append("do objecto seguro '");
			try
			{
				pstrBuilder.append(SubPolicyObject.GetInstance(Engine.getCurrentNameSpace(), lidObject).getLabel());
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

	private void ScanPolicy(SQLServer pdb)
		throws BigBangJewelException
	{
		int i;

		marrCoverageDefs = mobjPolicy.GetSubLine().GetCurrentCoverages();
		marrFieldDefs = new Tax[marrCoverageDefs.length][];
		for ( i = 0; i < marrCoverageDefs.length; i++ )
			marrFieldDefs[i] = marrCoverageDefs[i].GetCurrentTaxes();

		marrCoverages = mobjPolicy.GetCurrentCoverages(pdb);
		marrObjects = mobjPolicy.GetCurrentObjects(pdb);
		marrExercises = mobjPolicy.GetCurrentExercises(pdb);
		marrValues = mobjPolicy.GetCurrentValues(pdb);
	}

	private void InnerDefaultValidate(SQLServer pdb, StringBuilder pstrBuilder)
		throws BigBangJewelException
	{
		int i;
		HashMap<UUID, UUID> larrTrueCoverages;

		BigDecimal  policySalesPremium = (BigDecimal) mobjPolicy.getAt(Policy.I.PREMIUM);
		if (policySalesPremium == null) {
			pstrBuilder.append("O prémio comercial anual é de preenchimento obrigatório mas não está preenchido.\n");
		} else if (policySalesPremium.compareTo(BigDecimal.ZERO) == 0 ||  policySalesPremium.signum() == -1) {
			pstrBuilder.append("O prémio comercial anual deve corresponder a um valor positivo.\n");
		}

		BigDecimal  policyTotalPremium = (BigDecimal) mobjPolicy.getAt(Policy.I.TOTALPREMIUM);
		if (policyTotalPremium == null) {
			pstrBuilder.append("O prémio total anual é de preenchimento obrigatório mas não está preenchido.\n");
		} else if (policyTotalPremium.compareTo(BigDecimal.ZERO) == 0 ||  policyTotalPremium.signum() == -1) {
			pstrBuilder.append("O prémio total anual deve corresponder a um valor positivo.\n");
		}

		if ( mobjPolicy.getLabel().charAt(0) == '-' )
			pstrBuilder.append("O número de apólice é provisório.\n");

		larrTrueCoverages = new HashMap<UUID, UUID>();
		for ( i = 0; i < marrCoverages.length; i++ )
		{
			if ( marrCoverages[i].GetCoverage().IsHeader() ||
					((marrCoverages[i].IsPresent() != null) && marrCoverages[i].IsPresent()) )
			{
				larrTrueCoverages.put(marrCoverages[i].GetCoverage().getKey(), marrCoverages[i].getKey());

				ResetDeductibles(i, pdb);
			}

			if ( marrCoverages[i].GetCoverage().IsHeader() )
				continue;
			if ( marrCoverages[i].IsPresent() == null )
				pstrBuilder.append("O indicador de presença da cobertura '").append(marrCoverages[i].GetCoverage().getLabel()).
						append("' não está preenchido.\n");
			else if ( marrCoverages[i].GetCoverage().IsMandatory() && !marrCoverages[i].IsPresent())
				pstrBuilder.append("A cobertura '").append(marrCoverages[i].GetCoverage().getLabel()).
						append("' é obrigatória mas não está presente.\n");
		}

		for ( i = 0; i < marrValues.length; i++ )
		{
			if ( larrTrueCoverages.get(marrValues[i].GetTax().GetCoverage().getKey()) == null )
				continue;

			if ( marrValues[i].GetValue() == null )
			{
				if ( marrValues[i].GetTax().IsMandatory() )
				{
					AppendTag(pstrBuilder, marrValues[i]);
					pstrBuilder.append("é de preenchimento obrigatório, mas não está preenchido.\n");
				}
			}
			else
				CheckFormat(pstrBuilder, marrValues[i], true);
		}
	}

	private void ResetDeductibles(int plngCoverage, SQLServer pdb)
		throws BigBangJewelException
	{
		int j;
		int k;
		Tax lobjDeductibleType;
		Tax lobjDeductible;
		PolicyObject[] larrAuxObjects;
		PolicyExercise[] larrAuxExercises;
		PolicyObject[] larrAuxObjects2;
		PolicyExercise[] larrAuxExercises2;

		lobjDeductibleType = null;
		lobjDeductible = null;
		try {
			lobjDeductibleType = FindFieldDef(marrCoverages[plngCoverage].GetCoverage(), "TFRANQ");
			lobjDeductible = FindFieldDef(marrCoverages[plngCoverage].GetCoverage(), "FRANQ");
		} catch (BigBangJewelException e) { /*Ignore Not Found exceptions*/ }
		if ( lobjDeductible != null )
		{
			larrAuxObjects = (lobjDeductibleType.GetVariesByObject() ? marrObjects : new PolicyObject[] {null});
			larrAuxExercises = (lobjDeductibleType.GetVariesByObject() ? marrExercises : new PolicyExercise[] {null});
			j = 0;
			for (PolicyObject lobjAuxObj : larrAuxObjects)
			{
				for (PolicyExercise lobjAuxEx : larrAuxExercises)
				{
					j = FindValue(lobjDeductibleType,
							lobjAuxObj == null ? null : lobjAuxObj.getKey(),
							lobjAuxEx == null ? null : lobjAuxEx.getKey(),
							j);
					if (j < 0)
					{
						j = 0;
						continue;
					}
					if ( (marrValues[j].GetValue() != null) &&
							marrValues[j].GetValue().equals(Constants.ObjID_NoDeductibleType.toString()) )
					{
						larrAuxObjects2 = (lobjAuxObj == null ?
								(lobjDeductible.GetVariesByObject() ? marrObjects : new PolicyObject[] {null}) :
								new PolicyObject[] {lobjAuxObj});
						larrAuxExercises2 = (lobjAuxEx == null ?
								(lobjDeductibleType.GetVariesByObject() ? marrExercises : new PolicyExercise[] {null}) :
								new PolicyExercise[] {lobjAuxEx});
						k = 0;
						for (PolicyObject lobjAuxObj2 : larrAuxObjects2)
						{
							for (PolicyExercise lobjAuxEx2 : larrAuxExercises2)
							{
								k = FindValue(lobjDeductible,
										lobjAuxObj2 == null ? null : lobjAuxObj2.getKey(),
										lobjAuxEx2 == null ? null : lobjAuxEx2.getKey(),
										k);
								if (k < 0)
								{
									k = 0;
									continue;
								}
								marrValues[k].SetValue("0", pdb);
							}
						}
					}
				}
			}
		}
	}

	private void ScanSubPolicy(SQLServer pdb)
		throws BigBangJewelException
	{
		int i;

		marrCoverageDefs = mobjPolicy.GetSubLine().GetCurrentCoverages();
		marrFieldDefs = new Tax[marrCoverageDefs.length][];
		for ( i = 0; i < marrCoverageDefs.length; i++ )
			marrFieldDefs[i] = marrCoverageDefs[i].GetCurrentTaxes();

		marrExercises = mobjPolicy.GetCurrentExercises(pdb);

		marrSubCoverages = mobjSubPolicy.GetCurrentCoverages(pdb);
		marrSubObjects = mobjSubPolicy.GetCurrentObjects(pdb);
		marrSubValues = mobjSubPolicy.GetCurrentValues(pdb);
	}

	private void InnerDefaultSubValidate(SQLServer pdb, StringBuilder pstrBuilder)
		throws BigBangJewelException
	{
		int i;
		HashMap<UUID, UUID> larrTrueCoverages;

		larrTrueCoverages = new HashMap<UUID, UUID>();
		for ( i = 0; i < marrCoverages.length; i++ )
		{
			if ( marrCoverages[i].GetCoverage().IsHeader() ||
					((marrCoverages[i].IsPresent() != null) && marrCoverages[i].IsPresent()) )
			{
				larrTrueCoverages.put(marrCoverages[i].GetCoverage().getKey(), marrCoverages[i].getKey());

				ResetSubDeductibles(i, pdb);
			}

			if ( marrCoverages[i].GetCoverage().IsHeader() )
				continue;
			if ( marrCoverages[i].IsPresent() == null )
				pstrBuilder.append("O indicador de presença da cobertura '").append(marrCoverages[i].GetCoverage().getLabel()).
						append("' não está preenchido.\n");
			else if ( marrCoverages[i].GetCoverage().IsMandatory() && !marrCoverages[i].IsPresent())
				pstrBuilder.append("A cobertura '").append(marrCoverages[i].GetCoverage().getLabel()).
						append("' é obrigatória mas não está presente.\n");
		}

		for ( i = 0; i < marrSubValues.length; i++ )
		{
			if ( larrTrueCoverages.get(marrSubValues[i].GetTax().GetCoverage().getKey()) == null )
				continue;

			if ( marrSubValues[i].GetValue() == null )
			{
				if ( marrSubValues[i].GetTax().IsMandatory() )
				{
					AppendSubTag(pstrBuilder, marrSubValues[i]);
					pstrBuilder.append("é de preenchimento obrigatório, mas não está preenchido.\n");
				}
			}
			else
				CheckSubFormat(pstrBuilder, marrSubValues[i], true);
		}
	}

	private void ResetSubDeductibles(int plngSubCoverage, SQLServer pdb)
		throws BigBangJewelException
	{
		int j;
		int k;
		Tax lobjDeductibleType;
		Tax lobjDeductible;
		SubPolicyObject[] larrAuxObjects;
		PolicyExercise[] larrAuxExercises;
		SubPolicyObject[] larrAuxObjects2;
		PolicyExercise[] larrAuxExercises2;

		lobjDeductibleType = null;
		lobjDeductible = null;
		try {
			lobjDeductibleType = FindFieldDef(marrSubCoverages[plngSubCoverage].GetCoverage(), "TFRANQ");
			lobjDeductible = FindFieldDef(marrSubCoverages[plngSubCoverage].GetCoverage(), "FRANQ");
		} catch (BigBangJewelException e) { /*Ignore Not Found exceptions*/ }
		if ( lobjDeductible != null )
		{
			larrAuxObjects = (lobjDeductibleType.GetVariesByObject() ? marrSubObjects : new SubPolicyObject[] {null});
			larrAuxExercises = (lobjDeductibleType.GetVariesByObject() ? marrExercises : new PolicyExercise[] {null});
			j = 0;
			for (SubPolicyObject lobjAuxObj : larrAuxObjects)
			{
				for (PolicyExercise lobjAuxEx : larrAuxExercises)
				{
					j = FindSubValue(lobjDeductibleType,
							lobjAuxObj == null ? null : lobjAuxObj.getKey(),
							lobjAuxEx == null ? null : lobjAuxEx.getKey(),
							j);
					if (j < 0)
					{
						j = 0;
						continue;
					}
					if ( (marrSubValues[j].GetValue() != null) &&
							marrSubValues[j].GetValue().equals(Constants.ObjID_NoDeductibleType.toString()) )
					{
						larrAuxObjects2 = (lobjAuxObj == null ?
								(lobjDeductible.GetVariesByObject() ? marrSubObjects : new SubPolicyObject[] {null}) :
								new SubPolicyObject[] {lobjAuxObj});
						larrAuxExercises2 = (lobjAuxEx == null ?
								(lobjDeductibleType.GetVariesByObject() ? marrExercises : new PolicyExercise[] {null}) :
								new PolicyExercise[] {lobjAuxEx});
						k = 0;
						for (SubPolicyObject lobjAuxObj2 : larrAuxObjects2)
						{
							for (PolicyExercise lobjAuxEx2 : larrAuxExercises2)
							{
								k = FindSubValue(lobjDeductible,
										lobjAuxObj2 == null ? null : lobjAuxObj2.getKey(),
										lobjAuxEx2 == null ? null : lobjAuxEx2.getKey(),
										k);
								if (k < 0)
								{
									k = 0;
									continue;
								}
								marrSubValues[k].SetValue("0", pdb);
							}
						}
					}
				}
			}
		}
	}

	private static DetailedBase buildDefaultObject(Policy pobjPolicy, SubPolicy pobjSubPolicy) {
		DetailedBase aux = new DetailedBase(pobjPolicy, pobjSubPolicy) {
			@Override protected void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
					throws BigBangJewelException, PolicyValidationException {}
			@Override protected void InnerSubValidate(StringBuilder pstrBuilder, String pstrLineBreak)
					throws BigBangJewelException, PolicyValidationException {}
			@Override protected String InnerDoSubCalc(SQLServer pdb)
					throws BigBangJewelException, PolicyCalculationException { return null; }
			@Override protected String InnerDoCalc(SQLServer pdb)
					throws BigBangJewelException, PolicyCalculationException { return null; }
		};
		return aux;
	}
}
