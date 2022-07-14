//package com.example.application.config
//
//import com.example.application.security.SecurityUtils
//import org.springframework.context.annotation.Bean
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.builders.WebSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.provisioning.InMemoryUserDetailsManager
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//
//
//private const val LOGIN_URL = "/login"
//private const val LOGIN_PROCESSING_URL = "login"
//private const val LOGIN_FAILURE_URL = "/login?error"
//private const val LOGOUT_SUCCESS_URL = "/login"
//
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//    prePostEnabled = true,
//    securedEnabled = true
//)
//class SecurityConfigurer(
//) : WebSecurityConfigurerAdapter() {
//
//    @Bean
//    override fun authenticationManager(): AuthenticationManager {
//        return super.authenticationManager()
//    }
//
//    @Bean
//    fun requestCache(): CustomRequestCache? { // (2)
//        return CustomRequestCache()
//    }
//
//    override fun configure(http: HttpSecurity) {
//        http.csrf().disable()
//            .requestCache().requestCache(requestCache())
//            .and().authorizeRequests()
//            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
//            .anyRequest().authenticated()
//            .and().formLogin().loginPage(LOGIN_URL).permitAll()
//            .loginProcessingUrl(LOGIN_PROCESSING_URL)
//            .failureUrl(LOGIN_FAILURE_URL)
//            .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
//    }
//
//    @Bean
//    override fun userDetailsService(): UserDetailsService? {
//        val user: UserDetails = User.withUsername("user")
//            .password("{noop}userpass")
//            .roles("USER")
//            .build()
//        return InMemoryUserDetailsManager(user)
//    }
//
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val source = UrlBasedCorsConfigurationSource()
//        val corsConfiguration = CorsConfiguration().applyPermitDefaultValues()
//        corsConfiguration.addAllowedMethod("DELETE")
//        corsConfiguration.addAllowedMethod("PUT")
//        corsConfiguration.addAllowedMethod("POST")
//        source.registerCorsConfiguration("/**", corsConfiguration)
//        return source
//    }
//
//    @Bean
//    fun passwordEncoder() = BCryptPasswordEncoder(10)
//
//    override fun configure(web: WebSecurity) {
//        web.ignoring().antMatchers( // Client-side JS
//            "/VAADIN/**",  // the standard favicon URI
//            "/favicon.ico",  // the robots exclusion standard
//            "/robots.txt",  // web application manifest
//            "/manifest.webmanifest", "/sw.js","/offline.html", // web application manifest
//            "/frontend/**", // (development mode) static resources
//            "/webjars/**", // (development mode) webjars
//            "/icons/**", "/images/**",  // icons and images
//            "/styles/**",  // (development mode) H2 debugging console
//            "/h2-console/**", // (development mode) H2 debugging console
//            "/frontend-es5/**", "/frontend-es6/**" // (production mode) static resources
//        )
//    }
//}