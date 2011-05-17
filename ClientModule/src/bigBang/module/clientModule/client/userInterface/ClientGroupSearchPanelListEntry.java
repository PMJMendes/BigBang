package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.library.client.ValueWrapper;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.shared.SearchResult;
import bigBang.module.clientModule.shared.ClientGroupStub;

public class ClientGroupSearchPanelListEntry extends ListEntry<SearchResult> {

	protected ValueWrapper<ClientGroupStub> wrapper;
	
	public ClientGroupSearchPanelListEntry(ValueWrapper<ClientGroupStub> valueWrapper) {
		super(valueWrapper.getValue());
		valueWrapper.addValueChangeHandler(new ValueChangeHandler<ClientGroupStub>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<ClientGroupStub> event) {
				setInfo(event.getValue());
			}
		});
	}

	public <I extends Object> void setInfo(I info) {
		ClientGroupStub value = (ClientGroupStub) info;
		setTitle(value.name);
		setText("group hierarchy");
	};
}
