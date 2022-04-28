package com.example.demo.config

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// filter that is sitting in front of the controller and even in front of spring security to do side effects
// like reading data from the request headers etc or just print (as in our example here) the http method of every request

@Component
class CustomRequestFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println(request.method)
        filterChain.doFilter(request, response)
    }
}