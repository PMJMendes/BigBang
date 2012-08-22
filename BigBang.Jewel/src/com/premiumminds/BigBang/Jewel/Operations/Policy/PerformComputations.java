package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class PerformComputations
	extends Operation
{
	private static final long serialVersionUID = 1L;

	private String mstrReport;

	public PerformComputations(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_PerformComputations;
	}

	public String ShortDesc()
	{
		return "Execução de Cálculos";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return mstrReport;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;
		DetailedBase lobjCalc;

		try
		{
			lobjPolicy = (Policy)GetProcess().GetData();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			lobjCalc = lobjPolicy.GetDetailedObject();

			if ( lobjCalc == null )
				throw new PolicyCalculationException("Esta modalidade nao tem cálculos detalhados para efectuar.");

			mstrReport = lobjCalc.DoCalc(pdb);

			if ( mstrReport == null )
				throw new PolicyCalculationException("Esta modalidade nao tem cálculos detalhados para efectuar.");
		}
		catch (PolicyCalculationException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	protected boolean IsSilent()
	{
		return (mstrReport == null);
	}
}
