package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

public class Tax
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public double value;
	public String currencyId;
}
