package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.Line;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter.Action;

public class LineForm extends FormView<Line> {
	
	private TextBoxFormField name;
	private ExpandableListBoxFormField category;
	private Line line;
	private ActionInvokedEventHandler<Action> actionHandler;
	
	public LineForm(){
		
		addSection("Ramo");
		name = new TextBoxFormField("Nome");
		category = new ExpandableListBoxFormField(ModuleConstants.ListIDs.LineCategories, "Categoria");
		addFormField(name);
		addFormField(category);
	}

	protected void onDelete() {
		fireAction(Action.DELETE_LINE);
		
	}

	@Override
	public Line getInfo() {
		if(this.line == null)
			this.line = new Line();
		line.name = name.getValue();
		line.categoryId = category.getValue();
		line.categoryName = category.getSelectedItemText();
		return line;
	}

	@Override
	public void setInfo(Line info) {
		this.line = info != null ? (Line) info : new Line();
		this.name.setValue(info.name);
		this.category.setValue(info.categoryId);
	}
	
	public void setValue(Line info){
		super.setValue(info);
		this.line = info != null ? (Line) info : new Line();
		this.name.setValue(info.name);
		this.category.setValue(info.categoryId);
	}
	
	public Line getValue(){
		return line;
	}

	@Override
	public void clearInfo() {
		line = new Line();
		super.clearInfo();
	}
	
	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}
	
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}
	
	@Override
	public void setReadOnly(boolean readOnly){
		super.setReadOnly(readOnly);
	}

}
