package com.premiumminds.BigBang.Jewel.Reports;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
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

public class DASFormReport
	extends ReportBase
{
	public UUID midClient;
	public UUID midReceipt;

	protected UUID GetTemplateID()
	{
		return Constants.TID_DASForm;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Client lobjClient;
		HashMap<String, String> larrParams;
		Receipt lobjReceipt;
		IProcess lobjProc;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		ILog lobjLog;

		lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), midClient);

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

		larrParams = new HashMap<String, String>();
		larrParams.put("ClientName", (String)lobjClient.getAt(0));
		larrParams.put("Policy", (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel()));
		larrParams.put("Company", (String)lobjPolicy.GetCompany().getLabel());
		larrParams.put("DueDate", ((Timestamp)lobjReceipt.getAt(11)).toString().substring(0, 10));
		larrParams.put("PayDate", lobjLog.GetTimestamp().toString().substring(0, 10));

		return Generate(larrParams, new String[0][][]);
	}
}
