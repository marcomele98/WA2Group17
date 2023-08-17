import {useEffect, useState} from "react";
import API from "../../API";

export const useWarrantiesVM = (onError) => {
    const [warranties, setWarranties] = useState([]);

    const set = async () => {
        try {
            const result = await API.getWarranties();
            setWarranties(result);
        } catch (err) {
            switch (err.status) {
                default:
                    onError("Error while searching user");
            }
        }
    };

    useEffect(() => {
        set();
    }, []);

    return {warranties};

}