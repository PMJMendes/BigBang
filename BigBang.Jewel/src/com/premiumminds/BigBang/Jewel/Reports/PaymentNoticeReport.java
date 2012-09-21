package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class PaymentNoticeReport
	extends ReportBase
{
	public UUID midClient;
	public UUID[] marrReceiptIDs;
	public BigDecimal mdblTotal;
	public int mlngCount;

	protected UUID GetTemplateID()
	{
		return Constants.TID_PaymentNotice;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Client lobjClient;
		ObjectBase lobjZipCode;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Receipt lobjReceipt;
		IProcess lobjProc;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		boolean lbReversals;
		int i;

		lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);
		if ( lobjClient.getAt(4) == null )
			lobjZipCode = null;
		else
		{
			try
			{
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjClient.getAt(4));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
		ldtAux = new Timestamp(new java.util.Date().getTime());

		larrParams = new HashMap<String, String>();
		larrParams.put("ClientName", (String)lobjClient.getAt(0));
		larrParams.put("ClientAddress1", (lobjClient.getAt(2) == null ? "" : (String)lobjClient.getAt(2)));
		larrParams.put("ClientAddress2", (lobjClient.getAt(3) == null ? "" : (String)lobjClient.getAt(3)));
		larrParams.put("ClientZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("ClientZipLocal", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", ldtAux.toString().substring(0, 10));

		larrTables = new String[marrReceiptIDs.length][];
		mlngCount = 0;
		mdblTotal = new BigDecimal(0);
		lbReversals = true;
		for ( i = 0; i < larrTables.length; i++ )
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), marrReceiptIDs[i]);
			try
			{
				lobjProc = PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjReceipt.GetProcessID());
				if ( Constants.ProcID_Policy.equals(lobjProc.GetParent().GetScriptID()) )
				{
					lobjPolicy = (Policy)lobjProc.GetParent().GetData();
					lobjSubPolicy = null;
				}
				else
				{
					lobjPolicy = (Policy)lobjProc.GetParent().GetParent().GetData();
					lobjSubPolicy = (SubPolicy)lobjProc.GetParent().GetData();
				}
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( lbReversals && !Constants.RecType_Reversal.equals((UUID)lobjReceipt.getAt(Receipt.I.TYPE)) )
				lbReversals = false;

			larrTables[i] = new String[9];
			larrTables[i][0] = (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel());
			larrTables[i][1] = lobjReceipt.getLabel();
			larrTables[i][2] = lobjPolicy.GetSubLine().getDescription();
			larrTables[i][3] = (String)lobjPolicy.GetCompany().getAt(1);
			larrTables[i][4] = (lobjReceipt.getAt(9) == null ? "" : ((Timestamp)lobjReceipt.getAt(9)).toString().substring(0, 10));
			larrTables[i][5] = (lobjReceipt.getAt(10) == null ? "" : ((Timestamp)lobjReceipt.getAt(10)).toString().substring(0, 10));
			larrTables[i][6] = String.format("%,.2f", (BigDecimal)lobjReceipt.getAt(3));
			larrTables[i][7] = (lobjReceipt.getAt(11) == null ? "" : ((Timestamp)lobjReceipt.getAt(11)).toString().substring(0, 10));
			larrTables[i][8] = (lobjReceipt.getAt(14) == null ? "" : (((String)lobjReceipt.getAt(14)).length() > 20 ?
					((String)lobjReceipt.getAt(14)).substring(0, 20) : (String)lobjReceipt.getAt(14)));

			mlngCount++;
			mdblTotal = mdblTotal.add((BigDecimal)lobjReceipt.getAt(3));
		}

		if ( lbReversals )
		{
			larrParams.put("RecType", "recobro/reembolso");
			larrParams.put("EndText", "Segue em anexo uma cópia da participação.");
		}
		else
		{
			larrParams.put("RecType", "prémio");
			larrParams.put("EndText", "Por determinação legal, DL nº 72/2008, de 16 de Abril, o não cumprimento dos prazos estipulados " +
					"obrigará à devolução dos recibos de prémio à seguradora, para actuação da parte desta de acordo com as condições " +
					"gerais das apólices e nos termos do referido decreto lei.");
		}

		larrParams.put("Count", "" + mlngCount + " recibo" + (mlngCount == 1 ? "" : "s"));
		larrParams.put("Total", String.format("%,.2f", mdblTotal));

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
