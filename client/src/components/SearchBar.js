import React, { useState } from "react";
import { InputGroup, FormControl, Button, Col, Form } from "react-bootstrap";

//Voelssi usare questo componente in un form capisco se passare value e set value o no
export const SearchBar = ({ placeholder, buttonText, onSearch, className, size }) => {
  const [value, setValue] = useState("");
  return (
    <Form
      className={className}
      onSubmit={(event) => {
        event.preventDefault();
        onSearch(value);
      }}
    >
      <Col style={{ width: "100%" }}>
        <InputGroup>
          <FormControl
            size={size}
            type="text"
            placeholder={placeholder ? placeholder : "Search"}
            onChange={(ev) => setValue(ev.target.value)}
            value={value}
            className="mr-sm-2"
          />
          <Button variant="outline-success" type="submit">
            {buttonText ? buttonText : "Search"}
          </Button>
        </InputGroup>
      </Col>
    </Form>
  );
};
