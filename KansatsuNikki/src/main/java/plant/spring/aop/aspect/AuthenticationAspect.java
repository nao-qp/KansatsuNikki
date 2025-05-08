package plant.spring.aop.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class AuthenticationAspect {
	
	// @Pointcutを使って、@Authenticatedアノテーションがついたメソッドをターゲットにします
	//  @Pointcut("@annotation(Authenticated)") // Authenticatedアノテーションが付いているメソッド

  // @Aroundアノテーションで、実行前に認証処理を挿入
	@Around("@annotation(plant.spring.aop.annotation.Authenticated)")
	public Object checkAuthentication(MethodInvocationProceedingJoinPoint joinPoint) throws Throwable {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    boolean isAuthenticated = auth != null && auth.isAuthenticated()
	                              && !(auth instanceof AnonymousAuthenticationToken);	// 匿名ユーザーではないかどうかも確認

	    if (!isAuthenticated) {
	        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	        HttpServletRequest request = attr.getRequest();

	        String accept = request.getHeader("Accept");
	        boolean isApiRequest = accept != null && accept.contains("application/json");

	        if (isApiRequest) {
	            // JSONを返すAPIリクエストの場合
	            Map<String, Object> error = new HashMap<>();
	            error.put("status", "unauthorized");
	            error.put("redirectUrl", "/user/login");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	        } else {
	            // 通常のHTML画面表示リクエスト
	            HttpServletResponse response = attr.getResponse();
	            response.sendRedirect("/user/login");
	            return null;
	        }
	    }

	    return joinPoint.proceed();
	}
//	
//  @Around("@annotation(plant.spring.aop.annotation.Authenticated)") // 上記のPointcutを適用
//  public Object checkAuthentication(MethodInvocationProceedingJoinPoint joinPoint) throws Throwable {
//	  
//      // 認証チェックを行う
//      boolean isAuthenticated = checkUserAuthentication(); // 認証状態を確認するロジックを呼び出し
//      
//      if (!isAuthenticated) {
//      	return "redirect:/user/login";		// 認証情報がない場合は、ログインページにリダイレクトする
//      }
//      // 認証されている場合は、メソッドを実行
//      return joinPoint.proceed(); // メソッド実行を進める
//  }
//
//  private boolean checkUserAuthentication() {    	
//  	// 現在のユーザーの認証情報を取得
//      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      return authentication != null ? true : false;
//  }
}