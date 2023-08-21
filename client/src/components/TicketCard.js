import {Card} from "react-bootstrap";
import React from "react";

export const TicketCard = ({ticket, ActionElement}) => {
    return (
        <div key={ticket.id}>
            <Card className="card">
                <Card.Header className="d-flex justify-content-between align-items-center">
                    {ticket.title}
                    {ActionElement}
                </Card.Header>
                <Card.Body>
                    <Card.Text>{`STATUS: ${ticket.status}`}</Card.Text>
                    <Card.Text>{`Problem type: ${ticket.problemType}`}</Card.Text>
                </Card.Body>
            </Card>
        </div>
    );
}