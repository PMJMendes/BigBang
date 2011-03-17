package bigBang.module.tasksModule.client.userInterface;

import bigBang.library.shared.userInterface.ListEntry;
import bigBang.module.tasksModule.shared.Task;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class TaskListEntry extends ListEntry {

	public TaskListEntry(Task value) {
		super(value);
		if(value == null)
			return;
		setTitle(value.description);
		setText("asfhdifsdbspgad fd dg ag f f hf   fahfdhdfhadhad ghhahdfhhadf dfhfgad fhadh ");
		Button w = new Button("click");
		w.setSize("50px", "50px");
		setRightWidget(w);
		Button x = new Button("click");
		x.setSize("50px", "50px");
		setLeftWidget(x);
		
		setHeight("70px");
		//this.setRightClickable(true);
	}


}
