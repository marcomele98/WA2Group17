import {useEffect, useState} from "react";
import API from "../../API";

export const useTicketsVM = (onError)=> {
    const [tickets, setTickets] = useState([])

    const getTickets = async () => {
        try{
            const result = await API.getTickets();
            setTickets(result);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while searching tickets");
            }
        }
    }

    useEffect(() => {
        getTickets();
    }, []);

    return {tickets}
}