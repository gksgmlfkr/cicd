package dev.mvc.goals;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Setter @Getter
public class GoalsVO {

    /** 운동 목표 */
    private int goalsno=0;
    
    /** 체중 */
    @NotEmpty(message="체중은 필수 입력 항목입니다.")
    @Pattern(regexp = "\\d+", message = "숫자만 입력 가능합니다.")
    @Size(min=2, max=10, message="입력 글자 수는 1~10 입니다.")
    private String kg;

    /** 신장 */
    @NotEmpty(message="신장은 필수 입력 항목입니다.")
    @Pattern(regexp = "\\d+", message = "숫자만 입력 가능합니다.")
    @Size(min=1, max=10, message="입력 글자 수는 1~10 입니다.")
    private String cm="";
    
    /** 체지방 */
    @NotEmpty(message="체지방은 필수 입력 항목입니다.")
    @Pattern(regexp = "\\d+", message = "숫자만 입력 가능합니다.")
    @Size(min=1, max=10, message="입력 글자 수는 1~10 입니다.")
    private String ckg="";
    
    /** 골격근량 */
    @NotEmpty(message="골격근량은 필수 입력 항목입니다.")
    @Pattern(regexp = "\\d+", message = "숫자만 입력 가능합니다.")
    @Size(min=1, max=10, message="입력 글자 수는 1~10 입니다.")
    private String muscle="";
    
    /** 회원 번호 */
    @NotNull
    private int memberno= 0;
    
   

    
    /** 등록일 */
    private String gdate;
    


  




}
