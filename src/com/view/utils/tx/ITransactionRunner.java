package com.view.utils.tx;

import org.hibernate.Session;

public interface ITransactionRunner<T> {

	public Session getSession();

	public void setSession(Session session);

	/** 返回该事物的执行时间 */
	public long getTimeLast();

	public String getDescription();

	/**
	 * 在该方法中完成取数逻辑
	 * 
	 * @return 数据库操作结果
	 * @throws Exception
	 *             错误信息抛出异常,回滚事务
	 */
	public T dataLogic() throws Exception;

	/** 执行事务 */
	public T run();
}
