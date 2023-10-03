import React from "react";
import { Form, Row, Col, InputGroup } from "react-bootstrap";
import { Eye, EyeSlash } from "react-bootstrap-icons";
import { ClickableOpacity } from "./ClickableOpacity";

export const CustomControl = ({
  value,
  setValue,
  placeholder,
  disabled,
  isInvalid,
}) => {
  return (
    <Form.Group className="mb-3" controlId="NameInput">
      <Form.Control
        type="text"
        size="lg"
        value={value}
        onChange={(ev) => setValue(ev.target.value)}
        disabled={disabled}
        placeholder={placeholder}
        isInvalid={isInvalid}
        required
      />
    </Form.Group>
  );
};

export const RoleSelector = ({ role, setRole }) => {
  return (
    <Form.Group className="mb-3" controlId="RoleSelector" required>
      <Form.Select
        size="lg"
        value={role}
        onChange={(e) => setRole(e.target.value)}
        style={role ? {} : { color: "GrayText" }}
      >
        <option style={{ color: "GrayText" }} value="">
          Select the role
        </option>
        <option style={{ color: "black" }} value="EXPERT">
          EXPERT
        </option>
        <option style={{ color: "black" }} value="CASHIER">
          CASHIER
        </option>
      </Form.Select>
    </Form.Group>
  );
};

export const PasswordInput = ({
  password,
  setPassword,
  confirmPassword,
  setConfirmPassword,
  validated,
}) => {
  const [showPassword1, setShowPassword1] = React.useState(false);
  const [showPassword2, setShowPassword2] = React.useState(false);
  return (
    <Row>
      <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
        <Form.Group className="mb-3" controlId="PassWordInput">
          <InputGroup>
            <Form.Control
              size="lg"
              value={password}
              onChange={(ev) => setPassword(ev.target.value)}
              placeholder="Enter Password"
              isInvalid={
                !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=]).*$/.test(
                  password
                ) && validated
              }
              required
              style={{ borderRightColor: "transparent" }}
              type={showPassword2 ? "text" : "password"}
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
                  setShowPassword2(!showPassword2);
                }}
              >
                {showPassword2 ? <Eye /> : <EyeSlash />}
              </ClickableOpacity>
            </InputGroup.Text>
          </InputGroup>
          <Form.Control.Feedback type="invalid">
            Password must contain at least 1 lowercase letter, 1 uppercase
            letter, 1 number and 1 special character
          </Form.Control.Feedback>
        </Form.Group>
      </Col>
      <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
        <Form.Group className="mb-3" controlId="PassWordCheckInput">
          <InputGroup>
            <Form.Control
              size="lg"
              value={confirmPassword}
              onChange={(ev) => setConfirmPassword(ev.target.value)}
              placeholder="Confirm Password"
              isInvalid={
                /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=]).*$/.test(
                  password
                ) &&
                password !== confirmPassword &&
                validated
              }
              required
              style={{ borderRightColor: "transparent" }}
              type={showPassword1 ? "text" : "password"}
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
                  setShowPassword1(!showPassword1);
                }}
              >
                {showPassword1 ? <Eye /> : <EyeSlash />}
              </ClickableOpacity>
            </InputGroup.Text>
          </InputGroup>
          <Form.Control.Feedback type="invalid">
            Passwords do not match
          </Form.Control.Feedback>
        </Form.Group>
      </Col>
    </Row>
  );
};

export const SkillsCheckBoxes = ({
  skills,
  addSkill,
  removeSkill,
  validated,
}) => {
  return (
    <Form.Group className="mb-3" controlId="SkillsCheckBoxes" required>
      <Form.Label style={{ fontSize: 25 }}>Expert Skills: </Form.Label>
      {["SOFTWARE", "HARDWARE"].map((skill) => (
        <Form.Check
          type="checkbox"
          label={skill}
          isInvalid={skills.length === 0 && validated}
          checked={skills.includes(skill)}
          onChange={(e) => {
            if (e.target.checked) {
              addSkill(skill);
            } else {
              removeSkill(skill);
            }
          }}
        />
      ))}
      <Form.Control.Feedback type="invalid">
        Select at least one skill
      </Form.Control.Feedback>
    </Form.Group>
  );
};
