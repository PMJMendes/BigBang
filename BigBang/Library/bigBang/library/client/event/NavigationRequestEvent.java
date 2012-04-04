package bigBang.library.client.event;

import bigBang.library.client.userInterface.NavigationItem;

import com.google.gwt.event.shared.GwtEvent;

public class NavigationRequestEvent extends GwtEvent<NavigationRequestEventHandler> {
		
		public enum Navigation {
			NEXT,
			PREVIOUS, 
			FIRST,
			LAST,
			HOME,
			NAVIGATE_TO
		}
		
		protected String navigateToId;
		protected NavigationItem navigateTo;
		protected Navigation navigation;
		
		public NavigationRequestEvent(Navigation n){
			this(n, (NavigationItem)null);
		}
		
		public NavigationRequestEvent(Navigation n, NavigationItem to){
			this.navigation = n;
			this.navigateTo = to;
		}
		
		public NavigationRequestEvent(Navigation n, String to){
			this.navigation = n;
			this.navigateToId = to;
		}

		public String getNaviationItemId(){
			return this.navigateToId;
		}
		
		public NavigationItem getNavigationItem(){
			return this.navigateTo;
		}
		
		public Navigation getNavigationCommand(){
			return this.navigation;
		}
		
		public static final Type<NavigationRequestEventHandler> TYPE = new Type<NavigationRequestEventHandler>(); 
		

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<NavigationRequestEventHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(NavigationRequestEventHandler handler) {
			handler.onNavigationEvent(this);
		}
	}