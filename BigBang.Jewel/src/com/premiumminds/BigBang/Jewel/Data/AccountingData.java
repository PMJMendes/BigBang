package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.AccountingEntry;

public class AccountingData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public int mlngNumber;
	public Timestamp mdtDate;
	public BigDecimal mdblAccount;
	public BigDecimal mdblValue;
	public String mstrSign;
	public int mlngBook;
	public String mstrSupportDoc;
	public String mstrDesc;
	public UUID midDocType;
	public Integer mlngYear;
	public UUID midFile;
	public UUID midCostCenter;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mlngNumber     = (Integer)    pobjSource.getAt(AccountingEntry.I.NUMBER);
		mdtDate        = (Timestamp)  pobjSource.getAt(AccountingEntry.I.DATE);
		mdblAccount    = (BigDecimal) pobjSource.getAt(AccountingEntry.I.ACCOUNT);
		mdblValue      = (BigDecimal) pobjSource.getAt(AccountingEntry.I.VALUE);
		mstrSign       = (String)     pobjSource.getAt(AccountingEntry.I.SIGN);
		mlngBook       = (Integer)    pobjSource.getAt(AccountingEntry.I.BOOK);
		mstrSupportDoc = (String)     pobjSource.getAt(AccountingEntry.I.SUPPORT);
		mstrDesc       = (String)     pobjSource.getAt(AccountingEntry.I.DESCRIPTION);
		midDocType     = (UUID)       pobjSource.getAt(AccountingEntry.I.DOCTYPE);
		mlngYear       = (Integer)    pobjSource.getAt(AccountingEntry.I.YEAR);
		midFile        = (UUID)       pobjSource.getAt(AccountingEntry.I.FILE);
		midCostCenter  = (UUID)       pobjSource.getAt(AccountingEntry.I.COSTCENTER);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(AccountingEntry.I.NUMBER,      mlngNumber);
			pobjDest.setAt(AccountingEntry.I.DATE,        mdtDate);
			pobjDest.setAt(AccountingEntry.I.ACCOUNT,     mdblAccount);
			pobjDest.setAt(AccountingEntry.I.VALUE,       mdblValue);
			pobjDest.setAt(AccountingEntry.I.SIGN,        mstrSign);
			pobjDest.setAt(AccountingEntry.I.BOOK,        mlngBook);
			pobjDest.setAt(AccountingEntry.I.SUPPORT,     mstrSupportDoc);
			pobjDest.setAt(AccountingEntry.I.DESCRIPTION, mstrDesc);
			pobjDest.setAt(AccountingEntry.I.DOCTYPE,     midDocType);
			pobjDest.setAt(AccountingEntry.I.YEAR,        mlngYear);
			pobjDest.setAt(AccountingEntry.I.FILE,        midFile);
			pobjDest.setAt(AccountingEntry.I.COSTCENTER,  midCostCenter);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		if ( mstrSign == "D" )
			pstrBuilder.append("Débito - ");
		else
			pstrBuilder.append("Crédito - ");
		pstrBuilder.append(mdblValue);
	}
}
