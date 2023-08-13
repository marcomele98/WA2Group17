import { toast } from "react-toastify";

let id = 0;

export const errorToaster = (action) => {
  return async (...params) => {
    try {
      await action(...params);
    } catch (error) {
      toast.error(error, { position: "top-center" }, { toastId: id++ }); // Mostra il toast con il messaggio di errore
    }
  };
};
