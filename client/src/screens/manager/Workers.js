import React, { useEffect, useState } from "react";
import { Form, Row, Col } from "react-bootstrap";
import { useWorkersVM } from "../../presenters/WorkersVM";
import "../../style/App.css";
import { UserCard } from "../../components/UserCard";
import { toast } from "react-toastify";
import { ClickableOpacity } from "../../components/ClickableOpacity";
import { PlusSquareFill, Trash, Pencil } from "react-bootstrap-icons";
import { useNavigate } from "react-router-dom";
import {errorToast, errorToaster} from "../../utils/Error";
import { ConfirmDialog } from "../../components/ConfirmationModal";

export const Workers = () => {
  const [searchValue, setSearchValue] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [workerToDelete, setWorkerToDelete] = useState(null);

  const navigate = useNavigate();
  const workersVM = useWorkersVM((err) =>
    errorToast(err)
  );
  
  return (
    <>
      <ConfirmDialog
        title="Delete worker"
        text={`Are you sure you want to delete ${workerToDelete?.email} profile?`}
        show={showModal}
        onClose={() => setShowModal(false)}
        onConfirm={errorToaster(() => {
          workersVM.deleteWorker(workerToDelete?.email);
          setShowModal(false);
        })}
      />
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
            <PlusSquareFill
              size={47}
              className="text-primary"
              onClick={() => navigate("/manager/create-user")}
            />
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
                <div>
                  <ClickableOpacity
                    style={{ marginRight: 5 }}
                    onClick={() =>
                      navigate("/manager/edit-user/" + worker.email)
                    }
                  >
                    <Pencil size={25} className="text-primary" />
                  </ClickableOpacity>
                  <ClickableOpacity
                    onClick={() => {
                      setWorkerToDelete(worker);
                      setShowModal(true);
                    }}
                  >
                    <Trash size={30} className="text-danger" />
                  </ClickableOpacity>
                </div>
              }
              key={worker.email}
              withRole={true}
            />
          );
        })}
    </>
  );
};
