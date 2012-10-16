package com.premiumminds.BigBang.Jewel.Detailed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class AutoIndiv
	extends DetailedBase
{
	public AutoIndiv(Policy pobjPolicy, SubPolicy pobjSubPolicy)
	{
		super(pobjPolicy, pobjSubPolicy);
	}

	protected void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
		if ( (marrObjects == null) || (marrObjects.length == 0) )
		{
			pstrBuilder.append("Esta modalidade de apólice obriga a ter uma unidade de risco.\n");
			return;
		}

		if ( marrObjects.length > 1 )
		{
			pstrBuilder.append("Esta modalidade de apólice apenas permite ter uma unidade de risco.\n");
			return;
		}
	}

	protected void InnerSubValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
	}

	protected String InnerDoCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		BigDecimal ldblValue;
		BigDecimal ldblFMax;
		String lstrAux;
		int i, j;
		StringBuilder lstrBuilder;

		if ( (marrObjects == null) || (marrObjects.length != 1) )
			throw new PolicyCalculationException("Esta modalidade de apólice obriga a ter uma e só uma unidade de risco.");

		i = FindValue(FindFieldID("H", "BCALC"), marrObjects[0].getKey(), null, 0);
		lstrAux = marrValues[i].GetValue();
		if ( lstrAux == null )
			throw new PolicyCalculationException("O valor da viatura não está preenchido. Não há cálculos detalhados a efectuar.");

		lstrBuilder = new StringBuilder();

		try
		{
			ldblValue = new BigDecimal(lstrAux.replaceAll("\\.", "").replaceAll(",", "."));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		ldblFMax = ldblValue.multiply(new BigDecimal("0.2")).setScale(2, RoundingMode.HALF_UP);
		j = -1;
		for ( String lstr: new String[] {"CCC", "IRE", "FR", "FNAT", "AVAND", "GT"} )
		{
			j = FindCoverage(FindCoverageID(lstr), j + 1);
			if ( !marrCoverages[j].IsPresent() )
				continue;

			lstrBuilder.append("Para a cobertura de ").append(marrCoverages[j].getLabel()).append(", ");

			i = FindValue(FindFieldID(lstr, "CAP"), null, null, i);
			if ( marrValues[i].GetValue() == null )
			{
				lstrAux = String.format("%,.2f", ldblValue);
				lstrBuilder.append("o capital foi preenchido com o valor da viatura (").append(lstrAux).append(")");
				marrValues[i].SetValue(lstrAux, pdb);
			}

			i = FindValue(FindFieldID(lstr, "TFRANQ"), null, null, i);
			lstrAux = marrValues[i].GetValue();
			if ( lstrAux != null )
			{
				try
				{
					lstrAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), marrValues[i].GetTax().GetRefersToID()),
							UUID.fromString(lstrAux)).getLabel();
				}
				catch (Throwable e)
				{
					throw new BigBangJewelException(e.getMessage(), e);
				}
				if ( "%".equals(lstrAux) )
				{
					i = FindValue(FindFieldID(lstr, "FMAX"), null, null, i);
					if ( marrValues[i].GetValue() == null )
					{
						lstrAux = String.format("%,.2f", ldblFMax);
						lstrBuilder.append(" e a franquia máxima foi preenchida com 20% do mesmo (").append(lstrAux).append(")");
						marrValues[i].SetValue(lstrAux, pdb);
					}
				}
			}

			lstrBuilder.append(". ");
		}

		return lstrBuilder.toString();
	}

	protected String InnerDoSubCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return null;
	}
}
