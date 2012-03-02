package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class QuoteRequestData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;
	
	public String mstrNumber;
	public UUID midMediator;
	public String mstrNotes;
	public Boolean mbCaseStudy;
	public String mstrDocuShare;

	public UUID midManager;
	public UUID midProcess;

	public QuoteRequestSubLineData[] marrSubLines;
	public QuoteRequestObjectData[] marrObjects;

	public boolean mbModified;

	public QuoteRequestData mobjPrevValues;

	public void Clone(QuoteRequestData pobjSource)
	{
		mid = pobjSource.mid;
		mstrNumber = pobjSource.mstrNumber;
		midMediator = pobjSource.midMediator;
		mstrNotes = pobjSource.mstrNotes;
		mbCaseStudy = pobjSource.mbCaseStudy;
		mstrDocuShare = pobjSource.mstrDocuShare;
		midManager = pobjSource.midManager;
		midProcess = pobjSource.midProcess;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrNumber = (String)pobjSource.getAt(0);
		midProcess = (UUID)pobjSource.getAt(1);
		midMediator = (UUID)pobjSource.getAt(2);
		mstrNotes = (String)pobjSource.getAt(3);
		mbCaseStudy = (Boolean)pobjSource.getAt(4);
		mstrDocuShare = (String)pobjSource.getAt(5);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrNumber);
			pobjDest.setAt(1, midProcess);
			pobjDest.setAt(2, midMediator);
			pobjDest.setAt(3, mstrNotes);
			pobjDest.setAt(4, mbCaseStudy);
//			pobjDest.setAt(5, mstrDocuShare); JMMM: Nunca gravar por cima disto
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjAux;

		pstrBuilder.append("Número do Processo: ");
		pstrBuilder.append(mstrNumber);
		pstrBuilder.append(pstrLineBreak);

		if ( midMediator != null )
		{
			pstrBuilder.append("Mediador: ");
			try
			{
				lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Mediator),
						midMediator);
				pstrBuilder.append((String)lobjAux.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("(Erro a obter o mediador.)");
			}
			pstrBuilder.append(pstrLineBreak);
		}

		pstrBuilder.append("Observações: ");
		if ( mstrNotes != null )
			pstrBuilder.append(mstrNotes);
		pstrBuilder.append(pstrLineBreak);

		if ( (mbCaseStudy != null) && (boolean)mbCaseStudy )
			pstrBuilder.append("Case Study!").append(pstrLineBreak);
	}
}
