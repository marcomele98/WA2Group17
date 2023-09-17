import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import React from 'react';
import { useAssignedTicketsVM } from "../../presenters/manager/AssignedTicketsVM";


export const AssignedTickets = () => {
    const assignedTicketsVM = useAssignedTicketsVM(errorToast);
    
    return (
        <>
            <div>
                <h1>Assigned Tickets</h1>
            </div>
            {assignedTicketsVM.assignedTickets.sort((a, b) => b.timestamp.localeCompare(a.timestamp))
                .map(ticket => (
                    <TicketCard ticket={ticket} withProductDetais={true}/>
                ))}
            {assignedTicketsVM.assignedTickets.length === 0 && (
                <h2 className="text-2xl">No tickets</h2>
            )}

        </>
    );
};