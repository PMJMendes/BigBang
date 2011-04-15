package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.Line;

public class LineForm extends FormView<Line> {

	private TextBoxFormField name;
	private ExpandableListBoxFormField category;
	
	private Line line;
	
	public LineForm(){
		addSection("Informação Geral");
		
		name = new TextBoxFormField("Nome");
		category = new ExpandableListBoxFormField("", "Categoria", null);
	}
	
	@Override
	public Line getInfo() {
		line.name = name.getValue();
		line.categoryId = category.getValue();
		return line;
	}

	@Override
	public void setInfo(Line info) {
		this.line = info != null ? (Line) info : new Line();
		this.name.setValue(info.name);
		this.category.setValue(info.categoryId);
	}

}
