package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.ListEntry;

public class QuoteRequestSelectButton extends ListEntry<QuoteRequestStub>{
	
	protected Label numberLabel;
	protected Label label;
	protected Image invalidIcon;

	public QuoteRequestSelectButton(QuoteRequestStub value) {
		super(value);
		setHeight("40px");
	}

	public <I extends Object> void setInfo(I info) {
		QuoteRequestStub value = (QuoteRequestStub)info;
		if(value == null){
			numberLabel.setText("");
			label.setText("");
			return;
		}
		if(numberLabel == null){
			numberLabel = getFormatedLabel();
			numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
			numberLabel.setWordWrap(false);
			label = getFormatedLabel();
			label.getElement().getStyle().setFontSize(11, Unit.PX);
			label.getElement().getStyle().setProperty("whiteSpace", "");
			label.setHeight("1.2em");
			Resources resource = GWT.create(Resources.class);
			invalidIcon = new Image(resource.invalidEntry());
			setLeftWidget(invalidIcon);
			setInvalid(false);
		}
		if(value.id != null){

			numberLabel.setText(value.id.equalsIgnoreCase("new") ? "Nova Consulta" : "Consulta #" + value.processNumber);
			label.setText("(Todas as unidades de risco)");

			VerticalPanel panel = new VerticalPanel();
			panel.add(numberLabel);
			panel.add(label);

			setWidget(panel);
		}
		else{
			numberLabel.setText("");
			label.setText("");
		}
	}

	public void setInvalid(boolean b) {
		this.invalidIcon.setVisible(b);
	}
}
