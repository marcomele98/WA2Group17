import { useTicketsVM } from "../../presenters/customer/TicketsVM";
import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import { useNavigate } from "react-router-dom";


export const Tickets = () => {
    const navigate = useNavigate();
    const ticketsVM = useTicketsVM(errorToast);

    const handleClick = (id) => {
            navigate("/client/tickets/" + id)
    }

    return (
        <>
            {ticketsVM.tickets.sort((a, b) => b.timestamp.localeCompare(a.timestamp))
                .map((ticket) => 
                    <TicketCard ticket={ticket} withProductDetais={true} handleClick={() => handleClick(ticket.id)} />
                )}
            {ticketsVM.tickets.length === 0 && (
                <h2 className="text-2xl">No tickets</h2>
            )}
        </>
    );
}