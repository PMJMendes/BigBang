package bigBang.library.client.event;

import java.util.Collection;

import bigBang.library.client.Selectable;

import com.google.gwt.event.shared.GwtEvent;

public class SelectionChangedEvent extends GwtEvent<SelectionChangedEventHandler> {

	public static Type<SelectionChangedEventHandler> TYPE = new Type<SelectionChangedEventHandler>();
	private Collection<? extends Selectable> selected;


	public <S extends Selectable> SelectionChangedEvent(Collection<S> selected) {
		this.selected = selected;
	}

	public Collection<? extends Selectable> getSelected(){
		return selected;
	}

	public Selectable getFirstSelected(){
		for(Selectable s : selected){
			return s;
		}
		return null;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SelectionChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectionChangedEventHandler handler) {
		handler.onSelectionChanged(this);
	}

}