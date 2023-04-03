package it.polito.wa2.g17.server.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/error").setViewName("forward:/index.html")
        /*registry.addViewController("/{x:[\\w\\-]+}").setViewName("forward:/index.html")
        registry.addViewController("/{x:[\\w\\-]+}/{y:[\\w\\-]+}").setViewName("forward:/index.html")*/
    }
}