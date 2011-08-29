package bigBang.library.client.event;

import java.util.Collection;

import bigBang.library.client.Checkable;

import com.google.gwt.event.shared.GwtEvent;

public class CheckedSelectionChangedEvent extends GwtEvent<CheckedSelectionChangedEventHandler> {

	public static Type<CheckedSelectionChangedEventHandler> TYPE = new Type<CheckedSelectionChangedEventHandler>();

	protected Collection<Checkable> checkedList;

	public CheckedSelectionChangedEvent(Collection<Checkable> list) {
		this.checkedList = list;
	}

	public Collection<Checkable> getChecked(){
		return this.checkedList;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CheckedSelectionChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CheckedSelectionChangedEventHandler handler) {
		handler.onCheckedSelectionChanged(this);
	}

}
