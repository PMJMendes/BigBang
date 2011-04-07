package bigBang.library.shared;

import java.io.Serializable;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	String name;
	Address[] addresses;
	ContactInfo[] info;
	
	public Contact(){
		addresses = new Address[0];
		info = new ContactInfo[0];
	}
}
