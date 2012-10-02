package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class TriggerValidatePolicy
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerValidatePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ForceValidatePolicy;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Policy lobjPolicy;
		DetailedBase lobjValidation;

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
			lobjValidation = lobjPolicy.GetDetailedObject();

			if ( lobjValidation == null )
				DetailedBase.DefaultValidate(lobjPolicy, pdb);
			else
				lobjValidation.Validate(pdb);
		}
		catch (PolicyValidationException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
