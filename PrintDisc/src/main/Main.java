package main;

import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.standard.Media;

public class Main {
	public static void main(String[] args) {
		for (PrintService lsvc : getPrinters()) {
			System.out.println("Printer: " + lsvc.getName());
			for (Media lm : getTrays(lsvc)) {
				System.out.println("  -> " + lm.toString() + " (" + lm.getClass().toString() + ")");
			}
		}
	}

	private static PrintService[] getPrinters() {
		return PrinterJob.lookupPrintServices();
	}

	private static Media[] getTrays(PrintService psvc) {
		return (Media[])psvc.getSupportedAttributeValues(Media.class, null, null);
	}
}
