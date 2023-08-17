import React from "react";
import { useParams } from "react-router-dom";
import { useEditWorkerVM } from "../../presenters/UserVMs";
import {
  CustomControl,
  PasswordInput,
  RoleSelector,
  SkillsCheckBoxes,
} from "../../components/UserForm";
import { Form, Button, Row, Col } from "react-bootstrap";
import { errorToaster } from "../../utils/Error";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

export const EditWorker = () => {
  const { email } = useParams();
  const editWorkerVM = useEditWorkerVM(email);
  const [confirmPassword, setConfirmPassword] = React.useState("");
  const [editPassword, setEditPassword] = React.useState(false);
  const [validated, setValidated] = React.useState(false);
  const navigate = useNavigate();

  const onSubmit = errorToaster(async (event) => {
    event.preventDefault();
    setValidated(true);
    if (editPassword && editWorkerVM.password !== confirmPassword)
      throw "Passwords do not match";
    if (
      editPassword &&
      !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=]).*$/.test(
        editWorkerVM.password
      )
    )
      throw "Password must contain at least one lowercase letter, one uppercase letter, one number and one special character";
    await editWorkerVM.save();
    toast.success("User edited successfully", { position: "top-center" });
    navigate("/manager/users");
  });

  return (
    <Form onSubmit={onSubmit}>
      <h2 className="mb-3">Edit Profile</h2>
      <Row>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={editWorkerVM.email}
            placeholder={"Enter email"}
            disabled={true}
          />
        </Col>

        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <RoleSelector
            role={editWorkerVM.role}
            setRole={editWorkerVM.setRole}
          />
        </Col>
      </Row>
      <Row>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={editWorkerVM.name}
            placeholder={"Enter name"}
            disabled={true}
          />
        </Col>
        <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
          <CustomControl
            value={editWorkerVM.surname}
            placeholder={"Enter surname"}
            disabled={true}
          />
        </Col>
      </Row>

      <Form.Group className="mb-3" controlId="PassWordCheckBoxInput" required>
        <Form.Check
          type="checkbox"
          label="Change Password"
          checked={editPassword}
          onChange={(e) => {
            setEditPassword(e.target.checked);
            if (!e.target.checked) {
              editWorkerVM.setPassword("");
              setConfirmPassword("");
            }
          }}
        />
      </Form.Group>

      {editPassword && (
        <PasswordInput
          password={editWorkerVM.password}
          setPassword={editWorkerVM.setPassword}
          confirmPassword={confirmPassword}
          setConfirmPassword={setConfirmPassword}
          validated={validated}
        />
      )}

      {editWorkerVM.role == "EXPERT" && (
        <SkillsCheckBoxes
          skills={editWorkerVM.skills}
          addSkill={editWorkerVM.addSkill}
          removeSkill={editWorkerVM.removeSkill}
          validated={validated}
        />
      )}

      <Button variant="primary" type="submit" style={{ float: "right" }}>
        Update
      </Button>
    </Form>
  );
};
