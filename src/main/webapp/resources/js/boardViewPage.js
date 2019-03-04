/*
 * 작성자: 라대성
 * 작성일: 190304
 * 기능: boardViewPage
 */
$(function () {
	/*
	 * component
	 */
	// top 의 돋보기 아이콘 배치
	$('.searchForm .icon').css({"margin-top":"-12px"});
	// 좌측 네비게이션 바 토글
	$('#btnShowNavbar').click(function () {
		$('#navbarAside').animate({width:'toggle'}, 350);
		if ($('#btnShowNavbar').text() == '<<') {
			$('#btnShowNavbar').text('>>');
			$('#btnShowNavbar').animate({left:'-=17%'}, 350);
			$('#boardContent').animate({margin: '0 0 0 0'}, 350);
			/*$('#navbarAside').css({"display":"none"});*/
		} else if ($('#btnShowNavbar').text() == '>>') {
			$('#btnShowNavbar').text('<<');
			$('#btnShowNavbar').animate({left: '+=17%'}, 350);
			$('#boardContent').animate({margin: '0 0 0 17%'}, 350);
			/*$('#navbarAside').css({"display":"block"});*/
		}
	});
	// top 부분으로 이동
	$('.my-btn-toTop').click(function () {
		funcMovePage('indexTop');
	});
	// 게시글 카테고리 바꾸기
	$('#changeCategoryInWritePage').change(function () {
		var category3 = $(this).children('option:selected').val();
		var requestUrl = '/desktop/board/boardListPage/' + category3;
		
		location.href = requestUrl;
	});
	// 댓글, 좋아요, 설정 부분의 tooltip
	$('#rightSideComponent #iconComment').tooltip();
	$('#rightSideComponent #iconThumbUpDown').tooltip();
	$('#rightSideComponent #iconSettings').tooltip();
	
	/*
	 * modify 페이지로 변경
	 */
});