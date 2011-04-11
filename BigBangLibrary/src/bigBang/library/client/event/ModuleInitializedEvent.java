package bigBang.library.client.event;

import bigBang.library.client.Module;

import com.google.gwt.event.shared.GwtEvent;

public class ModuleInitializedEvent extends GwtEvent<ModuleInitializedEventHandler> {

	public static Type<ModuleInitializedEventHandler> TYPE = new Type<ModuleInitializedEventHandler>();
	private Module module;
	
	public ModuleInitializedEvent(Module module) {
		this.module = module;
	}
	
	public Module getModule(){
		return module;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ModuleInitializedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public void dispatch(ModuleInitializedEventHandler handler) {
		handler.onModuleInitialized(this);
	}

}
