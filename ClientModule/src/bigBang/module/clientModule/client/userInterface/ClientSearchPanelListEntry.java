package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.ValueWrapper;
import bigBang.library.client.userInterface.SearchPanelListEntry;

public class ClientSearchPanelListEntry extends SearchPanelListEntry<ClientStub> {

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
		if(value.id != null){
			Label clientNumberLabel = new Label(value.clientNumber);
			clientNumberLabel.setWidth("40px");
			setLeftWidget(clientNumberLabel);
			setTitle(value.name);
			setText(value.groupName);
			this.textLabel.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
		}else{
			setTitle("Novo Cliente");
		}
	};

	@Override
	public void setValue(ClientStub value) {
		super.setValue(value);
		if(this.wrapper != null)
			this.wrapper.setValue((ClientStub) value, false);
	}

	public ValueWrapper<ClientStub> getValueWrapper(){
		return this.wrapper;
	}

}
