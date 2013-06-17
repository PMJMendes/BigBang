package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.ILog;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PaymentData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class PaymentCoverLetterReport
	extends ReportBase
{
	public UUID midClient;
	public UUID[] marrReceiptIDs;
	public BigDecimal mdblTotal;
	public int mlngCount;

	protected UUID GetTemplateID()
	{
		return Constants.TID_PaymentCoverLetter;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Client lobjClient;
		ObjectBase lobjZipCode, lobjMode, lobjBank;
		Timestamp ldtAux;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Receipt lobjReceipt;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		ILog lobjLog;
		Payment lopP;
		PaymentData[] larrData;
		PaymentData lobjData;
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
		mdblTotal = BigDecimal.ZERO;
		for ( i = 0; i < larrTables.length; i++ )
		{
			lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), marrReceiptIDs[i]);
			lobjPolicy = lobjReceipt.getAbsolutePolicy();
			lobjSubPolicy = lobjReceipt.getSubPolicy();
			lobjLog = lobjReceipt.getPaymentLog();
			lobjData = null;
			lobjMode = null;
			lobjBank = null;
			try
			{
				lopP = (lobjLog == null ? null : (Payment)lobjLog.GetOperationData());
				larrData = (((lopP == null) || (lopP.marrData == null) || (lopP.marrData.length == 0)) ? null : lopP.marrData.clone());
				if ( larrData != null )
				{
					Arrays.sort(larrData, new Comparator<PaymentData>()
					{
						public int compare(PaymentData o1, PaymentData o2)
						{
							return -o1.mdblValue.compareTo(o2.mdblValue);
						}
					});
					lobjData = larrData[0];
					try
					{
						lobjMode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PaymentType),
								lobjData.midPaymentType);
						lobjBank = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Bank),
								lobjData.midBank);
					}
					catch (Throwable e)
					{
						throw new BigBangJewelException(e.getMessage(), e);
					}
				}
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			larrTables[i] = new String[6];
			larrTables[i][0] = (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel());
			larrTables[i][1] = lobjReceipt.getLabel();
			larrTables[i][2] = String.format("%,.2f", ((BigDecimal)lobjReceipt.getAt(3)).negate());
			larrTables[i][3] = (lobjMode == null ? "" : lobjMode.getLabel());
			larrTables[i][4] = (lobjData == null ? "" : lobjData.mstrCheque);
			larrTables[i][5] = (lobjBank == null ? "" : (String)lobjBank.getAt(1));

			mlngCount++;
			mdblTotal = mdblTotal.add((BigDecimal)lobjReceipt.getAt(3));
		}

		larrParams.put("Count", "" + mlngCount + " recibo" + (mlngCount == 1 ? "" : "s"));
		larrParams.put("Total", String.format("%,.2f", mdblTotal));

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
