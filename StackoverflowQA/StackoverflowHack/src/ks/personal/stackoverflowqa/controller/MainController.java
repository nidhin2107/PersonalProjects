package ks.personal.stackoverflowqa.controller;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import ks.personal.stackoverflowqa.factory.PdfGenerator;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * The Class MainController.
 */
public class MainController {

	/** The Constant className. */
	private static final String className = MainController.class.getName();
	
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(className);

	/** The from date. */
	private static String fromDate = null;
	
	/** The to date. */
	private static String toDate = null;
	
	/** The tag value. */
	private static String tagValue = null;
	
	/** The FILE. */
	private static String FILE = null;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Launching ...");
		long startTime = System.currentTimeMillis();

		int argPosition = 0;

		if (args.length < 4) {
			logger.info("Not sufficient arguments \n");
			logger.info("Usage - jarfile "
					+ "<fromDate in dd-mm-yyyy> <todate in dd-mm-yyyy> <tag eg: hadoop> <output file full path with filename eg: /user/123/myStackQA.pdf>");
			return;
		}

		fromDate = args[argPosition++];
		toDate = args[argPosition++];
		tagValue = args[argPosition++];
		FILE = args[argPosition++];

		PdfGenerator pdfGenerator = new PdfGenerator();

		Boolean status = pdfGenerator.validateDateFormat(fromDate, toDate);
		System.out.println(status);
		if (!status) {
			logger.info("Entered Date is not in the required format \n");
			logger.info("Date format is dd-mm-yyyy");
			return;
		}

		Document document = new Document();
		HashMap<Integer, List<String>> questItemsMap = new HashMap<Integer, List<String>>();

		PdfWriter.getInstance(document, new FileOutputStream(FILE));
		document.open();
		pdfGenerator.addMetaData(document);
		pdfGenerator.addTitlePage(document,fromDate, toDate, tagValue);
		questItemsMap = DataPopulator.getQuestionItems(fromDate, toDate, tagValue);
		pdfGenerator.addQAPage(document, questItemsMap);

		document.close();

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		logger.info(String.format("Completed in %s ms.", elapsedTime));
		System.exit(0);

	}

}
