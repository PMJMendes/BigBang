package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.Collection;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormViewSection;

public class AddSubLineSection extends FormViewSection {

	protected ExpandableListBoxFormField category, line, subLine;
	protected Button confirmButton = new Button("Acrescentar");

	public AddSubLineSection() {
		super("Acrescentar Ramo à Consulta de Mercado");
		category = new ExpandableListBoxFormField(BigBangConstants.EntityIds.CATEGORY, "Categoria");
		category.setEditable(false);
		line = new ExpandableListBoxFormField("Ramo");
		line.setEditable(false);
		subLine = new ExpandableListBoxFormField("Modalidade");
		subLine.setEditable(false);
		confirmButton.setEnabled(false);
		confirmButton.getElement().getStyle().setMarginTop(20, Unit.PX);

		ValueChangeHandler<String> valueChangeHandler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String value = event.getValue();
				value = value != null && value.isEmpty() ? null : value;
				if(event.getSource() == category) {
					line.setListId(value == null ? null : (BigBangConstants.EntityIds.LINE + "/" + value), new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							line.setValue(null, true);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
						}
					});
				}
				if(event.getSource() == line) {
					subLine.setListId(value == null ? null : (BigBangConstants.EntityIds.SUB_LINE + "/" + value), new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							subLine.setValue(null, true);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
						}
					});
				}
				if(event.getSource() == subLine) {
					confirmButton.setEnabled(value != null);
				}
			}
		};
		category.addValueChangeHandler(valueChangeHandler);
		line.addValueChangeHandler(valueChangeHandler);
		subLine.addValueChangeHandler(valueChangeHandler);
		
		addFormField(category, true);
		addFormField(line, true);
		addFormField(subLine, true);
		addWidget(confirmButton);
	}

	public void clear() {
		category.setValue(null, true);
	}

	public String getSubLineId() {
		return subLine.getValue();
	}
	
	public HasClickHandlers getConfirmButton(){
		return this.confirmButton;
	}
	
}
