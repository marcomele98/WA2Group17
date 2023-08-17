import {Card} from "react-bootstrap";
import React from "react";

export const WarrantyCard = ({warranty, ActionElement}) => {
    return (
        <div key={warranty.id}>
            <Card className="card">
                <Card.Header className="d-flex justify-content-between align-items-center">
                    {`Warranty# ${warranty.id}`}
                    {ActionElement}
                </Card.Header>
                <Card.Body>
                    <Card.Text>{`Product: ${warranty.product.name}`}</Card.Text>
                    <Card.Text>{`Product brand: ${warranty.product.brand}`}</Card.Text>
                    <Card.Text>{`Creation date: ${warranty.startDate}`}</Card.Text>
                    <Card.Text>{`Expiration date: ${warranty.endDate}`}</Card.Text>
                    <Card.Text>{`Ticket opened: ${warranty.tickets.length}`}</Card.Text>
                </Card.Body>
            </Card>
        </div>
    )
}