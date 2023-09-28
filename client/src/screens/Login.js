import React, { useEffect, useState } from "react";
import { Form, Button, Row, Col, InputGroup } from "react-bootstrap";
import "react-toastify/dist/ReactToastify.css";
import { toast } from "react-toastify";
import { useUser } from "../presenters/LoggedUser";
import "../style/Login.css";
import { useNavigate } from "react-router-dom";
import { errorToaster } from "../utils/Error";
import { Eye, EyeSlash } from "react-bootstrap-icons";
import { ClickableOpacity } from "../components/ClickableOpacity.js";

export function Login({ redirectRoute, setRedirectRoute }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false); //per mostrare la password
  const { logIn } = useUser();
  const { user } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      return;
    }
    if (redirectRoute) {
      const route = redirectRoute;
      setRedirectRoute(false);
      navigate(route);
    } else if (user) {
      navigate("/" + user.role.toLowerCase());
    }
  }, [user]);

  const handleSubmit = async (event) => {
    event.preventDefault(); //evita di ricaricare la pagina
    //vedo se validare (forse per login meglio di no)
    try {
      await logIn(username, password);
    } catch (err) {
      throw "Wrong username or password";
    }
  };

  return (
    <div className="Login below-nav main-content text-center">
      <br />
      <Row className="justify-content-center text-center">
        <h2> LOGIN </h2>
      </Row>
      <Form onSubmit={errorToaster(handleSubmit)}>
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
          <InputGroup>
            <Form.Control
              style={{ borderRightColor: "transparent" }}
              type={showPassword ? "text" : "password"}
              value={password}
              onChange={(ev) => setPassword(ev.target.value)}
            />
            <InputGroup.Text
              style={{
                borderLeftColor: "transparent",
                backgroundColor: "transparent",
              }}
            >
              <ClickableOpacity
                type="button"
                onClick={(e) => {
                  e.preventDefault();
                  e.stopPropagation();
                  setShowPassword(!showPassword);
                }}
              >
                {showPassword ? <Eye /> : <EyeSlash />}
              </ClickableOpacity>
            </InputGroup.Text>
          </InputGroup>
        </Form.Group>
        <br />
        <Row>
          <Col>
            <Form.Group size="lg">
              <Button
                style={{ width: 100 }}
                variant="primary"
                size="lg"
                id="submitLogin"
                type="submit"
              >
                Login
              </Button>
            </Form.Group>
          </Col>
          <Col>
            <Form.Group size="lg">
              <Button
                style={{ width: 100 }}
                variant="primary"
                size="lg"
                onClick={() => navigate("/signup")}
              >
                Signup
              </Button>
            </Form.Group>
          </Col>
        </Row>
      </Form>
    </div>
  );
}
