package net.detectiveSystem.entity.user;

import javax.persistence.Entity;

import net.system.entity.BaseEntity;
import net.system.entity.annotation.EntityInfo;
import net.system.entity.annotation.Meaning;

@Entity
@EntityInfo("用户实体")
public class User extends BaseEntity {
	private static final long serialVersionUID = -7482385152597531079L;

	@Meaning("用户名")
	private String loginName = "";
	@Meaning("密码")
	private String password = "";
	@Meaning("姓名")
	private String realName = "";
	@Meaning("邮箱")
	private String email = "";
	@Meaning("权限")
	private Integer permission = 0; // 0: 默认没有删除、和注册用户功能; 1: 拥有所有权限

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [loginName=" + loginName + ", password=" + password + ", realName=" + realName + ", email="
				+ email + "]";
	}

}
