package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class ContactInfoData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public UUID midOwner;
	public UUID midType;
	public String mstrValue;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		midOwner = (UUID)pobjSource.getAt(0); 
		midType = (UUID)pobjSource.getAt(1); 
		mstrValue = (String)pobjSource.getAt(2); 
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, midOwner);
			pobjDest.setAt(1, midType);
			pobjDest.setAt(2, mstrValue);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjInfoType;

		try
		{
			lobjInfoType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CInfoType),
					midType);
			pstrBuilder.append((String)lobjInfoType.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o tipo de informação de contacto.)");
		}
		pstrBuilder.append(": ");
		pstrBuilder.append(mstrValue);
		pstrBuilder.append(pstrLineBreak);
	}
}
