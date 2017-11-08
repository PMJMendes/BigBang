package bigBang.library.client.userInterface.reports;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.PrintSet;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;

public class PrintSetReportPanel extends List<PrintSet> {

	public static class Entry extends ListEntry<PrintSet>{

		protected Label date;
		protected Label printDate;
		protected Label extraInfo1;
		protected Label extraInfo2;
		protected Label userName;

		protected boolean initialized;

		public Entry(PrintSet value) {
			super(value);
			setHeight("45px");
		}

		public <I extends Object> void setInfo(I info) {
			date = getFormatedLabel();
			printDate = getFormatedLabel();
			userName = getFormatedLabel();
			extraInfo1 = getFormatedLabel();
			extraInfo2 = getFormatedLabel();
			
			HorizontalPanel cell = new HorizontalPanel();
			VerticalPanel dates = new VerticalPanel();
			VerticalPanel userInfo = new VerticalPanel();
			HorizontalPanel extraInfo = new HorizontalPanel();
			VerticalPanel cellWithInfo = new VerticalPanel();
			
			if(!initialized) {
				date = getFormatedLabel();
				printDate = getFormatedLabel();
				userName = getFormatedLabel();
				extraInfo1 = getFormatedLabel();
				extraInfo2 = getFormatedLabel();
				
				initialized = true;
			}
			dates.add(date);
			dates.add(printDate);
			userInfo.add(userName);
			extraInfo.add(extraInfo1);
			extraInfo.add(extraInfo2);
			dates.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
			userInfo.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
			extraInfo.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
			cell.add(userInfo);
			cell.add(dates);
			cell.setCellHorizontalAlignment(dates, HasHorizontalAlignment.ALIGN_RIGHT);
			cell.setCellHorizontalAlignment(userInfo, HasHorizontalAlignment.ALIGN_LEFT);
			extraInfo.setCellHorizontalAlignment(extraInfo1, HasHorizontalAlignment.ALIGN_LEFT);
			extraInfo.setCellHorizontalAlignment(extraInfo2, HasHorizontalAlignment.ALIGN_RIGHT);
			extraInfo.setSize("100%", "100%");
			cell.setSize("100%", "100%");
			cellWithInfo.setSize("100%", "100%");
			cellWithInfo.add(cell);
			cellWithInfo.add(extraInfo);
			
			PrintSet set = (PrintSet) info;
			this.date.setText(set.date);
			this.setTitle(set.userName);
			if(set.printDate == null){
				printDate.setVisible(false);
			}else{
				this.printDate.setText("Impresso a " + set.printDate);
				printDate.setVisible(true);
			}
			userName.setText(set.userName);
			extraInfo1.setText(set.extraInfo1);
			extraInfo2.setText(set.extraInfo2);
			
			setWidget(cellWithInfo);
			
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

	public void setPrintSets(PrintSet[] printSets){
		for(PrintSet printSet : printSets)
			this.add(new Entry(printSet));
	}

	public PrintSet getSelectedPrintSet(){

		if(!this.getSelected().isEmpty()){
			return (PrintSet)((Entry) this.getSelected().toArray()[0]).getValue();
		}
		return null;
	}

}
