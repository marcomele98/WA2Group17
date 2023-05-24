package it.polito.wa2.g17.server.products

import io.micrometer.observation.annotation.Observed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/API/products")
@Observed
class ProductController(private val productService: ProductService) {

    private val log: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<ProductDTO>{
        log.info("receive get all product command.");
        return productService.getAll()
    }

    @GetMapping("{ean}")
    @ResponseStatus(HttpStatus.OK)
    fun getProduct(@PathVariable ean: String): ProductDTO? {
        log.info("receive get product command.")
        return productService.getProduct(ean)
    }
}