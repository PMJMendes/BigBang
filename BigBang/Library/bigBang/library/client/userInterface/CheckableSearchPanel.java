package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.userInterface.view.SearchPanel;

public abstract class CheckableSearchPanel<T extends SearchResult> extends SearchPanel<T> {

	protected Set<String> markedForCheck;
	
	public CheckableSearchPanel(SearchDataBroker<T> broker) {
		super(broker);
		markedForCheck = new HashSet<String>();
		showSearchField(true);
		setCheckable(true);
	}

	public abstract ListEntry<T> addEntry(T value);
	
	@Override
	public void onResults(Collection<T> results) {
		for(T result : results){
			ListEntry<T> entry = addEntry(result);
			entry.setChecked(markedForCheck.contains(result.id.toLowerCase()), false);
		}
	}

	public void markForCheck(String id){
		id = id.toLowerCase();
		markedForCheck.add(id);
		for(ListEntry<T> entry : this){
			T value = entry.getValue();
			if(value.id.equalsIgnoreCase(id)){
				entry.setChecked(true, false);
			}
		}
	}

	public void markForCheck(Collection<String> ids) {
		for(String id : ids){
			markForUncheck(id);
		}
	}

	public void markForUncheck(String id){
		id = id.toLowerCase();
		markedForCheck.remove(id);
		for(ListEntry<T> entry : this){
			T value = entry.getValue();
			if(value.id.equalsIgnoreCase(id)){
				entry.setChecked(false, false);
			}
		}
	}

	public void markForUncheck(Collection<String> ids) {
		for(String id : ids){
			markForUncheck(id);
		}
	}
	
	public void unckeckAll(){
		for(ListEntry<T> entry : this){
			if(entry.isChecked()){
				entry.setChecked(false, true);
			}
		}
		this.markedForCheck.clear();
	}

	public boolean isMarkedForCheck(String id) {
		return this.markedForCheck.contains(id.toLowerCase());
	}
	
}
