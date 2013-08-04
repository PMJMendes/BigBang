package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DSBridgeData;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class ReceiveReception
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public transient DSBridgeData mobjImage;
	public DocOps mobjDocOps;
	private UUID midExpense;

	public ReceiveReception(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_ReceiveReception;
	}

	public String ShortDesc()
	{
		return "Recepção do Comprovativo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();

		lstrBuilder.append("Foi recebida a cópia da carta de notificação à seguradora, devidamente assinada e carimbada.");
		lstrBuilder.append(pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		midExpense = GetProcess().GetDataKey();

		if ( (mobjDocOps == null) && (mobjImage != null) )
			GenerateDocOps();

		if ( mobjDocOps != null )
			mobjDocOps.RunSubOp(pdb, midExpense);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();

		lstrBuilder.append("A anotação de recepção será retirada.");
		lstrBuilder.append(pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder();

		lstrBuilder.append("A anotação de recepção do comprovativo foi retirada.");
		lstrBuilder.append(pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.UndoLongDesc(lstrBuilder, pstrLineBreak);

		return lstrBuilder.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{

		if ( mobjDocOps != null )
			mobjDocOps.UndoSubOp(pdb, midExpense);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}

	private void GenerateDocOps()
	{
		DocDataLight lobjDoc;

		lobjDoc = new DocDataLight();
		lobjDoc.mstrName = "Comprovativo de Recepção";
		lobjDoc.midOwnerType = Constants.ObjID_Expense;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_HealthExpenseDoc;
		lobjDoc.mstrText = null;
		lobjDoc.mobjDSBridge = new DSBridgeData();
		lobjDoc.mobjDSBridge.mstrDSHandle = mobjImage.mstrDSHandle;
		lobjDoc.mobjDSBridge.mstrDSLoc = mobjImage.mstrDSLoc;
		lobjDoc.mobjDSBridge.mstrDSTitle = null;
		lobjDoc.mobjDSBridge.mbDelete = true;

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate2 = new DocDataLight[] {lobjDoc};
	}
}
