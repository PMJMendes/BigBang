package bigBang.library.shared;

import java.io.Serializable;

public class AttachmentStub
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String fileName;
	public String mimeType;
	public long size;
}
