package board.configuration;

import board.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

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
                .loginPage("/members/login")    //로그인 페이지
                .defaultSuccessUrl("/board/list") //로그인 성공 후
                .and()
                .logout()
                .logoutSuccessUrl("/members/login")//로그아웃 성공
                .invalidateHttpSession(true)    //http 세션 초기화
                .and()
                .exceptionHandling().accessDeniedPage("/user/denied");  //403  예외처리 핸들링
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
