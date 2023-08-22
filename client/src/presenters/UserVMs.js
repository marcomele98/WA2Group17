import { useEffect, useReducer } from "react";
import API from "../API";

export const emptyUser = {
  name: "",
  surname: "",
  email: "",
  role: "",
  password: "",
  skills: [],
};

export const userReducer = (state, action) => {
  switch (action.type) {
    case "SET":
      return action.payload;
    case "SET_NAME":
      return { ...state, name: action.payload.replace(/[^a-z ]/gi, "") };
    case "SET_SURNAME":
      return { ...state, surname: action.payload.replace(/[^a-z ]/gi, "") };
    case "SET_EMAIL":
      return { ...state, email: action.payload.replace(/[^a-z0-9@.]/gi, "") };
    case "SET_ROLE":
      return { ...state, role: action.payload };
    case "ADD_SKILL":
      return { ...state, skills: [...state.skills, action.payload] };
    case "REMOVE_SKILL":
      return {
        ...state,
        skills: state.skills.filter((skill) => skill !== action.payload),
      };
    case "SET_PASSWORD":
      return { ...state, password: action.payload };
    case "RESET":
      return emptyUser;
    default:
      return state;
  }
};

const useBaseUserVM = () => {
  const [user, dispatch] = useReducer(userReducer, emptyUser);

  const setName = (name) => {
    dispatch({ type: "SET_NAME", payload: name });
  };

  const setSurname = (surname) => {
    dispatch({ type: "SET_SURNAME", payload: surname });
  };

  const setEmail = (email) => {
    dispatch({ type: "SET_EMAIL", payload: email });
  };

  const setRole = (role) => {
    if (role !== "EXPERT") {
      for (let skill of user.skills) {
        dispatch({ type: "REMOVE_SKILL", payload: skill });
      }
    }
    dispatch({ type: "SET_ROLE", payload: role });
  };

  const addSkill = (skill) => {
    dispatch({ type: "ADD_SKILL", payload: skill });
  };

  const removeSkill = (skill) => {
    dispatch({ type: "REMOVE_SKILL", payload: skill });
  };

  const setPassword = (password) => {
    dispatch({ type: "SET_PASSWORD", payload: password });
  };

  const set = (user) => {
    dispatch({ type: "SET", payload: user });
  };

  const reset = () => {
    dispatch({ type: "RESET" });
  };

  return {
    ...user,
    setName,
    setSurname,
    setEmail,
    setRole,
    addSkill,
    removeSkill,
    setPassword,
    set,
    reset,
  };
};

export const useCreateWorkerVM = () => {
  const user = useBaseUserVM();

  const save = async () => {
    if (user.email === "") throw "Enter an email";
    if (user.name === "") throw "Enter a name";
    if (user.surname === "") throw "Enter a surname";
    if (user.role == "EXPERT" && user.skills.length === 0)
      throw "Enter at least one skill";
    if (user.role == "") throw "Select a role";
    if (!/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(user.email))
      throw "Enter a valid email";
    if (
      !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=]).*$/.test(user.password)
    )
      throw "Password must contain at least one lowercase letter, one uppercase letter, one number and one special character";
    try {
      await API.createWorker(user);
    } catch (err) {
      console.log(err);
      switch (err.status) {
        case 409:
          throw "User already exists";
        default:
          throw "Error while creating user";
      }
    }
  };

  return {
    ...user,
    save,
  };
};

export const useEditWorkerVM = (email) => {
  const user = useBaseUserVM();

  const fetch = async () => {
    try {
      const response = await API.getWorkerByEmail(email);
      user.set(response);
    } catch (err) {
      switch (err.status) {
        case 404:
          throw "User not found";
        default:
          throw "Error while searching user";
      }
    }
  };

  useEffect(() => {
    fetch();
  }, []);

  const save = async () => {
    if (user.role == "EXPERT" && user.skills.length === 0)
      throw "Enter at least one skill";
    if (user.role == "") throw "Select a role";
    try {
      await API.updateWorker(user.email, {
        name: user.name,
        surname: user.surname,
        role: user.role,
        password: user.password ? user.password : undefined,
        skills: user.skills,
      });
    } catch (err) {
      switch (err.status) {
        case 400:
          throw err.detail;
        case 409:
          throw "User already exists";
        case 404:
          throw "User not found";
        case 422:
          throw err.detail;
        default:
          throw "Error while creating user";
      }
    }
  };

  return {
    ...user,
    save,
  };
};

export const useSignupVM = () => {
  const user = useBaseUserVM();

  const save = async () => {
    if (user.email === "") throw "Enter an email";
    if (user.name === "") throw "Enter a name";
    if (user.surname === "") throw "Enter a surname";
    if (
      !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=]).*$/.test(user.password)
    )
      throw "Password must contain at least one lowercase letter, one uppercase letter, one number and one special character";
    try {
      await API.createProfile(user);
    } catch (err) {
      switch (err.status) {
        case 409:
          throw "User already exists";
        default:
          throw "Error while creating user";
      }
    }
  };

  return {
    name: user.name,
    surmane: user.surname,
    email: user.email,
    password: user.password,
    setName: user.setName,
    setSurname: user.setSurname,
    setEmail: user.setEmail,
    setPassword: user.setPassword,
    save,
  };
};
