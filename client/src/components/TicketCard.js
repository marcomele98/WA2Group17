import { Button, Card } from "react-bootstrap";
import React from "react";
import { useUser } from "../presenters/LoggedUser";
import { useNavigate } from "react-router-dom";

export const TicketCard = ({
  ticket,
  ActionElement,
  withProductDetais,
  openModal,
  setSelectedTicket,
  handleClick,
}) => {
  const timestampFields = ticket.timestamp.split(" ");
  const date = timestampFields[0];
  const time = timestampFields[1];
  const hour = time.split(":")[0];
  const minutes = time.split(":")[1];

  const statusVariant = { OPEN: "text-danger", CLOSED: "text-secondary", RESOLVED: "text-success", IN_PROGRESS: "text-danger" };
  return (
    <div key={ticket.id}>
      <Card
        className="card"
        onClick={handleClick}
        style={handleClick && { cursor: "pointer" }}
      >
        <Card.Header className="d-flex justify-content-between align-items-center">
          <strong>{ticket.title}</strong>
          {ActionElement ? (
            ActionElement
          ) : (
            <div className={statusVariant[ticket.status]}>
              {ticket.status.replace("_", " ")}
            </div>
          )}
        </Card.Header>
        <Card.Body>
          <Card.Text>
            <strong>Problem type: </strong> {ticket.problemType}
          </Card.Text>
          {withProductDetais && (
            <>
              <Card.Text>
                <strong>Product name: </strong> {ticket.warranty.product.name}
              </Card.Text>
              <Card.Text>
                <strong>Product brand: </strong> {ticket.warranty.product.brand}
              </Card.Text>
              <Card.Text>
                <strong>Product EAN: </strong> {ticket.warranty.product.ean}
              </Card.Text>
            </>
          )}
          <Card.Text>
            {" "}
            <strong>
              Opened on {date} at {hour}:{minutes}
            </strong>
          </Card.Text>
          {ticket.status != "OPEN" ? (
            <Card.Text>
              {" "}
              <strong>Assigned to: </strong> {ticket.expertEmail}
            </Card.Text>
          ) : null}
          {ticket.priority && (
            <Card.Text>
              {" "}
              <strong>Priority: </strong> {ticket.priority}
            </Card.Text>
          )}
        </Card.Body>
      </Card>
    </div>
  );
};
