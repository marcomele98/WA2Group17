import '../App.css';
import API from '../API';
import { toast } from "react-toastify";
import { useEffect, useState } from "react";
import { Card } from "react-bootstrap";
import { Link } from "react-router-dom";

function Products({ setIsLoading }) {

    const [products, setProducts] = useState([]);

    useEffect(() => {
        const getProductsFromServer = async () => {
            try {
                setIsLoading(true);
                const res = await API.getProducts();
                setProducts(res);
                setIsLoading(false);
            } catch (err) {
                toast.error("Server error.", { position: "top-center" }, { toastId: 4 });
                setIsLoading(false);
            }
        };
        getProductsFromServer()
    }, [])


    return (
        <>
            <div>
                {products.map((product) => {
                    return (
                    
                        <Card className='card' key={product.ean}>
                            <Card.Header>{product.name}
                                <Link to={"/products/" + product.ean} className="details"><div>
                                        Details
                                    </div></Link>

                            </Card.Header>
                            <Card.Body>
                                <Card.Text>{"brand: " + product.brand}</Card.Text>
                            </Card.Body>
                        </Card>
                    )
                })}
            </div>
        </>

    );
}


export { Products };