package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.Line;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

public class LineForm extends FormView<Line> {
	
	private TextBoxFormField name;
	private ExpandableListBoxFormField category;
	private Line line;
	
	public LineForm(){
		
		addSection("Detalhes do ramo");
		name = new TextBoxFormField("Nome");
		category = new ExpandableListBoxFormField(ModuleConstants.ListIDs.LineCategories, "Categoria");
		category.allowEdition(false);
		addFormField(name);
		addFormField(category);
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

	@Override
	public void clearInfo() {
		line = new Line();
		super.clearInfo();
	}
	
	@Override
	public void setReadOnly(boolean readOnly){
		super.setReadOnly(readOnly);
	}

}
