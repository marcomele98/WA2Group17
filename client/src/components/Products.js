import '../App.css';
import API from '../API';
import {toast} from "react-toastify";
import {useEffect, useState} from "react";
import {Card, FormControl} from "react-bootstrap";
import {Link} from "react-router-dom";

function Products({setIsLoading}) {

    const [products, setProducts] = useState([]);
    const [filter, setFilter] = useState("");

    useEffect(() => {
        const getProductsFromServer = async () => {
            try {
                setIsLoading(true);
                const res = await API.getProducts();
                setProducts(res);
                setIsLoading(false);
            } catch (err) {
                toast.error("Server error.", {position: "top-center"}, {toastId: 4});
                setIsLoading(false);
            }
        };
        getProductsFromServer()
    }, [])


    return (
        <>
            <div>
                <FormControl type="text" placeholder="Search" onChange={ev => setFilter(ev.target.value.toLowerCase())}
                             className="mr-sm-2 mb-3"/>
                {products
                    .filter(
                        product =>
                            !filter ||
                            (product.name.toLowerCase().includes(filter)
                                || product.brand.toLowerCase().includes(filter)
                                || product.ean.toLowerCase().includes(filter)))
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