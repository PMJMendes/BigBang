package com.premiumminds.BigBang.Jewel.FileIO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.premiumminds.BigBang.Jewel.Data.CasualtyData;
import com.premiumminds.BigBang.Jewel.Data.PolicyObjectData;
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyData;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.CreateSubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.Client.CreateCasualty;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

public class ShamirP
	extends FileIOBase
{
	public static final UUID FKPolicy = UUID.fromString("04460908-B283-4AC1-A8C5-A0FE012D2C5E");
	public static final UUID FKClient = UUID.fromString("AB113677-2276-435D-8366-A0FE00E1B27C");
	public static final UUID FKManager = UUID.fromString("DC1712D5-EDEF-4039-8C82-A0FE00E13F90");
	public static final UUID RDef_Imports = UUID.fromString("5D287368-3C08-40BE-B850-A125012D3E12");
	public static final UUID FormatID_ShamirP = UUID.fromString("9689FDF3-E88E-43FC-80F8-A126011E4265");
	public static final UUID ObjID_ImportStatus = UUID.fromString("86F90B2E-9AB4-456D-879B-A12600D2B843");

	public static class Fields
	{
	    public static final int LENSID            = 0;
	    public static final int PARTICIPATIONDATE = 1;
	    public static final int LENSINDICATOR     = 2;
	    public static final int EVENTINDICATOR    = 3;
	    public static final int SHAMIRPROCESS     = 4;
	    public static final int CASUALTYDATE      = 5;
	}

	public static class StatusCodes
	{
	    public static final UUID Code_0_Ok               = UUID.fromString("042D26D4-F0DF-43EC-B76D-A12600E14EB2");
	    public static final UUID Code_1_InternalError    = UUID.fromString("082DB047-ED33-4B8B-9A75-A12600E19096");
	    public static final UUID Code_2_RepeatedLens     = UUID.fromString("C3978EBF-F45F-464F-8E5A-A12600E199AD");
	    public static final UUID Code_3_ExistingLens     = UUID.fromString("A0C2E7FE-3388-4314-B396-A12600E1A5AE");
	    public static final UUID Code_4_LineFormatError  = UUID.fromString("1295A7DF-A2A3-4CFA-A369-A12600E1AF86");
	    public static final UUID Code_5_LensNotFound     = UUID.fromString("3069A1D0-B16F-4241-BB19-A126010E8CC9");
	    public static final UUID Code_6_LensIsActivated  = UUID.fromString("C4540139-BE2F-41B5-8E88-A12601119C90");
	    public static final UUID Code_7_LensNotActivated = UUID.fromString("92463F7C-FA66-488E-97B3-A126012030B8");
	    public static final UUID Code_8_LensNotCovered   = UUID.fromString("72F2A848-8E4C-457C-8535-A126012040AA");
	    public static final UUID Code_9_ExistingCasualty = UUID.fromString("DEBC5F99-E3E3-4EAC-810C-A126012210FB");
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
		FileSectionData[] larrCasualties;
		FileFieldData[] larrData;
		int i;

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
			createSession(ldb);
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw e;
		}

		larrSet = new HashSet<String>();
		larrCasualties = mobjData.getData()[0];

		for ( i = 0; i < larrCasualties.length; i++ )
		{
			try
			{
				ldb.BeginTrans();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (SQLException e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try
			{
				larrData = larrCasualties[i].getData();
				ParseCasualty(i, larrData, larrSet, ldb);
			}
			catch (Throwable e)
			{
			}

			try
			{
				ldb.Commit();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (SQLException e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		try
		{
			createReport(ldb, "Importação Shamir Participações", RDef_Imports, FormatID_ShamirP.toString() + "|" + midSession.toString());
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
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

	private void ParseCasualty(int plngLine, FileFieldData[] parrData, HashSet<String> parrSet, SQLServer pdb)
		throws BigBangJewelException
	{
		String lstrLineText;
		PolicyObject lobjObject;
		PolicyObjectData lobjData;
		String lstrPDate;
		String lstrCDate;
		Timestamp ldtCasualty;
		String lstrName;
		String lstrDesc;
		Client lobjClient;
		CreateCasualty lopCC;
		Casualty lobjCasualty;
		CreateSubCasualty lopCSC;

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

		if ( lobjData.mdtInclusion == null )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_7_LensNotActivated, null);
			return;
		}

		try
		{
			lstrPDate = ProcessDate(parrData[Fields.PARTICIPATIONDATE].getData());
			lstrCDate = ProcessDate(parrData[Fields.CASUALTYDATE].getData());
		}
		catch ( BigBangJewelException e )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_4_LineFormatError, null);
			return;
		}

		try
		{
			ldtCasualty = Timestamp.valueOf(lstrCDate + " 00:00:00.0");
		}
		catch ( Throwable e )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_1_InternalError, null);
			return;
		}

		if ( ldtCasualty.before(lobjData.mdtInclusion) || ldtCasualty.after(lobjData.mdtExclusion) )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_8_LensNotCovered, null);
			return;
		}

		lstrName = "Lentes N. " + lstrLineText;

		if ( FindCasualty(lstrName, pdb) )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_9_ExistingCasualty, null);
			return;
		}

		switch ( parrData[Fields.EVENTINDICATOR].getData().charAt(0) )
		{
		case 'F':
			lstrDesc = "Furto - ";
			break;
		case 'Q':
			lstrDesc = "Quebra - ";
			break;
		default:
			lstrDesc = "Desconhecido - ";
			break;
		}
		switch ( parrData[Fields.LENSINDICATOR].getData().charAt(0) )
		{
		case 'R':
			lstrDesc += "Lente Direita";
			break;
		case 'L':
			lstrDesc += "Lente Esquerda";
			break;
		case 'B':
			lstrDesc += "Ambas as Lentes";
			break;
		default:
			lstrDesc += "Lente Desconhecida";
			break;
		}

		try
		{
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), FKClient);
			lopCC = new CreateCasualty(lobjClient.GetProcessID());
			lopCC.mobjData = new CasualtyData();
			lopCC.mobjData.mdtCasualtyDate = ldtCasualty;
			lopCC.mobjData.mstrDescription = lstrName;
			lopCC.mobjData.mstrNotes = lstrDesc;
			lopCC.mobjData.midClient = FKClient;
			lopCC.mobjData.mbCaseStudy = false;
			lopCC.mobjData.midManager = FKManager;
			lopCC.Execute(pdb);

			lobjCasualty = Casualty.GetInstance(Engine.getCurrentNameSpace(), lopCC.mobjData.mid);
			lopCSC = new CreateSubCasualty(lobjCasualty.GetProcessID());
			lopCSC.mobjData = new SubCasualtyData();
			lopCSC.mobjData.midPolicy = FKPolicy;
			lopCSC.mobjData.mstrDescription = "Processo Shamir n. " + parrData[Fields.SHAMIRPROCESS].getData();
			lopCSC.mobjData.mstrNotes = "Data participação à Shamir: " + lstrPDate;
			lopCSC.mobjData.mstrGenericObject = lstrName;
			lopCSC.mobjData.midCasualty = lopCC.mobjData.mid;
			lopCSC.mobjData.midManager = FKManager;
			lopCSC.Execute(pdb);
		}
		catch ( Throwable e )
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_1_InternalError, null);
			return;
		}

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

	private boolean FindCasualty(String pstrDesc, SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefObjects;
        ResultSet lrsObjects;
		boolean lbFound;

		lbFound = false;

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
        	lrsObjects = lrefObjects.SelectByMembers(pdb, new int[] {SubCasualty.I.GENERICOBJECT, SubCasualty.I.POLICY},
        			new java.lang.Object[] {"!" + pstrDesc, FKPolicy}, new int[0]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
	        if (lrsObjects.next())
	        	lbFound = true;
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

		return lbFound;
	}

	private String ProcessDate(String pstrDate)
		throws BigBangJewelException
	{
		if ( (pstrDate == null) || (pstrDate.length() != 8) )
			throw new BigBangJewelException("Erro: Formato inválido na data de activação.");

		return pstrDate.substring(0, 4) + "-" + pstrDate.substring(4, 6) + "-" + pstrDate.substring(6, 8);
	}
}
