package com.premiumminds.BigBang.Jewel.Data;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.MediatorDeal;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;

public class MediatorDealData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midMediator;
	public UUID midSubLine;
	public BigDecimal mdblPercent;

	public MediatorDealData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midMediator = (UUID)pobjSource.getAt(MediatorDeal.I.MEDIATOR);
		midSubLine = (UUID)pobjSource.getAt(MediatorDeal.I.SUBLINE);
		mdblPercent = (BigDecimal)pobjSource.getAt(MediatorDeal.I.PERCENT);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MediatorDeal.I.MEDIATOR, midMediator);
			pobjDest.setAt(MediatorDeal.I.SUBLINE, midSubLine);
			pobjDest.setAt(MediatorDeal.I.PERCENT, mdblPercent);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		SubLine lobjSubLine;

		try
		{
			lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), midSubLine);
			pstrBuilder.append(lobjSubLine.getLine().getCategory().getLabel()).append(" / ").
					append(lobjSubLine.getLine().getLabel()).append(" / ").
					append(lobjSubLine.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter a modalidade.)");
		}

		pstrBuilder.append(": ").append(String.format("%,.2f", mdblPercent)).append(pstrLineBreak);
	}
}
