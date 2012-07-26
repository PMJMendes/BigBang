package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.PrintSet;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDetail;
import com.premiumminds.BigBang.Jewel.Objects.PrintSetDocument;
import com.premiumminds.BigBang.Jewel.Objects.SignatureRequest;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.SignatureRequestReport;

public class CreateSignatureRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID[] marrReceiptIDs;
	public int mlngDays;
	public boolean mbUseSets;
	public UUID midSet;
	public UUID midSetDocument;
	public UUID midSetDetail;
	public DocOps mobjDocOps;
	public UUID midRequestObject;
	private UUID midClient;
	private UUID midExternProcess;
//	private OutgoingMessageData mobjMessage;

	public CreateSignatureRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CreateSignatureRequest;
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Pedido de Assinatura de Recibo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi gerada uma carta de pedido de assinatura para este recibo.");
		lstrBuilder.append(pstrLineBreak).append(pstrLineBreak);

		mobjDocOps.LongDesc(lstrBuilder, pstrLineBreak);

		lstrBuilder.append(pstrLineBreak).append("Prazo limite de resposta: ").append(mlngDays).append(" dias.").append(pstrLineBreak);

		return lstrBuilder.toString();
	}

	public UUID GetExternalProcess()
	{
		return midExternProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		Calendar ldtAux;
		Timestamp ldtLimit;
		PrintSet lobjSet;
		PrintSetDocument lobjSetClient;
		PrintSetDetail lobjSetReceipt;
        SignatureRequest lobjRequest;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, mlngDays);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

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
					lobjSet.setAt(0, Constants.TID_SignatureRequest);
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

				lobjSetReceipt = PrintSetDetail.GetInstance(Engine.getCurrentNameSpace(), null);
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
			lobjRequest = SignatureRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjRequest.setAt(1, ldtLimit);
			lobjRequest.SaveToDb(pdb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_SignatureRequest);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjRequest.getKey(), GetProcess().getKey(),
					GetContext(), pdb);

			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, "Pedido de Assinatura de Recibos");
			lobjItem.setAt(1, Engine.getCurrentUser());
			lobjItem.setAt(2, Constants.ProcID_SignatureRequest);
			lobjItem.setAt(3, ldtNow);
			lobjItem.setAt(4, ldtLimit);
			lobjItem.setAt(5, Constants.UrgID_Valid);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_SigReq_ReceiveReply,
					Constants.OPID_SigReq_RepeatRequest, Constants.OPID_SigReq_CancelRequest}, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		midRequestObject = lobjRequest.getKey();
		midExternProcess = lobjProc.getKey();
	}

	private void generateDocOp()
		throws BigBangJewelException
	{
		SignatureRequestReport lrepSR;
		FileXfer lobjFile;
		DocumentData lobjDoc;

		lrepSR = new SignatureRequestReport();
		lrepSR.midClient = midClient;
		lrepSR.marrReceiptIDs = marrReceiptIDs;
		lobjFile = lrepSR.Generate();

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Pedido de Assinatura";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_SignatureRequestLetter;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Recibos";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepSR.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Total de Prémios";
		lobjDoc.marrInfo[1].mstrValue = lrepSR.mdblTotal.toPlainString();

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate = new DocumentData[]{lobjDoc};
	}
}
