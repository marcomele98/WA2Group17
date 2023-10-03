import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import React from "react";
import { useAssignedTicketsVM } from "../../presenters/manager/AssignedTicketsVM";
import { Col, Form } from "react-bootstrap";
import { useState } from "react";

export const AssignedTickets = () => {
  const assignedTicketsVM = useAssignedTicketsVM(errorToast);
  const [searchValue, setSearchValue] = useState("");

  return (
    <>
      <div>
        <h1>Assigned Tickets</h1>
      </div>
      <Col style={{ width: "100%", marginBottom:20 }}>
        <Form.Control
          size="lg"
          type="text"
          placeholder="Search"
          onChange={(ev) => setSearchValue(ev.target.value.toLowerCase())}
        />
      </Col>
      {assignedTicketsVM.assignedTickets
        .filter(
          (ticket) =>
            ticket.title.toLowerCase().startsWith(searchValue) ||
            ticket.problemType.toLowerCase().startsWith(searchValue)
        )
        .sort((a, b) => b.timestamp.localeCompare(a.timestamp))
        .map((ticket) => (
          <TicketCard ticket={ticket} withProductDetais={true} />
        ))}
      {assignedTicketsVM.assignedTickets.length === 0 && (
        <h2 className="text-2xl">No tickets</h2>
      )}
    </>
  );
};
