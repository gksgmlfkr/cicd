package dev.mvc.cate;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping("/cate")
@Controller
public class CateCont {

  @Autowired
  @Qualifier("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;

  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  public CateCont() {
    System.out.println("-> CateCont created.");
  }

//  @GetMapping(value = "/create") // http://localhost:9091/cate/create
//  public String create(Model model) {
//
//    ArrayList<CateVO> list = this.cateProc.list_all();
//    model.addAttribute("list", list);
//
//    return "cate/create"; // /templates/cate/create.html
//  }

  @PostMapping(value = "/create") // http://localhost:9091/cate/create
  public String create(Model model, @Valid CateVO cateVO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "cate/list_search";
    }

    int cnt = this.cateProc.create(cateVO);
    System.out.println("-> cnt: " + cnt);

    if (cnt == 1) {
      model.addAttribute("code", "create_success");
      model.addAttribute("name", cateVO.getName());
      model.addAttribute("namesub", cateVO.getNamesub());
    } else {
      model.addAttribute("code", "create_fail");
      model.addAttribute("cnt", cnt);
      return "/cate/msg"; // /templates/cate/msg.html
    }
    return "redirect:/cate/list_search";
  }

  @GetMapping(value = "/list_search")
  public String list_all(Model model, CateVO cateVO) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<CateVO> list = this.cateProc.list_all();
    model.addAttribute("list", list);

    return "cate/list_search"; // /cate/list_all.html
  }

  /**
   * 조회
   * 
   * @param model
   * @param cateno 조회할 카테고리 번호
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, int cateno) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    CateVO cateVO = this.cateProc.read(cateno);
    model.addAttribute("cateVO", cateVO);

    return "cate/read"; // /templates/cate/read.html
  }

  /**
   * 수정폼
   * 
   * @param model
   * @param cateno 조회할 카테고리 번호
   * @return
   */
  @GetMapping(value = "/update")
  public String update(Model model, int cateno) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    CateVO cateVO = this.cateProc.read(cateno);
    model.addAttribute("cateVO", cateVO);

    return "cate/update"; // /templates/cate/update.html

  }

  /**
   * 수정 처리
   * 
   * @param model
   * @param cateVO
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "/update") // http://localhost:9091/cate/update
  public String update_process(Model model, @Valid CateVO cateVO, BindingResult bindingResult) {
    ArrayList<CateVOMenu> menu = this.cateProc.menu();
    model.addAttribute("menu", menu);

    if (bindingResult.hasErrors()) {
      // 페이징 버튼 목록
      return "cate/update"; // /templates/cate/update.html
    }

    int cnt = this.cateProc.update(cateVO);
//      System.out.println("-> cnt: " + cnt);

    model.addAttribute("cnt", cnt);
    if (cnt == 1) {
      return "redirect:/cate/list_search";

    } else {
      model.addAttribute("code", "update_fail");
      return "cate/msg"; // /templates/cate/msg.html
    }

  }
  
  /**
   * 삭제 폼
   * http://localhost:9091/cate/delete/1
   * @param model
   * @param cateno Category number to delete.
   * @return
   */
  @GetMapping(value="/delete")
  public String delete(Model model, int cateno) {
    
   
      ArrayList<CateVOMenu> menu = this.cateProc.menu();
      model.addAttribute("menu", menu);
      
      CateVO cateVO = this.cateProc.read(cateno);
      model.addAttribute("cateVO", cateVO);
     
      return "cate/delete";  // /templates/cate/delete.html
   
  }
  
  /**
   * 삭제 처리
   * @param model
   * @param cateno 삭제할 레코드 번호
   * @param
   * @return
   */
  @PostMapping(value="/delete")
  public String delete_process(Model model, int cateno) {
    int cnt = this.cateProc.delete(cateno); // 삭제
    System.out.println("-> delete cnt: " + cnt);
    model.addAttribute("cnt", cnt);
    
    if (cnt == 1) {
      return "redirect:/cate/list_search";
    } else {
      model.addAttribute("code", "delete_fail");
      return "cate/msg"; // /templates/cate/msg.html
    }
    
  }
  
  /**
   * 출력 순서 높임: seqno 10 -> 1
   * @param model
   * @param cateno 조회할 카테고리 번호
   * @return
   */
  @GetMapping(value="/update_seqno_forward")
  public String update_seqno_forward(Model model, int cateno) {
    
    this.cateProc.update_seqno_forward(cateno);
    
    return "redirect:/cate/list_search"; 
  }
  
  /**
   * 출력 순서 낮춤: seqno 1 -> 10
   * @param model
   * @param cateno 조회할 카테고리 번호
   * @return
   */
  @GetMapping(value="/update_seqno_backward")
  public String update_seqno_backward(Model model, int cateno) {
   
    this.cateProc.update_seqno_backward(cateno);
    
    return "redirect:/cate/list_search";
    
  }
  
  /**
   * 카테고리 공개 설정
   * @param model
   * @param cateno 조회할 카테고리 번호
   * @return
   */
  @GetMapping(value="/update_visible_y")
  public String update_visible_y(Model model, int cateno) {

    this.cateProc.update_visible_y(cateno);
    
    return "redirect:/cate/list_search";  // /templates/cate/list_search.html
  }
  
  /**
   * 카테고리 비공개 설정
   * @param model
   * @param cateno 조회할 카테고리 번호
   * @return
   */
  @GetMapping(value="/update_visible_n")
  public String update_visible_n(Model model, int cateno) {
    this.cateProc.update_visible_n(cateno);
    
    return "redirect:/cate/list_search";  // /templates/cate/list_search.html
  }
}