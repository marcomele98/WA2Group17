import "./style/App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { useState } from "react";
import { Row } from "react-bootstrap";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Navigate } from "react-router-dom";
import { NavigationBar } from "./components/Navbar";
import { ToastContainer } from "react-toastify";
import { Products } from "./components/Products";
import { Product } from "./components/Product";
import { Profiles } from "./components/Profiles";
import "react-toastify/dist/ReactToastify.css";
import { UserProvider } from "./presenters/User";
import { Login } from "./screens/Login";
import { Unauthorized } from "./screens/Unauthorized";
import { AuthRoute } from "./components/AuthRoute";

function App() {

  return (
    <Router>
      <UserProvider>
        <div className="container-fluid">
          <ToastContainer />

          <NavigationBar />

          <Row id="body-container">
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/unauthorized" element={<Unauthorized />} />
              {/* TUTTE LE ROUTE CASHIER, CON QUESTA ROUTE SONO GESTITE LE QUESTIONI PERMESSI */}
              <Route element={<AuthRoute role="CASHIER" />}>
                <Route path="/cashier/*">
                  {/*TODO: capisco come impostare la schermata cashier*/}
                  <Route
                    index
                    element={<Products/>}
                  />
                  <Route
                    path="products/:ean"
                    element={<Product/>}
                  />
                  <Route
                    path="profiles"
                    element={<Profiles/>}
                  />
                </Route>
              </Route>
              {/*<Route
                path="/manager/profiles"
                element={<Profiles setIsLoading={setIsLoading} />}
              />
              <Route
                path="/manager/create-profile"
                element={<ProfileForm setIsLoading={setIsLoading} />}
              />
              <Route
                path="/edit-profile/:userToEditEmail"
                element={<ProfileForm setIsLoading={setIsLoading} />}
              />*/}
              <Route path="/*" element={<Navigate to="/login" />} />{" "}
            </Routes>
          </Row>
        </div>
      </UserProvider>
    </Router>
  );
}

export default App;
