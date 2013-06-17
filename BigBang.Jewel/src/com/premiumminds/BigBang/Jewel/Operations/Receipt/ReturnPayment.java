package com.premiumminds.BigBang.Jewel.Operations.Receipt;

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
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.PaymentReturnReport;

public class ReturnPayment
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrReceiptIDs;
	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	private UUID midClient;

	public ReturnPayment(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ReturnPayment;
	}

	public String ShortDesc()
	{
		return "Devolução de Pagamento";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O pagamento do recibo foi devolvido ao cliente, por falta de entrega da Declaração de Ausência de Sinistros.";
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
		Receipt lobjReceipt;

		lobjReceipt = (Receipt)GetProcess().GetData();

		try
		{
			midClient = lobjReceipt.getClient().getKey();

			if ( mobjDocOps == null )
				generateDocOp();

			if ( mbUseSets )
			{
				if ( midSet == null )
				{
					lobjSet = PrintSet.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjSet.setAt(0, Constants.TID_PaymentReturnLetter);
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
					lobjSetClient.setAt(1, mobjDocOps.marrCreate[0].mobjFile);
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

		try
		{
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Payable);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private void generateDocOp()
		throws BigBangJewelException
	{
		PaymentReturnReport lrepRCL;
		FileXfer lobjFile;
		DocumentData lobjDoc;

		lrepRCL = new PaymentReturnReport();
		lrepRCL.midClient = midClient;
		lrepRCL.marrReceiptIDs = marrReceiptIDs;
		lobjFile = lrepRCL.Generate();

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Devolução de Pagamento";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_PaymentReturnLetter;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Pagamentos Devolvidos";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepRCL.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Total de Pagamentos Devolvidos";
		lobjDoc.marrInfo[1].mstrValue = String.format("%,.2f", lrepRCL.mdblTotal);

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
	}
}
