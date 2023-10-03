import { useResolvedTicketsVM } from "../../presenters/ExpertVMs";
import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import { useNavigate } from "react-router-dom";
import { Col, Form } from "react-bootstrap";
import { useState } from "react";

export const ResolvedTickets = () => {
  const navigate = useNavigate();
  const resolvedTicketsVM = useResolvedTicketsVM(errorToast);
  const [low, setLow] = useState(true);
  const [medium, setMedium] = useState(true);
  const [high, setHigh] = useState(true);
  const [searchValue, setSearchValue] = useState("");

  const handleClick = (id) => {
    navigate("/expert/tickets/" + id);
  };

  return (
    <>
      <h1>Resolved Tickets</h1>
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
      {resolvedTicketsVM.tickets
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
        .sort((a, b) => b.timestamp.localeCompare(a.timestamp))
        .map((ticket) => (
          <TicketCard
            ticket={ticket}
            withProductDetais={true}
            handleClick={() => handleClick(ticket.id)}
          />
        ))}
      {resolvedTicketsVM.tickets.length === 0 && (
        <h2 className="text-2xl">No tickets</h2>
      )}
    </>
  );
};
