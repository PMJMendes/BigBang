package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;

public class MediatorData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrName;
	public String mstrISPNumber;
	public String mstrFiscalNumber;
	public String mstrBankID;
	public UUID midProfile;
	public String mstrAddress1;
	public String mstrAddress2;
	public UUID midZipCode;
	public BigDecimal mdblPercent;
	public String mstrCalcClass;
	public Boolean mbHasRetention;
	public String mstrAcctCode;
	public MediatorDealData[] marrDeals;
	public MediatorExceptionData[] marrExceptions;

	public MediatorData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrName = (String)pobjSource.getAt(Mediator.I.NAME);
		mstrISPNumber = (String)pobjSource.getAt(Mediator.I.ISPNUMBER);
		mstrFiscalNumber = (String)pobjSource.getAt(Mediator.I.FISCALNUMBER);
		mstrBankID = (String)pobjSource.getAt(Mediator.I.BANKINGID);
		midProfile = (UUID)pobjSource.getAt(Mediator.I.PROFILE);
		mstrAddress1 = (String)pobjSource.getAt(Mediator.I.ADDRESS1);
		mstrAddress2 = (String)pobjSource.getAt(Mediator.I.ADDRESS2);
		midZipCode = (UUID)pobjSource.getAt(Mediator.I.ZIPCODE);
		mdblPercent = (BigDecimal)pobjSource.getAt(Mediator.I.PERCENT);
		mstrCalcClass = (String)pobjSource.getAt(Mediator.I.CALCCLASS);
		mbHasRetention = (Boolean)pobjSource.getAt(Mediator.I.HASRETENTION);
		mstrAcctCode = (String)pobjSource.getAt(Mediator.I.ACCTCODE);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(Mediator.I.NAME, mstrName);
			pobjDest.setAt(Mediator.I.ISPNUMBER, mstrISPNumber);
			pobjDest.setAt(Mediator.I.FISCALNUMBER, mstrFiscalNumber);
			pobjDest.setAt(Mediator.I.BANKINGID, mstrBankID);
			pobjDest.setAt(Mediator.I.PROFILE, midProfile);
			pobjDest.setAt(Mediator.I.ADDRESS1, mstrAddress1);
			pobjDest.setAt(Mediator.I.ADDRESS2, mstrAddress2);
			pobjDest.setAt(Mediator.I.ZIPCODE, midZipCode);
			pobjDest.setAt(Mediator.I.PERCENT, mdblPercent);
			pobjDest.setAt(Mediator.I.CALCCLASS, mstrCalcClass);
			pobjDest.setAt(Mediator.I.HASRETENTION, mbHasRetention);
			pobjDest.setAt(Mediator.I.ACCTCODE, mstrAcctCode);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjProfile;
		ObjectBase lobjZipCode;
		int i;

		pstrBuilder.append("Nome: ");
		pstrBuilder.append(mstrName);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("Número no ISP: ");
		pstrBuilder.append(mstrISPNumber);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("NIFC: ");
		pstrBuilder.append(mstrFiscalNumber);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("NIB: ");
		pstrBuilder.append(mstrBankID);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Perfil de Comissionamento: ");
		try
		{
			lobjProfile = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CommProfile), midProfile);
			pstrBuilder.append((String)lobjProfile.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o perfil de comissionamento.)");
		}
		pstrBuilder.append(pstrLineBreak);

		if ( mdblPercent != null )
		{
			pstrBuilder.append("Percentagem de Retrocessão: ");
			pstrBuilder.append(mdblPercent);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( (mbHasRetention != null) && mbHasRetention )
		{
			pstrBuilder.append("Com retenção na fonte.");
			pstrBuilder.append(pstrLineBreak);
		}

		if ( mstrAcctCode != null )
		{
			pstrBuilder.append("Contabilidade: 278");
			pstrBuilder.append(mstrAcctCode);
			pstrBuilder.append(" / 6225");
			pstrBuilder.append(mstrAcctCode);
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Morada:");
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");
		pstrBuilder.append(mstrAddress1);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");
		pstrBuilder.append(mstrAddress2);
		pstrBuilder.append(pstrLineBreak);
		pstrBuilder.append("- ");

		try
		{
			lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode), midZipCode);
			pstrBuilder.append((String)lobjZipCode.getAt(0));
			pstrBuilder.append(" ");
			pstrBuilder.append((String)lobjZipCode.getAt(1));
			pstrBuilder.append(pstrLineBreak);
			pstrBuilder.append("- ");
        	pstrBuilder.append((String)lobjZipCode.getAt(4));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o código postal.)");
		}
		pstrBuilder.append(pstrLineBreak);

		if ( (marrDeals != null) && (marrDeals.length > 0) )
		{
			pstrBuilder.append("Percentagens negociadas:");

			for ( i = 0; i < marrDeals.length; i++ )
				marrDeals[i].Describe(pstrBuilder, pstrLineBreak);
		}
	}
}
