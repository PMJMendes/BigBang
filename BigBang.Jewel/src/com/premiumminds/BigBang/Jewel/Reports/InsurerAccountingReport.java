package com.premiumminds.BigBang.Jewel.Reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingDetail;
import com.premiumminds.BigBang.Jewel.Objects.InsurerAccountingMap;
import com.premiumminds.BigBang.Jewel.Objects.InsurerReceipt;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TextConverter;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class InsurerAccountingReport
	extends ReportBase
{
	public UUID midMap;
	public int mlngCount;
	public String mstrExtraText;
	public BigDecimal mdblExtraValue;
	public BigDecimal mdblPayables;
	public BigDecimal mdblTotalComms;
	public BigDecimal mdblTaxableComms;
	public BigDecimal mdblPreTax;
	public BigDecimal mdblTax;
	public BigDecimal mdblTotal;
	public BigDecimal mdblNetComms;
	public Timestamp mdtToday;
	public transient SQLServer mdb;

	protected UUID GetTemplateID()
	{
		return Constants.TID_InsurerAccounting;
	}

	public FileXfer Generate()
		throws BigBangJewelException
	{
		InsurerAccountingMap lobjMap;
		TransactionDetailBase[] larrDetails;
		Company lobjInsurer;
		ObjectBase lobjZipCode;
		HashMap<String, String> larrParams;
		String[][] larrTables;
		Receipt lobjReceipt;
		InsurerAccountingDetail lobjDetail;
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		InsurerReceipt lobjRec;
		BigDecimal ldblTotalPremiums;
		BigDecimal ldblDirectPremiums;
		BigDecimal ldblLifeComms;
		BigDecimal ldblLocalTotalComms;
		BigDecimal ldblTaxCoeff;
		boolean lbSubtract;
		int i;

		lobjMap = InsurerAccountingMap.GetInstance(Engine.getCurrentNameSpace(), midMap);
		larrDetails = lobjMap.getCurrentDetails();

		mlngCount = 0;
		mdblPayables = BigDecimal.ZERO;
		mdblTaxableComms = BigDecimal.ZERO;
		mdblPreTax = BigDecimal.ZERO;
		mdblTax = BigDecimal.ZERO;
		mdblTotal = BigDecimal.ZERO;

		ldblTotalPremiums = BigDecimal.ZERO;
		ldblDirectPremiums = BigDecimal.ZERO;
		ldblLocalTotalComms = BigDecimal.ZERO;
		lbSubtract = false;

		mstrExtraText = (String)lobjMap.getAt(InsurerAccountingMap.I.EXTRATEXT);
		mdblExtraValue = (BigDecimal)lobjMap.getAt(InsurerAccountingMap.I.EXTRAVALUE);
		if ( mdblExtraValue == null )
			mstrExtraText = null;
		if ( "".equals(mstrExtraText) )
			mstrExtraText = null;
		if ( mstrExtraText == null )
		{
			mdblExtraValue = BigDecimal.ZERO;
			mdblTotalComms = BigDecimal.ZERO;
			ldblLifeComms = BigDecimal.ZERO;
		}
		else
		{
			mstrExtraText = (String)lobjMap.getAt(InsurerAccountingMap.I.EXTRATEXT);
			mdblExtraValue = (BigDecimal)lobjMap.getAt(InsurerAccountingMap.I.EXTRAVALUE);
			if ( (lobjMap.getAt(InsurerAccountingMap.I.ISCOMMISSION) == null)  || !((Boolean)lobjMap.getAt(InsurerAccountingMap.I.ISCOMMISSION)) )
			{
				lbSubtract = true;
				mdblTotalComms = BigDecimal.ZERO;
				ldblLifeComms = BigDecimal.ZERO;
			}
			else
			{
				mdblTotalComms = mdblExtraValue;
				if ( (lobjMap.getAt(InsurerAccountingMap.I.HASTAX) == null) || !((Boolean)lobjMap.getAt(InsurerAccountingMap.I.HASTAX)) )
					ldblLifeComms = mdblExtraValue;
				else
					ldblLifeComms = BigDecimal.ZERO;
			}
		}

		lobjInsurer = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjMap.getAt(TransactionMapBase.I.OWNER));
		if ( lobjInsurer.getAt(8) == null )
			lobjZipCode = null;
		else
		{
			try
			{
				lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode),
						(UUID)lobjInsurer.getAt(8));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
		mdtToday = new Timestamp(new java.util.Date().getTime());

		larrParams = new HashMap<String, String>();
		larrParams.put("InsurerName", (String)lobjInsurer.getAt(0));
		larrParams.put("InsurerAddress1", (lobjInsurer.getAt(6) == null ? "" : (String)lobjInsurer.getAt(6)));
		larrParams.put("InsurerAddress2", (lobjInsurer.getAt(7) == null ? "" : (String)lobjInsurer.getAt(7)));
		larrParams.put("InsurerZipCode", (lobjZipCode == null ? "" : (String)lobjZipCode.getAt(0)));
		larrParams.put("InsurerZipLocal", ((lobjZipCode == null) || (lobjZipCode.getAt(1) == null) ? "" : (String)lobjZipCode.getAt(1)));
		larrParams.put("Date", mdtToday.toString().substring(0, 10));

		larrTables = new String[larrDetails.length][];
		for ( i = 0; i < larrDetails.length; i++ )
		{
			lobjDetail = (InsurerAccountingDetail)larrDetails[i];
			lobjReceipt = larrDetails[i].getReceipt();
			lobjPolicy = lobjReceipt.getAbsolutePolicy();
			lobjSubPolicy = lobjReceipt.getSubPolicy();

			larrTables[i] = new String[6];
			larrTables[i][0] = (lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel());
			larrTables[i][1] = lobjReceipt.getLabel();
			larrTables[i][2] = lobjReceipt.getReceiptType();
			larrTables[i][3] = lobjPolicy.GetSubLine().getDescription();
			larrTables[i][4] = String.format("%,.2f", lobjDetail.getPayablePremium());
			larrTables[i][5] = String.format("%,.2f", lobjDetail.getCommissions());

			mlngCount++;
			ldblTotalPremiums = ldblTotalPremiums.add(lobjDetail.getPremium());
			ldblDirectPremiums = ldblDirectPremiums.add(lobjDetail.getDirectPremium());
			mdblTotalComms = mdblTotalComms.add(lobjDetail.getCommissions());
			ldblLocalTotalComms = ldblLocalTotalComms.add(lobjDetail.getCommissions());
			ldblLifeComms = ldblLifeComms.add(lobjDetail.getLifeComms());
		}

		mdblPayables = ldblTotalPremiums.subtract(ldblDirectPremiums);
		mdblTaxableComms = mdblTotalComms.subtract(ldblLifeComms);

		if (lobjMap.getCompany()!=null && lobjMap.getCompany().getTaxRetention())
		{
			ldblTaxCoeff = Utils.getCommissionsTax();
			ldblTaxCoeff = ldblTaxCoeff.add(new BigDecimal(100)).setScale(10);
			ldblTaxCoeff = Utils.getCommissionsTax().setScale(10).divide(ldblTaxCoeff, RoundingMode.HALF_UP);
			mdblTax = mdblTaxableComms.multiply(ldblTaxCoeff).setScale(2, RoundingMode.HALF_UP);
		}
		else
		{
			mdblTax = BigDecimal.ZERO;
		}

		mdblPreTax = mdblPayables.subtract(mdblTotalComms);
		if ( lbSubtract )
			mdblPreTax = mdblPreTax.subtract(mdblExtraValue);
		mdblTotal = mdblPreTax.add(mdblTax);
		mdblNetComms = mdblTotalComms.subtract(mdblTax);

		try
		{
			lobjRec = InsurerReceipt.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjRec.setAt(InsurerReceipt.I.NUMBER, GetNewRecNumber(mdb));
			lobjRec.setAt(InsurerReceipt.I.INSURER, lobjInsurer.getKey());
			lobjRec.setAt(InsurerReceipt.I.VALUE, mdblTotalComms);
			lobjRec.setAt(InsurerReceipt.I.TAX, mdblTax);
			lobjRec.setAt(InsurerReceipt.I.DATE, mdtToday);
			lobjRec.SaveToDb(mdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrParams.put("Count", "" + mlngCount);
		larrParams.put("ExtraText", (mstrExtraText == null ? "" : mstrExtraText));
		larrParams.put("ExtraValue", (mstrExtraText == null ? "" : String.format("%,.2f", mdblExtraValue)));
		larrParams.put("PayablePremiums", String.format("%,.2f", mdblPayables));
		larrParams.put("LocalTotalComms", String.format("%,.2f", ldblLocalTotalComms));
		larrParams.put("TotalComms", String.format("%,.2f", mdblTotalComms));
		larrParams.put("TaxableComms", String.format("%,.2f", mdblTaxableComms));
		larrParams.put("Tax", String.format("%,.2f", mdblTax));
		larrParams.put("Total", String.format("%,.2f", mdblTotal));
		larrParams.put("Movement", (mdblTotal.signum() < 0 ? "receber da Seguradora" : "transferir para a Seguradora" ));

		larrParams.put("RecNumber", lobjRec.getLabel());
		larrParams.put("NetComms", String.format("%,.2f", mdblNetComms));
		larrParams.put("NIF", (lobjInsurer.getAt(4) == null ? "-" : (String)lobjInsurer.getAt(4)));
		larrParams.put("TextValue", TextConverter.fromCurrency(mdblNetComms.doubleValue()));

		return Generate(larrParams, new String[][][] {larrTables});
	}

	private String GetNewRecNumber(SQLServer pdb)
		throws BigBangJewelException
	{
		int mlngYear;
		String lstrFilter;
		IEntity lrefInsurerRecs;
        ResultSet lrsRecs;
        int llngResult;
        String lstrAux;
        int llngAux;

        mlngYear = Calendar.getInstance().get(Calendar.YEAR);

		try
		{
	        lstrFilter = Integer.toString(mlngYear) + "/%";
			lrefInsurerRecs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_InsurerReceipt)); 

			lrsRecs = lrefInsurerRecs.SelectByMembers(pdb, new int[] {InsurerReceipt.I.NUMBER}, new java.lang.Object[] {lstrFilter},
					new int[] {Integer.MIN_VALUE});
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			while ( lrsRecs.next() )
			{
				lstrAux = lrsRecs.getString(InsurerReceipt.I.NUMBER + 2).substring(lstrFilter.length() - 1, lstrFilter.length() + 3);
				llngAux = Integer.parseInt(lstrAux);
				if ( llngAux >= llngResult )
					llngResult = llngAux + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsRecs.close(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsRecs.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lstrFilter.substring(0, lstrFilter.length() - 1) + String.format("%04d", llngResult);
	}
}
