import { useEffect, useState } from "react";
import API from "../../API";

export const useUnassignedTicketsVM = (onError) => {
    const [openTickets, setOpenTickets] = useState([])
    const [effect, setEffect] = useState(true)

    const getOpenTickets = async () => {
        try {
            const result = await API.getOpenTickets();
            setOpenTickets(result);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while searching open tickets");
            }
        }
    }

    const assignTicket = async (assignTicketDTO, id) => {
        try {
            await API.assignTicket(assignTicketDTO, id);
            setEffect(true);
        } catch (e) {
            switch (e.status) {
                default:
                    onError("Error while assigning ticket");
            }
        }
    }

    useEffect(() => {
        if (effect) {
            getOpenTickets();
            setEffect(false);
        }
    }, [effect]);

    return { openTickets, assignTicket }
}