package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.premiumminds.BigBang.Jewel.Data.SubCasualtyData;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingEntity;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingHeadings;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyInsurerRequest;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class CreateSubCasualty
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public SubCasualtyData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_CreateSubCasualty;
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Sinistro";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criado o seguinte sub-sinistro:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		SubCasualty lobjAux;
		IScript lobjScript;
		IProcess lobjProc;
		SubCasualtyItem lobjItem;
		SubCasualtyInsurerRequest request;
		SubCasualtyFraming framing;
		SubCasualtyFramingEntity framingEntity;
		SubCasualtyFramingHeadings framingHeadings;
		int i;

		if ( mobjData.midManager == null )
			mobjData.midManager = GetProcess().GetManagerID();

		try
		{
			mobjData.mstrNumber = GetNewSubCasualtyNumber();

			lobjAux = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_SubCasualty);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), GetProcess().getKey(), GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.mid = lobjAux.getKey();
			mobjData.midProcess = lobjProc.getKey();
			mobjData.midManager = lobjProc.GetManagerID();
			mobjData.mobjPrevValues = null;

			if ( mobjData.marrItems != null )
			{
				for ( i = 0; i < mobjData.marrItems.length; i++ )
				{
					if ( mobjData.marrItems[i].mbNew )
					{
						lobjItem = SubCasualtyItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
						mobjData.marrItems[i].midSubCasualty = mobjData.mid;
						mobjData.marrItems[i].ToObject(lobjItem);
						lobjItem.SaveToDb(pdb);
						mobjData.marrItems[i].mid = lobjItem.getKey();
					}
				}
			}
			
			// Insurer requests
			if (mobjData.requests != null) {
				for (i=0; i<mobjData.requests.length; i++) {
					if (mobjData.requests[i].isNew) {
						request = SubCasualtyInsurerRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
						mobjData.requests[i].subCasualtyId = mobjData.mid;
						mobjData.requests[i].ToObject(request);
						request.SaveToDb(pdb);
						mobjData.requests[i].id = request.getKey();
					}
				}
			}
			
			// Framing
			if (mobjData.framing != null) {
				if (mobjData.framing.isNew) {
					framing = SubCasualtyFraming.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.framing.subCasualtyId = mobjData.mid;
					mobjData.framing.ToObject(framing);
					framing.SaveToDb(pdb);
					mobjData.framing.id = framing.getKey();
					
					// Framing Entities
					if (mobjData.framing.framingEntities != null) {
						for (i=0; i<mobjData.framing.framingEntities.length; i++) {
							if (mobjData.framing.framingEntities[i].isNew) {
								framingEntity = SubCasualtyFramingEntity.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
								mobjData.framing.framingEntities[i].framingId = mobjData.framing.id;
								mobjData.framing.framingEntities[i].ToObject(framingEntity);
								framingEntity.SaveToDb(pdb);
								mobjData.framing.framingEntities[i].id = framingEntity.getKey();
							}
						}
					}
					
					// Framing Headings
					if (mobjData.framing.framingHeadings != null) {
						if (mobjData.framing.framingHeadings.isNew) {
							framingHeadings = SubCasualtyFramingHeadings.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.framing.framingHeadings.framingId = mobjData.framing.id;
							mobjData.framing.framingHeadings.ToObject(framingHeadings);
							framingHeadings.SaveToDb(pdb);
							mobjData.framing.framingHeadings.id = framingHeadings.getKey();
						}
					}
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private String GetNewSubCasualtyNumber()
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
	        lstrFilter = GetProcess().GetData().getLabel() + ".%";
			lrefCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty)); 
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
