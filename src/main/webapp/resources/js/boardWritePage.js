/*
 * 작성자: 라대성
 * 작성일: 190303
 * 기능: boardWritePage
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
	
	// 게시글 카테고리 바꾸기
	$('#changeCategoryInWritePage').change(function () {
		var category3 = $(this).children('option:selected').val();
		var requestUrl = '/desktop/board/boardWritePage/' + category3;
		
		location.href = requestUrl;
	});
	
	// 책 정보 검색
	var availableTags = [
	      "ActionScript",
	      "AppleScript",
	      "Asp",
	      "BASIC",
	      "C",
	      "C++",
	      "Clojure",
	      "COBOL",
	      "ColdFusion",
	      "Erlang",
	      "Fortran",
	      "Groovy",
	      "Haskell",
	      "Java",
	      "JavaScript",
	      "Lisp",
	      "Perl",
	      "PHP",
	      "Python",
	      "Ruby",
	      "Scala",
	      "Scheme"
	    ];
	$('#searchBookInBoardWrite').autocomplete({
		source: function (request, response) {
			$.ajax({
				type: 'post',
				url: '/desktop/board/searchBookInfo',
				dataType: 'json',
				data: {searchBookInfo : request.term},
				success: function (data) {
					response(data);
				}
			});
		},
		minLength: 2
	});
	// 글쓰기 폼 (summernote)
	$('#summernote').summernote({
		height: 430,
		minHeight: 430,
		maxHeight: null,
		placeholder: '내용을 입력하세요',
	});
	// 글쓰기 cancel > 같은 카테고리의 리스트 페이지로 이동
	$('#boardWriteForm #btnBoardWriteCancel').click(function () {
		var categoryNum = $('#saveCategoryNum').text();
		var requestUrl = '/desktop/board/boardListPage/' + categoryNum;
		
		location.href = requestUrl;
	});
	
	// top 부분으로 이동
	$('.my-btn-toTop').click(function () {
		funcMovePage('indexTop');
	});
	
});