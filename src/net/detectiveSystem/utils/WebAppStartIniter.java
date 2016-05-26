package net.detectiveSystem.utils;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.detectiveSystem.entity.log.DetectiveSystemLog;
import net.detectiveSystem.entity.user.User;
import net.system.utils.MD5Utils;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.view.utils.tx.TransactionRunner;
import com.view.utils.webTools.WebTools;

/**
 * 初始化超级管理员
 * 
 */
class SAIniter extends TransactionRunner<Integer> {

	public SAIniter(Session session) {
		super(session, 5, "检查系统用户");
	}

	/**
	 * 检查 账号 是否存在
	 * 
	 * @param name
	 * @return none or id
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Long checkIsExistSA(String name) throws Exception {
		String hql = "select i.id from User i where i.loginName = :name";
		List<Object> ids = getSession().createQuery(hql).setString("name", name).list();
		if (ids.size() == 0) {
			return null;
		} else if (ids.size() == 1) {
			return (Long) ids.get(0);
		} else {
			throw new Exception("重复的用户名");
		}
	}

	// ==================================================================
	/**
	 * 创建超级管理员账户
	 * 
	 * @param name
	 * @return id
	 */
	public Long createUser(String name) {
		User user = new User();
		user.setLoginName(name);
		user.setPassword(MD5Utils.encode(name + "2015"));
		user.setRealName(name);
		user.setEmail("391940025@qq.com");
		user.setPermission(1);
		getSession().save(user);
		getSession().flush();
		return user.getId();
	}

	@Override
	public Integer dataLogic() throws Exception {
		setUserType(1);

		// 超级管理员
		Long saUserId = checkIsExistSA("sa");
		if (saUserId == null) {
			saUserId = createUser("sa");
		}
		return 1;
	}
}

// ==================================================================

@Component
public class WebAppStartIniter implements ServletContextListener {

	/** webApp 上线 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		String msg = "Dtective System，网站系统下线";
		Logger.getLogger(getClass()).info(msg);

		SessionFactory sessionFactory = WebTools.appContext.getBean(SessionFactory.class);
		Logger.getLogger(getClass()).info("sessionFactory : " + sessionFactory);
		WebAppStartInitLogger runer = new WebAppStartInitLogger(sessionFactory.openSession(), msg);
		runer.run();
	}

	/** webApp 停止 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String msg = "Detective System，网站系统上线";
		Logger.getLogger(getClass()).info(msg);

		SessionFactory sessionFactory = WebTools.appContext.getBean(SessionFactory.class);
		Logger.getLogger(getClass()).info("sessionFactory : " + sessionFactory);
		WebAppStartInitLogger runer = new WebAppStartInitLogger(sessionFactory.openSession(), msg);
		runer.run();

		// 初始化超级管理员 完成
		SAIniter sAIniter = new SAIniter(sessionFactory.openSession());
		sAIniter.run();

	}
}

// ==================================================================

class WebAppStartInitLogger extends TransactionRunner<Object> {
	String msg = "";

	public WebAppStartInitLogger(Session session, String msg) {
		super(session, 2, null);
		this.msg = msg;
	}

	@Override
	public Object dataLogic() throws Exception {
		setUserType(1);

		DetectiveSystemLog syslog = new DetectiveSystemLog();
		syslog.setLogType("系统事件");
		syslog.setLogContent(msg);
		syslog.setResult("--");

		syslog.setUserName("平台系统");
		syslog.setAccessIP("");

		getSession().save(syslog);

		return null;
	}

}
