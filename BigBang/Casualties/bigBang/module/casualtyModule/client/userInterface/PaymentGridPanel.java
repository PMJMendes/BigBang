package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PaymentGridPanel extends View{

	protected Grid grid;

	public PaymentGridPanel() {
		VerticalPanel wrapper = new VerticalPanel();
		initWidget(wrapper);

		wrapper.setSize("100%", "100%");
		grid = new Grid();

		grid.setStyleName("formPanel");

		wrapper.add(grid);
		grid.setSize("100%", "100%");


	}

	@Override
	protected void initializeView() {
		return;
	}

	public void setValue(MedicalFile.MedicalDetail[] details){

		grid.resize(details.length+1, 4);

		setTitles();

		NumberFormat nf = NumberFormat.getFormat("#,##0.00");
		
		for(int i = 1; i<=details.length; i++){
			grid.setWidget(i, 0, new Label(details[i-1].startDate));
			((Label)grid.getWidget(i, 0)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setWidget(i, 1, new Label(details[i-1].endDate));
			((Label)grid.getWidget(i, 1)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setWidget(i, 2, new Label(details[i-1].disabilityTypeLabel));
			((Label)grid.getWidget(i, 2)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setWidget(i, 3, new Label(details[i-1].benefits != null ? nf.format(details[i-1].benefits) : ""));
			((Label)grid.getWidget(i, 3)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

			if((i % 2) != 0){
				grid.getRowFormatter().getElement(i).getStyle().setBackgroundColor("#CCC");
			}
		}



	}

	private void setTitles() {
		grid.setWidget(0,0, new Label("Início"));
		((Label)grid.getWidget(0, 0)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0,1, new Label("Fim"));
		((Label)grid.getWidget(0, 1)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0,2, new Label("Tipo"));
		((Label)grid.getWidget(0, 2)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0,3, new Label("Valor (€)"));
		((Label)grid.getWidget(0, 3)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		grid.getColumnFormatter().setWidth(0, "100px");
		grid.getColumnFormatter().setWidth(1, "100px");
		grid.getColumnFormatter().setWidth(2, "100px");
		grid.getColumnFormatter().setWidth(3, "100px");

	}

}
