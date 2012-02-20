package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyCalculationException;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
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
		return Constants.OPID_SubPolicy_PerformComputations;
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
		SubPolicy lobjSubPolicy;
		DetailedBase lobjCalc;

		try
		{
			lobjSubPolicy = (SubPolicy)GetProcess().GetData();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			lobjCalc = lobjSubPolicy.GetDetailedObject();

			if ( lobjCalc == null )
				throw new PolicyCalculationException("Esta modalidade nao tem cálculos detalhados para efectuar em apólices adesão.");

			mstrReport = lobjCalc.DoSubCalc(pdb);
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

}
