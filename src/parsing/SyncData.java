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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.data.PointFeature;

public class SyncData {

	private String baseURL="http://www.gunviolencearchive.org/";
	
	public String syncDataFile() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		String basePage=getHtmlContent(baseURL+"last-72-hours");
		org.apache.log4j.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(org.apache.log4j.Level.FATAL);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
		
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

	        // Get the first page
	        final HtmlPage page1 = webClient.getPage(baseURL+"last-72-hours");

	        // Get the form that we are dealing with and within that form, 
	        // find the submit button and the field that we want to change.
	        

	        final HtmlAnchor anchor = page1.getAnchorByName("Export as CSV");
	        System.out.println(anchor.asText());
	        

	        

	        // Now submit the form by clicking the button and get back the second page.
	        // final HtmlPage page2 = button.click();
	    }
		
		
		/*List<String> links = getLink(basePage,"Export as CSV");
		System.out.println("ln26: size of links = "+ links.size() + 
				" links[0] = "+ links.get(0));
		
		String[] temp = links.get(0).split("\"");
		System.out.println("ln30: temp[1]"+temp[1]);
		
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38)) {
		    webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		    HtmlPage myPage = ((HtmlPage) webClient.getPage(baseURL+temp[1]) );
		    webClient.waitForBackgroundJavaScript(10 * 1000);
		    webClient.wait(10*1000);

		    String theContent = myPage.asXml();
		    System.out.println(theContent);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String exportPage = getHtmlContent(baseURL+temp[1]);
		List<String> links2 = getLink(exportPage,"Download");
		System.out.println("ln32: size of links2 = "+ links2.size() + 
				" links2[0] = "+ links2.get(0));
				
		*/
		
		return "s";
		
	}
	public List<PointFeature> parseGunViolance(PApplet p, String dataURL) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		syncDataFile();
		
		List<PointFeature> shootings = new ArrayList<PointFeature>();
		
		return shootings;
	}

	public String getHtmlContent(String htmlurl) {  
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
            System.out.println("你输入的URL格式有问题!");  
            me.getMessage();  
        } catch (final IOException e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }
	
	public List<String> getLink(String s, String key) {  
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
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {  
		SyncData t = new SyncData();
		t.syncDataFile();
        
        }  
}
