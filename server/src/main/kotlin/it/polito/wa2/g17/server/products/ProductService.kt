package it.polito.wa2.g17.server.products

interface ProductService {
    fun getAll(): List<ProductDTO>
    fun getProduct(ean: String): ProductDTO
}