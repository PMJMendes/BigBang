package bigBang.library.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ModuleInitializedEventHandler extends EventHandler {
	void onModuleInitialized(ModuleInitializedEvent event);
}