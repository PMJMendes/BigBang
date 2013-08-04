package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.ExpenseReturnReport;

public class ReturnToClient
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrExpenseIDs;
	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	private UUID midClient;
	//private OutgoingMessageData mobjMessage;

	public ReturnToClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_ReturnToClient;
	}

	public String ShortDesc()
	{
		return "Devolução ao Segurado";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerada uma carta de devolução ao segurado para esta despesa.");
		lstrBuilder.append(pstrLineBreak).append(pstrLineBreak);

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
		PrintSet lobjSet;
		PrintSetDocument lobjSetClient;
		PrintSetDetail lobjSetReceipt;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			midClient = GetProcess().GetParent().GetParent().GetDataKey();
		else
			midClient = (UUID)GetProcess().GetParent().GetData().getAt(2);

		try
		{
			if ( mobjDocOps == null )
				generateDocOp();

			if ( mbUseSets )
			{
				if ( midSet == null )
				{
					lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSet.setAt(0, Constants.TID_ExpenseReturn);
					lobjSet.setAt(1, new Timestamp(new java.util.Date().getTime()));
					lobjSet.setAt(2, Engine.getCurrentUser());
					lobjSet.setAt(3, (Timestamp)null);
					lobjSet.SaveToDb(pdb);
					midSet = lobjSet.getKey();
				}

				if ( midSetDocument == null )
				{
					lobjSetClient = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSetClient.setAt(0, midSet);
					lobjSetClient.setAt(1, mobjDocOps.marrCreate2[0].mobjFile);
					lobjSetClient.setAt(2, false);
					lobjSetClient.SaveToDb(pdb);
					midSetDocument = lobjSetClient.getKey();
				}

				lobjSetReceipt = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetReceipt.setAt(0, midSetDocument);
				lobjSetReceipt.setAt(1, null);
				lobjSetReceipt.SaveToDb(pdb);
				midSetDetail = lobjSetReceipt.getKey();
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());
	}

	private void generateDocOp()
		throws BigBangJewelException
	{
		ExpenseReturnReport lrepER;
		FileXfer lobjFile;
		DocDataLight lobjDoc;

		lrepER = new ExpenseReturnReport();
		lrepER.midClient = midClient;
		lrepER.marrExpenseIDs = marrExpenseIDs;
		lobjFile = lrepER.Generate();

		lobjDoc = new DocDataLight();
		lobjDoc.mstrName = "Devolução de Despesas";
		lobjDoc.midOwnerType = Constants.ObjID_Expense;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_ExpenseReturn;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Despesas";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepER.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Valor Total";
		lobjDoc.marrInfo[1].mstrValue = String.format("%,.2f", lrepER.mdblTotal);

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate2 = new DocDataLight[]{lobjDoc};
	}
}
