import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useState } from 'react';
import { Row } from 'react-bootstrap';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { Navigate } from 'react-router-dom';
import { NavigationBar } from './components/Navbar';
import { ToastContainer } from 'react-toastify';
import { Products } from './components/Products';
import { Product } from './components/Product';
import { Profiles } from './components/Profiles';
import 'react-toastify/dist/ReactToastify.css';
import { ProfileForm } from './components/ProfileForm';

function App() {

  const [isLoading, setIsLoading] = useState(false);


  return (
    <Router>
      <div className="container-fluid">
        <div className={isLoading ? "loading-overlay" : ""} />
        <ToastContainer />

        <NavigationBar />

        <Row id='body-container' >
          <Routes>
            <Route
              path="/products"
              element={<Products setIsLoading={setIsLoading} />}
            />

            <Route
              path="/products/:ean"
              element={<Product setIsLoading={setIsLoading} />}
            />

            <Route
              path="/profiles"
              element={<Profiles setIsLoading={setIsLoading} />}
            />

            <Route
              path="/create-profile"
              element={<ProfileForm setIsLoading={setIsLoading} />}
            />

            <Route
              path="/edit-profile/:userToEditEmail"
              element={<ProfileForm setIsLoading={setIsLoading} />}
            />

            <Route path="/*"
              element={<Navigate to="/products" />} />
            {" "}

          </Routes>
        </Row>

      </div>
    </Router>
  );
}

export default App;
