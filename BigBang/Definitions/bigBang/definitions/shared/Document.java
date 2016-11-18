package bigBang.definitions.shared;

import java.io.Serializable;

public class Document
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String ownerTypeId;
	public String ownerId;
	public String docTypeId;
	public String docTypeLabel;
	public String creationDate;
	public String text;
	public boolean hasFile;
	public String mimeType;
	public String fileName;
	public String fileStorageId;
	public ScanHandle source;
	public String emailId; //Attached document helper tag, client-side only.
	public String emailAttId; //Attached document helper tag, client-side only.
	public String emailFolderId; //Attached document helper tag, client-side only.
	public DocInfo[] parameters;
	public boolean displayAtPortal;

	public Document()
	{
		source = null;
		parameters = new DocInfo[0];
	}

	public Document(Document orig)
	{
		int i;

		this.id = orig.id;
		this.name = orig.name;
		this.ownerTypeId = orig.ownerTypeId;
		this.ownerId = orig.ownerId;
		this.docTypeId = orig.docTypeId;
		this.docTypeLabel = orig.docTypeLabel;
		this.creationDate = orig.creationDate;
		this.text = orig.text;
		this.hasFile = orig.hasFile;
		this.mimeType = orig.mimeType;
		this.fileName = orig.fileName;
		this.fileStorageId = orig.fileStorageId;
		this.source = orig.source;
		this.emailAttId = orig.emailAttId;

		if ( orig.parameters == null )
			this.parameters = null;
		else
		{
			this.parameters = new DocInfo[orig.parameters.length];
			for ( i = 0; i < this.parameters.length; i++ )
				this.parameters[i] = (orig.parameters[i] == null ? null : new DocInfo(orig.parameters[i]));
		}
	}
}
