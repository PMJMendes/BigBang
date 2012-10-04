package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Category;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.library.client.FormField;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;

public class MediatorCategoryFormSection extends CollapsibleFormViewSection {

	protected CoverageBroker broker;
	protected Map<String, Double> values;
	protected Map<String, FormField<Double>> valueFields;
	
	public MediatorCategoryFormSection(Category category, Map<String, Double> values) {
		super("");
		broker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		this.values = values;
		this.valueFields = new HashMap<String, FormField<Double>>();
		setCategory(category);
	}

	public void setCategory(Category category) {
		this.setHeaderText(category.value);
		broker.getLinesForCategory(category.id, new ResponseHandler<Line[]>() {
			
			@Override
			public void onResponse(Line[] response) {
				setLines(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				//TODO
			}
		});
	}
	
	protected void setLines(Line[] lines){
		for(Line line : lines) {
			addLine(line);
		}
	}
	
	protected void addLine(Line line){
		Label lineDesig = new Label();
		lineDesig.setText("Ramo " + line.name);
		lineDesig.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		lineDesig.getElement().getStyle().setProperty("clear", "both");
		
		addWidget(lineDesig, false);
		
		for(SubLine subLine : line.subLines) {
			addSubLine(subLine);
		}
	}
	
	protected void addSubLine(SubLine subLine){
		NumericTextBoxFormField field = new NumericTextBoxFormField(subLine.name, false);
		field.setUnitsLabel("%");
		field.setReadOnlyInternal(this.readOnly);
		addFormField(field, true);
		this.valueFields.put(subLine.id, field);
	}
	
	public Map<String, Double> getValues(){
		this.values.clear();
		
		for(String key : valueFields.keySet()) {
			Double value = valueFields.get(key).getValue();
			if(value != null) {
				this.values.put(key, value);
			}
		}
		
		return this.values;
	}
	
}
