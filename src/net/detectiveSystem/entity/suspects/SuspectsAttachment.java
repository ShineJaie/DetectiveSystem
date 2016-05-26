package net.detectiveSystem.entity.suspects;

import net.system.entity.BaseEntity;
import net.system.entity.annotation.EntityInfo;
import net.system.entity.annotation.Meaning;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@EntityInfo("�����˸���ʵ��")
public class SuspectsAttachment extends BaseEntity {

    @Meaning("��������")
    private Integer type = 0; // 0: ��ͨ����; 1: ����ͼƬ
    @Meaning("��������")
    private String name = "";
    @Meaning("��������")
    private String content = "";
    @Meaning("���������ļ���")
    private String folderName = "";

    @Meaning("������ʵ��")
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

