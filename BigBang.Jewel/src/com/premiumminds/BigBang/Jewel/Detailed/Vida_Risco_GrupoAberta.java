package com.premiumminds.BigBang.Jewel.Detailed;

import java.math.BigDecimal;

import Jewel.Engine.DataAccess.SQLServer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyCoverage;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicyValue;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class Vida_Risco_GrupoAberta
	extends DetailedBase
{
	public Vida_Risco_GrupoAberta(Policy pobjPolicy, SubPolicy pobjSubPolicy)
	{
		super(pobjPolicy, pobjSubPolicy);
	}

	protected void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
		int i;
		PolicyCoverage lobjMAJFM;
		PolicyValue lobjPERCEM;

		i = FindCoverage(FindCoverageID("MAJFM"), 0);
		if ( i < -1 )
			return;
		lobjMAJFM = marrCoverages[i];

		if ( (lobjMAJFM.IsPresent() != null) && lobjMAJFM.IsPresent() )
		{
			i = FindValue(FindFieldID("MAJFM", "PERCEM"), null, null, 0);
			if ( i < -1 )
			{
				pstrBuilder.append("O campo 'Percentagem por Filho' da cobertura 'Majoração do Capital por Filhos Menores'")
						.append(" é de preenchimento obrigatório, mas não está presente na apólice.\n");
				return;
			}
			lobjPERCEM = marrValues[i];

			if ( (lobjPERCEM.GetValue() == null) || !CheckFormat(pstrBuilder, lobjPERCEM, false) )
				return;

			if ( BigDecimal.ZERO.compareTo(new BigDecimal(lobjPERCEM.GetValue().replaceAll("\\.", "").replaceAll(",", "."))) >= 0 )
				pstrBuilder.append("O campo 'Percentagem por Filho' da cobertura 'Majoração do Capital por Filhos Menores'")
						.append(" tem que ter valor maior que zero.\n");
		}
	}

	protected void InnerSubValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
		int i;
		SubPolicyCoverage lobjMAJFM;
		SubPolicyValue lobjPERCEM;

		i = FindSubCoverage(FindCoverageID("MAJFM"), 0);
		if ( i < -1 )
			return;
		lobjMAJFM = marrSubCoverages[i];

		if ( (lobjMAJFM.IsPresent() != null) && lobjMAJFM.IsPresent() )
		{
			i = FindSubValue(FindFieldID("MAJFM", "PERCEM"), null, null, 0);
			if ( i < -1 )
			{
				pstrBuilder.append("O campo 'Percentagem por Filho' da cobertura 'Majoração do Capital por Filhos Menores'")
						.append(" é de preenchimento obrigatório, mas não está presente na apólice.\n");
				return;
			}
			lobjPERCEM = marrSubValues[i];

			if ( (lobjPERCEM.GetValue() == null) || !CheckSubFormat(pstrBuilder, lobjPERCEM, false) )
				return;

			if ( BigDecimal.ZERO.compareTo(new BigDecimal(lobjPERCEM.GetValue().replaceAll("\\.", "").replaceAll(",", "."))) >= 0 )
				pstrBuilder.append("O campo 'Percentagem por Filho' da cobertura 'Majoração do Capital por Filhos Menores'")
						.append(" tem que ter valor maior que zero.\n");
		}
	}

	protected String InnerDoCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return null;
	}

	protected String InnerDoSubCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return null;
	}
}
