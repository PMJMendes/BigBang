package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Reports.HealthReceiptReport;

public class CreateInternalReceipt
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public DocOps mobjDocOps;

	public CreateInternalReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CreateInternalReceipt;
	}

	public String ShortDesc()
	{
		return "Emissão de Recibo Interno";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuilder;

		lstrBuilder = new StringBuilder("Foi emitido um recibo para esta nota de débito.");
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
		try
		{
			if ( mobjDocOps == null )
				generateDocOp();
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
		HealthReceiptReport lrepHR;
		FileXfer lobjFile;
		DocDataLight lobjDoc;

		lrepHR = new HealthReceiptReport();
		try
		{
			lrepHR.midReceipt = GetProcess().GetDataKey();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		lobjFile = lrepHR.Generate();

		lobjDoc = new DocDataLight();
		lobjDoc.mstrName = "Recibo de Saúde";
		lobjDoc.midOwnerType = Constants.ObjID_Receipt;
		lobjDoc.midOwnerId = null;
		lobjDoc.midDocType = Constants.DocID_DebitNoteReceipt;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[2];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número";
		lobjDoc.marrInfo[0].mstrValue = lrepHR.mstrNumber;
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Valor";
		lobjDoc.marrInfo[1].mstrValue = lrepHR.mstrValue;

		mobjDocOps = new DocOps();
		mobjDocOps.marrCreate2 = new DocDataLight[]{lobjDoc};
	}
}
