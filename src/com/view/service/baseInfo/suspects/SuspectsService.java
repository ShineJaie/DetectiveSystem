package com.view.service.baseInfo.suspects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import com.view.beans.upload.FileMeta;
import com.view.utils.filePathTools.FilePathTools;
import net.detectiveSystem.entity.suspects.Suspects;
import net.detectiveSystem.entity.suspects.SuspectsAttachment;
import net.detectiveSystem.entity.user.User;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.view.utils.paging.Page4DataTable;
import com.view.utils.tx.TransactionRunner;
import com.view.web.controller.baseInfo.suspects.SuspectsParams;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 基础信息管理 打处对象管理
 */
@Service
public class SuspectsService {

    public String saveSuspects(Suspects suspects, String folderName) {

        Runer_saveSuspects runer = new Runer_saveSuspects(suspects, folderName);
        return runer.run();

    }

    private class Runer_saveSuspects extends TransactionRunner<String> {

        private Suspects suspects;
        private String folderName;

        public Runer_saveSuspects(Suspects suspects, String folderName) {
            super();
            this.suspects = suspects;
            this.folderName = folderName;
        }

        @Override
        public String dataLogic() throws Exception {

            JSONObject res = new JSONObject();

            if (suspects.getId() == null) {
                getSession().save(suspects);
                getSession().flush();
                setRelation(suspects.getId(), folderName, getSession());
            } else {
                getSession().update(suspects);
            }

            res.put("status", "success");
            res.put("data", "保存成功");

            return res.toString();
        }

    }

    public void deleteSuspects(Long id) {

        Runer_deleteSuspects runer = new Runer_deleteSuspects(id);
        runer.run();

    }

    private class Runer_deleteSuspects extends TransactionRunner<String> {

        private Long id;

        public Runer_deleteSuspects(Long id) {
            super();
            this.id = id;
        }

        @Override
        public String dataLogic() throws Exception {

            List<Map<String, Object>> attachmentIdAndFile = getAttachmentIdAndFile(id);
            for (Map<String, Object> map : attachmentIdAndFile) {
                String attachmentId = map.get("id").toString();

                String folderName = map.get("folderName").toString();
                String filePath = FilePathTools.getRootPath() + "/upload/suspectsAttachment/" + folderName;

                deleteFile(filePath);
                deleteFileDB(attachmentId, getSession());
            }

            Suspects suspects = (Suspects) getSession().load(Suspects.class, id);
            getSession().delete(suspects);

            return "ok";
        }

