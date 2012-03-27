package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.PaymentNoticeClient;
import com.premiumminds.BigBang.Jewel.Objects.PaymentNoticeReceipt;
import com.premiumminds.BigBang.Jewel.Objects.PaymentNoticeSet;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.PaymentNoticeReport;

public class CreatePaymentNotice
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrReceiptIDs;
	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetClient;
	public UUID midSetReceipt;
	public DocOps mobjDocOps;
	public OutgoingMessageData mobjMessage;

	public CreatePaymentNotice(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CreatePaymentNotice;
	}

	public String ShortDesc()
	{
		return "Envio de Aviso de Cobrança";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		UUID lidClient;
		PaymentNoticeReport lrepPN;
		DocumentData lobjDoc;
		PaymentNoticeSet lobjSet;
		PaymentNoticeClient lobjSetClient;
		PaymentNoticeReceipt lobjSetReceipt;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			lidClient = GetProcess().GetParent().GetParent().GetDataKey();
		else
			lidClient = (UUID)GetProcess().GetParent().GetData().getAt(2);

		try
		{
			if ( mobjDocOps == null )
			{
				lrepPN = new PaymentNoticeReport();
				lrepPN.midClient = lidClient;
				lrepPN.marrReceiptIDs = marrReceiptIDs;

				lobjDoc = new DocumentData();
				lobjDoc.mstrName = "Aviso de Cobrança";
				lobjDoc.midOwnerType = Constants.ObjID_Receipt;
				lobjDoc.midOwnerId = null;
				lobjDoc.midDocType = Constants.DocID_PaymentNotice;
				lobjDoc.mstrText = null;
				lobjDoc.mobjFile = lrepPN.Generate().GetVarData();
				lobjDoc.marrInfo = new DocInfoData[0];

				mobjDocOps = new DocOps();
				mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
			}

			if ( mbUseSets )
			{
				if ( midSet == null )
				{
					lobjSet = PaymentNoticeSet.GetInstance(Engine.getCurrentNameSpace(), null);
					lobjSet.setAt(0, new Timestamp(new java.util.Date().getTime()));
					lobjSet.setAt(1, Engine.getCurrentUser());
					lobjSet.setAt(2, (Timestamp)null);
					lobjSet.SaveToDb(pdb);
					midSet = lobjSet.getKey();
				}

				if ( midSetClient == null )
				{
					lobjSetClient = PaymentNoticeClient.GetInstance(Engine.getCurrentNameSpace(), null);
					lobjSetClient.setAt(0, midSet);
					lobjSetClient.setAt(1, lidClient);
					lobjSetClient.setAt(2, mobjDocOps.marrCreate[0].mobjFile);
					lobjSetClient.setAt(3, false);
					lobjSetClient.SaveToDb(pdb);
					midSetClient = lobjSetClient.getKey();
				}

				lobjSetReceipt = PaymentNoticeReceipt.GetInstance(Engine.getCurrentNameSpace(), null);
				lobjSetReceipt.setAt(0, midSetClient);
				lobjSetReceipt.setAt(1, GetProcess().GetDataKey());
				lobjSetReceipt.setAt(2, null);
				lobjSetReceipt.SaveToDb(pdb);
				midSetReceipt = lobjSetReceipt.getKey();
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());
	}
}
