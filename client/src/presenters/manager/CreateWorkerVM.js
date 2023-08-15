import { userReducer, emptyUser } from "../UserVMs";

const useCreateWorkerVM = () => {
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

    const reset = () => {
        dispatch({ type: "RESET" });
    }

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
        setName,
        setSurname,
        setEmail,
        setRole,
        addSkill,
        removeSkill,
        reset
    };
};