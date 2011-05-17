package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.library.client.ValueWrapper;
import bigBang.library.client.userInterface.SearchPanelListEntry;
import bigBang.library.shared.SearchResult;
import bigBang.module.clientModule.shared.ClientStub;

public class ClientSearchPanelListEntry extends SearchPanelListEntry<SearchResult> {

	protected ValueWrapper<ClientStub> wrapper;
	
	public ClientSearchPanelListEntry(ValueWrapper<ClientStub> valueWrapper) {
		super(valueWrapper.getValue());
		wrapper = valueWrapper;
		this.setHeight("40px");
		valueWrapper.addValueChangeHandler(new ValueChangeHandler<ClientStub>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<ClientStub> event) {
				setInfo(event.getValue());
			}
		});
	}
	
	public <I extends Object> void setInfo(I info) {
		ClientStub value = (ClientStub)info;
		setTitle(value.name);
		setText(value.clientNumber);
	};
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
	}
	
}
