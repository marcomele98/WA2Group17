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

export const TicketDetails = ({ticket}) => {


    return (
            <Row>
                <Col>
                    <div >
                        <div >
                            <strong>Title:</strong> {ticket?.title}
                        </div>
                        <div>
                            <strong>Problem Type:</strong> {ticket?.problemType}
                        </div>
                        <div>
                            <strong>Status:</strong> {ticket?.status}
                        </div>
                    </div>
                </Col>
                <Col>
                    <div>
                        <div>
                            <strong>Timestamp:</strong> {ticket?.timestamp}
                        </div>
                        <div>
                            <strong>Priority:</strong> {ticket?.priority || 'Not specified'}
                        </div>
                        <div>
                            <strong>Customer:</strong> {ticket?.warranty.customer.name + " " + ticket?.warranty.customer.surname}
                        </div>
                    </div>
                </Col>
                {ticket?.expert && (
                    <Col>
                        <div >
                            <div>
                                <strong>Expert Name:</strong> {ticket?.expert.name + " " + ticket?.expert.surname}
                            </div>
                            <div >
                                <strong>Email:</strong> {ticket?.expert.email}
                            </div>
                            {ticket?.warranty && (
                                <div>
                                    <strong>Product Name:</strong> {ticket?.warranty.product.name}
                                </div>
                            )}
                        </div>
                    </Col>)}
            </Row>
    );
}

