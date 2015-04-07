package bigBang.library.client.userInterface;

import bigBang.library.shared.DocuShareItem;
import bigBang.library.shared.ScanItem;

public class DocumentNavigationList extends FilterableList<ScanItem> {

	private String location;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	protected class DocumentEntry extends ListEntry<ScanItem> {

		public DocumentEntry(ScanItem value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			ScanItem item = (ScanItem) info;
			if (item instanceof DocuShareItem)
				setTitle(((DocuShareItem)item).desc);
			else
				setTitle(item.fileName);
		};
	}
	
	protected class FolderEntry extends NavigationListEntry<ScanItem> {

		public FolderEntry(ScanItem value) {
			super(value);
		}
		
		public <I extends Object> void setInfo(I info) {
			ScanItem item = (ScanItem) info;
			if (item instanceof DocuShareItem)
				setTitle(((DocuShareItem)item).desc);
			else
				setTitle(item.fileName);
		};
		
	}
	
	public void addEntryForItem(ScanItem item){
		if(item.directory){
			add(new FolderEntry(item));
		}else{
			add(new DocumentEntry(item));
		}
	}
}
