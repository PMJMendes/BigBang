package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.PolicyValidationException;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class ForceValidateSubPolicy
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ForceValidateSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_ForceValidateSubPolicy;
	}

	protected void Run(SQLServer pdb) 
		throws JewelPetriException
	{
		SubPolicy lobjSubPolicy;
		DetailedBase lobjValidation;

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
			lobjValidation = lobjSubPolicy.GetDetailedObject();

			if ( lobjValidation == null )
				DetailedBase.DefaultSubValidate(lobjSubPolicy);
			else
				lobjValidation.SubValidate();
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
