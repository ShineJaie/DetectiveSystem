package net.detectiveSystem.entity.suspects;

import javax.persistence.Entity;
import javax.persistence.Lob;

import net.system.entity.BaseEntity;
import net.system.entity.annotation.EntityInfo;
import net.system.entity.annotation.Meaning;

@Entity
@EntityInfo("嫌疑人实体")
public class Suspects extends BaseEntity {

	private static final long serialVersionUID = -4353365487658324286L;

	@Meaning("人员姓名")
	private String name = "";

	@Meaning("绰号")
	private String nickname = "";

	@Meaning("身份证号")
	private String idCard = "";

	@Meaning("手机号码")
	private String mobilephone = "";

	@Meaning("QQ号")
	private String qq = "";

	@Meaning("微信号")
	private String wechat = "";

	@Meaning("关系人")
	private String relationMan = "";

	@Meaning("简要案情")
	private String caseBrief = "";

	@Meaning("作案手法")
	private String modusOperandi = "";

	@Meaning("备注")
	private String remark = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getRelationMan() {
		return relationMan;
	}

	public void setRelationMan(String relationMan) {
		this.relationMan = relationMan;
	}

	@Lob
	public String getCaseBrief() {
		return caseBrief;
	}

	public void setCaseBrief(String caseBrief) {
		this.caseBrief = caseBrief;
	}

	public String getModusOperandi() {
		return modusOperandi;
	}

	public void setModusOperandi(String modusOperandi) {
		this.modusOperandi = modusOperandi;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
