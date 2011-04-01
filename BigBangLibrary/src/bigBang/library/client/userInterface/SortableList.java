package bigBang.library.client.userInterface;

import java.util.Collections;
import java.util.Comparator;

public abstract class SortableList <T> extends List<T> {

	protected Comparator<ListEntry<T>> comparator;
	
	public SortableList() {
		super();
		setComparator(new Comparator<ListEntry<T>>() {

			@Override
			public int compare(ListEntry<T> arg0, ListEntry<T> arg1) {
				int titleComp =arg0.getTitle().compareToIgnoreCase(arg1.getTitle());
				int textComp = arg0.getText().compareToIgnoreCase(arg1.getText());
				return Math.abs(titleComp) < Math.abs(textComp) ? titleComp : textComp;
			}
		});
	}
	
	public SortableList(Comparator<ListEntry<T>> comparator) {
		super();
		setComparator(comparator);
	}
	
	private void setComparator(Comparator<ListEntry<T>> comparator) {
		this.comparator = comparator;
	}

	public void sortListEntries() {
		/*if(comparator == null)
			return;
		Collections.sort(entries, comparator);
		render();*/ //TODO
	}

}
