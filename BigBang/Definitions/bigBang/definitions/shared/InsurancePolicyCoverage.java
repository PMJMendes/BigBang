package bigBang.definitions.shared;

import java.io.Serializable;

public class InsurancePolicyCoverage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static enum CasualtyLimitUnit {
		EURO,
		NUMBER_OF_DAYS
	}
	
	public static enum ValuePeriodicity {
		ABSOLUTE,
		DAILY
	}

	public String id;
	
	public String coverageId;
	public ValuePeriodicity valuePeriodicity;
	public float value;
	public String deductibleTypeId;
	public Double deductibleValue;
	public String deductibleNotes;
	public String casualtyLimit;
	public CasualtyLimitUnit casualtyLimitUnit;
	public int gracePeriod;
	public int maximumDuration;
	public String calculationGroupId;
	
	
}
