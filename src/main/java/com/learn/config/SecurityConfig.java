package com.learn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //for PreAuthorize etc
public class SecurityConfig
{

    //Below we've copied from a class named SpringBootWebSecurityConfiguration
    //to not use/ modify default security filter
    //some other modifications are also done according to the directions provided in the said class
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) ->
                requests.requestMatchers("/h2-console/**").permitAll() //no authentication req. for /h2-console and beyond
                .anyRequest().authenticated());

        //HTML related (inspect element) h2-console frames not visible until below config
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                        .sameOrigin()
                )
        );
        http.csrf(AbstractHttpConfigurer::disable); //else continuous sign in issue

//        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
    @Autowired
    DataSource dataSource;

    //For multiple users
    //InMemoryUserDetailsManager is Non-Persistent implementation
    //Meaning that user details (name and password) will not be stored in a database/disk
    @Bean
    public UserDetailsService userDetailsService()
    {
        UserDetails user1 = User.withUsername("user1")
                .password(passwordEncoder().encode("password1"))
//                .password("{noop}password1") //noop tells spring that password should be saved in plain text(& not encoded); not a good production practice
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                //.password(passwordEncoder().encode("adminPass"))
                .password("{noop}adminPass")
                .roles("ADMIN")
                .build();



        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(admin);

        return userDetailsManager;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //Bcrypt is internally built, uses 'salting'
    }


        //InMemoryUserDetailsManager requires object of type UserDetails
        //Below the constructor of the class is called
//        return new InMemoryUserDetailsManager(user1,admin); //creates users in memory (RAM)

    }

