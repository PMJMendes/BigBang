package bigBang.module.generalSystemModule.shared;

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
}
