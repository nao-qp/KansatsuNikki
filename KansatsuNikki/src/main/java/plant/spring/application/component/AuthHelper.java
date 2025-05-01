package plant.spring.application.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.service.impl.CustomUserDetails;

@Component
public class AuthHelper {
// 認証に関するメソッドクラス
	
	/**
	 * 編集対象の植物がユーザー自身のデータかどうかチェックする
	 * 
	 * @param user
	 * @param plant
	 * @return　植物IDがユーザーIDと同じであればtrueを返す
	 */
    public boolean isOwner(CustomUserDetails user, Plants plant) {
        return plant != null && user != null && plant.getUsersId().equals(user.getId());
    }
    
    /**
     * 認証エラー401を送りリダイレクトURL(ログインページ)とstatusを送る
     * @return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response)
     */
    public ResponseEntity<Map<String, Object>> unauthorizedRedirect() {
        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", "/user/login");
        response.put("status", "unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
