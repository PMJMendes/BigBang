package com.premiumminds.BigBang.Jewel.SysObjects;

import Jewel.Engine.SysObjects.ObjectBase;

public abstract class TransactionDetailBase
	extends ObjectBase
{
	public static class I
	{
		public static int OWNER   = 0;
		public static int RECEIPT = 1;
		public static int VOIDED  = 2;
	}
}
