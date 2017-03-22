package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

/**
 * Class to map the tblBBSubCasualtyInsurerRequest table
 */
public class SubCasualtyInsurerRequest extends ObjectBase {

	public static class I {
		public static int TYPE = 0;
		public static int REQUESTDATE = 1;
		public static int ACCEPTANCEDATE = 2;
		public static int CONFORMITY = 3;
		public static int RESENDDATE = 4;
		public static int CLARIFICATIONDATE = 5;
		public static int SUBCASUALTY =  6;
		public static int CLARIFICATIONTYPE = 7;
	}

	public static SubCasualtyInsurerRequest GetInstance(UUID pidNameSpace,
			UUID pidKey) throws BigBangJewelException {
		try {
			return (SubCasualtyInsurerRequest) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyInsurerRequest), pidKey);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static SubCasualtyInsurerRequest GetInstance(UUID pidNameSpace,
			ResultSet prsObject) throws BigBangJewelException {
		try {
			return (SubCasualtyInsurerRequest) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyInsurerRequest),
					prsObject);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize() throws JewelEngineException {
	}

}
