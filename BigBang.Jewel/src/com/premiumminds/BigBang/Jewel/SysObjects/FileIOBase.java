package com.premiumminds.BigBang.Jewel.SysObjects;

import Jewel.Engine.SysObjects.FileData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public abstract class FileIOBase
{
	public abstract void Parse(FileData pobjData) throws BigBangJewelException;
}
