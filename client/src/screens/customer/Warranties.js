import { useWarrantiesVM } from "../../presenters/customer/WarrantiesVM";
import { WarrantyCard } from "../../components/WarrantyCard";
import { Col, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

export const Warranties = () => {
  const warrantiesVM = useWarrantiesVM();
  const navigate = useNavigate();
  const [searchValue, setSearchValue] = useState("");

  return (
    <>
      <h1>Warranties</h1>
      <Col style={{ width: "100%", marginBottom: 20 }}>
        <Form.Control
          size="lg"
          type="text"
          placeholder="Search"
          onChange={(ev) => setSearchValue(ev.target.value.toLowerCase())}
        />
      </Col>
      {warrantiesVM.warranties
        .filter(
          (warranty) =>
            warranty.product.name.toLowerCase().startsWith(searchValue) ||
            warranty.product.brand.toLowerCase().startsWith(searchValue) ||
            warranty.product.ean.toLowerCase().startsWith(searchValue)
        )
        .map((warranty) => (
          <WarrantyCard
            warranty={warranty}
            onAction={() => navigate(warranty.id.toString())}
          />
        ))}
      {warrantiesVM.warranties.length === 0 && (
        <h2 className="text-2xl">No warranties</h2>
      )}
    </>
  );
};
