package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyExercise;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Operations.Policy.OpenNewExercise;

public class Exerciser
{
	@SuppressWarnings("deprecation")
	public static void CreateExercises()
		throws BigBangJewelException
	{
		IEntity lrefSubLines;
		IEntity lrefPolicies;
		MasterDB ldb;
		ResultSet lrs;
		ArrayList<SubLine> larrSubLines;
		ArrayList<Policy> larrPolicies;
		PolicyExercise[] larrExercises;
		OpenNewExercise lopONE;

		try
		{
			lrefSubLines = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubLine));
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs = lrefSubLines.SelectByMembers(ldb, new int[] {SubLine.I.EXERCISETYPE}, new java.lang.Object[] {Constants.ExID_Year}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrSubLines = new ArrayList<SubLine>();
		try
		{
			while ( lrs.next() )
				larrSubLines.add(SubLine.GetInstance(Engine.getCurrentNameSpace(), lrs));
		}
		catch (Throwable e)
		{
			try { lrs.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrPolicies = new ArrayList<Policy>();
		for ( SubLine sl : larrSubLines )
		{
			try
			{
				lrs = lrefPolicies.SelectByMembers(ldb, new int[] {Policy.I.SUBLINE, Policy.I.STATUS, Policy.I.DURATION},
						new java.lang.Object[] {sl.getKey(), Constants.StatusID_Valid, Constants.DurID_Ongoing}, null);
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try
			{
				while ( lrs.next() )
					larrPolicies.add(Policy.GetInstance(Engine.getCurrentNameSpace(), lrs));
			}
			catch (Throwable e)
			{
				try { lrs.close(); } catch (Throwable e1) {}
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try
			{
				lrs.close();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		for ( Policy pol : larrPolicies )
		{
			larrExercises = pol.GetCurrentExercises(ldb);
			if ( (larrExercises.length == 0) ||
					(((Timestamp)larrExercises[0].getAt(PolicyExercise.I.STARTDATE)).getYear() + 1900 >= Calendar.getInstance().get(Calendar.YEAR)) )
				continue;

			lopONE = new OpenNewExercise(pol.GetProcessID());
			lopONE.mobjData = new PolicyData();
			lopONE.mobjData.mid = pol.getKey();

			try
			{
				ldb.BeginTrans();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try
			{
				lopONE.Execute(ldb);
			}
			catch (Throwable e)
			{
				try
				{
					ldb.Rollback();
				}
				catch (Throwable e1)
				{
					try { ldb.Disconnect(); } catch (Throwable e2) {}
					throw new BigBangJewelException(e.getMessage(), e1);
				}
				continue;
			}

			try
			{
				ldb.Commit();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}
}
