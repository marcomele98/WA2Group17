import React, { useEffect, useState } from "react";
import { Form, Button, Row } from "react-bootstrap";
import "react-toastify/dist/ReactToastify.css";
import { toast } from "react-toastify";
import { useUser } from "../presenters/User";
import "../style/Login.css";
import { useNavigate } from "react-router-dom";

export function Login({ redirectRoute, setRedirectRoute }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { logIn } = useUser();
  const { user } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    console.log("redirectRoute: ", redirectRoute);
    if (!user)
      return
    if (redirectRoute) {
      const route = redirectRoute;
      setRedirectRoute(false);
      navigate(route);
    } else if (user) {
      navigate("/"+user.role.toLowerCase());
    }
  }, [user]);

  const handleSubmit = async (event) => {
    event.preventDefault(); //evita di ricaricare la pagina
    //vedo se validare (forse per login meglio di no)
    try {
      await logIn(username, password);
    } catch (err) {
      toast.error(err.message);
    }
  };

  return (
    <div className="Login below-nav main-content text-center">
      <br />
      <Row className="justify-content-center text-center">
        <h2> LOGIN </h2>
      </Row>
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="username">
          <Form.Label>Email</Form.Label>
          <Form.Control
            type="text"
            value={username}
            onChange={(ev) => setUsername(ev.target.value)}
          />
        </Form.Group>
        <br />
        <Form.Group controlId="password">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            value={password}
            onChange={(ev) => setPassword(ev.target.value)}
          />
        </Form.Group>
        <br />
        <Form.Group size="lg">
          <Button variant="success" size="lg" id="submitLogin" type="submit">
            Login
          </Button>
        </Form.Group>
      </Form>
    </div>
  );
}
