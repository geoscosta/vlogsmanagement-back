package com.vmetrix.v3.pagination;

import java.util.List;
import java.util.Map;
/**
 * 
 * class of pagination
 *
 */
public class V3Pagination<T> {
	
	private int pageSelected;
	private int totalPerPage;
	private Map<String,V3SortType> sortMap;
	
	/**
	 * constructor
	 * @param pageSelected
	 * @param totalPerPage
	 * @param objects
	 */
	public V3Pagination(int pageSelected, int totalPerPage, Object...objects) {
		super();
		this.pageSelected = pageSelected;
		this.totalPerPage = totalPerPage;
		
		convertObjectsInMapOfSortTypeAndCollumns(objects);
	}

	private Map<String,V3SortType> convertObjectsInMapOfSortTypeAndCollumns(Object[] objects) {
		return null;
	}

	/**
	 * enum to sort type of pagination
	 *
	 */
	public enum V3SortType {
		ASC, DESC
	}

	/**
	 * getPage of pagination
	 * @return
	 */
	public V3Pagination<T> getPage() {
		return null;
	}

	/**
	 * get the dataSet list
	 * @return
	 */
	public List<T> getDataSet() {
		return null;
	}

	/**
	 * get totalPerPage
	 * @return totalPerPage
	 */
	public int getTotalPerPage() {
		return totalPerPage;
	}

	/**
	 * get page selected
	 * @return pageSelected
	 */
	public int getPageSelected() {
		return pageSelected;
	}

	/**
	 * set the number of page selected
	 * @param pageSelected
	 */
	public void setPageSelected(int pageSelected) {
		this.pageSelected = pageSelected;
	}

	/**
	 * set totalPerPage
	 * @param totalPerPage
	 */
	public void setTotalPerPage(int totalPerPage) {
		this.totalPerPage = totalPerPage;
	}

	/**
	 * get sort map 
	 * @return sortMap
	 */
	public Map<String, V3SortType> getSortMap() {
		return sortMap;
	}

	/**
	 * set Sort Map 
	 * @param sortMap
	 */
	public void setSortMap(Map<String, V3SortType> sortMap) {
		this.sortMap = sortMap;
	}
	
}
