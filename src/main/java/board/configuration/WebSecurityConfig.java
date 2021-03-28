package board.configuration;

import board.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Bean public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**",".img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http
                .authorizeRequests().antMatchers("/api/member", "/","/user/login","/user/new").permitAll()
                .antMatchers("/list","/post", "/all", "/user/info", "/post/{no}", "board/search","/post/edit/{no}").hasRole("USER")           //USER, ADMIN 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN")     //ADMIN만 접근 가능
                .anyRequest().authenticated()   //나머지 요청들은 권한이 있어야만 접근 가능
                .and()
                    .formLogin()
                    .loginPage("/user/login")    //로그인 페이지
                    .defaultSuccessUrl("/") //로그인 성공 후
                .and()
                    .logout()
                    .logoutSuccessUrl("/")//로그아웃 성공
                    .invalidateHttpSession(true)    //http 세션 초기화
                    .clearAuthentication(true)  //권한 정보 제거
                .and()
                    .sessionManagement()
                    .maximumSessions(1)
                    .expiredUrl("/user/login")
                    .maxSessionsPreventsLogin(true); //동일한 사용자 로그인시 x, false 일 경우 기존사용자 session 종료
                http.exceptionHandling().accessDeniedPage("/user/denied");  //403  예외처리 핸들링
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
