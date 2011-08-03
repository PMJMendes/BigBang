package bigBang.definitions.client.types;

import java.io.Serializable;

public class Tax
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String coverageId;
	public String currencyId;
	public double value;
	
	public Tax(){}
	
	public Tax(Tax original) {
		this.id = original.id;
		this.name = original.name;
		this.coverageId = original.coverageId;
		this.currencyId = original.currencyId;
		this.value = original.value;
	}
}
