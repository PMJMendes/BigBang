package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;

public class QuoteRequestSubLineData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midQuoteRequest;
	public UUID midSubLine;

	public QuoteRequestCoverageData[] marrCoverages;
	public QuoteRequestValueData[] marrValues;

	public boolean mbNew;
	public boolean mbDeleted;

	public QuoteRequestSubLineData mobjPrevValues;

	public void Clone(QuoteRequestSubLineData pobjSource)
	{
		mid = pobjSource.mid;
		midQuoteRequest = pobjSource.midQuoteRequest;
		midSubLine = pobjSource.midSubLine;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midQuoteRequest = (UUID)pobjSource.getAt(0);
		midSubLine = (UUID)pobjSource.getAt(1);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, midQuoteRequest);
			pobjDest.setAt(1, midSubLine);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		SubLine lobjSubLine;

		pstrBuilder.append("Modalidade: ");
		try
		{
			lobjSubLine = SubLine.GetInstance(Engine.getCurrentNameSpace(), midSubLine);
			pstrBuilder.append(lobjSubLine.getLine().getCategory().getLabel()).append(" / ").
					append(lobjSubLine.getLine().getLabel()).append(" / ").append(lobjSubLine.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o nome da modalidade.)");
		}
		pstrBuilder.append(pstrLineBreak);
	}
}
