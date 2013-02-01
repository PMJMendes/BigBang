package bigBang.module.tasksModule.client.userInterface.view;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.TaskStub;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.tasksModule.client.userInterface.TaskSearchPanel;
import bigBang.module.tasksModule.client.userInterface.TaskSearchPanel.Entry;
import bigBang.module.tasksModule.client.userInterface.presenter.AgendaViewPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AgendaView extends View implements AgendaViewPresenter.Display{

	private final int LIST_WIDTH = 400; //px

	protected HasWidgets container;
	protected TaskSearchPanel searchPanel;
	protected ListHeader containerHeader;
	protected ExpandableListBoxFormField users;
	protected Button sendTask;
	
	public AgendaView() {
		
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		SplitLayoutPanel splitPanel = new SplitLayoutPanel();
		splitPanel.setSize("100%", "100%");

		SimplePanel tasksListWrapper = new SimplePanel();
		tasksListWrapper.setSize("100%", "100%");

		searchPanel = new TaskSearchPanel(){
			@Override
			protected void onAttach() {
				super.onAttach();
				doSearch();
			}
		};
		
		searchPanel.setSize("100%", "100%");
		tasksListWrapper.setWidget(searchPanel);

		splitPanel.addWest(tasksListWrapper, LIST_WIDTH);

		VerticalPanel contentWrapper = new VerticalPanel();
		contentWrapper.setSize("100%", "100%");

		containerHeader = new ListHeader();

		contentWrapper.add(this.containerHeader);

		SimplePanel previewPanel = new SimplePanel();
		previewPanel.setSize("100%", "100%");
		contentWrapper.add(previewPanel);
		contentWrapper.setCellHeight(previewPanel, "100%");

		previewPanel.setStyleName("emptyContainer");
		container = previewPanel;

		splitPanel.add(contentWrapper);

		wrapper.add(splitPanel);
		wrapper.setCellHeight(splitPanel, "100%");

		sendTask = new Button("Enviar Tarefa");
		users = new ExpandableListBoxFormField(BigBangConstants.EntityIds.USER, "");

		HorizontalPanel panel = new HorizontalPanel();

		panel.add(users);
		panel.add(sendTask);

		users.setVisible(false);
		sendTask.setVisible(false);
		panel.setCellVerticalAlignment(users, HasVerticalAlignment.ALIGN_MIDDLE);	
		panel.setCellVerticalAlignment(sendTask, HasVerticalAlignment.ALIGN_MIDDLE);		
		
		sendTask.setEnabled(false);
		
		users.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				sendTask.setEnabled(users.getValue() != null);
			}
		});
		
		containerHeader.setRightWidget(panel);
		
		searchPanel.doSearch(true);

	}
	
	@Override
	public HasValueSelectables<TaskStub> getTaskList() {
		return this.searchPanel;
	}

	@Override
	public void clear() {
		this.container.clear();
		this.containerHeader.setText("");
		users.clear();
	}

	@Override
	public void addTaskListEntry(TaskStub task) {
		TaskSearchPanel.Entry entry = new Entry(task);
		searchPanel.add(0, entry);
	}

	@Override
	public void updateTaskListEntry(TaskStub task) {
		for(ValueSelectable<TaskStub> entry : searchPanel){
			if(entry.getValue().id.equalsIgnoreCase(task.id)){
				entry.setValue(task);
				break;
			}
		}
	}

	@Override
	public void removeTaskListEntry(String taskId) {
		for(ValueSelectable<TaskStub> entry : searchPanel){
			if(entry.getValue().id.equalsIgnoreCase(taskId)){
				searchPanel.remove(entry);
				break;
			}
		}
	}

	@Override
	public void setScreenDescription(String description) {
		this.containerHeader.setText(description);
	}

	@Override
	public void setSendToUserVisible(boolean b){
		this.users.setVisible(b);
		this.sendTask.setVisible(b);
	}

	@Override
	public HasClickHandlers getSendTaskButton() {
		return sendTask;
	}

	@Override
	public String getSelectedUserId() {
		return users.getValue();
	}

	@Override
	public void refreshList() {
		searchPanel.doSearch();
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public HasWidgets getContainer(){
		return container;
	}
}
