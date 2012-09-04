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
	public Double commissionPercent; // Novo!
	public String description; // Novo!
	public Boolean isLife; // Novo!
	public Coverage[] coverages; // Novo!

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
