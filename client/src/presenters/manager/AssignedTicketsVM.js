import { useEffect, useState } from "react";
import API from "../../API";

export const useAssignedTicketsVM = (onError) => {
    const [assignedTickets, setAssignedTickets] = useState([])

    const getAssignedTickets = async () => {
        try {
            const result = await API.getAssignedTickets();
            setAssignedTickets(result);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while searching tickets");
            }
        }
    }


    useEffect(() => {

        getAssignedTickets();
    }, []);

    return { assignedTickets }
}