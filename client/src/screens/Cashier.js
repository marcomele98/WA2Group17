import React from "react";
import { useCashierVM } from "../presenters/CashierVM";
import { Button, Col, Form } from "react-bootstrap";
import { SearchBar } from "../components/SearchBar";
import { Row } from "react-bootstrap";
import {errorToast, errorToaster, successToast} from "../utils/Error";
import { UserCard } from "../components/UserCard";
import { ClickableOpacity } from "../components/ClickableOpacity";
import { XLg } from "react-bootstrap-icons";
import { ProductCard } from "../components/ProductCard";
import "../style/App.css";
import { toast } from "react-toastify";

function Cashier() {
  const cashierVM = useCashierVM();

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!cashierVM.duration) errorToast("You must insert a duration");
    else if (!cashierVM.type) errorToast("You must select a typology");
    else {
      try {
        await cashierVM.createWarranty();
        cashierVM.reset();
        successToast("Warranty created");
      } catch (err) {
        errorToast(err.message);
      }
    }
  };

  return (
    <>
      <h2 className="text-center">Create Warranty</h2>
      {!cashierVM.user ? (
        <SearchBar
          size="lg"
          className="pt-4"
          placeholder="Search user email"
          type="email"
          onSearch={errorToaster(cashierVM.setUser)}
        />
      ) : (
        <>
          <UserCard
            className="pt-4"
            user={cashierVM.user}
            ActionElement={
              <ClickableOpacity onClick={cashierVM.removeUser}>
                <XLg color="black" />
              </ClickableOpacity>
            }
          />
          {!cashierVM.product ? (
            <SearchBar
              size="lg"
              className="pt-4"
              placeholder="Search product ean"
              type="text"
              maxLength={13}
              minLength={13}
              onSearch={errorToaster(cashierVM.setProduct)}
            />
          ) : (
            <>
              <ProductCard
                product={cashierVM.product}
                className="pt-4"
                ActionElement={
                  <ClickableOpacity onClick={cashierVM.removeProduct}>
                    <XLg color="black" />
                  </ClickableOpacity>
                }
              />
              <Form onSubmit={handleSubmit}>
                <Row className="pt-4">
                  <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
                    <Form.Group controlId="typology">
                      <Form.Select
                        size="lg"
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
                  <Col xs={12} sm={12} md={6} lg={6} xl={6} xxl={6}>
                    <Form.Group controlId="duration">
                      <Form.Control
                        size="lg"
                        type="number"
                        min={1}
                        placeholder="Insert the warranty duration in years"
                        value={cashierVM.duration}
                        onChange={(ev) =>
                          cashierVM.setDuration(ev.target.value)
                        }
                      />
                    </Form.Group>
                  </Col>
                </Row>
                <Button
                  className="mt-4"
                  variant="primary"
                  size="lg"
                  id="submitLogin"
                  type="submit"
                  disabled={!(cashierVM.type && cashierVM.duration)}
                  style={{ float: "right" }}
                  onClick={handleSubmit}
                >
                  Create Warranty
                </Button>
              </Form>
            </>
          )}
        </>
      )}
    </>
  );
}

export { Cashier };
