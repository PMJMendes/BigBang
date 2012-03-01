package bigBang.module.generalSystemModule.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.dataAccess.CoverageDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.interfaces.CoveragesServiceAsync;

public class CoverageBrokerImpl extends DataBroker<Line> implements
CoverageBroker {

	protected CoveragesServiceAsync service;
	protected boolean needsRefresh = true;

	protected Line[] lines;

	/**
	 * The constructor
	 */
	public CoverageBrokerImpl(){
		this(CoveragesService.Util.getInstance());
		this.dataElementId = BigBangConstants.EntityIds.COVERAGE;
	}

	/**
	 * The constructor
	 * @param service The service to be used by the broker implementation
	 */
	public CoverageBrokerImpl(CoveragesServiceAsync service){
		this.service = service;

	}

	@Override
	public void getLines(final ResponseHandler<Line[]> handler) {
		if(needsRefresh()){
			this.service.getLines(new BigBangAsyncCallback<Line[]>() {

				@Override
				public void onResponseSuccess(Line[] result) {
					lines = result;

					
					for(DataBrokerClient<Line> c : getClients()) {
						((CoverageDataBrokerClient) c).setLines(result);
					}
					handler.onResponse(result);
					needsRefresh = false;
				}
				@Override
				public void onResponseFailure(Throwable caught){
					handler.onError(new String[]{
							new String("Could not get lines.")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{
		
			handler.onResponse(lines);
//			for(DataBrokerClient<Line> c : getClients()) {
//				((CoverageDataBrokerClient) c).setLines(lines);
//			}
		}
	}

	@Override
	public void getLine(String lineId, ResponseHandler<Line> handler) {
		
		for(int i = 0; i<lines.length; i++){
			if(lines[i].id.equalsIgnoreCase(lineId)){
				handler.onResponse(lines[i]);
				return;
			}
		}
		
	}

	@Override
	public void addLine(Line line, final ResponseHandler<Line> handler) {
		this.service.createLine(line, new BigBangAsyncCallback<Line>() {

			@Override
			public void onResponseSuccess(Line result) {
				Line[] newArray = new Line[lines.length+1];
				
				for(int i = 0; i<lines.length; i++){
					newArray[i] = lines[i];
				}
				newArray[lines.length] = result;
				lines = newArray;
				
				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).addLine(result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not create line.")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void updateLine(Line line, final ResponseHandler<Line> handler) {
		
		this.service.saveLine(line, new BigBangAsyncCallback<Line>() {

			@Override
			public void onResponseSuccess(Line result) {
				for(int i = 0; i<lines.length; i++){
					if(lines[i].id.equalsIgnoreCase(result.id)){
						lines[i] = result;
						break;
					}
				}
				
				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).updateLine(result);
				}
				handler.onResponse(result);
			}
			
			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not save line.")	
				});
				super.onResponseFailure(caught);
			}
		
		});
		
		
	
		
	}

	@Override
	public void removeLine(final String lineId, final ResponseHandler<Line> handler) {
		this.service.deleteLine(lineId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				
				Line[] newArray = new Line[lines.length-1];
				int curr = 0;
				Line deleted = new Line();
				
				for(int i = 0; i< lines.length; i++){
					if(!lineId.equalsIgnoreCase(lines[i].id)){
						newArray[curr] = lines[i];
						curr++;
					}else
						deleted = lines[i];
				}
				
				for(DataBrokerClient<Line> c : CoverageBrokerImpl.this.getClients()){
					((CoverageDataBrokerClient)c).removeLine(lineId);
				}
				handler.onResponse(deleted);
			}
			
			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not delete line.")	
				});
				super.onResponseFailure(caught);
			}
		});
	}

	@Override
	public void getSubLines(final String parentLineId,
			final ResponseHandler<SubLine[]> handler) {
		if(needsRefresh()){
			this.service.getLines(new BigBangAsyncCallback<Line[]>() {

				@Override
				public void onResponseSuccess(Line[] result) {
					lines = result;

					for(DataBrokerClient<Line> c : getClients()) {
						((CoverageDataBrokerClient) c).setLines(result);
					}

					handler.onResponse(getSubLinesLocal(parentLineId));
					needsRefresh = false;
				}
				@Override
				public void onResponseFailure(Throwable caught) {
					handler.onError(new String[]{
							new String("Could not get sublines.")	
					});
					super.onResponseFailure(caught);
				}
				
				
			});
		}else{
			
			

			handler.onResponse(getSubLinesLocal(parentLineId));
//			
//			for(DataBrokerClient<Line> c : getClients()) {
//				((CoverageDataBrokerClient) c).setLines(lines);
//			}
		}

	}

	private SubLine[] getSubLinesLocal(String parentLineId) {

		for(int i=0; i<lines.length; i++){
			if(lines[i].id.equalsIgnoreCase(parentLineId)){
				return lines[i].subLines;
			}
		}
		return null;
	}

	@Override
	public void getSubLine(String parentLineId, String subLineId,
			ResponseHandler<SubLine> handler) {

		SubLine[] subLines = getSubLinesLocal(parentLineId);

		for(int i = 0; i<subLines.length; i++){
			if(subLines[i].id.equalsIgnoreCase(subLineId)){
				handler.onResponse(subLines[i]);
				return;
			}
		}

		throw new RuntimeException("The requested subline could not be found locally. id:\""+subLineId+"\"");
	}

	@Override
	public void addSubLine(SubLine subLine, final ResponseHandler<SubLine> handler) {
		this.service.createSubLine(subLine, new BigBangAsyncCallback<SubLine>() {

			@Override
			public void onResponseSuccess(SubLine result) {
				SubLine[] oldArray = getSubLinesLocal(result.lineId);
				SubLine[] newArray = new SubLine[oldArray.length+1];

				for(int i = 0; i<oldArray.length; i++){
					newArray[i] = oldArray[i];
				}
				newArray[newArray.length-1] = result;

				for(int i =0; i<lines.length; i++){
					if(lines[i].id.equalsIgnoreCase(result.lineId)){
						lines[i].subLines = newArray;
						break;
					}
				}


				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).addSubLine(result.lineId, result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not create subline.")	
				});
				super.onResponseFailure(caught);
			}


		});
	}

	@Override
	public void updateSubLine(SubLine subLine,
			final ResponseHandler<SubLine> handler) {

		this.service.saveSubLine(subLine, new BigBangAsyncCallback<SubLine>() {

			@Override
			public void onResponseSuccess(SubLine result) {
				SubLine[] subLines = getSubLinesLocal(result.lineId);

				for(int i = 0; i<subLines.length; i++){
					if(subLines[i].id.equalsIgnoreCase(result.id)){
						subLines[i] = result;
						break;
					}
				}

				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).updateSubLine(result.lineId, result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not update subline.")	
				});
				super.onResponseFailure(caught);
			}


		});


	}

	@Override
	public void removeSubLine(final String lineId, final String subLineId,
			final ResponseHandler<SubLine> handler) {

		service.deleteSubLine(subLineId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {

				SubLine[] oldArray = getSubLinesLocal(lineId);
				SubLine[] newArray = new SubLine[oldArray.length-1];
				int curr = 0;
				SubLine deleted = new SubLine();

				for(int i = 0; i<oldArray.length; i++){
					if(!oldArray[i].id.equalsIgnoreCase(subLineId)){
						newArray[curr] = oldArray[i];
						curr++;
					}
					else{
						deleted = oldArray[i];
					}

				}

				for(int i = 0; i<lines.length; i++){
					if(lines[i].id.equalsIgnoreCase(deleted.lineId)){
						lines[i].subLines = newArray;
						break;
					}
				}


				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).removeSubLine(deleted.lineId, deleted.id);
				}
				handler.onResponse(deleted);

			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not delete subline.")	
				});
				super.onResponseFailure(caught);
			}


		});

	}

	@Override
	public void getCoverages(final String parentLineId, final String parentSubLineId,
			final ResponseHandler<Coverage[]> handler) {

		if(needsRefresh()){
			this.service.getLines(new BigBangAsyncCallback<Line[]>() {

				@Override
				public void onResponseSuccess(Line[] result) {
				 
					lines = result;


					//Lines
					for(DataBrokerClient<Line> c : getClients()) {
						((CoverageDataBrokerClient) c).setLines(result);
					}

					handler.onResponse(getCoveragesLocal(parentSubLineId, getSubLinesLocal(parentLineId)));
					needsRefresh = false;
				}
				
				@Override
				public void onResponseFailure(Throwable caught){
					handler.onError(new String[]{
							new String("Could not get coverages.")	
					});
					super.onResponseFailure(caught);
				}
			});
			
			
		}else{

			handler.onResponse(getCoveragesLocal(parentSubLineId, getSubLinesLocal(parentLineId)));
//			for(DataBrokerClient<Line> c : getClients()) {
//				((CoverageDataBrokerClient) c).setLines(lines);
//			}
		}

	}

	private Coverage[] getCoveragesLocal(String parentSubLineId,
			SubLine[] subLinesLocal) {

		for(int i =0; i<subLinesLocal.length; i++){
			if(subLinesLocal[i].id.equalsIgnoreCase(parentSubLineId)){
				return subLinesLocal[i].coverages;
			}
		}
		return null;
	}

	@Override
	public void getCoverage(String parentLineId, String parentSubLineId, String coverageId,
			ResponseHandler<Coverage> handler) {

		Coverage[] coverages = getCoveragesLocal(parentSubLineId, getSubLinesLocal(parentLineId));

		for(int i = 0; i<coverages.length; i++){
			if(coverages[i].id.equalsIgnoreCase(coverageId)){
				handler.onResponse(coverages[i]);
				return;
			}
		}

		throw new RuntimeException("The requested coverage could not be found locally. id:\""+coverageId+"\"");


	}

	@Override
	public void addCoverage(final String parentLineId, final Coverage coverage, final ResponseHandler<Coverage> handler){
		this.service.createCoverage(coverage, new BigBangAsyncCallback<Coverage>() {

			@Override
			public void onResponseSuccess(Coverage result) {

				SubLine[] subLines = getSubLinesLocal(parentLineId);
				Coverage[] oldArray = getCoveragesLocal(coverage.subLineId, subLines);
				Coverage[] newArray = new Coverage[oldArray.length+1];


				for(int i = 0; i<oldArray.length; i++){
					newArray[i] = oldArray[i];
				}
				newArray[newArray.length-1] = result;

				for(int i = 0; i<subLines.length; i++){
					if(subLines[i].id.equalsIgnoreCase(coverage.subLineId)){
						subLines[i].coverages = newArray;
						break;
					}
				}

				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).addCoverage(result.subLineId, result);
				}
				handler.onResponse(result);
			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not create coverage.")	
				});
				super.onResponseFailure(caught);
			}

		});
	}

	@Override
	public void updateCoverage(final String parentLineId, final Coverage coverage,
			final ResponseHandler<Coverage> handler) {
		this.service.saveCoverage(coverage, new BigBangAsyncCallback<Coverage>() {

			@Override
			public void onResponseSuccess(Coverage result) {

				SubLine[] subLines = getSubLinesLocal(parentLineId);
				Coverage[] coverages = getCoveragesLocal(coverage.subLineId, subLines);

				for(int i = 0; i < coverages.length; i++){
					if(coverages[i].id.equalsIgnoreCase(coverage.id)){
						coverages[i] = coverage;
					}
				}

				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).updateCoverage(result.subLineId, result);
				}
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not save coverage.")	
				});
				super.onResponseFailure(caught);
			}
		});

	}

	@Override
	public void removeCoverage(final String parentLineId, final String parentSubLineId, final String coverageId,
			final ResponseHandler<Coverage> handler) {
		service.deleteCoverage(coverageId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {

				SubLine[] subLines = getSubLinesLocal(parentLineId);
				Coverage[] oldArray = getCoveragesLocal(parentSubLineId, subLines);
				Coverage[] newArray = new Coverage[oldArray.length-1];
				int curr = 0;
				Coverage deleted = new Coverage();

				for(int i = 0; i<oldArray.length; i++){
					if(!oldArray[i].id.equalsIgnoreCase(coverageId)){
						newArray[curr] = oldArray[i];
						curr++;
					}
					else{
						deleted = oldArray[i];
					}
				}

				for(int i = 0; i<subLines.length; i++){
					if(subLines[i].id.equalsIgnoreCase(parentSubLineId)){
						subLines[i].coverages = newArray;
						break;
					}
				}

				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).removeCoverage(deleted.subLineId, deleted.id);
				}
				handler.onResponse(deleted);

			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not delete coverage.")	
				});
				super.onResponseFailure(caught);
			}

		});


	}

	@Override
	public void getTaxes(final String parentLineId, final String parentSubLineId, final String parentCoverageId, final ResponseHandler<Tax[]> handler) {

		if(needsRefresh()){
			this.service.getLines(new BigBangAsyncCallback<Line[]>() {

				@Override
				public void onResponseSuccess(Line[] result) {
					lines = result;
				
					for(DataBrokerClient<Line> c : getClients()) {
						((CoverageDataBrokerClient) c).setLines(result);
					}

					handler.onResponse(getTaxesLocal(parentCoverageId, getCoveragesLocal(parentSubLineId, getSubLinesLocal(parentLineId))));
					needsRefresh = false;
				}
				
				@Override
				public void onResponseFailure(Throwable caught){
					handler.onError(new String[]{
							new String("Could not get taxes.")	
					});
					super.onResponseFailure(caught);
				}
			});
		}else{

			handler.onResponse(getTaxesLocal(parentCoverageId, getCoveragesLocal(parentSubLineId, getSubLinesLocal(parentLineId))));
//			for(DataBrokerClient<Line> c : getClients()) {
//				((CoverageDataBrokerClient) c).setLines(lines);
//			}
		}

	}

	private Tax[] getTaxesLocal(String parentCoverageId,
			Coverage[] coveragesLocal) {

		for(int i = 0; i<coveragesLocal.length; i++){
			if(coveragesLocal[i].id.equalsIgnoreCase(parentCoverageId)){
				return coveragesLocal[i].taxes;
			}
		}

		return null;
	}

	@Override
	public void getTax(String parentLineId, String parentSubLineId, String parentCoverageId, String taxId, 
			ResponseHandler<Tax> handler) {

		Tax[] taxes = getTaxesLocal(parentCoverageId, getCoveragesLocal(parentSubLineId, getSubLinesLocal(parentLineId)));

		for(int i = 0; i<taxes.length; i++){
			if(taxes[i].id.equalsIgnoreCase(taxId)){
				handler.onResponse(taxes[i]);
				return;
			}
		}

		throw new RuntimeException("The requested tax could not be found locally. id:\""+taxId+"\"");


	}

	@Override
	public void addTax(final String parentLineId, final String parentSubLineId, final Tax tax,
			final ResponseHandler<Tax> handler) {
		this.service.createTax(tax, new BigBangAsyncCallback<Tax>() {

			@Override
			public void onResponseSuccess(Tax result) {

				SubLine[] subLines = getSubLinesLocal(parentLineId);
				Coverage[] coverages = getCoveragesLocal(parentSubLineId, subLines);
				Tax[] oldArray = getTaxesLocal(tax.coverageId, coverages);
				Tax[] newArray = new Tax[oldArray.length+1];

				for(int i = 0; i<oldArray.length; i++){
					newArray[i] = oldArray[i];
				}
				newArray[newArray.length-1] = tax;

				for(int i = 0; i<coverages.length; i++){
					if(coverages[i].id.equalsIgnoreCase(tax.id)){
						coverages[i].taxes = newArray;
					}
				}

				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).addTax(result.coverageId, result);
				}

			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not create tax.")	
				});
				super.onResponseFailure(caught);
			}
		});

	}
	@Override
	public void updateTax(final String parentLineId, final String parentSubLineId, final Tax tax,
			final ResponseHandler<Tax> handler) {
		this.service.saveTax(tax, new BigBangAsyncCallback<Tax>() {

			@Override
			public void onResponseSuccess(Tax result) {

				SubLine[] subLines = getSubLinesLocal(parentLineId);
				Coverage[] coverages = getCoveragesLocal(parentSubLineId, subLines);
				Tax[] taxes = getTaxesLocal(tax.coverageId, coverages);

				for(int i = 0; i<taxes.length; i++){
					if(taxes[i].id.equalsIgnoreCase(tax.id)){
						taxes[i] = tax;
					}
				}

				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).updateTax(result.coverageId, result);
				}
				handler.onResponse(result);

			}

			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not save tax.")	
				});
				super.onResponseFailure(caught);
			}

		});

	}

	@Override
	public void removeTax(final String parentLineId, final String parentSubLineId, final String parentCoverageId, final String taxId,
			final ResponseHandler<Void> handler) {
		
		this.service.deleteTax(taxId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				
				SubLine[] subLines = getSubLinesLocal(parentLineId);
				Coverage[] coverages = getCoveragesLocal(parentSubLineId, subLines);
				Tax[] oldArray = getTaxesLocal(parentCoverageId, coverages);
				Tax[] newArray = new Tax[oldArray.length-1];
				int curr = 0;
				Tax deleted = new Tax();
				
				for(int i = 0; i<oldArray.length; i++){
					if(!oldArray[i].id.equalsIgnoreCase(taxId)){
						newArray[curr] = oldArray[i];
					}
					else{
						deleted = oldArray[i];
					}
				}
				
				for(int i = 0; i<coverages.length; i++){
					if(coverages[i].id.equalsIgnoreCase(parentCoverageId)){
						coverages[i].taxes = newArray;
						break;
					}
				}
				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).removeTax(deleted.coverageId, deleted.id);
				}
				
			}
			
			@Override
			public void onResponseFailure(Throwable caught){
				handler.onError(new String[]{
						new String("Could not delete coverage.")	
				});
				super.onResponseFailure(caught);
			}
		
		
			
			
		});

	}

	@Override
	public void requireDataRefresh() {
		this.needsRefresh = true;
	}

	/**
	 * Returns whether or not the broker needs to refresh its data.
	 * @return True if the broker needs to refresh, false otherwise
	 */
	public boolean needsRefresh(){
		return this.needsRefresh;
	}

	@Override
	public void notifyItemCreation(String itemId) {
		requireDataRefresh();
		getLinesLocal();
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		requireDataRefresh();
		getLinesLocal();
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		requireDataRefresh();
		getLinesLocal();
	}

	public void getLinesLocal(){
		getLines(new ResponseHandler<Line[]>() {

			@Override
			public void onResponse(Line[] response) {
			}
			@Override
			public void onError(Collection<ResponseError> errors) {
			}
		});
	}


}
