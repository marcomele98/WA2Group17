import React from "react";
import { useCashierVM } from "../presenters/CashierVM";
import { Button, Col, Form } from "react-bootstrap";
import { SearchBar } from "../components/SearchBar";
import { Row } from "react-bootstrap";
import { errorToaster } from "../utils/Error";
import { User } from "../components/User";
import { ClickableOpacity } from "../components/ClickableOpacity";
import { XLg } from "react-bootstrap-icons";
import { Product } from "../components/Product";
import "../style/App.css";
import { toast } from "react-toastify";

function Cashier() {
  const cashierVM = useCashierVM();

  const handleSubmit = async (event) => {
    event.preventDefault(); //evita di ricaricare la pagina
    if(!cashierVM.user)
      toast.error("You must select a user");
    else if(!cashierVM.product)
      toast.error("You must select a product");
    else if(!cashierVM.duration)
      toast.error("You must insert a duration");
    else if(!cashierVM.type)
      toast.error("You must select a typology");
    else{
      try {
        await cashierVM.createWarranty();
        cashierVM.reset();
        toast.success("Warranty created");
      } catch (err) {
        toast.error(err.message);
      }
    }

  }

  return (
    <>
      <h2 className="text-center">Create Warranty</h2>
      {cashierVM.user ? (
        <Row className="pt-4">
          <User //TODO: troppo margine a destra
            user={cashierVM.user}
            ActionElement={
              <ClickableOpacity onClick={cashierVM.removeUser}>
                <XLg color="black" />
              </ClickableOpacity>
            }
          />
        </Row>
      ) : (
        <SearchBar
          className={"pt-4"}
          placeholder="Search user email"
          onSearch={errorToaster(cashierVM.setUser)}
        />
      )}
      {cashierVM.product ? (
        <Row className="pt-4">
          <Product //TODO: troppo margine a destra
            product={cashierVM.product}
            ActionElement={
              <ClickableOpacity onClick={cashierVM.removeProduct}>
                <XLg color="black" />
              </ClickableOpacity>
            }
          />
        </Row>
      ) : (
        <SearchBar
          className="pt-4"
          placeholder="Search product ean"
          onSearch={errorToaster(cashierVM.setProduct)}
        />
      )}
      <Form onSubmit={handleSubmit}>
        <Row className="pt-4">
          <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
            <Form.Group controlId="duration">
              <Form.Control
                type="number"
                min={1}
                placeholder="Insert the warranty duration in years"
                value={cashierVM.duration}
                onChange={(ev) => cashierVM.setDuration(ev.target.value)}
              />
            </Form.Group>
          </Col>
          <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
            <Form.Group controlId="typology">
              <Form.Select
                value={cashierVM.type}
                onChange={(e) => cashierVM.setType(e.target.value)}
                style={cashierVM.type ? {} : { color: "GrayText" }}
              >
                <option style={{ color: "GrayText" }} value="">
                  Select the warranty typology
                </option>
                <option style={{ color: "black" }} value="BASE">
                  BASE
                </option>
                <option style={{ color: "black" }} value="CASCO">
                  CASCO
                </option>
              </Form.Select>
            </Form.Group>
          </Col>
        </Row>
        <Button className="mt-4" variant="success" size="lg" id="submitLogin" type="submit" style={{float:"right"}} onClick={handleSubmit}>
          Create Warranty
        </Button>
      </Form>
    </>
  );
}

export { Cashier };
