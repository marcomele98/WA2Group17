import React, { createContext, useContext, useEffect, useReducer } from "react";
import API from "../API";
import jwtDecode from "jwt-decode";

const UserContext = createContext(undefined);

const CREATE = 0;
const DELETE = 1;


const reducer = (state, action) => {
  switch (action.type) {
    case CREATE:
      try {
        let decoded = jwtDecode(localStorage.getItem("accessToken"));
        return {
          role: decoded.resource_access["wa2g17-keycloak-client"].roles[0],
          email: decoded.email,
          surname: decoded.family_name,
          name: decoded.given_name,
          username: decoded.preferred_username,
          skills: decoded.skills,
        };
      } catch (err) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        return undefined;
      }
    case DELETE:
      return undefined;
    default:
      return state;
  }
};

export const UserProvider = ({ children }) => {
  const [user, dispatch] = useReducer(reducer, null);

  const refresh = async () => {
    let refreshToken = localStorage.getItem("refreshToken");
    
    try {
      jwtDecode(localStorage.getItem("accessToken"));
  
    } 
    catch (err) {
      await API.refreshToken();
    }

    if (refreshToken && !user) {
      try {
        let refreshInfo = jwtDecode(refreshToken);
        if (refreshInfo.exp * 1000 > Date.now()) {
          dispatch({ type: CREATE });
        } else {
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
        }
      } catch (err) {
        console.log(err);
      }
    }
  }


  useEffect(() => {
   refresh();
  }, []);

  const logIn = async (email, password) => {
    try {
      let response = await API.logIn(email, password);
      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);
      dispatch({ type: CREATE });
    } catch (err) {
      throw err;
    }
  };

  const logOut = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    dispatch({ type: DELETE });
  };

  return (
    <UserContext.Provider value={{ user, logIn, logOut }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => useContext(UserContext);
