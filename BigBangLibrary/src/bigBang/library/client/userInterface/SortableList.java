package bigBang.library.client.userInterface;

import java.util.Collections;
import java.util.Comparator;

public abstract class SortableList<T> extends List<T> {

	protected Comparator<ListEntry<T>> comparator;
	
	public SortableList(Comparator<ListEntry<T>> comparator) {
		super();
		setComparator(comparator);
	}
	
	private void setComparator(Comparator<ListEntry<T>> comparator) {
		this.comparator = comparator;
	}

	public void sortListEntries() {
		if(comparator == null)
			return;
		Collections.sort(listEntries, comparator);
		setListEntries(listEntries);
	}

}
