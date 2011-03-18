package bigBang.library.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class OperationCompletedEvent extends GwtEvent<OperationCompletedEventHandler> {

		public static Type<OperationCompletedEventHandler> TYPE = new Type<OperationCompletedEventHandler>();
		private String operationId;
		private String operationInstanceId;
		
		public OperationCompletedEvent(String operationId, String operationInstanceId) {
			this.operationId = operationId;
			this.operationInstanceId = operationInstanceId;
		}
		
		public String getOperationId(){
			return this.operationId;
		}
		
		public String getOperationInstanceId(){
			return this.operationInstanceId;
		}

		@Override
		public Type<OperationCompletedEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(OperationCompletedEventHandler handler) {
			handler.onOperationCompleted(this);
		}

}
