package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import org.apache.ecs.GenericElement;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualtyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.MedicalDetail;
import com.premiumminds.BigBang.Jewel.Objects.MedicalFile;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

/**
 * Class responsible for the creation of the Casualty Productivity Map
 */
public class SubCasualtyProductivityMap extends SubCasualtyListingsBase {
	
	// Constants
	private static final String NO_VALUE = "-";
	private static final String TITLE_REPLACE_STR_1 = "<tit>";
	private static final String TITLE_REPLACE_STR_2 = "<tit2>";
	private static final String COL_01 = "Data de Encerramento (__/__/____)";
	private static final String COL_02 = "Data de Participação (__/__/____)";
	private static final String COL_03 = "Período de Gestão";
	private static final String COL_04 = "Gestor do Sinistro";
	private static final String COL_05 = "Cliente";
	private static final String COL_06 = "Apólice";
	private static final String COL_07 = "Ramo";
	private static final String COL_08 = "Segurador";
	private static final String COL_09 = "N.º de Processos Encerrados em " + TITLE_REPLACE_STR_1 + " de " + TITLE_REPLACE_STR_2 + " pela EGS";
	private static final String COL_10 = "Valores das Indemnizações (€)";
	private static final String COL_11 = "Processos Indemnizados (N.º)";
	private static final String COL_12 = "Valores dos Danos Reclamados (€)";
	private static final String COL_13 = "Processos com Valores Reclamados Inferiores ou Iguais aos Valores Indemnizados (N.º)";
	private static final String COL_14 = "Sinistros Declinados (N.º)";
	private static final String COL_15 = "Sinistros Declinados que Foram Avisados Previamente pela Credite-EGS que Seriam Declinados (N.º)";
	private static final String LIN_PERCENT = "Percentagem de Processos";
	private static final String LIN_TOTAL = "Totais";
	private static final int COLUMN_BREAK_POINT = 33;
	private static final String YES_STRING = "Sim";
	private static final String NO_STRING = "Não";
	
	// Width Constants
	private static final int WIDTH_COL_01 = 99;
	private static final int WIDTH_COL_02 = 99;
	private static final int WIDTH_COL_03 = 99;
	private static final int WIDTH_COL_04 = 99;
	private static final int WIDTH_COL_05 = 99;
	private static final int WIDTH_COL_06 = 99;
	private static final int WIDTH_COL_07 = 99;
	private static final int WIDTH_COL_08 = 99;
	private static final int WIDTH_COL_09 = 99;
	private static final int WIDTH_COL_10 = 99;
	private static final int WIDTH_COL_11 = 99;
	private static final int WIDTH_COL_12 = 99;
	private static final int WIDTH_COL_13 = 99;
	private static final int WIDTH_COL_14 = 99;
	private static final int WIDTH_COL_15 = 99;
	
	// Class Variables
	private int totalProcesses = 0; // number of processes
	private BigDecimal settlementTotal = BigDecimal.ZERO; // valor total de indemnizações
	private int settledProcesses = 0; // number of settled processes
	private BigDecimal claimedValueTotal = BigDecimal.ZERO; // number of claimed values
	private int smallerClaimProcesses = 0; // number of processes with a claimed value inferior to the one paid
	private int declinedCasualties = 0; // number of declined Casualties
	private int warnedDeclinedCasualties = 0; // number of declined Casualties that were warned by Credite-EGS
	private String paramClientUUID = NO_VALUE;
	private String paramClientName= NO_VALUE;
	private String paramStartDate = NO_VALUE;
	private String paramEndDate = NO_VALUE;
	
	/**
	 * Inner class which holds the information to display at the map
	 */
	private class SubCasualtyData {
		
		private String closingDate;
		private String casualtyDate;
		private int managementTime;
		private String manager;
		private String client;
		private String policyNumber;
		private String category;
		private String company;
		private String casualtyNumber;
		private String settlementValue;
		private boolean settledProcess;
		private String damagesClaimed;
		private boolean smallerClaimProcess;
		private boolean declinedCasualty;
		private boolean warnedDeclinedCasualty;
		
