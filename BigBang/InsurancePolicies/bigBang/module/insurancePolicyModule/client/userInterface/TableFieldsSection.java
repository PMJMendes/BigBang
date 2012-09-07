package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.library.client.userInterface.view.FormViewSection;

public class TableFieldsSection extends FormViewSection{

	//PolicyFormTable table;
	CoverageFieldsGrid table = new CoverageFieldsGrid();
	
	public TableFieldsSection() {
		super("Coberturas");
		addWidget(table);

	}
	
	public void setHeaders(Coverage[] coverages, ColumnHeader[] columns){
		table.setHeaders(coverages, columns);
	}

	public void setTableData(ColumnField[] fields) {
		table.fillTable(fields);
	}
	
}
