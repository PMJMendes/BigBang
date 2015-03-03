package com.premiumminds.BigBang.Jewel.Objects;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.DetailedBase;

public class SubLine
	extends ObjectBase
{
	public static class I
	{
		public static final int NAME         = 0;
		public static final int LINE         = 1;
		public static final int OBJTYPE      = 2;
		public static final int EXERCISETYPE = 3;
		public static final int CALCCLASS    = 4;
		public static final int PERCENT      = 5;
		public static final int ISLIFE       = 6;
		public static final int DESCRIPTION  = 7;
		public static final int BISHR        = 8;
	}

    public static SubLine GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubLine), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static SubLine GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (SubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubLine), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private Line mrefLine;
    private Class<?> mrefClass;
    private Constructor<?> mrefConst;

    public void Initialize()
		throws JewelEngineException
	{
    	try
    	{
			mrefLine = Line.GetInstance(getNameSpace(), (UUID)getAt(1));
		}
    	catch (Throwable e)
    	{
    		throw new JewelEngineException(e.getMessage(), e);
		}
	}

    public UUID getObjectType()
    {
    	return (UUID)getAt(I.OBJTYPE);
    }

    public UUID getExerciseType()
    {
    	return (UUID)getAt(I.EXERCISETYPE);
    }

    public Line getLine()
    {
    	if ( mrefLine == null )
    	{
    		try
    		{
				Initialize();
			}
    		catch (Throwable e)
    		{
			}
    	}

    	return mrefLine;
    }

    public Coverage[] GetCurrentCoverages()
    	throws BigBangJewelException
    {
		Entity lrefCoverages;
		MasterDB ldb;
		ArrayList<Coverage> larrAux;
		ResultSet lrsCoverages;

		larrAux = new ArrayList<Coverage>();

		try
		{
			lrefCoverages = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Coverage)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}


		try
		{
			lrsCoverages = lrefCoverages.SelectByMembers(ldb, new int[] {Constants.FKSubLine_In_Coverage},
					new java.lang.Object[] {getKey()}, new int[] {-Constants.Mandatory_In_Coverage, 0});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsCoverages.next() )
				larrAux.add(Coverage.GetInstance(Engine.getCurrentNameSpace(), lrsCoverages));
		}
		catch (Throwable e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoverages.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrAux.toArray(new Coverage[larrAux.size()]);
    }

    public DetailedBase GetDetailedObject(Policy pobjPolicy, SubPolicy pobjSubPolicy)
    	throws BigBangJewelException
    {
    	if ( getAt(I.CALCCLASS) == null )
    		return null;

		try
		{
			if ( mrefClass == null )
				mrefClass = Class.forName(((String)getAt(I.CALCCLASS)).replaceAll("MADDS", "Jewel"));
			if ( mrefConst == null )
				mrefConst = mrefClass.getConstructor(new Class<?>[] {Policy.class, SubPolicy.class});
			return (DetailedBase)mrefConst.newInstance(new java.lang.Object[] {pobjPolicy, pobjSubPolicy});
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
    }

    public BigDecimal getPercent()
    {
    	return (BigDecimal)getAt(I.PERCENT);
    }

    public boolean getIsLife()
    {
    	return (Boolean)getAt(I.ISLIFE);
    }

    public String getDescription()
    {
    	if ( getAt(I.DESCRIPTION) == null )
    		return getLine().getCategory().getLabel();

    	return (String)getAt(I.DESCRIPTION);
    }

    public boolean isHR()
    {
    	if ( getAt(I.BISHR) == null )
    		return false;

    	return (Boolean)getAt(I.BISHR);
    }
}
