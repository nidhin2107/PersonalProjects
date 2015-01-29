package ks.personal.stackoverflowqa.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * The Class Generator.
 */
public class Generator {

	/** The Constant className. */
	private static final String className = Generator.class.getName();

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(className);

	/** The baseurl. */
	private static String baseurl = "http://api.stackexchange.com/2.2/";

	/** The filter info. */
	private static String filterInfo = "filter=withbody";

	/** The order detail. */
	private static String orderDetail = "order=desc";

	/** The sort info. */
	private static String sortInfo = "sort=activity";

	/** The quest url additional info. */
	private static String questUrlAdditionalInfo = "search/advanced";

	/** The site name. */
	private static String siteName = "stackoverflow";

	/** The page size. */
	private static String pageSize = "100";

	/**
	 * Generate answer url string.
	 * 
	 * @param answerId
	 *            the answer id
	 * @return the string
	 */
	public static String generateAnswerUrlString(String answerId) {

		String answerUrlString = baseurl + "answers/" + answerId + "?" + orderDetail + "&" + sortInfo + "&site="
				+ siteName + "&" + filterInfo;

		return answerUrlString;
	}

	/**
	 * Generate question url string.
	 * 
	 * @param fromDate
	 *            the from date
	 * @param toDate
	 *            the to date
	 * @param tagValue
	 *            the tag value
	 * @return the string
	 */
	public static String generateQuestionUrlString(String fromDate, String toDate, String tagValue) {

		long unixTimeFromDate = generateUnixTime(fromDate);
		long unixTimeToDate = generateUnixTime(toDate);

		String questUrlString = baseurl + questUrlAdditionalInfo + "?pagesize=" + pageSize + "&fromdate="
				+ unixTimeFromDate + "&todate=" + unixTimeToDate + "&" + orderDetail + "&" + sortInfo
				+ "&accepted=True&answers=1&tagged=" + tagValue + "&site=" + siteName + "&" + filterInfo;

		return questUrlString;

	}

	/**
	 * Generate unix time.
	 * 
	 * @param fromDate
	 *            the from date
	 * @return the long
	 */
	private static long generateUnixTime(String fromDate) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateInString = fromDate;
		Date date = null;
		try {

			date = formatter.parse(dateInString);

		} catch (ParseException e) {
			logger.error("Parse Exception @ generateUnixTime " + e.getStackTrace());
		}

		long unixTime = date.getTime() / 1000;

		return unixTime;

	}

}
