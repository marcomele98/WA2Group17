import React from "react";
import { Card, Row } from "react-bootstrap";
import { saveAs } from "file-saver";
import Iframe from 'react-iframe-click';

export const MessageCard = ({ message }) => {
  const download = (url) => {
    saveAs(url, "attachment"); // Put your image URL here.
  };
  return (
    <Card className="${className}" style={{ width: 500 }}>
      <Card.Header>{message.userEmail}</Card.Header>
      <Card.Body>
        <Row>
          {message.attachments?.map((attachment, index) =>
            attachment.type.includes("image") ? (
              <img style={{ width: "100%" }} src={attachment.url} onClick={()=>download(attachment.url)} />
            ) : (
              <Iframe src={attachment.url}  onInferredClick={()=>download(attachment.url)}/>
            )
          )}
        </Row>
        <Card.Text>{message.text}</Card.Text>
      </Card.Body>
      <Card.Footer style={{ textAlign: "right" }} className="text-muted">
        <span>{message.timestamp}</span>
      </Card.Footer>
    </Card>
  );
};
