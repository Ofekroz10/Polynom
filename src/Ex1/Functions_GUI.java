package Ex1;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;


import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;





public class Functions_GUI implements functions
{
	private LinkedList<function> collection;
	private GuiParams guiParams;

	/*
	 * Initialize an object with empty Linked List of functions, initialize guiParams to (1000,600,400,new Range(-10,10),new Range(-15,15))
	 */
	public Functions_GUI()
	{
		collection = new LinkedList<function>();
		guiParams = new GuiParams(1000,600,400,new Range(-10,10),new Range(-15,15));

	}
	/*
	 * Initialize an object with functions given from a collection. Collection interface methods.
	 */
	public Functions_GUI(int width,int hight,int resolution,Range x,Range y)
	{
		collection = new LinkedList<function>();
		guiParams = new GuiParams(width,hight,resolution,x,y);
	}
	/*
	 * All this functions use with linkedlist functions
	 */
	@Override
	public boolean add(function arg0) {
		return(collection.add(arg0));

	}

	@Override
	public boolean addAll(Collection<? extends function> arg0) {
		return (collection.addAll(arg0));

	}

	@Override
	public void clear() {
		collection.clear();

	}

	@Override
	public boolean contains(Object arg0) {
		return (collection.contains(arg0));
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return (collection.containsAll(arg0));
	}

	@Override
	public boolean isEmpty() {
		return (collection.isEmpty());
	}

	@Override
	public Iterator<function> iterator() {
		return collection.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return (collection.remove(arg0));
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return collection.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return collection.retainAll(arg0);
	}

	@Override
	public int size() {
		return collection.size();
	}

	@Override
	public Object[] toArray() {
		return collection.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return(collection.toArray(arg0));
	}
	/*
	 * Initialize function to this object from a given file. (Throws IOExepction)
	 */
	@Override
	public void initFromFile(String file) throws IOException {
		try {
			BufferedReader abc = new BufferedReader(new FileReader(file));
			LinkedList<String> lines = new LinkedList<String>();
			String line;

			while((line = abc.readLine()) != null) {
				ComplexFunction c = new ComplexFunction("plus",null,null);
				line = line.replace(" ","");
				lines.add(line);
				function a  = c.initFromString(line);
				collection.add(a);
				//System.out.println(a.toString());

			}
			abc.close();

		}
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("file is not exist");
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	 * Save the object's functions on a file. (Create new file named "file.txt" if isnt exist. Throws IOExpection).
	 */
	@Override
	public void saveToFile(String file) throws IOException {
		try (FileWriter writer = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(writer)) {

			bw.write(this.collectionAsStr());
			bw.close();
			String jSonName = getJsonName(file);
			//this.toJson(jSonName);

		}

		catch(FileNotFoundException  e)
		{
			throw new  FileNotFoundException ("the file is not writble");
		}



	}
	
	private String getJsonName(String file) {
		return file.substring(0,file.length()-4)+".json";
	}
	/*
	 * Draw the object's function on a graphic window by given parameters.
	 */
	@Override
	public void drawFunctions(int width, int height, Range rx, Range ry, int resolution) {

		XYChart chart = new  XYChartBuilder().width(width).height(height).theme(ChartTheme.Matlab).build();
		drawXY(chart);
		// Customize Chart
		int j=0;
		int k =0;
		double steps = this.getResolutionForArray(rx,resolution);
		
		int arrayLen = getLen(steps);

		double[][]values = new double[collection.size()][arrayLen];
		double[] xAsis = new double[arrayLen];
		boolean init = false;
		//chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		for (function funcs : collection) 
		{
			for(double i =guiParams.getRange_x().get_min();i<guiParams.getRange_x().get_max();i+=steps)
			{
				values[j][k] = funcs.f(i);

				if(!init)
					xAsis[k] = i;
				k++;
			}

			k=0;
			init  = true;
			chart.addSeries(funcs.toString(), xAsis, values[j]).setMarker(SeriesMarkers.NONE);   
			j++;
		}
		chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
		chart.getStyler().setXAxisMax(guiParams.getRange_x().get_max());
		chart.getStyler().setXAxisMin(guiParams.getRange_x().get_min());
		chart.getStyler().setYAxisMax(guiParams.getRange_y().get_max());
		chart.getStyler().setYAxisMin(guiParams.getRange_y().get_min());
		//chart.getStyler().set


		new SwingWrapper(chart).displayChart();

	}

	private void drawXY(XYChart chart) {
		XYSeries x= (XYSeries) chart.addSeries("X asis", new int[]{-10000,10000}, new int[] {0,0}).setMarker(SeriesMarkers.NONE);
		x.setLineColor(Color.black);
		XYSeries y =(XYSeries) chart.addSeries("Y asis", new int[]{0,0}, new int[] {-10000,10000}).setMarkerColor(Color.black);
		y.setLineColor(Color.black);
	}
	/*
	 * Draw the object's function on a graphic window by given parameters on json file.
	 */
	@Override
	public void drawFunctions(String json_file) {
		try {
			fromJson(json_file);

			if( guiParams.FileItegrity()) {
				this.drawFunctions(this.guiParams.getWidth(),this.guiParams.getHight(),this.guiParams.getRange_x(),this.guiParams.getRange_y(),this.guiParams.getResolution());
			}else {
				throw new ArithmeticException("your json file not god");
			}



		}
		catch(IOException e)
		{
			System.out.println(json_file +" do not exist");
		}




	}

	/*
	 * return the collection as string
	 */
	private String collectionAsStr()
	{
		StringBuilder sb = new StringBuilder();
		for (function temp : collection) {
			sb.append( temp.toString()+"\n");
		}
		return sb.toString();
	}
	private void toJson(String filename) throws JsonIOException, IOException
	{
		try (FileWriter writer = new FileWriter(filename)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(this.guiParams, writer);
		}

		catch(FileNotFoundException  e)
		{
			throw new  FileNotFoundException ("the file is not writble");
		}
		catch(Exception e)
		{
			throw e;
		}

	}
	/*
	 * read from json file which his name is filename, and initialize guiParams with the data from the json file.
	 */
	public boolean fromJson(String filename) throws JsonIOException, IOException
	{
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new FileReader(filename));
			GuiParamsArray reviews = gson.fromJson(reader, GuiParamsArray.class);
			this.guiParams = reviews.toGuiParams();
			return true;
		}

		catch(FileNotFoundException  e)
		{
			throw new  FileNotFoundException ("the file is not writble");
		}
		catch(Exception e)
		{
			throw e;
		}

	}
	/*
	 * calculate the length of the array that represent the x and y coordination dependence on jump value.
	 */
	private int getLen(double jump)
	{
		int count = 0;
		for (double i =guiParams.getRange_x().get_min();i<guiParams.getRange_x().get_max();i+=jump)
		{
			count++;
		}
		return count;
	}
	/*
	 * return the resolution by calculate the range length/resolution.
	 */
	public double getResolutionForArray(Range range_x,int res) {
		double x1 = range_x.get_min();
		double x2 = range_x.get_max();
		double x = 0;
		if(x2>=0&&x1>=0)
			x= (x2-x1);
		else if(x2>=0&&x1<0)
			x= (x2+Math.abs(x1));
		else
			x=( Math.abs(x1)-Math.abs(x2));
		return x/res;
	}

	public GuiParams getGUIParams()
	{
		return this.guiParams.copy();
	}

}
