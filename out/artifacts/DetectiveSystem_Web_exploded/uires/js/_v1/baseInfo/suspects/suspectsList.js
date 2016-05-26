define(["jquery", "config", "DataTables_bootstrap", 'sco.confirm', 'sco.message', 'fileinput', 'fileinput_locale_zh'],
    function ($, config, DataTables_bootstrap, sco_confirm, sco_message, fileinput, fileinput_locale_zh) {

        var url_delete = "/DetectiveSystem/action/suspects/delete";
        var url_editData = "/DetectiveSystem/action/suspects/edit/data";
        var page_edit = "/DetectiveSystem/action/suspects/create .inner_container";
        var page_show = "/DetectiveSystem/action/suspects/show .inner_container";
        var url_save = "/DetectiveSystem/action/suspects/save";
        var url_upload = "/DetectiveSystem/action/suspects/upload";

        // 打处对象列表
        var initpage_suspectsList = function () {
            drawTable();
            act_search();
        };

        var act_search = function () {
            $(".act_search").off("click").click(function () {
                var data = {};
                $(".search_param[para_name]").each(function () {
                    data[$(this).attr("para_name")] = $(this).val();
                });
                var jsonParams = $.extend({}, data);
                sessionStorage.setItem("suspectsList_searchParams", JSON.stringify(jsonParams));

                $("#suspectsList").DataTable().ajax.reload(); // 重新加载Ajax数据源的表数据
            });
        };

        var act_delete = function () {
            $("#suspectsList").off("click", ".act_delete").on("click", ".act_delete", function () {
                var suspectsId = $(this).attr("suspectsId");

                config.showConfimDlg({
                    "title": "确认",
                    "message": "是否删除该条记录?",
                    "showDlgEx": null,
                    "action": function () {
                        $.post(url_delete, {
                            id: suspectsId
                        }, function (data) {
                            config.convert2Sco_message(data);
                            if (data.status == "success") {
                                $("#suspectsList").DataTable().ajax.reload(); // 重新加载Ajax数据源的表数据
                            }
                        }, "json");
                    }
                });
            });
        };

        var act_edit = function () {
            $("#suspectsList").off("click", ".act_edit").on("click", ".act_edit", function () {
                var suspectsId = $(this).attr("suspectsId");
                $.post(url_editData, {
                    id: suspectsId
                }, function (data) {
                    $(".inner_container").load(page_edit, function () {
                        $(".add_param[para_name]").each(function () {
                            $(this).val(data[$(this).attr("para_name")]);
                        });

                        initFileinput(data.folderName, data.fileinput_fjtp, data.id, 1);
                        act_editSave(data.id);
                    });
                }, "json");
            });
        };

        var act_editSave = function (id) {
            $(".inner_container").off("click", ".act_save").on("click", ".act_save", function () {
                var param = {
                    id: id
                };
                $(".add_param[para_name]").each(function () {
                    param[$(this).attr("para_name")] = $(this).val();
                });

                $.post(url_save, param, function (data) {
                    config.convert2Sco_message(data);
                    if (data.status == "success") {
                        window.location.href = "/DetectiveSystem/action/suspects/list";
                    }
                }, "json");
            });
        };

        var act_show = function () {
            $("#suspectsList").off("click", ".act_show").on("click", ".act_show", function () {
                var suspectsId = $(this).attr("suspectsId");
                $.post(url_editData, {
                    id: suspectsId
                }, function (data) {
                    $(".inner_container").load(page_show, function () {
                        $(".add_param[para_name]").each(function () {
                            $(this).val(data[$(this).attr("para_name")]);
                        });

                        var imgs = data.imgs;
                        for (var i = 0; i < imgs.length; ++i) {
                            $(".imgs_content").append($("<img src=" + imgs[i].src + "  alt=" + imgs[i].name + " style='height:105px; width:140px;' />"));
                            //console.log(imgs[i].src);
                            //console.log(imgs[i].name);
                        }

                    });
                }, "json");
            });
        };

        var initComplete = function () {
            console.log("finish load...");
            act_delete();
            act_edit();
            act_show();
        };

        var genTdWithTitle = function (data, length) {
            var subData = data.substr(0, length);
            if (data.length > length) {
                subData += "...";
            }
            var res = $("<div></div>").append($("<span></span>").html(subData).attr("title", data));
            return res.html();
        };

        var drawTable = function () {

            var tableConfig = {
                "processing": true, // 显示加载信息
                "serverSide": true, // 开启服务器模式
                "searching": false, // 开启搜索功能
                "stateSave": true, // 允许浏览器缓存Datatables，以便下次恢复之前的状态
                "ordering": false, // 是否启用Datatables排序
                //"scrollX": true, // 水平滚动条
                "scrollCollapse": true, // 允许表减少在高度有限的显示的行数
                // "order" : [ [ 1, 'desc' ] ], // 设置默认排序
                "initComplete": initComplete(), // 表格加载完成回调函数
                "dom": "Rlfrtip", // 按什么顺序定义表的控制元素在页面上出现
                "ajax": {
                    "url": "/DetectiveSystem/action/suspects/json",
                    "type": "POST",
                    "data": function (data) {
                        // 添加额外的参数传给服务器
                        var result = JSON.parse(sessionStorage.getItem("suspectsList_searchParams"));
                        if ($.isEmptyObject(result)) {
                            var curData = {};
                            $(".search_param[para_name]").each(function () {
                                curData[$(this).attr("para_name")] = $(this).val();
                            });
                            $.extend(true, data, curData);
                        } else {
                            $.extend(true, data, result);
                        }
                        // 修改原dataTables参数
                        config.planify(data);
                    }
                },
                "columnDefs": [{
                    "orderable": false, // 设置不可排序
                    "targets": -1,
                    "data": null,
                    "render": function (data, type, full, meta) {
                        var res = "";
                        if (full["permission"] == 1) {
                            res += "<button class='btn btn-xs btn-info act_show' suspectsId='" + full["id"] + "'><span class='glyphicon glyphicon-blackboard' aria-hidden='true'></span> 详情</button>";
                            res += " <button class='btn btn-xs btn-success act_edit' suspectsId='" + full["id"] + "'><span class='glyphicon glyphicon-edit' aria-hidden='true'></span> 修改</button>";
                            res += " <button class='btn btn-xs btn-danger act_delete' suspectsId='" + full["id"] + "'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> 删除</button>";
                        } else {
                            res += "<button class='btn btn-xs btn-info act_show' suspectsId='" + full["id"] + "'><span class='glyphicon glyphicon-blackboard' aria-hidden='true'></span> 详情</button>";
                            /*res += " <button class='btn btn-xs btn-success' disabled='disabled'><span class='glyphicon glyphicon-edit' aria-hidden='true'></span> 修改</button>";
                             res += " <button class='btn btn-xs btn-danger' disabled='disabled'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> 删除</button>";*/
                        }
                        return res;
                    }
                }],
                "columns": [{
                    "data": "name",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "nickname",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "idCard",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "mobilephone",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "qq",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "wechat",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "relationMan",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "caseBrief",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "modusOperandi",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    "data": "remark",
                    "render": function (data, type, full, meta) {
                        if (type === 'display') {
                            return genTdWithTitle(data, 20);
                        } else {
                            return data;
                        }
                    }
                }, {
                    //"width": "25%",
                    "data": null
                }]
            };

            tableConfig = $.extend(true, {}, config.dataTableConfig, tableConfig);

            var dataTable = $("#suspectsList").on('init.dt', function () {
                $("#suspectsList").removeClass("display").addClass("table table-striped table-bordered");
            }).DataTable(tableConfig).on('draw.dt', function () {
                $("#suspectsList").removeClass("display").addClass("table table-striped table-bordered");
            });

            $(window).resize(function () {
                dataTable.columns.adjust().draw();
            });

            // 设置默认排序
            // dataTable.order([ 1, 'desc' ]).draw()

        };

        // ============================================================================

        // 添加打处对象
        var initpage_suspectsInfo = function () {
            var uuid = generateUUID();
            initFileinput(uuid, null, null, 1);
            act_save(uuid);
        };

        var act_save = function (uuid) {
            $(".form_user").off("click", ".act_save").on("click", ".act_save", function () {
                var param = {};
                $(".add_param[para_name]").each(function () {
                    param[$(this).attr("para_name")] = $(this).val();
                });

                param.folderName = uuid;
                $.post(url_save, param, function (data) {
                    config.convert2Sco_message(data);
                    if (data.status == "success") {
                        window.location.href = "/DetectiveSystem/action/suspects/list";
                    }
                }, "json");
            });
        };

        var initFileinput = function (uuid, fileinput_fjtp, suspectsId, isUpload) {
            if (uuid == null || uuid == undefined || uuid.length == 0) {
                uuid = generateUUID();
            }
            if (fileinput_fjtp == null || fileinput_fjtp == undefined || fileinput_fjtp.length == 0) {
                fileinput_fjtp = {};
                fileinput_fjtp.initialPreview = [];
                fileinput_fjtp.initialPreviewConfig = [];
            }
            if (suspectsId == null || suspectsId == undefined) {
                suspectsId = "";
            }
            $("#input-fjtp").fileinput(
                {
                    initialPreview: fileinput_fjtp.initialPreview,
                    initialPreviewConfig: fileinput_fjtp.initialPreviewConfig,
                    layoutTemplates: {
                        actions: '<div class="file-actions">\n' + '    <div class="file-footer-buttons">\n' + '        {delete}' + '    </div>\n' + '    <div class="file-upload-indicator" tabindex="-1" title="{indicatorTitle}">{indicator}</div>\n'
                        + '    <div class="clearfix"></div>\n' + '</div>',
                    },
                    language: "zh",
                    uploadUrl: url_upload,
                    uploadAsync: false,
                    uploadExtraData: {
                        folderName: uuid,
                        type: 1,
                        suspectsId: suspectsId,
                        isUpload: isUpload
                    },
                    maxFileCount: 1,
                    allowedFileExtensions: ["jpg", "png", "jpeg", "bmp", "gif"],
                    browseClass: "btn btn-success",
                    browseLabel: "附件图片",
                    browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
                    removeClass: "btn btn-danger",
                    removeLabel: "取消",
                    removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> ",
                    uploadClass: "btn btn-info",
                    uploadLabel: "上传",
                    uploadIcon: "<i class=\"glyphicon glyphicon-upload\"></i> ",
                    showCaption: false
                });
        };

        var generateUUID = function () {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                var r = (d + Math.random() * 16) % 16 | 0;
                d = Math.floor(d / 16);
                return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
            });
            return uuid;
        };

        return {
            initpage_suspectsList: initpage_suspectsList,
            initpage_suspectsInfo: initpage_suspectsInfo,
        }
    });