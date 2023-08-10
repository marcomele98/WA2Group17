import API from '../API';
import {toast} from "react-toastify";
import {useEffect, useState} from "react";
import {Card, FormControl} from "react-bootstrap";
import {Link} from "react-router-dom";

function Products() {

    const [products, setProducts] = useState([]);
    const [filter, setFilter] = useState("");

    useEffect(() => {
        const getProductsFromServer = async () => {
            try {
                const res = await API.getProducts();
                setProducts(res);
            } catch (err) {
                toast.error("Server error.", {position: "top-center"}, {toastId: 4});
            }
        };
        getProductsFromServer()
    }, [])


    return (
        <>

            <div className="searchBar">
                <FormControl type="text" placeholder="Search" onChange={ev => setFilter(ev.target.value.toLowerCase())}
                             className="mr-sm-2 mb-3"/>
            </div>
            <div className="products">
                {products
                    .filter(
                        product =>
                            product.name.toLowerCase().includes(filter)
                                || product.brand.toLowerCase().includes(filter)
                                || product.ean.toLowerCase().includes(filter))
                    .map((product) => {
                        return (

                            <Card className='card' key={product.ean}>
                                <Card.Header>{product.name}
                                    <Link to={"/products/" + product.ean} className="details">
                                        <div>
                                            Details
                                        </div>
                                    </Link>

                                </Card.Header>
                                <Card.Body>
                                    <Card.Text>{"brand: " + product.brand}</Card.Text>
                                </Card.Body>
                            </Card>
                        )
                    })
                }
            </div>
        </>

    );
}


export {Products};