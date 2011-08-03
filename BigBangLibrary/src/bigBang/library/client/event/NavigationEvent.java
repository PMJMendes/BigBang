package bigBang.library.client.event;

import bigBang.library.client.userInterface.NavigationItem;

import com.google.gwt.event.shared.GwtEvent;

public class NavigationEvent extends GwtEvent<NavigationEventHandler> {
		
		public enum Navigation {
			NEXT,
			PREVIOUS, 
			FIRST,
			LAST,
			HOME,
			NAVIGATE_TO
		}
		
		protected NavigationItem navigateTo;
		protected Navigation navigation; 
		
		public NavigationEvent(Navigation n){
			this(n, null);
		}
		
		public NavigationEvent(Navigation n, NavigationItem to){
			this.navigation = n;
			this.navigateTo = to;
		}

		public NavigationItem getNavigationItem(){
			return this.navigateTo;
		}
		
		public Navigation getNavigationCommand(){
			return this.navigation;
		}
		
		public static final Type<NavigationEventHandler> TYPE = new Type<NavigationEventHandler>(); 
		

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<NavigationEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(NavigationEventHandler handler) {
			handler.onNavigationEvent(this);
		}
	}