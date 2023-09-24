import "./style/App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import {useEffect, useState} from "react";
import {Row} from "react-bootstrap";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Navigate} from "react-router-dom";
import {NavigationBar} from "./components/Navbar";
import {ToastContainer} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {UserProvider} from "./presenters/LoggedUser";
import {Login} from "./screens/Login";
import {Unauthorized} from "./screens/Unauthorized";
import {AuthRoute} from "./components/AuthRoute";
import {Cashier} from "./screens/Cashier";
import {UnassignedTickets} from "./screens/manager/UnassignedTickets";
import {Workers} from "./screens/manager/Workers";
import {AssignTicket} from "./screens/manager/AssignTicket";
import {CreateWorker} from "./screens/manager/CreateWorker";
import {EditWorker} from "./screens/manager/EditWorker";
import {Signup} from "./screens/Signup";
import {CreateTicket} from "./screens/customer/CreateTicket";
import {Tickets} from "./screens/customer/Tickets";
import {Warranties} from "./screens/customer/Warranties";
import {Warranty} from "./screens/customer/Warranty";
import {Ticket} from "./screens/Ticket";
import {AssignedTickets} from "./screens/manager/AssignedTickets";

function App() {
    const [redirectRoute, setRedirectRoute] = useState(false);

    useEffect(() => {
        console.log("redirect route: ", redirectRoute);
    }, [redirectRoute]);

    return (
        <Router>
            <UserProvider>
                <div className="container-fluid">
                    <ToastContainer/>

                    <NavigationBar/>

                    <Row id="body-container">
                        <Routes>
                            <Route
                                index
                                path="/login"
                                element={
                                    <Login
                                        redirectRoute={redirectRoute}
                                        setRedirectRoute={setRedirectRoute}
                                    />
                                }
                            />
                            <Route path="/unauthorized" element={<Unauthorized/>}/>
                            {/* TUTTE LE ROUTE CASHIER, CON QUESTA ROUTE SONO GESTITE LE QUESTIONI PERMESSI */}
                            <Route
                                element={
                                    <AuthRoute
                                        role="CASHIER"
                                        setRedirectRoute={setRedirectRoute}
                                    />
                                }
                            >
                                <Route path="/cashier" element={<Cashier/>}/>
                            </Route>
                            <Route
                                element={
                                    <AuthRoute
                                        role="MANAGER"
                                        setRedirectRoute={setRedirectRoute}
                                    />
                                }
                            >
                                <Route path="/manager/*">
                                    <Route index element={<Navigate to="unassigned-tickets"/>}/>
                                    <Route
                                        path="unassigned-tickets"
                                        element={<UnassignedTickets/>}
                                    />
                                    <Route
                                        path="assigned-tickets"
                                        element={<AssignedTickets/>}
                                    />
                                    <Route path="users" element={<Workers/>}/>
                                    <Route path="create-user" element={<CreateWorker/>}/>
                                    <Route path="edit-user/:email" element={<EditWorker/>}/>
                                    <Route
                                        path="assign-ticket/:ticketId"
                                        element={<AssignTicket/>}
                                    />
                                </Route>
                            </Route>
                            <Route element={<AuthRoute role="CLIENT" setRedirectRoute={setRedirectRoute}/>}>
                                <Route path="/client/*">
                                    <Route index element={<Navigate to="warranties"/>}/>
                                    <Route path="warranties" element={<Warranties/>}/>
                                    <Route path="warranties/:id" element={<Warranty/>}/>
                                    <Route path="create-ticket/:warrantyId" element={<CreateTicket/>}/>
                                    <Route path="tickets" element={<Tickets/>}/>
                                    <Route path="tickets/:id" element={<Ticket/>}/>
                                </Route>
                            </Route> 
                            <Route element={<AuthRoute role="EXPERT" setRedirectRoute={setRedirectRoute}/>}>
                                <Route path="/expert/*">
                                    <Route path="tickets/:id" element={<Ticket/>}/>
                                </Route>
                            </Route>
                            <Route path="/signup" element={<Signup/>}/>
                            <Route path="/*" element={<Navigate to="/login"/>}/>
                        </Routes>
                    </Row>
                </div>
            </UserProvider>
        </Router>
    );
}

export default App;
