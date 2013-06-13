package hr.fer.ztel.myFridge.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import hr.fer.ztel.myFridge.data.Food;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class BarcodeSearchKonzum {

	private static final String FETCH_IMAGE_URI = "http://online.konzum.hr/images/products/";

	public static String getSourcePage(String searchUri) throws ClientProtocolException, IOException {

		if (searchUri == null || searchUri.isEmpty()) {
			return null;
		}

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(searchUri);

		HttpResponse response = null;
		response = httpclient.execute(httpGet);

		String searchResult = null;
		searchResult = EntityUtils.toString(response.getEntity());
		httpGet.releaseConnection();
		return searchResult;
	}

	public static Food foodDetailExtraction(String searchResult) throws IOException {
		Food item = new Food();
		String description = "";
		byte[] imageLarge = null;

		if (searchResult == null || searchResult.isEmpty()) {
			return null;
		}

		Pattern patternItemDescription = Pattern.compile("<strong>Opis:(.+?)</strong>(.+?)<br/>",
				Pattern.DOTALL);
		Pattern patternImage = Pattern.compile(
				"<img alt=\"\"(.+?)src=\"/images/products/(.+?)\"(.+?)/></a>", Pattern.DOTALL);

		Matcher itemDescriptionMatcher = patternItemDescription.matcher(searchResult);
		Matcher imageMatcher = patternImage.matcher(searchResult);

		if (itemDescriptionMatcher.find()) {
			description = itemDescriptionMatcher.group(2).trim();
		}
		if (imageMatcher.find()) {
			imageLarge = getImage(imageMatcher.group(2));
		}
		item.setDescription(description);
		item.setImageLarge(imageLarge);

		return item;

	}

	public static String detailsUriExtraction(String searchResult) {

		if (searchResult == null || searchResult.isEmpty()) {
			return null;
		}

		String detailsUri = "";
		Pattern patternDetailsUri = Pattern.compile(
				"<td class=\"row2\" >(.+?)<div>(.+?)<a href=\"(.+?)\" style", Pattern.DOTALL);
		Matcher detailsMatcher = patternDetailsUri.matcher(searchResult);
		if (detailsMatcher.find()) {
			detailsUri = detailsMatcher.group(3).trim();
		}

		return detailsUri;

	}

	public static Food foodMainDataExtraction(String searchResult, String itemBarcode)
			throws IOException {

		Food item = null;

		Pattern patternItemName = Pattern.compile("class=\"noFloat\">(.+?)</a></h3>");
		Pattern patternItemManufacturer = Pattern.compile("manufacturer\">(.+?)</div>", Pattern.DOTALL);
		Pattern patternImage = Pattern.compile("src=\"/images/products/(.+?)\"");

		Matcher matcher1 = patternItemName.matcher(searchResult);
		Matcher matcher2 = patternItemManufacturer.matcher(searchResult);
		Matcher matcher3 = patternImage.matcher(searchResult);

		if (matcher1.find() && matcher2.find() && matcher3.find()) {
			String itemName = matcher1.group(1).trim();
			String itemManufacturer = matcher2.group(1).trim();

			byte[] itemImageSmall = getImage(matcher3.group(1));

			item = new Food(null, itemBarcode, itemName, itemManufacturer, itemImageSmall, null, null);
		}

		return item;
	}

	private static byte[] getImage(String imageID) throws IOException {
		byte[] image = null;

		// http://online.konzum.hr
		URL url = new URL(FETCH_IMAGE_URI + imageID);

		InputStream inputStream = url.openStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int n = 0;
		while (-1 != (n = inputStream.read(buffer))) {
			output.write(buffer, 0, n);
		}
		inputStream.close();

		// the content of the image
		image = output.toByteArray();

		return image;

	}
}
