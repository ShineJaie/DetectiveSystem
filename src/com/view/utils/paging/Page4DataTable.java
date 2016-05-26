package com.view.utils.paging;

import java.util.List;
import java.util.Map;

/**
 * DataTables插件的分页参数
 */
public class Page4DataTable {
	private Integer draw = 1; // DataTables队列
	private Integer start = 0; // 当前页码
	private Integer length = 10; // 总记录数

	private Map<SearchCriterias, Object> search;

	private List<Map<ColumnCriterias, Object>> columns;

	private List<Map<OrderCriterias, Object>> order;

	public enum SearchCriterias {
		value, regex
	}

	public enum OrderCriterias {
		column, dir
	}

	public enum ColumnCriterias {
		data, name, searchable, orderable, searchValue, searchRegex
	}

	// ===========================================

	public int getFirstResult() {
		return start;
	}

	public int getMaxResults() {
		return length;
	}

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Map<SearchCriterias, Object> getSearch() {
		return search;
	}

	public void setSearch(Map<SearchCriterias, Object> search) {
		this.search = search;
	}

	public List<Map<ColumnCriterias, Object>> getColumns() {
		return columns;
	}

	public void setColumns(List<Map<ColumnCriterias, Object>> columns) {
		this.columns = columns;
	}

	public List<Map<OrderCriterias, Object>> getOrder() {
		return order;
	}

	public void setOrder(List<Map<OrderCriterias, Object>> order) {
		this.order = order;
	}

}
