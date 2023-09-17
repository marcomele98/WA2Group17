import React, { useEffect } from 'react'
import { errorToast, errorToaster } from '../utils/Error';
import { useTicketVM } from "../presenters/TicketVM"
import { useParams } from "react-router-dom";
import { Row, Col, Container } from 'react-bootstrap';
import { MessageCard } from '../components/MessageCard';
import { useUser } from "../presenters/LoggedUser";
import { Card, ListGroup, ListGroupItem } from 'react-bootstrap';
import '.././components/Chat.css'
import { Form, Button } from 'react-bootstrap';
import { ClickableOpacity } from '../components/ClickableOpacity';
import { Paperclip, X } from 'react-bootstrap-icons';
import { successToast } from '../utils/Error';
import { useNavigate } from 'react-router-dom';
import { useAddMessageVM } from '../presenters/AddMessageVM';
import { useState } from 'react';

export const AddMessageForm = ({ id, setDirty}) => {
    const ticketVM = useTicketVM(errorToast);
    const addMessageVM = useAddMessageVM();

    const fileInputRef = React.useRef(null);

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
    }

    const onSubmit = errorToaster(async (event) => {
        event.preventDefault();
        await addMessageVM.save(id);
        addMessageVM.reset();
        setDirty(true)
        successToast("Message sended successfully")
    });


    return (

        <Form onSubmit={onSubmit}>
            <div className="attachments d-flex flex-wrap">
                {Object.keys(addMessageVM.attachments).map((attachmentId, index) => (
                    <Card key={index} className="attachment-card p-2" style={{ display: "flex", flexDirection: "row" }}>
                        <Card.Img variant="top" src={URL.createObjectURL(addMessageVM.attachments[attachmentId])}
                            style={{ width: "20px", height: "20px", marginRight: 15 }} />
                        <Card.Text className="m-0 texts-secondary"
                            style={{ marginLeft: 10 }}>{addMessageVM.attachments[attachmentId].name}</Card.Text>
                        <ClickableOpacity onClick={(e) => {
                            e.preventDefault();
                            addMessageVM.removeAttachment(attachmentId)
                        }
                        }>
                            <X size={25} />
                        </ClickableOpacity>
                    </Card>
                ))}
            </div>
            <div className="input-with-icon">
                <Form.Group className="mb-3" controlId="formDescription">
                    <Form.Control as="textarea" rows={3} placeholder="Type your message here"
                        value={addMessageVM.description}
                        required
                        size="lg"
                        onChange={(e) => addMessageVM.setDescription(e.target.value)}
                        style={{ paddingRight: '40px' }}
                    />
                    <ClickableOpacity className="attachment-icon" style={{ marginLeft: '5px', cursor: 'pointer' }}
                        onClick={handleAttachmentClick}>
                        <Paperclip size={35} />
                    </ClickableOpacity>
                    <Form.Control
                        style={{ display: 'none' }}
                        ref={fileInputRef}
                        onChange={onChangeAttachment}
                        type="file"

                    />
                </Form.Group>
            </div>
            <Button type="submit" size="lg" style={{ float: "right" }}>
                Send Message
            </Button>
        </Form>
    );
}
