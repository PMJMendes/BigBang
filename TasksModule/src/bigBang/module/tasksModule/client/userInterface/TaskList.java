package bigBang.module.tasksModule.client.userInterface;

import bigBang.definitions.shared.Task;
import bigBang.library.client.userInterface.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class TaskList extends List<Task> {

	public TaskList(){
		super();
		HorizontalPanel topWidgetWrapper = new HorizontalPanel();
		topWidgetWrapper.setSize("100%", "50px");
		
		Button dateOrderButton = new Button();
		dateOrderButton.setSize("100%", "50px");
		dateOrderButton.getElement().getStyle().setMargin(0, Unit.PX);
		dateOrderButton.setHTML("<img src=\"images/barClockIcon1.png\" style=\"width:30px; height:30px\"/>");
		topWidgetWrapper.add(dateOrderButton);
		topWidgetWrapper.setCellWidth(dateOrderButton, "50%");
		
		Button processOrderButton = new Button();
		processOrderButton.setSize("100%", "50px");
		processOrderButton.getElement().getStyle().setMargin(0, Unit.PX);
		topWidgetWrapper.add(processOrderButton);
		topWidgetWrapper.setCellWidth(processOrderButton, "50%");
		
		setHeaderWidget(topWidgetWrapper);
		
		updateFooterLabel();
	}
	
	/*@Override
	public void setEntries(Collection<Task> entries) {
		for(Task o : entries){
			final TaskListEntry entry = new TaskListEntry((Task)o);
			super.addListEntry(entry);
			entry.getLeftWidget().addDomHandler(new ClickHandler() {
				
				public void onClick(ClickEvent event) {
					GWT.log("hello");
					removeListEntry(entry);
				}
			}, ClickEvent.getType());
		}
		this.updateFooterLabel();
	}*/
	
	private void updateFooterLabel() {
		int nEntries = size();
		setFooterText((nEntries == 0 ? "Sem" : nEntries) + " tarefas por realizar");
	}

}