		public String getClosingDate() {
			return closingDate;
		}
		public void setClosingDate(String closingDate) {
			this.closingDate = closingDate;
		}
		public String getCasualtyDate() {
			return casualtyDate;
		}
		public void setCasualtyDate(String casualtyDate) {
			this.casualtyDate = casualtyDate;
		}
		public int getManagementTime() {
			return managementTime;
		}
		public void setManagementTime(int managementTime) {
			this.managementTime = managementTime;
		}
		public String getManager() {
			return manager;
		}
		public void setManager(String manager) {
			this.manager = manager;
		}
		public String getClient() {
			return client;
		}
		public void setClient(String client) {
			this.client = client;
		}
		public String getPolicyNumber() {
			return policyNumber;
		}
		public void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public String getCasualtyNumber() {
			return casualtyNumber;
		}
		public void setCasualtyNumber(String casualtyNumber) {
			this.casualtyNumber = casualtyNumber;
		}
		public String getSettlementValue() {
			return settlementValue;
		}
		public void setSettlementValue(String settlementValue) {
			this.settlementValue = settlementValue;
		}
		public boolean isSettledProcess() {
			return settledProcess;
		}
		public void setSettledProcess(boolean settledProcess) {
			this.settledProcess = settledProcess;
		}
		public String getDamagesClaimed() {
			return damagesClaimed;
		}
		public void setDamagesClaimed(String damagesClaimed) {
			this.damagesClaimed = damagesClaimed;
		}
		public boolean isSmallerClaimProcess() {
			return smallerClaimProcess;
		}
		public void setSmallerClaimProcess(boolean smallerClaimProcess) {
			this.smallerClaimProcess = smallerClaimProcess;
		}
		public boolean isDeclinedCasualty() {
			return declinedCasualty;
		}
		public void setDeclinedCasualty(boolean declinedCasualty) {
			this.declinedCasualty = declinedCasualty;
		}
		public boolean isWarnedDeclinedCasualty() {
			return warnedDeclinedCasualty;
		}
		public void setWarnedDeclinedCasualty(boolean warnedDeclinedCasualty) {
			this.warnedDeclinedCasualty = warnedDeclinedCasualty;
		}
		
		// This method sets the management time according to the casualty date and closing date
		public void setManagementTime(Timestamp startDate, Timestamp endDate) {
			
			  long milliseconds1 = startDate.getTime();
			  long milliseconds2 = endDate.getTime();

			  long diff = milliseconds2 - milliseconds1;
			  /* long diffSeconds = diff / 1000;
			  long diffMinutes = diff / (60 * 1000);
			  long diffHours = diff / (60 * 60 * 1000); 
			  long diffDays = diff / (24 * 60 * 60 * 1000); */
			  int diffDays = (int) diff / (24 * 60 * 60 * 1000);
			  
			  setManagementTime(diffDays);
		}
		
