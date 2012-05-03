package bigBang.library.client.userInterface.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.TransactionSet;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;

public class TransactionSetReportPanel extends List<TransactionSet> {

	static Resources resources = GWT.create(Resources.class);
	
	public static class Entry extends ListEntry<TransactionSet>{

		protected Label date;
		protected Image isComplete;
		protected boolean initialized;
		
		public Entry(TransactionSet value) {
			super(value);
			setHeight("40px");
		}
		
		public <I extends Object> void setInfo(I info) {
			if(!initialized){
				date = getFormatedLabel();
				isComplete = new Image(resources.completeIcon());
				
				HorizontalPanel rightContainer = new HorizontalPanel();
				rightContainer.add(date);
				rightContainer.add(isComplete);
				initialized = true;
			}
			
			TransactionSet set = (TransactionSet) info;
			
			date.setText(set.date);
			this.setText(set.userName);
			isComplete.setVisible(set.isComplete);
			
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
			return (TransactionSet) this.getSelected().toArray()[0];
		}
		return null;
		
	}
	
}
