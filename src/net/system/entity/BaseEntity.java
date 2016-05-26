package net.system.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import net.system.entity.annotation.EntityInfo;
import net.system.entity.annotation.Meaning;

@MappedSuperclass
@EntityInfo("实体基类")
public class BaseEntity extends EntityImpl {
	private static final long serialVersionUID = -2808781489666525467L;

	@Meaning("创建时间")
	private Date createDate;
	@Meaning("修改时间")
	private Date modifyDate;

	@Column(updatable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
