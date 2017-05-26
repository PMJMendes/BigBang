package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

/**
 * Class to map the SubCasualtyFramingHeadings table
 */
public class SubCasualtyFramingHeadings extends ObjectBase {

	public static class I {
		public static int SUBFRAMING 	= 0;
		public static int BASESALARY 	= 1;
		public static int FEEDALLOWANCE	= 2;
		public static int OTHERFEES12	= 3;
		public static int OTHERFEES14	= 4;
	}
	
	public static SubCasualtyFramingHeadings GetInstance(UUID pidNameSpace,
			UUID pidKey) throws BigBangJewelException {
		try {
			return (SubCasualtyFramingHeadings) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyFramingHeadings), pidKey);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	public static SubCasualtyFramingHeadings GetInstance(UUID pidNameSpace,
			ResultSet prsObject) throws BigBangJewelException {
		try {
			return (SubCasualtyFramingHeadings) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyFramingHeadings),
					prsObject);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	public void Initialize() throws JewelEngineException {
	}	
}
