package bigBang.library.shared;

import java.io.Serializable;

public class ZipCode
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String code;
	public String city;
	public String county;
	public String district;
	public String country;
	
	public ZipCode(){
		code = city = county = district = country = "";
	}
}