        private List<Map<String, Object>> getAttachmentIdAndFile(Long id) {
            String sql = "SELECT a.id AS id, a.folderName AS folderName FROM suspectsattachment a "
                    + "WHERE a.suspects_id =:id";
            List<Map<String, Object>> sqlRes = getSession().createSQLQuery(sql).addScalar("id", Hibernate.LONG)
                    .addScalar("folderName", Hibernate.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                    .setLong("id", id).list();
            return sqlRes;
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
            super();
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
     * 获取打处对象列表 数据
     *
     * @param suspectsParams 打处对象搜索参数
     * @param pager          分页参数
     * @param permission     权限
     * @return DataTables 表格数据
     */
    public JSONObject getTableData(Page4DataTable pager, SuspectsParams suspectsParams, Integer permission) {

        Runner_getTableData runner = new Runner_getTableData(pager, suspectsParams, permission);
        JSONObject res = runner.run();

        return res;
    }

    private class Runner_getTableData extends TransactionRunner<JSONObject> {

        private Page4DataTable pager;
        private SuspectsParams suspectsParams;
        private Integer permission;

        public Runner_getTableData(Page4DataTable pager, SuspectsParams suspectsParams, Integer permission) {
            super();
            this.pager = pager;
            this.suspectsParams = suspectsParams;
            this.permission = permission;
        }

        @SuppressWarnings("unchecked")
        @Override
        public JSONObject dataLogic() throws Exception {

            JSONObject result = new JSONObject();

            String[] datas = {"id", "name", "nickname", "idCard", "mobilephone", "qq", "wechat", "relationMan",
                    "caseBrief", "modusOperandi", "remark"};

            String selectSql = "SELECT s.id AS id, s.`name` AS name, s.nickname AS nickname, "
                    + "s.idCard AS idCard, s.mobilephone AS mobilephone, s.qq AS qq, "
                    + "s.wechat AS wechat, s.relationMan AS relationMan, s.caseBrief AS caseBrief, "
                    + "s.modusOperandi AS modusOperandi, s.remark AS remark ";

            String baseSql = "FROM suspects s WHERE 1 ";

            String orderSql = "ORDER BY s.modifyDate DESC";

            if (!suspectsParams.getName().isEmpty()) {
                baseSql += "AND s.name LIKE :name ";
            }
            if (!suspectsParams.getNickname().isEmpty()) {
                baseSql += "AND s.nickname LIKE :nickname ";
            }
            if (!suspectsParams.getMobilephone().isEmpty()) {
                baseSql += "AND s.mobilephone LIKE :mobilephone ";
            }
            if (!suspectsParams.getQq().isEmpty()) {
                baseSql += "AND s.qq LIKE :qq ";
            }
            if (!suspectsParams.getWechat().isEmpty()) {
                baseSql += "AND s.wechat LIKE :wechat ";
            }
            if (!suspectsParams.getModusOperandi().isEmpty()) {
                baseSql += "AND s.modusOperandi LIKE :modusOperandi ";
            }
            if (!suspectsParams.getRelationMan().isEmpty()) {
                baseSql += "AND s.relationMan LIKE :relationMan ";
            }

            String countSql = "SELECT COUNT(*) ";

            SQLQuery dataQuery = getSession().createSQLQuery(selectSql + baseSql + orderSql);

            SQLQuery countQuery = getSession().createSQLQuery(countSql + baseSql + orderSql);

            if (!suspectsParams.getName().isEmpty()) {
                dataQuery.setString("name", "%" + suspectsParams.getName() + "%");
                countQuery.setString("name", "%" + suspectsParams.getName() + "%");
            }
            if (!suspectsParams.getNickname().isEmpty()) {
                dataQuery.setString("nickname", "%" + suspectsParams.getNickname() + "%");
                countQuery.setString("nickname", "%" + suspectsParams.getNickname() + "%");
            }
            if (!suspectsParams.getMobilephone().isEmpty()) {
                dataQuery.setString("mobilephone", "%" + suspectsParams.getMobilephone() + "%");
                countQuery.setString("mobilephone", "%" + suspectsParams.getMobilephone() + "%");
            }
            if (!suspectsParams.getQq().isEmpty()) {
                dataQuery.setString("qq", "%" + suspectsParams.getQq() + "%");
                countQuery.setString("qq", "%" + suspectsParams.getQq() + "%");
            }
            if (!suspectsParams.getWechat().isEmpty()) {
                dataQuery.setString("wechat", "%" + suspectsParams.getWechat() + "%");
                countQuery.setString("wechat", "%" + suspectsParams.getWechat() + "%");
            }
            if (!suspectsParams.getModusOperandi().isEmpty()) {
                dataQuery.setString("modusOperandi", "%" + suspectsParams.getModusOperandi() + "%");
                countQuery.setString("modusOperandi", "%" + suspectsParams.getModusOperandi() + "%");
            }
            if (!suspectsParams.getRelationMan().isEmpty()) {
                dataQuery.setString("relationMan", "%" + suspectsParams.getRelationMan() + "%");
                countQuery.setString("relationMan", "%" + suspectsParams.getRelationMan() + "%");
            }

            for (String data : datas) {
                dataQuery.addScalar(data, Hibernate.STRING);
            }

            List<Map<String, Object>> dataList = dataQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                    .setFirstResult(pager.getFirstResult()).setMaxResults(pager.getMaxResults()).list();

            Object count = countQuery.uniqueResult();

            Object total = getSession().createSQLQuery("SELECT COUNT(*) FROM suspects").uniqueResult();

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
                itemResult.put("name", item.get("name"));
                itemResult.put("nickname", item.get("nickname"));
                itemResult.put("idCard", item.get("idCard"));
                itemResult.put("mobilephone", item.get("mobilephone"));
                itemResult.put("qq", item.get("qq"));
                itemResult.put("wechat", item.get("wechat"));
                itemResult.put("relationMan", item.get("relationMan"));
                itemResult.put("caseBrief", item.get("caseBrief"));
                itemResult.put("modusOperandi", item.get("modusOperandi"));
                itemResult.put("remark", item.get("remark"));
                itemResult.put("permission", permission);

                result.append("data", itemResult);
            }

            return result;
        }
    }

    /**
     * 修改打处对象信息
     *
     * @param id
     * @return
     */
    public JSONObject editSuspects(Long id) {
        Runer_editSuspects runer = new Runer_editSuspects(id);
        JSONObject res = runer.run();
        return res;
    }

    private class Runer_editSuspects extends TransactionRunner<JSONObject> {

        private Long id;

        public Runer_editSuspects(Long id) {
            super();
            this.id = id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public JSONObject dataLogic() throws Exception {

            String[] datas = {"id", "name", "nickname", "idCard", "mobilephone", "qq", "wechat", "relationMan",
                    "caseBrief", "modusOperandi", "remark"};

            String sql = "SELECT s.id AS id, s.`name` AS name, s.nickname AS nickname, "
                    + "s.idCard AS idCard, s.mobilephone AS mobilephone, s.qq AS qq, "
                    + "s.wechat AS wechat, s.relationMan AS relationMan, s.caseBrief AS caseBrief, "
                    + "s.modusOperandi AS modusOperandi, s.remark AS remark FROM suspects s WHERE s.id =:id ";

            SQLQuery dataQuery = getSession().createSQLQuery(sql);

            for (String data : datas) {
                if (data.equals("id")) {
                    dataQuery.addScalar(data, Hibernate.LONG);
                } else {
                    dataQuery.addScalar(data, Hibernate.STRING);
                }
            }

            List<Map<String, String>> dataRes = dataQuery.setLong("id", id)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

            JSONObject res = new JSONObject();
            for (String data : datas) {
                res.put(data, dataRes.get(0).get(data));
            }
            // 附件图片
            List<Map<String, String>> imgs = getImgs();
            if (!imgs.isEmpty()) {
                res.put("folderName", imgs.get(0).get("folderName"));
                res.put("fileinput_fjtp", fileinput_response(imgs.get(0).get("folderName"), 1, getSession()));
            } else {
                res.put("folderName", JSONObject.NULL);
            }

            res.put("imgs", imgs);

            return res;
        }

        private List<Map<String, String>> getImgs() {
            String sql = "SELECT CONCAT( '/DetectiveSystem', a.content ) AS src, a.name AS name, " +
                    "a.folderName AS folderName FROM suspectsattachment a WHERE a.suspects_id = :id";
            List<Map<String, String>> list = getSession().createSQLQuery(sql).addScalar("src", Hibernate.STRING)
                    .addScalar("name", Hibernate.STRING).addScalar("folderName", Hibernate.STRING).setLong("id", id)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                    .list();
            return list;
        }

    }

    /**
     * 上传附件图片
     *
     * @param request
     * @param folderName
     * @param type       0: 普通附件; 1: 附件图片
     * @param suspectsId
     * @param isUpload   0: 不上传; 1: 上传
     * @return
     */
    public JSONObject uploadFile(MultipartHttpServletRequest request, String folderName, Integer type, Long suspectsId, Integer isUpload) {
        Runer_uploadFile runer = new Runer_uploadFile(request, folderName, type, suspectsId, isUpload);
        JSONObject res = runer.run();
        return res;
    }

    private class Runer_uploadFile extends TransactionRunner<JSONObject> {

        private MultipartHttpServletRequest request;
        private String folderName;
        private Integer type;
        private Long suspectsId;
        private Integer isUpload;

        public Runer_uploadFile(MultipartHttpServletRequest request, String folderName, Integer type, Long suspectsId, Integer isUpload) {
            super();
            this.request = request;
            this.folderName = folderName;
            this.type = type;
            this.suspectsId = suspectsId;
            this.isUpload = isUpload;
        }

        /**
         * 依据原始文件名生成新文件名
         *
         * @return
         */
        private String getName(String fileName) {
            Random random = new Random();
            fileName = "" + random.nextInt(10000) + System.currentTimeMillis() + getFileExt(fileName);
            return fileName;
        }

        /**
         * 获取文件扩展名
         *
         * @return string
         */
        private String getFileExt(String fileName) {
            return fileName.substring(fileName.lastIndexOf("."));
        }

        @Override
        public JSONObject dataLogic() throws Exception {

            if (isUpload == 0) {
                return fileinput_response(folderName, type, getSession());
            }

            JSONObject res = new JSONObject();

            String filePath = "/upload/suspectsAttachment/" + folderName + "/";
            String savePath = FilePathTools.getRootPath() + filePath;
            File saveFile = new File(savePath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }

            String fileNameWithPath = "";

            // 1. build an iterator
            Iterator<String> itr = request.getFileNames();
            MultipartFile mpf = null;
            // 2. get each file
            LinkedList<FileMeta> files = new LinkedList<FileMeta>();
            FileMeta fileMeta = null;

            ArrayList<String> fileNameWithPathArr = new ArrayList<String>();
            while (itr.hasNext()) {

                // 2.1 get next MultipartFile
                mpf = request.getFile(itr.next());

                // 2.2 if files > 10 remove the first from the list
                if (files.size() >= 10) {
                    files.pop();
                }

                // 2.3 create new fileMeta
                fileMeta = new FileMeta();
                fileMeta.setFileName(mpf.getOriginalFilename());
                fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
                fileMeta.setFileType(mpf.getContentType());

                String originalName = ""; // 原始文件名
                originalName = mpf.getOriginalFilename();

                String fileName = ""; // 上传文件名
                fileName = getName(mpf.getOriginalFilename());
                fileNameWithPath = savePath + fileName;

                try {
                    fileMeta.setBytes(mpf.getBytes());

                    // copy file to local disk (make sure the path
                    // "e.g. D:/temp/files" exists)

                    File file = new File(fileNameWithPath);
                    if (file.isDirectory()) {
                        fileNameWithPath += mpf.getOriginalFilename();
                    }
                    FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(fileNameWithPath));
                    fileNameWithPathArr.add(fileNameWithPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 2.4 add to files
                files.add(fileMeta);

                SuspectsAttachment suspectsAttachment = new SuspectsAttachment();
                suspectsAttachment.setType(type);
                suspectsAttachment.setName(originalName);
                suspectsAttachment.setContent(filePath + fileName);
                suspectsAttachment.setFolderName(folderName);
                getSession().save(suspectsAttachment);
                getSession().flush();
            }
            // 获取附件信息
            res = fileinput_response(folderName, type, getSession());

            // 若informManagementId不为空，则设置关联关系(编辑功能)
            if (suspectsId != null) {
                setRelation(suspectsId, folderName, getSession());
            }

            return res;
        }
    }

    /**
     * 获取fileinput需要的服务器返回结果
     *
     * @param folderName
     * @param type
     * @param session
     * @return
     */
    public JSONObject fileinput_response(String folderName, Integer type, Session session) {
        JSONObject res = new JSONObject();
        List<Map<String, Object>> attachmentList = getAttachments(folderName, type, session);

        List<String> initialPreview = new ArrayList<String>();
        List<Map<String, Object>> initialPreviewConfig = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < attachmentList.size(); i++) {
            String contenturl = attachmentList.get(i).get("contenturl").toString();
            String src = "/DetectiveSystem" + contenturl;
            String caption = attachmentList.get(i).get("caption").toString();
            String fileSuffix = caption.substring(caption.lastIndexOf(".") + 1);
            List<String> imageSuffixs = Arrays.asList("jpg", "png", "jpeg", "bmp", "gif");
            if (imageSuffixs.contains(fileSuffix)) {
                initialPreview.add("<a href=" + src + "><img class='file-preview-image' style='height:160px' src='"
                        + src + "'></a>");
            } else {
                initialPreview.add("<a href=" + src + "><div class='file-preview-text' style='text-align: center;'>"
                        + "<h2><i class='glyphicon glyphicon-file'></i></h2>" + caption + "</div></a>");
            }

            Map<String, Object> configItem = new HashMap<String, Object>();
            configItem.put("caption", caption);
            configItem.put("url", "/DetectiveSystem/action/suspects/file/delete");
            configItem.put("width", "120px");
            Map<String, Object> extra = new HashMap<String, Object>();
            extra.put("filePath", FilePathTools.getRootPath() + contenturl);
            extra.put("id", attachmentList.get(i).get("id").toString());
            configItem.put("extra", extra);
            initialPreviewConfig.add(configItem);
        }

        res.put("initialPreview", initialPreview);
        res.put("initialPreviewConfig", initialPreviewConfig);
        res.put("append", false);
        return res;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAttachments(String folderName, Integer type, Session session) {
        String sql = "SELECT a.id as id, a. NAME AS caption, a.content AS contenturl FROM suspectsattachment a "
                + "WHERE a.type =:type AND a.folderName =:folderName";
        List<Map<String, Object>> sqlRes = session.createSQLQuery(sql).addScalar("id", Hibernate.STRING)
                .addScalar("caption", Hibernate.STRING).addScalar("contenturl", Hibernate.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setInteger("type", type)
                .setString("folderName", folderName).list();
        return sqlRes;
    }

    /**
     * 关联附件
     *
     * @param suspectsId
     * @param folderName
     * @param session
     */
    public void setRelation(Long suspectsId, String folderName, Session session) {
        String sql = "UPDATE suspectsattachment a SET a.suspects_id =:id WHERE a.folderName =:folderName";
        session.createSQLQuery(sql).setLong("id", suspectsId).setString("folderName", folderName)
                .executeUpdate();
    }

    /**
     * 删除文件和其数据库中的数据
     *
     * @param filePath
     * @param id
     */
    public Boolean deleteFileAndDB(String filePath, String id) {
        Runer_deleteFromDB runer = new Runer_deleteFromDB(filePath, id);
        Boolean res = runer.run();
        return res;
    }

    private class Runer_deleteFromDB extends TransactionRunner<Boolean> {

        private String filePath;
        private String id;

        public Runer_deleteFromDB(String filePath, String id) {
            super();
            this.filePath = filePath;
            this.id = id;
        }

        @Override
        public Boolean dataLogic() throws Exception {

            String sql = "SELECT COUNT(1) FROM suspectsattachment WHERE id = :id";
            Object sqlRes = getSession().createSQLQuery(sql).setString("id", id).uniqueResult();

            if (sqlRes != null && Integer.parseInt(sqlRes.toString()) > 0) {
                // 删除文件
                deleteFile(filePath);
            }

            // 删除数据库
            deleteFileDB(id, getSession());
            return true;
        }
    }

    /**
     * 删除此路径名表示的文件或目录。 如果此路径名表示一个目录，则会先删除目录下的内容再将目录删除，所以该操作不是原子性的。
     * 如果目录中还有目录，则会引发递归动作。
     *
     * @param filePath 要删除文件或目录的路径。
     * @return 当且仅当成功删除文件或目录时，返回 true；否则返回 false。
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return deleteFileHandle(file);
    }

    private boolean deleteFileHandle(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    this.deleteFileHandle(files[i]);
                }
            }
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * 删除数据库中的文件记录
     *
     * @param id
     * @param session
     */
    public void deleteFileDB(String id, Session session) {
        String sql = "DELETE FROM suspectsattachment WHERE id =:id";
        session.createSQLQuery(sql).setString("id", id).executeUpdate();
        session.flush();
    }
}
