package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;

public class CasualtyData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrNumber;
	public Timestamp mdtCasualtyDate;
	public String mstrDescription;
	public String mstrNotes;
	public Boolean mbCaseStudy;
	public UUID midClient;
	public BigDecimal mdblPercentFault;
	public Boolean mbFraud;

	public UUID midManager;
	public UUID midProcess;

	public CasualtyData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber =       (String)     pobjSource.getAt(Casualty.I.NUMBER);
		midProcess =       (UUID)       pobjSource.getAt(Casualty.I.PROCESS);
		mdtCasualtyDate =  (Timestamp)  pobjSource.getAt(Casualty.I.DATE);
		mstrDescription =  (String)     pobjSource.getAt(Casualty.I.DESCRIPTION);
		mstrNotes =        (String)     pobjSource.getAt(Casualty.I.NOTES);
		mbCaseStudy =      (Boolean)    pobjSource.getAt(Casualty.I.CASESTUDY);
		midClient =        (UUID)       pobjSource.getAt(Casualty.I.CLIENT);
		mdblPercentFault = (BigDecimal) pobjSource.getAt(Casualty.I.PERCENTFAULT);
		mbFraud =      (Boolean)    pobjSource.getAt(Casualty.I.FRAUD);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(Casualty.I.NUMBER,       mstrNumber);
			pobjDest.setAt(Casualty.I.PROCESS,      midProcess);
			pobjDest.setAt(Casualty.I.DATE,         mdtCasualtyDate);
			pobjDest.setAt(Casualty.I.DESCRIPTION,  mstrDescription);
			pobjDest.setAt(Casualty.I.NOTES,        mstrNotes);
			pobjDest.setAt(Casualty.I.CASESTUDY,    mbCaseStudy);
			pobjDest.setAt(Casualty.I.CLIENT,       midClient);
			pobjDest.setAt(Casualty.I.PERCENTFAULT, mdblPercentFault);
			pobjDest.setAt(Casualty.I.FRAUD,    mbFraud);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		pstrBuilder.append("Número do Processo: ");
		pstrBuilder.append(mstrNumber);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data do Sinistro: ");
		pstrBuilder.append(mdtCasualtyDate == null ? "(não indicada)" : mdtCasualtyDate.toString().substring(0, 10));

		pstrBuilder.append("Descrição: ");
		if ( mstrDescription != null )
			pstrBuilder.append(mstrDescription);
		pstrBuilder.append(pstrLineBreak);

		if ( mdblPercentFault != null )
		{
			pstrBuilder.append("Responsabilidade: ");
			pstrBuilder.append(mdblPercentFault);
			pstrBuilder.append("%");
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Notas Internas: ");
		if ( mstrNotes != null )
			pstrBuilder.append(mstrNotes);
		pstrBuilder.append(pstrLineBreak);

		if ( (mbCaseStudy != null) && (boolean)mbCaseStudy )
			pstrBuilder.append("Case Study!").append(pstrLineBreak);
		
		if ( (mbFraud != null) && (boolean)mbFraud )
			pstrBuilder.append("Fraude!").append(pstrLineBreak);
	}
}
