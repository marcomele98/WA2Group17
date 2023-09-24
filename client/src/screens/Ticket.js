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
import { Messages } from '../components/Messages';
import { TicketDetails } from '../components/TicketDetails';

export const Ticket = () => {
    const { id } = useParams();
    const ticketVM = useTicketVM(errorToast, id);

    return (
        <div style={{ paddingBottom: 20 }}>
            <h1 >Ticket Details</h1>

                <TicketDetails ticket={ticketVM.ticket}></TicketDetails>
                <Messages id={id} messages={ticketVM.ticket?.messages} refreshMessages={ticketVM.getTicket}></Messages>
        </div>
    );
}

