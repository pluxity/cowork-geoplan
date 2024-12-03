$(function() {

	// 관리자 좌측 메뉴 처리
	$(".sidebar > li.nav-item").each(function() {
		var menuUrl = $(this).data("menu-url");
		if(_CURRENT_URL.indexOf(menuUrl) != -1) {
			$(this).addClass("active");
			$(this).find(".dropdown-menu").addClass("show");
		} else {
			$(this).removeClass("active");
			$(this).find(".dropdown-menu").removeClass("show");
		}
	});
	$(".sidebar .dropdown-item").each(function() {
		if($(this).attr("href") == _CURRENT_URL) {
			$(this).addClass("active");
		} else {
			$(this).removeClass("active");
		}
	});


});