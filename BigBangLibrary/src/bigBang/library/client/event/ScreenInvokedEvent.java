package bigBang.library.client.event;

import bigBang.library.shared.Parameter;

import com.google.gwt.event.shared.GwtEvent;

public class ScreenInvokedEvent extends GwtEvent<ScreenInvokedEventHandler> {
	
	public static Type<ScreenInvokedEventHandler> TYPE = new Type<ScreenInvokedEventHandler>();
	
	private String screenId;
	private String sectionId;
	private Parameter[] parameters;
	
	public ScreenInvokedEvent(String screenId, String sectionId, Parameter[] parameters){
		this.setScreenId(screenId);
		this.setSectionId(sectionId);
		this.setParameters(parameters);
	}
	
	@Override
	public Type<ScreenInvokedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScreenInvokedEventHandler handler) {
		handler.onScreenInvoked(this);
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

}
