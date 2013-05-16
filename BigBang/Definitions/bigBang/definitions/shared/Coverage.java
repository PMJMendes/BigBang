package bigBang.definitions.shared;

import java.io.Serializable;

public class Coverage
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String subLineId;
	public boolean isMandatory;
	public boolean isHeader;
	public String tag; //Novo!
	public Integer order;
	public Tax[] taxes;

	public Coverage()
	{
		taxes = new Tax[0];
	}
	
	public Coverage(Coverage original){
		this.id = original.id;
		this.name = original.name;
		this.subLineId = original.subLineId;
		this.isMandatory = original.isMandatory;
		this.isHeader = original.isHeader;
		this.tag = original.tag;
		this.order = original.order;
		this.taxes = original.taxes;
	}
}
