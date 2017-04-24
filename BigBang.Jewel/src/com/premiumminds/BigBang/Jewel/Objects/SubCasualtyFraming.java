package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

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
		public static int COVERAGENOTES = 19;
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
	
	public SubCasualtyFramingEntity[] GetCurrentFramingEntities() throws BigBangJewelException {
    	ArrayList<SubCasualtyFramingEntity> iEntities;
    	IEntity entEntity;
        MasterDB ldb;
        ResultSet resultSet;

        iEntities = new ArrayList<SubCasualtyFramingEntity>();

    	try {
    		entEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualtyFramingEntities)); 
    		ldb = new MasterDB();
    	} catch (Throwable e) {
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
    	
    	try {
    		resultSet = entEntity.SelectByMembers(ldb, new int[] {SubCasualtyFramingEntity.I.SUBFRAMING},
    				new java.lang.Object[] {getKey()}, new int[0]);
    	} catch (Throwable e) {
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
    	
    	try {
    		while (resultSet.next()) {
    			iEntities.add(SubCasualtyFramingEntity.GetInstance(getNameSpace(), resultSet));
    		}
    	} catch (BigBangJewelException e) {
    		try { 
    			resultSet.close(); 
    		} catch (Throwable e1) {
    			
    		}
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw e;
    	} catch (Throwable e) {
    		try { 
    			resultSet.close(); 
    		} catch (Throwable e1) {
    			
    		}
    		
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw new BigBangJewelException(e.getMessage(), e);
    	}

    	try {
    		resultSet.close();
    	} catch (Throwable e) {
    		try { 
    			ldb.Disconnect(); 
    		} catch (Throwable e1) {
    			
    		}
    		throw new BigBangJewelException(e.getMessage(), e);
    	}
    	
    	try {
    		ldb.Disconnect();
    	} catch (Throwable e) {
    		throw new BigBangJewelException(e.getMessage(), e);
    	}

    	return iEntities.toArray(new SubCasualtyFramingEntity[iEntities.size()]);
    }
	
	public void Initialize() throws JewelEngineException {
	}
}
