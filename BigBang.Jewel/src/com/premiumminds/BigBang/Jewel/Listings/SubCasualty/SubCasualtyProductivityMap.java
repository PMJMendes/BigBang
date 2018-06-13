package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.AlignType;
import org.apache.ecs.GenericElement;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Strong;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Objects.PNLog;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualtyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;
import com.premiumminds.BigBang.Jewel.Objects.MedicalDetail;
import com.premiumminds.BigBang.Jewel.Objects.MedicalFile;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

/**
 * Class responsible for the creation of the Casualty Productivity Map
 */
public class SubCasualtyProductivityMap extends SubCasualtyListingsBase {
	
	// Constants
	private static final String NO_VALUE = "-";
	//private static final String TITLE_REPLACE_STR_1 = "<tit>";
	//private static final String TITLE_REPLACE_STR_2 = "<tit2>";
	private static final String TITLE_SPLIT = "<splt>";
	private static final String COL_01 = "N.º do" + TITLE_SPLIT + "Processo";
	private static final String COL_02 = "Data de" + TITLE_SPLIT + "Sinistro" + TITLE_SPLIT + "(____/__/__)";
	private static final String COL_03 = "Data de" + TITLE_SPLIT + "Participação" + TITLE_SPLIT + "(____/__/__)";
	private static final String COL_04 = "Data de" + TITLE_SPLIT + "Encerramento" + TITLE_SPLIT + "(____/__/__)";
	private static final String COL_05 = "Período" + TITLE_SPLIT + "de Gestão";
	private static final String COL_06 = "Existência" + TITLE_SPLIT + "de Processo" + TITLE_SPLIT + "Judicial";
	private static final String COL_07 = "Gestor do" + TITLE_SPLIT + "Sinistro";
	private static final String COL_08 = "Apólice";
	private static final String COL_09 = "Ramo";
	private static final String COL_10 = "Segurador";
	private static final String COL_11 = "Valores" + TITLE_SPLIT + "dos Danos" + TITLE_SPLIT + "Reclamados" + TITLE_SPLIT + "(€)";
	private static final String COL_12 = "Valores" + TITLE_SPLIT + "das Franquias";
	private static final String COL_13 = "Valores das" + TITLE_SPLIT + "Indemnizações" + TITLE_SPLIT + "(€)";
	private static final String COL_14 = "Processos" + TITLE_SPLIT + "Indemnizados" + TITLE_SPLIT + "(N.º)";
	private static final String COL_15 = "Processos" + TITLE_SPLIT + "com Valores" + TITLE_SPLIT + "Reclamados" + TITLE_SPLIT + "Inferiores" + TITLE_SPLIT + "ou Iguais" + TITLE_SPLIT + "aos Valores" + TITLE_SPLIT + "Indemnizados" + TITLE_SPLIT + "(N.º)";
	private static final String COL_16 = "Sinistros" + TITLE_SPLIT + "Declinados" + TITLE_SPLIT + "(N.º)";
	private static final String COL_17 = "Sinistros" + TITLE_SPLIT + "Declinados" + TITLE_SPLIT + "que Foram" + TITLE_SPLIT + "Avisados" + TITLE_SPLIT + "Previamente" + TITLE_SPLIT + "pela Credite-EGS" + TITLE_SPLIT + "que Seriam" + TITLE_SPLIT + "Declinados" + TITLE_SPLIT + "(N.º)";
	private static final String TOTAL_PROCESSES = "Número Total de Processos";
	private static final String LIN_PERCENT = "Percentagem de Processos";
	private static final String LIN_TOTAL = "Valores Totais";
	private static final int COLUMN_BREAK_POINT = 33;
	private static final String YES_STRING = "Sim";
	private static final String NO_STRING = "Não";
	private static final String CLIENT_ROW_STR = "Cliente: ";
	private static final int NR_OF_COLUMNS = 17;
	
	// Width Constants
	private static final int WIDTH_COL_01 = 60;
	private static final int WIDTH_COL_02 = 60;
	private static final int WIDTH_COL_03 = 60;
	private static final int WIDTH_COL_04 = 60;
	private static final int WIDTH_COL_05 = 60;
	private static final int WIDTH_COL_06 = 60;
	private static final int WIDTH_COL_07 = 60;
	private static final int WIDTH_COL_08 = 60;
	private static final int WIDTH_COL_09 = 60;
	private static final int WIDTH_COL_10 = 60;
	private static final int WIDTH_COL_11 = 60;
	private static final int WIDTH_COL_12 = 60;
	private static final int WIDTH_COL_13 = 60;
	private static final int WIDTH_COL_14 = 60;
	private static final int WIDTH_COL_15 = 60;
	private static final int WIDTH_COL_16 = 60;
	private static final int WIDTH_COL_17 = 60;
	
	// Class Variables
	private int totalProcesses = 0; // number of processes
	private BigDecimal settlementTotal = BigDecimal.ZERO; // valor total de indemnizações
	private int settledProcesses = 0; // number of settled processes
	private BigDecimal claimedValueTotal = BigDecimal.ZERO; // number of claimed values
	private int smallerClaimProcesses = 0; // number of processes with a claimed value inferior to the one paid
	private int declinedCasualties = 0; // number of declined Casualties
	private int warnedDeclinedCasualties = 0; // number of declined Casualties that were warned by Credite-EGS
	private String paramClientUUID = NO_VALUE;
	private String paramClientGroupUUID = NO_VALUE;
	private String paramClientName = NO_VALUE;
	private String paramClientGroupName = NO_VALUE;
	private String paramStartDate = NO_VALUE;
	private String paramEndDate = NO_VALUE;
	private BigDecimal deductibleValueTotal = BigDecimal.ZERO;
	
	private String schemaName = "credite_egs";
	
	/**
	 * Inner class which holds the information to display at the map
	 */
	private class SubCasualtyData {
		
