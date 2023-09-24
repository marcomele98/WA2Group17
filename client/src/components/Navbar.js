import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Button, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { useUser } from "../presenters/LoggedUser";

function NavigationBar() {
    const navigate = useNavigate();
    const location = useLocation();
    const { user, logOut } = useUser();

    const logOutHandler = () => {
        logOut();
        navigate("/login");
    };

    return (
        <Navbar
            className="fixed-top p-2"
            style={{ height: 55 }} //TODO: tolgo inline style
            id="my-navbar"
            collapseOnSelect
            expand="lg"
            bg="primary"
            variant="dark"
        >
            <Navbar.Brand className="navbar-brand">WA2</Navbar.Brand>
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
            <Navbar.Collapse id="responsive-navbar-nav">
                <Nav variant="primary" className="me-auto">
                    {
                        {
                            MANAGER: (
                                <>

                                    <NavDropdown title="Tickets" style={navLinkStyle}
                                        active={location?.pathname === "/manager/assigned-tickets" || location?.pathname === "/manager/unassigned-tickets"}
                                    >
                                        <NavDropdown.Item className='dropdown-item:hover' style={{ backgroundColor: location?.pathname === "/manager/unassigned-tickets" ? "#0c6dfd" : false, color: location?.pathname === "/manager/unassigned-tickets" ? "white" : false, }} active={location?.pathname === "/managerunassigned-tickets"} onClick={() => navigate('manager/unassigned-tickets')}>Unassigned Tickets</NavDropdown.Item>
                                        <NavDropdown.Divider />
                                        <NavDropdown.Item className='dropdown-item:hover' style={{ backgroundColor: location?.pathname === "/manager/assigned-tickets" ? "#0c6dfd" : false, color: location?.pathname === "/manager/assigned-tickets" ? "white" : false, }} active={location?.pathname === "/manager/assigned-tickets"} onClick={() => navigate('manager/assigned-tickets')}>Assigned Tickets</NavDropdown.Item>
                                    </NavDropdown>


                                    <Nav.Link
                                        style={navLinkStyle}
                                        active={location?.pathname === "/manager/users"}
                                        onClick={() => navigate("/manager/users")}
                                    >
                                        Users
                                    </Nav.Link>
                                </>
                            ),
                            CASHIER: <></>,
                            CLIENT: (
                                <>
                                    <Nav.Link
                                        style={navLinkStyle}
                                        active={location?.pathname === "/client/warranties"}
                                        onClick={() => navigate("/client/warranties")}
                                    >
                                        Warranties
                                    </Nav.Link>
                                    <Nav.Link
                                        style={navLinkStyle}
                                        active={location?.pathname === "/client/tickets"}
                                        onClick={() => navigate("/client/tickets")}
                                    >
                                        Tickets
                                    </Nav.Link>
                                </>
                            ),
                            default: <></>,
                        }[user?.role]
                    }
                </Nav>
                <Nav variant="primary">
                    {user ? (
                        <Nav.Link
                            active
                            style={navLinkStyle}
                            onClick={logOutHandler}
                        >
                            {/*TODO: quando il bottone è selezionato, il colore del testo è nero, lo rendo del colore della navbar*/}
                            <Button variant="outline-light">Log out</Button>
                        </Nav.Link>
                    ) : (
                        false
                    )}
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );
}

const navLinkStyle = {
    fontSize: 18,
};

export { NavigationBar };
