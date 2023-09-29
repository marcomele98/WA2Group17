import React from "react";
import { Row, Col } from "react-bootstrap";
import ".././components/Chat.css";
export const TicketDetails = ({ ticket }) => {
  return (
    <Row>
      <Col>
        <div>
          <div>
            <strong>Product:</strong> {ticket?.warranty?.product?.name}
          </div>
          <div>
            <strong>Warranty:</strong> {ticket?.warranty?.typology}
          </div>
          <div>
            <strong>Problem Type:</strong> {ticket?.problemType}
          </div>
        </div>
      </Col>
      <Col>
        <div>
        <div>
            <strong>Priority:</strong> {ticket?.priority || "Not specified"}
          </div>
          <div>
            <strong>Date:</strong> {ticket?.timestamp.split(" ")[0]}
          </div>
            <div>
                <strong>Time:</strong> {ticket?.timestamp.split(" ")[1].split(".")[0]}
            </div>
        </div>
      </Col>
      {ticket?.expert && (
        <Col>
          <div>
          <div>
            <strong>Customer:</strong>{" "}
            {ticket?.warranty?.customer?.name +
              " " +
              ticket?.warranty?.customer?.surname}
          </div>
            <div>
              <strong>Expert:</strong>{" "}
              {ticket?.expert.name + " " + ticket?.expert.surname}
            </div>
            <div>
            <strong>Status:</strong> {ticket?.status.replace("_", " ")}
          </div>
          </div>
        </Col>
      )}
    </Row>
  );
};
