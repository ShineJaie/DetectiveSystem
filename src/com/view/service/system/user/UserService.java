package com.view.service.system.user;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.detectiveSystem.entity.user.User;
import net.system.utils.MD5Utils;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.view.utils.date.DateTools;
import com.view.utils.paging.Page4DataTable;
import com.view.utils.paging.Page4DataTable.OrderCriterias;
import com.view.utils.tx.TransactionRunner;

/**
 * 系统信息管理 用户管理
 *
 */
@Service
public class UserService {

	public String saveUser(User user) {

		UserSaver saver = new UserSaver(user);
		return saver.run();

	}

	private class UserSaver extends TransactionRunner<String> {

		private User user;

		public UserSaver(User user) {
			super(2, "新建用户");
			this.user = user;
		}

		@Override
		public String dataLogic() throws Exception {

			JSONObject res = new JSONObject();

			// 验证用户账号是是否已经注册
			String sql = "SELECT COUNT(*) FROM `user` u WHERE u.loginName =:loginName";
			Object sqlRes = getSession().createSQLQuery(sql).setString("loginName", user.getLoginName()).uniqueResult();

			long count = Long.parseLong(sqlRes.toString());
			if (count > 0) {
				res.put("status", "error");
				res.put("data", "已存在相同的用户账号");
				return res.toString();
			}

			user.setPassword(MD5Utils.encode(user.getPassword()));
			getSession().save(user);

			res.put("status", "success");
			res.put("data", "新建用户成功");

			return res.toString();
		}

	}

	public void deleteUser(Long id) {

		UserDeleter del = new UserDeleter(id);
		del.run();

	}

	private class UserDeleter extends TransactionRunner<String> {

		private Long id;

		public UserDeleter(Long id) {
			super(4, "删除用户");
			this.id = id;
		}

		@Override
		public String dataLogic() throws Exception {

			User user = (User) getSession().load(User.class, id);
			getSession().delete(user);

			return null;
		}

	}

	public User getUserByLoginName(String loginName) {

		UserByLoginNameGetter getter = new UserByLoginNameGetter(loginName);
		User user = getter.run();

		return user;
	}

	private class UserByLoginNameGetter extends TransactionRunner<User> {

		private String loginName;

		public UserByLoginNameGetter(String loginName) {
			super(1, null);
			this.loginName = loginName;
		}

		@Override
		public User dataLogic() throws Exception {

			String hql = "from User u where u.loginName = :loginName";

			User user = (User) getSession().createQuery(hql).setString("loginName", loginName).uniqueResult();

			return user;
		}

	}

	/**
	 * 获取用户列表 数据
	 * 
	 * @param realName
	 *            姓名
	 * @param pager
	 *            分页参数
	 * 
	 * @return DataTables 表格数据
	 */
	public JSONObject getTableData(Page4DataTable pager, String realName) {

		Runner_getTableData runner = new Runner_getTableData(pager, realName);
		JSONObject res = runner.run();

		return res;
	}

	private class Runner_getTableData extends TransactionRunner<JSONObject> {

		private Page4DataTable pager;
		private String realName;

		public Runner_getTableData(Page4DataTable pager, String realName) {
			super(1, null);
			this.pager = pager;
			this.realName = realName;
		}

		@SuppressWarnings("unchecked")
		@Override
		public JSONObject dataLogic() throws Exception {

			JSONObject result = new JSONObject();

			String[] datas = { "id", "createDate", "loginName", "realName", "email", "password", "permission" };

			List<String> orderList = Arrays.asList("createDate", "loginName", "realName", "email", "permission");

			String selectSql = "SELECT u.id as id, u.createDate AS createDate, u.email AS email, "
					+ "u.loginName AS loginName, u.realName AS realName, u.password AS password, "
					+ "u.permission AS permission ";

			String baseSql = "FROM `user` u WHERE 1 ";

			if (!realName.isEmpty()) {
				baseSql += "AND u.realName LIKE :realName ";
			}

			for (Map<OrderCriterias, Object> orderMap : pager.getOrder()) {
				baseSql += "ORDER BY u."
						+ orderList.get(Integer.valueOf(orderMap.get(OrderCriterias.column).toString()) - 1) + " "
						+ orderMap.get(OrderCriterias.dir).toString();
			}

			String countSql = "SELECT COUNT(*) ";

			SQLQuery dataQuery = getSession().createSQLQuery(selectSql + baseSql);

			SQLQuery countQuery = getSession().createSQLQuery(countSql + baseSql);

			if (!realName.isEmpty()) {
				dataQuery.setString("realName", "%" + realName + "%");
				countQuery.setString("realName", "%" + realName + "%");
			}

			for (String data : datas) {
				dataQuery.addScalar(data);
			}

			List<Map<String, Object>> dataList = dataQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
					.setFirstResult(pager.getFirstResult()).setMaxResults(pager.getMaxResults()).list();

			Object count = countQuery.uniqueResult();

			Object total = getSession().createSQLQuery("SELECT COUNT(*) FROM `user`").uniqueResult();

			result.put("recordsTotal", total.toString()); // 总数
			result.put("recordsFiltered", count.toString()); // 过滤过的总数

			if (dataList.isEmpty()) {
				result.put("data", new JSONArray());
			}

			for (Map<String, Object> item : dataList) {
				JSONObject itemResult = new JSONObject();

				itemResult.put("draw", pager.getDraw());
				itemResult.put("DT_RowId", item.get("id"));
				itemResult.put("id", item.get("id"));
				itemResult.put("createDate", DateTools.formateDate((Date) item.get("createDate")));
				itemResult.put("email", item.get("email"));
				itemResult.put("loginName", item.get("loginName"));
				itemResult.put("realName", item.get("realName"));
				itemResult.put("password", item.get("password"));
				itemResult.put("permission", item.get("permission"));

				result.append("data", itemResult);
			}

			return result;
		}
	}
}
