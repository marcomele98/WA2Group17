import React, { useEffect, useState } from "react";
import { Form, Row, Col } from "react-bootstrap";
import { useWorkersVM } from "../../presenters/WorkersVM";
import "../../style/App.css";
import { UserCard } from "../../components/UserCard";
import { toast } from "react-toastify";
import { ClickableOpacity } from "../../components/ClickableOpacity";
import { PlusSquareFill } from "react-bootstrap-icons";

export const Workers = () => {
  const [searchValue, setSearchValue] = useState("");
  const workersVM = useWorkersVM((err) =>
    toast.error(err, { position: "top-center" }, { toastId: 10 })
  );
  useEffect(() => {
    console.log(workersVM.workers);
  }, [workersVM.workers]);
  return (
    <>
      <Row style={{ margin: 0, padding: 0, marginBottom: 20 }}>
        <Col style={{ width: "100%" }}>
          <Form.Control
            size="lg"
            type="text"
            placeholder="Search"
            onChange={(ev) => setSearchValue(ev.target.value.toLowerCase())}
          />
        </Col>
        <Col xs="auto" sm="auto" md="auto" lg="auto" xl="auto" xxl="auto">
          <ClickableOpacity>
            <PlusSquareFill size={47} className="plus" fill="#198753" />
          </ClickableOpacity>
        </Col>
      </Row>
      {workersVM.workers
        .filter(
          (worker) =>
            worker.name.toLowerCase().startsWith(searchValue) ||
            worker.surname.toLowerCase().startsWith(searchValue) ||
            worker.email.toLowerCase().startsWith(searchValue)
        )
        .sort((a, b) =>
          a.email.toLowerCase() > b.email.toLowerCase() ? 1 : -1
        )
        .map((worker) => {
          return (
            <UserCard
              user={worker}
              ActionElement={
                <ClickableOpacity style={{ fontSize: 20 }}>
                  Edit
                </ClickableOpacity>
              }
              key={worker.email}
              withRole={true}
            />
          );
        })}
    </>
  );
};
