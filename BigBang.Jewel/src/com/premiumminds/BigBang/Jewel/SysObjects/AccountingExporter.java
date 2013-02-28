package com.premiumminds.BigBang.Jewel.SysObjects;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.FileSpec;
import Jewel.Engine.Implementation.TransportChannel;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.Interfaces.IFileField;
import Jewel.Engine.Interfaces.IFileSection;
import Jewel.Engine.Interfaces.IFileSpec;
import Jewel.Engine.Interfaces.ITransportChannel;
import Jewel.Engine.SysObjects.FileData;
import Jewel.Engine.SysObjects.FileFieldData;
import Jewel.Engine.SysObjects.FileSectionData;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AccountingEntry;

public class AccountingExporter
{
	public static void Export()
		throws BigBangJewelException
	{
		IFileSpec lrefSpec;
		IFileSection lrefSection;
		IFileField[] larrFieldSpecs;
		ITransportChannel lrefChannel;
		IEntity lrefEntries;
		MasterDB ldb;
		ResultSet lrs;
		ArrayList<AccountingEntry> larrEntries;
		FileFieldData[] larrFields;
		FileSectionData[][] larrSections;
		FileData lobjFile;
		int i;
		FileXfer lobjResult;
		UUID lidInstance;
		SimpleDateFormat lformat;
		String lstrName;

		try
		{
			lrefSpec = FileSpec.GetInstance(Constants.NSID_BigBang, Constants.FormatID_Accouting);
			lrefSection = lrefSpec.getSections()[0];
			larrFieldSpecs = lrefSection.getFields();
			lrefChannel = TransportChannel.GetInstance(Constants.NSID_BigBang, Constants.ChannelID_Accounting);
			lrefEntries = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AccountingEntry));

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrs = lrefEntries.SelectByMembers(ldb, new int[] {AccountingEntry.I.FILE}, new java.lang.Object[] {null},
					new int[] {AccountingEntry.I.DATE, AccountingEntry.I.NUMBER, AccountingEntry.I.ACCOUNT});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrEntries = new ArrayList<AccountingEntry>();
		try
		{
			while ( lrs.next() )
				larrEntries.add(AccountingEntry.GetInstance(Engine.getCurrentNameSpace(), lrs));
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

		if ( larrEntries.size() > 0 )
		{
			larrSections = new FileSectionData[1][];
			larrSections[0] = new FileSectionData[larrEntries.size()];
			i = 0;
			for ( AccountingEntry lobjEntry: larrEntries )
			{
				larrFields = new FileFieldData[8];
				larrFields[0] = new FileFieldData(larrFieldSpecs[0], ((BigDecimal)lobjEntry.getAt(AccountingEntry.I.ACCOUNT)).toString());
				larrFields[1] = new FileFieldData(larrFieldSpecs[1], ((BigDecimal)lobjEntry.getAt(AccountingEntry.I.VALUE)).toString());
				larrFields[2] = new FileFieldData(larrFieldSpecs[2], (String)lobjEntry.getAt(AccountingEntry.I.SIGN));
				larrFields[3] = new FileFieldData(larrFieldSpecs[3], ((Timestamp)lobjEntry.getAt(AccountingEntry.I.DATE)).toString().substring(0, 10));
				larrFields[4] = new FileFieldData(larrFieldSpecs[4], ((Integer)lobjEntry.getAt(AccountingEntry.I.NUMBER)).toString());
				larrFields[5] = new FileFieldData(larrFieldSpecs[5], ((Integer)lobjEntry.getAt(AccountingEntry.I.BOOK)).toString());
				larrFields[6] = new FileFieldData(larrFieldSpecs[6], (String)lobjEntry.getAt(AccountingEntry.I.SUPPORT));
				larrFields[7] = new FileFieldData(larrFieldSpecs[7], (String)lobjEntry.getAt(AccountingEntry.I.DESCRIPTION));
				larrSections[0][i] = new FileSectionData(lrefSection, larrFields);
				i++;
			}
			lobjFile = new FileData(lrefSpec, larrSections);

			lformat = new SimpleDateFormat("yyyyMMdd.HHmmss");
			lstrName = "mov." + lformat.format(Calendar.getInstance().getTime()) + ".txt";
			try
			{
				lobjResult = lrefSpec.BuildFile(lobjFile, lstrName);
				lidInstance = lrefChannel.PutFile("", lobjResult);

				ldb.BeginTrans();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try
			{
				for ( AccountingEntry lobjEntry: larrEntries )
				{
					lobjEntry.setAt(AccountingEntry.I.FILE, lidInstance);
					lobjEntry.SaveToDb(ldb);
				}
			}
			catch (Throwable e)
			{
				try { ldb.Rollback(); } catch (Throwable e1) {}
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
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

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
