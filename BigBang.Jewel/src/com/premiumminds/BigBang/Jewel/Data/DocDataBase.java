package com.premiumminds.BigBang.Jewel.Data;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class DocDataBase
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrName;
	public UUID midOwnerType;
	public UUID midOwnerId;
	public UUID midDocType;
	public String mstrText;
	public Timestamp mdtRefDate;
	public DocInfoData[] marrInfo;

	public DSBridgeData mobjDSBridge;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrName = (String)pobjSource.getAt(0);
		midOwnerType = (UUID)pobjSource.getAt(1);
		midOwnerId = (UUID)pobjSource.getAt(2);
		midDocType = (UUID)pobjSource.getAt(3);
		mstrText = (String)pobjSource.getAt(4);
		mdtRefDate = (Timestamp)pobjSource.getAt(6);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(0, mstrName);
			pobjDest.setAt(1, midOwnerType);
			pobjDest.setAt(2, midOwnerId);
			pobjDest.setAt(3, midDocType);
			pobjDest.setAt(4, mstrText);
			pobjDest.setAt(6, mdtRefDate);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		ObjectBase lobjOwner;
		ObjectBase lobjDocType;
		int i;

		pstrBuilder.append("Documento para: ");
		try
		{
			lobjOwner = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), midOwnerType), midOwnerId);
			pstrBuilder.append(lobjOwner.getLabel());
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o dono do documento.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Nome: ");
		pstrBuilder.append(mstrName);
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Tipo: ");
		try
		{
			lobjDocType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DocType),
					midDocType);
			pstrBuilder.append((String)lobjDocType.getAt(0));
		}
		catch (Throwable e)
		{
			pstrBuilder.append("(Erro a obter o tipo de documento.)");
		}
		pstrBuilder.append(pstrLineBreak);

		pstrBuilder.append("Data: ");
		pstrBuilder.append(mdtRefDate.toString().substring(0, 10));
		pstrBuilder.append(pstrLineBreak);

		if ( mstrText != null )
		{
			pstrBuilder.append("Texto:");
			pstrBuilder.append(pstrLineBreak);
			pstrBuilder.append(mstrText);
			pstrBuilder.append(pstrLineBreak);
		}

		if ( marrInfo != null )
		{
			for ( i = 0; i < marrInfo.length; i++ )
				marrInfo[i].Describe(pstrBuilder, pstrLineBreak);
		}
	}
}
