import { useEffect, useState } from "react";
import API from "../API";

export const useWorkersVM = (onError) => {
  const [workers, setWorkers] = useState([]);

  const set = async () => {
    try {
      const result = await API.getWorkersProfiles();
      setWorkers(result);
    } catch (err) {
      switch (err.status) {
        default:
          onError("Error while searching user");
      }
    }
  };

  useEffect(() => {
    set();
  }, []);

  return { workers };
};
