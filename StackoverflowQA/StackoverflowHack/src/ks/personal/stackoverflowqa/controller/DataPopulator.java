package ks.personal.stackoverflowqa.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import ks.personal.stackoverflowqa.beans.AnswerItemsPropertyTags;
import ks.personal.stackoverflowqa.beans.ItemsPropertyTags;
import ks.personal.stackoverflowqa.factory.Generator;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.Chunk;

/**
 * The Class Controller.
 */
public class DataPopulator {

	/** The Constant className. */
	private static final String className = DataPopulator.class.getName();

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(className);

	/**
	 * Read url.
	 * 
	 * @param urlString
	 *            the url string
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	private static String readUrl(String urlString) throws Exception {

		BufferedReader reader = null;
		try {

			URL url = new URL(urlString);

			GZIPInputStream gzip = new GZIPInputStream(url.openStream());
			reader = new BufferedReader(new InputStreamReader(gzip));

			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();

		} finally {
			if (reader != null)
				reader.close();
		}

	}

	/**
	 * Gets the question items.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param tagValue the tag value
	 * @return the question items
	 * @throws Exception the exception
	 */
	public static HashMap<Integer, List<String>> getQuestionItems(String fromDate, String toDate, String tagValue)
			throws Exception {

		String generatedQuestUrl = Generator.generateQuestionUrlString(fromDate, toDate, tagValue);

		String jsonString = readUrl(generatedQuestUrl);

		JSONObject jObject = new JSONObject(jsonString);
		JSONArray jArray = jObject.getJSONArray("items");

		ItemsPropertyTags itemsPropertyTags = new ItemsPropertyTags();
		HashMap<Integer, List<String>> itemsMap = new HashMap<Integer, List<String>>();
		List<String> itemsValues;

		for (int i = 0; i < jArray.length(); i++) {

			itemsValues = new ArrayList<String>();
			int count = i + 1;
			JSONObject jObj = jArray.getJSONObject(i);

			itemsPropertyTags.setBody(jObj.getString("body"));
			itemsPropertyTags.setAccepted_answer_id(jObj.getInt("accepted_answer_id"));
			itemsPropertyTags.setCreation_date(jObj.getInt("creation_date"));
			itemsPropertyTags.setQuestion_id(jObj.getInt("question_id"));
			itemsPropertyTags.setTitle(jObj.getString("title"));

			itemsValues.add(itemsPropertyTags.getQuestion_id().toString());
			itemsValues.add(itemsPropertyTags.getTitle());
			itemsValues.add(itemsPropertyTags.getBody());
			itemsValues.add(itemsPropertyTags.getAccepted_answer_id().toString());

			itemsMap.put(count, itemsValues);

			logger.info("Question_id = " + itemsPropertyTags.getQuestion_id() + "*** Accepted_answer_id = "
					+ itemsPropertyTags.getAccepted_answer_id());
		}

		logger.info("Total extracted questions count - "+itemsMap.size());
		return itemsMap;
	}

	/**
	 * Gets the answer items.
	 *
	 * @param quesAcceptedAnsId the ques accepted ans id
	 * @return the answer items
	 * @throws Exception the exception
	 */
	public static HashMap<Integer, List<String>> getAnswerItems(String quesAcceptedAnsId) throws Exception {

		String generatedAnswerUrl = Generator.generateAnswerUrlString(quesAcceptedAnsId);

		String jsonString = readUrl(generatedAnswerUrl);

		JSONObject jObject = new JSONObject(jsonString);
		JSONArray jArray = jObject.getJSONArray("items");

		AnswerItemsPropertyTags answerItemsPropertyTags = new AnswerItemsPropertyTags();
		HashMap<Integer, List<String>> answerItemsMap = new HashMap<Integer, List<String>>();
		List<String> answerItemsValues;

		for (int i = 0; i < jArray.length(); i++) {

			answerItemsValues = new ArrayList<String>();
			int count = i + 1;
			JSONObject jObj = jArray.getJSONObject(i);

			answerItemsPropertyTags.setAnswer_id(jObj.getInt("answer_id"));
			answerItemsPropertyTags.setBody(jObj.getString("body"));
			answerItemsPropertyTags.setIs_accepted(jObj.getBoolean("is_accepted"));
			answerItemsPropertyTags.setQuestion_id(jObj.getInt("question_id"));
			answerItemsPropertyTags.setCreation_date(jObj.getInt("creation_date"));

			answerItemsValues.add(answerItemsPropertyTags.getQuestion_id().toString());
			answerItemsValues.add(answerItemsPropertyTags.getAnswer_id().toString());
			answerItemsValues.add(answerItemsPropertyTags.getBody());

			answerItemsMap.put(count, answerItemsValues);

		}

		return answerItemsMap;
	}

	public static String checkForMoreResults(String fromDate, String toDate, String tagValue) throws Exception {
		
		String generatedQuestUrl = Generator.generateQuestionUrlString(fromDate, toDate, tagValue);
		

		String jsonString = readUrl(generatedQuestUrl);
		
		JSONObject jObject = new JSONObject(jsonString);
		boolean hasmorestatus= jObject.getBoolean("has_more");
		
		String message = null;
		
		if (hasmorestatus) {
			
			message =  "The queried date range has more results , decrease the timeline for more optimised resultsets";
		}
		
		return message;
	}

	public static int checkForQuota(String fromDate, String toDate, String tagValue) throws Exception {
		
		String generatedQuestUrl = Generator.generateQuestionUrlString(fromDate, toDate, tagValue);

		String jsonString = readUrl(generatedQuestUrl);

		JSONObject jObject = new JSONObject(jsonString);
		int quotaRemaining= jObject.getInt("quota_remaining");
		
	
		return quotaRemaining;
	}

}
