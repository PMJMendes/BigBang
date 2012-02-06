package com.premiumminds.BigBang.Jewel.Detailed;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class TestClass
	extends DetailedBase
{
	public TestClass(Policy pobjPolicy)
	{
		super(pobjPolicy);
	}

	protected void InnerValidate(StringBuilder pstrBuilder, String pstrLineBreak)
		throws BigBangJewelException, PolicyValidationException
	{
	}

	protected String InnerDoCalc()
		throws BigBangJewelException, PolicyCalculationException
	{
		throw new PolicyCalculationException("Esta modalidade não tem cálculos detalhados para efectuar.");
	}
}
