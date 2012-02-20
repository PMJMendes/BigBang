package com.premiumminds.BigBang.Jewel.Detailed;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class TestClass
	extends DetailedBase
{
	public TestClass(Policy pobjPolicy, SubPolicy pobjSubPolicy)
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
		PolicyValue lobjCap, lobjFranq, lobjTax;
		int i;
		BigDecimal ldblCap, ldblFranq, ldblTax;

		lobjCap = null;
		lobjFranq = null;
		lobjTax = null;

		i = FindValue(FindFieldID("DME", "CAPITAL"), null, null, 0);
		if ( i >= 0 )
			lobjCap = marrValues[i];
		i = FindValue(FindFieldID("DME", "FRANQ"), null, null, i + 1);
		if ( i >= 0 )
			lobjFranq = marrValues[i];
		i = FindValue(FindFieldID("DME", "TAXA"), null, null, i + 1);
		if ( i >= 0 )
			lobjTax = marrValues[i];

		if ( (lobjCap == null) || (lobjTax == null) || (lobjCap.GetValue() == null) || (lobjTax.GetValue() == null) )
			return null;
		try
		{
			ldblCap = new BigDecimal(lobjCap.GetValue());
		}
		catch (NumberFormatException e)
		{
			throw new PolicyCalculationException("Valor inválido para o capital de despesas médicas no estrangeiro.");
		}
		try
		{
			ldblTax = new BigDecimal(lobjTax.GetValue());
		}
		catch (NumberFormatException e)
		{
			throw new PolicyCalculationException("Valor inválido para a taxa de despesas médicas no estrangeiro.");
		}

		ldblFranq = ldblCap.multiply(ldblTax)
				.add(BigDecimal.valueOf(5L)).divideToIntegralValue(BigDecimal.valueOf(10L)).divide(BigDecimal.valueOf(100L))
				.stripTrailingZeros();

		try
		{
			if ( lobjFranq == null )
			{
				lobjFranq = PolicyValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjFranq.setAt(1, mobjPolicy.getKey());
				lobjFranq.setAt(2, FindFieldID("DME", "FRANQ"));
				lobjFranq.setAt(3, null);
				lobjFranq.setAt(4, null);
			}

			lobjFranq.setAt(0,  ldblFranq.toPlainString());
			lobjFranq.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return "Despesas Médicas no Estrangeiro: Calculada a nova franquia.";
	}

	protected String InnerDoSubCalc(SQLServer pdb)
		throws BigBangJewelException, PolicyCalculationException
	{
		return "Não houve alterações.";
	}
}
