package net.detectiveSystem.entity.log;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.xml.bind.annotation.XmlRootElement;

import com.view.utils.webTools.WebTools;

import net.system.entity.BaseEntity;
import net.system.entity.annotation.EntityInfo;
import net.system.entity.annotation.Meaning;

/**
 * 记录用户的操作
 * 
 */
@Entity
@EntityInfo("网站操作日志")
@XmlRootElement
public class DetectiveSystemLog extends BaseEntity {
	private static final long serialVersionUID = 7007616679152676790L;

	@Meaning("日志创建的毫秒值")
	private Long createDate_timeInMillis = 0L;

	@Meaning("操作用户名")
	private String userName;

	@Meaning("来源ip")
	private String accessIP;

	@Meaning("日志类型")
	private String logType;

	@Meaning("日志内容")
	private String logContent;

	@Meaning("操作结果")
	private String result;

	public String getAccessIP() {
		return accessIP;
	}

	public Long getCreateDate_timeInMillis() {
		return createDate_timeInMillis;
	}

	@Lob
	public String getLogContent() {
		return logContent;
	}

	public String getLogType() {
		return logType;
	}

	public String getResult() {
		return result;
	}

	public String getUserName() {
		return userName;
	}

	public void setAccessIP(String accessIP) {
		accessIP = WebTools.dealWithNullVal(accessIP);
		this.accessIP = accessIP;
	}

	public void setCreateDate_timeInMillis(Long createDate_timeInMillis) {
		this.createDate_timeInMillis = createDate_timeInMillis;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setUserName(String userName) {
		userName = WebTools.dealWithNullVal(userName);
		this.userName = userName;
	}

}
