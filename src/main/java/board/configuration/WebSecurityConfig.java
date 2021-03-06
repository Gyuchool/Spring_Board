package board.configuration;

import board.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;



@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**",".img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http
                .httpBasic().disable()//기본 로그인 페이지 사용x
                .csrf().disable()   //REST API 사용하기 때문에
                .authorizeRequests().antMatchers("/authenticate", "/join", "/login", "/api/member", "/","/members/login","/members/new").permitAll()
                .antMatchers("/list","/post", "/all", "/post/{no}", "board/search","/post/edit/{no}").hasRole("USER")           //USER, ADMIN 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN")     //ADMIN만 접근 가능
                .anyRequest().authenticated()   //나머지 요청들은 권한이 있어야만 접근 가능
                .and()
                .formLogin()
                .loginPage("/members/login")    //로그인 페이지
                .defaultSuccessUrl("/board/list") //로그인 성공 후
                .and()
                .logout()
                .logoutSuccessUrl("/members/login")//로그아웃 성공
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
