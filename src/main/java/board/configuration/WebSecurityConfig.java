package board.configuration;

import board.Service.MemberService;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void configuration(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**",".img/**", "/lib/**");
    }

    protected void configure(HttpSecurity http) throws Exception{

        http.rememberMe()
                .key("secrete")
                .userDetailsService(memberService)
                .tokenValiditySeconds(60*60*24*7)   //default 토큰 유지 기간 : 2주
                .tokenRepository(tokenRepository());
        http.authorizeRequests()
                .antMatchers("/","/members/login","/members/new").permitAll()  //누구나 접근 가능
                .antMatchers("/list","/post", "/post/{no}", "board/search","/post/edit/{no}").hasRole("ROLE_USER")           //USER, ADMIN 접근 가능
                .antMatchers("/admin/**").hasRole("ROLE_ADMIN")     //ADMIN만 접근 가능
                .anyRequest().authenticated()   //나머지 요청들은 권한이 있어야만 접근 가능
                .and()
                    .formLogin()
                    .loginPage("/members/login")    //로그인 페이지
                    .defaultSuccessUrl("/list") //로그인 성공 후
                .and()
                    .logout()
                    .logoutSuccessUrl("/members/login") //로그아웃 성공
                    .invalidateHttpSession(true);   //세션 날리기

    }

    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }


    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }


}
