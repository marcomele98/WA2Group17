import API from "../API";
import { useState, useEffect } from "react";

const UNRESOLVED = 0;
const RESOLVED = 1; 


const useTicketsVM = (onError, type) => {
    const [tickets, setTickets] = useState([])

    const getTickets = async () => {
        try{
            const result = type === UNRESOLVED ? await API.getUnresovedTickets() : await API.getResolvedTickets();
            console.log(result);
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


export const useUnresolvedTicketsVM = (onError) => {
    return useTicketsVM(onError, UNRESOLVED);
}

export const useResolvedTicketsVM = (onError) => {
    return useTicketsVM(onError, RESOLVED);
}