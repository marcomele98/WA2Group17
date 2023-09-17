import React, { useEffect } from 'react'
import { errorToast } from '../utils/Error';
import { useTicketVM } from "../presenters/TicketVM"
import { useParams } from "react-router-dom";
import { Row, Col } from 'react-bootstrap';
import { MessageCard } from '../components/MessageCard';
import { useUser } from "../presenters/LoggedUser";
import { ListGroup, ListGroupItem } from 'react-bootstrap';
import '.././components/Chat.css'
import { Button } from 'react-bootstrap';
import { useState } from 'react';
import { AddMessageForm } from './AddMessageForm';

export const Ticket = () => {
    const ticketVM = useTicketVM(errorToast);
    const { id } = useParams();
    const user = useUser();
    const [dirty, setDirty] = useState(true);
    const [showMessages, setShowMessages] = useState(false);

    useEffect(() => {
        if (dirty) {
            ticketVM.getTicket(id);
            setDirty(false);
        }
    }, [dirty])

    return (
        <div style={{ paddingBottom: 20 }}>
            <h1 >Ticket Details</h1>

            <Row>
                <Col>
                    <div >
                        <div >
                            <strong>Title:</strong> {ticketVM.ticket?.title}
                        </div>
                        <div>
                            <strong>Problem Type:</strong> {ticketVM.ticket?.problemType}
                        </div>
                        <div>
                            <strong>Status:</strong> {ticketVM.ticket?.status}
                        </div>
                    </div>
                </Col>
                <Col>
                    <div>
                        <div>
                            <strong>Timestamp:</strong> {ticketVM.ticket?.timestamp}
                        </div>
                        <div>
                            <strong>Priority:</strong> {ticketVM.ticket?.priority || 'Not specified'}
                        </div>
                        <div>
                            <strong>Customer:</strong> {ticketVM.ticket?.warranty.customer.name + " " + ticketVM.ticket?.warranty.customer.surname}
                        </div>
                    </div>
                </Col>
                {ticketVM.ticket?.expert && (
                    <Col>
                        <div >
                            <div>
                                <strong>Expert Name:</strong> {ticketVM.ticket?.expert.name + " " + ticketVM.ticket?.expert.surname}
                            </div>
                            <div >
                                <strong>Email:</strong> {ticketVM.ticket?.expert.email}
                            </div>
                            {ticketVM.ticket?.warranty && (
                                <div>
                                    <strong>Product Name:</strong> {ticketVM.ticket?.warranty.product.name}
                                </div>
                            )}
                        </div>
                    </Col>)}
            </Row>
                <Button style={{marginTop:20, marginBottom:20}} variant="primary" onClick={() => setShowMessages(!showMessages)}>{showMessages ? "Hide Messages" : "Show Messages"}</Button>
            {showMessages ? (
                <>
                <Button style={{marginLeft:20, marginTop:20, marginBottom:20}} variant="primary" onClick={() => setDirty(true)}>Refresh Messages</Button>

                    <div className="chat-container">
                        <ListGroup>
                            {ticketVM.ticket?.messages.map((message, index) => (
                                <ListGroupItem key={index} className={message.userEmail === user.user.email ? 'user-card' : 'other-card'}>
                                    <MessageCard message={message} />
                                </ListGroupItem>
                            ))}
                        </ListGroup>
                    </div>
                    <div style={{ paddingTop: "20px" }}>
                        <AddMessageForm id={id} setDirty={setDirty}></AddMessageForm>
                    </div>
                </>
            ) : null
            }


        </div>
    );
}