		private String closingDate;
		private String notificationDate;
		private String casualtyDate;
		private String managementTime;
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
		private boolean judicialProcess;
		private String deductibleValue;
		
		SubCasualtyData() {
			setClosingDate(NO_VALUE);
			setCasualtyDate(NO_VALUE);
			setManagementTime(NO_VALUE);
			setManager(NO_VALUE);
			setClient(NO_VALUE);
			setPolicyNumber(NO_VALUE);
			setCategory(NO_VALUE);
			setCompany(NO_VALUE);
			setCasualtyNumber(NO_VALUE);
			setSettlementValue(NO_VALUE);
			setSettledProcess(false);
			setDamagesClaimed(NO_VALUE);
			setSmallerClaimProcess(false);
			setDeclinedCasualty(false);
			setWarnedDeclinedCasualty(false);
			setNotificationDate(NO_VALUE);
			setJudicialProcess(false);
			setDeductibleValue(NO_VALUE);
		}
		
		private String getClosingDate() {
			return closingDate;
		}
		private void setClosingDate(String closingDate) {
			this.closingDate = closingDate;
		}
		private String getCasualtyDate() {
			return casualtyDate;
		}
		private void setCasualtyDate(String casualtyDate) {
			this.casualtyDate = casualtyDate;
		}
		private String getManagementTime() {
			return managementTime;
		}
		private void setManagementTime(String longStr) {
			this.managementTime = longStr;
		}
		private String getManager() {
			return manager;
		}
		private void setManager(String manager) {
			this.manager = manager;
		}
		private String getClient() {
			return client;
		}
		private void setClient(String client) {
			this.client = client;
		}
		private String getPolicyNumber() {
			return policyNumber;
		}
		private void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}
		private String getCategory() {
			return category;
		}
		private void setCategory(String category) {
			this.category = category;
		}
		private String getCompany() {
			return company;
		}
		private void setCompany(String company) {
			this.company = company;
		}
		private String getCasualtyNumber() {
			return casualtyNumber;
		}
		private void setCasualtyNumber(String casualtyNumber) {
			this.casualtyNumber = casualtyNumber;
		}
		private String getSettlementValue() {
			return settlementValue;
		}
		private void setSettlementValue(String settlementValue) {
			this.settlementValue = settlementValue;
		}
		private boolean isSettledProcess() {
			return settledProcess;
		}
		private void setSettledProcess(boolean settledProcess) {
			this.settledProcess = settledProcess;
		}
		private String getDamagesClaimed() {
			return damagesClaimed;
		}
		private void setDamagesClaimed(String damagesClaimed) {
			this.damagesClaimed = damagesClaimed;
		}
		private boolean isSmallerClaimProcess() {
			return smallerClaimProcess;
		}
		private void setSmallerClaimProcess(boolean smallerClaimProcess) {
			this.smallerClaimProcess = smallerClaimProcess;
		}
		private boolean isDeclinedCasualty() {
			return declinedCasualty;
		}
		private void setDeclinedCasualty(boolean declinedCasualty) {
			this.declinedCasualty = declinedCasualty;
		}
		private boolean isWarnedDeclinedCasualty() {
			return warnedDeclinedCasualty;
		}
		private void setWarnedDeclinedCasualty(boolean warnedDeclinedCasualty) {
			this.warnedDeclinedCasualty = warnedDeclinedCasualty;
		}
		private String getNotificationDate() {
			return notificationDate;
		}
		private void setNotificationDate(String notificationDate) {
			this.notificationDate = notificationDate;
		}
		private boolean isJudicialProcess() {
			return judicialProcess;
		}
		private void setJudicialProcess(boolean judicialProcess) {
			this.judicialProcess = judicialProcess;
		}
		private String getDeductibleValue() {
			return deductibleValue;
		}
		private void setDeductibleValue(String deductibleValue) {
			this.deductibleValue = deductibleValue;
		}
		
		// This method sets the management time according to the casualty date and closing date
		private void setManagementTime(Timestamp startDate, Timestamp endDate) {
			
			  long diffDays = calculateDaysInterval(startDate, endDate);
			  
			  String longStr = Long.toString(diffDays);
			  
			  setManagementTime(longStr);
		}
		
