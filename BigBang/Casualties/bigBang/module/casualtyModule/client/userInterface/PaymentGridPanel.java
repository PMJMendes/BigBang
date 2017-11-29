package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.shared.MedicalFile;
import bigBang.library.client.Session;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PaymentGridPanel extends View{

	protected Grid grid;
	protected double totalBenefits;  
	protected ScrollPanel scrollWrapper;

	public PaymentGridPanel() {
		
		ScrollPanel sc = new ScrollPanel();
		this.scrollWrapper = sc;
		sc.getElement().getStyle().setProperty("overflowX", "hidden");
		
		VerticalPanel wrapper = new VerticalPanel();
		
		
		sc.setSize("100%", "100%");
		sc.add(wrapper);
		sc.setStyleName("formPanel");	

		wrapper.setSize("100%", "100%");
		grid = new Grid();

		grid.setStyleName("formPanel");

		wrapper.add(grid);
		grid.setSize("100%", "100%");

		initWidget(sc);
	}

	@Override
	protected void initializeView() {
		return;
	}

	public void setValue(MedicalFile.MedicalDetail[] details){

		grid.resize(details.length+2, 5);
		
		totalBenefits = 0;

		setTitles();
		
		int i;

		NumberFormat nf = NumberFormat.getFormat("#,##0.00");
		
		for(i = 1; i<=details.length; i++){
			grid.setWidget(i, 0, new Label(details[i-1].startDate));
			((Label)grid.getWidget(i, 0)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setWidget(i, 1, new Label(details[i-1].endDate));
			((Label)grid.getWidget(i, 1)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setWidget(i, 2, new Label(details[i-1].disabilityTypeLabel));
			((Label)grid.getWidget(i, 2)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setWidget(i, 3, new Label(details[i-1].percentDisability != null ? details[i-1].percentDisability.toString() + "%" : "-%"));
			((Label)grid.getWidget(i, 3)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			grid.setWidget(i, 4, new Label(details[i-1].benefits != null ? nf.format(details[i-1].benefits) : ""));
			((Label)grid.getWidget(i, 4)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

			if((i % 2) != 0){
				grid.getRowFormatter().getElement(i).getStyle().setBackgroundColor("#CCC");
			}
			
			if (details[i-1].benefits!=null) {
				totalBenefits += details[i-1].benefits;
			}
		}

		// now for the sum
		grid.setWidget(i, 0, new Label("TOTAL"));
		((Label)grid.getWidget(i, 0)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(i, 1, new Label(""));
		((Label)grid.getWidget(i, 1)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(i, 2, new Label(""));
		((Label)grid.getWidget(i, 2)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(i, 3, new Label(""));
		((Label)grid.getWidget(i, 3)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		grid.setWidget(i, 4, new Label(nf.format(totalBenefits)));
		((Label)grid.getWidget(i, 4)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		grid.getRowFormatter().getElement(i).getStyle().setBackgroundColor("#9FF");

	}

	private void setTitles() {
		grid.setWidget(0,0, new Label("Início"));
		((Label)grid.getWidget(0, 0)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0,1, new Label("Fim"));
		((Label)grid.getWidget(0, 1)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0,2, new Label("Tipo"));
		((Label)grid.getWidget(0, 2)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0,3, new Label("% Inval"));
		((Label)grid.getWidget(0, 3)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		grid.setWidget(0,4, new Label("Valor (" + Session.getCurrency() + ")"));
		((Label)grid.getWidget(0, 4)).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		grid.getColumnFormatter().setWidth(0, "100px");
		grid.getColumnFormatter().setWidth(1, "100px");
		grid.getColumnFormatter().setWidth(2, "100px");
		grid.getColumnFormatter().setWidth(3, "100px");
		grid.getColumnFormatter().setWidth(4, "100px");
	}

}
