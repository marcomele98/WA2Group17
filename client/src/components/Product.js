import React from "react";
import { Card } from "react-bootstrap";

function Product({ product, ActionElement }) {
  return (
    <div>
      <Card className="card">
        <Card.Header className="d-flex justify-content-between align-items-center">
          {product.name}
          {ActionElement}
        </Card.Header>
        <Card.Body>
          <Card.Text>{"EAN: " + product.ean}</Card.Text>
          <Card.Text>{"Brand: " + product.brand}</Card.Text>
        </Card.Body>
      </Card>
    </div>
  );
}

export { Product };
