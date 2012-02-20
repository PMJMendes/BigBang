package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class NegotiationData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midCompany;
	public String mstrNotes;
	public Timestamp mdtLimitDate;

	public UUID midManager;
	public UUID midProcess;
	
	public NegotiationData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midCompany = (UUID)pobjSource.getAt(0);
		mstrNotes = (String)pobjSource.getAt(1);
		mdtLimitDate = (Timestamp)pobjSource.getAt(2);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, midCompany);
			pobjDest.setAt(1, mstrNotes);
			pobjDest.setAt(2, mdtLimitDate);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;

		pstrBuilder.append("Negociação com a seguradora ");
		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Company), midCompany);
			pstrBuilder.append(lobjAux.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter a seguradora.)");
		}
		pstrBuilder.append(pstrLineBreak);

		if ( mstrNotes != null )
		{
			pstrBuilder.append("Observações:").append(pstrLineBreak).append(mstrNotes).append(pstrLineBreak);
		}
	}
}