		/**
		 * This method sets the report values, giving a sub-casualty
		 */
		private void setValues(SubCasualty subCasualty)
				throws BigBangJewelException {
			
			Timestamp startDate = null;
			Timestamp closingDate;
			
			// Sets the closing date, if possible
			closingDate = Timestamp.valueOf(getClosingDateFomLogs(subCasualty) + " 00:00:00.0");
			if (closingDate != null) {
				setClosingDate(closingDate.toString().substring(0, 10));
			}
			
			// Sets the casualty's date, if possible
			if (subCasualty.GetCasualty().getAt(Casualty.I.DATE) != null) {
				setCasualtyDate(subCasualty.GetCasualty().getAt(Casualty.I.DATE)
						.toString().substring(0, 10));
				startDate = Timestamp.valueOf(getCasualtyDate() + " 00:00:00.0");
			}
			
			// Sets the management Time, if possible
			if (startDate!=null && closingDate!=null) {
				setManagementTime(startDate, closingDate);
			}			
			
			// Sets the casualty's manager, if possible
			if (subCasualty.GetCasualty().GetProcessID() != null) {
				
				try {				
					UUID managerID = PNProcess.GetInstance(Engine.getCurrentNameSpace(), subCasualty.GetCasualty().GetProcessID()).GetManagerID();					
					if (managerID != null) {
						String managerName = User.GetInstance(Engine.getCurrentNameSpace(), managerID).getDisplayName();						
						if (managerName != null) {
							setManager(managerName);
						}
					}				
				} catch (Throwable e) {
					throw new BigBangJewelException("Could not get the Casualty's manager " +  e.getMessage(), e);
				}
			}
			
			// Sets the casualty's client, if possible
			if (subCasualty.getAbsolutePolicy().GetClient() != null) {
				setClient(subCasualty.getAbsolutePolicy().GetClient().getLabel());
			}
			
			// Sets the sub-casualty's policy/sub-policy number, if possible
			if (subCasualty.getAbsolutePolicy().getAt(Policy.I.NUMBER) != null) {
				setClient(subCasualty.getAbsolutePolicy().getAt(Policy.I.NUMBER).toString());
			}
			
			// Sets the sub-casualty's category, if possible
			if (subCasualty.getAbsolutePolicy().GetSubLine() != null &&
				subCasualty.getAbsolutePolicy().GetSubLine().getLine() != null &&
				subCasualty.getAbsolutePolicy().GetSubLine().getLine().getCategory() != null) {

				String catStr =  subCasualty.getAbsolutePolicy().GetSubLine().getLabel() + "/" +
						subCasualty.getAbsolutePolicy().GetSubLine().getLine().getLabel() + "/" +
						subCasualty.getAbsolutePolicy().GetSubLine().getLine().getCategory().getLabel();
				
				setCategory(catStr);
			}
			
			// Sets the sub-casualty's company, if possible
			if (subCasualty.getAbsolutePolicy().GetCompany() != null) {
				setCompany(subCasualty.getAbsolutePolicy().GetCompany().getLabel());
			}
			
			// Sets the sub-casualty's internal process number, if possible
			if (subCasualty.getAt(SubCasualty.I.NUMBER) != null) {
				setCasualtyNumber(subCasualty.getAt(SubCasualty.I.NUMBER)
						.toString());
			}
			
			// Sets the values which come from the sub-casualty items
			setItemsValue(subCasualty);
			
			// Sets the values which come from the sub-casualty framing
			setFramingValue(subCasualty);
		}
		
		/**
		 * This method gets the values coming from the sub-casualty framing, namely
		 * whether the casualty was refused by the company, and whether that was warned
		 * by the manager 
		 */
		private void setFramingValue(SubCasualty subCasualty) throws BigBangJewelException {
			
			if (subCasualty.GetFraming() != null) {
				
				SubCasualtyFraming framing = subCasualty.GetFraming();
				
				if (framing.getAt(SubCasualtyFraming.I.DECLINEDCASUALTY) != null) {
					if(((Boolean)framing.getAt(SubCasualtyFraming.I.DECLINEDCASUALTY)).booleanValue()) {
						setDeclinedCasualty(true);
						declinedCasualties++;
					}
				}
				
				if (framing.getAt(SubCasualtyFraming.I.DECLINEDWARNING) != null) {
					if(((Boolean)framing.getAt(SubCasualtyFraming.I.DECLINEDWARNING)).booleanValue()) {
						setWarnedDeclinedCasualty(true);
						warnedDeclinedCasualties++;
					}
				}
			}
		}
		
