import {useNavigate, useParams} from "react-router-dom";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import {ProblemTypeSelector} from "../../components/ProblemTypeSelector";
import {useCreateTicketVM} from "../../presenters/customer/CreateTicketVM";
import {Paperclip, X} from "react-bootstrap-icons";
import "../../style/App.css"
import {ClickableOpacity} from "../../components/ClickableOpacity";
import React, { useRef} from "react";
import { errorToaster, successToast} from "../../utils/Error";

export const CreateTicket = () => {
    const {warrantyId} = useParams();
    const createTicketVM = useCreateTicketVM(warrantyId);
    const fileInputRef = useRef(null);
    const navigate = useNavigate();
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
            createTicketVM.addAttachment(file);
        }
    }

    const onSubmit = errorToaster(async (event) => {
        event.preventDefault();
        await createTicketVM.save();
        successToast("Ticket created successfully")
        navigate("/client/warranties");
    });

    return (<div>
        <h1> Create Ticket </h1>
        <Form onSubmit={onSubmit}>
            <Row className="pt-4">
                <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
                    <Form.Group className="mb-3" controlId="formTitle">
                        <Form.Control
                            size="lg"
                            value={createTicketVM.title}
                            required
                            onChange={(e) => createTicketVM.setTitle(e.target.value)} type="text"
                            placeholder="Enter title"/>

                    </Form.Group>
                </Col>
                <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
                    <ProblemTypeSelector problemType={createTicketVM.problemType}
                                         setProblemType={createTicketVM.setProblemType}/>
                </Col>
            </Row>
            <div className="attachments d-flex flex-wrap">
                {Object.keys(createTicketVM.attachments).map((attachmentId, index) => (
                    <Card key={index} className="attachment-card p-2" style={{display: "flex", flexDirection: "row"}}>
                        <Card.Img variant="top" src={URL.createObjectURL(createTicketVM.attachments[attachmentId])}
                                  style={{width: "20px", height: "20px", marginRight: 15}}/>
                        <Card.Text className="m-0 texts-secondary"
                                   style={{marginLeft: 10}}>{createTicketVM.attachments[attachmentId].name}</Card.Text>
                        <ClickableOpacity onClick={(e) => {
                            e.preventDefault();
                            createTicketVM.removeAttachment(attachmentId)
                        }
                        }>
                            <X size={25}/>
                        </ClickableOpacity>
                    </Card>
                ))}
            </div>
            <div className="input-with-icon">
                <Form.Group className="mb-3" controlId="formDescription">
                    <Form.Control as="textarea" rows={3} placeholder="Describe the problem"
                                  required
                                  size="lg"
                                  onChange={(e) => createTicketVM.setDescription(e.target.value)}
                                  style={{paddingRight: '40px'}}
                    />
                    <ClickableOpacity className="attachment-icon" style={{marginLeft: '5px', cursor: 'pointer'}}
                                      onClick={handleAttachmentClick}>
                        <Paperclip size={35}/>
                    </ClickableOpacity>
                    <Form.Control
                        style={{display: 'none'}}
                        ref={fileInputRef}
                        onChange={onChangeAttachment}
                        type="file"

                    />
                </Form.Group>
            </div>
            <Button type="submit" size="lg" style={{float: "right"}}>
                Create Ticket
            </Button>

        </Form>
    </div>);
}