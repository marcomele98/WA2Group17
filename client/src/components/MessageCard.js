import React from "react";
import { Card } from "react-bootstrap";

export const MessageCard = ({ message, className }) => {

  return (
    <Card className="${className}" style={{width:500}}>
        <Card.Header>{message.userEmail}</Card.Header>
      <Card.Body>
        <Card.Text>{message.text}</Card.Text>
      </Card.Body>
      <Card.Footer style={{textAlign:"right"}} className="text-muted">
        <span>{message.timestamp}</span>
      </Card.Footer>
    </Card>
  );
}

