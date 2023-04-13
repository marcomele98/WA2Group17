package it.polito.wa2.g17.server.products

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/API/products")
class ProductController(private val productService: ProductService) {
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<ProductDTO>{
        return productService.getAll()
    }

    @GetMapping("{ean}")
    @ResponseStatus(HttpStatus.OK)
    fun getProduct(@PathVariable ean: String): ProductDTO? {
        return productService.getProduct(ean)
    }
}