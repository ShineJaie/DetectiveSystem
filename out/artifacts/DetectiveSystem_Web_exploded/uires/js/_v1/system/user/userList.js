define([ "jquery", "config", "DataTables_bootstrap", "ColReorderWithResize", 'sco.confirm', 'sco.message' ], function($, config, DataTables_bootstrap, ColReorderWithResize, sco_confirm, sco_message) {

	var url_delete = "/DetectiveSystem/action/user/delete";
	var url_save = "/DetectiveSystem/action/user/save";

	// 用户列表
	var initpage_userList = function() {
		drawTable();
		act_search();
	};

	var act_search = function() {
		$(".search_realName").focusin(function() {

		}).focusout(function() {

		}).keypress(function(event) {
			if (event.which == 13) {
				$("#userList").DataTable().ajax.reload(); // 重新加载Ajax数据源的表数据
			}
		});

		$(".act_search").off("click").click(function() {
			$("#userList").DataTable().ajax.reload(); // 重新加载Ajax数据源的表数据
		});
	};

	var act_delete = function() {
		$("#userList").off("click", ".act_delete").on("click", ".act_delete", function() {
			var userid = $(this).attr("userid");

			config.showConfimDlg({
				"title" : "确认",
				"message" : "是否删除该条记录?",
				"showDlgEx" : null,
				"action" : function() {
					$.post(url_delete, {
						id : userid
					}, function(data) {
						config.convert2Sco_message(data);
						if (data.status == "success") {
							$("#userList").DataTable().ajax.reload(); // 重新加载Ajax数据源的表数据
						}
					}, "json");
				}
			});
		});
	};

	var initComplete = function() {
		console.log("finish load...");
		act_delete();
	};

	var genTdWithTitle = function(data) {
		var res = $("<div></div>").append($("<span></span>").html(data).attr("title", data));
		return res.html();
	};

	// Formatting function for row details - modify as you need
	var format = function(data) {
		// `data` is the original data object for the row
		return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' + '<tr>' + '<td>密码:</td>' + '<td>' + data.password + '</td>' + '</tr>' + '<tr>' + '<td>说明:</td>' + '<td>密码已经过加密</td>' + '</tr>' + '</table>';
	}

	var showDetail = function(dataTable) {
		$('#userList tbody').off('click', 'td.details-control').on('click', 'td.details-control', function() {
			console.log("show detail");

			var tr = $(this).closest('tr');
			var row = dataTable.row(tr);

			if (row.child.isShown()) {
				// This row is already open - close it
				row.child.hide();
				tr.removeClass('shown');
			} else {
				// Open this row
				row.child(format(row.data())).show();
				tr.addClass('shown');
			}
		});
	};

	var drawTable = function() {

		var tableConfig = {
			"processing" : true, // 显示加载信息
			"serverSide" : true, // 开启服务器模式
			"searching" : false, // 开启搜索功能
			"stateSave" : false, // 允许浏览器缓存Datatables，以便下次恢复之前的状态
			// "ordering" : false, // 是否启用Datatables排序
			// "scrollX" : true, // 水平滚动条
			"scrollCollapse" : true, // 允许表减少在高度有限的显示的行数
			"order" : [ [ 1, 'desc' ] ], // 设置默认排序
			"initComplete" : initComplete(), // 表格加载完成回调函数
			"dom" : "Rlfrtip", // 按什么顺序定义表的控制元素在页面上出现
			"ajax" : {
				"url" : "/DetectiveSystem/action/user/json",
				"type" : "POST",
				"data" : function(data) {
					// 添加额外的参数传给服务器
					data.realName = $(".search_realName").val();

					// 修改原dataTables参数
					config.planify(data);
				}
			},
			"columnDefs" : [ {
				"orderable" : false, // 设置不可排序
				"targets" : -1,
				"data" : null,
				"render" : function(data, type, full, meta) {
					var res = "<button class='btn btn-xs btn-danger act_delete' userid='" + full["id"] + "'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span> 删除</button>";
					return res;
				}
			} ],
			"columns" : [ {
				"className" : 'details-control',
				"orderable" : false,
				"data" : null,
				"defaultContent" : ''
			}, {
				"data" : "createDate",
				"render" : function(data, type, full, meta) {
					if (type === 'display') {
						return genTdWithTitle(data);
					} else {
						return data;
					}
				}
			}, {
				"data" : "loginName",
				"render" : function(data, type, full, meta) {
					if (type === 'display') {
						return genTdWithTitle(data);
					} else {
						return data;
					}
				}
			}, {
				"data" : "realName",
				"render" : function(data, type, full, meta) {
					if (type === 'display') {
						return genTdWithTitle(data);
					} else {
						return data;
					}
				}
			}, {
				"data" : "permission",
				"render" : function(data, type, full, meta) {
					if (data == 0) {
						data = "普通权限";
					} else if (data == 1) {
						data = "超级管理员";
					}
					if (type === 'display') {
						return genTdWithTitle(data);
					} else {
						return data;
					}
				}
			}, {
				"data" : "email",
				"render" : function(data, type, full, meta) {
					if (type === 'display') {
						return genTdWithTitle(data);
					} else {
						return data;
					}
				}
			}, {
				"data" : null
			} ]
		};

		tableConfig = $.extend(true, {}, config.dataTableConfig, tableConfig);

		var dataTable = $("#userList").on('init.dt', function() {
			$("#userList").removeClass("display").addClass("table table-striped table-bordered");
		}).DataTable(tableConfig).on('draw.dt', function() {
			$("#userList").removeClass("display").addClass("table table-striped table-bordered");
		});

		$(window).resize(function() {
			dataTable.columns.adjust().draw();
		});

		// 设置默认排序
		// dataTable.order([ 1, 'desc' ]).draw()

		// Add event listener for opening and closing details
		showDetail(dataTable);

	};

	// ============================================================================

	// 添加用户
	var initpage_userInfo = function() {
		act_save();
	};

	var act_save = function() {
		$(".form_user").off("click", ".act_save").on("click", ".act_save", function() {
			var param = {};
			$(".search_param[para_name]").each(function() {
				param[$(this).attr("para_name")] = $(this).val();
			});

			$.post(url_save, param, function(data) {
				config.convert2Sco_message(data);
				if (data.status == "success") {
					window.location.href = "/DetectiveSystem/action/user/list";
				}
			}, "json");
		});
	};

	return {
		initpage_userList : initpage_userList,
		initpage_userInfo : initpage_userInfo
	}
});