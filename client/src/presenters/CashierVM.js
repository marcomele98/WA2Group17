import { useReducer } from "react";
import API from "../API";

const reducer = (state, action) => {
  switch (action.type) {
    case "SET_PRODUCT":
      return { ...state, product: action.payload };
    case "REMOVE_PRODUCT":
      return { ...state, product: null };
    case "SET_USER":
      return { ...state, user: action.payload };
    case "REMOVE_USER":
      return { ...state, user: null };
    case "SET_DURATION":
      return { ...state, duration: action.payload };
    case "SET_TYPE":
      return { ...state, type: action.payload };
    case "RESET":
      return { user: null, product: null, duration: null, type: null };
    default:
      return state;
  }
};

export const useCashierVM = () => {
  const [state, dispatch] = useReducer(reducer, {
    user: null,
    product: null,
    duration: null,
    type: null,
  });

  const setProduct = async (ean) => {
    try {
      const product = await API.getProductByEan(ean);
      dispatch({ type: "SET_PRODUCT", payload: product });
    } catch (err) {
      switch(err.status){
        case 404:
          throw "Product not found";
        default:
          throw "Error while searching product";         
      }
      //TODO: gestisco altri errori
    }
  };

  const removeProduct = () => {
    dispatch({ type: "REMOVE_PRODUCT" });
  };

  const setUser = async (email) => {
    try {
      console.log(1);
      const user = await API.getCustomerByEmail(email);
      dispatch({ type: "SET_USER", payload: user });
    } catch (err) {
      switch(err.status){
        case 404:
          throw "User not found";
        default:
          throw "Error while searching user";
          
      }
    }
  };

  const removeUser = () => {
    dispatch({ type: "REMOVE_USER" });
  };

  const setDuration = (duration) => {
    dispatch({ type: "SET_DURATION", payload: duration });
  };

  const setType = (type) => {
    dispatch({ type: "SET_TYPE", payload: type });
  };

  const reset = () => {
    dispatch({ type: "RESET" });
  };

  const save = () => {
    try {
      API.createWarranty({
        productEan: state.product.ean,
        durationYears: state.duration,
        typology: state.type,
        customerEmail: state.user.email,
      });
    } catch (err) {
      //TODO: gestire errori
      console.log(err);
    }
  };

  return {
    ...state,
    createWarranty: save,
    setProduct,
    removeProduct,
    setUser,
    removeUser,
    setDuration,
    setType,
    reset
  };
};
