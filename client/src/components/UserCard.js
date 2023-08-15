import React from "react";
import { Card } from "react-bootstrap";

export const UserCard = ({ user, ActionElement, className, key, withRole }) => {
  return (
    <div className={className} key={key}>
      <Card className="card">
        <Card.Header className="d-flex justify-content-between align-items-center">
          {user.email}
          {ActionElement}
        </Card.Header>
        <Card.Body>
          <Card.Text>{"Name: " + user.name}</Card.Text>
          <Card.Text>{"Surname: " + user.surname}</Card.Text>
          {withRole && (
            <>
              <Card.Text>{"Role: " + user.role.toLowerCase()}</Card.Text>
              {
                user.skills && user.skills.length > 0 && <Card.Text>{"Skills: " + user.skills.join(", ").toLowerCase()}</Card.Text>
              }
            </>
          )}
        </Card.Body>
      </Card>
    </div>
  );
};
