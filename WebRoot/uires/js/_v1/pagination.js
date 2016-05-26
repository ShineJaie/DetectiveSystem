// 获取表格
// containerStr tableLink 必须
// 如果有搜索参数则补在 params 里
;
(function($) {
	$.fn.pagination = function(settings, func) {
		 console.log('pagination', settings);
		var options = {
			containerStr : "",
			tableLink : "",
			params : {
				pageNumber : 1,
				pageSize : 15
			}
		};
		if (settings != null) {
			$.extend(true, options, settings);
		}

		$(options.containerStr).load(options.tableLink, options.params, function() {
			console.log('load finish');

			if ($(options.containerStr + " input.pagedata").length != 0) {
				var thisPageData = $(options.containerStr + " input.pagedata").val();
				// console.log(thisPageData);
				// console.log(JSON.parse(thisPageData));
				thisPageData = JSON.parse(thisPageData);
				thisPageData.currentNumber = options.params.pageNumber;
				thisPageData.pageSize = options.params.pageSize;
				createPager(options.containerStr, thisPageData);
				tableAction(options, func);
			}
			if (typeof func == "function") {
				func();
			}
			
			/**
			 * private 创建分页
			 * 
			 * @param targetStr
			 * @param pagerData
			 *            分页显示用的数据
			 */
			var createPager = function(targetStr, pagerData) {
				// console.log(pagerData, requestData);
				if (typeof pagerData == 'undefined') {
					return;
				}
				// console.log("pagerData", pagerData);

				var pageSize = pagerData["pageSize"];
				var leftNumber = pagerData["leftNumber"];
				var btnNumbers = pagerData["btnNumbers"];
				var currentNumber = pagerData["currentNumber"];
				var rightNumber = pagerData["rightNumber"];
				var pageCount = pagerData["pageCount"];
				var totalCount = pagerData["totalCount"];
				// console.log(btnNumbers);

				$(targetStr + " .paginationDiv").empty();
				var paginationDiv = $(targetStr + " .paginationDiv").addClass("clearfix")
						.attr("pagesize", pageSize).attr("pagenumber", currentNumber);
				var ul = $('<ul class="pagination pull-right"></ul>');

				var leftpage = $('<li><a href="javascript:;" aria-label="Previous" pagenum="'
						+ leftNumber + '">' + '<span aria-hidden="true">&laquo;</span></a></li>');
				if (leftNumber == -1) {
					leftpage.addClass("disabled");
				}
				ul.append(leftpage);

				for (var i = 0; i < btnNumbers.length; ++i) {
					var btn = $('<li class="pagebtn"><a href="javascript:;" pagenum="'
							+ btnNumbers[i] + '">' + btnNumbers[i] + '</a></li>');
					// console.log(currentNumber, btnNumbers[i]);
					if (currentNumber == btnNumbers[i]) {
						btn.addClass("active");
					}
					ul.append(btn);
				}

				var rightpage = $(' <li><a href="javascript:;" aria-label="Next" pagenum="'
						+ rightNumber
						+ '" >'
						+ '<span aria-hidden="true">&raquo;</span></a></li>');
				if (rightNumber == -1) {
					rightpage.addClass("disabled");
				}
				ul.append(rightpage);

				var pageSizeSelect = $('<div class="select-pageSize pull-right">'
						+ '<select class="form-control input-sm set-items-per-page" >'
						+ ' <option>15</option>' + ' <option>30</option>'
						+ '<option>80</option>' + '<option>100</option>'
						+ ' <option>200</option>' + ' </select>' + ' </div>');
				pageSizeSelect.find("select").val(pageSize);
				var textMyxs = $('<div class="text-myxs pull-right">'
						+ '<span>每页显示：</span>' + '</div>');
				var textPageCount = $('<div class="text-page-count pull-right">第 '
						+ currentNumber + '/' + pageCount + ' 页( 共有 ' + totalCount
						+ ' 条记录 )' + ' </div>');

				paginationDiv.append(ul, pageSizeSelect, textMyxs, textPageCount);
			};
			
			/**
			 * private 表格的按钮动作
			 * 
			 * @param data
			 * @param func
			 */
			var tableAction = function(options, func) {
				$(options.containerStr + " .pagebtn ").unbind("click");
				$(options.containerStr + " .pagebtn ").click(
						function() {

							// console.log($(this).find("a").attr("pagenum"));
							var pageSize = $(this).parents().find(".paginationDiv").attr(
									"pageSize");
							var pageNumber = $(this).find("a").attr("pagenum");
							if (pageNumber < 0) {
								console.log("invade number");
								return;
							}
							options.params.pageNumber = pageNumber;
							options.params.pageSize = pageSize;

//							config.setIsInLoad(data.containerStr, true);
							pagination(options, func);
						});

				$(options.containerStr + " .set-items-per-page").unbind("change");
				$(options.containerStr + " .set-items-per-page").change(function() {
					var pagesizeAft = $(this).val();
					options.params.pageNumber = 1;
					options.params.pageSize = pagesizeAft;

//					config.setIsInLoad(data.containerStr, true);
					pagination(options, func);
				});
			};
		});
		return this;
	};
}(jQuery));
