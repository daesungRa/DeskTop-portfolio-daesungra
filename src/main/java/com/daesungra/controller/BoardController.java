package com.daesungra.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daesungra.domain.BoardVo;
import com.daesungra.domain.BookVo;
import com.daesungra.service.BoardService;

@Controller
@RequestMapping(value="/board")
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;

	// get board list
	@RequestMapping(value="/boardListPage/{category}", method=RequestMethod.GET)
	public String getBoardListPage (HttpServletRequest request, @PathVariable int category) {
		logger.info("call boardListPage category:" + category);
		
		List<BoardVo> listDate = boardService.getBoardListDate(category); // 날짜별 정렬결과 리스트
		List<BoardVo> listHit = boardService.getBoardListHit(category); // 조회수별 정렬결과 리스트
		logger.info("[getBoardList] 검색 리스트 길이 : " + listDate.size() + ", " + listHit.size());
		request.setAttribute("boardListDate", listDate);
		request.setAttribute("boardListHit", listHit);
		request.setAttribute("category", category);
		
		return "/board/boardListPage";
	}
	
	// search book info
	@ResponseBody // json 타입으로 반환하기 위함
	@RequestMapping(value="/searchBookInfo", method=RequestMethod.POST)
	public String searchBookInfo (HttpServletRequest request) {
		StringBuffer jsonResult = new StringBuffer();
		String search = request.getParameter("searchBookInfo");
		logger.info("search book info :" + request.getParameter("searchBookInfo"));
		
		List<BookVo> bookList = boardService.getBookInfo(search);
		if (bookList != null && bookList.size() > 0) { // 검색된 vo 객체만큼 json 타입 문자열 생성
			jsonResult.append("[" + bookList.get(0).toJson());
			for (int i = 1; i < bookList.size(); i++) {
				jsonResult.append(", " + bookList.get(i).toJson());
			}
			jsonResult.append("]");
		}
		
		logger.info("jsonResult : " + jsonResult.toString());
		return jsonResult.toString();
	}
	
	// get write page
	@RequestMapping(value="/boardWritePage/{category}", method=RequestMethod.GET)
	public String getBoardWritePage (HttpServletRequest request, @PathVariable int category) {
		logger.info("call boardWritePage category:" + category);
		request.setAttribute("category", category);
		
		return "/board/boardWritePage";
	}
	
	// write action
	@RequestMapping(value="/boardWriteAction", method=RequestMethod.POST)
	public String boardWriteAction (HttpServletRequest request) {
		BoardVo bvo = null;
		logger.info("call board write action");
		
		bvo = boardService.boardWrite(request);
		if (bvo != null) {
			request.setAttribute("boardVo", bvo);
		}
		request.setAttribute("category", request.getParameter("category"));
		
		return "/board/boardViewPage"; // 작성 후 해당 뷰 페이지로 이동
	}
	
	// view
	@RequestMapping(value="/boardViewPage/{serial}", method=RequestMethod.GET)
	public String getBoardViewPage (HttpServletRequest request, @PathVariable int serial) {
		BoardVo bvo = null;
		BoardVo insertVo = new BoardVo();
		logger.info("call boardViewPage serial : " + serial);
		
		insertVo.setSerial(serial);
		if (request.getSession().getAttribute("userId") != null) { // 접속정보가 있으면 아이디 세팅
			insertVo.setUserId((String) request.getSession().getAttribute("userId"));
		}
		bvo = boardService.boardView(insertVo);
		if (bvo != null) {
			request.setAttribute("boardVo", bvo);
			request.setAttribute("category", bvo.getCategory());
		}
		
		return "/board/boardViewPage";
	}
	
	// modify
	@RequestMapping(value="/boardModifyAction", method=RequestMethod.POST)
	public String boardModifyAction (HttpServletRequest request, @ModelAttribute BoardVo bvo) {
		BoardVo resultVo = null;
		logger.info("call board modify action");
		
		bvo.setUserId((String) request.getSession().getAttribute("userId"));
		resultVo = boardService.boardModify(bvo);
		if (resultVo != null) {
			request.setAttribute("boardVo", resultVo);
		}
		request.setAttribute("category", resultVo.getCategory());
		
		return "/board/boardViewPage"; // 수정 후 해당 뷰 페이지로 이동
	}
	
	// remove
}
