package bigBang.library.client.userInterface.reports;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.PrintSet;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;

public class PrintSetReportPanel extends List<PrintSet> {

	public static class Entry extends ListEntry<PrintSet>{

		protected Label date;
		protected Label printDate;

		protected boolean initialized;

		public Entry(PrintSet value) {
			super(value);
			setHeight("40px");
		}

		public <I extends Object> void setInfo(I info) {
			if(!initialized){
				date = getFormatedLabel();
				printDate = getFormatedLabel();
				
				VerticalPanel rightContainer = new VerticalPanel();
				rightContainer.add(date);
				rightContainer.add(printDate);
				rightContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

				rightContainer.setSize("100%", "100%");
				setRightWidget(rightContainer);
				initialized = true;
			}

			PrintSet set = (PrintSet) info;
			this.date.setText(set.date);
			this.setTitle(set.userName);
			if(set.printDate == null){
				printDate.setVisible(false);
			}else{
				this.printDate.setText("Impresso a " + set.printDate);
				printDate.setVisible(true);
			}
			setSelected(this.isSelected(), false);
		}

		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);

			if(!initialized){
				return;
			}

			if(selected){
				date.getElement().getStyle().setColor("white");
				this.getElement().getStyle().setColor("white");
				printDate.getElement().getStyle().setColor("white");
			}
			else if(this.value.printDate == null){
				date.getElement().getStyle().setColor("#0066ff");
				this.getElement().getStyle().setColor("black");
			}else{
				date.getElement().getStyle().setColor("gray");
				this.getElement().getStyle().setColor("gray");
				printDate.getElement().getStyle().setColor("gray");
			}
		}
	}

	@Override
	protected void initializeView() {
		return;
	}

	public void setPrintSets(PrintSet[] printSets){
		for(PrintSet printSet : printSets)
			this.add(new Entry(printSet));
	}

	public PrintSet getSelectedPrintSet(){

		if(!this.getSelected().isEmpty()){
			return (PrintSet) this.getSelected().toArray()[0];
		}
		return null;
	}

}
