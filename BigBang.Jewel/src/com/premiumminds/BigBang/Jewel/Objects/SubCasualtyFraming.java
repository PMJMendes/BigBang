package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

/**
 * Class to map the tblBBSubCasualtyFraming table
 */
public class SubCasualtyFraming extends ObjectBase {

	public static class I {
		public static int TYPE = 0;
		public static int SUBCASUALTY = 1;
		public static int ANALYSISDATE = 2;
		public static int FRAMINGDIFFICULTY = 3;
		public static int VALIDPOLICY = 4;
		public static int VALIDITYNOTES = 5;
		public static int GENERALEXCLUSIONS = 6;
		public static int GENERALEXCLUSIONSNOTES = 7;
		public static int RELEVANTCOVERAGE = 8;
		public static int COVERAGERELEVANCYNOTES = 9;
		public static int COVERAGEVALUE = 10;
		public static int COVERAGEEXCLUSIONS = 11;
		public static int COVERAGEEXCLUSIONSNOTES = 12;
		public static int FRANCHISE = 13;
		public static int DEDUCTIBLETYPE = 14;
		public static int FRANCHISENOTES = 15;
		public static int INSUREREVALUATION = 16;
		public static int INSUREREVALUATIONNOTES = 17;
		public static int EXPERTEVALUATION = 18;
		public static int EXPERTEVALUATIONNOTES = 19;
	}
	
	public static SubCasualtyFraming GetInstance(UUID pidNameSpace,
			UUID pidKey) throws BigBangJewelException {
		try {
			return (SubCasualtyFraming) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyFraming), pidKey);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	public static SubCasualtyFraming GetInstance(UUID pidNameSpace,
			ResultSet prsObject) throws BigBangJewelException {
		try {
			return (SubCasualtyFraming) Engine.GetWorkInstance(Engine
					.FindEntity(pidNameSpace,
							Constants.ObjID_SubCasualtyFraming),
					prsObject);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	public void Initialize() throws JewelEngineException {
	}
}
