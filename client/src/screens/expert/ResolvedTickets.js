import { useResolvedTicketsVM  } from "../../presenters/ExpertVMs";
import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import { useNavigate } from "react-router-dom";


export const ResolvedTickets = () => {
    const navigate = useNavigate();
    const resolvedTicketsVM = useResolvedTicketsVM(errorToast);

    const handleClick = (id) => {
            navigate("/expert/tickets/" + id)
    }

    return (
        <>
            {resolvedTicketsVM.tickets.sort((a, b) => b.timestamp.localeCompare(a.timestamp))
                .map((ticket) => 
                    <TicketCard ticket={ticket} withProductDetais={true} handleClick={() => handleClick(ticket.id)} />
                )}
            {resolvedTicketsVM.tickets.length === 0 && (
                <h2 className="text-2xl">No tickets</h2>
            )}
        </>
    );
}