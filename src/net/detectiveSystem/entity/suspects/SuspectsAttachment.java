package net.detectiveSystem.entity.suspects;

import net.system.entity.BaseEntity;
import net.system.entity.annotation.EntityInfo;
import net.system.entity.annotation.Meaning;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@EntityInfo("嫌疑人附件实体")
public class SuspectsAttachment extends BaseEntity {

    @Meaning("附件类型")
    private Integer type = 0; // 0: 普通附件; 1: 附件图片
    @Meaning("附件名称")
    private String name = "";
    @Meaning("附件内容")
    private String content = "";
    @Meaning("附件所在文件夹")
    private String folderName = "";

    @Meaning("嫌疑人实体")
    private Suspects suspects;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Suspects getSuspects() {
        return suspects;
    }

    public void setSuspects(Suspects suspects) {
        this.suspects = suspects;
    }
}

