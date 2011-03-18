package bigBang.module.tasksModule.client.userInterface;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.tasksModule.shared.Task;

import com.google.gwt.user.client.ui.Button;

public class TaskListEntry extends ListEntry<Task> {

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
