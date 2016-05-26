package net.system.utils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Pager - 分页辅助类
 * 
 * 新建后:设置 pageNumber, pageSize, property 设置查询的属性 keyword : 设置查询的关键字. orderBy :
 * 设置排序的属性. orderType : 设置排序的顺序. totalCount : 被结果赋值.
 * 
 * !!带有校验信息
 */
public class PageUtils {

	/* 排序方式 */
	public enum OrderType {
		asc, desc
	}

	@NotNull
	@Min(1)
	private Integer pageNumber = 1; // 当前页码

	@NotNull
	@Min(1)
	private Integer pageSize = 15; // 每页记录数

	private Integer totalCount = 0; // 总记录数
	private Integer pageCount = 0; // 总页数
	private String property; // 查找属性名称
	private String keyword; // 查找关键字
	private String orderBy = "createDate"; // 排序字段
	private OrderType orderType = OrderType.desc; // 排序方式

	public int getFirstResult() {
		return (pageNumber - 1) * pageSize;
	}

	public int getMaxResults() {
		return pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = Integer.parseInt("" + totalCount);
	}

	public Integer getPageCount() {
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			++pageCount;
		}
		return pageCount;
	}

	/**
	 * @param pageCount
	 *            由 totalcount 和 pageSize 计算得到 <br/>
	 *            不需要手动设置
	 */
	@Deprecated
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderTypeStr() {
		if (orderType == OrderType.asc) {
			return "asc";
		} else {
			return "desc";
		}
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

}
