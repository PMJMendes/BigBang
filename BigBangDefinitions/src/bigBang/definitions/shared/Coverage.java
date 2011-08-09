package bigBang.definitions.shared;

import java.io.Serializable;

public class Coverage
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String subLineId;
	public Tax[] taxes;

	public Coverage()
	{
		taxes = new Tax[0];
	}
	
	public Coverage(Coverage original){
		this.id = original.id;
		this.name = original.name;
		this.subLineId = original.subLineId;
		this.taxes = original.taxes;
	}
}
