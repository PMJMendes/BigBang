package bigBang.library.client.userInterface;

import java.util.HashMap;
import java.util.Map;

import bigBang.library.client.FormField;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ExpandableSelectionManagementPanelInstantiator;

public class MutableSelectionFormFieldFactory {

	private static Map<String, ExpandableSelectionManagementPanelInstantiator> presenters = new HashMap<String, ExpandableSelectionManagementPanelInstantiator>();
	
	public static void registerPanelInstantiator(String listId, ExpandableSelectionManagementPanelInstantiator instantiator) {
		presenters.put(listId.toLowerCase(), instantiator);
	}
	
	public static FormField<String> getFormField(String listId, HasParameters parameters) {
		ExpandableSelectionFormFieldPanel panel = null;
		String name = parameters == null ? "" : parameters.getParameter("name");

		if(listId != null && presenters.containsKey(listId.toLowerCase())) {
			panel = presenters.get(listId.toLowerCase()).getInstance();
			panel.setParameters(parameters);
			return new ExpandableSelectionFormField(listId, name, panel);
		}else{
			return new ExpandableListBoxFormField(listId, name);
		}
	}
	
}
