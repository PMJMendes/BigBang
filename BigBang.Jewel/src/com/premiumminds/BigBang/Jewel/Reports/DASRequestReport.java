package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.ILog;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class DASRequestReport
	extends ReportBase
{
	public UUID midClient;
	public UUID midReceipt;

	protected UUID GetTemplateID()
	{
		return Constants.TID_DASRequest;
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
		ILog lobjLog;

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

		larrTables = new String[1][];
		lobjReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), midReceipt);
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
			lobjLog = lobjProc.GetLiveLog(Constants.OPID_Receipt_Payment);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrTables[0] = new String[6];
		larrTables[0][0] = (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel());
		larrTables[0][1] = lobjReceipt.getLabel();
		larrTables[0][2] = lobjPolicy.GetSubLine().getLine().getCategory().getLabel();
		larrTables[0][3] = (String)lobjPolicy.GetCompany().getAt(1);
		larrTables[0][4] = String.format("%,.2f", ((BigDecimal)lobjReceipt.getAt(3)));
		larrTables[0][5] = (String)lobjReceipt.getAt(14);

		larrParams.put("Count", "1 recibo");
		larrParams.put("Total", String.format("%,.2f", ((BigDecimal)lobjReceipt.getAt(3))));

		larrParams.put("Policy", (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel()));
		larrParams.put("Company", (String)lobjPolicy.GetCompany().getLabel());
		larrParams.put("DueDate", ((Timestamp)lobjReceipt.getAt(11)).toString().substring(0, 10));
		larrParams.put("PayDate", lobjLog.GetTimestamp().toString().substring(0, 10));

		return Generate(larrParams, new String[][][] {larrTables});
	}
}
