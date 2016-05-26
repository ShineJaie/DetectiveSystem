;
(function($) {
	$.fn.showLinkLocation = function() {
		return this.filter('a').append(function() {
			return ' (' + this.href + ')';
		});
	};
}(jQuery));

// 用法
// $('a').showLinkLocation();
