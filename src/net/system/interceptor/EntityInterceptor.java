package net.system.interceptor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

/**
 * 过滤器 - 自动填充创建、更新时间
 */
@Component
public class EntityInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 1924167010850687731L;

	/** "创建日期"属性名称 */
	private static final String CREATE_DATE = "createDate";
	/** "修改日期"属性名称 **/
	private static final String MODIFY_DATE = "modifyDate";
	/** 待处理的实体对象 **/
	@SuppressWarnings("unused")
	private Object entity;

	static HashMap<String, Integer> createDate_forSearch = new HashMap<String, Integer>();
	static {
		createDate_forSearch.put("createDate_year", Calendar.YEAR);
		createDate_forSearch.put("createDate_month", Calendar.MONTH);
		createDate_forSearch.put("createDate_date", Calendar.DAY_OF_MONTH);
		createDate_forSearch.put("createDate_hour", Calendar.HOUR_OF_DAY);
		createDate_forSearch.put("createDate_minute", Calendar.MINUTE);
		createDate_forSearch.put("createDate_second", Calendar.SECOND);

	}

	/**
	 * 保存数据时回调此方法 <br/>
	 * 自动设置值：createDate, modifyDate, createDate_year, createDate_month,
	 * createDate_date, createDate_hour, createDate_minute, createDate_second,
	 * createDate_timeInMillis
	 **/
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {

		// 自动设置createDate 和 modifyDate
		this.entity = entity;
		Calendar cal = Calendar.getInstance();
		Date createDate = cal.getTime();
		for (int i = 0; i < propertyNames.length; i++) {
			if (CREATE_DATE.equals(propertyNames[i])
					|| MODIFY_DATE.equals(propertyNames[i])) {
				state[i] = createDate;
			}

			// 自动设置 createDate_forSearch 里面的属性值
			String perpname = propertyNames[i];
			if (createDate_forSearch.containsKey(perpname)) {
				int dateValue = cal.get(createDate_forSearch.get(perpname));
				state[i] = perpname.equals("createDate_month") ? dateValue + 1
						: dateValue;
			}

			// 自动设置 createDate_timeInMillis 毫秒值
			if (perpname.equals("createDate_timeInMillis")) {
				state[i] = cal.getTimeInMillis();
			}
		}
		return true;
	}

	/**
	 * 更新数据时回调此方法 <br/>
	 * 自动设置值：modifyDate
	 **/
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		this.entity = entity;
		Date updateDate = new Date();
		for (int i = 0; i < propertyNames.length; i++) {
			if (MODIFY_DATE.equals(propertyNames[i])) {
				currentState[i] = updateDate;
			}
		}
		return true;
	}

}
