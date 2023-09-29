import React, { useEffect } from "react";
import { Card, Row, Col } from "react-bootstrap";
import { saveAs } from "file-saver";
import { ClickableOpacity } from "./ClickableOpacity";
import { Download } from "react-bootstrap-icons";
import { formatDate } from "../utils/DateUtils";

export const MessageCard = ({ message, textColor }) => {
  useEffect(() => {
    console.log(textColor);
  }, [textColor]);
  const download = (url) => {
    saveAs(url, "attachment"); // Put your image URL here.
  };

  return (
    <Card
      style={{ backgroundColor: "transparent", width: 500, borderWidth: "0px" }}
    >
      <Card.Header
        style={{ backgroundColor: "transparent", borderWidth: "0px" }}
      >
        <Row>
          <Col style={{ color: textColor, fontStyle: "italic" }}>
            {message.userEmail}
          </Col>
          <Col
            style={{
              float: "right",
              textAlign: "right",
              color: textColor,
              fontStyle: "italic",
            }}
          >
            {formatDate(message.timestamp)}
          </Col>
        </Row>
      </Card.Header>
      <Card.Body>
        <Col>
          {message.attachments?.map((attachment, index) => (
            <Card>
              <Row
                style={{
                  margin: 10,
                  borderWidth: "1px",
                  borderColor: "GrayText",
                }}
              >
                {attachment.type.includes("image") ? (
                  <img
                    style={{
                      width: "90%",
                      height: 150,
                      objectFit: "none",
                    }}
                    src={attachment.url}
                  />
                ) : (
                  <iframe
                    src={attachment.url}
                    style={{ height: 150, width: "90%" }}
                  />
                )}
                <ClickableOpacity onClick={() => download(attachment.url)}>
                  <Download size={40} />
                </ClickableOpacity>
              </Row>
            </Card>
          ))}
        </Col>
        <Card.Text style={{ color: textColor, marginTop: 20, fontSize: 18 }}>
          {message.text}
        </Card.Text>
      </Card.Body>
    </Card>
  );
};
