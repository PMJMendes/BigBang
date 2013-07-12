package com.premiumminds.BigBang.Jewel.Data;

public class DocDataFull
	extends DocDataLight
{
	private static final long serialVersionUID = 1L;

	public DocDataHeavy mobjPrevValues;

	public void FromOld(DocumentData pobjSource)
	{
		super.FromOld(pobjSource);

		this.mobjPrevValues.FromOld(pobjSource.mobjPrevValues);
	}
}
