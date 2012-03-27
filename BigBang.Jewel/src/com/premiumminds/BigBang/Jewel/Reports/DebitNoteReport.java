package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Line;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.SysObjects.NumberConv;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;

public class DebitNoteReport
	extends ReportBase
{
	public String mstrNumber;
	public UUID midPolicy;
	public String mstrReceipt;
	public Timestamp mdtMaturity;
	public BigDecimal mdblValue;
	public Timestamp mdtDate;

	protected UUID GetTemplateID()
	{
		return Constants.TID_DebitNote;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		Policy lobjPolicy;
		Company lobjCompany;
		SubLine lobjSubLine;
		Line lobjLine;
		Category lobjCategory;
		Client lobjClient;
		ObjectBase lobjZipCode;
		HashMap<String, String> larrParams;

		lobjPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), midPolicy);
		lobjCompany = lobjPolicy.GetCompany();
		lobjSubLine = lobjPolicy.GetSubLine();
		lobjLine = lobjSubLine.getLine();
		lobjCategory = lobjLine.getCategory();
		lobjClient = lobjPolicy.GetClient();
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

		larrParams = new HashMap<String, String>();
		larrParams.put("Number", mstrNumber);
		larrParams.put("Company", lobjCompany.getLabel());
		larrParams.put("Policy", (lobjPolicy.getLabel().startsWith("-") ? "(em emissão)" : lobjPolicy.getLabel()));
		larrParams.put("Receipt", (mstrReceipt == null ? "(sem número)" : mstrReceipt));
		larrParams.put("Line", lobjCategory.getLabel());
		larrParams.put("Maturity", mdtMaturity.toString().substring(0, 10));
		larrParams.put("Value", mdblValue.toPlainString());
		larrParams.put("Client", lobjClient.getLabel());
		larrParams.put("Address1", (lobjClient.getAt(2) == null ? "" : (String)lobjClient.getAt(2)));
		larrParams.put("Address2", (lobjClient.getAt(3) == null ? "" : (String)lobjClient.getAt(3)));
		larrParams.put("ZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("ZipLocal", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Text", NumberConv.getAsEuroText(mdblValue.doubleValue()));
		larrParams.put("Date", mdtDate.toString().substring(0, 10));

		return Generate(larrParams, null);
	}
}
