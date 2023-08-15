import { useEffect, useReducer } from "react";

export const emptyUser = {
  name: "",
  surname: "",
  email: "",
  role: "",
  skills: [],
};

export const userReducer = (state, action) => {
  switch (action.type) {
    case "SET":
      return action.payload;
    case "SET_NAME":
      return { ...state, name: action.payload };
    case "SET_SURNAME":
      return { ...state, surname: action.payload };
    case "SET_EMAIL":
      return { ...state, email: action.payload };
    case "SET_ROLE":
      return { ...state, role: action.payload };
    case "ADD_SKILL":
      return { ...state, skills: [...state.skills, action.payload] };
    case "REMOVE_SKILL":
      return {
        ...state,
        skills: state.skills.filter((skill) => skill !== action.payload),
      };
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
    }

    const setSurname = (surname) => {
        dispatch({ type: "SET_SURNAME", payload: surname });
    }

    const setEmail = (email) => {
        dispatch({ type: "SET_EMAIL", payload: email });
    }

    const setRole = (role) => {
        dispatch({ type: "SET_ROLE", payload: role });
    }

    const addSkill = (skill) => {
        dispatch({ type: "ADD_SKILL", payload: skill });
    }

    const removeSkill = (skill) => {
        dispatch({ type: "REMOVE_SKILL", payload: skill });
    }

    const set = (user) => {
        dispatch({ type: "SET", payload: user });
    }

    const reset = () => {
        dispatch({ type: "RESET" });
    }

    return {
        ...user,
        setName,
        setSurname,
        setEmail,
        setRole,
        addSkill,
        removeSkill,
        set,
        reset
    };
}

export const useCreateWorkerVM = () => {
    const user = useBaseUserVM();

    const save = async () => {
        try {
            await API.createWorker(user);
        } catch (err) {
            switch (err.status) {
                case 409:
                    throw "User already exists";
                default:
                    throw "Error while creating user";
            }
        }
    }

    return {
        ...user,
        save
    };
}

export const useUpdateWorkerVM = (email) => {
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
    }

    useEffect(() => {
        fetch();
    }, []);

    const save = async () => {
        try {
            await API.updateWorker(user, email);
        } catch (err) {
            switch (err.status) {
                case 409:
                    throw "User already exists";
                default:
                    throw "Error while creating user";
            }
        }
    }

    return {
        ...user,
        save
    };
}


export const useSignupVM = () => {
    const user = useBaseUserVM();

    const save = async () => {
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
    }

    return {
        name: user.name,
        surmane: user.surname,
        email: user.email,
        setName: user.setName,
        setSurname: user.setSurname,
        setEmail: user.setEmail,
        save
    };
}
