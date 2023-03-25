import '../App.css';
import API from '../API';
import { toast } from "react-toastify";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";


function Product({ setIsLoading }) {

    const [product, setProduct] = useState({});

    const navigate = useNavigate();
    const { ean } = useParams();

    useEffect(() => {
        const getProductFromServer = async () => {
            try {
                setIsLoading(true);
                const res = await API.getProductByEan(ean);
                setProduct(res);
                setIsLoading(false);
            } catch (err) {
                setIsLoading(false);
                if (err.status == 404)
                    navigate("/products")
                else
                    toast.error("Server error", { position: "top-center" }, { toastId: 4 });
            }
        };
        getProductFromServer()
    }, [ean])


    return (
        <div>
            <h3>{product.name}</h3>
            <div>{"ean: " + product.ean}</div>
            <div>{"brand: " + product.brand}</div>
        </div>
    )

}


export { Product };