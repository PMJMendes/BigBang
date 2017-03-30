package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

/**
 * Class to map the tblBBSubCasualtyFramingEntities table
 */
public class SubCasualtyFramingEntities extends ObjectBase {

	public static class I {
		public static int TYPE = 0;
		public static int SUBFRAMING = 1;
		public static int ENTITYTYPE = 2;
		public static int EVALUATION = 3;
		public static int EVALUATIONNOTES = 4;
	}
	
	public static SubCasualtyFramingEntities GetInstance(UUID pidNameSpace,
			UUID pidKey) throws BigBangJewelException {
		try {
			return (SubCasualtyFramingEntities) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyFramingEntities), pidKey);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	public static SubCasualtyFramingEntities GetInstance(UUID pidNameSpace,
			ResultSet prsObject) throws BigBangJewelException {
		try {
			return (SubCasualtyFramingEntities) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyFramingEntities),
					prsObject);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize() throws JewelEngineException {
	}	
}
