import React from "react";
import { useSignupVM } from "../presenters/UserVMs";
import { Form, Button, Row, Col } from "react-bootstrap";
import {
  CustomControl,
  PasswordInput,
  RoleSelector,
  SkillsCheckBoxes,
} from "../components/UserForm";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { errorToaster } from "../utils/Error";

export const Signup = () => {
  const signupVM = useSignupVM();
  const [confirmPassword, setConfirmPassword] = React.useState("");
  const [validated, setValidated] = React.useState(false);
  const navigate = useNavigate();

  const onSubmit = errorToaster(async (event) => {
    event.preventDefault();
    setValidated(true);
    if (signupVM.password !== confirmPassword) throw "Passwords do not match";
    await signupVM.save();
    toast.success("Account created successfully", { position: "top-center" });
    navigate("/customer");
  });

  return (
    <Form onSubmit={onSubmit}>
      <h2 className="mb-3">Signup</h2>
      <CustomControl
        value={signupVM.email}
        setValue={signupVM.setEmail}
        placeholder={"Enter email"}
        isInvalid={
          !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(
            signupVM.email
          ) && validated
        }
      />
      <Row>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={signupVM.name}
            setValue={signupVM.setName}
            placeholder={"Enter name"}
          />
        </Col>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={signupVM.surname}
            setValue={signupVM.setSurname}
            placeholder={"Enter surname"}
          />
        </Col>
      </Row>

      <PasswordInput
        password={signupVM.password}
        setPassword={signupVM.setPassword}
        confirmPassword={confirmPassword}
        setConfirmPassword={setConfirmPassword}
        validated={validated}
      />

      <Button variant="primary" type="submit" style={{ float: "right" }}>
        Signup
      </Button>
    </Form>
  );
};
