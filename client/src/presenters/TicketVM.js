import { useEffect, useState } from "react";
import API from "../API";

export const useTicketVM = (onError, id) => {
    const [ticket, setTicket] = useState()

    const getTicket = async () => {
        try {
            const result = await API.getTicket(id);
            setTicket(result);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while searching ticket " + id);
            }
        }
    }

    useEffect(() => {
        getTicket();
    }, []);

    return { ticket, getTicket }
}