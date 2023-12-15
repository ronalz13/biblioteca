/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblioteca;

import com.egg.biblioteca.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ronald
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {
    
    @Autowired 
    public UsuarioService usuarioServ;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        
        auth.userDetailsService(usuarioServ)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
        
      @Override
      protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/**")
                        .permitAll()
                .and().formLogin()
                        .loginPage("/login")
                            .loginProcessingUrl("/logincheck")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/inicio")
                            .permitAll()
                .and().logout()
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login")
                            .permitAll()
                        .and().csrf()
                               .disable();
        
        
                 
        
      }
    
}
