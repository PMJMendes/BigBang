package bigBang.library.shared;

import java.io.Serializable;

public class ScanItem
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public boolean directory;
	public String handle;
	public String fileName;
	public String mimeType;
}
