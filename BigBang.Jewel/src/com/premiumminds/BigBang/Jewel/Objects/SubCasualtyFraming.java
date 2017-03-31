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
		public static int SUBCASUALTY = 0;
		public static int ANALYSISDATE = 1;
		public static int FRAMINGDIFFICULTY = 2;
		public static int VALIDPOLICY = 3;
		public static int VALIDITYNOTES = 4;
		public static int GENERALEXCLUSIONS = 5;
		public static int GENERALEXCLUSIONSNOTES = 6;
		public static int RELEVANTCOVERAGE = 7;
		public static int COVERAGERELEVANCYNOTES = 8;
		public static int COVERAGEVALUE = 9;
		public static int COVERAGEEXCLUSIONS = 10;
		public static int COVERAGEEXCLUSIONSNOTES = 11;
		public static int FRANCHISE = 12;
		public static int DEDUCTIBLETYPE = 13;
		public static int FRANCHISENOTES = 14;
		public static int INSUREREVALUATION = 15;
		public static int INSUREREVALUATIONNOTES = 16;
		public static int EXPERTEVALUATION = 17;
		public static int EXPERTEVALUATIONNOTES = 18;
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
