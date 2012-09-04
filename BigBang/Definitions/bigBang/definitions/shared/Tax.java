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
	public String unitsLabel;
	public String defaultValue;
	public boolean variesByObject;
	public boolean variesByExercise;
	public String refersToEntityId;
	public int columnOrder;
	public boolean mandatory;
	public String tag; //Novo!
	public boolean visible; //Novo!

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
		this.mandatory = original.mandatory;
		this.tag = original.tag;
		this.visible = original.visible;
	}
}
