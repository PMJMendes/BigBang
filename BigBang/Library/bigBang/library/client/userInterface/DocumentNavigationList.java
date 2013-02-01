package bigBang.library.client.userInterface;

import bigBang.library.shared.DocuShareItem;

public class DocumentNavigationList extends FilterableList<DocuShareItem> {

	private String location;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	protected class DocumentEntry extends ListEntry<DocuShareItem> {

		public DocumentEntry(DocuShareItem value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			DocuShareItem item = (DocuShareItem) info;
			setTitle(item.desc);
		};
	}
	
	protected class FolderEntry extends NavigationListEntry<DocuShareItem> {

		public FolderEntry(DocuShareItem value) {
			super(value);
		}
		
		public <I extends Object> void setInfo(I info) {
			DocuShareItem item = (DocuShareItem) info;
			setTitle(item.desc);
		};
		
	}
	
	public void addEntryForItem(DocuShareItem item){
		if(item.directory){
			add(new FolderEntry(item));
		}else{
			add(new DocumentEntry(item));
		}
	}
}
