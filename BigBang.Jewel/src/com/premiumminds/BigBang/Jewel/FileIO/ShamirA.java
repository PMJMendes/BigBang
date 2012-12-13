package com.premiumminds.BigBang.Jewel.FileIO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileFieldData;
import Jewel.Engine.SysObjects.FileSectionData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Data.PolicyObjectData;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ManageData;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

public class ShamirA
	extends FileIOBase
{
	public static final UUID FKPolicy = UUID.fromString("04460908-B283-4AC1-A8C5-A0FE012D2C5E");
	public static final UUID ValueID_SalesDate = UUID.fromString("64883643-ED00-498E-82D4-9F960156930B");
	public static final UUID ValueID_Cost = UUID.fromString("BF80A9FC-AD3F-4630-8DD3-9F9601565146");
	public static final UUID RDef_Imports = UUID.fromString("5D287368-3C08-40BE-B850-A125012D3E12");
	public static final UUID FormatID_ShamirA = UUID.fromString("6B92C238-68CB-455C-A185-A126010E1D2D");
	public static final UUID ObjID_ImportStatus = UUID.fromString("86F90B2E-9AB4-456D-879B-A12600D2B843");

	public static class Fields
	{
	    public static final int LENSID         =  0;
	    public static final int ACTIVATIONDATE =  1;
	}

	public static class StatusCodes
	{
	    public static final UUID Code_0_Ok              = UUID.fromString("042D26D4-F0DF-43EC-B76D-A12600E14EB2");
	    public static final UUID Code_1_InternalError   = UUID.fromString("082DB047-ED33-4B8B-9A75-A12600E19096");
	    public static final UUID Code_2_RepeatedLens    = UUID.fromString("C3978EBF-F45F-464F-8E5A-A12600E199AD");
	    public static final UUID Code_3_ExistingLens    = UUID.fromString("A0C2E7FE-3388-4314-B396-A12600E1A5AE");
	    public static final UUID Code_4_LineFormatError = UUID.fromString("1295A7DF-A2A3-4CFA-A369-A12600E1AF86");
	    public static final UUID Code_5_LensNotFound    = UUID.fromString("3069A1D0-B16F-4241-BB19-A126010E8CC9");
	    public static final UUID Code_6_LensIsActivated = UUID.fromString("C4540139-BE2F-41B5-8E88-A12601119C90");
	}

	public UUID GetStatusTable()
		throws BigBangJewelException
	{
		return ObjID_ImportStatus;
	}

	public void Parse()
		throws BigBangJewelException
	{
		MasterDB ldb;
		HashSet<String> larrSet;
		ArrayList<PolicyObjectData> larrObjects;
		FileSectionData[] larrSales;
		FileFieldData[] larrData;
		int i;
		Policy lobjPolicy;
		ManageData lopMD;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.BeginTrans();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrSet = new HashSet<String>();
		larrObjects = new ArrayList<PolicyObjectData>();

		larrSales = mobjData.getData()[0];

		try
		{
			createSession(ldb);

			for ( i = 0; i < larrSales.length; i++ )
			{
				larrData = larrSales[i].getData();
				try
				{
					ParseSale(i, larrData, larrSet, larrObjects, ldb);
				}
				catch (Throwable e)
				{
				}
			}

			lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), FKPolicy);
			lopMD = new ManageData(lobjPolicy.GetProcessID());
			lopMD.mobjData = new PolicyData();
			lopMD.mobjData.FromObject(lobjPolicy);
			lopMD.mobjData.marrObjects = larrObjects.toArray(new PolicyObjectData[larrObjects.size()]);

			lopMD.Execute(ldb);

			createReport(ldb, "Importação Shamir Activações", RDef_Imports, FormatID_ShamirA.toString() + "|" + midSession.toString());
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Rollback(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { ldb.Rollback(); } catch (SQLException e1) {}
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

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void ParseSale(int plngLine, FileFieldData[] parrData, HashSet<String> parrSet,
			ArrayList<PolicyObjectData> parrObjects, SQLServer pdb)
		throws BigBangJewelException
	{
		String lstrLineText;
		PolicyObject lobjObject;
		PolicyObjectData lobjData;
		String lstrDate;
		Timestamp ldtStart;
		Calendar ldtAux2;
		Timestamp ldtEnd;

		lstrLineText = parrData[Fields.LENSID].getData();

		if ( parrSet.contains(lstrLineText) )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_2_RepeatedLens, null);
			return;
		}

		lobjObject = FindLens(lstrLineText, pdb);

		if ( lobjObject == null )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_5_LensNotFound, null);
			return;
		}

		lobjData = new PolicyObjectData();
		lobjData.FromObject(lobjObject);

		if ( lobjData.mdtInclusion != null )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_6_LensIsActivated, null);
			return;
		}

		try
		{
			lstrDate = ProcessDate(parrData[Fields.ACTIVATIONDATE].getData());
		}
		catch ( BigBangJewelException e )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_4_LineFormatError, null);
			return;
		}

		try
		{
			ldtStart = Timestamp.valueOf(lstrDate + " 00:00:00.0");
	    	ldtAux2 = Calendar.getInstance();
	    	ldtAux2.setTimeInMillis(ldtStart.getTime());
	    	ldtAux2.add(Calendar.YEAR, 1);
	    	ldtEnd = new Timestamp(ldtAux2.getTimeInMillis());
		}
		catch ( Throwable e )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_1_InternalError, null);
			return;
		}

		lobjData.mdtInclusion = ldtStart;
		lobjData.mdtExclusion = ldtEnd;
		lobjData.mbNew = false;
		lobjData.mbDeleted = false;
		parrObjects.add(lobjData);

		createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_0_Ok, lobjData.mid);
	}

	private PolicyObject FindLens(String pstrNumber, SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefObjects;
        ResultSet lrsObjects;
        PolicyObject lobjResult;

		lobjResult = null;

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
        	lrsObjects = lrefObjects.SelectByMembers(pdb, new int[] {PolicyObject.I.NAME, PolicyObject.I.POLICY},
        			new java.lang.Object[] {"!" + pstrNumber, FKPolicy}, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
	        if (lrsObjects.next())
	        	lobjResult = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), lrsObjects);
        }
        catch (Throwable e)
        {
			try { lrsObjects.close(); } catch (Throwable e2) {}
        	throw new BigBangJewelException(e.getMessage(), e);
        }

        try
        {
        	lrsObjects.close();
        }
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjResult;
	}

	private String ProcessDate(String pstrDate)
		throws BigBangJewelException
	{
		if ( (pstrDate == null) || (pstrDate.length() != 8) )
			throw new BigBangJewelException("Erro: Formato inválido na data de activação.");

		return pstrDate.substring(0, 4) + "-" + pstrDate.substring(4, 6) + "-" + pstrDate.substring(6, 8);
	}
}
