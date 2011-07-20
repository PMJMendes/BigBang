package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import bigBang.library.client.ValueWrapper;
import bigBang.library.client.userInterface.SearchPanelListEntry;
import bigBang.library.shared.SearchResult;
import bigBang.module.clientModule.shared.ClientStub;

public class ClientSearchPanelListEntry extends SearchPanelListEntry<SearchResult> {

	protected ValueWrapper<ClientStub> wrapper;
	protected static ValueChangeHandler<ClientStub> valueChangeHandler;
	
	public ClientSearchPanelListEntry(ValueWrapper<ClientStub> valueWrapper) {
		super(valueWrapper.getValue());
		
		if(valueChangeHandler == null) {
			valueChangeHandler = new ValueChangeHandler<ClientStub>() {

				@SuppressWarnings("unchecked")
				@Override
				public void onValueChange(ValueChangeEvent<ClientStub> event) {
					if(ClientSearchPanelListEntry.this.wrapper.getValue() == ((ValueWrapper<ClientStub>)event.getSource()).getValue()){
						ClientSearchPanelListEntry.this.setInfo(event.getValue());
					}
				}
			};
		}
		
		wrapper = valueWrapper;
		this.setHeight("40px");
		valueWrapper.addValueChangeHandler(valueChangeHandler);
	}
	
	public <I extends Object> void setInfo(I info) {
		ClientStub value = (ClientStub)info;
		setTitle(value.name);
		setText(value.clientNumber);
	};
	
}
