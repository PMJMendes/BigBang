package bigBang.library.shared;

import java.io.Serializable;

public class MailItemStub
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public boolean isFolder;
	public boolean isFromMe;
	public String subject;
	public String from;
	public String timestamp;
	public int attachmentCount;
	public String bodyPreview;
	public String folderId;
	public String parentFolderId; 
	public boolean isParentFolder;
	public boolean isMoreMailsButton;
}
