import React, { useEffect } from 'react'
import { errorToast, errorToaster } from '../utils/Error';
import { MessageCard } from './MessageCard';
import { Card, ListGroup, ListGroupItem } from 'react-bootstrap';
import '.././components/Chat.css'
import { Form, Button } from 'react-bootstrap';
import { ClickableOpacity } from './ClickableOpacity';
import { Paperclip, X } from 'react-bootstrap-icons';
import { successToast } from '../utils/Error';
import { useState } from 'react';
import { useAddMessageVM } from '../presenters/AddMessageVM';
import { useUser } from '../presenters/LoggedUser';

export const Messages = ({ id, messages, refreshMessages }) => {

    const [dirty, setDirty] = useState(true);
    const [showMessages, setShowMessages] = useState(false);
    const fileInputRef = React.useRef(null);
    const user = useUser();
    const addMessageVM = useAddMessageVM();

    useEffect(() => {
        if (dirty) {
            refreshMessages();
            setDirty(false);
        }
    }, [dirty])
    
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
        <>
            <Button style={{ marginTop: 20, marginBottom: 20 }} variant="primary" onClick={() => setShowMessages(!showMessages)}>{showMessages ? "Hide Messages" : "Show Messages"}</Button>
            {
                showMessages ? (
                    <>
                        <Button style={{ marginLeft: 20, marginTop: 20, marginBottom: 20 }} variant="primary" onClick={() => setDirty(true)}>Refresh Messages</Button>

                        <div className="chat-container">
                            <ListGroup>
                                {messages?.map((message, index) => (
                                    <ListGroupItem key={index} className={message.userEmail === user.user.email ? 'user-card' : 'other-card'}>
                                        <MessageCard message={message} />
                                    </ListGroupItem>
                                ))}
                            </ListGroup>
                        </div>
                        <div style={{ paddingTop: "20px" }}>
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
                        </div>
                    </>
                ) : null
            }
        </>
    )
}
