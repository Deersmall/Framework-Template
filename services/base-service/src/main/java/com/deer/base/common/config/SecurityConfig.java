//package com.deer.base.config;
//
//import com.deer.base.filter.JwtAuthenticationFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Autowired
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//
//    // Eureka 安全配置 (最高优先级)
//    @Bean
//    @Order(1)
//    public SecurityFilterChain eurekaSecurityFilterChain(HttpSecurity http) throws Exception {
//
//        http.antMatcher("/**") // 保护所有路径
//            .authorizeRequests()
//            .antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll() // 静态资源放行
//            .antMatchers("/", "/eureka/**").authenticated() // Eureka 相关路径需要认证
//            .and()
//            .httpBasic() // 使用 HTTP Basic 认证
//            .and()
//            .csrf().disable(); // 禁用 CSRF
//
//        return http.build();
//    }
//
//    // 应用安全配置 (较低优先级)
//    @Bean
//    @Order(2)
//    public SecurityFilterChain applicationSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .requestMatchers().antMatchers("/**") // 匹配所有请求
//                .and()
//                .csrf().disable()
//                .cors().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/auth/login").permitAll()  // 登录接口开放
//                .antMatchers("/druid/**").hasRole("ADMIN") // Druid 监控需要管理员权限
//                .anyRequest().authenticated()  // 其他所有接口需要认证
//                .and()
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // 添加 AuthenticationManager bean
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // 内存用户管理
//    @Bean
//    public UserDetailsService userDetailsService() {
//        // Eureka 用户
//        UserDetails eurekaUser = User.builder()
//                .username("Deer")
//                .password(passwordEncoder().encode("Deer_small"))
//                .roles("USER")
//                .build();
//
//        // 管理员用户
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(eurekaUser, adminUser);
//    }
//}