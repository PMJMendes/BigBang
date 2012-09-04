package com.premiumminds.BigBang.Jewel.Data;

import java.io.Serializable;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

import Jewel.Engine.SysObjects.ObjectBase;

public interface DataBridge
	extends Serializable
{
	public void FromObject(ObjectBase pobjSource);
	public void ToObject(ObjectBase pobjDest) throws BigBangJewelException;
	
	public void Describe(StringBuilder pstrBuilder, String pstrLineBreak);
}
