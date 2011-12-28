package bigBang.definitions.shared;

import java.io.Serializable;

public class Tax
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String coverageId;
	public String fieldTypeId;
	public String unitsLabel; // Novo!
	public String defaultValue; // Default value
	public boolean variesByObject; // Novo!
	public boolean variesByExercise;  // Novo!
	public String refersToEntityId;  // Novo!
	public int columnOrder; // Novo!

	public Tax(){}
	
	public Tax(Tax original) {
		this.id = original.id;
		this.name = original.name;
		this.coverageId = original.coverageId;
		this.unitsLabel = original.unitsLabel;
		this.fieldTypeId = original.fieldTypeId;
		this.defaultValue = original.defaultValue;
		this.variesByObject = original.variesByObject;
		this.variesByExercise = original.variesByExercise;
		this.refersToEntityId = original.refersToEntityId;
		this.columnOrder = original.columnOrder;
	}
}
