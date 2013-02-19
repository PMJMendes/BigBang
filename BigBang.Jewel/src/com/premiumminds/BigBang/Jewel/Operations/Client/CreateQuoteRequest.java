package com.premiumminds.BigBang.Jewel.Operations.Client;

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
import com.premiumminds.BigBang.Jewel.Data.QuoteRequestData;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestCoverage;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestSubLine;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestValue;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class CreateQuoteRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public QuoteRequestData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateQuoteRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_CreateQuoteRequest;
	}

	public String ShortDesc()
	{
		return "Criação de Consulta de Mercado";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;
		int i, j;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criada a seguinte consulta de mercado:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjData.marrObjects != null )
		{
			for ( i = 0; i < mobjData.marrObjects.length; i++ )
			{
				if ( mobjData.marrObjects[i] == null )
					continue;

				lstrResult.append("Unidade de risco:").append(pstrLineBreak);
				mobjData.marrObjects[i].Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);
			}
		}

		if ( mobjData.marrSubLines != null )
		{
			for ( i = 0; i < mobjData.marrSubLines.length; i++ )
			{
				if ( mobjData.marrSubLines[i] == null )
					continue;

				lstrResult.append("Modalidade:").append(pstrLineBreak);
				mobjData.marrSubLines[i].Describe(lstrResult, pstrLineBreak);
				lstrResult.append(pstrLineBreak);

				if ( mobjData.marrSubLines[i].marrCoverages != null )
				{
					for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
					{
						lstrResult.append("Cobertura:").append(pstrLineBreak);
						mobjData.marrSubLines[i].marrCoverages[j].Describe(lstrResult, pstrLineBreak);
						lstrResult.append(pstrLineBreak);
					}
				}

				if ( mobjData.marrSubLines[i].marrValues != null )
				{
					for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
					{
						mobjData.marrSubLines[i].marrValues[j].Describe(lstrResult, pstrLineBreak);
					}
				}
			}
		}

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
		QuoteRequest lobjQuoteReq;
		QuoteRequestSubLine lobjQRSubLine;
		QuoteRequestCoverage lobjCoverage;
		QuoteRequestObject lobjObject;
		QuoteRequestValue lobjValue;
		IScript lobjScript;
		IProcess lobjProc;
		int i, j;

		try
		{
			if ( mobjData.mstrNumber == null )
				mobjData.mstrNumber = GetNewProcessNumber();
			if ( mobjData.midManager == null )
				mobjData.midManager = Engine.getCurrentUser();
			if ( mobjData.midMediator == null )
				mobjData.midMediator = (UUID)GetProcess().GetData().getAt(8);

			lobjQuoteReq = QuoteRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjQuoteReq);
			lobjQuoteReq.SaveToDb(pdb);
			mobjData.mid = lobjQuoteReq.getKey();

			if ( mobjData.marrObjects != null )
			{
				for ( i = 0; i < mobjData.marrObjects.length; i++ )
				{
					if ( mobjData.marrObjects[i] == null )
						continue;

					mobjData.marrObjects[i].midOwner = mobjData.mid;
					lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrObjects[i].ToObject(lobjObject);
					lobjObject.SaveToDb(pdb);
					mobjData.marrObjects[i].mid = lobjObject.getKey();
				}
			}

			if ( mobjData.marrSubLines != null )
			{
				for ( i = 0; i < mobjData.marrSubLines.length; i++ )
				{
					if ( mobjData.marrSubLines[i] == null )
						continue;

					mobjData.marrSubLines[i].midQuoteRequest = mobjData.mid;
					lobjQRSubLine = QuoteRequestSubLine.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrSubLines[i].ToObject(lobjQRSubLine);
					lobjQRSubLine.SaveToDb(pdb);
					mobjData.marrSubLines[i].mid = lobjQRSubLine.getKey();

					if ( mobjData.marrSubLines[i].marrCoverages != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrCoverages.length; j++ )
						{
							mobjData.marrSubLines[i].marrCoverages[j].midQRSubLine = mobjData.marrSubLines[i].mid;
							lobjCoverage = QuoteRequestCoverage.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrSubLines[i].marrCoverages[j].ToObject(lobjCoverage);
							lobjCoverage.SaveToDb(pdb);
							mobjData.marrSubLines[i].marrCoverages[j].mid = lobjCoverage.getKey();
						}
					}

					if ( mobjData.marrSubLines[i].marrValues != null )
					{
						for ( j = 0; j < mobjData.marrSubLines[i].marrValues.length; j++ )
						{
							mobjData.marrSubLines[i].marrValues[j].midQRSubLine = mobjData.marrSubLines[i].mid;
							mobjData.marrSubLines[i].marrValues[j].midObject = ( mobjData.marrSubLines[i].marrValues[j].mlngObject < 0 ?
									null : mobjData.marrObjects[mobjData.marrSubLines[i].marrValues[j].mlngObject].mid );
							lobjValue = QuoteRequestValue.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrSubLines[i].marrValues[j].ToObject(lobjValue);
							lobjValue.SaveToDb(pdb);
							mobjData.marrSubLines[i].marrValues[j].mid = lobjValue.getKey();
						}
					}
				}
			}

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjQuoteReq.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjQuoteReq.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_QuoteRequest);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjQuoteReq.getKey(), GetProcess().getKey(),
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;
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
		IEntity lrefQuoteReqs;
        MasterDB ldb;
        ResultSet lrsQuoteReqs;
        int llngResult;
        String lstrAux;
        int llngAux;

		try
		{
	        lstrFilter = "+" + ((Integer)GetProcess().GetData().getAt(1)).toString() + ".%";
			lrefQuoteReqs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_QuoteRequest)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsQuoteReqs = lrefQuoteReqs.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {lstrFilter},
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
			while ( lrsQuoteReqs.next() )
			{
				lstrAux = lrsQuoteReqs.getString(2).substring(lstrFilter.length() - 1);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsQuoteReqs.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsQuoteReqs.close();
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
