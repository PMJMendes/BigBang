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
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.ExpenseMapReport;

public class SendNotification
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrExpenseIDs;
	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	private UUID midCompany;
//	private OutgoingMessageData mobjMessage;

	public SendNotification(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_SendNotification;
	}

	public String ShortDesc()
	{
		return "Comunicação à Seguradora";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerado um mapa de notificação à seguradora para esta despesa.");
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
		PrintSetDocument lobjSetCompany;
		PrintSetDetail lobjSetExpense;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			midCompany = (UUID)GetProcess().GetParent().GetData().getAt(2);
		else
			midCompany = (UUID)GetProcess().GetParent().GetParent().GetData().getAt(2);

		try
		{
			if ( mobjDocOps == null )
				generateDocOp();

			if ( mbUseSets )
			{
				if ( midSet == null )
				{
					lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSet.setAt(0, Constants.TID_ExpenseMap);
					lobjSet.setAt(1, new Timestamp(new java.util.Date().getTime()));
					lobjSet.setAt(2, Engine.getCurrentUser());
					lobjSet.setAt(3, (Timestamp)null);
					lobjSet.SaveToDb(pdb);
					midSet = lobjSet.getKey();
				}

				if ( midSetDocument == null )
				{
					lobjSetCompany = PrintSetDocument.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSetCompany.setAt(0, midSet);
					lobjSetCompany.setAt(1, mobjDocOps.marrCreate[0].mobjFile);
					lobjSetCompany.setAt(2, false);
					lobjSetCompany.SaveToDb(pdb);
					midSetDocument = lobjSetCompany.getKey();
				}

				lobjSetExpense = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjSetExpense.setAt(0, midSetDocument);
				lobjSetExpense.setAt(1, null);
				lobjSetExpense.SaveToDb(pdb);
				midSetDetail = lobjSetExpense.getKey();
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
		ExpenseMapReport lrepEM;
		FileXfer lobjFile;
		DocumentData lobjDoc;

		lrepEM = new ExpenseMapReport();
		lrepEM.midCompany = midCompany;
		lrepEM.marrExpenseIDs = marrExpenseIDs;
		lobjFile = lrepEM.Generate();

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Pedido de Reembolso";
		lobjDoc.midOwnerType = Constants.ObjID_Expense;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_ExpenseRequest;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Despesas";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepEM.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Total a Reembolsar";
		lobjDoc.marrInfo[1].mstrValue = lrepEM.mdblTotal.toPlainString();

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
	}
}
