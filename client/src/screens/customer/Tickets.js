import {useTicketsVM} from "../../presenters/customer/TicketsVM";
import {errorToast} from "../../utils/Error";
import {TicketCard} from "../../components/TicketCard";

export const Tickets = () => {
    const ticketsVM = useTicketsVM(errorToast);
    return (
        <>
            {ticketsVM.tickets.sort((a, b) => b.timestamp.localeCompare(a.timestamp))
                .map(ticket => (
                    <TicketCard ticket={ticket} withProductDetais={true}/>
                ))}
            {ticketsVM.tickets.length === 0 && (
                <h2 className="text-2xl">No tickets</h2>
            )}
        </>
    );
}