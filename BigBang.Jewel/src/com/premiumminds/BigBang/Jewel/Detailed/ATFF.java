package com.premiumminds.BigBang.Jewel.Detailed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class ATFF
	extends DetailedBase
{
	public ATFF(Policy pobjPolicy, SubPolicy pobjSubPolicy)
	{
		super(pobjPolicy, pobjSubPolicy);
	}

	@SuppressWarnings("deprecation")
	protected void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
		UUID lidExercise;
		int ex, i, j;

		j = -1;
		for ( ex = 0; ex < marrExercises.length; ex++ )
		{
			lidExercise = marrExercises[ex].getKey();

			for ( i = 0; i < ((Timestamp)marrExercises[ex].getAt(PolicyExercise.I.STARTDATE)).getMonth(); i++ )
			{
				j = FindValue(FindFieldID("LEGAL", "C" + (i + 1)), null, lidExercise, j + 1);
				if ( (j >= 0) && (marrValues[j].GetValue() != null) )
					pstrBuilder.append("O campo do mês ").append(i + 1).append(" do exercício de ")
							.append(marrExercises[marrExercises.length - 1].getLabel())
							.append(" está preenchido, mas é anterior à data de início do exercício.");
			}
		}
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
		BigDecimal ldblTCom;
		BigDecimal ldblProv;
		BigDecimal[] larrMonths;
		BigDecimal ldblTotal;
		String lstrAux;
		int ex;
		int llngResult;
		int llngDelta;
		int llngPremium;
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

		j = FindValue(FindFieldID("H", "TCOM"), null, null, j + 1);
		lstrAux = marrValues[j].GetValue();
		if ( lstrAux == null )
			throw new PolicyCalculationException("Erro: Taxa comercial não está preenchida");
		try
		{
			ldblTCom = new BigDecimal(lstrAux.replaceAll("\\.", "").replaceAll(",", "."));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		ldblTCom = ldblTCom.multiply(new BigDecimal("0.0107")).add(new BigDecimal("0.0015"));

		lstrBuilder = new StringBuilder();

		for ( ex = 0; ex < 2 && ex < marrExercises.length; ex++ )
		{
			lidExercise = marrExercises[ex].getKey();

			llngResult = FindValue(FindFieldID("LEGAL", "CCALC"), null, lidExercise, j + 1);
			llngDelta = FindValue(FindFieldID("LEGAL", "DELTA"), null, lidExercise, llngResult + 1);
			llngPremium = FindValue(FindFieldID("LEGAL", "PCALC"), null, lidExercise, llngDelta + 1);

			j = FindValue(FindFieldID("LEGAL", "CPROV"), null, lidExercise, llngPremium + 1);
			lstrAux = marrValues[j].GetValue();
			if ( lstrAux == null )
				ldblProv = null;
			else
				ldblProv = new BigDecimal(lstrAux.replaceAll("\\.", "").replaceAll(",", "."));

			larrMonths = new BigDecimal[12];

			ldblTotal = BigDecimal.ZERO;
			llngNum = 0;
			llngLast = -1;
			for ( i = 0; i < 12; i++ )
			{
				j = FindValue(FindFieldID("LEGAL", "C" + (i + 1)), null, lidExercise, j + 1);
				lstrAux = marrValues[j].GetValue();
				if ( lstrAux != null )
				{
					if ( llngLast >= 0 )
						throw new PolicyCalculationException("Erro: O mês " + llngLast + " para o exercício de " +
								marrExercises[marrExercises.length - 1].getLabel() + " não está preenchido.");

					larrMonths[i] = new BigDecimal(lstrAux.replaceAll("\\.", "").replaceAll(",", "."));
					ldblTotal = ldblTotal.add(larrMonths[i]);
					llngNum++;
				}
				else
				{
					if ( llngNum > 0 )
						llngLast = i;
				}
			}

			if ( llngNum == 0 )
			{
				marrValues[llngResult].SetValue("0.00", pdb);
				marrValues[llngDelta].SetValue((ldblProv == null ? null : String.format("%,.2f", (ldblProv))), pdb);
				marrValues[llngPremium].SetValue((ldblProv == null ? null :
						String.format("%,.2f", (ldblProv.multiply(ldblTCom).setScale(2, RoundingMode.HALF_UP)))), pdb);
				lstrBuilder.append("Não há meses preenchidos para calcular para o exercício de ")
						.append(marrExercises[marrExercises.length - 1].getLabel()).append(". ");
			}
			else
			{
				lstrBuilder.append("Cálculos efectuados para o exercício de ");
				lstrBuilder.append(marrExercises[marrExercises.length - 1].getLabel());
				lstrBuilder.append(": (N. de meses: ");
				lstrBuilder.append(llngNum);
				lstrBuilder.append("). Total calculado: ");

				if ( llngLast < 0 )
					ldblTotal = ldblTotal.multiply(new BigDecimal("12.0")).divide(new BigDecimal(llngNum), 2, RoundingMode.HALF_UP);
				else
					ldblTotal = ldblTotal.multiply(ldblBCalc).divide(new BigDecimal(llngNum), 2, RoundingMode.HALF_UP);

				lstrAux = String.format("%,.2f", (ldblTotal))/*.replaceAll("\\.", "#").replaceAll(",", ".").replaceAll("#", ",")*/;
				marrValues[llngResult].SetValue(lstrAux, pdb);
				lstrBuilder.append(lstrAux);
				lstrBuilder.append(Utils.getCurrency());
				lstrBuilder.append(". ");

				if ( ldblProv == null )
				{
					lstrBuilder.append("(O capital provisório não está preenchido.) ");
					marrValues[llngDelta].SetValue(null, pdb);
					marrValues[llngPremium].SetValue(null, pdb);
				}
				else
				{
					ldblTotal = ldblTotal.subtract(ldblProv);
					lstrAux = String.format("%,.2f", (ldblTotal))/*.replaceAll("\\.", "#").replaceAll(",", ".").replaceAll("#", ",")*/;
					lstrBuilder.append("Desvio calculado: ");
					lstrBuilder.append(lstrAux);
					lstrBuilder.append(Utils.getCurrency());
					lstrBuilder.append(". ");
					marrValues[llngDelta].SetValue(lstrAux, pdb);

					ldblTotal = ldblTotal.multiply(ldblTCom).setScale(2, RoundingMode.HALF_UP);
					lstrAux = String.format("%,.2f", (ldblTotal))/*.replaceAll("\\.", "#").replaceAll(",", ".").replaceAll("#", ",")*/;
					lstrBuilder.append("Prémio de acerto calculado: ");
					lstrBuilder.append(lstrAux);
					lstrBuilder.append(Utils.getCurrency());
					lstrBuilder.append(". ");
					marrValues[llngPremium].SetValue(lstrAux, pdb);
				}
			}
		}

		return lstrBuilder.toString();
	}

	protected String InnerDoSubCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return "Não houve alterações.";
	}
}
