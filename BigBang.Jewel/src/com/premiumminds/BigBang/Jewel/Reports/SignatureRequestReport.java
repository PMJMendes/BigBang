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

public class SignatureRequestReport
	extends ReportBase
{
	public UUID midClient;
	public UUID[] marrReceiptIDs;
	public BigDecimal mdblTotal;
	public int mlngCount;

	protected UUID GetTemplateID()
	{
		return Constants.TID_SignatureRequest;
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

			larrTables[i] = new String[9];
			larrTables[i][0] = (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel());
			larrTables[i][1] = lobjReceipt.getLabel();
			larrTables[i][2] = lobjPolicy.GetSubLine().getLine().getCategory().getLabel();
			larrTables[i][3] = (String)lobjPolicy.GetCompany().getAt(1);
			larrTables[i][4] = ((BigDecimal)lobjReceipt.getAt(3)).toPlainString();
			larrTables[i][5] = (String)lobjReceipt.getAt(14);

			mlngCount++;
			mdblTotal = mdblTotal.add((BigDecimal)lobjReceipt.getAt(3));
		}

		larrParams.put("Count", "" + mlngCount + " recibo" + (mlngCount == 1 ? "" : "s"));
		larrParams.put("Total", mdblTotal.toPlainString());

		return Generate(larrParams, new String[][][] {larrTables});
	}

}
