package it.polito.wa2.g17.server.products

data class ProductDTO(
    val ean: String,
    val name: String,
    val brand: String
)

fun Product.toDTO(): ProductDTO {
    return ProductDTO(ean, name, brand)
}