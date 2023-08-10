import React from "react";
import { Outlet, Route, useNavigate } from "react-router-dom";
import { useUser } from "../presenters/User";

export const AuthRoute = (props) => {

  const user = useUser();

  const navigate = useNavigate();

  const required_role = props.role;

  console.assert(required_role, "AuthRoute requires a role prop");

  if (!user) {
    navigate("/login");
  }
  
  if (user.role !== required_role) {
    navigate("/unauthorized");
  }

  return <Outlet/>;
};
