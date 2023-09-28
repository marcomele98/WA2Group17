import React from 'react'
import { Row, Col } from 'react-bootstrap';
import '.././components/Chat.css'
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
                            <strong>Customer:</strong> {ticket?.warranty?.customer?.name + " " + ticket?.warranty?.customer?.surname}
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

