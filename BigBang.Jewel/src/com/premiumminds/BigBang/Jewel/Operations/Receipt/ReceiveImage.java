package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ReceiveImage
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public ReceiptData mobjData;
	public transient DSBridgeData mobjImage;
	private UUID midReceipt;
	public DocOps mobjDocOps;
	private boolean mbWithAgenda;

	public ReceiveImage(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ReceiveImage;
	}

	public String ShortDesc()
	{
		return "Associação a Imagem";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("Foi criado um documento com a imagem digitalizada do recibo.");

		if ( mobjData != null )
		{
			lstrResult.append(pstrLineBreak);
			lstrResult.append("Novos dados do recibo:");
			lstrResult.append(pstrLineBreak);
			mobjData.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		DocDataLight lobjDoc;
		IProcess lobjProc;
		Receipt lobjReceipt;
		AgendaItem lobjItem;
		Timestamp ldtAux;
		Calendar ldtAux2;
		boolean b;
		TriggerAutoValidate lopTAV;

		mbWithAgenda = false;

		lobjProc = GetProcess();
		midReceipt = lobjProc.GetDataKey();
		lobjReceipt = (Receipt)lobjProc.GetData();

		if ( mobjData != null )
		{
			mobjData.mobjPrevValues = new ReceiptData();
			mobjData.mobjPrevValues.FromObject(lobjReceipt);

			mobjData.midManager = GetProcess().GetManagerID();
			mobjData.mbInternal = mobjData.mobjPrevValues.mbInternal;
			mobjData.mlngEntryNumber = mobjData.mobjPrevValues.mlngEntryNumber;
			mobjData.mlngEntryYear = mobjData.mobjPrevValues.mlngEntryYear;
			mobjData.midStatus = mobjData.mobjPrevValues.midStatus;
			mobjData.midPolicy = mobjData.mobjPrevValues.midPolicy;
			mobjData.midSubPolicy = mobjData.mobjPrevValues.midSubPolicy;
			mobjData.midSubCasualty = mobjData.mobjPrevValues.midSubCasualty;

			try
			{
				mobjData.ToObject(lobjReceipt);
				lobjReceipt.SaveToDb(pdb);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		lobjDoc = new DocDataLight();
		lobjDoc.mstrName = "Original";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_ReceiptScan;
		lobjDoc.mstrText = null;
		lobjDoc.mobjDSBridge = new DSBridgeData();
		lobjDoc.mobjDSBridge.mstrDSHandle = mobjImage.mstrDSHandle;
		lobjDoc.mobjDSBridge.mstrDSLoc = mobjImage.mstrDSLoc;
		lobjDoc.mobjDSBridge.mstrDSTitle = null;
		lobjDoc.mobjDSBridge.mbDelete = true;

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate2 = new DocDataLight[] {lobjDoc};

		mobjDocOps.RunSubOp(pdb, midReceipt);

		try
		{
			b = lobjReceipt.canAutoValidate();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( b )
		{
			lopTAV = new TriggerAutoValidate(lobjProc.getKey());
			TriggerOp(lopTAV, pdb);
		}
		else
		{
	    	try
	    	{
				if ( !Constants.ProfID_Simple.equals(lobjReceipt.getProfile()) )
				{
					ldtAux = new Timestamp(new java.util.Date().getTime());
			    	ldtAux2 = Calendar.getInstance();
			    	ldtAux2.setTimeInMillis(ldtAux.getTime());
			    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

					lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjItem.setAt(0, "Validação de Recibo");
					lobjItem.setAt(1, lobjProc.GetManagerID());
					lobjItem.setAt(2, Constants.ProcID_Receipt);
					lobjItem.setAt(3, ldtAux);
					lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
					lobjItem.setAt(5, Constants.UrgID_Pending);
					lobjItem.SaveToDb(pdb);
					lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_Receipt_ValidateReceipt,
							Constants.OPID_Receipt_SetReturnToInsurer}, pdb);

					mbWithAgenda = true;
		    	}
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("O documento será apagado. A imagem digitalizada será disponibilizada para outro recibo.");

		if ( mobjData != null )
		{
			lstrResult.append(pstrLineBreak);
			lstrResult.append("Os dados anteriores serão repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();

		lstrResult.append("O documento com a imagem digitalizada do recibo foi apagado. A imagem foi disponibilizada para outro recibo.");

		if ( mobjData != null )
		{
			lstrResult.append(pstrLineBreak);
			lstrResult.append("Os dados anteriores foram repostos:");
			lstrResult.append(pstrLineBreak);
			mobjData.mobjPrevValues.Describe(lstrResult, pstrLineBreak);
		}

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		Receipt lobjAux;

		if ( mbWithAgenda )
		{
			larrItems = new HashMap<UUID, AgendaItem>();
			lrs = null;
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
				lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
				while ( lrs.next() )
				{
					lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
					larrItems.put((UUID)lobjAgendaProc.getAt(0),
							AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
				}
				lrs.close();
				lrs = null;

				for ( AgendaItem lobjItem: larrItems.values() )
				{
					lobjItem.ClearData(pdb);
					lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
				}
			}
			catch (Throwable e)
			{
				if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		mobjDocOps.UndoSubOp(pdb, midReceipt);

		if ( mobjData != null )
		{
			try
			{
				lobjAux = Receipt.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);

				mobjData.mobjPrevValues.midPolicy = (UUID)lobjAux.getAt(Receipt.I.POLICY);
				mobjData.mobjPrevValues.midSubPolicy = (UUID)lobjAux.getAt(Receipt.I.SUBPOLICY);
				mobjData.mobjPrevValues.midSubCasualty = (UUID)lobjAux.getAt(Receipt.I.SUBCASUALTY);

				mobjData.mobjPrevValues.ToObject(lobjAux);
				lobjAux.SaveToDb(pdb);
	    	}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Receipt;
		lobjSet.marrChanged = new UUID[] {midReceipt};

		return new UndoSet[] {lobjSet}; 
	}
}
