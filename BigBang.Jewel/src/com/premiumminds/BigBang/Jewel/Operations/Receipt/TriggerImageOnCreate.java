package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class TriggerImageOnCreate
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public transient DSBridgeData mobjImage;
	private UUID midReceipt;
	private DocOps mobjDocOps;

	public TriggerImageOnCreate(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TriggerImageOnCreate;
	}

	public String ShortDesc()
	{
		return "Associação a Imagem";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi automaticamente associado ao documento da imagem digitalizada aquando da introdução.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		DocumentData lobjDoc;
		Receipt lobjReceipt;
		boolean b;
		TriggerAutoValidate lopTAV;

		midReceipt = GetProcess().GetDataKey();

		lobjDoc = new DocumentData();
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
		mobjDocOps.marrCreate = new DocumentData[] {lobjDoc};

		mobjDocOps.RunSubOp(pdb, midReceipt);

		lobjReceipt = (Receipt)GetProcess().GetData();

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
			lopTAV = new TriggerAutoValidate(GetProcess().getKey());
			TriggerOp(lopTAV, pdb);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A associação será retirada. A imagem digitalizada será disponibilizada para outro recibo.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A imagem automaticamente associada aquando da introdução do recibo foi retirada e disponibilizada novamente."; 
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		mobjDocOps.UndoSubOp(pdb, midReceipt);
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
