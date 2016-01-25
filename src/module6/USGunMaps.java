package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
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
	// statesFiles was downloaded from http://eric.clst.org/Stuff/USGeoJSON
	
	private String statesFile = "gz_2010_us_040_00_500k.json";
	List<Marker> statesMarkers;
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each incident
	private List<Marker> gunMarkers;

	HashMap<String, Float> gunViolences;
	
	private int numKilled = 0;
	private int numInjured = 0;
	private int numIncidents;

	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		int zoomLevel = 4;
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
		
		List<PointFeature> gunViolences =  ParseFeed.loadgunDataFromCSV(this,gunDataURL);
		gunMarkers = new ArrayList<Marker>();
		
		for(Feature gun : gunViolences) {
			this.numKilled= this.numKilled + (int) gun.getProperty("numKilled");
			this.numInjured= this.numInjured + (int) gun.getProperty("numInjured");
			gunMarkers.add(new GunMarker(gun));
		  
		}
	   this.numIncidents = gunMarkers.size();
	   
		// Load country polygons and adds them as markers
		map.zoomAndPanTo(zoomLevel, new Location(39.50f, -98.35f));
		map.addMarkers(gunMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	// helper method to draw key in GUI
		private void addKey() {	
			// Remember you can use Processing's graphics methods here
			fill(255, 250, 240);
			
			int xbase = 25;
			int ybase = 50;
			
			rect(xbase, ybase, 150, 250);
			
			fill(0);
			textAlign(LEFT, CENTER);
			textSize(12);
			text("U.S. Gun Vialences ", xbase+25, ybase+25);
			text("in the past 3 days", xbase+35, ybase+40);
			
			
			fill(color(255, 255, 0));
			ellipse(xbase+35, ybase+80, 12, 12);
			fill(color(0, 0, 255));
			ellipse(xbase+35, ybase+60, 12, 12);
			fill(color(255, 0, 0));
			ellipse(xbase+35, ybase+100, 12, 12);
			
			textAlign(LEFT, CENTER);
			fill(0, 0, 0);
			text("Everyone safe", xbase+50, ybase+60);
			text("People killed", xbase+50, ybase+80);
			text("People injured", xbase+50, ybase+100);

			
			text(this.numInjured + " people were injured;",xbase+50, ybase+140);
			text(this.numKilled + " people were killed,",xbase+50, ybase+160);
			text("in " + this.numIncidents + " gun-related incidents",xbase+50, ybase+180);
			text("during the past 3 days ",xbase+50, ybase+200);
		}
	
}
