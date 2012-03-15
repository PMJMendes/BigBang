package bigBang.library.client.userInterface.view;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.presenter.ExchangeItemSelectionViewPresenter;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

public class ExchangeItemSelectionView extends View implements ExchangeItemSelectionViewPresenter.Display{

	FilterableList<ExchangeItemStub> emails;
	FilterableList<AttachmentStub> attachments;
	
	public static class EmailEntry extends ListEntry<ExchangeItemStub>{

		protected Label from;
		protected Label timestamp;
		protected Label subject;
		protected Label bodyPreview;
		protected boolean initialized = false;
		
		public EmailEntry(ExchangeItemStub value) {
			super(value);
			setInfo(value);
			setHeight("65px");
		}
		
		public void setInfo(ExchangeItemStub item){
			if(!initialized){
				from = getFormatedLabel();
				from.setWordWrap(false);
				timestamp = getFormatedLabel();
				timestamp.setWordWrap(false);
				HorizontalPanel top = new HorizontalPanel();
				top.add(from);
				top.add(timestamp);
				top.setCellWidth(from, "80%");
				top.setCellWidth(timestamp, "20%");
				
				subject = getFormatedLabel();
				subject.getElement().getStyle().setFontSize(11, Unit.PX);
				
				bodyPreview = getFormatedLabel();
				bodyPreview.getElement().getStyle().setFontSize(10, Unit.PX);
				bodyPreview.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
				
				VerticalPanel container = new VerticalPanel();
				container.setSize("100%", "100%");
				
				container.add(top);
				container.add(subject);
				container.add(bodyPreview);
				
				initialized = true;
			}
			
			from.setText("De: "+item.from);
			timestamp.setText(item.timestamp);
			subject.setText(item.subject);
			bodyPreview.setText(item.bodyPreview);
			
		}
		
	}
	
	
	public ExchangeItemSelectionView(){

		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("800px", "650px");
		
		//LEFT
		VerticalPanel leftWrapper = new VerticalPanel();
		leftWrapper.setSize("100%", "100%");
		ListHeader header = new ListHeader("Lista de E-mails");
		emails = new FilterableList<ExchangeItemStub>();
		emails.setHeaderWidget(header);
		emails.showFilterField(false);
		leftWrapper.add(emails);
		leftWrapper.setCellHeight(emails, "100%");
		wrapper.addWest(leftWrapper, 200);
		
		
		//RIGHT
		VerticalPanel rightWrapper = new VerticalPanel();
		rightWrapper.setSize("100%", "100%");
		ListHeader aHeader = new ListHeader("Lista de Anexos");
		attachments = new FilterableList<AttachmentStub>();
		attachments.setHeaderWidget(aHeader);
		attachments.showFilterField(false);
		rightWrapper.add(attachments);
		rightWrapper.setCellHeight(attachments, "100%");
		wrapper.addEast(rightWrapper, 200);
		
		//CENTER
	}
	
	@Override
	public void addEmailEntry(ExchangeItemStub email){
		EmailEntry entry = new EmailEntry(email);
		emails.add(entry);
	}
	
	@Override
	protected void initializeView() {
		
	}

}
