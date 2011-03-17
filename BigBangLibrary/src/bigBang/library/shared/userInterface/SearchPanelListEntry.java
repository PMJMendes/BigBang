package bigBang.library.shared.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;

public class SearchPanelListEntry extends ListEntry {

	private CheckBox checkBox;
	
	public SearchPanelListEntry(Object value) {
		super(value);
		
		this.checkBox = new CheckBox();
		this.checkBox.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				event.stopPropagation();
			}
		});

		this.checkBox.setTitle("Seleccionar");
		this.setLeftWidget(this.checkBox);
		
		this.setDoubleClickable(true);
	}
	
	public void setCheckable(boolean checkable){
		this.checkBox.setVisible(checkable);
		this.checkBox.setEnabled(checkable);
	}
	
	public void setChecked(boolean checked) {
		this.checkBox.setValue(checked);
	}
	
	public boolean isChecked(){
		return this.checkBox.getValue();
	}
	
	@Override
	public void onDoubleClick(Event event){
		this.setChecked(!isChecked());
		event.stopPropagation();
		event.preventDefault();
	}

}
