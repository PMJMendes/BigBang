package bigBang.library.shared;

import java.io.Serializable;

public class Document implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	Parameter[] parameters;
	String text;
	String mimeType;
	String fileStorageId;
	
	public Document(){
		parameters = new Parameter[0];
	}

}
