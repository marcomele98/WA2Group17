
package it.polito.wa2.g17.server

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ErrorController1  {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest): String {
        // Get the HTTP error code
        val statusCode = request.getAttribute("javax.servlet.error.status_code") as Int

        // If the error code is 404, forward the request to index.html
        if (statusCode == 404) {
            return "forward:/index.html"
        }

        // For all other errors, return an error page
        return "error"
    }

}
