import { useEffect, useState } from "react";
import API from "../API";

export const useTicketVM = (onError, id) => {
    const [ticket, setTicket] = useState()
    const [loading, setLoading] = useState(true);

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

    const closeTicket = async (id) => {
        try {
            let ticket = await API.closeTicket(id);
            console.log(ticket);
            setLoading(true);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while closing ticket " + id);
            }
        }
    }

    const reopenTicket = async (id ) => {
        try {
            let ticket = await API.reopenTicket(id);
            setLoading(true);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while reopening ticket " + id);
            }
        }
    }

    const resolveTicket = async (id) => {
        try {
            let ticket = await API.resolveTicket(id);
            setLoading(true);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while resolving ticket " + id);
            }
        }
    }

    useEffect(() => {
        if(loading) {
            getTicket();
            setLoading(false);
        }
    }, [loading]);

    return { ticket, getTicket, closeTicket, reopenTicket, resolveTicket }
}