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

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.ietr.ganttdisplay.exception.NoFileNameException;
import org.ietr.ganttdisplay.ui.RefreshActionListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;

/**
 * Displaying in a swing frame the Gantt chart corresponding to the xml data
 * entered in the command line.
 *
 * @author mpelcat
 */
public class GanttDisplay {

	/**
	 * Main method. Parses the xml file name in the command line and displays
	 * data in a Gantt chart if data is correctly formatted
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		String fileName = null;

		try {
			fileName = parseFileName(args);
		} catch (NoFileNameException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(2);
		}

		File file = new File(fileName);
		JFrame frame = new JFrame("Gantt Chart Plotter");

		JButton button = new JButton("Refresh");
		frame.add(button, BorderLayout.SOUTH);

		ChartPanel chartPanel = new ChartPanel(ChartFactory.createGanttChart(
				"Solution Gantt", "Operators", "Time", null, true, true, false));
		chartPanel.setPreferredSize(new java.awt.Dimension(
				GanttPlotter.xDimension, GanttPlotter.yDimension));
		chartPanel.setMouseZoomable(true, false);
		frame.add(chartPanel);

		// Set the default close operation for the window, or else the
		// program won't exit when clicking close button
		// (The default is HIDE_ON_CLOSE, which just makes the window
		// invisible, and thus doesn't exit the app)
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Set the visibility as true, thereby displaying it
		frame.setVisible(true);
		frame.pack();

		button.addActionListener(new RefreshActionListener(chartPanel, file));
	}

	/**
	 * Parses the file name in the command line.
	 *
	 * @param args
	 */
	private static String parseFileName(String[] args)
			throws NoFileNameException {
		String fileName = null;
		boolean fileNameArg = false;

		for (String arg : args) {
			if (fileNameArg) {
				fileName = arg;
			}
			if (arg.equalsIgnoreCase("-f")) {
				fileNameArg = true;
			}
		}

		if (fileName == null) {
			throw (new NoFileNameException());
		}

		return fileName;
	}

}
