package bigBang.module.tasksModule.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class NumberOfTasksUpdateEvent extends GwtEvent<NumberOfTasksUpdateEventHandler> {

	public static Type<NumberOfTasksUpdateEventHandler> TYPE = new Type<NumberOfTasksUpdateEventHandler>();
	
	private int value;
	
	public NumberOfTasksUpdateEvent(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NumberOfTasksUpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NumberOfTasksUpdateEventHandler handler) {
		handler.onUpdate(this);
	}

}
