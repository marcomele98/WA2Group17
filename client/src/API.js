import axios from "axios";

axios.defaults.withCredentials = true;
const APIURL = new URL("http://localhost:8081/API/");

axios.interceptors.request.use(function (config) {
  // Do something before request is sent
  document.body.classList.add("loading-overlay");
  return config;
}, undefined);

axios.interceptors.response.use(
  function (response) {
    // Do something with response data
    document.body.classList.remove("loading-overlay");
    return response;
  },
  async function (error) {
    const originalRequest = error.config;
    let refreshTokenError, res;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      let url = new URL("refresh", APIURL);
      const params = new URLSearchParams();
      params.append("refreshToken", localStorage.getItem("refreshToken"));
      url.search = params.toString();
      console.log(url);
      [refreshTokenError, res] = await axios
        .post(url, undefined, undefined)
        .then(async (response) => {
          //handle success
          console.log("refreshed");
          localStorage.setItem("accessToken", response.data.accessToken);
          localStorage.setItem("refreshToken", response.data.refreshToken);
          error.config.headers[
            "Authorization"
          ] = `Bearer ${response.data.accessToken}`;
          return [null, await axios.request(originalRequest)];
        })
        .catch(function (error) {
          console.log("refresh failed");
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          //window.location.reload();
          console.log("refresh failed");
          if (error.response) {
            // Request made and server responded
            console.log(error.response.data);
          } else if (error.request) {
            // The request was made but no response was received
            console.log(error.request);
          } else {
            // Something happened in setting up the request that triggered an Error, 404 not found
            console.log("Error", error.message);
          }
        });
      if (refreshTokenError) {
        document.body.classList.remove("loading-overlay");
        return Promise.reject(refreshTokenError);
      }
      return Promise.resolve(res);
    }
    document.body.classList.remove("loading-overlay");
    return Promise.reject(error);
  }
  /*async function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    const refreshToken = localStorage.getItem("refreshToken");
    if (
      error.response?.status === 401 &&
      !error.config._retry &&
      refreshToken != null &&
      error.config.url !== new URL("login", APIURL) &&
      error.config.url !== new URL("refresh", APIURL)
    ) {
      console.log("refreshing");
      error.config._retry = true;
      console.log(1);
      let url = new URL("refresh", APIURL);
      console.log(1.5);
      const params = new URLSearchParams();
      console.log(1.75);
      params.append("refreshToken", refreshToken);
      console.log(1.8);
      url.search = params.toString();
      console.log(2 + url);
      try {
        const response = await axios //TODO: non funziona
          .post(url, undefined, undefined);
        console.log("refreshed");
        localStorage.setItem("accessToken", response.data.accessToken);
        localStorage.setItem("refreshToken", response.data.refreshToken);
        error.config.headers[
          "Authorization"
        ] = `Bearer ${response.data.accessToken}`;
        let res = await axios.request(error.config)
        return Promise.reject(res);
      } catch (err) {
        console.log("refresh failed");
      }
      // Non sono sicuro che sia giusto togliere il loading-overlay in questo punto (forse prima di fare la chiamata?)
      document.body.classList.remove("loading-overlay");
      return Promise.reject(error);
    }
  }*/
);

async function logIn(username, password) {
  // call: POST /api/sessions
  try {
    let response = await axios.post(new URL("login", APIURL), {
      username: username,
      password: password,
    });
    return response.data;
  } catch (err) {
    throw err;
  }
}

async function getProducts() {
  // call: GET /api/products
  try {
    let response = await axios.get(new URL("products", APIURL), {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
      },
    });
    return response.data;
  } catch (err) {
    throw err;
  }
}

async function getProductByEan(ean) {
  // call: GET /api/products/:ean
  try {
    let response = await axios.get("API/products/" + ean);
    return response.data;
  } catch (err) {
    throw err;
  }
}

async function getProfileByEmail(email) {
  // call: GET /api/profiles/:email
  try {
    let response = await axios.get("API/profiles/" + email);
    return response.data;
  } catch (err) {
    throw err;
  }
}

async function createProfile(profile) {
  // call: POST /api/profiles
  try {
    let response = await axios.post("API/profiles", profile);
    return response.data;
  } catch (err) {
    throw err;
  }
}

async function updateProfile(profile, email) {
  // call: PUT /api/profiles/:email
  try {
    let response = await axios.put("API/profiles/" + email, profile);
    return response.data;
  } catch (err) {
    throw err;
  }
}

const API = {
  getProducts,
  getProductByEan,
  getProfileByEmail,
  createProfile,
  updateProfile,
  logIn,
};

export default API;
