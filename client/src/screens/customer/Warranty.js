import { useNavigate, useParams } from "react-router-dom";
import React, { useEffect } from "react";
import { TicketCard } from "../../components/TicketCard";
import { useWarrantyVM } from "../../presenters/manager/WarrantyVM";
import { Col, Row } from "react-bootstrap";
import { PlusSquareFill } from "react-bootstrap-icons";
import { ClickableOpacity } from "../../components/ClickableOpacity";
import { useUser } from "../../presenters/LoggedUser";

export const Warranty = () => {
    const { id } = useParams();
    const warrantyVM = useWarrantyVM(id);
    const areTicketsEmpty = warrantyVM.tickets.length === 0;
    const navigate = useNavigate();

    const handleClick = (id) => {
            navigate("/client/tickets/" + id)
    }

    return (
        <>
            <Row className="align-items-end">
                <Col width={{ width: "100%" }}>
                    <h2>
                        {`${warrantyVM.typology} warranty #${warrantyVM.id}`}
                    </h2>
                </Col>
                <Col style={{ fontSize: 30 }} className={warrantyVM.valid ? "text-success" : "text-danger"} xs="auto"
                    sm="auto" md="auto"
                    lg="auto" xl="auto" xxl="auto">
                    {warrantyVM.valid ? "VALID" : "INVALID"}
                </Col>
            </Row>
            <Row style={{ height: 20 }} />
            <Row>
                <Col style={{ fontSize: 25 }}>
                    {`Your warranty on product ${warrantyVM.product.name} by ${warrantyVM.product.brand} is valid from ${warrantyVM.startDate} to ${warrantyVM.endDate}`}
                </Col>
            </Row>
            <Row style={{ height: 20 }} />
            <Row className="align-items-end">
                <Col width={{ width: "100%" }}>
                    <h3>
                        {areTicketsEmpty ? `No tickets yet` : `Tickets`}
                    </h3>
                </Col>
                {
                    warrantyVM.valid &&
                    <Col xs="auto" sm="auto" md="auto" lg="auto" xl="auto" xxl="auto">
                        <ClickableOpacity>
                            <PlusSquareFill
                                size={40}
                                className="text-primary"
                                onClick={() => navigate(`/client/create-ticket/${warrantyVM.id}`)}
                            />
                        </ClickableOpacity>
                    </Col>
                }
            </Row>
            <Row style={{ height: 20 }} />
            {warrantyVM.tickets.map(ticket =><TicketCard ticket={ticket} handleClick={() => handleClick(ticket.id)}/>)}
        </>)
}