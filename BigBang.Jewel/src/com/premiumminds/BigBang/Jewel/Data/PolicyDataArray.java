package com.premiumminds.BigBang.Jewel.Data;

import java.io.Serializable;

public class PolicyDataArray
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public PolicyCoverageData[] marrCoverages;
	public PolicyObjectData[] marrObjects;
	public PolicyExerciseData[] marrExercises;
	public PolicyValueData[] marrValues;
}
