package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class SubPolicyObject
	extends ObjectBase
{
	public static class I
	{
		public static int NAME             =  0;
		public static int SUBPOLICY        =  1;
		public static int TYPE             =  2;
		public static int ADDRESS1         =  3;
		public static int ADDRESS2         =  4;
		public static int ZIPCODE          =  5;
		public static int INCLUSIONDATE    =  6;
		public static int EXCLUSIONDATE    =  7;
		public static int FISCALNUMBERI    =  8;
		public static int SEX              =  9;
		public static int DATEOFBIRTH      = 10;
		public static int CLIENTNUMBERI    = 11;
		public static int INSURERIDI       = 12;
		public static int FISCALNUMBERC    = 13;
		public static int PREDOMACTIVITY   = 14;
		public static int GRIEVOUSACTIVITY = 15;
		public static int ACTIVITYNOTES    = 16;
		public static int PRODUCTNOTES     = 17;
		public static int SALESVOLUME      = 18;
		public static int EUENTITY         = 19;
		public static int CLIENTNUMBERC    = 20;
		public static int MAKEANDMODEL     = 21;
		public static int EQUIPMENTDESC    = 22;
		public static int FIRSTREGISTRY    = 23;
		public static int MANUFACTUREYEAR  = 24;
		public static int CLIENTIDE        = 25;
		public static int INSURERIDE       = 26;
		public static int SITEDESCRIPTION  = 27;
		public static int SPECIES          = 28;
		public static int RACE             = 29;
		public static int AGE              = 30;
		public static int CITYNUMBER       = 31;
		public static int ELECTRONICID     = 32;
	}

    public static SubPolicyObject GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (SubPolicyObject)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubPolicyObject), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private SubPolicy mrefOwner;

	public static SubPolicyObject GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubPolicyObject)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubPolicyObject), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
		if ( getAt(1) != null )
		{
			try
			{
				mrefOwner = SubPolicy.GetInstance(getNameSpace(), (UUID)getAt(1));
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}
	}

    public SubPolicy GetOwner()
    {
    	if ( mrefOwner == null )
    	{
			try
			{
				Initialize();
			}
			catch (Throwable e)
			{
			}
    	}

    	return mrefOwner;
    }
}
