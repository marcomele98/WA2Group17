import { useState } from "react";
import API from "../API";

export const useTicketVM = (onError) => {
    const [ticket, setTicket] = useState()

    const getTicket = async (id) => {
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

    return { ticket, getTicket }
}