/*******************************************************************************
 * Copyright or © or Copr. IETR/INSA - Rennes 2018.
 * 
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 ******************************************************************************/
package org.ietr.ganttdisplay;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ietr.ganttdisplay.model.Gantt;
import org.ietr.ganttdisplay.model.GanttElement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

/**
 * Gantt plotter of a Gantt using JFreeChart
 *
 * @author mpelcat
 */
public class GanttPlotter {

	/**
	 * Initial dimensions of the window
	 */
	public static final int X_DIM = 700;
	public static final int Y_DIM = 500;

	/**
	 * Creates a chart.
	 *
	 * @param dataset
	 *            a dataset.
	 *
	 * @return A chart.
	 */
	private static JFreeChart createChart(Gantt gantt, IntervalCategoryDataset dataset) {

		JFreeChart chart = ChartFactory.createGanttChart("Solution Gantt", // title
				"Operators", // x-axis label
				"Time", // y-axis label
				null, // data
				true, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		Paint p = getBackgroundColorGradient();
		chart.setBackgroundPaint(p);

		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.black);
		final RectangleInsets rectangleInsets = new RectangleInsets(5.0, 5.0, 5.0, 5.0);
		plot.setAxisOffset(rectangleInsets);
		plot.setOrientation(PlotOrientation.HORIZONTAL);

		DateAxis xaxis = (DateAxis) plot.getRangeAxis();
		xaxis.setDateFormatOverride(new VertexDateFormat());
		xaxis.setPositiveArrowVisible(true);

		DefaultDrawingSupplier d = new DefaultDrawingSupplier();

		plot.setDrawingSupplier(d);
		GanttRenderer ren = new MyGanttRenderer();

		for(GanttElement element : gantt.getElementSet()){
			((MyGanttRenderer)ren).addColor(element.getTitle(), element.getColor());
		}

		ren.setSeriesItemLabelsVisible(0, false);
		ren.setSeriesVisibleInLegend(0, false);
		ren.setSeriesItemLabelGenerator(0,
				new IntervalCategoryItemLabelGenerator());
		ren.setSeriesToolTipGenerator(0, new MapperGanttToolTipGenerator());

		ren.setAutoPopulateSeriesShape(false);

		plot.setRenderer(ren);

		plot.setDataset(dataset);
		return chart;

	}

	/**
	 * Creates the JFreeChart data from the Gantt data
	 *
	 * @param gantt the input data
	 */
	private static IntervalCategoryDataset createDataset(Gantt gantt) {

		TaskSeries series = new TaskSeries("Scheduled");
		Map<String,Long> seriesDurations = new HashMap<>();

		for (String mapping : gantt.getMappings()){
			Task currenttask;

			currenttask = new Task(mapping, new SimpleTimePeriod(0, 1));
			series.add(currenttask);
			seriesDurations.put(mapping, 0l);
		}

		for (GanttElement element : gantt.getElementSet()){
			Task t = new Task(element.getTitle(), new SimpleTimePeriod(element.getStart(), element.getEnd()));
			series.get(element.getMapping()).addSubtask(t);

			long currentSerieDuration = seriesDurations.get(element.getMapping());
			seriesDurations.put(element.getMapping(), Math.max(currentSerieDuration, element.getEnd()));
		}


		for (Entry<String, Long> mapping : seriesDurations.entrySet()){
			series.get(mapping.getKey()).setDuration(
					new SimpleTimePeriod(0, mapping.getValue()));
		}


		TaskSeriesCollection collection = new TaskSeriesCollection();
		collection.add(series);

		return collection;

	}

	/**
	 * Creates the JFreeChart data from the Gantt data
	 *
	 * @param gantt the input data
	 */
	public void plot(Gantt gantt, ChartPanel chartPanel) {

		JFreeChart chart = createChart(gantt, createDataset(gantt));

		chartPanel.setChart(chart);

	}

	/**
	 * Creates the background color
	 */
	public static LinearGradientPaint getBackgroundColorGradient() {
		Point2D start = new Point2D.Float(0, 0);
		Point2D end = new Point2D.Float(X_DIM, Y_DIM);
		float[] dist = { 0.0f, 0.8f };
		Color[] colors = { new Color(170, 160, 190), Color.WHITE };
		return new LinearGradientPaint(start, end, dist, colors);
	}
}
