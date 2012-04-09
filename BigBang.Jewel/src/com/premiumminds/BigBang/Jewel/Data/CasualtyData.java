package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

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

	public UUID midManager;
	public UUID midProcess;

	public CasualtyData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber = (String)pobjSource.getAt(0);
		midProcess = (UUID)pobjSource.getAt(1);
		mdtCasualtyDate = (Timestamp)pobjSource.getAt(2);
		mstrDescription = (String)pobjSource.getAt(3);
		mstrNotes = (String)pobjSource.getAt(4);
		mbCaseStudy = (Boolean)pobjSource.getAt(5);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrNumber);
			pobjDest.setAt(1, midProcess);
			pobjDest.setAt(2, mdtCasualtyDate);
			pobjDest.setAt(3, mstrDescription);
			pobjDest.setAt(4, mstrNotes);
			pobjDest.setAt(5, mbCaseStudy);
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

		pstrBuilder.append("Notas Internas: ");
		if ( mstrNotes != null )
			pstrBuilder.append(mstrNotes);
		pstrBuilder.append(pstrLineBreak);

		if ( (mbCaseStudy != null) && (boolean)mbCaseStudy )
			pstrBuilder.append("Case Study!").append(pstrLineBreak);
	}
}
