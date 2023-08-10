import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Button, Nav, Navbar } from "react-bootstrap";
import { useUser } from "../presenters/User";

function NavigationBar() {
  const navigate = useNavigate();
  const location = useLocation();
  const user = useUser();

  return (
    <Navbar
      className="fixed-top p-2"
      style={{height: 55}} //TODO: tolgo inline style
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
                    active={location?.pathname === "/products"}
                    onClick={() => navigate("/products")}
                  >
                    Products
                  </Nav.Link>
                  <Nav.Link
                    style={navLinkStyle}
                    active={location?.pathname === "/profiles"}
                    onClick={() => navigate("/profiles")}
                  >
                    Profiles
                  </Nav.Link>
                </>
              ),
              CASHIER: <></>,
              CUSTOMER: <></>,
              default: <></>,
            }[user?.role]
          }
        </Nav>
        <Nav variant="success" >
          {user.username ? (
            <Nav.Link active style={navLinkStyle} onClick={user.logOut}>
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
