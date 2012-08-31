package com.premiumminds.BigBang.Jewel.Detailed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class ATFF
	extends DetailedBase
{
	public ATFF(Policy pobjPolicy, SubPolicy pobjSubPolicy)
	{
		super(pobjPolicy, pobjSubPolicy);
	}

	protected void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
	}

	protected void InnerSubValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
	}

	protected String InnerDoCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		UUID lidExercise;
		BigDecimal ldblBCalc;
		BigDecimal ldblProv;
		BigDecimal[] larrMonths;
		BigDecimal ldblTotal;
		String lstrAux;
		int llngResult;
		int llngDelta;
		int llngLast;
		int llngNum;
		int i, j;
		StringBuilder lstrBuilder;

		j = FindValue(FindFieldID("H", "BCALC"), null, null, 0);
		lstrAux = marrValues[j].GetValue();
		if ( lstrAux == null )
			throw new PolicyCalculationException("Erro: Base de cálculo não está preenchida");
		try
		{
			ldblBCalc = new BigDecimal(Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_FieldValues),
					UUID.fromString(lstrAux)).getLabel());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lidExercise = marrExercises[marrExercises.length - 1].getKey();

		llngResult = FindValue(FindFieldID("LEGAL", "CCALC"), null, lidExercise, j + 1);
		llngDelta = FindValue(FindFieldID("LEGAL", "DELTA"), null, lidExercise, llngResult + 1);

		j = FindValue(FindFieldID("LEGAL", "CPROV"), null, lidExercise, llngDelta + 1);
		lstrAux = marrValues[j].GetValue();
		if ( lstrAux == null )
			ldblProv = null;
		else
			ldblProv = new BigDecimal(lstrAux);

		larrMonths = new BigDecimal[12];

		ldblTotal = new BigDecimal(0);
		llngNum = 0;
		llngLast = -1;
		for ( i = 0; i < 12; i++ )
		{
			j = FindValue(FindFieldID("LEGAL", "C" + (i + 1)), null, lidExercise, j + 1);
			lstrAux = marrValues[j].GetValue();
			if ( lstrAux != null )
			{
				if ( llngLast >= 0 )
					throw new PolicyCalculationException("Erro: O mês " + llngLast + " não está preenchido.");

				larrMonths[i] = new BigDecimal(lstrAux);
				ldblTotal = ldblTotal.add(larrMonths[i]);
				llngNum++;
			}
			else
			{
				if ( llngLast < -1 )
					llngLast = i;
			}
		}

		if ( llngNum == 0 )
		{
			marrValues[llngResult].SetValue("0.00", pdb);
			marrValues[llngDelta].SetValue((ldblProv == null ? null : String.format("%,.2f", (ldblProv))), pdb);
			return "Não há meses preenchidos para calcular para o exercício de " + marrExercises[marrExercises.length - 1].getLabel() + ".";
		}

		lstrBuilder = new StringBuilder();

		lstrBuilder.append("Cálculos efectuados para o exercício de ");
		lstrBuilder.append(marrExercises[marrExercises.length - 1].getLabel());
		lstrBuilder.append(": (N. de meses: ");
		lstrBuilder.append(llngNum);
		lstrBuilder.append("). Total calculado: ");

		ldblTotal = ldblTotal.multiply(ldblBCalc).divide(new BigDecimal(llngNum)).setScale(2, RoundingMode.HALF_UP);
		lstrAux = String.format("%,.2f", (ldblTotal));
		marrValues[llngResult].SetValue(lstrAux, pdb);
		lstrBuilder.append(lstrAux);
		lstrBuilder.append("€. ");

		if ( ldblProv == null )
		{
			lstrBuilder.append("(O capital provisório não está preenchido.)");
			marrValues[llngDelta].SetValue(null, pdb);
		}
		else
		{
			ldblTotal = ldblProv.subtract(ldblTotal);
			lstrAux = String.format("%,.2f", (ldblTotal));
			lstrBuilder.append("Desvio calculado: ");
			lstrBuilder.append(lstrAux);
			lstrBuilder.append("€.");
			marrValues[llngDelta].SetValue(lstrAux, pdb);
		}
		
		return lstrBuilder.toString();
	}

	protected String InnerDoSubCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return "Não houve alterações.";
	}
}
