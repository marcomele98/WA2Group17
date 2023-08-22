import {Card} from "react-bootstrap";
import React from "react";

export const TicketCard = ({ticket, ActionElement, withProductDetais}) => {
    const timestampFields = ticket.timestamp.split(" ");
    const date = timestampFields[0];
    const time = timestampFields[1];
    const hour = time.split(":")[0];
    const minutes = time.split(":")[1];
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
                    {
                        withProductDetais && (
                            <>
                                <Card.Text>
                                    {`Product name: ${ticket.warranty.product.name}`}
                                </Card.Text>
                                <Card.Text>
                                    {`Product brand: ${ticket.warranty.product.brand}`}
                                </Card.Text>
                                <Card.Text>
                                    {`Product EAN: ${ticket.warranty.product.ean}`}
                                </Card.Text>
                            </>
                        )
                    }
                    <Card.Text>{`Opened on ${date} at ${hour}:${minutes}`}</Card.Text>
                </Card.Body>
            </Card>
        </div>
    );
}