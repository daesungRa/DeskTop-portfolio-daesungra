package com.daesungra.domain;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

public class PageDto {
	
	@Autowired
	private SqlSession sqlSession;
	private int category; // db select 파라미터

	private int listSize;
	private int blockSize;
	private int nowPage;
	
	private int totSize;
	private int totPage;
	private int totBlock;
	private int nowBlock;
	
	private int startNo;
	private int endNo;
	private int startPage;
	private int endPage;
	
	public PageDto (int listSize, int blockSize, int nowPage, int category) {
		this.listSize = listSize;
		this.blockSize = blockSize;
		this.nowPage = nowPage;
		this.category = category;
	}
	
	public void pageCompute () {
		// 입력된 카테고리 기반으로 totSize 구한 뒤 나머지 요소 구하기
		this.totSize = sqlSession.selectOne("board.boardListPagenation", this.category);
		this.totPage = (int) Math.ceil(this.totSize / (double) this.listSize);
		this.totBlock = (int) Math.ceil(this.totPage / (double) this.blockSize);
		this.nowBlock = (int) Math.ceil(this.nowPage / (double) this.blockSize);
		this.endNo = this.nowPage * this.listSize;
		this.startNo = this.endNo - this.listSize + 1;
		if (this.endNo > this.totSize) this.endNo = this.totSize;
		this.endPage = this.nowBlock * this.blockSize;
		this.startPage = this.endPage - this.blockSize + 1;
		if (this.endPage > this.totPage) this.endPage = this.totPage;
		System.out.println("boardPageConpute 완료");
		
		System.out.println("listSize: " + listSize);
		System.out.println("blockSize: " + blockSize);
		System.out.println("nowPage: " + nowPage);
		System.out.println("totSize: " + totSize);
		System.out.println("totPage: " + totPage);
		System.out.println("totBlock: " + totBlock);
		System.out.println("nowBlock: " + nowBlock);
		System.out.println("endNo: " + endNo);
		System.out.println("startNo: " + startNo);
		System.out.println("endPage: " + endPage);
		System.out.println("startPage: " + startPage);
	}

	public int getNowPage() {
		return nowPage;
	}
	public int getTotSize() {
		return totSize;
	}
	public int getTotPage() {
		return totPage;
	}
	public int getTotBlock() {
		return totBlock;
	}
	public int getNowBlock() {
		return nowBlock;
	}
	public int getStartNo() {
		return startNo;
	}
	public int getEndNo() {
		return endNo;
	}
	public int getStartPage() {
		return startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	
}