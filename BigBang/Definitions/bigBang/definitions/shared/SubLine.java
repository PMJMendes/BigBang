package bigBang.definitions.shared;

import java.io.Serializable;

public class SubLine
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String lineId;
	public String objectTypeId;
	public String exercisePeriodId;
	public Double commissionPercent;
	public String description;
	public Boolean isLife;
	public Boolean isHR;
	public Coverage[] coverages;

	public SubLine()
	{
		coverages = new Coverage[0];
	}
	
	public SubLine(SubLine original) {
		this.id = original.id;
		this.name = original.name;
		this.lineId = original.lineId;
		this.objectTypeId = original.objectTypeId;
		this.exercisePeriodId = original.exercisePeriodId;
		this.coverages = original.coverages;
	}
}
