import axios from "axios";

axios.defaults.withCredentials = true;
const APIURL = new URL("http://localhost:8081/API/");

axios.interceptors.request.use(function (config) {
  // Do something before request is sent
  document.body.classList.add('loading-overlay');
  return config;
}, undefined);

axios.interceptors.response.use(
  function (response) {
    // Do something with response data
    document.body.classList.remove('loading-overlay');
    return response;
  }, function (error) {
  // Any status codes that falls outside the range of 2xx cause this function to trigger
  console.log(error);
  if (error.response?.status === 401) {
    const refreshToken = localStorage.getItem("refreshToken");
    if (refreshToken) {
      console.log(new URL("refresh", APIURL).toString());
      let url = new URL("refresh", APIURL)
      url.set("refreshToken", refreshToken)
      axios //TODO: non funziona
        .post(new URL("refresh", APIURL))
        .then((response) => {
          console.log("refreshed");
          localStorage.setItem("accessToken", response.data.accessToken);
          localStorage.setItem("refreshToken", response.data.refreshToken);
          error.config.headers["Authorization"] = `Bearer ${response.data.accessToken}`;
          return axios.request(error.config);
        })
        .catch((err) => {
          console.log("refresh error " + err);
        });
        
    }
  }
  // Non sono sicuro che sia giusto togliere il loading-overlay in questo punto (forse prima di fare la chiamata?)
  document.body.classList.remove('loading-overlay');
  return Promise.reject(error);
  
});

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
      }
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
