import {Card, Col} from "react-bootstrap";
import React from "react";
import {useNavigate} from "react-router-dom";

export const WarrantyCard = ({warranty, onAction}) => {
    return (<div key={warranty.id}>
        <Card className="card">
            <Card.Header className="d-flex justify-content-between align-items-center">
                {warranty.product.name}
                <div className="d-flex justify-content-between align-items-center"
                     style={{marginRight: 10, fontSize: 20}}
                     className={warranty.valid ? "text-success" : "text-danger"}>
                    {warranty.valid ? "VALID" : "INVALID"}
                </div>
            </Card.Header>
            <Card.Body>
                <Card.Text>{`Type: ${warranty.typology}`}</Card.Text>
                <Card.Text>{`Product brand: ${warranty.product.brand}`}</Card.Text>
                <Card.Text>{`Creation date: ${warranty.startDate}`}</Card.Text>
                <Card.Text>{`Expiration date: ${warranty.endDate}`}</Card.Text>
                <Card.Text>{`Ticket opened: ${warranty.tickets.length}`}</Card.Text>
                <Card.Link onClick={onAction}>See details and manage related tickets</Card.Link>
            </Card.Body>
        </Card>
    </div>)
}