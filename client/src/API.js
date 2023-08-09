import axios from "axios";

axios.defaults.withCredentials = true;
const APIURL = new URL("http://localhost:3001/API/");

axios.interceptors.response.use(null, function (error) {
  // Any status codes that falls outside the range of 2xx cause this function to trigger
  // Do something with response error
  if (error.response?.status === 401) {
    
  }
  return Promise.reject(error);
});

/*
// Add a 401 response interceptor
window.axios.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    if (401 === error.response.status) {
        // handle error: inform user, go to login, etc
    } else {
        return Promise.reject(error);
    }
});
*/

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
    let response = await axios.get("API/products");
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

const API = { getProducts, getProductByEan, getProfileByEmail, createProfile, updateProfile }

export default API;