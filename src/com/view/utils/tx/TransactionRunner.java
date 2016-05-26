package com.view.utils.tx;

import java.util.Calendar;

import net.detectiveSystem.entity.log.DetectiveSystemLog;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.view.beans.accesslog.UserAccessLogBean;
import com.view.utils.webTools.WebTools;

/**
 * 包裹数据库事务的抽象类.<br/>
 * 1.继承该类, <br/>
 * 2.实现dataLogic()方法(在方法中使用getSession()获取session), <br/>
 * 3.执行run()方法完成事物, <br/>
 * 4使用getTimeLast()方法获取执行时间 <br/>
 * actionTypeKeys = { "其他", "数据库:select", "数据库:insert", "数据库:update",
 * "数据库:delete" };<br/>
 * userType 操作类型, 0:用户, 1:系统平台，不记录用户名称
 * 
 * @param <T>
 *            返回的数据类型
 */
public abstract class TransactionRunner<T> implements ITransactionRunner<T> {
	Logger log = Logger.getLogger(getClass());

	public static String[] actionTypeKeys = { "其他", "数据库:select", "数据库:insert",
			"数据库:update", "数据库:delete", "数据库:多种操作" };

	private Session session = null;
	long timeLast = 0;
	private String description = null;
	int actionType = 0;
	protected int userType = 0;// 操作类型, 0:用户, 1:系统平台

	/**
	 * 默认的构造函数,方便使用，表示的查询事务 <br/>
	 * session = null,如果没有指定的话，在执行的时候会自动获取一个session <br/>
	 * actionType =1 (数据库：select) <br/>
	 * description = null
	 */
	public TransactionRunner() {
		super();
		this.actionType = 1;
		this.description = null;
	}

	/**
	 * 构造函数
	 * 
	 * @param actionType
	 *            "其他",<br/>
	 *            "数据库:select",<br/>
	 *            "数据库:insert",<br/>
	 *            "数据库:update",<br/>
	 *            "数据库:delete",<br/>
	 *            "数据库:多种操作";
	 * @param description
	 *            有 description 则将内容保存的数据库日志
	 */
	public TransactionRunner(int actionType, String description) {
		super();
		this.actionType = actionType;
		this.description = description;
	}

	/**
	 * 构造函数
	 * 
	 * @param session
	 * @param actionType
	 *            "其他",<br/>
	 *            "数据库:select",<br/>
	 *            "数据库:insert",<br/>
	 *            "数据库:update",<br/>
	 *            "数据库:delete",<br/>
	 *            "数据库:多种操作";
	 * @param description
	 *            有 description 则将内容保存的数据库日志
	 */
	public TransactionRunner(Session session, int actionType, String description) {
		super();
		this.session = session;
		this.actionType = actionType;
		this.description = description;

	}

	/**
	 * 构造函数
	 * 
	 * @param sessionFactory
	 *            传入 sessionFactory 自己获取session
	 * @param actionType
	 *            "其他",<br/>
	 *            "数据库:select",<br/>
	 *            "数据库:insert",<br/>
	 *            "数据库:update",<br/>
	 *            "数据库:delete",<br/>
	 *            "数据库:多种操作";
	 * @param description
	 *            有 description 则将内容保存的数据库日志
	 */
	public TransactionRunner(SessionFactory sessionFactory, int actionType,
			String description) {
		super();
		this.session = sessionFactory.openSession();
		this.actionType = actionType;
		this.description = description;

	}

	// ============================================================

	/**
	 * 在该方法中完成取数逻辑
	 * 
	 * @return 指定类型的操作结果
	 * @throws Exception
	 *             错误信息抛出异常,回滚事务
	 */
	public abstract T dataLogic() throws Exception;

	public int getActionType() {
		return actionType;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * 取数逻辑使用的session, 不要在外部使用
	 * 
	 * @return session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * 返回该事物的执行时间
	 * 
	 * @return 事物的执行时间
	 */
	public long getTimeLast() {
		return timeLast;
	}

	// ============================================================

	/**
	 * 执行事务
	 * 
	 * @return 指定类型的操作结果
	 */
	public T run() {
		log.info("*************** 正在使用 [TransactionRunner] 执行事物 ***************");
		T res = null;
		Calendar begin = Calendar.getInstance();
		try {
			if (session == null) {
				SessionFactory sf = WebTools.appContext
						.getBean(SessionFactory.class);
				session = sf.openSession();
			}

			session.beginTransaction();
			log.info("[TransactionRunner] : 事物开始");

			res = dataLogic();

			session.getTransaction().commit();
			log.info("[TransactionRunner] : 事物完成");
			saveLog(1, null);
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			log.info("[TransactionRunner] : 事物错误!! 已经回滚");
			saveLog(2, e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
			log.info("[TransactionRunner] : session 关闭");

			Calendar end = Calendar.getInstance();
			timeLast = end.getTimeInMillis() - begin.getTimeInMillis();
			log.info("*************** [TransactionRunner] : 执行结束 [" + timeLast
					+ "ms] ***************");

		}

		return res;
	}

	// ============================================================

	/**
	 * 存储用户操作日志
	 * 
	 * @param status
	 *            1:操作正常 2:操作失败
	 */
	private void saveLog(int status, String errorMsg) {
		if (description == null) {
			description = "";
		}
		if (status == 2) {
			description += " [" + errorMsg + "] " + getClass().getName();
		}
		if (!description.equals("")) {

			try {
				session.beginTransaction();

				String result = null;
				if (status == 1) {
					result = "操作正常";
				} else if (status == 2) {
					result = "操作失败";
				}

				DetectiveSystemLog syslog = new DetectiveSystemLog();
				syslog.setLogType(actionTypeKeys[actionType]);
				syslog.setLogContent(description);
				syslog.setResult(result);

				try {
					UserAccessLogBean userAccessLogBean = WebTools.appContext
							.getBean(UserAccessLogBean.class);
					syslog.setUserName(userAccessLogBean.getUserName());
					syslog.setAccessIP(userAccessLogBean.getRequestHost());
				} catch (Exception e) {
					Logger.getLogger(getClass()).warn("良性错误 没有发现登录用户");
					// e.printStackTrace();
					syslog.setUserName("-No Username-");
					syslog.setAccessIP("-No Host-");
				}

				session.save(syslog);

				session.getTransaction().commit();

			} catch (Exception e) {
				e.printStackTrace();
				session.getTransaction().rollback();
			}
		}
	}

	/**
	 * 设置操作类型
	 * 
	 * @param actionType
	 *            "其他",<br/>
	 *            "数据库:select",<br/>
	 *            "数据库:insert",<br/>
	 *            "数据库:update",<br/>
	 *            "数据库:delete",<br/>
	 *            "数据库:多种操作";
	 */
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * 设置产生日志的类型<br/>
	 * 0:用户,记录用户名和id;<br/>
	 * 1:系统平台, 不记录用户信息
	 * 
	 * @param userType
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

}
