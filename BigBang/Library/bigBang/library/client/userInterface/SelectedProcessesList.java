package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasCheckables;

public abstract class SelectedProcessesList<T extends ProcessBase> extends FilterableList<T> implements HasCheckables {
	
	public SelectedProcessesList(){
		ListHeader header = new ListHeader("Seleccionados");
		setHeaderWidget(header);
		showFilterField(false);
		setCheckable(true);
	}
	
	public abstract ListEntry<T> addEntry(T value);
}
