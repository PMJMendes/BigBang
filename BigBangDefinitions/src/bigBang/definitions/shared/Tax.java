package bigBang.definitions.shared;

import java.io.Serializable;

public class Tax
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String coverageId;
	public String currencyId; // Isto devia chamar-se fieldTypeId
	public double value; // Default value
	public String unitsLabel; // Novo!
	public boolean variesByObject; // Novo!
	public boolean variesByExercise;  // Novo!
	public String refersToEntityId;  // Novo!
	public int columnOrder; // Novo!

	public Tax(){}
	
	public Tax(Tax original) {
		this.id = original.id;
		this.name = original.name;
		this.coverageId = original.coverageId;
		this.currencyId = original.currencyId;
		this.value = original.value;
	}
}
