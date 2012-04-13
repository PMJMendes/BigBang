package bigBang.module.receiptModule.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	@Source("images/searchIcon1.png")
	ImageResource searchIcon();

	@Source("images/managerIcon.png")
	ImageResource managerIcon();
	
	@Source("images/receipt.png")
	ImageResource massCreationIcon();
	
	@Source("images/payment.png")
	ImageResource paymentIcon();
	
	@Source("images/sendReceipt.png")
	ImageResource sendReceiptIcon();
	
	@Source("images/accountability.png")
	ImageResource accountabilityIcon();
	
}