		/**
		 * This method sets the report values, giving a sub-casualty
		 */
		private void setValues(SubCasualty subCasualty)
				throws BigBangJewelException {
			
			Timestamp startDate = null;
			Timestamp closingDate = null;
			
			// Sets the closing date, if possible
			String closingDateFomLogs = getClosingDateFomLogs(subCasualty);
			if (closingDateFomLogs!=null) {
				closingDate = Timestamp.valueOf(closingDateFomLogs + " 00:00:00.0");
			}
			if (closingDate != null) {
				setClosingDate(closingDate.toString().substring(0, 10));
			}
			
			// Sets the notification date, if possible
			String notificationDateFomLogs = getNotificationDateFomLogs(subCasualty);
			if (notificationDateFomLogs!=null) {
				startDate = Timestamp.valueOf(notificationDateFomLogs + " 00:00:00.0");
			}
			if (startDate != null) {
				setNotificationDate(startDate.toString().substring(0, 10));
			}
			
			// Sets the casualty's date, if possible
			if (subCasualty.GetCasualty().getAt(Casualty.I.DATE) != null) {
				setCasualtyDate(subCasualty.GetCasualty().getAt(Casualty.I.DATE)
						.toString().substring(0, 10));
			}
			
			// Sets the management Time, if possible
			if (startDate!=null && closingDate!=null) {
				setManagementTime(startDate, closingDate);
			}			
			
			// Sets the judicial process' flag
			if (subCasualty.getAt(SubCasualty.I.HASJUDICIAL) != null) {
				//mbHasJudicial      = (Boolean)   pobjSource.getAt(SubCasualty.I.HASJUDICIAL)
				setJudicialProcess(((Boolean) subCasualty.getAt(SubCasualty.I.HASJUDICIAL)).booleanValue());
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
				setPolicyNumber(subCasualty.getAbsolutePolicy().getAt(Policy.I.NUMBER).toString());
			}
			
			// Sets the sub-casualty's category, if possible
			if (subCasualty.getAbsolutePolicy().GetSubLine() != null &&
				subCasualty.getAbsolutePolicy().GetSubLine().getLine() != null &&
				subCasualty.getAbsolutePolicy().GetSubLine().getLine().getCategory() != null) {

				String catStr = subCasualty.getAbsolutePolicy().GetSubLine().getLine().getCategory().getLabel() + " / " +
						subCasualty.getAbsolutePolicy().GetSubLine().getLine().getLabel() + " / " +
						subCasualty.getAbsolutePolicy().GetSubLine().getLabel();
				
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

				BigDecimal deductible = BigDecimal.ZERO;
				BigDecimal settlement = BigDecimal.ZERO;
				BigDecimal damagesClaimed = BigDecimal.ZERO;
						
				// needed to identify if there's no value for all, in case a '-' must be outputted, and not a 0
				boolean allDeductibleNull = true;
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
					if (temp.getAt(SubCasualtyItem.I.DAMAGES) != null) {
						damagesClaimed = damagesClaimed.add((BigDecimal) temp
								.getAt(SubCasualtyItem.I.DAMAGES));
							allDamagesNull = false;
					}
					// Franquia
					if (temp.getAt(SubCasualtyItem.I.DEDUCTIBLE) != null) {
						deductible = deductible.add((BigDecimal) temp
								.getAt(SubCasualtyItem.I.DEDUCTIBLE));
						allDeductibleNull = false;
					}
				}
				
				// If there is no items, the values are null
				if (currentItems.length == 0) {
					settlement = null;
					damagesClaimed = null;
					deductible = null;
					setSettledProcess(false);
				}
				
				if (allSettlementNull) {
					settlement = null;
					setSettledProcess(false);
				}
				if (allDamagesNull) {
					damagesClaimed = null;
				}
				if (allDeductibleNull) {
					deductible = null;
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
				}
				if (deductible != null) {
					setDeductibleValue(String.format("%,.2f",
							((BigDecimal) deductible)));
					
					deductibleValueTotal = deductibleValueTotal.add(deductible);
				}
				
				if (!allSettlementNull && !allDamagesNull) {
					if (settlement.compareTo(damagesClaimed) == 1) {
						setSmallerClaimProcess(true);
						smallerClaimProcesses++;
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
		 * This method gets the date where the sub-casualty was created, from the 
		 * casualty's history
		 */
		private String getNotificationDateFomLogs(SubCasualty subCasualty) throws BigBangJewelException {

			StringBuilder logsQuery;
			
			// Logs' entity
			IEntity logsEntity;
			
			MasterDB database;
			ResultSet fetchedLogs;
			ArrayList<PNLog> logsList = new ArrayList<PNLog>();
			
			logsQuery = new StringBuilder();
			
			try {
				// Gets the entity to fetch
				logsEntity = Entity.GetInstance(Engine.FindEntity(
						Engine.getCurrentNameSpace(), Constants.Process_Log));

				// The query "part" responsible for getting the logs
				logsQuery.append(logsEntity.SQLForSelectByMembers(
						new int[] { 0 /* corresponds to column FKOperation @ credite_egs.tblPNLogs */ },
						new java.lang.Object[] { subCasualty.GetCasualty().GetProcessID() }, null));
						
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}
			
			// Gets the MasterDB
			try {
				database = new MasterDB();
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}

			// Fetches the sub-casualty's logs
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
			
			for (PNLog tmp : logsList) {
				try {
					// Checks whether the log describes the creation of a sub-casualty
					if (tmp.GetOperation().getKey().equals(Constants.OPID_Casualty_CreateSubCasualty)) {
						// Checks whether the sub-casualty created is the one to which we're trying to 
						// get the date
						if (tmp.GetExternalProcess().equals(subCasualty.GetProcessID())) {
							return tmp.GetTimestamp().toString().substring(0, 10);
						}
					}
				} catch (JewelPetriException e) {
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
			
			return null;
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
			ArrayList<PNLog> logsList = new ArrayList<PNLog>();
			
			logsQuery = new StringBuilder();
			
			String result = null;
			
			try {
				// Gets the entity to fetch
				logsEntity = Entity.GetInstance(Engine.FindEntity(
						Engine.getCurrentNameSpace(), Constants.Process_Log));

				// The query "part" responsible for getting the logs
				logsQuery.append(logsEntity.SQLForSelectByMembers(
						new int[] { 0 /* corresponds to column FKOperation @ credite_egs.tblPNLogs */ },
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

			// Fetches the sub-casualty's logs
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

		            return  l1.GetTimestamp().compareTo(l2.GetTimestamp());
		        }
		    });
			
			// the following cycle only works because the logs were ordered 
			for (PNLog tmp : logsList) {
				try {
					// Checks whether the log describes the sub-casualty being marked for closing, not undone, stores the date
					if (tmp.GetOperation().getKey().equals(Constants.OPID_SubCasualty_MarkForClosing) && !tmp.IsUndone()) {
						result = tmp.GetTimestamp().toString().substring(0, 10);
						continue;
					}
					// Checks whether the log marked as closed, was later reversed, and cleans the closing date 
					if (tmp.GetOperation().getKey().equals(Constants.OPID_SubCasualty_UndoMarkForClosing) && !tmp.IsUndone()) {
						result = null;
						continue;
					}
					// Checks whether the log marked as closed, was later rejected, and cleans the closing date 
					if (tmp.GetOperation().getKey().equals(Constants.OPID_SubCasualty_RejectClosing) && !tmp.IsUndone()) {
						result = null;
						continue;
					}
				} catch (JewelPetriException e) {
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
			
			return result;
		}
	}
	
	/**
	 * This method gets sub-casualties
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
				
				if (!paramClientGroupUUID.equals(NO_VALUE)) {
					IEntity clientEntity;
					try {
						clientEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
						subCasualtyQuery.append(" WHERE [t1].[fkclient] IN (SELECT [PK] FROM (")
								.append(clientEntity.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {paramClientGroupUUID}, null))
								.append(") [AuxCli])");
					}
					catch (Throwable e)
					{
						throw new BigBangJewelException(e.getMessage(), e);
					}
				}
				
			subCasualtyQuery.append(") [AuxCas] WHERE 1=1");
					
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
		subCasualtyQuery.append(")");
		
		// Adds the query part responsible for getting the closed processes
		try {
			subCasualtyQuery
					.append(" AND [Process] IN ( select FKProcess from ("
							+ " select * FROM ("
							+ " SELECT *,"
							+ "ROW_NUMBER() OVER (PARTITION BY FKPROCESS ORDER BY _TSCREATE DESC) AS rn"
							+ " FROM " + schemaName + ".tblPNLogs "
							+ " where FKOperation in ('"
							+ Constants.OPID_SubCasualty_MarkForClosing + "', '"
							+ Constants.OPID_SubCasualty_RejectClosing + "', '"
							+ Constants.OPID_SubCasualty_UndoMarkForClosing
							+ "')" + " and Undone=0"); 
			
			// Appends the Casualty filter by date (the Timestamp for the closing action)
			if (!paramStartDate.equals(NO_VALUE)) {
				subCasualtyQuery.append(" AND [TStamp] >= '").append(paramStartDate)
						.append("'");
			}
			if (!paramEndDate.equals(NO_VALUE)) {
				subCasualtyQuery.append(" AND [TStamp] < DATEADD(d, 1, '")
						.append(paramEndDate).append("')");
			}
							
			subCasualtyQuery
					.append(") last_op where rn = 1 "
							+ ") final_op " + "where FKOperation = '"
							+ Constants.OPID_SubCasualty_MarkForClosing + "'");
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
		
		if (!Utils.getCurrency().equals("€")) {
			schemaName = "bbangola"; // TODO: Fast fix to work in Angola and Alvalade, but a different solution must be implemented (soon, while developing the new report) 
		} 
		
		// Sets the class variables for the parameters
		if ((reportParams[0] != null) && !"".equals(reportParams[0])) {
			paramClientUUID = reportParams[0];
			Client client = Client.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(reportParams[0]));
			paramClientName = (String) client.getAt(Client.I.NAME);;
		} 	
		if ((reportParams[1] != null) && !"".equals(reportParams[1])) {
			paramClientGroupUUID = reportParams[1];
			ClientGroup clientGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(),
					UUID.fromString(paramClientGroupUUID));
			paramClientGroupName = (String) clientGroup.getAt(ClientGroup.I.NAME);
		} 	
		if ((reportParams[2] != null) && !"".equals(reportParams[2])) {
			paramStartDate = reportParams[2];
		}		
		if ((reportParams[3] != null) && !"".equals(reportParams[3])) {
			paramEndDate = reportParams[3];
		}
		
		// Calls the method that validates the parameters, and throws an exception (with the corresponding message) in case an error occurs.
		validateParams();
		
		// The sub-casualties, to display at the report
		SubCasualty[] subCasualties = getSubCasualties(reportParams);
		
		// The array of Data to display
		HashMap<String, ArrayList<SubCasualtyData>> subCasualtiesMap = new HashMap<String, ArrayList<SubCasualtyData>>();
		
		for (int i = 0; i < subCasualties.length; i++) {
			SubCasualtyData data = new SubCasualtyData();
			data.setValues(subCasualties[i]);
			String clientName = data.getClient();
			if (subCasualtiesMap.get(clientName) == null) {
				subCasualtiesMap.put(clientName,
						new ArrayList<SubCasualtyData>());
			}
			subCasualtiesMap.get(clientName).add(data);
		}
		
		return createReport(subCasualtiesMap, reportParams);
	}
	
	/**
	 * This methods validates the report parameters, and throws an exception
	 * that should be displayed to the user if there's something wrong
	 */
	private void validateParams() throws BigBangJewelException {
		
		// Only Client OR Client Group may be set
		if (!paramClientUUID.equals(NO_VALUE) && !paramClientGroupUUID.equals(NO_VALUE)) {
			throw new BigBangJewelException("Não se pode gerar o relatório simultâneamente para um cliente e grupo de clientes.");
		}
		
		// When you don't have a client or a group set...
		if (paramClientUUID.equals(NO_VALUE) && paramClientGroupUUID.equals(NO_VALUE)) {
			// Either the client/client group or the dates must be set
			if (paramStartDate.equals(NO_VALUE) && paramEndDate.equals(NO_VALUE)) {
				throw new BigBangJewelException("Deve definir-se o cliente/grupo de clientes OU um intervalo de tempo."); 
			}
			// You must define at least an initial date
			if (paramStartDate.equals(NO_VALUE)) {
				throw new BigBangJewelException("Ao gerar um relatório para todos uns clientes, deve definir pelo menos a data inicial."); 
			}
			// The dates' interval should not be bigger then 15 months
			Timestamp startDate = Timestamp.valueOf(paramStartDate
					+ " 00:00:00.0");
			Timestamp endDate = paramEndDate.equals(NO_VALUE) ? new Timestamp(
					System.currentTimeMillis()) : Timestamp
					.valueOf(paramEndDate + " 00:00:00.0");
			long days = calculateDaysInterval(startDate, endDate);
			if (days > 457) {
				throw new BigBangJewelException("Ao gerar um relatório para todos uns clientes, o intervalo não deve ser superior a 15 meses."); 
			}
		}
	}

	/**
	 * This is the initial method "designing" the report
	 */
	private GenericElement[] createReport(HashMap<String, ArrayList<SubCasualtyData>> subCasualtiesMap, String[] reportParams)
			throws BigBangJewelException {

		GenericElement[] reportResult = new GenericElement[1];

		// Builds the table with the report. This
		// "table with one TR with one TD" is needed
		// to export to Excel
		Table table;
		TR[] tableRows = new TR[1];
		TD mainContent = new TD();
		mainContent.addElement(buildSubCasualtyMap(subCasualtiesMap, reportParams));
		tableRows[0] = ReportBuilder.buildRow(new TD[] { mainContent });

		table = ReportBuilder.buildTable(tableRows);

		reportResult[0] = table;

		return reportResult;
	}

	/**
	 * This is the method which creates the header, the sub-casualties'lines,  
	 * the totals' line and the notes
	 */
	private Table buildSubCasualtyMap(
			HashMap<String, ArrayList<SubCasualtyData>> subCasualtiesMap, String[] reportParams) throws BigBangJewelException {

		Table table;
		
		// Sets the number of rows (basically sections) of the report
		// considering the fact it has/doesn't have notes
		int numberOfRows = (reportParams[3] == null || reportParams[3]
				.trim().equals("")) ? 2 : 3;
		
		TR[] tableRows = new TR[numberOfRows];
		
		// Builds the header row
		TD headerContent = new TD();
		try {
			headerContent.addElement(buildHeader());
		} catch (BigBangJewelException e) {
			throw new BigBangJewelException("Could not build the report's header " + e.getMessage(), e);
		}
		tableRows[0] = ReportBuilder.buildRow(new TD[] { headerContent });
		
		// Builds the row with the main content
		TD infoContent = new TD();
		
		infoContent.addElement(buildContentTable(subCasualtiesMap));
		tableRows[1] = ReportBuilder.buildRow(new TD[] { infoContent });
		
		if (numberOfRows == 3) {
			TD reportNotes = new TD();
			reportNotes.addElement(buildReportNotesTable(reportParams));
			tableRows[2] = ReportBuilder.buildRow(new TD[] { reportNotes });
		}

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, false);
		table.setStyle("width:100%;");	
		
		return table;
	}

	/**
	 * The method responsible for printing the table with the content
	 */
	private Table buildContentTable(HashMap<String, ArrayList<SubCasualtyData>> subCasualtiesMap) {

		Table table;
		TR[] tableRows;
		TD mainContent;
		int rowNum = 0;

		tableRows = new TR[3];

		// Builds the row corresponding to the "real content" of the report -
		// meaning... the values from the policies
		mainContent = new TD();
		mainContent.setColSpan(2);
		mainContent.addElement(buildReportInfo(subCasualtiesMap));
		tableRows[rowNum++] = ReportBuilder.buildRow(new TD[] { mainContent });

		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, false);

		return table;
	}

	/**
	 * The method responsible for printing the sub-casualties' info
	 */
	private Table buildReportInfo(HashMap<String, ArrayList<SubCasualtyData>> subCasualtiesMap) {

		Table table;
		int nrOfRows = subCasualtiesMap.size() == 1 ? 0 : subCasualtiesMap.size(); // the client names' rows (when more than one client)
		nrOfRows += 4; // the titles, and summary rows
		for (ArrayList<SubCasualtyData> val : subCasualtiesMap.values()) {
			nrOfRows += val.size();
		}
		TR[] tableRows = new TR[nrOfRows];
		int rowNum = 0;
		
		// Builds the row with the column names
		tableRows[rowNum++] = ReportBuilder
				.buildRow(buildTitlesRow());
		ReportBuilder.styleRow(tableRows[0], true);
		
		for (String clName : subCasualtiesMap.keySet()) {
			if (subCasualtiesMap.size() > 1) {
				tableRows[rowNum++] = constructClientRow(clName);
			}
			for (SubCasualtyData subCasualty: subCasualtiesMap.get(clName)) {
				tableRows[rowNum] = ReportBuilder
						.buildRow(buildSubCasualtyRow(subCasualty));
				ReportBuilder.styleRow(tableRows[rowNum++], false);
			}
		}
		
		// Builds the row with the total number of policies
		tableRows[rowNum++] = constructTotalProcessesRow();
		tableRows[rowNum++] = constructSummaryRow(LIN_TOTAL, false);
		tableRows[rowNum++] = constructSummaryRow(LIN_PERCENT, true);
		
		table = ReportBuilder.buildTable(tableRows);
		ReportBuilder.styleTable(table, false);

		return table;
	}

	/**
	 * The method responsible for printing a row with a single sub-casualty's info
	 */
	private TD[] buildSubCasualtyRow(SubCasualtyData subCasualty) {

		TD[] cells = new TD[NR_OF_COLUMNS];
		
		int curCol = 0;
		
		cells[curCol] = safeBuildCell(subCasualty.getCasualtyNumber(),
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, false);
		
		cells[curCol] = safeBuildCell(subCasualty.getCasualtyDate(),
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getNotificationDate(),
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getClosingDate(),
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getManagementTime(),
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		String boolStr = subCasualty.isJudicialProcess() ? YES_STRING : NO_STRING;
		cells[curCol] = safeBuildCell(boolStr,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getManager(),
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getPolicyNumber(),
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		String categoryName = subCasualty.getCategory();
		if (categoryName.length() <= COLUMN_BREAK_POINT) {
			cells[curCol] = safeBuildCell(categoryName,
					TypeDefGUIDs.T_String, false, false);
		} else {
			ArrayList<String> categoryArray = new ArrayList<String>();
			categoryArray.add(categoryName);
			cells[curCol] = buildValuesTable(
					splitValue(categoryArray, COLUMN_BREAK_POINT),
					WIDTH_COL_09, false, false);
		}
		styleCenteredCell(cells[curCol++], true, true);
		
		String companyName = subCasualty.getCompany();
		if (companyName.length() <= COLUMN_BREAK_POINT) {
			cells[curCol] = safeBuildCell(companyName,
					TypeDefGUIDs.T_String, false, false);
		} else {
			ArrayList<String> companyArray = new ArrayList<String>();
			companyArray.add(companyName);
			cells[curCol] = buildValuesTable(
					splitValue(companyArray, COLUMN_BREAK_POINT),
					WIDTH_COL_10, false, false);
		}
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getDamagesClaimed(),
				TypeDefGUIDs.T_String, true, true);
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getDeductibleValue(),
				TypeDefGUIDs.T_String, true, true);
		styleCenteredCell(cells[curCol++], true, true);
		
		cells[curCol] = safeBuildCell(subCasualty.getSettlementValue(),
				TypeDefGUIDs.T_String, true, true);
		styleCenteredCell(cells[curCol++], true, true);		
		
		boolStr = subCasualty.isSettledProcess() ? YES_STRING : NO_STRING;
		cells[curCol] = safeBuildCell(boolStr,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);		
		
		boolStr = subCasualty.isSmallerClaimProcess() ? YES_STRING : NO_STRING;
		cells[curCol] = safeBuildCell(boolStr,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		boolStr = subCasualty.isDeclinedCasualty() ? YES_STRING : NO_STRING;
		cells[curCol] = safeBuildCell(boolStr,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		boolStr = subCasualty.isWarnedDeclinedCasualty() ? YES_STRING : NO_STRING;
		cells[curCol] = safeBuildCell(boolStr,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		return cells;
	}

	/**
	 * The method responsible for printing the column names' row
	 */
	private TD[] buildTitlesRow() {

		TD[] cells = new TD[NR_OF_COLUMNS];
		
		String[] allTitles = {COL_01, COL_02, COL_03, COL_04, COL_05, COL_06, 
				COL_07, COL_08, COL_09, COL_10, COL_11, COL_12, COL_13, 
				COL_14, COL_15, COL_16, COL_17};
		
		for (int i=0; i<allTitles.length; i++) {
			String tmp = allTitles[i];
			
			if (!tmp.contains(TITLE_SPLIT)) {
				cells[i] = ReportBuilder.buildCell(allTitles[i],
						TypeDefGUIDs.T_String);
				cells[i].setAlign("center");
				ReportBuilder.styleCell(cells[i], false, (i!=0));
			} else {
				String[] tmpArr = tmp.split(TITLE_SPLIT);
				
				Table table;
				TR[] tableRows = new TR[tmpArr.length];
				
				for (int u=0; u<tmpArr.length; u++) {
					TD[] tdArr = new TD[1];
					tdArr[0] = ReportBuilder.buildCell(tmpArr[u], TypeDefGUIDs.T_String);
					tdArr[0].setAlign("center");
					ReportBuilder.styleCell(tdArr[0], false, false);
					tableRows[u] = ReportBuilder.buildRow(tdArr);
				}
				table = ReportBuilder.buildTable(tableRows);
				cells[i] = new TD(table);
				cells[i].setAlign("center");
				ReportBuilder.styleCell(cells[i], false, (i!=0));
			}			
		}
		
		setCellWidths(cells);

		return cells;
	}

	/**
	 * This method builds the report's header, with a credite's logo, the
	 * report's title and the date the report was extracted
	 */
	private Table buildHeader() throws BigBangJewelException {

		Table table;
		TR[] tableRows = new TR[1];
		TD[] cells = new TD[3];
		
		// Builds the cell with the header image
		TD imageContent = new TD();
		imageContent.addElement(getImage());
		imageContent.setAlign(AlignType.left);
		imageContent.setStyle("width:10%; padding-bottom:15px;");
		cells[0] = imageContent;
		
		// Builds the cell with the report title and client name
		TD title = new TD();
		
		String titleString = "Sub-processos de sinistros";
		if (!paramClientName.equals(NO_VALUE)) {
			titleString += " de " + paramClientName;
		}
		if (!paramClientGroupName.equals(NO_VALUE)) {
			titleString += " do grupo " + paramClientGroupName;
		}
		titleString += " encerrados";
		if (!paramStartDate.equals(NO_VALUE)) {
			titleString += " desde " + paramStartDate;
		}
		if (!paramEndDate.equals(NO_VALUE)) {
			titleString += " até " + paramEndDate;
		}
		titleString += ".";
		
		Strong titleStrong = new Strong(titleString);
		titleStrong.setStyle("font-size: 15px;");
		title.addElement(titleStrong);
		title.setAlign(AlignType.middle);
		title.setStyle("padding-top:30px;");
		cells[1] = title;
		
		// Builds the cell with the date the report was extracted
		TD dateTd = new TD();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Strong dateStrong = new Strong(dateFormat.format(date).toString());
		dateStrong.setStyle("font-size: 15px;");
		dateTd.addElement(dateStrong);
		dateTd.setAlign(AlignType.right);
		dateTd.setStyle("width:10%; padding-top:80px;");
		cells[2] = dateTd;

		tableRows[0] = ReportBuilder.buildRow(cells);
		tableRows[0].setStyle("height:135px; font-weight:bold;");

		table = ReportBuilder.buildTable(tableRows);
		table.setStyle("width:100%;");

		return table;
	}
	
	/**
	 * This method builds the section with the notes the user added to the
	 * report upon generation
	 */
	private Table buildReportNotesTable(String[] reportParams)
			throws BigBangJewelException {

		Table table;
		TR[] tableRows = new TR[2];
		TD[] notes = new TD[1];

		String reportNotes = reportParams[4];
		if (reportNotes == null || reportNotes.isEmpty())
		{
			reportNotes = "&nbsp;";
		}

		// Creates a line with the word "Observações"
		notes[0] = ReportBuilder.buildCell("Observações",
				TypeDefGUIDs.T_String, false);
		notes[0].setStyle("padding-top:20px;padding-left:5px;width:100px;");
		tableRows[0] = ReportBuilder.buildRow(notes);
		tableRows[0].setStyle("height:50px;font-weight:bold;");

		notes = new TD[1];

		// Creates a line with the notes added as parameter
		Div textDiv = new Div(reportNotes);
		TD textTd = new TD();
		notes[0] = textTd;
		notes[0].addElementToRegistry(textDiv);
		notes[0].setColSpan(4);
		notes[0].setStyle("padding-top:20px;padding-left:5px;width:100px;word-wrap:break-word;");
		tableRows[1] = ReportBuilder.buildRow(notes);
		tableRows[1].setStyle("word-wrap:break-word;");

		table = ReportBuilder.buildTable(tableRows);
		table.setStyle("width:100%;");

		return table;
	}
	
	/**
	 * This method gets and configures the Crédite-EGS' logo to display at the
	 * top of the report
	 */
	private IMG getImage() throws BigBangJewelException {

		IMG result = new IMG();

		// Crédite-egs
		if (Utils.getCurrency().equals("€")) {
			result.setSrc("data:image/png;base64,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");
		} else {
			// Angola
			result.setSrc("data:image/png;base64,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");
		}
		result.setID("credite_logo");
		result.setBorder(0);
		result.setHeight(116);
		result.setWidth(180);

		return result;
	}
	
	/**
	 * This method builds the row with the information about the preceding
	 * sub-casualties' Client
	 */
	private TR constructClientRow(String clName) {

		TD rowContent;
		TR row;
		
		// Builds the TD with the content
		rowContent = ReportBuilder.buildCell(CLIENT_ROW_STR + clName,
				TypeDefGUIDs.T_String);
		rowContent.setColSpan(NR_OF_COLUMNS);
		ReportBuilder.styleCell(rowContent, true, false);

		// Builds the TR encapsulating the TD
		row = ReportBuilder.buildRow(new TD[] { rowContent });
		row.setStyle("height:35px;background:#e2f2ff;font-weight:bold;");

		return row;
	}
	
	/**
	 * This method builds a the row with the total number of processes
	 */
	private TR constructTotalProcessesRow() {

		TD[] cells = new TD[2];
		TR row;

		cells[0] = ReportBuilder.buildHeaderCell(TOTAL_PROCESSES);
		cells[0].setWidth("1px");
		cells[0].setColSpan(3);
		ReportBuilder.styleCell(cells[0], true, false);

		cells[1] = ReportBuilder.buildHeaderCell(totalProcesses + ""); 
		ReportBuilder.styleCell(cells[1], true, false);

		int colspan= NR_OF_COLUMNS - 1;
		
		cells[1].setColSpan(colspan);

		row = ReportBuilder.buildRow(cells);
		row.setStyle("height:30px;background:#e6e6e6;");

		return row;
	}
	
	/**
	 * This method builds a "summary row" with info to display at the end of the
	 * report. It is (REALLY) similar to ReportBuilder's constructDualRow
	 * method, but without the left line, and allowing to print a "money" value
	 * formatted properly.
	 */
	private TR constructSummaryRow(String text, boolean percent) {

		TD[] cells = new TD[8];
		TR row;
		
		int curCol = 0;
		
		String valToInsert = NO_VALUE;
		float percentFloat;

		cells[curCol] = ReportBuilder.buildHeaderCell(text);
		cells[curCol++].setColSpan(10);
		
		ReportBuilder.styleCell(cells[0], true, false);
		
		if (!percent || totalProcesses==0) {
			valToInsert = claimedValueTotal.toString();
		} else {
			valToInsert = NO_VALUE;
		}
		cells[curCol] = safeBuildCell(valToInsert,
				TypeDefGUIDs.T_String, !percent, !percent);
		styleCenteredCell(cells[curCol++], true, true);
		
		if (!percent || totalProcesses==0) {
			valToInsert = deductibleValueTotal.toString();
		} else {
			valToInsert = NO_VALUE;
		}
		cells[curCol] = safeBuildCell(valToInsert,
				TypeDefGUIDs.T_String, !percent, !percent);
		styleCenteredCell(cells[curCol++], true, true);
				
		if (!percent || totalProcesses==0) {
			valToInsert = settlementTotal.toString();
		} else {
			valToInsert = NO_VALUE;
		}
		cells[curCol] = safeBuildCell(valToInsert,
				TypeDefGUIDs.T_String, !percent, !percent);
		styleCenteredCell(cells[curCol++], true, true);
		
		if (!percent || totalProcesses==0) {
			valToInsert = String.valueOf(settledProcesses);
		} else {
			percentFloat = (settledProcesses * 100f) / totalProcesses;
			BigDecimal bd = new BigDecimal(Float.toString(percentFloat));
	        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);      
			valToInsert = bd.toString() + "%";
		}
		cells[curCol] = safeBuildCell(valToInsert,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		if (!percent || totalProcesses==0) {
			valToInsert = String.valueOf(smallerClaimProcesses);
		} else {
			percentFloat = (smallerClaimProcesses * 100f) / totalProcesses;
			BigDecimal bd = new BigDecimal(Float.toString(percentFloat));
	        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);      
			valToInsert = bd.toString() + "%";
		}
		cells[curCol] = safeBuildCell(valToInsert,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		if (!percent || totalProcesses==0) {
			valToInsert = String.valueOf(declinedCasualties);
		} else {
			percentFloat = (declinedCasualties * 100f) / totalProcesses;
			BigDecimal bd = new BigDecimal(Float.toString(percentFloat));
	        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);      
			valToInsert = bd.toString() + "%";
		}
		cells[curCol] = safeBuildCell(valToInsert,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		if (!percent || totalProcesses==0) {
			valToInsert = String.valueOf(warnedDeclinedCasualties);
		} else {
			percentFloat = (warnedDeclinedCasualties * 100f) / totalProcesses;
			BigDecimal bd = new BigDecimal(Float.toString(percentFloat));
	        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);      
			valToInsert = bd.toString() + "%";
		}
		cells[curCol] = safeBuildCell(valToInsert,
				TypeDefGUIDs.T_String, false, false);
		styleCenteredCell(cells[curCol++], true, true);
		
		row = ReportBuilder.buildRow(cells);
		row.setStyle("height:30px;background:#e6e6e6;");

		return row;
	}
	
	/**
	 * Builds a cell in a "safe way", meaning that if the value is null, the
	 * cell is built simply with a whitespace, preventing cells with the '?'
	 * character
	 */
	private TD safeBuildCell(java.lang.Object pobjValue, UUID pidType,
			boolean addEuro, boolean alignRight) {
		if (pobjValue == null || pobjValue.toString().trim().length() == 0) {
			return ReportBuilder.buildCell(NO_VALUE, TypeDefGUIDs.T_String);
		}
		if (addEuro && !pobjValue.toString().equals(NO_VALUE)) {
			String valueString = pobjValue + " " + Utils.getCurrency();
			return safeBuildCell(valueString, TypeDefGUIDs.T_String, false,
					alignRight);
		}
		return ReportBuilder.buildCell(pobjValue, pidType, alignRight);
	}
	
	/**
	 * Similar to ReportBuilder.styleCell, but makes the text centered
	 */
	public static void styleCenteredCell(TD pcell, boolean pbAddTop, boolean pbAddLeft) {
		pcell.setStyle("overflow:hidden;white-space:nowrap;padding-left:5px;padding-right:5px;text-align:center;" +
				(pbAddTop ? "border-top:1px solid #3f6d9d;" : "") +
				(pbAddLeft ? "border-left:1px solid #3f6d9d;" : ""));
	}
	
	/**
	 * This method sets the cells' width
	 */
	private void setCellWidths(TD[] cells) {
		
		int currColl = 0;
		cells[currColl++].setWidth(WIDTH_COL_01);
		cells[currColl++].setWidth(WIDTH_COL_02);
		cells[currColl++].setWidth(WIDTH_COL_03);
		cells[currColl++].setWidth(WIDTH_COL_04);
		cells[currColl++].setWidth(WIDTH_COL_05);
		cells[currColl++].setWidth(WIDTH_COL_06);
		cells[currColl++].setWidth(WIDTH_COL_07);
		cells[currColl++].setWidth(WIDTH_COL_08);
		cells[currColl++].setWidth(WIDTH_COL_09);
		cells[currColl++].setWidth(WIDTH_COL_10);
		cells[currColl++].setWidth(WIDTH_COL_11);
		cells[currColl++].setWidth(WIDTH_COL_12);
		cells[currColl++].setWidth(WIDTH_COL_13);
		cells[currColl++].setWidth(WIDTH_COL_14);
		cells[currColl++].setWidth(WIDTH_COL_15);
		cells[currColl++].setWidth(WIDTH_COL_16);
		cells[currColl++].setWidth(WIDTH_COL_17);
	}
	
	/**
	 * This method is used to split a string, which could be "too wide", and
	 * wreck the report's look. It gets not a String, but an array list with one
	 * element (the string) for it is the way it is represented in the
	 * CoverageData's class.
	 */
	private ArrayList<String> splitValue(ArrayList<String> stringToSplit,
			int breakPosition) {

		ArrayList<String> result = new ArrayList<String>();
		String[] split = stringToSplit.get(0).split("\\s+");
		String tmp = "";

		// Splits when it occupies more than (approximately) one row's length
		for (int i = 0; i < split.length; i++) {
			tmp = tmp + " " + split[i];
			if ((tmp.length() / (breakPosition - 6)) >= 1) {
				result.add(tmp);
				tmp = "";
			} else if (i + 1 == split.length) {
				result.add(tmp);
			}
		}

		return (result.size() == 0 ? stringToSplit : result);
	}
	
	/**
	 * This method returns a TD containing a value or a table of values,
	 * contained in an ArrayList. It's used to build tables to display inside a
	 * TD.
	 */
	private TD buildValuesTable(ArrayList<String> data, int width,
			boolean addEuro, boolean alignRight) {

		TD content;

		content = new TD();
		content.setColSpan(1);

		if (data == null) {
			return ReportBuilder.buildCell(" ", TypeDefGUIDs.T_String);
		}

		// Builds a "simple" TD or a table with a TD with each value
		if (data.size() == 1) {
			content = safeBuildCell(data.get(0), TypeDefGUIDs.T_String,
					addEuro, alignRight);
			content.setWidth(width);
			ReportBuilder.styleCell(content, false, false);
			ReportBuilder.styleInnerContainer(content);
		} else {

			Table table;
			TR[] tableRows = new TR[data.size()];

			// Iterates the values, and adds them to the table
			for (int i = 0; i < data.size(); i++) {
				TD[] cell = new TD[1];

				cell[0] = safeBuildCell(data.get(i), TypeDefGUIDs.T_String,
						addEuro, alignRight);
				/*cell[0].setWidth(width);
				cell[0].setStyle("overflow:hidden;white-space:nowrap");*/

				tableRows[i] = ReportBuilder.buildRow(cell);
				tableRows[i].setStyle(("height:15px;"));
			}

			table = ReportBuilder.buildTable(tableRows);
			ReportBuilder.styleTable(table, true);
			table.setStyle("background-color:white;margin-top:8px;margin-bottom:8px;width:100%;");

			content.addElement(new Div().addElement(table).setStyle(
					"width:inherit;"));
			ReportBuilder.styleInnerContainer(content);
		}

		return content;
	}

	/**
	 * Given two timestamps, this methods calculates the time interval in days.
	 */
	private long calculateDaysInterval(Timestamp startDate, Timestamp endDate) {
		long milliseconds1 = startDate.getTime();
		long milliseconds2 = endDate.getTime();

		long diff = milliseconds2 - milliseconds1;
		/*
		 * long diffSeconds = diff / 1000; long diffMinutes = diff / (60 *
		 * 1000); long diffHours = diff / (60 * 60 * 1000); long diffDays = diff
		 * / (24 * 60 * 60 * 1000);
		 */
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}
}
