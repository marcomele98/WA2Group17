import React from "react";
import { useCreateWorkerVM } from "../../presenters/UserVMs";
import { Form, Button, Row, Col } from "react-bootstrap";
import {
  CustomControl,
  PasswordInput,
  RoleSelector,
  SkillsCheckBoxes,
} from "../../components/UserForm";
import { useNavigate } from "react-router-dom";
import {errorToaster, successToast} from "../../utils/Error";

export const CreateWorker = () => {
  const createWorkerVM = useCreateWorkerVM();
  const [confirmPassword, setConfirmPassword] = React.useState("");
  const [validated, setValidated] = React.useState(false);
  const navigate = useNavigate();

  const onSubmit = errorToaster(async (event) => {
    event.preventDefault();
    setValidated(true);
    if (createWorkerVM.password !== confirmPassword)
      throw "Passwords do not match";
    await createWorkerVM.save();
    successToast("User created successfully");
    navigate("/manager/users");
  });

  return (
    <Form onSubmit={onSubmit}>
      <h2 className="mb-3">Edit Profile</h2>
      <Row>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={createWorkerVM.email}
            setValue={createWorkerVM.setEmail}
            placeholder={"Enter email"}
            isInvalid={
              !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(
                createWorkerVM.email
              ) && validated
            }
          />
        </Col>

        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <RoleSelector
            role={createWorkerVM.role}
            setRole={createWorkerVM.setRole}
          />
        </Col>
      </Row>
      <Row>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={createWorkerVM.name}
            setValue={createWorkerVM.setName}
            placeholder={"Enter name"}
          />
        </Col>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={createWorkerVM.surname}
            setValue={createWorkerVM.setSurname}
            placeholder={"Enter surname"}
          />
        </Col>
      </Row>

      <PasswordInput
        password={createWorkerVM.password}
        setPassword={createWorkerVM.setPassword}
        confirmPassword={confirmPassword}
        setConfirmPassword={setConfirmPassword}
        validated={validated}
      />

      {createWorkerVM.role == "EXPERT" && (
        <SkillsCheckBoxes
          skills={createWorkerVM.skills}
          addSkill={createWorkerVM.addSkill}
          removeSkill={createWorkerVM.removeSkill}
          validated={validated}
        />
      )}

      <Button variant="primary" type="submit" style={{ float: "right" }}>
        Create
      </Button>
    </Form>
  );
};
