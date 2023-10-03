import { useTicketsVM } from "../../presenters/customer/TicketsVM";
import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import { useNavigate } from "react-router-dom";
import { Form, Col } from "react-bootstrap";
import { useState } from "react";

const sortCards = (a, b) => {
  let statusOrder = { OPEN: 2, CLOSED: 1, RESOLVED: 1, IN_PROGRESS: 3 };
  var priorityA = statusOrder[a.status];
  var priorityB = statusOrder[b.status];
  if (priorityA === priorityB) {
    return b.timestamp.localeCompare(a.timestamp);
  }
  return priorityB - priorityA;
};
export const Tickets = () => {
  const navigate = useNavigate();
  const ticketsVM = useTicketsVM(errorToast);
  const [searchValue, setSearchValue] = useState("");
  const handleClick = (id) => {
    navigate("/client/tickets/" + id);
  };

  return (
    <>
      <h1>Tickets</h1>
      <Col style={{ width: "100%", marginBottom: 20 }}>
        <Form.Control
          size="lg"
          type="text"
          placeholder="Search"
          onChange={(ev) => setSearchValue(ev.target.value.toLowerCase())}
        />
      </Col>
      {ticketsVM.tickets
        .filter(
          (ticket) =>
            ticket.title.toLowerCase().startsWith(searchValue) ||
            ticket.problemType.toLowerCase().startsWith(searchValue)
        )
        .sort(sortCards)
        .map((ticket) => (
          <TicketCard
            ticket={ticket}
            withProductDetais={true}
            handleClick={() => handleClick(ticket.id)}
          />
        ))}
      {ticketsVM.tickets.length === 0 && (
        <h2 className="text-2xl">No tickets</h2>
      )}
    </>
  );
};
