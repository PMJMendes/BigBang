package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.premiumminds.BigBang.Jewel.Listings.SubCasualtyListingsBase;

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
	private BigDecimal claimedValue = BigDecimal.ZERO; // number of claimed values
	private int smallerClaimProcesses = 0; // number of processes with a claimed value inferior to the one paid
	private int declinedCasualties = 0; // number of declined Casualties
	private int warnedDeclinedCasualties = 0; // number of declined Casualties that were warned by Credite-EGS
	
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
	}
		
}
