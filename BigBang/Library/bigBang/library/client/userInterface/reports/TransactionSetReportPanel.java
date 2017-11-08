package bigBang.library.client.userInterface.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.TransactionSet;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;

public class TransactionSetReportPanel extends List<TransactionSet> {

	static Resources resources = GWT.create(Resources.class);
	
	public static class Entry extends ListEntry<TransactionSet>{

		protected Label date;
		protected Label userName;
		protected Image isComplete;
		protected boolean initialized;
		protected Label company;
		
		public Entry(TransactionSet value) {
			super(value);
			setHeight("55px");
		}
		
		public <I extends Object> void setInfo(I info) {
			if(!initialized){
				userName = getFormatedLabel();
				userName.getElement().getStyle().setFontSize(14, Unit.PX);
				company = getFormatedLabel();
				//company.getElement().getStyle().setFontSize(14, Unit.PX);
				date = getFormatedLabel();
				isComplete = new Image(resources.completeIcon());
				VerticalPanel container = new VerticalPanel();
				container.setSize("100%", "100%");
				container.add(userName);
				container.add(company);
				container.add(date);
				container.setCellVerticalAlignment(userName, HasVerticalAlignment.ALIGN_TOP);
				container.setCellVerticalAlignment(userName, HasVerticalAlignment.ALIGN_MIDDLE);
				container.setCellVerticalAlignment(date, HasVerticalAlignment.ALIGN_BOTTOM);
				setWidget(container);
				
				HorizontalPanel rightWrapper = new HorizontalPanel();
				rightWrapper.add(isComplete);
				rightWrapper.setHeight("100%");
				rightWrapper.setCellVerticalAlignment(isComplete, HasVerticalAlignment.ALIGN_MIDDLE);
				setRightWidget(rightWrapper);
				
				initialized = true;
			}
			
			TransactionSet set = (TransactionSet) info;
			isComplete.setVisible(set.isComplete);
			userName.setText(set.userName);
			company.setText(set.company);
			date.setText(set.date);
			setSelected(this.isSelected(), false);
		};
	
		@Override
		public void setSelected(boolean selected, boolean fireEvents) {
			super.setSelected(selected, fireEvents);
			
			if(!initialized){
				return;
			}
			
			if(selected){
				date.getElement().getStyle().setColor("white");
				this.getElement().getStyle().setColor("white");
			}
			else{
				date.getElement().getStyle().setColor("#0066ff");
				this.getElement().getStyle().setColor("black");
			}
		
		}
		
	}
	

	public void setTransationsSets(TransactionSet[] transactionSets){
		for(TransactionSet traSet : transactionSets)
			this.add(new Entry(traSet));
	}
	
	public TransactionSet getSelectedTransactionSet(){
		
		if(!this.getSelected().isEmpty()){
			return (TransactionSet)((Entry) this.getSelected().toArray()[0]).getValue();
		}
		return null;
		
	}
	
}