		/**
		 * This method gets the values coming from the sub-casualty items, namely
		 * the settlement value, if the process was settled, the damages claimed, 
		 * and if the claimed value was smaller than the settled value
		 */
		private void setItemsValue(SubCasualty subCasualty) throws BigBangJewelException {
			
			// Gets the sub-casualty's category
			Category categoryObj = subCasualty.GetSubLine().getLine().getCategory();
			
			// Tries to set the numeric values from the sub-casualty's details
			if (subCasualty.GetCurrentItems() != null) {

				SubCasualtyItem[] currentItems = subCasualty.GetCurrentItems();

				BigDecimal settlement = BigDecimal.ZERO;
				BigDecimal damagesClaimed = BigDecimal.ZERO;
						
				// needed to identify if there's no value for all, in case a '-' must be outputted, and not a 0
				boolean allSettlementNull = true;
				boolean allDamagesNull = true;
				
				// The settlement and damages is the sum from all
				// sub-casualty's items
				// There is also one total for all sub-casualties
				// If any of them is undefined, all sums become assigned with
				// the "NO_VALUE" constant
				for (int i = 0; i < currentItems.length; i++) {
					SubCasualtyItem temp = currentItems[i];
					// Indemnização
					if (temp.getAt(SubCasualtyItem.I.SETTLEMENT) != null) {
						settlement = settlement.add((BigDecimal) temp
								.getAt(SubCasualtyItem.I.SETTLEMENT));
							allSettlementNull = false;
					}
					
					// Valor reclamado
					// Indemnização
					if (temp.getAt(SubCasualtyItem.I.DAMAGES) != null) {
						damagesClaimed = settlement.add((BigDecimal) temp
								.getAt(SubCasualtyItem.I.DAMAGES));
							allDamagesNull = false;
					}
				}
				
				// If there is no items, the values are null
				if (currentItems.length == 0) {
					settlement = null;
					damagesClaimed = null;
					setSettledProcess(false);
				}
				
				if (allSettlementNull) {
					settlement = null;
					setSettledProcess(false);
				}
				if (allDamagesNull) {
					damagesClaimed = null;
				}
				
				// Special case to work accidents
				if (categoryObj.getKey()
						.equals(Constants.PolicyCategories.WORK_ACCIDENTS)) {
					settlement = getSettlementFromMedicalFiles(subCasualty, settlement);
				}
				
				if (settlement != null) {
					setSettlementValue(String.format("%,.2f",
							((BigDecimal) settlement)));

					settlementTotal = settlementTotal.add(settlement);
					
					setSettledProcess(true);
					
					settledProcesses++;
				}
				if (damagesClaimed != null) {
					setDamagesClaimed(String.format("%,.2f",
							((BigDecimal) damagesClaimed)));
					
					claimedValueTotal = claimedValueTotal.add(damagesClaimed);
					
					smallerClaimProcesses++;
				}
				
				if (!allSettlementNull && !allDamagesNull) {
					if (settlement.compareTo(damagesClaimed) == 1) {
						setSmallerClaimProcess(true);
					}
				}
			}
			
		}
		
