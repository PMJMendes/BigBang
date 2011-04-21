package bigBang.library.shared;

import java.io.Serializable;

public class Document
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String ownerId;
	public String docTypeId;
	public String text;
	public String mimeType;
	public String fileName;
	public String fileStorageId;
	public Parameter[] parameters;

	public Document()
	{
		parameters = new Parameter[0];
	}
}
