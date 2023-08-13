import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Button, Nav, Navbar } from "react-bootstrap";
import { useUser } from "../presenters/User";

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
      bg="success"
      variant="dark"
    >
      <Navbar.Brand className="navbar-brand">WA2</Navbar.Brand>
      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse id="responsive-navbar-nav">
        <Nav variant="success" className="me-auto">
          {
            {
              MANAGER: (
                <>
                  <Nav.Link
                    style={navLinkStyle}
                    active={location?.pathname === "/manager/unassigned-tickets"}
                    onClick={() => navigate("/manager/unassigned-tickets")}
                  >
                    Products
                  </Nav.Link>
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
              CUSTOMER: <></>,
              default: <></>,
            }[user?.role]
          }
        </Nav>
        <Nav variant="success">
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
