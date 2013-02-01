package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class ContactData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrName;
	public UUID midOwnerType;
	public UUID midOwnerId;
	public String mstrAddress1;
	public String mstrAddress2;
	public UUID midZipCode;
	public UUID midContactType;
	public ContactInfoData[] marrInfo;
	public ContactData[] marrSubContacts;

	public ContactData mobjPrevValues;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrName = (String)pobjSource.getAt(0);
		midOwnerType = (UUID)pobjSource.getAt(1);
		midOwnerId = (UUID)pobjSource.getAt(2);
		mstrAddress1 = (String)pobjSource.getAt(3);
		mstrAddress2 = (String)pobjSource.getAt(4);
		midZipCode = (UUID)pobjSource.getAt(5);
		midContactType = (UUID)pobjSource.getAt(6);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrName);
			pobjDest.setAt(1, midOwnerType);
			pobjDest.setAt(2, midOwnerId);
			pobjDest.setAt(3, mstrAddress1);
			pobjDest.setAt(4, mstrAddress2);
			pobjDest.setAt(5, midZipCode);
			pobjDest.setAt(6, midContactType);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjOwner;
		ObjectBase lobjZipCode;
		ObjectBase lobjContactType;
		int i;

		pstrBuilder.append("Contacto para: ");
		try
		{
			lobjOwner = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), midOwnerType), midOwnerId);
			pstrBuilder.append(lobjOwner.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o dono do contacto.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Nome: ");
		pstrBuilder.append(mstrName);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Tipo: ");
		try
		{
			lobjContactType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ContactType),
					midContactType);
			pstrBuilder.append((String)lobjContactType.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o tipo de contacto.)");
		}
		pstrBuilder.append(pstrLineBreak);

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
			lobjZipCode = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjectGUIDs.O_PostalCode), 
					midZipCode);
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

		if ( marrInfo != null )
		{
			for ( i = 0; i < marrInfo.length; i++ )
				marrInfo[i].Describe(pstrBuilder, pstrLineBreak);
		}

		if ( (marrSubContacts != null) )
		{
			pstrBuilder.append("Sub-Contactos: ");
			pstrBuilder.append(pstrLineBreak);
			for ( i = 0; i < marrSubContacts.length; i++ )
				marrSubContacts[i].Describe(pstrBuilder, pstrLineBreak);
		}
	}
}
