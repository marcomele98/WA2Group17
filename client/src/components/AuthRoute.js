import React, { useEffect } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { useUser } from "../presenters/User";

export const AuthRoute = ({role, setRedirectRoute}) => {
  const {user} = useUser();

  const navigate = useNavigate();

  const location = useLocation();

  const required_role = role;

  console.assert(required_role, "AuthRoute requires a role prop");

  useEffect(() => {
    if (user===null) {
      setRedirectRoute(location.pathname);
      console.log("redirect route: ", location.pathname);
      navigate("/login");
    }
    if (user && user.role !== required_role) {
      navigate("/unauthorized");
    }
  }, [user]);

  return <Outlet />;
};

