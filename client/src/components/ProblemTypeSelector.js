import {Form} from "react-bootstrap";
import React from "react";

export const ProblemTypeSelector = ({setProblemType, problemType}) => {
    return (
        <Form.Group controlId="problemType">
            <Form.Select
                size="lg"
                value={problemType}
                onChange={(e) => setProblemType(e.target.value)}
                style={problemType ? {} : { color: "GrayText" }}
                required
            >
                <option style={{ color: "GrayText" }} value="">
                    Select the category of the problem
                </option>
                <option style={{ color: "black" }} value="HARDWARE">
                    HARDWARE
                </option>
                <option style={{ color: "black" }} value="SOFTWARE">
                    SOFTWARE
                </option>
            </Form.Select>
        </Form.Group>
    );
}