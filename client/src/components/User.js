import React from "react";
import { Card } from "react-bootstrap";

export const User = ({user, ActionElement}) => {
  return (
    <div>
      <Card className="card">
        <Card.Header className="d-flex justify-content-between align-items-center">
          {user.email}
          {ActionElement}
        </Card.Header>
        <Card.Body>
          <Card.Text>{"Name: " + user.name}</Card.Text>
          <Card.Text>{"Surname: " + user.surname}</Card.Text>
        </Card.Body>
      </Card>
    </div>
  );
};
