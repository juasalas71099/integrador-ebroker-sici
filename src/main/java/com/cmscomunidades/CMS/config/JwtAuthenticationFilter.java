package com.cmscomunidades.CMS.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        //final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        //username = jwtService.extractUsername(jwt);
        /*
        if(SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                //Guardar Logger en la bd eficenLogs que no sean GET
//                if(!request.getMethod().equals("GET")){
//                    saveLogger(username,request,response);
//                }
            }
        }*/

        filterChain.doFilter(request,response);

    }

//
//    public void saveLogger(String username, HttpServletRequest request,HttpServletResponse response){
//        if(!request.getServletPath().contains("permiso") && !request.getServletPath().contains("logs")){
//            ApiLog apiLog = new ApiLog();
//            apiLog.setUsuario(username);
//            apiLog.setAccion(request.getMethod());
//            apiLog.setFecha(LocalDateTime.now());
//            apiLog.setRuta(request.getServletPath());
//            apiLog.setRespuesta(response.getStatus()+"");
//            apiLog.setIp(request.getRemoteAddr());
//            apiLogRest.save(apiLog);
//        }
//
//    }
}