		/**
		 * If it is a work-accidents' policy, the settlement is the sum of the
		 * benefits from the medical details, from the medical files
		 */
		private BigDecimal getSettlementFromMedicalFiles(
				SubCasualty subCasualty, BigDecimal prevValue)
				throws BigBangJewelException {

			IEntity filesEntity;
			MasterDB database;
			ResultSet fetchedFiles;
			BigDecimal result = prevValue == null ? BigDecimal.ZERO : prevValue;
			boolean allResultNull = true;

			try {
				filesEntity = Entity.GetInstance(Engine.FindEntity(
						Engine.getCurrentNameSpace(),
						Constants.ObjID_MedicalFile));
				database = new MasterDB();
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try {
				fetchedFiles = filesEntity.SelectByMembers(database,
						new int[] { MedicalFile.I.SUBCASUALTY },
						new java.lang.Object[] { subCasualty.getKey() },
						new int[] { MedicalFile.I.REFERENCE });
				if (fetchedFiles == null) {
					if (prevValue == null) {
						return null;
					} else {
						return prevValue;
					}
				}
			} catch (Throwable e) {
				try {
					database.Disconnect();
				} catch (SQLException e1) {
				}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try {
				while (fetchedFiles.next()) {
					for (MedicalDetail tmp : (MedicalFile.GetInstance(
							Engine.getCurrentNameSpace(), fetchedFiles)
							.GetCurrentDetails())) {
						if (tmp.getAt(MedicalDetail.I.BENEFITS) != null) {
							result = result.add((BigDecimal) tmp
									.getAt(MedicalDetail.I.BENEFITS));
							allResultNull = false;
						}
					}
				}
			} catch (Throwable e) {
				try {
					fetchedFiles.close();
				} catch (SQLException e1) {
				}
				try {
					database.Disconnect();
				} catch (SQLException e1) {
				}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try {
				fetchedFiles.close();
			} catch (Throwable e) {
				try {
					database.Disconnect();
				} catch (SQLException e1) {
				}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try {
				database.Disconnect();
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if (allResultNull == true && prevValue == null) {
				return null;
			}
			return result;
		}
			
		/**
		 * This method gets the closing date from a sub-casualty
		 */
		private String getClosingDateFomLogs(SubCasualty subCasualty) throws BigBangJewelException {
			
			StringBuilder logsQuery;
			
			// Logs' entity
			IEntity logsEntity;
			
			MasterDB database;
			ResultSet fetchedLogs;
			ArrayList<PNLog> logsList = null;
			
			logsQuery = new StringBuilder();
			
			try {
				// Gets the entity to fetch
				logsEntity = Entity.GetInstance(Engine.FindEntity(
						Engine.getCurrentNameSpace(), Constants.Process_Log));

				// The query "part" responsible for getting the sub-casualties
				logsQuery.append(logsEntity.SQLForSelectByMembers(
						new int[] { 1 /* corresponds to column FKOperation @ credite_egs.tblPNLogs */ },
						new java.lang.Object[] { subCasualty.GetProcessID() }, null));
						
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}
			
			// Gets the MasterDB
			try {
				database = new MasterDB();
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}

			// Fetches the sub-casualties
			try {
				fetchedLogs = database.OpenRecordset(logsQuery
						.toString());
			} catch (Throwable e) {
				try {
					database.Disconnect();
				} catch (SQLException e1) {
				}
				throw new BigBangJewelException(e.getMessage(), e);
			}
			
			// Adds the logs to an arraylist
			try {
				while (fetchedLogs.next()) {
					logsList.add(PNLog.GetInstance(
							Engine.getCurrentNameSpace(), fetchedLogs));
				}
			} catch (Throwable e) {
				try {
					fetchedLogs.close();
				} catch (SQLException e1) {
				}
				try {
					database.Disconnect();
				} catch (SQLException e1) {
				}
				throw new BigBangJewelException(e.getMessage(), e);
			}
			
			// sorts the logs
			Collections.sort(logsList, new Comparator<PNLog>() {
		        @Override
		        public int compare(PNLog l1, PNLog l2) {

		            return  l2.GetTimestamp().compareTo(l1.GetTimestamp());
		        }
		    });
			
			if (logsList.size()>0) {
				return logsList.get(0).GetTimestamp().toString().substring(0, 10);
			}
			
			return null;
		}
	}
	
	/**
	 * This method gets the closed sub-casualties
	 */
	protected SubCasualty[] getSubCasualties(String[] reportParams)
			throws BigBangJewelException {

		StringBuilder subCasualtyQuery;
		
		// Sub-casualty, Casualty and Logs' entities
		IEntity subCasualtyEntity;
		IEntity casualtyEntity;

		MasterDB database;
		ResultSet fetchedSubCasualties;
		ArrayList<SubCasualty> subCasualtiesList;

		if (Utils.getCurrentAgent() != null) {
			return new SubCasualty[0];
		}
		
		subCasualtyQuery = new StringBuilder();
		try {
			// Gets the entity to fetch
			subCasualtyEntity = Entity.GetInstance(Engine.FindEntity(
					Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			casualtyEntity = Entity.GetInstance(Engine.FindEntity(
					Engine.getCurrentNameSpace(), Constants.ObjID_Casualty));

			// The query "part" responsible for getting the sub-casualties
			subCasualtyQuery.append("SELECT * FROM ("
					+ subCasualtyEntity.SQLForSelectAll()
					+ ") [AuxSubC] WHERE [Casualty] IN (SELECT [PK] FROM (");
					
				// If a client was defined in the 
				if (!paramClientUUID.equals(NO_VALUE)) {
					subCasualtyQuery.append(casualtyEntity.SQLForSelectByMembers(
									new int[] { Casualty.I.CLIENT },
									new java.lang.Object[] { paramClientUUID }, null));
				} else {
					subCasualtyQuery.append(casualtyEntity.SQLForSelectAll());
				}
			subCasualtyQuery.append(") [AuxCas] WHERE 1=1");
					
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
		
		// Appends the Casualty filter by date
		if (!paramStartDate.equals(NO_VALUE)) {
			subCasualtyQuery.append(" AND [Date] >= '").append(paramStartDate)
					.append("'");
		}
		if (!paramEndDate.equals(NO_VALUE)) {
			subCasualtyQuery.append(" AND [Date] < DATEADD(d, 1, '")
					.append(paramEndDate).append("')");
		}
		
		subCasualtyQuery.append(")");
		
		// Adds the query part responsible for getting the closed processes
		try {
			subCasualtyQuery
					.append(" AND [Process] IN ( select FKProcess from ("
							+ " select * FROM ("
							+ " SELECT *,"
							+ "ROW_NUMBER() OVER (PARTITION BY FKPROCESS ORDER BY _TSCREATE DESC) AS rn"
							+ " FROM credite_egs.tblPNLogs "
							+ " where FKOperation in ('"
							+ Constants.OPID_SubCasualty_CloseProcess + "', '"
							+ Constants.OPID_SubCasualty_ExternReopenProcess
							+ "')" + " and Undone=0) last_op where rn = 1 "
							+ ") final_op " + "where FKOperation = '"
							+ Constants.OPID_SubCasualty_CloseProcess + "'");
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
		
		subCasualtyQuery.append(")");
		
		// Gets the MasterDB
		try {
			database = new MasterDB();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Fetches the sub-casualties
		try {
			fetchedSubCasualties = database.OpenRecordset(subCasualtyQuery
					.toString());
		} catch (Throwable e) {
			try {
				database.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		
		subCasualtiesList = new ArrayList<SubCasualty>();

		// Adds the sub-casualties to an arraylist
		try {
			while (fetchedSubCasualties.next()) {
				subCasualtiesList.add(SubCasualty.GetInstance(
						Engine.getCurrentNameSpace(), fetchedSubCasualties));
			}
		} catch (Throwable e) {
			try {
				fetchedSubCasualties.close();
			} catch (SQLException e1) {
			}
			try {
				database.Disconnect();
			} catch (SQLException e1) {
			}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if (subCasualtiesList.size() > 0) {
			totalProcesses = subCasualtiesList.size();
		}
		
		return subCasualtiesList.toArray(new SubCasualty[subCasualtiesList
				.size()]);
	}	
	
	/**
	 * The method responsible for creating the report
	 */
	public GenericElement[] doReport(String[] reportParams)
			throws BigBangJewelException {
		
		// Sets the class variables for the parameters
		if ((reportParams[0] != null) && !"".equals(reportParams[0])) {
			paramClientUUID = reportParams[0];
			Client client = Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(reportParams[0]));
			paramClientName = (String) client.getAt(Client.I.NAME);;
		} 		
		if ((reportParams[1] != null) && !"".equals(reportParams[1])) {
			paramStartDate = reportParams[1];
		}		
		if ((reportParams[2] != null) && !"".equals(reportParams[2])) {
			paramEndDate = reportParams[1];
		}
		
		// The sub-casualties, to display at the report
		SubCasualty[] subCasualties = getSubCasualties(reportParams);
		
		return null;
	}
}
