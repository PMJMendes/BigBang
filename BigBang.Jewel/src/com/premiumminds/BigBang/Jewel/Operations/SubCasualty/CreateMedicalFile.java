package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MedicalFileData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.MedicalDetail;
import com.premiumminds.BigBang.Jewel.Objects.MedicalFile;

public class CreateMedicalFile
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public MedicalFileData mobjData;

	public CreateMedicalFile(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_CreateMedicalFile;
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Ficha Clínica";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criado o seguinte processo:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		MedicalFile lobjAux;
		MedicalDetail lobjDetail;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;
		int i;

		if ( mobjData.midManager == null )
			mobjData.midManager = Engine.getCurrentUser();

		try
		{
			mobjData.mstrReference = GetNewProcessNumber();
			mobjData.midSubCasualty = GetProcess().GetDataKey();

			lobjAux = MedicalFile.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);
			mobjData.mid = lobjAux.getKey();

			if ( mobjData.marrDetails != null )
			{
				for ( i = 0; i < mobjData.marrDetails.length; i++ )
				{
					if ( mobjData.marrDetails[i] == null )
						continue;

					mobjData.marrDetails[i].midFile = mobjData.mid;

					lobjDetail = MedicalDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrDetails[i].ToObject(lobjDetail);
					lobjDetail.SaveToDb(pdb);
				}
			}

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_MedicalFile);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), GetProcess().getKey(), GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.midProcess = lobjProc.getKey();
			mobjData.midManager = lobjProc.GetManagerID();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			if ( mobjData.mdtNextDate != null )
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Revisão de Processo de Baixa ou Internamento");
				lobjItem.setAt(1, GetProcess().GetManagerID());
				lobjItem.setAt(2, Constants.ProcID_MedicalFile);
				lobjItem.setAt(3, new Timestamp(new java.util.Date().getTime()));
				lobjItem.setAt(4, mobjData.mdtNextDate);
				lobjItem.setAt(5, Constants.UrgID_Pending);
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_MedicalFile_ManageData}, pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private String GetNewProcessNumber()
		throws BigBangJewelException
	{
		String lstrFilter;
		IEntity lrefCasualties;
        MasterDB ldb;
        ResultSet lrsCasualties;
        int llngResult;
        String lstrAux;
        int llngAux;

		try
		{
	        lstrFilter = GetProcess().GetData().getLabel() + ".FC%";
			lrefCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MedicalFile)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCasualties = lrefCasualties.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {lstrFilter},
					new int[] {Integer.MIN_VALUE});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			while ( lrsCasualties.next() )
			{
				lstrAux = lrsCasualties.getString(2).substring(lstrFilter.length() - 1);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsCasualties.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCasualties.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
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

		return lstrFilter.substring(0, lstrFilter.length() - 1) + llngResult;
	}
}
