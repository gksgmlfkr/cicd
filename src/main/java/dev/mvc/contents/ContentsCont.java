package dev.mvc.contents;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.cate.CateProcInter;
import dev.mvc.cate.CateVO;
import dev.mvc.cate.CateVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.member.MemberVO;
import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/contents")
@Controller
public class ContentsCont {

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.cate.CateProc") // @Component("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;

  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc") // @Component("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;

  @Autowired
  Security security;

  public ContentsCont() {
    System.out.println("-> ContentsCont created.");
  }

  @GetMapping(value = "/msg")
  public String msg(Model model, String url) {

    return url; // /templates/...
  }
  
  /**
   * 게시글 작성 폼
   * @param model
   * @param contentsVO
   * @param cateno
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model, ContentsVO contentsVO, int cateno) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    CateVO cateVO = this.cateProc.read(cateno);
    model.addAttribute("cateVO", cateVO);

    return "contents/create"; // /templates/contents/create.html
  }

  /**
   * 게시글 작성 처리
   * @param request
   * @param session
   * @param model
   * @param contentsVO
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, HttpSession session, Model model, ContentsVO contentsVO,
      RedirectAttributes ra) {

//    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
    // ------------------------------------------------------------------------------
    // 파일 전송 코드 시작
    // ------------------------------------------------------------------------------
    String file1 = ""; // 원본 파일명 image
    String file1saved = ""; // 저장된 파일명, image
    String thumb1 = ""; // preview image

    String upDir = Contents.getUploadDir(); // 파일을 업로드할 폴더 준비
    System.out.println("-> upDir: " + upDir);

    // 전송 파일이 없어도 file1MF 객체가 생성됨.
    // <input type='file' class="form-control" name='file1MF' id='file1MF'
    // value='' placeholder="파일 선택">
    MultipartFile mf = contentsVO.getFile1MF();

    file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
    System.out.println("-> 원본 파일명 산출 file1: " + file1);

    long size1 = mf.getSize(); // 파일 크기
    if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
      if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
          thumb1 = Tool.preview(upDir, file1saved, 200, 150);
        }
        contentsVO.setFile1(file1); // 순수 원본 파일명
        contentsVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
        contentsVO.setThumb1(thumb1); // 원본이미지 축소판
        contentsVO.setSize1(size1); // 파일 크기

      } else { // 전송 못 하는 파일 형식
        ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
        ra.addFlashAttribute("cnt", 0); // 업로드 할 수 없는 파일
        ra.addFlashAttribute("url", "/contents/msg"); // msg.html, redirect parameter 적용
        return "redirect:/contents/msg";

      }

    } else { // 글만 등록 하는 경우
      System.out.println("-> 글만 등록");
    }

    // ------------------------------------------------------------------------------
    // 파일 전송 코드 종료
    // ------------------------------------------------------------------------------

    // Call By Reference: 메모리 공유, Hashcode 전달
//      int memberno = (int) session.getAttribute("memberno"); // adminno FK
//      contentsVO.setMemberno(memberno);
    contentsVO.setMemberno(1); // Test용
    int cnt = this.contentsProc.create(contentsVO);

    // ------------------------------------------------------------------------------
    // PK의 return
    // ------------------------------------------------------------------------------
    // System.out.println("--> contentsno: " + contentsVO.getContentsno());
    // mav.addObject("contentsno", contentsVO.getContentsno()); // redirect
    // parameter 적용
    // ------------------------------------------------------------------------------

    if (cnt == 1) {
//        type 1
//        return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

//        type 2
//        model.addAttribute("cnt", cnt);
//        model.addAttribute("code","create_success");
//        return "contents/msg";

//        type3 권장
//        return "redirect:/contents/list_all";
//        ra.addFlashAttribute("cateno", contentsVO.getCateno()); // controller-> controller: X
      ra.addAttribute("cateno", contentsVO.getCateno()); // controller -> controller : O

      return "redirect:/contents/list_cate";

//        return "redirect:/contents/list_by_cateno?cateno=" + contentsVO.getCateno();

    } else {

      ra.addFlashAttribute("cnt", 0); // 업로드 할 수 없는 파일
      ra.addFlashAttribute("url", "/contents/msg"); // msg.html, redirect parameter 적용
      return "redirect:/contents/msg"; // Post -> Get - param...
    }

//    } else { // 로그인 실패한 경우

//      return "redirect:/member/login_form_need";
  }

  /**
   * 관리자 전용 전체 게시글 목록
   * @param session
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);
    System.out.println("-> list_all");

//    if (this.memberProc.isMemberAdmin(session) == true) {
    
//      int memberno = (int)session.getAttribute("memberno");
      int memberno = 1;
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      ArrayList<ContentsVO> list = this.contentsProc.list_all();

      model.addAttribute("list", list);

      return "contents/list_all";

//    } else {
//      return "redirect:/member/login_form_need"; // /WEB-INF/views/admin/login_need.jsp
//
//    }

  }

  /**
   * 카테고리 별 게시글 목록
   * 
   * @return
   */
  @GetMapping(value = "/list_cate")
  public String list_cate(HttpSession session, Model model, int cateno) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    CateVO cateVO = this.cateProc.read(cateno);
    model.addAttribute("cateVO", cateVO);
    
//    int memberno = (int)session.getAttribute("memberno");
    int memberno = 1;
    MemberVO memberVO = this.memberProc.read(memberno);
    model.addAttribute("memberVO", memberVO);

