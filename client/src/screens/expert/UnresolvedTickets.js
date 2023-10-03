import { useUnresolvedTicketsVM } from "../../presenters/ExpertVMs";
import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import { useNavigate } from "react-router-dom";
import { Col, Form } from "react-bootstrap";
import { useState } from "react";

const sortCards = (a, b) => {
  let priorityOrder = { HIGH: 3, MEDIUM: 2, LOW: 1 };
  var priorityA = priorityOrder[a.priority];
  var priorityB = priorityOrder[b.priority];
  if (priorityA === priorityB) {
    return a.timestamp.localeCompare(b.timestamp);
  }
  return priorityB - priorityA;
};

export const UnresolvedTickets = () => {
  const [low, setLow] = useState(true);
  const [medium, setMedium] = useState(true);
  const [high, setHigh] = useState(true);
  const [searchValue, setSearchValue] = useState("");
  const navigate = useNavigate();
  const unresolvedTicketsVM = useUnresolvedTicketsVM(errorToast);

  const handleClick = (id) => {
    navigate("/expert/tickets/" + id);
  };

  return (
    <>
      <h1>Unresolved Tickets</h1>
      <Col style={{ width: "100%", marginBottom: 20 }}>
        <Form.Control
          size="lg"
          type="text"
          placeholder="Search"
          onChange={(ev) => setSearchValue(ev.target.value.toLowerCase())}
        />
      </Col>
      <Form>
        <Form.Label style={{ marginRight: 20, marginBottom: 20, fontSize: 20 }}>
          Filter by priority:{" "}
        </Form.Label>
        <Form.Check
          inline
          checked={low}
          onClick={() => setLow(!low)}
          label="LOW"
        />
        <Form.Check
          inline
          checked={medium}
          onClick={() => setMedium(!medium)}
          label="MEDIUM"
        />
        <Form.Check
          inline
          checked={high}
          onClick={() => setHigh(!high)}
          label="HIGH"
        />
      </Form>
      {unresolvedTicketsVM.tickets
        .filter(
          (ticket) =>
            ticket.title.toLowerCase().startsWith(searchValue) ||
            ticket.problemType.toLowerCase().startsWith(searchValue)
        )
        .filter((ticket) => {
          if (ticket.priority === "LOW") return low;
          if (ticket.priority === "MEDIUM") return medium;
          if (ticket.priority === "HIGH") return high;
        })
        .sort(sortCards)
        .map((ticket) => (
          <TicketCard
            ticket={ticket}
            withProductDetais={true}
            handleClick={() => handleClick(ticket.id)}
          />
        ))}
      {unresolvedTicketsVM.tickets.length === 0 && (
        <h2 className="text-2xl">No tickets</h2>
      )}
    </>
  );
};
