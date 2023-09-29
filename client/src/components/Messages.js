import React, { useEffect } from "react";
import { errorToast, errorToaster } from "../utils/Error";
import { MessageCard } from "./MessageCard";
import { Card, ListGroup, ListGroupItem } from "react-bootstrap";
import ".././components/Chat.css";
import { Form, Button } from "react-bootstrap";
import { ClickableOpacity } from "./ClickableOpacity";
import { Paperclip, X, SendFill } from "react-bootstrap-icons";
import { successToast } from "../utils/Error";
import { useState } from "react";
import { useAddMessageVM } from "../presenters/AddMessageVM";
import { useUser } from "../presenters/LoggedUser";
import { isObjectEmpty } from "../utils/objects";

export const Messages = ({ id, messages, refreshMessages, canSendMessage }) => {
  const fileInputRef = React.useRef(null);
  const user = useUser();
  const addMessageVM = useAddMessageVM();
  const [chatHeight, setChatHeight] = useState(0);
  const [pageHeight, setPageHeight] = useState(window.innerHeight);

  const updatePageHeight = () => {
    setPageHeight(window.innerHeight);
  };

  useEffect(() => {
    window.addEventListener("resize", updatePageHeight);
    return () => {
      window.removeEventListener("resize", updatePageHeight);
    };
  }, []);

  useEffect(() => {
    console.log("pageHeight", pageHeight);
  }, [pageHeight]);

  const handleAttachmentClick = (e) => {
    e.stopPropagation();
    e.preventDefault();
    // Simula un click sull'input file quando l'utente fa clic sull'icona della clip
    fileInputRef.current.click();
  };

  const onChangeAttachment = (e) => {
    e.stopPropagation();
    e.preventDefault();
    const file = e.target.files[0];
    if (file) {
      addMessageVM.addAttachment(file);
    }
  };

  const onSubmit = errorToaster(async (event) => {
    event.preventDefault();
    await addMessageVM.save(id);
    addMessageVM.reset();
    refreshMessages(id);
    successToast("Message sended successfully");
  });

  useEffect(() => {
    if (!canSendMessage) {
      setChatHeight(pageHeight - 270);
    } else if (!isObjectEmpty(addMessageVM.attachments)) {
      setChatHeight(pageHeight - 420);
    } else {
      setChatHeight(pageHeight - 380);
    }
  }, [isObjectEmpty(addMessageVM.attachments), canSendMessage, pageHeight]);

  useEffect(() => {
    var objDiv = document.getElementById("chat");
    console.log(objDiv.clientHeight);
    objDiv.scrollTop = objDiv.scrollHeight;
  }, [messages.length, isObjectEmpty(addMessageVM.attachments), chatHeight]);

  useEffect(() => {
    console.log("Messages: ", messages);
  }, [messages]);

  return (
    <>
      <div
        className="chat-container"
        id="chat"
        style={{
          height: chatHeight,
          overflow: "scroll",
          marginTop: 20,
        }}
      >
        <ListGroup>
          {messages?.map((message, index) => (
            <ListGroupItem
              key={index}
              className={
                message.userEmail === user.user.email
                  ? "user-card"
                  : "other-card"
              }
            >
              <MessageCard message={message} textColor={message.userEmail === user.user.email ? "white" : "black"} />
            </ListGroupItem>
          ))}
        </ListGroup>
      </div>
      {canSendMessage && (
        <div
          id="input-message"
          style={{
            position: "fixed",
            bottom: 0,
            width: "100%",
            background: "white",
            paddingRight: 20,
            paddingTop: 10,
          }}
        >
          <Form onSubmit={onSubmit}>
            <div className="attachments d-flex flex-wrap">
              {Object.keys(addMessageVM.attachments).map(
                (attachmentId, index) => (
                  <Card
                    key={index}
                    className="attachment-card p-2"
                    style={{ display: "flex", flexDirection: "row" }}
                  >
                    <Card.Img
                      variant="top"
                      src={URL.createObjectURL(
                        addMessageVM.attachments[attachmentId]
                      )}
                      style={{ width: "20px", height: "20px", marginRight: 15 }}
                    />
                    <Card.Text
                      className="m-0 texts-secondary"
                      style={{ marginLeft: 10 }}
                    >
                      {addMessageVM.attachments[attachmentId].name}
                    </Card.Text>
                    <ClickableOpacity
                      onClick={(e) => {
                        e.preventDefault();
                        addMessageVM.removeAttachment(attachmentId);
                      }}
                    >
                      <X size={25} />
                    </ClickableOpacity>
                  </Card>
                )
              )}
            </div>
            <div className="input-with-icon">
              <Form.Group className="mb-3" controlId="formDescription">
                <Form.Control
                  as="textarea"
                  rows={3}
                  placeholder="Type your message here"
                  value={addMessageVM.description}
                  required
                  size="lg"
                  onChange={(e) => addMessageVM.setDescription(e.target.value)}
                  style={{ paddingRight: "70px" }}
                />
                <ClickableOpacity
                  className="attachment-icon2"
                  style={{ cursor: "pointer" }}
                  onClick={handleAttachmentClick}
                >
                  <Paperclip size={35} />
                </ClickableOpacity>
                <ClickableOpacity
                  className="send-icon"
                  style={{ cursor: "pointer" }}
                  onClick={onSubmit}
                >
                  <SendFill size={30} color="#007bff" />
                </ClickableOpacity>
                <Form.Control
                  style={{ display: "none" }}
                  ref={fileInputRef}
                  onChange={onChangeAttachment}
                  type="file"
                />
              </Form.Group>
            </div>
          </Form>
        </div>
      )}
    </>
  );
};
