package bigBang.module.generalSystemModule.client.dataAccess;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.CoverageBroker;
import bigBang.definitions.client.brokerClient.CoverageDataBrokerClient;
import bigBang.definitions.client.types.Coverage;
import bigBang.definitions.client.types.Line;
import bigBang.definitions.client.types.SubLine;
import bigBang.definitions.client.types.Tax;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.dataAccess.DataBroker;
import bigBang.library.client.dataAccess.DataBrokerCache;
import bigBang.library.client.dataAccess.DataBrokerClient;
import bigBang.library.client.response.ResponseHandler;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;
import bigBang.module.generalSystemModule.interfaces.CoveragesServiceAsync;

public class CoverageBrokerImpl extends DataBroker<Line> implements
CoverageBroker {

	protected CoveragesServiceAsync service;
	protected boolean needsRefresh = true;

	protected DataBrokerCache linesCache;
	protected DataBrokerCache subLinesCache;
	protected DataBrokerCache coveragesCache;
	protected DataBrokerCache taxesCache;

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

		this.linesCache = this.cache;
		this.subLinesCache = new DataBrokerCache();
		this.coveragesCache = new DataBrokerCache();
		this.taxesCache = new DataBrokerCache();

		this.linesCache.setThreshold(0);
		this.subLinesCache.setThreshold(0);
		this.coveragesCache.setThreshold(0);
		this.taxesCache.setThreshold(0);
	}

	@Override
	public void getLines(final ResponseHandler<Line[]> handler) {
		if(needsRefresh()){
			this.service.getLines(new BigBangAsyncCallback<Line[]>() {

				@Override
				public void onSuccess(Line[] result) {
					linesCache.clear();

					//Populates the caches

					//Lines
					for(int i = 0; i < result.length; i++) {
						linesCache.add(result[i].id, result[i]);
						SubLine[] subLines = result[i].subLines; 
						//Sub lines
						for(int j = 0; j < subLines.length; j++){
							subLinesCache.add(subLines[j].id, subLines[j]);
							Coverage[] coverages = subLines[j].coverages;
							//Coverages
							for(int k = 0; k < coverages.length; k++){
								coveragesCache.add(coverages[k].id, coverages[k]);
								Tax[] taxes = coverages[k].taxes;
								//Taxes
								for(int l = 0; l < taxes.length; l++){
									taxesCache.add(taxes[l].id, taxes[l]);
								}
							}
						}
					}
					for(DataBrokerClient<Line> c : getClients()) {
						((CoverageDataBrokerClient) c).setLines(result);
					}
					handler.onResponse(result);
					needsRefresh = false;
				}
			});
		}else{
			int size = linesCache.getNumberOfEntries();
			Line[] lines = new Line[size];
			
			int i = 0;
			for(Object o : this.linesCache.getEntries()){
				lines[i] = (Line) o;
				i++;
			}
			handler.onResponse(lines);
		}
	}

	@Override
	public void getLine(String lineId, ResponseHandler<Line> handler) {
		if(!linesCache.contains(lineId))
			throw new RuntimeException("The requested line could not be found locally. id:\""+lineId+"\"");
		handler.onResponse((Line) linesCache.get(lineId));
	}

	@Override
	public void addLine(Line line, final ResponseHandler<Line> handler) {
		this.service.createLine(line, new BigBangAsyncCallback<Line>() {

			@Override
			public void onSuccess(Line result) {
				linesCache.add(result.id, result);
				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient) c).addLine(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void updateLine(Line line, final ResponseHandler<Line> handler) {
		if(!linesCache.contains(line.id))
			throw new RuntimeException("The requested line could not be found locally. id:\""+line.id+"\"");
		this.service.saveLine(line,  new BigBangAsyncCallback<Line>() {

			@Override
			public void onSuccess(Line result) {
				linesCache.update(result.id, result);
				for(DataBrokerClient<Line> c : getClients()){
					((CoverageDataBrokerClient)c).updateLine(result);
				}
				handler.onResponse(result);
			}
		});
	}

	@Override
	public void removeLine(final String lineId, final ResponseHandler<Line> handler) {
		this.service.deleteLine(lineId, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				linesCache.remove(lineId);
				//TODO URGENT FJVC
			}
		});
	}

	@Override
	public void getSubLines(String parentLineId,
			ResponseHandler<SubLine[]> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getSubLine(String parentLineId, String subLineId,
			ResponseHandler<SubLine> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSubLine(String parentLineId, SubLine subLine, ResponseHandler<SubLine> handler) {
		//TODO
	}

	@Override
	public void updateSubLine(String parentLineId, String subLineId,
			ResponseHandler<SubLine> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSubLine(String parentLineId, String subLineId,
			ResponseHandler<Void> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCoverages(String parentSubLineId,
			ResponseHandler<Coverage[]> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCoverage(String parentSubLineId, String coverageId,
			ResponseHandler<Coverage> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCoverage(String parentSubLineId, Coverage coverage, ResponseHandler<Coverage> handler){
		//TODO
	}

	@Override
	public void updateCoverage(String parentSubLineId, String coverageId,
			ResponseHandler<Coverage> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeCoverage(String parentSubLineId, String coverageId,
			ResponseHandler<Void> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTaxes(String parentCoverageId, ResponseHandler<Tax[]> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTax(String taxId, String coverageId,
			ResponseHandler<Tax> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTax(String parentCoverageId, Tax tax,
			ResponseHandler<Tax> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTax(String parentCoverageId, Tax tax,
			ResponseHandler<Tax> handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTax(String parentCoverageId, String taxId,
			ResponseHandler<Void> handler) {
		// TODO Auto-generated method stub

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

}
