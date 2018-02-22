package challenge;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/h2-console/*").permitAll()
            .anyRequest().authenticated();

        http.csrf().disable();
        http.httpBasic();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("batman").password("password").roles("USER")
                .and()
                .withUser("superman").password("password").roles("USER")
                .and()
                .withUser("catwoman").password("password").roles("USER")
                .and()
                .withUser("dococ").password("password").roles("USER")
                .and()
                .withUser("spiderman").password("password").roles("USER")
                .and()
                .withUser("daredevil").password("password").roles("USER")
                .and()
                .withUser("profx").password("password").roles("USER")
                .and()
                .withUser("alfred").password("password").roles("USER")
                .and()
                .withUser("ironman").password("password").roles("USER")
                .and()
                .withUser("zod").password("password").roles("USER");

    }
}