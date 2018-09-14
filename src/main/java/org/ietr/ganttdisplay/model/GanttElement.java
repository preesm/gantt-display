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
/**
 * 
 */
package org.ietr.ganttdisplay.model;

import java.awt.Color;

/**
 * @author mpelcat
 *
 */
public class GanttElement {
	
	/**
	 * Starting date
	 */
	private Long start = 0l;

	/**
	 * End date
	 */
	private Long end = 0l;

	/**
	 * Display name of the Gantt element
	 */
	private String title = null;

	/**
	 * Display name of the mapping line of the Gantt element
	 */
	private String mapping = null;

	/**
	 * Color of the Gantt element
	 */
	private Color color = null;
	
	public GanttElement(Long start, Long end, String title, Color color, String mapping) {
		this.start = start;
		this.end = end;
		this.title = title;
		this.color = color;
		this.mapping = mapping;
	}

	public Long getStart() {
		return start;
	}

	public Long getEnd() {
		return end;
	}

	public String getTitle() {
		return title;
	}

	public Color getColor() {
		return color;
	}

	public String getMapping() {
		return mapping;
	}

	@Override
	public String toString() {
		return "GanttElement [start=" + start + ", end=" + end + ", title="
				+ title + ", mapping=" + mapping + ", color=" + color + "]";
	}
}
