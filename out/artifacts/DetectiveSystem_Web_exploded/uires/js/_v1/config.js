define(["jquery"], function ($) {

    // dataTables默认配置
    var dataTableConfig = {
        stateSave: true,
        "language": {
            "emptyTable": "没有记录数据",
            "info": "显示 _START_ - _END_ 共 _TOTAL_ 条",
            "infoEmpty": "没有数据",
            "infoFiltered": "(由 _MAX_ 条结果过滤)",
            "infoPostFix": "",
            "thousands": ",",
            "lengthMenu": "每页 _MENU_ 条",
            "loadingRecords": "加载中...",
            "processing": "处理中...",
            "search": "搜索:",
            "zeroRecords": "没有匹配的结果",
            "paginate": {
                "first": "第一页",
                "last": "最后一页",
                "next": "下一页",
                "previous": "上一页"
            },
            "aria": {
                "sortAscending": ": 以升序排列此列",
                "sortDescending": ": 以降序排列此列"
            }
        }
    };

    // 修改原dataTables的请求参数
    var planify = function (data) {
        console.log("planify...");
        for (var i = 0; i < data.columns.length; i++) {
            column = data.columns[i];
            column.searchRegex = column.search.regex;
            column.searchValue = column.search.value;
            delete (column.search);
        }
    };

    // 将json的返回值转换成Sco_message的显示结果
    var convert2Sco_message = function (resObj) {
        require(['sco.message', 'sco.confirm'], function (sco_message, sco_confirm) {
            if (resObj.status == "success") {
                $.scojs_message(resObj.data, $.scojs_message.TYPE_OK);
            } else {
                $.scojs_message(resObj.data, $.scojs_message.TYPE_ERROR);
            }
        });
    };

    // TODO 删除确认框
    var showConfimDlg = function (option) {
        var finalOption = {
            "title": "确认",
            "message": "确定要删除吗?",
            'maxHeight': $(window).height() * 0.9 - 25,
            'modal_width': "380px",
            "showDlgEx": null,
            "action": function () {
            }
        };
        $.extend(true, finalOption, option);
        var modal = {};
        drawDialog_confirm(finalOption);
        if (typeof finalOption.showDlgEx == "function") {
            finalOption.showDlgEx();
        }
        modal = $.scojs_confirm({
            target: "#ajax-load-group-modal",
            title: finalOption.title,
            content: finalOption.message,
            action: finalOption.action
        });
        modal.show();
        return modal;
    };

    var drawDialog_confirm = function (finalOption) {
        finalOption = $.extend(true, {
            "title": "确认",
            "message": "确定要删除吗?",
            'maxHeight': $(window).height() * 0.9 - 25,
            'modal_width': "380px",
            "action": function () {
            }
        }, finalOption);
        var modalText = '<div class="modal fade" id="ajax-load-group-modal" tabindex="-1"' + 'role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
            + '<div class="modal-dialog">' + '<div class="modal-content">' + '<div class="modal-header">'
            + '<button type="button" class="close" data-dismiss="modal">' + '<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>'
            + '</button>' + '<strong class="modal-title" id="myModalLabel">-标题-</strong>' + '</div>' + '<div class="modal-body">' + '<div class="inner">'
            + '</div>' + '</div>' + '<div class="modal-footer">' + '<button type="button" class="btn btn-danger"' + 'data-dismiss="modal">取消</button> '
            + '<button type="button" class="btn btn-success" data-action="1">确定</button>' + '</div>' + '</div>' + '</div>';
        /*
         * '<div class="modal-footer">' + '<button type="button" class="btn
         * btn-sm btn-danger" data-dismiss="modal"><i class="fa fa-times"></i>
         * 关闭</button></div>' +
         */

        $("#ajax-load-group-modal").remove();
        var modelPanel = $(modalText);
        $('body').append(modelPanel);

        modelPanel.find(".modal-body").css({
            "max-height": finalOption.maxHeight + "px",
            "overflow-y": "auto"
        });

        modelPanel.find(".modal-title").text(finalOption.title);
        modelPanel.find(".inner").text(finalOption.message);
        if (finalOption.modal_width != undefined) {
            modelPanel.find(".modal-dialog").css("width", finalOption.modal_width);
        }
    };

    return {
        showConfimDlg: showConfimDlg,
        planify: planify,
        dataTableConfig: dataTableConfig,
        convert2Sco_message: convert2Sco_message
    }
});