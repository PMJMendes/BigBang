package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.math.BigDecimal;
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
	private UUID midClient;
	private DocOps mobjDocOps;
//	private OutgoingMessageData mobjMessage;
	private transient BigDecimal mdblTotal;
	private transient int mlngCount;

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
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerado um aviso de cobrança para este recibo.");
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
		PaymentNoticeSet lobjSet;
		PaymentNoticeClient lobjSetClient;
		PaymentNoticeReceipt lobjSetReceipt;
		byte[] lobjFileData;
		DocumentData lobjDoc;
		DocInfoData[] larrInfo;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
			midClient = GetProcess().GetParent().GetParent().GetDataKey();
		else
			midClient = (UUID)GetProcess().GetParent().GetData().getAt(2);

		larrInfo = new DocInfoData[2];
		larrInfo[0] = new DocInfoData();
		larrInfo[0].mstrType = "Número de Recibos";
		larrInfo[1] = new DocInfoData();
		larrInfo[1].mstrType = "Total a Liquidar";

		try
		{
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
					lobjFileData = generateReport();

					larrInfo[0].mstrValue = Integer.toString(mlngCount);
					larrInfo[1].mstrValue = mdblTotal.toPlainString();

					lobjSetClient = PaymentNoticeClient.GetInstance(Engine.getCurrentNameSpace(), null);
					lobjSetClient.setAt(0, midSet);
					lobjSetClient.setAt(1, midClient);
					lobjSetClient.setAt(2, lobjFileData);
					lobjSetClient.setAt(3, false);
					lobjSetClient.setAt(4, mlngCount);
					lobjSetClient.setAt(5, mdblTotal);
					lobjSetClient.SaveToDb(pdb);
					midSetClient = lobjSetClient.getKey();
				}
				else
				{
					lobjSetClient = PaymentNoticeClient.GetInstance(Engine.getCurrentNameSpace(), midSetClient);

					lobjFileData = (byte[])lobjSetClient.getAt(2);

					larrInfo[0].mstrValue = Integer.toString(((Integer)lobjSetClient.getAt(4)));
					larrInfo[1].mstrValue = ((BigDecimal)lobjSetClient.getAt(5)).toPlainString();
				}

				lobjSetReceipt = PaymentNoticeReceipt.GetInstance(Engine.getCurrentNameSpace(), null);
				lobjSetReceipt.setAt(0, midSetClient);
				lobjSetReceipt.setAt(1, GetProcess().GetDataKey());
				lobjSetReceipt.setAt(2, null);
				lobjSetReceipt.SaveToDb(pdb);
				midSetReceipt = lobjSetReceipt.getKey();
			}
			else
			{
				lobjFileData = generateReport();

				larrInfo[0].mstrValue = Integer.toString(mlngCount);
				larrInfo[1].mstrValue = mdblTotal.toPlainString();
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Aviso de Cobrança";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_PaymentNotice;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFileData;
		lobjDoc.marrInfo = larrInfo;

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};

		mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());
	}

	private byte[] generateReport()
		throws BigBangJewelException
	{
		PaymentNoticeReport lrepPN;
		FileXfer lobjResult;

		lrepPN = new PaymentNoticeReport();
		lrepPN.midClient = midClient;
		lrepPN.marrReceiptIDs = marrReceiptIDs;

		lobjResult = lrepPN.Generate();
		mdblTotal = lrepPN.mdblTotal;
		mlngCount = lrepPN.mlngCount;

		return lobjResult.GetVarData();
	}
}
