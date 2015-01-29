package ks.personal.stackoverflowqa.factory;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import ks.personal.stackoverflowqa.constants.GlobalConstants;
import ks.personal.stackoverflowqa.controller.DataPopulator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

/**
 * The Class PdfGenerator.
 */
public class PdfGenerator {
	
	/** The Constant className. */
	private static final String className = PdfGenerator.class.getName();
	
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(className);

	/** The question title font. */
	private static Font questionTitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

	/** The question descrb font. */
	private static Font questionDescrbFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

	/** The answer descrb font. */
	private static Font answerDescrbFont = new Font(Font.FontFamily.COURIER, 10, Font.ITALIC);
	
	private static Font warningInfoFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL); 



	public Boolean validateDateFormat(String fromDate, String toDate) {
		boolean bval = false;
		SimpleDateFormat[] possibleFormats = new SimpleDateFormat[] { new SimpleDateFormat("dd-MM-yyyy") };

		Date retValfromDate = null;
		Date retValtoDate = null;
		for (SimpleDateFormat f : possibleFormats) {
			f.setLenient(false);
			try {
				retValfromDate = f.parse(fromDate);
				retValtoDate = f.parse(toDate);
			} catch (ParseException e) {
				logger.error("ParseException "+e.getMessage());
			}
		}
		
		logger.info("From date - "+ retValfromDate + "To date -" + retValtoDate);
		
		if (retValfromDate == null || retValtoDate == null) {

			bval = false;

		} else {
			bval = true;
		}
		return bval;

	}

	/**
	 * Adds the meta data.
	 * 
	 * @param document
	 *            the document
	 */
	public void addMetaData(Document document) {
		document.addTitle("StackOverFlow_QA");
		document.addSubject("StackOverFlow_QA");
		document.addKeywords("StackOverFlow,KS");
		document.addAuthor("K S Nidhin");
		document.addCreator("K S Nidhin");
	}

	/**
	 * Adds the title page.
	 * 
	 * @param document
	 *            the document
	 * @param tagValue 
	 * @param toDate 
	 * @param fromDate 
	 * @throws Exception 
	 */
	public void addTitlePage(Document document, String fromDate, String toDate, String tagValue) throws Exception {
		
		int quotaRemaining = DataPopulator.checkForQuota(fromDate, toDate, tagValue);
		
		if (quotaRemaining == 0) {
			
			logger.warn("You have exceded the fetch quota for the day !! Try again tommorrow !!");
			return;
		}

		Paragraph preface = new Paragraph();

		addEmptyLine(preface, 1);

		questionTitleFont.setColor(BaseColor.MAGENTA);
		preface.add(new Paragraph("Stackoverflow QA Collection ", questionTitleFont));
		addEmptyLine(preface, 1);

		preface.add(new Paragraph("PDF generated by: " + System.getProperty("user.name") + "  , " + new Date(),
				questionDescrbFont));

		preface.add(new Paragraph(GlobalConstants.nameTextEffectString,warningInfoFont));
		addEmptyLine(preface, 1);
		preface.add(new Paragraph(GlobalConstants.sitenameTextEffectString,warningInfoFont));

		addEmptyLine(preface, 7);

		preface.add(new Paragraph(
				"This document is a preliminary version and to be used only for educational purposes and "
						+ "should NOT be reproduced in any form. Sorry if the doc doesnt look appealing"
						+ " , will improve it ;) . .Share the doc at your own risk ;-) -- K S Nidhin", answerDescrbFont));

		
		
		addEmptyLine(preface, 1);
		warningInfoFont.setColor(BaseColor.RED);
		preface.add(new Paragraph(DataPopulator.checkForMoreResults(fromDate, toDate, tagValue),warningInfoFont));

		document.add(preface);

		document.newPage();
	}



	/**
	 * Adds the qa page.
	 *
	 * @param document the document
	 * @param questItemsMap the quest items map
	 * @throws Exception the exception
	 */
	public void addQAPage(Document document, HashMap<Integer, List<String>> questItemsMap) throws Exception {

		HashMap<Integer, List<String>> ansItemsMap;
		int questionMapCount = questItemsMap.size();

		for (int i = 0; i < questionMapCount; i++) {

			String quesTitle = questItemsMap.get(i + 1).get(1);
			String quesBody = questItemsMap.get(i + 1).get(2);
			String quesQuestionId = questItemsMap.get(i + 1).get(0);
			String quesAcceptedAnsId = questItemsMap.get(i + 1).get(3);

			ansItemsMap = DataPopulator.getAnswerItems(quesAcceptedAnsId);

			String ansQuestionId = ansItemsMap.get(1).get(0);
			String ansAnswerId = ansItemsMap.get(1).get(1);
			String ansBody = ansItemsMap.get(1).get(2);
			

			Paragraph questionTitle = new Paragraph();
			addEmptyLine(questionTitle, 1);

			questionTitleFont.setColor(BaseColor.BLUE);
			questionTitle.add(new Paragraph(quesTitle, questionTitleFont));

			addEmptyLine(questionTitle, 1);

			document.add(questionTitle);
			addQuestionDescrb(document, quesBody);
			addEmptyLine(questionTitle, 1);
			addAnswerTitle(document);
			addAnswerDescrb(document, ansBody);
			dottedLineSeparator(document);
		}

	}

	/**
	 * Dotted line separator.
	 * 
	 * @param document
	 *            the document
	 * @throws DocumentException
	 *             the document exception
	 */
	private static void dottedLineSeparator(Document document) throws DocumentException {

		DottedLineSeparator separator = new DottedLineSeparator();

		separator.setPercentage(59500f / 523f);
		Chunk linebreak = new Chunk(separator);

		document.add(linebreak);

	}

	/**
	 * Adds the answer title.
	 * 
	 * @param document
	 *            the document
	 * @throws DocumentException
	 *             the document exception
	 */
	private static void addAnswerTitle(Document document) throws DocumentException {

		Paragraph answerTitle = new Paragraph();
		answerDescrbFont.setColor(BaseColor.BLUE);

		answerTitle.add(new Paragraph("Answer : ", questionTitleFont));

		document.add(answerTitle);
	}

	/**
	 * Adds the question descrb.
	 *
	 * @param document the document
	 * @param quesBody the ques body
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void addQuestionDescrb(Document document, String quesBody) throws IOException {

		String quesBodyDesc = quesBody;
		quesBodyDesc = quesBodyDesc.replace("\n", "<br />");
		quesBodyDesc = quesBodyDesc.replace("<hr>", "");
		HTMLWorker htmlWorker = new HTMLWorker(document);
		
		htmlWorker.parse(new StringReader(quesBodyDesc));

	}

	/**
	 * Adds the answer descrb.
	 *
	 * @param document the document
	 * @param ansBody the ans body
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void addAnswerDescrb(Document document, String ansBody) throws IOException {

		String ansBodyDesc = ansBody;

		ansBodyDesc = ansBodyDesc.replace("\n", "<br />");
		ansBodyDesc = ansBodyDesc.replace("<hr>", "");
		HTMLWorker htmlWorker = new HTMLWorker(document);

		htmlWorker.parse(new StringReader(ansBodyDesc));

	}

	/**
	 * Adds the empty line.
	 * 
	 * @param paragraph
	 *            the paragraph
	 * @param number
	 *            the number
	 */
	private static void addEmptyLine(Paragraph paragraph, int number) {

		for (int i = 0; i < number; i++) {

			paragraph.add(new Paragraph(" "));
		}
	}

}
