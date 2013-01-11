package com.premiumminds.BigBang.Jewel.Data;

import java.util.UUID;

import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;

public class MessageAddressData
	implements DataBridge
{
	private static final long serialVersionUID = 1L;

	public UUID mid;

	public String mstrAddress;
	public UUID midOwner;
	public UUID midUsage;
	public UUID midUser;
	public UUID midInfo;
	public String mstrDisplay;

	public void FromObject(ObjectBase pobjSource)
	{
		mid = pobjSource.getKey();

		mstrAddress = (String)pobjSource.getAt(MessageAddress.I.ADDRESS);
		midOwner    =   (UUID)pobjSource.getAt(MessageAddress.I.OWNER);
		midUsage    =   (UUID)pobjSource.getAt(MessageAddress.I.USAGE);
		midUser     =   (UUID)pobjSource.getAt(MessageAddress.I.USER);
		midInfo     =   (UUID)pobjSource.getAt(MessageAddress.I.CONTACTINFO);
		mstrDisplay = (String)pobjSource.getAt(MessageAddress.I.DISPLAYNAME);
	}

	public void ToObject(ObjectBase pobjDest)
		throws BigBangJewelException
	{
		try
		{
			pobjDest.setAt(MessageAddress.I.ADDRESS,     mstrAddress);
			pobjDest.setAt(MessageAddress.I.OWNER,       midOwner);
			pobjDest.setAt(MessageAddress.I.USAGE,       midUsage);
			pobjDest.setAt(MessageAddress.I.USER,        midUser);
			pobjDest.setAt(MessageAddress.I.CONTACTINFO, midInfo);
			pobjDest.setAt(MessageAddress.I.DISPLAYNAME, mstrDisplay);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak)
	{
		if ( Constants.UsageID_From.equals(midUsage) )
			pstrBuilder.append(" DE: ");
		else if ( Constants.UsageID_To.equals(midUsage) )
			pstrBuilder.append(" PARA: ");
		else if ( Constants.UsageID_CC.equals(midUsage) )
			pstrBuilder.append(" CC: ");
		else if ( Constants.UsageID_BCC.equals(midUsage) )
			pstrBuilder.append(" BCC: ");
		else if ( Constants.UsageID_ReplyTo.equals(midUsage) )
			pstrBuilder.append(" RESPONDER A: ");
		else
			pstrBuilder.append(" (outro): ");

		if ( mstrDisplay != null )
			pstrBuilder.append(mstrDisplay).append(" &lt;").append(mstrAddress).append("&gt;");
		else
			pstrBuilder.append(mstrAddress);
		pstrBuilder.append(pstrLineBreak);
	}
}