    ArrayList<ContentsVO> list = this.contentsProc.list_cate(cateno);
    model.addAttribute("list", list);

    return "contents/list_cate";

  }
  
  /**
   * 게시글 조회
   * http://localhost:9091/contents/read?contentsno=17
   * @return
   */
  @GetMapping(value="/read")
  public String read(Model model, int contentsno) { 
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);  
    
    long size1 = contentsVO.getSize1();
    String size1_label = Tool.unit(size1);
    contentsVO.setSize1_label(size1_label);
    
    model.addAttribute("contentsVO", contentsVO);
    
    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    model.addAttribute("cateVO", cateVO);
    
    this.contentsProc.view(contentsno);
    

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_contents(contentsno);
    // mav.addObject("reply_list", reply_list);

    return "contents/read";
  }
  
  /**
   * 게시글 수정 폼
   * 
   * @return
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, int contentsno, RedirectAttributes ra) {
    
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);
    
//    if (this.memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      ContentsVO contentsVO = this.contentsProc.read(contentsno);
      model.addAttribute("contentsVO", contentsVO);
      
      CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
      model.addAttribute("cateVO", cateVO);
      
      return "contents/update_text";
      
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // mav.addObject("content", content);

//    } else {
//      ra.addAttribute("url", "/member/login_cookie_need"); 
//      return "redirect:/contents/msg"; 
//    }
    
  }
  
  /**
   * 게시글 수정 처리
   * http://localhost:9091/contents/update_text.do?contentsno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, RedirectAttributes ra, ContentsVO contentsVO) {
    
//    if (this.memberProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("contentsno", contentsVO.getContentsno());
      map.put("passwd", contentsVO.getPasswd());
      
      if (this.contentsProc.password_check(map) == 1) { // 패스워드 일치
        this.contentsProc.update_text(contentsVO); // 글수정  
         
        ra.addAttribute("contentsno", contentsVO.getContentsno());
        ra.addAttribute("cateno", contentsVO.getCateno());
//        ra.addAttribute("now_page", now_page);
//        ra.addAttribute("word", search_word);
        
        return "redirect:/contents/read"; // 페이지 자동 이동
        
      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail");
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/contents/msg"); // msg.jsp, redirect parameter 적용
        
        return "redirect:/contents/msg";  // POST -> GET -> JSP 출력
      }
      
//    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
//      ra.addAttribute("url", "/member/login_cookie_need"); 
//      
//      return "redirect:/contents/msg"; 
//    }
    
  }
  
  /**
   * 파일 수정 폼
   * http://localhost:9091/contents/update_file.do?contentsno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, int contentsno) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);
    
//    model.addAttribute("word", word);
//    model.addAttribute("now_page", now_page);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    model.addAttribute("contentsVO", contentsVO);
    
    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    model.addAttribute("cateVO", cateVO);
    
    return "contents/update_file";
  }
  
  /**
   * 파일 수정 처리 http://localhost:9091/contents/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra, 
                                   ContentsVO contentsVO) {

    
//    if (this.memberProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      ContentsVO contentsVO_old = contentsProc.read(contentsVO.getContentsno());
      
      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = contentsVO_old.getFile1saved();  // 실제 저장된 파일명
      String thumb1 = contentsVO_old.getThumb1();       // 실제 저장된 preview 이미지 파일명
      long size1 = 0;
         
      String upDir =  Contents.getUploadDir(); // C:/kd/deploy/resort_v2sbm3c/contents/storage/
      
      Tool.deleteFile(upDir, file1saved);  // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1);     // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------
          
      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = "";          // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF' 
      //           value='' placeholder="파일 선택">
      MultipartFile mf = contentsVO.getFile1MF();
          
      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize();  // 파일 크기
          
      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir); 
        
        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200); 
        }
        
      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1="";
        file1saved="";
        thumb1="";
        size1=0;
      }
          
      contentsVO.setFile1(file1);
      contentsVO.setFile1saved(file1saved);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------
          
      this.contentsProc.update_file(contentsVO); // Oracle 처리

      ra.addAttribute("contentsno", contentsVO.getContentsno());
      ra.addAttribute("cateno", contentsVO.getCateno());
//      ra.addAttribute("word", word);
//      ra.addAttribute("now_page", now_page);
      
      return "redirect:/contents/read"; // request -> param으로 접근 전환
                
//    } else {
//      ra.addAttribute("url", "/member/login_cookie_need"); // login_need.jsp, redirect parameter 적용
//      
//      return "redirect:/contents/msg"; // GET
//    }

    
  }   
  
  /**
   * 게시글 삭제 폼
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                             int contentsno, int cateno) {
    
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("cateno", cateno);
//    model.addAttribute("word", word);
//    model.addAttribute("now_page", now_page);
    
//    if (memberProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      ContentsVO contentsVO = this.contentsProc.read(contentsno);
      model.addAttribute("contentsVO", contentsVO);
      
      CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
      model.addAttribute("cateVO", cateVO);
      
      return "contents/delete"; 
      
//    } else {
//      ra.addAttribute("url", "/membe/login_cookie_need"); // /WEB-INF/views/admin/login_need.jsp
//      return "redirect:/contents/msg"; 
//    }

  }
  
  /**
   * 게시글 삭제 처리 
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra, Model model, int contentsno, int cateno) {
    
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    ContentsVO contentsVO_read = contentsProc.read(contentsno);
        
    String file1saved = contentsVO_read.getFile1saved();
    String thumb1 = contentsVO_read.getThumb1();
    
    String uploadDir = Contents.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.contentsProc.delete(contentsno); // DBMS 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
//    HashMap<String, Object> hashMap = new HashMap<String, Object>();
//    hashMap.put("cateno", cateno);
//    hashMap.put("word", word);
    
//    if (contentsProc.list_by_cateno_search_count(hashMap) % Contents.RECORD_PER_PAGE == 0) {
//      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
//      if (now_page < 1) {
//        now_page = 1; // 시작 페이지
//      }
//    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("cateno", cateno);
//    ra.addAttribute("word", word);
//    ra.addAttribute("now_page", now_page);
    
    return "redirect:/contents/list_cate"; 
    
  
  }   
  

}