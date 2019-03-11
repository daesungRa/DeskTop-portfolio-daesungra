package com.daesungra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daesungra.domain.BoardReportVo;
import com.daesungra.domain.PageDto;
import com.daesungra.service.AdminService;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private PageDto pageDto;
	
	// 게시글 리스트 조회 및 페이징 처리를 위한 변수
	private int listSize = 10;
	private int blockSize = 5;
	private int nowPage = 1;
	
	/*
	 * get admin page
	 */
	// 최초 로딩 시 관련된 모든 객체를 얻어옴
	@RequestMapping(value="/getAdminPage/{nowPage}", method=RequestMethod.GET)
	public ModelAndView getAdminPage (Model model, @PathVariable(name = "nowPage", required = false) int nowPage) {
		logger.info("[admin] load admin page");
		// 응답 객체
		ModelAndView mav = new ModelAndView();
		int dateFlag = 3; // 조회 날짜 매개변수 flag (1: 오늘, 2: 하루 전, 3: 일주일 전, 4: 한달 전)
		
		// 페이징 처리 후 결과값이 세팅된 매개변수 정의
		this.nowPage = 1;
		pageDto.setAdminPageDto(this.listSize, this.blockSize, this.nowPage, dateFlag); // 페이징 처리를 위한 도메인 객체
		pageDto.adminBoardReportPageCompute();
		
		Map<String, Object> pagenatedInputData = new HashMap<String, Object>(); // 페이징 결과값을 포함한 db input data
		pagenatedInputData.put("startNo", pageDto.getStartNo());
		pagenatedInputData.put("endNo", pageDto.getEndNo());
		pagenatedInputData.put("dateFlag", dateFlag);
		
		// board report list 저장 객체 선언
		List<BoardReportVo> brvoList = null;
		
		// board report list 저장 객체 조회 및 세팅
		brvoList = adminService.getBoardReportList(pagenatedInputData);
		if (brvoList != null) {
			logger.info("[boardreport list - admin controller] 신고 리스트 조회 완료");
			// board report list 에 대한 pageDto 생성
			PageDto brPageDto = new PageDto();
			brPageDto.setNowPage(pageDto.getNowPage());
			brPageDto.setTotSize(pageDto.getTotSize());
			brPageDto.setTotPage(pageDto.getTotPage());
			brPageDto.setTotBlock(pageDto.getTotBlock());
			brPageDto.setNowBlock(pageDto.getNowBlock());
			brPageDto.setStartNo(pageDto.getStartNo());
			brPageDto.setEndNo(pageDto.getEndNo());
			brPageDto.setStartPage(pageDto.getStartPage());
			brPageDto.setEndPage(pageDto.getEndPage());
			
			mav.addObject("brvoList", brvoList);
			mav.addObject("brPageDto", brPageDto);
		}
		
		mav.setViewName("/admin/adminPage");
		return mav;
	}
	
	// 게시글 신고 리스트 관련 정보
	@RequestMapping(value="/getBoardReportList", method=RequestMethod.POST)
	public ModelAndView getBoardReportList (HttpServletRequest request) {
		logger.info("[boardreport list - admin controller] 게시글 신고 목록 요청");
		// 응답 객체
		ModelAndView mav = new ModelAndView();
		int dateFlag = 3; // 조회 날짜 매개변수 flag (1: 오늘, 2: 하루 전, 3: 일주일 전, 4: 한달 전)
		
		// 요청 데이터 세팅
		try {
			this.nowPage = Integer.parseInt(request.getParameter("nowPage"));
			dateFlag = Integer.parseInt(request.getParameter("dateFlag"));
		} catch (Exception ex) { ex.printStackTrace(); }
		
		// 페이징 처리 후 결과값이 세팅된 매개변수 정의
		pageDto.setAdminPageDto(this.listSize, this.blockSize, this.nowPage, dateFlag); // 페이징 처리를 위한 도메인 객체
		pageDto.adminBoardReportPageCompute();
		
		Map<String, Object> pagenatedInputData = new HashMap<String, Object>(); // 페이징 결과값을 포함한 db input data
		pagenatedInputData.put("startNo", pageDto.getStartNo());
		pagenatedInputData.put("endNo", pageDto.getEndNo());
		pagenatedInputData.put("dateFlag", dateFlag);
		
		// board report list 저장 객체 선언
		List<BoardReportVo> brvoList = null;
		
		// 신고글 리스트를 구해서 mav 로 반환
		brvoList = adminService.getBoardReportList(pagenatedInputData);
		if (brvoList != null) {
			logger.info("[boardreport list - admin controller] 신고 리스트 조회 완료");
			// board report list 에 대한 pageDto 생성
			PageDto brPageDto = new PageDto();
			brPageDto.setNowPage(pageDto.getNowPage());
			brPageDto.setTotSize(pageDto.getTotSize());
			brPageDto.setTotPage(pageDto.getTotPage());
			brPageDto.setTotBlock(pageDto.getTotBlock());
			brPageDto.setNowBlock(pageDto.getNowBlock());
			brPageDto.setStartNo(pageDto.getStartNo());
			brPageDto.setEndNo(pageDto.getEndNo());
			brPageDto.setStartPage(pageDto.getStartPage());
			brPageDto.setEndPage(pageDto.getEndPage());
			
			mav.addObject("brvoList", brvoList);
			mav.addObject("brPageDto", brPageDto);
		}

		mav.setViewName("/admin/adminReportListPart"); // 리턴 경로
		return mav;
	}
	
	// 게시글 신고정보 가져오기
	@RequestMapping(value="/getBoardReportInfo/{serial}", method=RequestMethod.GET)
	public ModelAndView getBoardReportInfo (HttpServletRequest request, @PathVariable(name = "serial", required = false) int serial) {
		logger.info("[get boardreport info - admin controller] 게시글 신고정보 요청, serial : " + serial);
		ModelAndView mav = new ModelAndView();
		BoardReportVo brvo = null;
		
		brvo = adminService.getBoardReportInfo(serial);
		if (brvo != null) {
			logger.info("[get boardreport info - admin controller] 게시글 신고정보 반환완료, serial : " + serial);
			mav.addObject("brvo", brvo);
		}
		
		mav.setViewName("/admin/adminReportControlPart");
		return mav;
	}
	
	// 게시글 블럭 처리
	@ResponseBody
	@RequestMapping(value="/boardBlockAction", method=RequestMethod.POST)
	public String boardBlockAction (HttpServletRequest request) {
		String result = "0";
		int fSerial = Integer.parseInt(request.getParameter("fSerial"));
		logger.info("[board blocking action - admin controller] 게시글 블럭처리, fSerial : " + fSerial);
		
		boolean blockingResult = adminService.boardBlockAction(fSerial);
		if (blockingResult) {
			result = "1";
		}
		
		return result;
	}
	
	// 게시글 블럭 처리
	@ResponseBody
	@RequestMapping(value="/boardBlockFreeAction", method=RequestMethod.POST)
	public String boardBlockFreeAction (HttpServletRequest request) {
		String result = "0";
		int fSerial = Integer.parseInt(request.getParameter("fSerial"));
		logger.info("[board block free action - admin controller] 게시글 블럭해제 요청, fSerial : " + fSerial);
		
		boolean blockingResult = adminService.boardBlockFreeAction(fSerial);
		if (blockingResult) {
			result = "1";
		}
		
		return result;
	}
	
	// 게시글 신고요청 완료처리
	@ResponseBody
	@RequestMapping(value="/boardReportComplete", method=RequestMethod.POST)
	public String boardReportComplete (HttpServletRequest request) {
		String result = "0";
		int serial = Integer.parseInt(request.getParameter("serial"));
		logger.info("[boardreport complete - admin controller] 게시글 신고정보 완료처리, serial : " + serial);
		
		boolean reportComplete = adminService.boardReportCompleteAction(serial);
		if (reportComplete) {
			result = "1";
		}
		
		return result;
	}
}
