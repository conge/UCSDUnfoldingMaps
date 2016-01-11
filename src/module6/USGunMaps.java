package module6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import parsing.UpdateGunData;
import processing.core.PApplet;

/** USGunMaps
 * An application with an interactive map to display gun violence report 
 * for the past 72 hours in the US.
 * Data source is from http://www.gunviolencearchive.org
 * @author Qingyang Li
 * Date: Jan 10, 2016
 */
public class USGunMaps extends PApplet {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private static final String baseURL = "http://www.gunviolencearchive.org";
	private String gunDataURL=UpdateGunData.updateDataFile(baseURL);
	
	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
		
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String statesFile = "gz_2010_us_040_00_500k.json";
	List<Marker> statesMarkers;
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each incident
	private List<Marker> gunMarkers;
	
	
	
	HashMap<String, Float> gunViolences;

	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    gunDataURL = "";
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load states features and markers
		// Load state polygons and adds them as markers
		
		// Load country polygons and adds them as markers
		List<Feature> states = GeoJSONReader.loadData(this, statesFile);
		statesMarkers = MapUtils.createSimpleMarkers(states);
		map.addMarkers(statesMarkers);
		System.out.println(statesMarkers.get(0).getId());
				

		//List<Feature> states = GeoJSONReader.loadData(this, statesFile);
		//statesMarkers = MapUtils.createSimpleMarkers(states);
		
		
		
		//     STEP 3: read in earthquake RSS feed
		
		gunViolences =  ParseFeed.loadgunDataFromCSV(this,gunDataURL);
		
	   
		
		
		map.addMarkers(statesMarkers);
		System.out.println(statesMarkers.get(0).getId());
	    
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		//addKey();
		
	}
	

}
