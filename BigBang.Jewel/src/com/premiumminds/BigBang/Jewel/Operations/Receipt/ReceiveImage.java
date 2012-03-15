package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ReceiveImage
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public transient DSBridgeData mobjImage;
	private UUID midReceipt;
	private DocOps mobjDocOps;

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
		return "Foi criado um documento com a imagem digitalizada do recibo.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		DocumentData lobjDoc;

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
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O documento será apagado. A imagem digitalizada será disponibilizada para outro recibo.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O documento com a imagem digitalizado do recibo foi apagado. A imagem foi disponibilizada para outro recibo.";
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
