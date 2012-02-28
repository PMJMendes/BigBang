package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequestObject;
import com.premiumminds.BigBang.Jewel.Objects.Tax;

public class QuoteRequestValueData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrValue;
	public UUID midOwner;
	public UUID midField;
	public UUID midObject;
	public int mlngObject;

	public boolean mbNew;
	public boolean mbDeleted;

	public QuoteRequestValueData mobjPrevValues;

	public void Clone(QuoteRequestValueData pobjSource)
	{
		mid = pobjSource.mid;
		mstrValue = pobjSource.mstrValue;
		midOwner = pobjSource.midOwner;
		midField = pobjSource.midField;
		midObject = pobjSource.midObject;
		mlngObject = pobjSource.mlngObject;
	}

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrValue = (String)pobjSource.getAt(0);
		midOwner = (UUID)pobjSource.getAt(1);
		midField = (UUID)pobjSource.getAt(2);
		midObject = (UUID)pobjSource.getAt(3);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrValue);
			pobjDest.setAt(1, midOwner);
			pobjDest.setAt(2, midField);
			pobjDest.setAt(3, midObject);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		Tax lobjTax;
		QuoteRequestObject lobjObject;

		try
		{
			lobjTax = Tax.GetInstance(Engine.getCurrentNameSpace(), midField);
			pstrBuilder.append(lobjTax.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o nome do campo.)");
		}

		if ( midObject != null )
		{
			pstrBuilder.append(" [");
			try
			{
				lobjObject = QuoteRequestObject.GetInstance(Engine.getCurrentNameSpace(), midObject);
				pstrBuilder.append(lobjObject.getLabel());
			}
			catch (Throwable e)
			{
				pstrBuilder.append("<Erro a obter o objecto correspondente.>");
			}
			pstrBuilder.append("]");
		}

		pstrBuilder.append(": ").append(mstrValue).append(pstrLineBreak);
	}
}
