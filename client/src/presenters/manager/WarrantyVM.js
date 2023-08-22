import {useEffect, useState} from "react";
import API from "../../API";

export const useWarrantyVM = (id, onError) => {
    const [warranty, setWarranty] = useState({
        product: {
            brand: "",
            name: "",
            ean: ""
        },
        tickets: []
    })
    const getWarranty = async () => {
        try {
            const result = await API.getWarranty(id);
            setWarranty(result)
        } catch (err) {
            switch (err.status) {
                case 404:
                    onError("Warranty not found");
                default:
                    onError("Error while searching warranty");
            }
        }
    }

    useEffect(() => {
        getWarranty();
    }, []);

    return {...warranty};
}
