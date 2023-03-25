import { useNavigate, useLocation } from "react-router-dom";
import { Nav, Navbar } from 'react-bootstrap';
import '../App.css';

function NavigationBar() {
  const navigate = useNavigate();
  const location = useLocation();
  return (
    <Navbar className="fixed-top p-2" id="my-navbar" collapseOnSelect expand="lg" bg="success" variant="dark">
      <Navbar.Brand className="navbar-brand">
        WA2
      </Navbar.Brand>
      <Navbar.Toggle aria-controls="responsive-navbar-nav" />
      <Navbar.Collapse id="responsive-navbar-nav" >
        <Nav variant="success" className="me-auto" >
          <Nav.Link style={navLinkStyle} active={location?.pathname === "/products"} onClick={() => navigate('/products')}>Products</Nav.Link>
          <Nav.Link style={navLinkStyle} active={location?.pathname === "/profiles"} onClick={() => navigate('/profiles')}>Profiles</Nav.Link>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
}

const navLinkStyle = {
  fontSize: 18,
}


export { NavigationBar };