import React, { useEffect } from 'react'
import { errorToast } from '../../utils/Error';
import { useTicketVM } from "../../presenters/TicketVM"
import { useParams } from "react-router-dom";
import { Row, Col } from 'react-bootstrap';
import { MessageCard } from '../../components/MessageCard';
import { useUser } from "../../presenters/LoggedUser";
import { ListGroup, ListGroupItem } from 'react-bootstrap';
import '../../components/Chat.css'
import { Button } from 'react-bootstrap';
import { useState } from 'react';
import { Messages } from '../../components/Messages';
import { TicketDetails } from '../../components/TicketDetails';

export const CustomerTicket = ({}) => {
    const { id } = useParams();
    const ticketVM = useTicketVM(errorToast, id);

    useEffect(() => {
        console.log(ticketVM.ticket?.status)
    }, [ticketVM.ticket])

    return (
        <div style={{ paddingBottom: 20 }}>
            <Row>
                <Col>
                    <h1>Ticket #{id}</h1>
                </Col>
                {ticketVM.ticket && <>
                {
                    <Col>
                    {
                    (ticketVM.ticket?.status === "OPEN" || ticketVM.ticket?.status === "IN_PROGRESS" ) ?
                    (
                                <Button style={{float: 'right'}} variant="danger" onClick={() => ticketVM.closeTicket(id)}>Close</Button>

                    ) : (
                        
                            <Button variant="warning" style={{float: 'right'}}  onClick={() => ticketVM.reopenTicket(id)}>Reopen</Button>
                    
                    )
}
                    </Col>
                }
                </>
            }
            </Row>
            {ticketVM.ticket &&
            <>
                <TicketDetails ticket={ticketVM.ticket}></TicketDetails>
                <Messages id={id} messages={ticketVM.ticket?.messages} refreshMessages={ticketVM.getTicket} canSendMessage={(ticketVM.ticket.status!="CLOSED" && ticketVM.ticket.status!="RESOLVED")}></Messages>
            </>
            }
        </div>
    );
}

