package dev.mvc.health;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 개발자가 구현합니다.
 * @author soldesk
 *
 */
public interface HealthProcInter {
  /**
   * 등록
   * @param healthVO
   * @return
   */
  public int create(HealthVO healthVO);

  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<HealthVO> list_all();
 
  /**
   * 카테고리별 등록된 글 목록
   * @param htcno
   * @return
   */
  public ArrayList<HealthVO> list_by_htcno(int htcno);
  
  /**
   * 조회
   * @param healthno
   * @return
   */
  public HealthVO read(int healthno);
  
  /**
   * map 등록, 수정, 삭제
   * @param map
   * @return 수정된 레코드 갯수
   */
  public int map(HashMap<String, Object> map);

  /**
   * youtube 등록, 수정, 삭제
   * @param youtube
   * @return 수정된 레코드 갯수
   */
  public int youtube(HashMap<String, Object> map);
  
  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<HealthVO> list_by_htcno_search(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_htcno_search_count(HashMap<String, Object> hashMap);
  
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param healthVO
   * @return
   */
  public ArrayList<HealthVO> list_by_htcno_search_paging(HashMap<String, Object> map);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param htcno 카테고리 번호
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int htcno, int now_page, String word, String list_file, int search_count, 
                                      int record_per_page, int page_per_block);   

  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
  
  /**
   * 글 정보 수정
   * @param healthVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(HealthVO healthVO);
  
  /**
   * 파일 정보 수정
   * @param healthVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(HealthVO healthVO);
  
  /**
   * 삭제
   * @param healthno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int healthVO);
  
  /**
   * FK htcno 값이 같은 레코드 갯수 산출
   * @param htcno
   * @return
   */
  public int count_by_htcno(int htcno);
  
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param htcno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_htcno(int htcno);

  /**
   * FK adminsno 값이 같은 레코드 갯수 산출
   * @param adminsno
   * @return
   */
  public int count_by_adminsno(int adminsno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param adminsno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_adminsno(int adminsno);
  

  
  /**
   * 추천수 증가
   * @param healthno
   * @return 추천수 증가 여부
   */
  public int recom(int healthno);
  
  /**
   * 추천수 감소
   * @param healthno
   * @return 추천수 감소 여부
   */
  public int recom_cancel(int healthno);
}
