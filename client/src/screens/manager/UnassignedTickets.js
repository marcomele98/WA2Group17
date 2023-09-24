import { useUnassignedTicketsVM } from "../../presenters/manager/UnasignTicketVM";
import { errorToast } from "../../utils/Error";
import { TicketCard } from "../../components/TicketCard";
import { useWorkersVM } from "../../presenters/WorkersVM";
import React, { useEffect, useState } from 'react';
import { Modal, Button, DropdownButton, Dropdown } from 'react-bootstrap';
import { successToast } from "../../utils/Error";


export const UnassignedTickets = () => {
    const workersVM = useWorkersVM(errorToast);
    const openTicketsVM = useUnassignedTicketsVM(errorToast);
    const [showModal, setShowModal] = useState(false);
    const [selectedExpert, setSelectedExpert] = useState(null);
    const [selectedPriority, setSelectedPriority] = useState(null);
    const [selectedTicket, setSelectedTicket] = useState(null);

    const handleModal = (id) => {
        setSelectedTicket(id);
        setShowModal(true);
    }

    const handleShowModal = () => setShowModal(true);

    const handleCloseModal = () => {
        setShowModal(false);
        setSelectedExpert(null);
        setSelectedPriority(null)
        setSelectedTicket(null);
    }

    const handleExpertSelect = (option) => {
        setSelectedExpert(option);
    }

    const handlePrioritySelect = (option) => {
        setSelectedPriority(option);
    }

    const handleAssignTicket = () => {
        const assignTicketDTO = {
            expertEmail: selectedExpert,
            priority: selectedPriority
        }
        openTicketsVM.assignTicket(assignTicketDTO, selectedTicket);
        successToast("Ticket assigned successfully")
        handleCloseModal();
    }

    useEffect(() => {
        console.log(selectedTicket);
        console.log(workersVM.workers)
    }, [selectedTicket]);

    return (
        <>
            <Modal show={showModal} onHide={handleCloseModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Assign Ticket to Expert</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <DropdownButton
                        id="dropdown-basic-button"
                        title={selectedExpert || 'Select an Expert'}
                    >
                        {workersVM.workers.filter(
                            (worker) => (
                                    worker.role === "EXPERT" 
                                    && worker.name != "admin" 
                                    && worker.skills.includes(openTicketsVM.openTickets.find(t => t.id === selectedTicket)?.problemType)
                                )
                        ).map((option) => (
                            <Dropdown.Item
                                key={option.email}
                                onClick={() => handleExpertSelect(option.email)}
                            >
                                {option.email}
                            </Dropdown.Item>
                        ))}
                    </DropdownButton>
                    <div style={{ paddingTop: "20px" }}></div>
                    <DropdownButton
                        id="dropdown-basic-button"
                        title={selectedPriority || 'Select Priority'}
                    >
                        <Dropdown.Item onClick={() => handlePrioritySelect('LOW')}>
                            LOW
                        </Dropdown.Item>
                        <Dropdown.Item onClick={() => handlePrioritySelect('MEDIUM')}>
                            MEDIUM
                        </Dropdown.Item>
                        <Dropdown.Item onClick={() => handlePrioritySelect('HIGH')}>
                            HIGH
                        </Dropdown.Item>
                    </DropdownButton>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseModal}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleAssignTicket} disabled={selectedPriority === null || selectedExpert == null}>
                        Assign
                    </Button>
                </Modal.Footer>
            </Modal>


            <div>
                <h1>Unassigned Tickets</h1>
            </div>
            {openTicketsVM.openTickets.sort((a, b) => b.timestamp.localeCompare(a.timestamp))
                .map(ticket => (
                    <TicketCard ticket={ticket} withProductDetais={true} openModal={handleShowModal} setSelectedTicket={setSelectedTicket} ActionElement={<Button variant="primary" onClick={() => { handleModal(ticket.id) }}>Assign Ticket</Button>} />
                ))}
            {openTicketsVM.openTickets.length === 0 && (
                <h2 className="text-2xl">No tickets</h2>
            )}

        </>
    );
};