package plant.spring.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	//パスワードを暗号化するクラスのインスタンスを生成するメソッドをBeanに登録
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(mvc.pattern("/user/signup")).permitAll()
                .requestMatchers("/plant/other/**").permitAll() // 他ユーザーの植物一覧ページ参照
                .requestMatchers("/uploads/**").permitAll() // uploadsフォルダへのアクセスを許可
                .anyRequest().authenticated()
        );

        //ログイン処理
        http.formLogin(login -> login
                .loginProcessingUrl("/user/login")
                .loginPage("/user/login")
                .failureUrl("/user/login?error")
                .usernameParameter("account")
                .passwordParameter("pass")
                .defaultSuccessUrl("/plant/mypage",true)
                .permitAll()
        );

        // CSRF 対策を無効に設定 (一時的)
        http.csrf(csrf -> csrf
                .disable()
        );

        return http.build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
