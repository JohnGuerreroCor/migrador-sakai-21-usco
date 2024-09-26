package org.usco.lcms.utilidad;

import java.util.List;

public class JSONRespuesta {
	int draw;
	int recordsTotal;
	int recordsFiltered;
	List data;
	/**
	 * 
	 */
	public JSONRespuesta() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param draw
	 * @param recordsTotal
	 * @param recordsFiltered
	 * @param data
	 */
	public JSONRespuesta(int draw, int recordsTotal, int recordsFiltered, List data) {
		super();
		this.draw = draw;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
		this.data = data;
	}
	/**
	 * @return the draw
	 */
	public int getDraw() {
		return draw;
	}
	/**
	 * @param draw the draw to set
	 */
	public void setDraw(int draw) {
		this.draw = draw;
	}
	/**
	 * @return the recordsTotal
	 */
	public int getRecordsTotal() {
		return recordsTotal;
	}
	/**
	 * @param recordsTotal the recordsTotal to set
	 */
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	/**
	 * @return the recordsFiltered
	 */
	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	/**
	 * @param recordsFiltered the recordsFiltered to set
	 */
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	/**
	 * @return the data
	 */
	public List getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List data) {
		this.data = data;
	}
	
}
