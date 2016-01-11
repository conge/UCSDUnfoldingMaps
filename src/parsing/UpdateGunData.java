package parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.data.PointFeature;

public class UpdateGunData {
	//private String baseURL="http://www.gunviolencearchive.org";


	
	/** updateDataFile
	 * downloads the gun violence data in the past 72 hours.
	 * 
	 * @return path of the downloaded data file
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	
	public static String updateDataFile(String baseURL)  {
		// get the first page, static html
		String fileURL = null;
		String basePage		= getHtmlContent(baseURL+"/last-72-hours");
		List<String> links = getLink(basePage, "Export as CSV");
        String nextPageURL	= links.get(0).split("\"")[1];
		
        // nextPage is a dynamic page which got auto redirected several times
        // Use HtmlUnit to handle it here
		org.apache.log4j.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(org.apache.log4j.Level.FATAL);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
		
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38)) {
			// setup WebClient
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setTimeout(35000);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			// must use WaitingRefreshHandler. 
			// the default refreshHandler will cause runtime error
			// the ThreadedRefreshHandler will not get to the page we need.
			webClient.setRefreshHandler(new WaitingRefreshHandler());
			
	        // Get the dynamic page
			HtmlPage nextPage = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
	        nextPage = webClient.getPage(baseURL+nextPageURL);
			
	        // all the information to get the final file is in the page url
	        String finalPageURL = nextPage.getUrl().toString();
	        String[] splittedStr = finalPageURL.split("\\u003F|&");
	        
	        // reconstruct the file URL
	        fileURL=splittedStr[0]+"/download?"+splittedStr[2]+"&"+splittedStr[1];
	        
	        // System.out.println("Ln65:  Output: "+fileURL);
	        // System.out.println("Ln66 Expected: http://www.gunviolencearchive.org/export-finished/download?uuid=1e1fc646-a270-420e-8b41-3f497794831f&filename=public%3A//export-b2884e31-e9f7-4eec-a631-bf0d38e07fc5.csv");
	    } catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return fileURL;
		
	}
	public List<PointFeature> parseGunViolance(PApplet p, String dataURL) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		updateDataFile("http://www.gunviolencearchive.org");
		
		List<PointFeature> shootings = new ArrayList<PointFeature>();
		
		return shootings;
	}

	/** getHtmlContent
	 * 
	 * code adapted from:
	 * http://blog.csdn.net/yaohucaizi/article/details/8929325
	 * @param htmlurl url of a html page
	 * @return the content of the html 
	 */
	public static String getHtmlContent(String htmlurl) {  
        URL url;  
        String temp;  
        StringBuffer sb = new StringBuffer();  
        try {  
            url = new URL(htmlurl); 
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

          
            BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));// 读取网页全部内容  
            while ((temp = in.readLine()) != null) {  
                sb.append(temp);  
            }  
            in.close();  
        } catch (final MalformedURLException me) {  
            System.out.println("URL is Malformed!");  
            me.getMessage();  
        } catch (final IOException e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }
	
	/** getLink
	 *  adapted from 
	 * @param s String of html content
	 * @param key String, identify the anchor with the key as text.
	 * @return a list of String match the regex.
	 */
	public static List<String> getLink(String s, String key) {  
        String regex;  
        List<String> list = new ArrayList<String>();  
        regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>"+key +"</a>";
        
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);  
        Matcher ma = pa.matcher(s);  
        while (ma.find()) {  
            list.add(ma.group());  
        }  
        return list;  
    }  
	/*
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {  
		UpdateGunData t = new UpdateGunData();
		t.updateDataFile();    
	} 
	*/ 
}
