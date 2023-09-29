import { useUnresolvedTicketsVM } from "../../presenters/ExpertVMs";
import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import { useNavigate } from "react-router-dom";


export const UnresolvedTickets = () => {
    const navigate = useNavigate();
    const unresolvedTicketsVM = useUnresolvedTicketsVM(errorToast);

    const handleClick = (id) => {
            navigate("/expert/tickets/" + id)
    }

    return (
        <>
            {unresolvedTicketsVM.tickets.sort((a, b) => b.timestamp.localeCompare(a.timestamp))
                .map((ticket) => 
                    <TicketCard ticket={ticket} withProductDetais={true} handleClick={() => handleClick(ticket.id)} />
                )}
            {unresolvedTicketsVM.tickets.length === 0 && (
                <h2 className="text-2xl">No tickets</h2>
            )}
        </>
    );
}